# 1. ESTÁGIO DE CONSTRUÇÃO (BUILD STAGE)
# Usa uma imagem base que possui o JDK e o Maven para compilar o código.
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo de configuração do Maven (pom.xml)
COPY pom.xml .

# Copia todo o código fonte
COPY src ./src

# Empacota a aplicação, pulando os testes
RUN mvn package -DskipTests

# 2. ESTÁGIO DE EXECUÇÃO (RUNNING STAGE)
# Usa uma imagem base mínima que possui apenas o JRE (mais leve e seguro).
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Expõe a porta que o Spring Boot usa (8080)
EXPOSE 8080

# Copia o JAR do estágio de construção para o estágio de execução
COPY --from=build /app/target/trabalho_final_APS_2-0.0.1-SNAPSHOT.jar app.jar

# Comando para iniciar a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]