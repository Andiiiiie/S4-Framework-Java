package objets;
import etu1938.framework.ModelView;
import etu1938.framework.File_class;
import etu1938.framework.annotations.MappingUrl;
import etu1938.framework.annotations.Singleton;

import java.sql.Date;
@Singleton
public class Objet {

    Date date;
    Integer nombre;
    public Objet()
    {

    }


    @MappingUrl(method = "save")
    public ModelView save()
    {
        ModelView modelView=new ModelView("page.jsp");
        modelView.addItem("saved",this.nombre);
        return modelView;
    }

    @MappingUrl(method = "get")
    public ModelView get(Integer id)
    {
        ModelView modelView=new ModelView("test_id.jsp");
        modelView.addItem("id",id);
        return modelView;
    }
    @MappingUrl(method = "upload")
    public  ModelView upload(File_class file)
    {
        ModelView modelView=new ModelView("test_id.jsp");
        modelView.addItem("id",file.getName());
        return modelView;
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
