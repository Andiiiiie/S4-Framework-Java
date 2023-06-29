package etu1938.framework.core;

import java.util.List;
import java.util.Vector;

public class Mapping {
    String className;
    String Method;

    public Mapping(String className,String method)
    {
        setClassName(className);
        setMethod(method);
    }
    public Mapping()
    {

    }

    public static Vector<String> getAttributs_list(String n)
    {
        Vector<String> reponses=new Vector<>();
        String[] tab=n.split("/");
        for (int i=1;i<tab.length;i++)
        {
            reponses.add(tab[i]);
        }
        return reponses;
    }

    public static String getNomMethode(String n)
    {
        String[] tab=n.split("/");
        return tab[0];
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }


}
