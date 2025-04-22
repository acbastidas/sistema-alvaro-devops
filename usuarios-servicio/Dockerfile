#=============================================================
# ETAPA DE CONSTRUCCIÓN (BUILD STAGE)
# Compila el JAR dentro de un contenedor
#=============================================================
FROM maven:3.8.1-jdk-8 AS builder

# Establece el directorio de trabajo dentro del contenedor de construcción
WORKDIR /app

# Copia el pom.xml del módulo y el pom.xml del proyecto raíz
COPY pom.xml ./
COPY ../pom.xml ../pom.xml

# Copia el código fuente
COPY src/ src/

# Compila el JAR, sin ejecutar las pruebas
RUN --mount=type=cache,target=/root/.m2/repository mvn clean package -f pom.xml -DskipTests

#=============================================================
# ETAPA DE EJECUCIÓN (RUN STAGE)
# Crea una imagen ligera con el JAR resultante
#=============================================================
FROM openjdk:17-jdk-slim

WORKDIR /app

ARG ARTIFACT_ID=usuarios-servicio
ARG VERSION=1.0-SNAPSHOT
ARG APP_PORT=8081

COPY --from=builder /app/target/${ARTIFACT_ID}-${VERSION}.jar app.jar

EXPOSE ${APP_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
