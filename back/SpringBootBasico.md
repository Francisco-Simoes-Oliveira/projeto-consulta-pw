
# Spring Boot Básico

O Spring Boot é um framework que facilita o desenvolvimento do java.
A idéia básica é de marcar partes do código com anotações, que são manuseadas pelo spring boot, que age de maneira refletiva com o código.

Instala as dependências com ``./mvnw clean install``
Inicia o Spring com o comando ``./mvnw spring-boot:run``

O Spring manuseia Beans, e somente beans, beans são classes marcadas com os seguintes valores

```java
@Bean // Genérico
@Controller // Representa a entrada do Restful
@Repository // Representa a conexão com banco de dados
@Service // Representa serviços que alteram os dados para o retorno
@Entity // Representam entidades e modelos lógicos
```

## Autowired

A anotação autowired faz com que o Spring Boot gerencie os objetos pra você, marcar um objeto que seja um Bean com ``@Autowired`` automaticamente cria o objeto pra ele

```java
  @Autowired
  String hello;
```

IMPORTANTE: PRECISA SER UM BEAN ou seja precisa estar com as anotações acima!!!

## Estrutura

O projeto é dividido nessas pastas:

- controller: entrada e saída do HTTP
- service: manipulação de dados
- repository: conexão com o banco
- entity: modelos de tabelas do banco de dados
- dto: objetos de reposta e requisição

### Classe Controller

```java

@RestController
@RequestMapping("/url")
@CorsOrigin
public class ExampleController{

  @Autowired
  ExampleService service;

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
}
```

### Classe Entity

```java

@Data
@Entity
class Example{

  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "example_id")
    private Long id;

  @NotBlank (message = "Nome não pode estar vazio")
  private String name;

  @PastOrPresent(message = "Data de nascimento não deve ser futura")
  private LocalDateTime dateOfBirth;

  @Email(message = "Email inválido")
  private String email;

}
```

### Classe repository

```java
@Repository
public interface ExampleRepository extends JpaRepository<Long, Example>{
  // Primeiro tipo é o tipo do is, pode ser UUID; segundo é a entidade
}
```

### Classe service

```java

@Service
public class ExampleService{
  @Autowired
  ExampleRepository repository;

  public Example findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Exemplo não encontrado"));
    }

    public Example insert(@NonNull Example ex) {
        return repository.save(ex);
    }

    public void delete(Long id) {
        repository.delete(findById(id));
    }

    public Example update(@NonNull Example ex) {
        Example exdb = findById(ex.getId());

        exdb.setName(ex.getName());
        exdb.setEmail(ex.getEmail());
        exdb.setDateOfBirth(ex.getDateOfBirth());

        return repository.save(exdb);
    }

    public List<Example> listAll() {
        return repository.findAll();
    }
}
```
