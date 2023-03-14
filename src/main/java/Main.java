import annotations.MappingUrl;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        String path="/home/andie/IdeaProjects/S4-Framework-Java/src/main/java/objets";
        Vector<String> liste=getClasses(path, "objets");
        for(String nom:liste)
        {
            System.out.println("tato");
            Class<?> cl= null;
            try {
                cl = Class.forName(nom);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Method[] methods= cl.getDeclaredMethods();
            for (Method method:methods)
            {
                System.out.println("aonaa :"+method.getName());
                if(method.isAnnotationPresent(MappingUrl.class))
                {
                    System.out.println(method.getAnnotation(MappingUrl.class).method());
                }
            }
        }
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
