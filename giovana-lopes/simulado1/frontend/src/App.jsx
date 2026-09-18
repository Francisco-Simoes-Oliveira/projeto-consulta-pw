import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import InvestimentoHome from './pages/InvestimentoHome';
import InvestimentoCalculo from './pages/InvestimentoCalculo';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<InvestimentoHome />} />
        <Route path="/calculo" element={<InvestimentoCalculo />} />
      </Routes>
    </BrowserRouter>
  );
}
