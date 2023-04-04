package etu1938.framework.servlet;

import etu1938.framework.ModelView;
import etu1938.framework.annotations.MappingUrl;
import etu1938.framework.Mapping;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class FrontServlet extends HttpServlet {
    private HashMap<String, Mapping> MappingUrls;


    @Override
    public void init() throws ServletException {
        HashMap<String, Mapping> map = new HashMap<>();
        String path=getInitParameter("PATH");
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

        Mapping mapping = getMappingUrls().get(mots[mots.length-1]);
        if(mapping == null) {
            response.getWriter().println("404 Not Found");
            return;
        }
        try {
            Class<?> cl = Class.forName(mapping.getClassName());
            Object o = cl.getDeclaredConstructor().newInstance();
            Method m = cl.getDeclaredMethod(mapping.getMethod());
            ModelView mv = (ModelView) m.invoke(o);
            for(Map.Entry<String, Object> entry :mv.getData().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                request.setAttribute(key,value);
                // faire quelque chose avec la clé et la valeur
            }
            request.getRequestDispatcher(mv.getView()).forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        MappingUrls = mappingUrls;
    }
}
