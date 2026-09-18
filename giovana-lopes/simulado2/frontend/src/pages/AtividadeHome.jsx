import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import { Activity, ArrowRight } from 'lucide-react';

export default function AtividadeHome() {
  const navigate = useNavigate();
  const [dataHoraAbertura, setDataHoraAbertura] = useState('');

  useEffect(() => {
    const agora = new Date();
    const formatado = agora.toLocaleDateString('pt-BR') + ' às ' + 
                      agora.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    setDataHoraAbertura(formatado);
  }, []);

  return (
    <div className="page-wrapper">
      {/* Componente Header reutilizado */}
      <Header titulo="Monitoramento de Atividade Física" />

      <main className="container main-content">
        <div className="card-hub">
          <div className="icon-badge">
            <Activity size={44} color="#059669" />
          </div>

          <h2 className="greeting-text">
            Olá, você acessou esta página em <strong>{dataHoraAbertura}</strong>
          </h2>

          <p className="description-text">
            Sistema de registro e análise de atividades físicas (corrida/caminhada). Calcule sua velocidade média e acompanhe a classificação de desempenho.
          </p>

          <button 
            onClick={() => navigate('/calculo')} 
            className="btn-green-main"
          >
            Registrar Atividade
            <ArrowRight size={18} />
          </button>
        </div>
      </main>
    </div>
  );
}
