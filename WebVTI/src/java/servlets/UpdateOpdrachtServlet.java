/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.OpdrachtDB;
import domain.Opdracht;
import domain.enums.OpdrachtStatus;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author soezen
 */
public class UpdateOpdrachtServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
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
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String output = "F";
        String action = request.getParameter("action");
        String oldStatusCode = request.getParameter("oldStatus");
        String newStatusCode = request.getParameter("newStatus");
        String comment = request.getParameter("comments");
        String opdrachtId = request.getParameter("opdrachtId");

        // TODO process request to map of keys and values (regardless of what contenttype
        if (request.getContentType().contains("multipart/form-data")) {
            try {
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator it = upload.getItemIterator(request);
                while (it.hasNext()) {
                    FileItemStream item = it.next();
                    if (item.isFormField()) {
                        String input = "";
                        try {
                            InputStream stream = item.openStream();
                            StringWriter writer = new StringWriter();

                            while (stream.available() > 0) {
                                writer.write(stream.read());
                            }
                            input = writer.toString();

                        } catch (IOException ex) {
                            Logger.getLogger(InputOpdrachtServlet.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if ("oldStatus".equals(item.getFieldName())) {
                            oldStatusCode = input;
                        } else if ("newStatus".equals(item.getFieldName())) {
                            newStatusCode = input;
                        } else if ("comments".equals(item.getFieldName())) {
                            comment = input;
                        } else if ("action".equals(item.getFieldName())) {
                            action = input;
                        } else if ("opdrachtId".equals(item.getFieldName())) {
                            opdrachtId = input;
                        }
                    }
                }
            } catch (FileUploadException ex) {
                Logger.getLogger(UpdateOpdrachtServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if ("updateStatus".equals(action)) {
            try {
                OpdrachtStatus oldStatus = OpdrachtStatus.valueOf(Integer.valueOf(oldStatusCode));
                OpdrachtStatus newStatus = OpdrachtStatus.valueOf(Integer.valueOf(newStatusCode));
                if (oldStatus.nextAllowed(newStatus)
                        && ((newStatus.isCommentRequired() && !(comment == null || "".equals(comment))))
                        || !newStatus.isCommentRequired()) {
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
     * Handles the HTTP
     * <code>GET</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
