/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.GebruikerDB;
import database.OpdrachtDB;
import database.OpdrachtTypeDB;
import database.OptieDB;
import decorators.OpdrachtDecorator;
import domain.*;
import domain.enums.OpdrachtStatus;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utils.Validator;

/**
 *
 * @author soezen
 */
public class InputOpdrachtServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            HashMap<String, String> errors = (HashMap<String, String>) request.getAttribute("errors");
            if (errors == null) {
                out.write("jsp/detailsOpdracht.jsp?id=" + request.getAttribute("result"));
            } else {
                StringBuilder sb = new StringBuilder();
                for (Entry<String, String> entry : errors.entrySet()) {
                    sb.append("<li>").append(entry.getValue()).append("</li>");
                }
                response.setContentType("text/html");
                out.write(sb.toString());
            }
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OpdrachtDecorator decorator = (OpdrachtDecorator) request.getSession().getAttribute("opdracht");
        Gebruiker gebruiker = (Gebruiker) request.getSession().getAttribute("gebruiker");
        if (decorator != null) {
            OpdrachtDB db = new OpdrachtDB();
            OpdrachtTypeDB otdb = new OpdrachtTypeDB();
            Opdracht opdracht = new Opdracht();
            opdracht.setAanmaakDatum(new Date());
            opdracht.setStatus(OpdrachtStatus.AANGEVRAAGD);
            opdracht.setOpdrachtgever(gebruiker);
            OpdrachtType ot = otdb.getWithId(decorator.opdrachtType);
            opdracht.setOpdrachtType(ot);
            List<OpdrachtTypeInput> velden = db.getInputVelden(ot, gebruiker);
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload handler = new ServletFileUpload(factory);
            try {
                List items = handler.parseRequest(request);
                setData(opdracht, velden, items);
            } catch (FileUploadException e) {
                // process errors and report to user
                e.printStackTrace();
                HashMap<String, String> errors = new HashMap<String, String>();
                errors.put("Bestand", "Laden van het bestand is niet gelukt");
                request.setAttribute("errors", errors);
                processRequest(request, response);
                return;
            }

            HashMap<String, String> errors = Validator.getErrors(opdracht);
            if (errors.isEmpty()) {
                opdracht = db.persist(opdracht);
                request.setAttribute("result", opdracht.getId());
            } else {
                request.setAttribute("errors", errors);
            }
        }
        processRequest(request, response);
    }

    private OpdrachtTypeInput getField(List<OpdrachtTypeInput> velden, FileItem item) {
        String naam = item.getFieldName();
        for (OpdrachtTypeInput oti : velden) {
            if (oti.getInputVeldInt() == Integer.valueOf(naam)) {
                return oti;
            }
        }
        return null;
    }

    private void putDataInOpdracht(OpdrachtTypeInput veld, FileItem item, Opdracht opdracht) {
        GebruikerDB gdb = new GebruikerDB();
        String input = item.getString();
        switch (veld.getInputVeld().getType()) {
            case VAST:
                String naam = veld.getInputVeld().getNaam();
                if (naam.equals("Opdrachtgever")) {
                    Gebruiker opdrachtgever = gdb.getWithId(Long.valueOf(input));
                    opdracht.setOpdrachtgever(opdrachtgever);
                } else if (naam.equals("Bestand")) {
                    if (!item.isFormField()) {
                        // TODO get unique name by using opdrachtid
                        // TODO output folder in property file steken zodat het makkelijk te wijzigen valt
                        File file = new File("d:/upload", item.getName());
                        try {
                            // TODO uncomment this when deploying
                            //     item.write(file);
                            opdracht.setBestand("d:/upload/" + item.getName());
                        } catch (Exception e) {
                            System.out.println("Error trying to write file");
                            e.printStackTrace();
                        }
                    }
                } else if (naam.equals("Aantal")) {
                    opdracht.setAantal(Integer.valueOf(input));
                } else if (naam.equals("Commentaar")) {
                    if (!"".equals(input)) {
                        opdracht.setCommentaar(input);
                    }
                } else if (naam.equals("Klassen")) {
                    // doelgroep wordt gelinkt met opdracht
                    // om te factureren aan leerlingen wordt gekeken naar de opdracht aanmaakdatum
                    // die wordt gebruikt om het schooljaar te bepalen
                    // dan wordt in de tabel doelgroepleerlingen gekeken welke leerlingen er allemaal in die doelgroep
                    // zaten in dat schooljaar
                    // en dan wordt ook nog eens extra gekeken naar de leerling geldigheid om te zien
                    // of deze opdracht ook voor hem werd uitgevoerd
                    // TODO correct this.
                  //  Doelgroep doelgroep = (Doelgroep) DatabaseManager.getObjectWithId(Doelgroep.class, Long.valueOf(input));
//                    if (doelgroep != null) {
//                        opdracht.addDoelgroep(doelgroep);
//                    }

                }
                break;
            case DATUM:
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                format.setLenient(false);
                try {
                    format.parse(input);
                    opdracht.addInputWaarde(veld.getInputVeld(), input);
                } catch (ParseException ex) {
                    // TODO show error to user
                    ex.printStackTrace();
                }

                break;
            case GETAL:
                try {
                    Integer.parseInt(input);
                    opdracht.addInputWaarde(veld.getInputVeld(), input);
                } catch (NumberFormatException e) {
                    // TODO show error to user
                }
                break;
            case TEKST:
                opdracht.addInputWaarde(veld.getInputVeld(), input);
                break;
        }
    }

    private void setData(Opdracht opdracht, List<OpdrachtTypeInput> velden, List items) {
        Iterator it = items.iterator();
        while (it.hasNext()) {
            FileItem item = (FileItem) it.next();
            System.err.println(item.getFieldName() + " - " + item.getString());
            OpdrachtTypeInput veld = getField(velden, item);
            if (veld != null) {
                putDataInOpdracht(veld, item, opdracht);
            } else {
                Long optieId = Long.valueOf(item.getString());
                OptieDB odb = new OptieDB();
                Optie optie = odb.getWithId(optieId);
                if (optie != null) {
                    opdracht.addOptie(optie);
                } else {
                    System.out.println("not optie: " + item.getString());
                }
            }
        }

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
