package objets;
import annotations.MappingUrl;

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
