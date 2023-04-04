package objets;
import etu1938.framework.ModelView;
import etu1938.framework.annotations.MappingUrl;

public class Objet {

    String attribut;
    public Objet()
    {

    }

    @MappingUrl(method = "descri")
    public ModelView description()
    {
        this.setAttribut("haha");
        ModelView modelView=new ModelView("page.jsp");
        modelView.addItem("test",this.getAttribut());
        return modelView;
    }

    public String getAttribut() {
        return attribut;
    }

    public void setAttribut(String attribut) {
        this.attribut = attribut;
    }
}
