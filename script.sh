tomcat_libs="/home/andie/tomcat/lib/*"
tomcat_webapps="/home/andie/tomcat/webapps"

# Compile framework
javac -cp "temp" -d temp framework/java/etu1938/framework/annotations/MappingUrl.java
javac -cp "temp" -d temp framework/java/etu1938/framework/Mapping.java
javac -cp "temp:$tomcat_libs" -d temp framework/java/etu1938/framework/servlet/FrontServlet.java
# Framework Class -> jar
jar --create --verbose --file test-framework/webapp/WEB-INF/lib/framework.jar -C temp .
rm -rf temp

# War structure
mkdir temp
cp -r test-framework/webapp/* temp/
# Compile test
framework_jar="temp/WEB-INF/lib/framework.jar"
javac -cp "temp/WEB-INF/classes:$framework_jar" -d temp/WEB-INF/classes test-framework/java/objets/Objet.java
# Project Class -> war
jar --create --verbose --file "$tomcat_webapps"/framework.war -C temp .
rm -rf temp

