# React Básico

O React é um framework we deixa utilizar o jsx, um tipo de arquivo e linguagem que mescla o html e css com o javascript.

O react moderno possibilita redesenhar a tela segundo regras de código, fazendo designs interativos  e reutilizáveis

## Inicializar

Para inicializar o React nos computadores do laboratório, seu utiliza o npm, Node Package Manager

```cmd
npm install create-react-app
```

Isso irá criar um projeto react

NOTA: O vite não funciona nas máquinas do laboratorio, deve se utilizar o create react app por causa do node desatualizado.

## Estrutura de arquivos

O código fonte jsx está na pasta src. Uma sugestão de estrutura dentro dessa pasta é:

- components: partes reutilizáveis
- pages: páginas
- services: conexão com apis
- config: configurações

## Código

Um componente sempre JSX retorna uma estrutura HTML:

```jsx

const ExampleButton = () => {
  return (
    <button>
    Me Clique
    </button>
  )
}
```

### Estados e Efeitos

Para alterar o código, se utiliza estados (useState) e efeitos (useEffect). Estados irão redesenhar a página quando um valor é alterado e manter os valores em uma variável de estado, efeitos irão redesenhar a tela como consequencia do redesenho da página, especialmente quando uma variável é alterada. Utilizando o mesmo exemplo;

```jsx

const ExampleButton = () => {
  const [label, setLabel] = useState(""); //Alterar essa variável redesenha a tela

   useEffect(() => {
      //...
    }, []); //Uma única vez, essa função é rodadda

    useEffect(() => {
      //...
    }); //Toda vez que a tela é desenhada, essa função é rodadda

  return (
    <button>
    {label}
    </button>
  )
}
```

### Prop

Uma prop, é um valor ou componente que é passado como parâmetro em um outro componente, exemplo:

```jsx
const ExampleHeader = ({label}) => {

  //botão só tem titulo se uma label for adicionada
  return (
    <button>
    {label} 
    </button>
  )
}
```
