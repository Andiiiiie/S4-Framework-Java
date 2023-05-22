package objets;
import etu1938.framework.ModelView;
import etu1938.framework.annotations.MappingUrl;

import java.sql.Date;

public class Objet {

    Attribut attribut;
    Date date;

    Integer nombre;
    public Objet()
    {

    }

    @MappingUrl(method = "descri")
    public ModelView description()
    {
        this.setAttribut(new Attribut("haha"));
        ModelView modelView=new ModelView("page.jsp");
        modelView.addItem("test",this.getAttribut());
        return modelView;
    }
    @MappingUrl(method = "save")
    public ModelView save()
    {
        ModelView modelView=new ModelView("page.jsp");
        modelView.addItem("saved",this);
        return modelView;
    }

    public Attribut getAttribut() {
        return attribut;
    }

    public void setAttribut(Attribut attribut) {
        this.attribut = attribut;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }
}
