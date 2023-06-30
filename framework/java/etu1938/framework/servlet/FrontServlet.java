package etu1938.framework.servlet;

import com.google.gson.Gson;
import etu1938.framework.annotations.*;
import etu1938.framework.core.ModelView;
import etu1938.framework.core.Mapping;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

import etu1938.framework.tools.File_class;

import java.io.*;
import java.security.Key;
import java.util.Collection;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

@MultipartConfig
public class FrontServlet extends HttpServlet {
    private HashMap<String, Mapping> MappingUrls;
    private HashMap<Class,Object> Instances;

    public static String nom_session;
    public static  String[] profils;

    @Override
    public void init() throws ServletException {
        String path=getInitParameter("PATH");
        setMappingUrls(path);
        setInstances(new HashMap<>());
        setNom_session(getInitParameter("nom_session"));
        setProfils(getInitParameter("profiles").split(","));
    }




    public void setMappingUrls(String path)
    {
        HashMap<String, Mapping> map = new HashMap<>();
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

    public Boolean canAccess(Method method,String connecte)
    {
        if (connecte==null && method.isAnnotationPresent(User.class))
        {
            return false;
        }
        if(method.isAnnotationPresent(User.class))
        {
            User user=method.getAnnotation(User.class);
            String[] autorises=user.profil().split(",");
            for (String s:autorises)
            {
                if(s.equals(connecte))
                {
                    return true;
                }
            }
        }
        else
        {
            return true;
        }
        return false;
    }


    private void processRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //PrintWriter out=response.getWriter();
        String lien= String.valueOf(request.getRequestURL());
        String[] mots=lien.split("/",5);

        Mapping mapping = getMappingUrls().get(mots[mots.length-1]);
        if(mapping == null) {
            response.getWriter().println("404 Not Found");
            return;
        }
        try {
            Class<?> cl = Class.forName(mapping.getClassName());
            Object o=null;
            if (cl.isAnnotationPresent(Singleton.class))
            {
                if(getInstances().get(cl)!=null)
                {
                    o=getInstances().get(cl);
                }
                else
                {
                    getInstances().put(cl,cl.getDeclaredConstructor().newInstance());
                    o=getInstances().get(cl);

                }
            }
            else
            {
                o = cl.getDeclaredConstructor().newInstance();
            }
            o=cl.cast(o);


            Method m=findMethodByName(cl,mapping.getMethod());
            HttpSession session = request.getSession();
            String connecte=null;
            if (session.getAttribute(FrontServlet.getNom_session())!=null)
            {
                connecte = session.getAttribute(FrontServlet.getNom_session()).toString();
            }



            if(canAccess(m,connecte))
            {
                if(m.isAnnotationPresent(Session.class))
                {
                    HashMap<String, Object> sessions=new HashMap<>();
                    Enumeration<String> liste_session=session.getAttributeNames();
                    while (liste_session.hasMoreElements())
                    {
                        String key=liste_session.nextElement();
                        Object object=session.getAttribute(key);
                        sessions.put(key,object);

                    }
                    Method method_call_session=o.getClass().getMethod("setSession",sessions.getClass());
                    method_call_session.invoke(o,sessions);
                }
                Vector<String> liste_arguments=new Vector<>();
                Parameter[] parameters = m.getParameters();
                for (Parameter parameter : parameters) {
                    String argumentName = parameter.getName();
                    liste_arguments.add(argumentName);
                }


                Object[] methodArgs = new Object[parameters.length];
                int nb=0;
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
                    if( liste_arguments.contains(attribut))
                    {
                        Class<?> type=null;
                        for (int i=0;i<parameters.length;i++)
                        {
                            if(parameters[i].getName().equals(attribut))
                            {
                                type=parameters[i].getType();
                                //out.println("type:  "+type.getName());
                            }
                        }
                        methodArgs[nb]=this.casting(type,request.getParameter(attribut));
                        nb++;
                    }
                }
                // Obtenir les fichiers téléchargés à partir de la
                String contentType1 = request.getContentType();
                if (contentType1 != null && (contentType1.startsWith("multipart/form-data") || contentType1.startsWith("multipart/mixed"))) {
                    // La requête utilise le type de contenu multipart
                    Collection<Part> parts = request.getParts();
                    if (parts.size()!=0)
                    {

                        // Parcourir les objets Part correspondant aux fichiers téléchargés
                        for (Part part : parts) {
                            String contentType = part.getContentType();
                            // Obtenir le nom attribué au champ de fichier
                            if (contentType != null && contentType.startsWith("multipart/form-data")) {
                                //out.println("tatoe "+parts.size());
                                String fieldName = part.getName();
                                String originalFileName = getFileName(part);
                                InputStream fileContent = part.getInputStream();
                                byte[] fileBytes = fileContent.readAllBytes();
                                String source = "source";
                                File_class fileClass = new File_class(originalFileName, source, fileBytes);
                                if (isAttribute(cl, fieldName)) {
                                    Field field = cl.getDeclaredField(fieldName);
                                    Method temp = cl.getDeclaredMethod(getSetter(fieldName), field.getType());

                                    temp.invoke(o, fileClass);
                                } else if (liste_arguments.contains(fieldName)) {
                                    methodArgs[nb] = fileClass;
                                }
                                fileContent.close();
                            }

                        }
                    }

                    if(m.isAnnotationPresent(Allowed.class))
                    {
                        ModelView mv = (ModelView) m.invoke(o,methodArgs);

                        if(getInstances().get(cl)!=null)
                        {
                            clearObject(cl);
                        }

                        //ajouter toutes les sessions
                        for(Map.Entry<String, Object> entry :mv.getSession().entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if(value==null)
                            {
                                session.removeAttribute(key);
                            }
                            else
                            {
                                session.setAttribute(key,value);
                            }
                        }
                        if(mv.getJson())
                        {
                            Gson gson=new Gson();
                            String json=gson.toJson(mv.getData());
                            PrintWriter out=response.getWriter();
                            out.println(json);
                        }

                        else
                        {
                            for(Map.Entry<String, Object> entry :mv.getData().entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                request.setAttribute(key,value);
                            }

                            request.getRequestDispatcher(mv.getView()).forward(request, response);
                        }


                    }
                    else
                    {
                        response.getWriter().println("Cannot access this method");
                    }
                }
                else
                {
                    Object objet=m.invoke(o,methodArgs);
                    Gson gson=new Gson();
                    String json=gson.toJson(objet);
                    response.getWriter().println(json);
                }
            }





        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void clearObject(Class<?> cl) throws NoSuchFieldException, IllegalAccessException {
        Object o = getInstances().get(cl);
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Permet d'accéder aux champs privés si nécessaire
            Class<?> fieldType = field.getType();
            if (fieldType.isPrimitive()) {
                // Pour les types primitifs, utilisez leurs valeurs par défaut
                if (fieldType == boolean.class) {
                    field.setBoolean(o, false);
                } else if (fieldType == byte.class) {
                    field.setByte(o, (byte) 0);
                } else if (fieldType == short.class) {
                    field.setShort(o, (short) 0);
                } else if (fieldType == int.class) {
                    field.setInt(o, 0);
                } else if (fieldType == long.class) {
                    field.setLong(o, 0L);
                } else if (fieldType == float.class) {
                    field.setFloat(o, 0.0f);
                } else if (fieldType == double.class) {
                    field.setDouble(o, 0.0);
                } else if (fieldType == char.class) {
                    field.setChar(o, '\u0000');
                }
            } else {
                // Pour les types non primitifs, définissez la valeur sur null
                field.set(o, null);
            }
        }
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf("=") + 1).trim().replace("\"", "");
            }
        }
        return "";
    }

    private static Method findMethodByName(Class<?> clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {

            if (method.getName().equals(methodName)) {
                return method;
            }
        }

        return null;
    }



    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        MappingUrls = mappingUrls;
    }

    public HashMap<Class, Object> getInstances() {
        return Instances;
    }

    public void setInstances(HashMap<Class, Object> instances) {
        Instances = instances;
    }

    public static String getNom_session() {
        return nom_session;
    }

    public static void setNom_session(String nom_session) {
        FrontServlet.nom_session = nom_session;
    }

    public static String[] getProfils() {
        return profils;
    }

    public static void setProfils(String[] profils) {
        FrontServlet.profils = profils;
    }
}
