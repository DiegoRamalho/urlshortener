FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS=""

# add directly the war
ADD *.war /app.war

EXPOSE 8080
CMD java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.war
