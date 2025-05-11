FROM eclipse-temurin:17-jre-alpine
LABEL maintainer="@joseagama"

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el jar generado por Maven al contenedor
COPY target/ticket-api-1.0-SNAPSHOT.jar app.jar

# Expone el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]