import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Activity, Home, PlayCircle } from 'lucide-react';

/**
 * Requisito PDF 2:
 * - O Header deve ser usado em todas as páginas.
 * - O título da página (ex.: "Formulário") deve ser parametrizável.
 */
export default function Header({ titulo = 'Monitoramento de Atividade Física', subtitulo = '' }) {
  const location = useLocation();

  return (
    <header className="app-header">
      <div className="header-container">
        <div className="header-branding">
          <Activity size={28} color="#059669" />
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
            <PlayCircle size={16} /> Atividade
          </Link>
        </nav>
      </div>
    </header>
  );
}
