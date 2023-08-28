package com.fouriApps.view.servlet;

import com.octetstring.vde.util.Base64;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Map;

import javax.naming.NamingException;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.adf.share.ADFContext;

import org.json.JSONException;
import org.json.JSONObject;

public class DashBoardServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String TASK_FLOW_ID ="faces/fragments/Reports.jspx";
//    private static final String TASK_FLOW_ID ="faces/FilmStrip";
//    private static final String TASK_FLOW_ID ="faces/adf.task-flow?adf.tfId=Reports_BTF&adf.tfDoc=/taskFlow/Reports_BTF.xml&";
    private static final String InValidTASK_FLOW_ID ="faces/pages/InvalidPage.jspx";

    //continue anna

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    Map<Object, Object> sessionMap = ADFContext.getCurrent().getSessionScope();
    
    /**Process the HTTP doGet request.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = null;
        String reDirect="Error";
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>DashBoard Servlet</title></head>");
        out.println("<body>");
        //---
        String url = request.getRequestURL().toString();
        String subUrl =url.substring(0, url.lastIndexOf("/"));
//        String jwtToken = request.getParameter("jwt");
        String jwtToken="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsIng1dCI6IjhzQkZrd3J4Mk00cXRnNHo1V19lV3E2c256QSIsImtpZCI6InRydXN0c2VydmljZSJ9.eyJleHAiOjE1ODUxMTkxMTAsInN1YiI6IkxFa3BvIiwiaXNzIjoid3d3Lm9yYWNsZS5jb20iLCJwcm4iOiJMRWtwbyIsImlhdCI6MTU4NTExOTA1MH0.f4v61c1YJLT_aArTKzlaG_ebOMF1Slx_L0JVjSboh33xVWUFNe3Hf2288o5m1s5wcib1y_vBi-jAUoTJa6UEHXHZlCfKvBAOw1-zMPsBBfENxjO7HyiSXGIdD-iRAOOm2sCmfFGpZOwGJvyamrquadRRYDbrQ_JavSn9Y4T0hGdNI_dVeTv7BMqbUfnq1g0z30xWFQgfX8xMlBYxd--PgVLs4Rc9deooQgwu2evfP6EGaczVk_EgSf8eA9EqJi6yX0b1MegyRnsfS8Wi9BiBZRfJ8uuNxBC8HcmkJJ3vzapsSwJ4XnlQXDBpT5YSqkm1tgOzzJGtwHOBovSazKK2nA";
        HttpSession session = request.getSession();
        if(jwtToken!=null){
            try{
                reDirect = checkLoginUserRole(jwtToken,session);
//                System.err.println("reDirect -->"+reDirect);
                if(reDirect.equalsIgnoreCase("invalidUser")){
                    path = subUrl + "/" + InValidTASK_FLOW_ID +"?errorType="+reDirect;
                }else{
                    path = subUrl + "/" + TASK_FLOW_ID;

                }
               System.err.println("path"+path);
            }catch(Exception e){
              
              
            }
        }else{
            try {
            reDirect = "invalidUser";
//             checkLoginUserRole("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsIng1dCI6IjhzQkZrd3J4Mk00cXRnNHo1V19lV3E2c256QSIsImtpZCI6InRydXN0c2VydmljZSJ9.eyJleHAiOjE1ODUxMTkxMTAsInN1YiI6IkxFa3BvIiwiaXNzIjoid3d3Lm9yYWNsZS5jb20iLCJwcm4iOiJMRWtwbyIsImlhdCI6MTU4NTExOTA1MH0.f4v61c1YJLT_aArTKzlaG_ebOMF1Slx_L0JVjSboh33xVWUFNe3Hf2288o5m1s5wcib1y_vBi-jAUoTJa6UEHXHZlCfKvBAOw1-zMPsBBfENxjO7HyiSXGIdD-iRAOOm2sCmfFGpZOwGJvyamrquadRRYDbrQ_JavSn9Y4T0hGdNI_dVeTv7BMqbUfnq1g0z30xWFQgfX8xMlBYxd--PgVLs4Rc9deooQgwu2evfP6EGaczVk_EgSf8eA9EqJi6yX0b1MegyRnsfS8Wi9BiBZRfJ8uuNxBC8HcmkJJ3vzapsSwJ4XnlQXDBpT5YSqkm1tgOzzJGtwHOBovSazKK2nA", session);   
         
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if(reDirect.equalsIgnoreCase("invalidUser")){
                path = subUrl + "/" + InValidTASK_FLOW_ID +"?errorType="+reDirect;
            }else{
                path = subUrl + "/" + TASK_FLOW_ID;    
            }
            
        }
        //---   
        System.err.println("=="+path);
        response.sendRedirect(path);
        out.println("<p>Redirecting to Dashboard taskflow</p>");
        out.println("</body></html>");
        out.close();
    }
    
    
    public String checkLoginUserRole(String jwt,HttpSession session) throws SQLException, JSONException, NamingException {
            
            //        ADFContext.getCurrent().getPageFlowScope().put("jwt",jwt);
            String pageRedirect = "invalid";
        
            String userRole = null;
            if (jwt != null) {
                //        System.err.println("==>in");
                String inputEncodedText = jwt;
                //ADFContext.getCurrent().getPageFlowScope().get("tokens").toString();
                String[] xIn = inputEncodedText.split("\\.");
                byte b[] = Base64.decode(xIn[1]);
                System.err.println("b===="+b);
                String tempPass = new String(b);
                
                int chkOccurance = tempPass.lastIndexOf("}");
                if (chkOccurance < 1) {
                    tempPass += "}";
                }
                JSONObject jo;

                jo = new JSONObject(tempPass);
//                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//                Timestamp expFromToken = new Timestamp(jo.getLong("exp"));

                //comment the below if statement for local
                //                if(timestamp.compareTo(expFromToken)>0){
                //                    return "tokenExpired";
                //                }
                //till this you have to comment
                String userName ="LEkpo";
//                String userName = jo.getString("prn");
//                ADFContext.getCurrent().getSessionScope().put("userName", jo.getString("prn"));
                ADFContext.getCurrent().getSessionScope().put("userName", userName);
//                            String userName ="LEkpo";

                session.setAttribute("userName", userName);
                  System.err.println("PRN==>"+userName);
                //                userRole=getDBConnection(userName);
                //                if(userRole.equalsIgnoreCase("NO_ROLE")){
                //                    pageRedirect = "invalidUser";
                //                }else{
                pageRedirect = "validUser";
                //                }
            } else {
                pageRedirect = "invalidUser";
            }
            return pageRedirect;
        }
    

    public String getDBConnection(String USER_NAME, HttpSession session) throws NamingException,
                                                           SQLException {
            String retStr = "validUser";
            return retStr;              
            
        }
    

}
