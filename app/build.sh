javac -cp /usr/share/tomcat9/lib/servlet-api.jar -d ./WebContent/WEB-INF/classes/ src/main/java/library/orm/*.java src/main/java/library/servlets/*.java src/main/java/library/*.java
jar cfv app.war -C WebContent .
sudo cp app.war /usr/share/tomcat9/webapps
