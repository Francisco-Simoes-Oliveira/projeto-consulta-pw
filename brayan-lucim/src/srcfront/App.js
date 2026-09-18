import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from './pages/Home/Home';
import Investimento from './pages/Investimento/Investimento';

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home/>}/>
          <Route path="/investimento" element={<Investimento />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
