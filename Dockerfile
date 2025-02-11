# Usa a imagem do OpenJDK 17 (ou a versão que preferir)
FROM openjdk:23-jdk-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo JAR gerado para dentro do contêiner
COPY target/*.jar app.jar

# Define o comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]