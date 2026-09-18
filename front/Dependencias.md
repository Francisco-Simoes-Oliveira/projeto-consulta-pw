# Dependências do React

react-router-dom, axios

## React Router

O router deixa a movimentação entre páginas sem o flash branco entre as áginas (chamado de flash of unstyled content). Para utilizá-lo no App.jsx deve se adicionar as rotas do seu aplicativo:

```jsx
const App = () => {
    return (

      <BrowserRouter>
          <Routes>
            <Route path="a" element={<ExemploA/>}>
            </Route>
            <Route path="b" element={<ExemploB/>}>
            <Route>
            <Route path="c" element={<ExemploC/>}>
            </Route>
          </Routes>
      </BrowserRouter>

    );
}

export default App;

```

Para então navegar entre as rotas, se usa o useNavigate, e passa o caminho para aquela rota

```jsx
const ExampleButton = () => {
  const navigate = useNavigate();

  return <button onClick={() => navigate("/a")}>Navegar para A</button>;
};
```

## Axios

O axios facilita a realização de requisições HTTP:

```js

import api from '../config/AxiosConfig';

abstract class BaseService {

    const endPoint: string;


    constructor(endPoint) {
        this.endPoint = endPoint;
    }

    async insertIn(target, data) {
        return await api.post(this.endPoint + target, data);
    }

    async insert(data) {
        return await api.post(this.endPoint, data);
    }

    async update(data) {
        return await api.put(this.endPoint, data);
    }

    async delete(id) {
        return await api.delete(`${this.endPoint}/${id}`);
    }

    async getAll() {
        return await api.get(this.endPoint);
    }
}

export default BaseService;

```

Dessa classe base é possivel inicializar outras classes para outros endpoints, utilizando um exemplo singleton:

```js
import BaseService from "./BaseService";

//Classe singleton para extender a classe service para outros endpoints
class ExampleService extends BaseService{

  static #instance: ExampleService;

  private constructor(){
    super('/example');
  }

  public static get instance(){
    if(!ExampleService.#instance){
      this.#instance = new ExampleService();
    }

    return this.#instance;
  }
}

export default ExampleService;

```
