import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from './pages/Home/Home';
import Calculo from './pages/Calculo/Calculo';
import './App.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/calculo" element={<Calculo />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
