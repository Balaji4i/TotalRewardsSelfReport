package com.fouriApps.view.backing;

import com.fouriApps.view.utils.ADFUtils;
import com.fouriApps.view.utils.JSFUtils;

import com.octetstring.vde.util.Base64;


import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.event.PhaseId;
import oracle.adf.controller.v2.lifecycle.Lifecycle;
import oracle.adf.controller.v2.lifecycle.PagePhaseEvent;
import oracle.adf.controller.v2.lifecycle.PagePhaseListener;
import oracle.adf.model.BindingContext;
import oracle.adf.model.OperationBinding;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import org.json.JSONObject;

import weblogic.servlet.cluster.SessionState;

public class PhaseListener implements PagePhaseListener {
    
    java.util.Map sessionMap = ADFContext.getCurrent().getSessionScope();
    private String jwt;
    public PhaseListener() {
        super();
    }
   

    public void afterPhase(PagePhaseEvent phaseEvent) {
    }

    public void beforePhase(PagePhaseEvent phaseEvent) {
        if (phaseEvent.getPhaseId() == Lifecycle.PREPARE_RENDER_ID) {
            onPageLoad();
        }
    }

    public PhaseId getPhaseId() {
        return null;
    }
    public void onPageLoad(){
        if (!AdfFacesContext.getCurrentInstance().isPostback()) {
          String jwt=JSFUtils.resolveExpression("#{pageFlowScope.token}")==null?null:JSFUtils.resolveExpression("#{pageFlowScope.token}").toString();
          System.err.println("jwt-->"+jwt);
        // Sample Token
//        jwt= "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsIng1dCI6IlRBc0pMVXY0MFVuaWRJclVrRGFwRzhFXzlLOCJ9.eyJleHAiOjE0ODM1MzA3NzEsImlzcyI6Ind3dy5vcmFjbGUuY29tIiwicHJuIjoiYXBpIiwiaWF0IjoxNDgzNTE2MzcxfQ.ALvDilyGj-VQUmRQnUc7L1tz895bxjiSfPetczwqbUhMTmBIIoyJd9tKFQTnYPg8esUiiym8RnXRisFXcWmdmcoYAg3bbhqQ877KBDdXg6_CAk5h4OSHG8jgXhWFSJsE-";
        String userRole = null;
        if (jwt != null) {
            System.err.println("==>with token");
            String inputEncodedText = jwt;
            try {
                String[] xIn = inputEncodedText.split("\\.");
                byte b[] = Base64.decode(xIn[1]);
                String tempPass = new String(b);
                int chkOccurance = tempPass.lastIndexOf("}");
                if (chkOccurance < 1) {
                    tempPass += "}";
                }
                JSONObject jo;

                jo = new JSONObject(tempPass);
                System.err.println("PRN==>"+jo.getString("prn"));
                ADFContext.getCurrent().getSessionScope().put("userName", jo.getString("prn"));
                ADFContext.getCurrent().getPageFlowScope().put("redirect","Scheduler");
            } catch (Exception e) {
                ADFContext.getCurrent().getPageFlowScope().put("redirect","Error");
            }
        } else {
            System.err.println("Token Null");
            ADFContext.getCurrent().getPageFlowScope().put("redirect","Error");
            }
          System.err.println("Redirect Pageflow==>"+ ADFContext.getCurrent().getPageFlowScope().get("redirect"));    
    }
}

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}


