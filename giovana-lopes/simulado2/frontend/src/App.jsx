import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import AtividadeHome from './pages/AtividadeHome';
import AtividadeCalculo from './pages/AtividadeCalculo';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<AtividadeHome />} />
        <Route path="/calculo" element={<AtividadeCalculo />} />
      </Routes>
    </BrowserRouter>
  );
}
