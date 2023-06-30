package etu1938.framework.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModelView {
    String view;
    HashMap<String, Object> data;
    HashMap<String, Object> session;

    Boolean isJson=false;

    Boolean InvalidateSession=false;

    List<String> removeSession=new ArrayList<>();

    public ModelView(String view) {
        setView(view);
        setData(new HashMap<>());
        setSession(new HashMap<>());
    }

    public void addItem(String key,Object value)
    {
        this.getData().put(key,value);
    }

    public void addSessionToRemove(String key)
    {
        getRemoveSession().add(key);
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

    public Boolean getJson() {
        return isJson;
    }

    public void setJson(Boolean json) {
        isJson = json;
    }

    public Boolean getInvalidateSession() {
        return InvalidateSession;
    }

    public void setInvalidateSession(Boolean invalidateSession) {
        InvalidateSession = invalidateSession;
    }

    public List<String> getRemoveSession() {
        return removeSession;
    }

    public void setRemoveSession(List<String> removeSession) {
        this.removeSession = removeSession;
    }
}
