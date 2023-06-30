package objets;
import etu1938.framework.annotations.*;
import etu1938.framework.core.ModelView;
import etu1938.framework.tools.Connexion;
import etu1938.framework.tools.File_class;

import java.sql.Date;
import java.util.HashMap;

@Singleton
public class Objet {

    Date date;
    Integer nombre;

    HashMap<String,Objet> session;
    public Objet()
    {

    }
    @Session
    @MappingUrl(method = "test_session")
    public ModelView test_session()
    {
        ModelView retour=new ModelView("sessions.jsp");
        retour.addItem("sessions",getSession());
        return retour;
    }


    @MappingUrl(method = "test_fonction")
    @Allowed
    public String test_fonction()
    {
        return "metyyyy";
    }

    @MappingUrl(method = "test_json")
    public ModelView test_json()
    {
        ModelView m=new ModelView("no page");
        m.setJson(true);
        m.addItem("haha",1);
        return m;
    }

    @MappingUrl(method = "addSession")
    public ModelView ajouterSession()
    {
        ModelView modelView=new ModelView("page.jsp");
        modelView.addSession("aonaa","metyyy");
        return modelView;
    }

    @MappingUrl(method = "supprimerSession")
    public ModelView supprimer_session()
    {
        ModelView modelView=new ModelView("page.jsp");
        modelView.addSessionToRemove("aonaa");
        return modelView;
    }


    @MappingUrl(method = "save")
    @User(profil = "admin,test")
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
    @MappingUrl(method = "connect")
    public ModelView test_connexion()
    {
        ModelView reps=new ModelView("page.jsp");
        Connexion.login(reps,"admin");
        return reps;
    }
    @MappingUrl(method = "deconnect")
    public ModelView test_deconnexion()
    {
        ModelView reps=new ModelView("page.jsp");
        Connexion.deconnect(reps);
        return reps;
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

    public HashMap<String, Objet> getSession() {
        return session;
    }

    public void setSession(HashMap<String, Objet> session) {
        this.session = session;
    }
}
