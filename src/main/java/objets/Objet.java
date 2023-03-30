package etu1938.framework.objets;
import etu1938.framework.annotations.MappingUrl;

public class Objet {
    public Objet()
    {

    }


    @MappingUrl(method = "descri")
    public void description()
    {
        System.out.println("aona.Objet description");
    }
}
