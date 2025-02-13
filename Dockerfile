# Usa uma imagem oficial do Maven para compilar o projeto
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia os arquivos do projeto e realiza o build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Usa uma imagem menor para rodar a aplicação
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copia o JAR gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["java", "-jar", "app.jar"]