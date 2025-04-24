# sistema-alvaro-devops

## Sistema alvaro - DevOps

### 1. Descripción General

Este repositorio contiene el código fuente y la configuración DevOps para el **Sistema alvaro devops**.

Este sistema gestiona el proceso de matrícula académica, permitiendo a los estudiantes inscribirse en asignaturas, y a los administradores gestionar usuarios y asignaturas disponibles.

El sistema está diseñado siguiendo una arquitectura de microservicios para facilitar la escalabilidad, mantenibilidad y despliegue independiente de sus componentes.

### 2. Microservicios de Negocio

El sistema se compone de los siguientes microservicios principales (ubicados en la carpeta raíz del proyecto):

- **`/usuarios-servicio`**: Responsable de la gestión de usuarios (estudiantes, profesores, administradores). Implementa operaciones CRUD y es la base para la autenticación y autorización (a ser implementadas).
- **`/asignaturas-servicio`**: Gestiona la información relacionada con las asignaturas o cursos ofrecidos (creación, consulta, actualización, eliminación de asignaturas, pensum, etc.).
- **`/matriculas-servicio`**: Maneja el proceso de matriculación o inscripción de estudiantes en las asignaturas. Interactúa con los servicios de usuarios y asignaturas utilizando comunicación inter-servicio.
- **`/gateway-servicio`** (Estructura base): Servirá como el punto de entrada único (API Gateway) para todas las solicitudes externas, enrutando las peticiones a los microservicios correspondientes.

### 3. Componentes de Infraestructura

Además de los microservicios de negocio, se utilizan los siguientes componentes de infraestructura, también gestionados dentro de este repositorio o configurados externamente:

- **`/eureka-server`**: Servidor de descubrimiento de servicios (Service Discovery) centralizado. Los microservicios se registran en él y lo consultan para encontrarse dinámicamente.
- **`/config-server`**: Servidor centralizado para gestionar la configuración externa de todos los microservicios, obteniendo las propiedades desde un repositorio Git externo.
- **Bases de Datos MongoDB**: Se utiliza una instancia separada de MongoDB para cada microservicio de negocio (`usuarios_db`, `asignaturas_db`, `matriculas_db`) para mantener el acoplamiento bajo a nivel de datos.
- **Repositorio de Configuración Externa**: Un repositorio separado en GitHub (`https://github.com/acbastidas/microservices-config.git`) almacena los archivos de configuración (`.properties`) para cada microservicio, que son leídos por el `config-server`.

### 4. Arquitectura

La arquitectura general del sistema sigue un patrón de microservicios con componentes de soporte:

1.  **Service Discovery (Eureka)**: Los microservicios se registran en el `eureka-server` al iniciar. Otros servicios consultan a Eureka para encontrar las instancias activas de los servicios que necesitan.
2.  **Centralized Configuration (Config Server)**: Al iniciar, los microservicios cliente (`usuarios-servicio`, `asignaturas-servicio`, `matriculas-servicio`, `gateway-servicio`) se conectan al `config-server` (que encuentran vía Eureka) para obtener su configuración específica desde el repositorio Git externo.
3.  **API Gateway**: Las solicitudes externas entrarán por el `gateway-servicio` y serán enrutadas al microservicio de negocio adecuado.
4.  **Inter-service Communication**: Los microservicios se comunican entre sí (ej: `matriculas-servicio` llama a `usuarios-servicio` y `asignaturas-servicio`) utilizando Feign Clients y el descubrimiento de servicios de Eureka.
5.  **Database per Service**: Cada microservicio de negocio tiene su propia base de datos MongoDB dedicada.

![Diagrama de Arquitectura (Ejemplo simple)](link-aqui-si-tienes-un-diagrama.png)
_(Reemplaza 'link-aqui-si-tienes-un-diagrama.png' por el enlace a un diagrama si lo creas)_

### 5. Tecnologías Utilizadas

- **Lenguaje y Framework**: Java con Spring Boot.
- **Spring Cloud Components**: Eureka (Service Discovery), Config Server, OpenFeign (Comunicación Inter-servicio).
- **Base de Datos**: MongoDB con Spring Data MongoDB.
- **Contenerización**: Docker.
- **Orquestación Local**: Docker Compose (para desarrollo y pruebas locales).
- **Herramienta de Construcción**: Apache Maven.
- **Pruebas Automatizadas**: JUnit 5, Mockito, Spring Boot Test (`@WebMvcTest`, `@DataMongoTest`).
- **Gestión de Configuración Externa**: Git y Spring Cloud Config Server.

### 6. Estructura del Repositorio

```bash
.
├── asignaturas-servicio/     # Código fuente del microservicio de asignaturas
├── config-server/            # Código fuente del servidor de configuración
├── eureka-server/            # Código fuente del servidor de descubrimiento
├── gateway-servicio/         # Código fuente del API Gateway
├── matriculas-servicio/      # Código fuente del microservicio de matrículas
├── usuarios-servicio/        # Código fuente del microservicio de usuarios
├── docker-compose.yml        # Archivo para orquestar servicios con Docker Compose (a definir)
├── pom.xml                   # Archivo POM padre de Maven para el proyecto multi-módulo
├── .gitignore                # Archivos y carpetas ignorados por Git
└── README.md                 # Este archivo
(Nota: El repositorio de configuración externa microservices-config es independiente y no se incluye en esta estructura de carpetas principal).

7. Configuración Externa
Las configuraciones específicas para cada microservicio y entorno se gestionan de forma centralizada en el siguiente repositorio de GitHub, que es leído por el config-server:

https://github.com/acbastidas/microservices-config.git

Cada archivo .properties en este repositorio corresponde a un microservicio (ej: usuarios-servicio.properties, application.properties para configuración compartida).

8. Cómo Empezar (Ejecución Local)
Pre-requisitos
Asegúrate de tener instalado lo siguiente:

Java Development Kit (JDK) [Especifica la versión que usaste, ej: JDK 17]
Apache Maven [Especifica la versión que usaste]
Docker Desktop (incluye Docker Engine y Docker Compose)
Git
Levantar el Entorno Local con Docker Compose
(Este paso asume que ya tienes un archivo docker-compose.yml completo que defina todos los servicios: MongoDBs, Eureka, Config Server, Microservicios y Gateway).

Clona el repositorio principal: git clone https://github.com/acbastidas/sistema-alvaro-devops.git (Asegúrate de usar la URL correcta de tu repositorio principal)
Navega a la carpeta del proyecto: cd sistema-alvaro-devops
(Opcional si aún no lo tienes) Clona o actualiza tu repositorio de configuración externa. Asegúrate de que el config-server esté apuntando a este repositorio clonado o al remoto en GitHub. git clone https://github.com/acbastidas/microservices-config.git ../microservices-config (La ubicación puede variar, asegúrate que coincida con la config del config-server)
Construye las imágenes Docker de tus microservicios (esto compilará el código y creará las imágenes): mvn clean package -DskipTests docker-compose build (Nota: -DskipTests salta las pruebas durante el package, si quieres ejecutarlas, omite este flag)
Levanta todos los servicios definidos en docker-compose.yml. Importante: El orden en el archivo docker-compose.yml o la configuración de dependencias (depends_on) es crucial (MongoDBs -> Eureka -> Config Server -> Microservicios -> Gateway). docker-compose up -d (-d para ejecutar en segundo plano)
Verifica que los contenedores se hayan levantado correctamente con docker-compose ps.
Puedes verificar el registro de servicios en el Dashboard de Eureka (generalmente en http://localhost:8761).
Ejecutar Microservicios Individualmente (Sin Docker Compose)
Útil para desarrollo y depuración de un solo servicio.

Asegúrate de que Eureka Server y Config Server estén corriendo (puedes levantarlos con Docker Compose o ejecutarlos individualmente con mvn spring-boot:run). Si corres las BDs fuera de Docker Compose, asegúrate también de que estén activas.
Navega a la carpeta del microservicio que deseas ejecutar: cd sistema-alvaro-devops/usuarios-servicio (o el módulo correspondiente)
Ejecuta el microservicio. Si usas el perfil local (para conectar a Mongo en localhost en lugar del nombre del servicio de Docker Compose), actívalo: mvn spring-boot:run -Dspring-boot.run.profiles=local (Asegúrate de que tu application-local.properties o @TestPropertySource use localhost para la BD si la corres localmente fuera de Docker Compose).
Repite para otros microservicios según sea necesario.
Probar Endpoints
Utiliza una herramienta como Postman para enviar peticiones a los endpoints de tus microservicios.
Las URLs base dependerán de si estás usando el Gateway o accediendo a los servicios directamente, y si corren localmente o en Docker Compose.
Si usas Gateway (asumiendo puerto 8080): http://localhost:8080/[nombre-servicio]/[ruta] (ej: http://localhost:8080/usuarios/users)
Si accedes directo al microservicio local (sin Gateway): http://localhost:[puerto-servicio]/[ruta] (ej: http://localhost:8081/users para usuarios)
Ejecutar Pruebas Automatizadas
Para ejecutar las pruebas de un módulo específico (ej: usuarios-servicio): cd sistema-alvaro-devops/usuarios-servicio mvn clean test -e (El flag -e muestra mas detalles si hay errores)
Para ejecutar las pruebas de todos los módulos desde la raíz: cd sistema-alvaro-devops mvn clean test -e

9. Pruebas Automatizadas y CI/CD
El sistema incluye pruebas unitarias y de integración automatizadas para los microservicios, con cobertura en capas de controlador, servicio y repositorio. Las pruebas utilizan:

JUnit 5 y Mockito para pruebas unitarias.

Spring Boot Test con anotaciones como @WebMvcTest y @DataMongoTest para pruebas de integración.

Pruebas ejecutadas con mvn test y validadas tanto localmente como en el pipeline CI.

Pruebas realizadas (abril 2025)
Se completaron las pruebas en los siguientes módulos:

✅ usuarios-servicio: Controlador, Servicio y Repositorio cubiertos con tests automatizados.

✅ Pruebas de integración usando @WebMvcTest y simulación de requests HTTP.

✅ Conectividad entre servicios verificada vía Postman (matriculas-servicio consume usuarios-servicio y asignaturas-servicio correctamente).

✅ Se valida que las matrículas se creen correctamente ingresando IDs válidos de usuario y asignatura.

Pipeline CI/CD
Se ha configurado un pipeline básico de integración continua en GitHub Actions (.github/workflows/test.yml) que:

Verifica que el proyecto compile correctamente.

Ejecuta los tests unitarios e integración en cada push o pull request a la rama main.

🎥 También se está generando una grabación de video como evidencia de funcionamiento exitoso del sistema completo con Docker + Postman.

10. Próximos Pasos
Implementar seguridad con Spring Security + JWT.

Automatizar despliegue con DockerHub + CI/CD completo.

Desplegar en la nube (Azure, AWS o Render).

Agregar monitoreo centralizado con Prometheus + Grafana.

Añadir documentación Swagger/OpenAPI por microservicio.
```
