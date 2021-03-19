FROM java:8

EXPOSE 8181

ADD pawn-blog-web-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java", "-jar", "/app.jar"]
