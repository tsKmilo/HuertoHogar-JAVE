# Etapa 1: Compilación de la aplicación con Maven y JDK 21
# Usamos una imagen que contiene Maven y la versión de Java (Temurin 21) especificada en tu pom.xml
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar solo el pom.xml para aprovechar el cache de capas de Docker
# y descargar las dependencias solo cuando el pom.xml cambie.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el resto del código fuente y compilar la aplicación
COPY src ./src
RUN mvn package -DskipTests

# Etapa 2: Creación de la imagen final de ejecución
# Usamos una imagen JRE (Java Runtime Environment) ligera, ya que no necesitamos el JDK completo para ejecutar.
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar el JAR compilado desde la etapa de 'build'
COPY --from=build /app/target/MCore-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que corre la aplicación Spring Boot (por defecto 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar solo el pom.xml para aprovechar el cache de capas de Docker
# y descargar las dependencias solo cuando el pom.xml cambie.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el resto del código fuente y compilar la aplicación
COPY src ./src
RUN mvn package -DskipTests

# Etapa 2: Creación de la imagen final de ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el JAR compilado desde la etapa de 'build'
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto en el que corre la aplicación Spring Boot (por defecto 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]