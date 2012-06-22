/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.*;
import decorators.OpdrachtDecorator;
import domain.*;
import domain.enums.OpdrachtStatus;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
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
            opdracht.setAanmaakDatum(new Date());
            opdracht.setStatus(OpdrachtStatus.AANGEVRAAGD);
            opdracht.setOpdrachtgever(gebruiker);
            OpdrachtType ot = otdb.getWithId(decorator.opdrachtType);
            opdracht.setOpdrachtType(ot);
            List<OpdrachtTypeInput> velden = otidb.list();
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload handler = new ServletFileUpload(factory);
            try {
                // List items = handler.parseRequest(request);
                FileItemIterator items = handler.getItemIterator(request);
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

    private OpdrachtTypeInput getField(List<OpdrachtTypeInput> velden, String fieldName) {
        System.out.println("fieldname = " + fieldName);
        for (OpdrachtTypeInput oti : velden) {
            System.out.println("oti id = " + oti.getInputVeld().getId());
            if (oti.getInputVeld().getId().compareTo(Long.valueOf(fieldName)) == 0) {
                System.out.println("found");
                return oti;
            }
        }
        return null;
    }

    private void putDataInOpdracht(OpdrachtTypeInput veld, FileItem item, Opdracht opdracht) {
        GebruikerDB gdb = new GebruikerDB();
        String input = item.getString();
        InputVeld iv = veld.getInputVeld();
        switch (iv.getType()) {
            case VAST:
                String naam = iv.getNaam();
                if (naam.equals("Opdrachtgever")) {
                    Gebruiker opdrachtgever = gdb.getWithId(Long.valueOf(input));
                    opdracht.setOpdrachtgever(opdrachtgever);
                } else if (naam.equals("Bestand")) {
                    if (!item.isFormField()) {
                        InputStream stream = null;
//                        try {
                        //     FileItemStream streamItem = (FileItemStream) item;
                        //     stream = streamItem.openStream();
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
                    DoelgroepDB ddb = new DoelgroepDB();
                    Doelgroep doelgroep = ddb.getWithId(Long.valueOf(input));
                    if (doelgroep != null) {
                        opdracht.addDoelgroep(doelgroep);
                    }

                }
                break;
            case DATUM:
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                format.setLenient(false);
                try {
                    format.parse(input);
                    opdracht.addInputWaarde(iv, input);
                } catch (ParseException ex) {
                    // TODO show error to user
                    ex.printStackTrace();
                }

                break;
            case GETAL:
                try {
                    Integer.parseInt(input);
                    opdracht.addInputWaarde(iv, input);
                } catch (NumberFormatException e) {
                    // TODO show error to user
                }
                break;
            case TEKST:
                opdracht.addInputWaarde(iv, input);
                break;
        }
    }

    private void putDataInOpdracht(OpdrachtTypeInput veld, FileItemStream item, Opdracht opdracht) {
        GebruikerDB gdb = new GebruikerDB();
        InputStream input;
        StringBuilder strBuilder = new StringBuilder();
            
        try {
            input = item.openStream();

            int len;
            byte[] buffer = new byte[8192];
            while ((len = input.read(buffer, 0, buffer.length)) != -1) {
                System.out.println(buffer);
                // TODO put input from stream in string or file
            }
        } catch (IOException ex) {
            Logger.getLogger(InputOpdrachtServlet.class.getName()).log(Level.SEVERE, null, ex);
        }


        InputVeld iv = veld.getInputVeld();
        switch (iv.getType()) {
            case VAST:
                String naam = iv.getNaam();
                if (naam.equals("Opdrachtgever")) {
                    Gebruiker opdrachtgever = gdb.getWithId(Long.valueOf(strBuilder.toString()));
                    opdracht.setOpdrachtgever(opdrachtgever);
                } else if (naam.equals("Bestand")) {
                    if (!item.isFormField()) {
                        InputStream stream = null;
                        try {
                            FileItemStream streamItem = (FileItemStream) item;
                            stream = streamItem.openStream();
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
                        } catch (IOException ex) {
                            Logger.getLogger(InputOpdrachtServlet.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                stream.close();
                            } catch (IOException ex) {
                                Logger.getLogger(InputOpdrachtServlet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                } else if (naam.equals("Aantal")) {
                    opdracht.setAantal(Integer.valueOf(strBuilder.toString()));
                } else if (naam.equals("Commentaar")) {
                    if (!"".equals(strBuilder.toString())) {
                        opdracht.setCommentaar(strBuilder.toString());
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
                    Doelgroep doelgroep = ddb.getWithId(Long.valueOf(strBuilder.toString()));
                    if (doelgroep != null) {
                        opdracht.addDoelgroep(doelgroep);
                    }

                }
                break;
            case DATUM:
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                format.setLenient(false);
                try {
                    format.parse(strBuilder.toString());
                    opdracht.addInputWaarde(iv, strBuilder.toString());
                } catch (ParseException ex) {
                    // TODO show error to user
                    ex.printStackTrace();
                }

                break;
            case GETAL:
                try {
                    Integer.parseInt(strBuilder.toString());
                    opdracht.addInputWaarde(iv, strBuilder.toString());
                } catch (NumberFormatException e) {
                    // TODO show error to user
                }
                break;
            case TEKST:
                opdracht.addInputWaarde(iv, strBuilder.toString());
                break;
        }
    }

    private void setData(Opdracht opdracht, List<OpdrachtTypeInput> velden, FileItemIterator items) {
        try {
            while (items.hasNext()) {
                FileItemStream item = (FileItemStream) items.next();
                System.err.println(item.getFieldName() + " - " + item.getName());
                System.err.println(item.getContentType() + " - " + item.openStream().toString());
                OpdrachtTypeInput veld = getField(velden, item.getFieldName());
                if (veld != null) {
                    putDataInOpdracht(veld, item, opdracht);
                } else {
                    Long optieId = Long.valueOf(item.getFieldName());
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

    private void setData(Opdracht opdracht, List<OpdrachtTypeInput> velden, List items) {
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
