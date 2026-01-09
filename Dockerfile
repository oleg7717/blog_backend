FROM tomcat:11.0-jdk21-openjdk-slim

# Удаляем стандартные приложения Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Копируем WAR файл приложения
COPY build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

# Экспортируем порт Tomcat
EXPOSE 8080

CMD ["catalina.sh", "run"]