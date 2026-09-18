# Dependências utilizadas

Lombok, WebMVC, Validator, DevTools

## Lombok

O lombok cria os getters e setters automaticamente em classes marcadas com ``@Data``
O lombok também deixa criar construtor para todos argumentos com ``@AllArgsContructor``

## WebMVC

Possibilita criar componentes e métodos que aceitam verbos REST:

```java
// T é o tipo de saída
// R é o tipo de entrada

    @GetMapping("/{id}")
    public ResponseEntity<T> get(@PathVariable @Valid R request){
      T response = new T();
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/")
    public ResponseEntity<T> post(@PathVariable @Valid R request){
      T response = new T();
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<T> get(@PathVariable @Valid R id){
      T response = new T();
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> get(
      @PathVariable @Valid R id 
      @RequestBody R request
    ){
      T response = new T();
      return ResponseEntity.status(HttpStatus.OK).body(response);
    }

```

## Validator

O validator checa os valores abaixo e emitem um erro caso o valor não condizer com os parâmetros, por exemplo

```java

@NotBlank
User user //Usuário não pode estar vazio

@PastOrPresent
LocalDateTime time // Não pode ser um tempo futuro

@Email
String email // Automaticamente valida se é um endereço de email



```

## SpringData

Permite a interação com a api de Persistencia em banco de dados do Java. Precisa do Driver de banco.

## Driver do Banco

Realiza a conexão com o banco de dados, podendo ser de vários tipos, para configurar adicione no application.properties a classe daquele Driver

ex:

- MariaDB: ``spring.datasource.driver-class-name=org.mariadb.jdbc.Driver``
- MySQL: ``spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver``

## Devtools

Ferramentas do desenvolvedor, que por exemplo, detectam mudanças no código fonte e reiniciam o servidor criado


## SpringDoc

O spring doc cria uma página web segundo os padrões do Swagger. Essa página detalha os verbos disponíbilizados pelo servidor e deixa testar as requisições. Essa pagina está em:

```
http://localhost:8080/swagger-ui/index.html
```
