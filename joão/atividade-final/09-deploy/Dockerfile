# Vai na raiz do backend (do lado do pom.xml).
#
# São duas etapas ("multi-stage"):
#   1) uma imagem com Maven compila o projeto e gera o .jar;
#   2) uma imagem só com Java roda esse .jar.
# Assim a imagem final fica pequena (não leva o Maven junto).

# ---------- etapa 1: compilar ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# copia só o pom primeiro: se ele não mudar, o Docker reaproveita as dependências baixadas
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- etapa 2: executar ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Render, Railway e afins mandam a porta na variável PORT.
# Se ela não existir (rodando local), usa 8080.
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]
