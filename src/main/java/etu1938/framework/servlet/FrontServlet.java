package etu1938.framework.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

public class FrontServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request,response);
    }

    private void processRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String lien= String.valueOf(request.getRequestURL());
        response.getWriter().println(lien);
        String[] mots=lien.split("/",5);
        response.getWriter().println(mots[mots.length-1]);
    }
}
