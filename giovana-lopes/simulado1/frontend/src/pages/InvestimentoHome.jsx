import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import { ArrowRight, Calculator } from 'lucide-react';

export default function InvestimentoHome() {
  const navigate = useNavigate();
  const [mensagemAcesso, setMensagemAcesso] = useState('');

  useEffect(() => {
    const agora = new Date();
    const dia = String(agora.getDate()).padStart(2, '0');
    const mes = String(agora.getMonth() + 1).padStart(2, '0');
    const ano = agora.getFullYear();
    const hora = String(agora.getHours()).padStart(2, '0');
    const minuto = String(agora.getMinutes()).padStart(2, '0');

    setMensagemAcesso(`Olá, você acessou esta página dia ${dia}/${mes}/${ano} às ${hora}:${minuto}`);
  }, []);

  return (
    <div className="page-wrapper">
      {/* Componente Header reutilizável */}
      <Header titulo="Cálculo de Investimento" />

      <main className="container main-content">
        <div className="window-card">
          <div className="window-bar">
            <span className="window-tab">Home</span>
            <div className="window-controls">
              <span className="btn-win"></span>
              <span className="btn-win"></span>
              <span className="btn-win"></span>
            </div>
          </div>

          <div className="window-body">
            <h2 className="greeting-text">{mensagemAcesso}</h2>

            <div className="hero-action-box">
              <button 
                onClick={() => navigate('/calculo')} 
                className="btn-green-main"
              >
                <Calculator size={18} />
                Realizar o Calculo de Investimento
                <ArrowRight size={18} />
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
