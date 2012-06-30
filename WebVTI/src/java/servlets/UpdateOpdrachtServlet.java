/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import utils.Util;
import database.DatabaseManager;
import database.OpdrachtDB;
import domain.Opdracht;
import domain.OpdrachtType;
import domain.enums.OpdrachtStatus;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author soezen
 */
public class UpdateOpdrachtServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.print(request.getAttribute("output"));
        } finally { 
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String action = request.getParameter("action");
        String output = "F";
        if ("updateStatus".equals(action)) {
            String oldStatusCode = request.getParameter("oldStatus");
            String newStatusCode = request.getParameter("newStatus");
            String comment = request.getParameter("comments");
            System.out.println("comment: " + comment + " - " + request.getAttribute("comments"));
            
            try {
                OpdrachtStatus oldStatus = OpdrachtStatus.valueOf(Integer.valueOf(oldStatusCode));
                OpdrachtStatus newStatus = OpdrachtStatus.valueOf(Integer.valueOf(newStatusCode));
                if(oldStatus.nextAllowed(newStatus) 
                        && ((newStatus.isCommentRequired() && !(comment == null || "".equals(comment)))) 
                        || !newStatus.isCommentRequired()) {
                    String opdrachtId = request.getParameter("opdrachtId");
                    OpdrachtDB odb = new OpdrachtDB();
                    Opdracht opdracht = odb.getWithId(Long.valueOf(opdrachtId));
                    // TODO check if user has rights
                    if (opdracht != null) {
                        opdracht.setStatus(newStatus);
                        opdracht.appendCommentaar(comment);
                        odb.update(opdracht);
                        output = "S";
                    }
                }
                
                
            } catch (NumberFormatException e) {
                // invalid status code
            }
            
        } 
        request.setAttribute("output", output);
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
