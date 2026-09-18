# Banco e WebSocket — erros comuns

## 1. GET funciona mas phpMyAdmin não mostra os registros

Primeiro verifique `application.properties`.

Se estiver assim:

```properties
spring.datasource.url=jdbc:h2:file:./data/provabase
```

você está usando **H2**, não MySQL. O phpMyAdmin só administra MySQL/MariaDB.

Para usar MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/prova_base?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

E no `pom.xml`:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

## 2. SockJS: Access-Control-Allow-Credentials deve ser true

Se aparecer:

```text
The value of the 'Access-Control-Allow-Credentials' header ... must be 'true'
```

No CORS do Spring Security:

```java
c.setAllowedOrigins(List.of("http://localhost:5173"));
c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
c.setAllowedHeaders(List.of("Authorization", "Content-Type"));
c.setAllowCredentials(true);
```

E no WebSocket:

```java
registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("http://localhost:5173")
        .withSockJS();
```

Depois reinicie o backend e o frontend.
