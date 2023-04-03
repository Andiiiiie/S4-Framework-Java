package objets;
import etu1938.framework.ModelView;
import etu1938.framework.annotations.MappingUrl;

public class Objet {
    public Objet()
    {

    }

    @MappingUrl(method = "descri")
    public ModelView description()
    {
        return new ModelView("page.jsp");
    }
}
