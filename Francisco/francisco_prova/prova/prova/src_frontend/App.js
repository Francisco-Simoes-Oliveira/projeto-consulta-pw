
import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';

import Home from "./pages/Home"
import Calculadora from "./pages/Calculadora"

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
            <Route path='/' element={<Home />}> </Route>
            <Route path='/calculadora' element={<Calculadora />}></Route>
        </Routes>
      </BrowserRouter>

    </div>
  );
}

export default App;
