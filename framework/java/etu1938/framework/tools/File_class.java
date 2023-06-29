package etu1938.framework.tools;

public class File_class {
    String name;
    String path;
    byte[] content = new byte[0];


    public File_class(String name, String path, byte[] content)
    {
        setName(name);
        setContent(content);
        setPath(path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
