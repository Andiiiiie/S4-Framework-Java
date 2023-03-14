package etu1938.framework.servlet;

import etu1938.framework.annotations.MappingUrl;
import etu1938.framework.Mapping;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

public class FrontServlet extends HttpServlet {
    private HashMap<String, Mapping> MappingUrls;


    @Override
    public void init() throws ServletException {
        HashMap<String, Mapping> map = new HashMap<String, Mapping>();
        String path="/home/andie/IdeaProjects/S4-Framework-Java/src/main/java/etu1938.framework.objets";
        Vector<String> liste=getClasses(path, "objets");
        for(String nom:liste)
        {
            Class<?> cl= null;
            try {
                cl = Class.forName(nom);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Method[] methods= cl.getDeclaredMethods();
            for (Method method:methods)
            {
                if(method.isAnnotationPresent(MappingUrl.class))
                {
                    Mapping value1 = new Mapping(nom,method.getName());
                    map.put(method.getAnnotation(MappingUrl.class).method(), value1);
                }
            }
        }
        setMappingUrls(map);
    }
    public static Vector<String> getClasses(String path,String p)
    {
        File file=new File(path);
        String[] liste=file.list();
        Vector<String> reponse=new Vector<>();
        for (int i=0;i< liste.length;i++)
        {
            if(liste[i].endsWith(".java"))
            {
                reponse.add(p+"."+liste[i].split(".java")[0]);
            }
        }
        return reponse;
    }



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
        String[] mots=lien.split("/",5);
        response.getWriter().println(mots[mots.length-1]);
        response.getWriter().println(getMappingUrls().get(mots[mots.length-1]).getClassName()+":"+getMappingUrls().get(mots[mots.length-1]).getMethod());

    }


    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        MappingUrls = mappingUrls;
    }
}
