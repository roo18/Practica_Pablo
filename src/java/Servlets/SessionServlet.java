/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author alumno
 */
public class SessionServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //establece el tipo MIME del mensaje de respuesta
        response.setContentType("text/html");
        //Crea un flujo de salida para escribir la respuesta a la peticion del cliente
        PrintWriter out = response.getWriter();

        //Recoge la sesion actual si existe, en otro caso crea una nueva.
        HttpSession session = request.getSession();
        Integer contadorAccesos;
        synchronized (session) {
            contadorAccesos = (Integer) session.getAttribute("contadorAccesos");
            if (contadorAccesos == null) {
                contadorAccesos = 0;
            } else {
                contadorAccesos = new Integer(contadorAccesos + 1);
            }
            session.setAttribute("contadorAccesos", contadorAccesos);

        }
        int limiteCnt = Integer.parseInt(getServletContext().getInitParameter("limiteContador"));
        if (contadorAccesos == limiteCnt) {
            response.sendRedirect("superado.html");
        }

        //Escribe el mensaje de respuesta en una pagina html
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html><head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            out.println("<title>Servlet de prueba de sesion</title></head><body>");
            out.println("<h2>Accesos : " + contadorAccesos + " en esta session </h2>");
            out.println("<p>(Identificador de sesion: " + session.getId() + " )</p>");
            out.println("<p>(Fecha de creacion de la sesion: " + session.getCreationTime() + ")</p>");
            out.println("<p>(Fecha de ultimo acesso a la sesion: " + session.getLastAccessedTime() + ")</p>");
            out.println("<p>(Maximo tiempo inactivo de la sesion: " + session.getMaxInactiveInterval() + ")</p>");
            out.println("<p><a href='" + request.getRequestURL() + "'>Refrescar</a></p>");
            out.println("<p><a href='" + response.encodeURL(request.getRequestURI()) + "'>Refrescar con reescritura de URLs</a></p>");
            out.println("</body></html>");
        } finally {
            out.close();
        }
    }
}
