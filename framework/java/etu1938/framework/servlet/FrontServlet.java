package etu1938.framework.servlet;

import etu1938.framework.ModelView;
import etu1938.framework.annotations.MappingUrl;
import etu1938.framework.Mapping;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
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
                System.out.println(nom);
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

    public Boolean isAttribute(Class<?> objet,String attribut)
    {
        Field[] fields = objet.getDeclaredFields();

        for (Field field : fields) {
            if(attribut.equals(field.getName()))
            {
                return true;
            }
        }
        return false;
    }

    public String getSetter(String attribut)
    {
        String capitalizedStr = attribut.substring(0, 1).toUpperCase() + attribut.substring(1);
        return "set"+capitalizedStr;
    }

    public Object casting(Class type,String string)
    {
        Object objet=null;

        //regarder si cet objet a un constructeur qui recoit
        try {
            Constructor constructor=type.getConstructor(String.class);
            objet=constructor.newInstance(string);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            try {
                objet = type.getMethod("valueOf", String.class).invoke(type, string);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }
        return objet;
    }


    private void processRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {

        String lien= String.valueOf(request.getRequestURL());
        String[] mots=lien.split("/",5);

        Mapping mapping = getMappingUrls().get(mots[mots.length-1]);
        if(mapping == null) {
            response.getWriter().println("404 Not Found");
            //afficher tout les parametres

            return;
        }
        try {
            Class<?> cl = Class.forName(mapping.getClassName());
            Object o = cl.getDeclaredConstructor().newInstance();
            Method m = cl.getDeclaredMethod(mapping.getMethod());


            //get Parameter from form and call setters if object attribute
            Enumeration<String> liste=request.getParameterNames();
            while (liste.hasMoreElements())
            {
                String attribut=liste.nextElement();
                if(isAttribute(cl,attribut))
                {
                    Field field=cl.getDeclaredField(attribut);
                    Method temp=cl.getDeclaredMethod(getSetter(attribut),field.getType());
                    if (this.casting(field.getType(),request.getParameter(attribut))==null)
                    {
                        throw new RuntimeException("ERROR WHILE CASTING");
                    }
                    temp.invoke(o,this.casting(field.getType(),request.getParameter(attribut)));
                }
            }


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
