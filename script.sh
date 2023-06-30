tomcat_libs="/home/andie/tomcat/lib/*"
libs="/home/andie/IdeaProjects/S4-Framework-Java/test-framework/webapp/WEB-INF/lib/gson-2.10.1.jar"
tomcat_webapps="/home/andie/tomcat/webapps"

# Compile framework
javac -parameters -cp "temp" -d temp framework/java/etu1938/framework/annotations/MappingUrl.java
javac -parameters -cp "temp" -d temp framework/java/etu1938/framework/annotations/Singleton.java
javac -parameters -cp "temp" -d temp framework/java/etu1938/framework/annotations/Session.java
javac -parameters -cp "temp" -d temp framework/java/etu1938/framework/annotations/User.java
javac -parameters -cp "temp" -d temp framework/java/etu1938/framework/core/Mapping.java
javac -parameters -cp "temp" -d temp framework/java/etu1938/framework/core/ModelView.java
javac -parameters -cp "temp" -d temp framework/java/etu1938/framework/tools/File_class.java
javac -parameters -cp "temp:$tomcat_libs:$libs" -d temp framework/java/etu1938/framework/servlet/FrontServlet.java
javac -parameters -cp "temp" -d temp framework/java/etu1938/framework/tools/Connexion.java



# Framework Class -> jar
jar --create --verbose --file test-framework/webapp/WEB-INF/lib/framework.jar -C temp .
rm -rf temp

# War structure
mkdir temp
cp -r test-framework/webapp/* temp/
# Compile test
framework_jar="temp/WEB-INF/lib/framework.jar"
javac -parameters -cp "temp/WEB-INF/classes:$framework_jar" -d temp/WEB-INF/classes test-framework/java/objets/Objet.java
# Project Class -> war
jar --create --verbose --file "$tomcat_webapps"/framework.war -C temp .
rm -rf temp