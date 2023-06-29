package etu1938.framework.core;

import java.util.HashMap;

public class ModelView {
    String view;
    HashMap<String, Object> data;
    HashMap<String, Object> session;

    public ModelView(String view) {
        setView(view);
        setData(new HashMap<>());
        setSession(new HashMap<>());
    }

    public void addItem(String key,Object value)
    {
        this.getData().put(key,value);
    }

    public void deleteSession(String key)
    {
        addSession(key,null);
    }

    public void addSession(String key,Object value)
    {
        this.getSession().put(key,value);
    }



    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public HashMap<String, Object> getSession() {
        return session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }
}
