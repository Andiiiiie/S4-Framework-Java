import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //test sprint8
        String url="test";
        String[] mots=url.split("\\?");
        Map<String, String> map=new LinkedHashMap<>();
        if(mots.length>1)
        {
            String parametres=mots[1];
            map=getParameters(parametres);
            for (String key : map.keySet()) {
                System.out.println("Clé : " + key);
            }

            // Parcourir les valeurs de la HashMap avec une boucle for-each
            for (String value : map.values()) {
                System.out.println("Valeur : " + value);
            }
        }

        Objet objet=new Objet();
        Method method=findMethodByName(objet.getClass(),"test",map);
        System.out.println(method.getName());
        /*Parameter[] parameters=new Parameter[0];
        // Obtention des paramètres de la méthode
        if(method.getParameterCount()!=0)
        {
            parameters = method.getParameters();
        }*/
        Parameter[] parameters=method.getParameters();

        // Vérification du nombre de paramètres
        if (parameters.length !=map.size()) {
            System.out.println("Nombre incorrect d'arguments.");
            return;
        }

        // Construction du tableau d'arguments
        Object[] methodArgs = new Object[parameters.length];
        List<Map.Entry<String, String>> entries = new ArrayList<>(map.entrySet());
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, String> entry = entries.get(i);
            int index = i + 1; // L'indice commence à 1

            System.out.println("Index: " + index);
            System.out.println("Valeur: " + entry.getValue());
        }
        for (int i = 0; i < parameters.length; i++) {

            System.out.println(parameters[i].getType());
            methodArgs[i] = casting(parameters[i].getType(),entries.get(i).getValue());
        }

        // Appel de la méthode avec les arguments appropriés
        int result = (int) method.invoke(objet, methodArgs);
        System.out.println(result);
    }


    private static Method findMethodByName(Class<?> clazz, String methodName,Map<String,String> arguments) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.getParameterCount() == arguments.size()) {
                return method;
            }
        }

        return null;
    }


    public static Object casting(Class type,String string)
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

    public static Map<String, String> getParameters(String s)
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
    }
    public  static String getSetter(String attribut)
    {
        String capitalizedStr = attribut.substring(0, 1).toUpperCase() + attribut.substring(1);
        return "set"+capitalizedStr;
    }
    public static Vector<String> getClasses(String path,String p)
    {
        File file=new File(path);
        String[] liste=file.list();
        Vector<String> reponse=new Vector<>();
        if(liste!=null)
        {
            for (int i=0;i< liste.length;i++)
            {
                if(liste[i].endsWith(".java"))
                {
                    reponse.add(p+"."+liste[i].split(".java")[0]);
                }
            }
        }

        return reponse;
    }
}
