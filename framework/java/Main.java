import java.io.File;
import java.lang.reflect.Method;
import java.util.Vector;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {

    }
    public  static String getSetter(String attribut)
    {
        String capitalizedStr = attribut.substring(0, 1).toUpperCase() + attribut.substring(1);
        return "set"+capitalizedStr;
    }
    public static Vector<String> getClasses(String path,String p)
    {
        File file=new File(path);
        String[] liste=file.list();
        Vector<String> reponse=new Vector<>();
        if(liste!=null)
        {
            for (int i=0;i< liste.length;i++)
            {
                if(liste[i].endsWith(".java"))
                {
                    reponse.add(p+"."+liste[i].split(".java")[0]);
                }
            }
        }

        return reponse;
    }
}
