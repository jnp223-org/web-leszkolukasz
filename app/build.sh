javac -cp /usr/share/tomcat9/lib/servlet-api.jar -d ./WebContent/WEB-INF/classes/ src/main/java/servlets/Home.java
jar cfv app.war -C WebContent .
sudo cp app.war /var/lib/tomcat9/webapps
