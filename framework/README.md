# INSTALLATION
- Copier le framework.jar dans votre dossier lib
- dans votre projet creer un dossier objets dans le dossier java et mettre classes .java dans ce dossier
- Dans votre projet dans web.xml ajouter :
 ```
  <servlet>
  <servlet-name>FrontServlet</servlet-name>
  <servlet-class>etu1938.framework.servlet.FrontServlet</servlet-class>
  <init-param>
  <param-name>PATH</param-name>
  <param-value>[chemin absolu vers le dossier objets de vote votre projet]</param-value>
  </init-param>
  </servlet>
  <servlet-mapping>
      <servlet-name>FrontServlet</servlet-name>
      <url-pattern>/</url-pattern>
  </servlet-mapping>
   ```
>UTILISER DES TYPES PRIMITIFS<br>
>UTILISER DES TYPES DATE SQL
# MODIFIER LE SCRIPT.SH
- modifier tomcat_libs en le chemin du lib de votre dossier tomcat  et rajoute /*
- modifier tomcat_webapps en le chemin du webapps de votre dossier tomcat  
- modifier project_name par le nom du projet .war que vous voulez
- mettre script.sh dans le meme dossier que le dossier de votre projet


# UTILISATION
- apres avoir creer une classe dans  le dossier objets rajouter cette ligne dans le script.sh:<br>
```javac -cp "temp/WEB-INF/classes:$framework_jar" -d temp/WEB-INF/classes [nom de votre projet]/java/objets/[nom de classe]```
- renvoyer vers une page il faut creer une fonction qui retourn Modelview et annoter :
```@MappingUrl(method = "[le chemin pour appeler cette fonction exemple: save]") ```
>NE PAS OUBLIER SES IMPORTS:<br>
  ```import etu1938.framework.ModelView; ```<br>
  ```import etu1938.framework.annotations.MappingUrl; ```