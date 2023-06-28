package etu1938.framework.servlet;

import etu1938.framework.ModelView;
import etu1938.framework.annotations.MappingUrl;
import etu1938.framework.Mapping;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.util.*;

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
//        PrintWriter out=response.getWriter();
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
            //Method m = cl.getDeclaredMethod(mapping.getMethod());
            Method m=findMethodByName(cl,mapping.getMethod());
            Vector<String> liste_arguments=new Vector<>();
            //if (m!= null) {
                Parameter[] parameters = m.getParameters();
                for (Parameter parameter : parameters) {
                    String argumentName = parameter.getName();
                    liste_arguments.add(argumentName);
                }
            //}


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
//                    out.println("tatto \n");
//                    out.println(attribut+"  "+ request.getParameter(attribut));
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



            // Construction du tableau d'arguments




            //envoyer donnees a la methode si il y en a

            ModelView mv = (ModelView) m.invoke(o,methodArgs);

            //ModelView mv = (ModelView) m.invoke(o,arguements);
            for(Map.Entry<String, Object> entry :mv.getData().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                request.setAttribute(key,value);
                // faire quelque chose avec la clé et la valeur
            }
            request.getRequestDispatcher(mv.getView()).forward(request, response);
        } catch (Exception e) {
//            out.println("tato");
            throw new RuntimeException(e);
        }
    }

/*    private void processRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
        PrintWriter out=response.getWriter();


        String lien= String.valueOf(request.getRequestURL());
        try {


        String[] mots=lien.split("/",5);
            out.println(lien+"    "+mots[mots.length-1]);
        Mapping mapping = getMappingUrls().get(mots[mots.length-1]);
        out.println(mapping.getClassName());
        Map<String, String> map=new LinkedHashMap<>();

        Class<?> cl = Class.forName(mapping.getClassName());
        Object o = cl.getDeclaredConstructor().newInstance();

        Method m = findMethodByName(cl,mapping.getMethod());

        Enumeration<String> liste1=request.getParameterNames();
        while (liste1.hasMoreElements())
        {

            String temp= liste1.nextElement();
            out.println("temp "+temp);

            if(isParameter(mapping,temp))
            {
                out.println("tato eee:  "+(String) request.getParameter(temp));
                map.put(temp, (String) request.getParameter(temp));
            }

        }
        if(mapping == null) {
            response.getWriter().println("404 Not Found");
            //afficher tout les parametres

            return;
        }







            //get Parameter from form and call setters if object attribute
            Enumeration<String> liste=request.getParameterNames();

            while (liste.hasMoreElements())
            {
                String attribut=liste.nextElement();

                out.println(attribut);
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

            out.println("hehe"+m.getName());
            Parameter[] parameters = m.getParameters();
            // Construction du tableau d'arguments
            Object[] methodArgs = new Object[parameters.length];
            List<Map.Entry<String, String>> entries = new ArrayList<>(map.entrySet());
            out.println(entries.size()+"  size");


            for (int i = 0; i < entries.size(); i++) {
                Map.Entry<String, String> entry = entries.get(i);
                int index = i + 1; // L'indice commence à 1

            }
            for (int i = 0; i < parameters.length; i++) {

                out.println(parameters[i].getType()+"   valeur: "+entries.get(i).getValue());
                methodArgs[i] = casting(parameters[i].getType(),entries.get(i).getValue());
            }

            //envoyer donnees a la methode si il y en a

            ModelView mv = (ModelView) m.invoke(o,methodArgs);
            for(Map.Entry<String, Object> entry :mv.getData().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                request.setAttribute(key,value);
                // faire quelque chose avec la clé et la valeur
            }

            request.getRequestDispatcher(mv.getView()).forward(request, response);
            out.println("tsy maNDEHA");
        } catch (Exception e) {
            out.println("ato @ exception"+e.toString());
           e.printStackTrace(out);
        }
    }*/




    private static Method findMethodByName(Class<?> clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {

            if (method.getName().equals(methodName)) {
                return method;
            }
        }

        return null;
    }


    /*public static Map<String, String> getParameters(String s)
    {
        Map<String, String> parameterAttribution=new LinkedHashMap<>();
        String[] mots1=s.split("&");
        for (int i=0;i<mots1.length;i++)
        {
            System.out.println(mots1[i]);
            String[] mots2=mots1[i].split("=");
            parameterAttribution.put(mots2[0],mots2[1]);
        }
        return  parameterAttribution;
    }*/

    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        MappingUrls = mappingUrls;
    }
}
