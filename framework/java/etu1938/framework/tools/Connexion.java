package etu1938.framework.tools;


import etu1938.framework.core.ModelView;
import etu1938.framework.servlet.FrontServlet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Connexion {



    public static void login(ModelView modelView,String profil)
    {
        String paramValue= FrontServlet.nom_session;
        modelView.addSession(paramValue, profil);
    }

    public static void exit(ModelView modelView)
    {
        modelView.deleteSession(FrontServlet.nom_session);
    }
}
