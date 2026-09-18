import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Landmark, Home, Calculator } from 'lucide-react';

/**
 * Requisito PDF 1:
 * - O componente "Header" deve ser reutilizável entre esta página e a página de cálculo.
 * - O texto "Formulário" / "Listagem" no título deve ser parametrizado e reutilizável.
 */
export default function Header({ titulo = 'Cálculo de Investimento', subtitulo = '' }) {
  const location = useLocation();

  return (
    <header className="app-header">
      <div className="header-container">
        <div className="header-branding">
          <Landmark size={28} color="#15803d" />
          <h1 className="header-title">
            {titulo}
            {subtitulo && <span className="header-subtitle-param"> - {subtitulo}</span>}
          </h1>
        </div>

        <nav className="header-nav">
          <Link to="/" className={`nav-link ${location.pathname === '/' ? 'active' : ''}`}>
            <Home size={16} /> Home
          </Link>
          <Link to="/calculo" className={`nav-link ${location.pathname === '/calculo' ? 'active' : ''}`}>
            <Calculator size={16} /> Cálculo & Listagem
          </Link>
        </nav>
      </div>
    </header>
  );
}
