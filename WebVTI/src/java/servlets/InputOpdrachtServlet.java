/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import business.PrijsService;
import database.*;
import decorators.OpdrachtDecorator;
import domain.*;
import domain.constraints.OptiePrijsConstraint;
import domain.constraints.OptieTypePrijsConstraint;
import domain.constraints.PrijsConstraint;
import domain.enums.InputVeldType;
import domain.enums.OpdrachtStatus;
import domain.enums.PrijsType;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utils.InvalidContentException;
import utils.Util;
import utils.Validator;

/**
 *
 * @author soezen
 */
public class InputOpdrachtServlet extends HttpServlet {

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

        OpdrachtDecorator decorator = (OpdrachtDecorator) request.getSession().getAttribute("opdracht");
        Gebruiker gebruiker = (Gebruiker) request.getSession().getAttribute("gebruiker");
        if (decorator != null) {
            OpdrachtDB db = new OpdrachtDB();
            OpdrachtTypeDB otdb = new OpdrachtTypeDB();
            OpdrachtTypeInputDB otidb = new OpdrachtTypeInputDB();
            Opdracht opdracht = new Opdracht();
            HashMap<String, String> errors = new HashMap<String, String>();
            opdracht.setAanmaakDatum(new Date());
            opdracht.setStatus(OpdrachtStatus.AANGEVRAAGD);
            opdracht.setOpdrachtgever(gebruiker);
            OpdrachtType ot = otdb.getWithId(decorator.opdrachtType);
            opdracht.setOpdrachtType(ot);
            List<OpdrachtTypeInput> velden = otidb.list();
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload handler = new ServletFileUpload(factory);
            try {
                try {
                    System.out.println("TRYING 1");
                    // List items = handler.parseRequest(request);
                    FileItemIterator items = handler.getItemIterator(request);
                    setData(opdracht, velden, items);
                } catch (FileUploadBase.InvalidContentTypeException e) {
                    System.out.println("1 FAILED, TRYING 2");
                    setData(opdracht, velden, request);
                } catch (FileUploadException e) {
                    // process errors and report to user
                    e.printStackTrace();
                    errors.put("Bestand", "Laden van het bestand is niet gelukt");
                    request.setAttribute("errors", errors);
                    processRequest(request, response);
                    return;
                }
            } catch (InvalidContentException e) {
                errors.put(e.getFieldName(), e.getMessage());
                request.setAttribute("errors", errors);
                processRequest(request, response);
                return;
            }

            errors.putAll(Validator.getErrors(opdracht));
            if (errors.isEmpty()) {
                ConstraintDB cdb = new ConstraintDB();
                OptieDB odb = new OptieDB();
                PrijsService prijsService = new PrijsService();
                opdracht.setPrijs(prijsService.calculatePrijs(opdracht.getOpties()));
                opdracht = db.persist(opdracht);
                request.setAttribute("result", opdracht.getId());
            } else {
                System.out.println("ERRORS");
                request.setAttribute("errors", errors);
            }
        }
        processRequest(request, response);
    }

    private OpdrachtTypeInput getField(List<OpdrachtTypeInput> velden, String fieldName) {
        if (!fieldName.startsWith("OP")) {
            for (OpdrachtTypeInput oti : velden) {
                if (oti.getInputVeld().getId().compareTo(Long.valueOf(fieldName)) == 0) {
                    return oti;
                }
            }
        }
        return null;
    }

    private void putDataInOpdracht(OpdrachtTypeInput veld, FileItem item, Opdracht opdracht) throws InvalidContentException {
        String input = item.getString();
        if ("Bestand".equals(veld.getInputVeld().getNaam())) {
            if (!item.isFormField()) {
//                        InputStream stream = null;
//                        try {
                //     FileItemStream streamItem = (FileItemStream) item;
                //     stream = streamItem.openStream();
                // TODO get unique name by using opdrachtid
                // TODO output folder in property file steken zodat het makkelijk te wijzigen valt
//                        File file = new File("d:/upload", item.getName());
                try {
                    // TODO uncomment this when deploying
                    //     item.write(file);
                    opdracht.setBestand("d:/upload/" + item.getName());
                } catch (Exception e) {
                    System.out.println("Error trying to write file");
                    e.printStackTrace();
                }
                //    } catch (IOException ex) {
                //        Logger.getLogger(InputOpdrachtServlet.class.getName()).log(Level.SEVERE, null, ex);
                //    } finally {
//                            try {
//                                stream.close();
//                            } catch (IOException ex) {
//                                Logger.getLogger(InputOpdrachtServlet.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                        }
            }
        } else {
            putDataInOpdracht(veld, input, opdracht);
        }
    }

    private void putDataInOpdracht(OpdrachtTypeInput veld, String value, Opdracht opdracht) throws InvalidContentException {
        GebruikerDB gdb = new GebruikerDB();
        InputVeld iv = veld.getInputVeld();
        System.out.println("TYPE: " + iv.getType());
        if (!"".equals(value)) {
            switch (iv.getType()) {
                case VAST:
                    String naam = iv.getNaam();
                    if (naam.equals("Opdrachtgever")) {
                        Gebruiker opdrachtgever = gdb.getWithId(Long.valueOf(value));
                        opdracht.setOpdrachtgever(opdrachtgever);
                    } else if (naam.equals("Aantal")) {
                        try {
                            opdracht.setAantal(Integer.valueOf(value));
                        } catch (NumberFormatException e) {
                            throw new InvalidContentException(InputVeldType.GETAL, naam, value);
                        }
                    } else if (naam.equals("Commentaar")) {
                        if (!"".equals(value)) {
                            opdracht.setCommentaar(value);
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
                        DoelgroepDB ddb = new DoelgroepDB();
                        Doelgroep doelgroep = ddb.getWithId(Long.valueOf(value));
                        if (doelgroep != null) {
                            opdracht.addDoelgroep(doelgroep);
                        }

                    } 
                    break;
                case DATUM:
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    format.setLenient(false);
                    try {
                        format.parse(value);
                        opdracht.addInputWaarde(iv, value);
                    } catch (ParseException ex) {
                        throw new InvalidContentException(InputVeldType.DATUM, iv.getNaam(), value);
                    }
                    System.out.println("DATUM: " + value);
                    break;
                case GETAL:
                    try {
                        Integer.parseInt(value);
                        opdracht.addInputWaarde(iv, value);
                    } catch (NumberFormatException e) {
                        throw new InvalidContentException(InputVeldType.GETAL, iv.getNaam(), value);
                    }
                    break;
                case TEKST:
                    opdracht.addInputWaarde(iv, value);
                    break;
            }
        }
    }

    private void putDataInOpdracht(OpdrachtTypeInput veld, FileItemStream item, Opdracht opdracht) throws InvalidContentException {
        InputStream input;
        String output = "";

        try {
            input = item.openStream();
            StringWriter writer = new StringWriter();

            while (input.available() > 0) {
                writer.write(input.read());
            }
            output = writer.toString();
            System.out.println("output = " + output);

        } catch (IOException ex) {
            Logger.getLogger(InputOpdrachtServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        if ("Bestand".equals(veld.getInputVeld().getNaam())) {
            if (!item.isFormField()) {
                InputStream stream = null;
                File file = new File("d:/upload", item.getName());
                FileWriter writer = null;

                try {
                    FileItemStream streamItem = (FileItemStream) item;
                    stream = streamItem.openStream();
                    // TODO fix permission problem
                    //  writer = new FileWriter(file);
                    // TODO get unique name by using opdrachtid
                    // TODO output folder in property file steken zodat het makkelijk te wijzigen valt
                    try {
                        // TODO uncomment this when deploying
                        while (stream.available() > 0) {
                            //        writer.write(stream.read());
                        }

                        //     item.write(file);
                        opdracht.setBestand("d:/upload/" + item.getName());
                    } catch (IOException e) {
                        System.out.println("Error writing file");
                    } catch (Exception e) {
                        System.out.println("Error trying to write file");
                        e.printStackTrace();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(InputOpdrachtServlet.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (stream != null) {
                            stream.close();
                        }
                        if (writer != null) {
                            writer.close();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(InputOpdrachtServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            putDataInOpdracht(veld, output, opdracht);
        }
    }

    private void setData(Opdracht opdracht, List<OpdrachtTypeInput> velden, FileItemIterator items) throws InvalidContentException {
        try {
            while (items.hasNext()) {
                FileItemStream item = (FileItemStream) items.next();
                OpdrachtTypeInput veld = getField(velden, item.getFieldName());
                if (veld != null) {
                    putDataInOpdracht(veld, item, opdracht);
                } else {
                    Long optieId = Long.valueOf(item.getFieldName().substring(2));
                    OptieDB odb = new OptieDB();
                    Optie optie = odb.getWithId(optieId);
                    if (optie != null) {
                        opdracht.addOptie(optie);
                    } else {
                        System.out.println("not optie: " + item.getFieldName());
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(InputOpdrachtServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileUploadException e) {
        }
    }

    private void setData(Opdracht opdracht, List<OpdrachtTypeInput> velden, List items) throws InvalidContentException {
        Iterator it = items.iterator();
        while (it.hasNext()) {
            FileItem item = (FileItem) it.next();
            System.err.println(item.getFieldName() + " - " + item.getName());
            OpdrachtTypeInput veld = getField(velden, item.getFieldName());
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

    private void setData(Opdracht opdracht, List<OpdrachtTypeInput> velden, HttpServletRequest request) {
        Set<String> names = request.getParameterMap().keySet();

        for (String name : names) {
            System.out.println(name + " - " + request.getParameter(name));
        }
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
