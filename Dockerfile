#=============================================================
# ETAPA DE CONSTRUCCIÓN (BUILD STAGE)
# Compilación de un módulo específico del proyecto multi-módulo
#=============================================================
FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

# Copiamos los POMs
COPY pom.xml ./
COPY config-server/pom.xml config-server/pom.xml
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY gateway-servicio/pom.xml gateway-servicio/pom.xml
COPY usuarios-servicio/pom.xml usuarios-servicio/pom.xml
COPY asignaturas-servicio/pom.xml asignaturas-servicio/pom.xml
COPY matriculas-servicio/pom.xml matriculas-servicio/pom.xml

# Descargamos dependencias
RUN --mount=type=cache,target=/root/.m2/repository mvn dependency:go-offline -B

# Copiamos los códigos fuente
COPY config-server/src config-server/src
COPY eureka-server/src eureka-server/src
COPY gateway-servicio/src gateway-servicio/src
COPY usuarios-servicio/src usuarios-servicio/src
COPY asignaturas-servicio/src asignaturas-servicio/src
COPY matriculas-servicio/src matriculas-servicio/src

# ARGs de compilación
ARG MODULE_NAME=usuarios-servicio
ARG ARTIFACT_ID=usuarios-servicio
ARG VERSION=1.0-SNAPSHOT
ARG SKIP_TESTS=true

# Compilamos solo el módulo especificado
RUN --mount=type=cache,target=/root/.m2/repository mvn clean package -pl ${MODULE_NAME} -am -B -DskipTests=${SKIP_TESTS}

#=============================================================
# ETAPA DE EJECUCIÓN (RUN STAGE)
# Ejecutamos solo el JAR compilado del módulo específico
#=============================================================
FROM openjdk:17-jdk-slim AS runner

WORKDIR /app

ARG MODULE_NAME
ARG ARTIFACT_ID
ARG VERSION
ARG APP_PORT=8081

# Copiamos el JAR resultante desde la etapa de build
COPY --from=builder /app/${MODULE_NAME}/target/${ARTIFACT_ID}-${VERSION}.jar app.jar

EXPOSE ${APP_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
