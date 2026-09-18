import React, { useState, useEffect } from 'react';
import Header from '../components/Header';
import atividadeService from '../services/atividadeService';
import { Play, Save, Trash2, CheckCircle, AlertCircle } from 'lucide-react';

export default function AtividadeCalculo() {
  const [distancia, setDistancia] = useState('');
  const [tempo, setTempo] = useState('');
  const [resultadoCalculo, setResultadoCalculo] = useState(null);
  const [registros, setRegistros] = useState([]);
  const [loading, setLoading] = useState(false);
  const [feedback, setFeedback] = useState(null);

  useEffect(() => {
    carregarRegistros();
  }, []);

  const carregarRegistros = async () => {
    try {
      const dados = await atividadeService.buscarTodos();
      setRegistros(dados);
    } catch (err) {
      setFeedback({ tipo: 'erro', texto: 'Erro ao carregar registros de atividades.' });
    }
  };

  const validarEntrada = () => {
    const d = parseFloat(distancia);
    const t = parseFloat(tempo);

    if (isNaN(d) || d <= 0) {
      setFeedback({ tipo: 'erro', texto: 'Distância inválida: deve ser maior que zero (ex.: 5 km).' });
      return null;
    }
    if (isNaN(t) || t <= 0) {
      setFeedback({ tipo: 'erro', texto: 'Tempo inválido: deve ser maior que zero (ex.: 30 min).' });
      return null;
    }
    return { distancia: d, tempo: t };
  };

  // Botão Calcular: envia dados ao backend (POST /atividades/calcular)
  const handleCalcular = async () => {
    const payload = validarEntrada();
    if (!payload) return;

    setLoading(true);
    setFeedback(null);
    try {
      const res = await atividadeService.calcular(payload);
      setResultadoCalculo(res);
      setFeedback({ tipo: 'sucesso', texto: 'Cálculo e classificação concluídos pelo backend!' });
    } catch (err) {
      setFeedback({ tipo: 'erro', texto: 'Falha ao processar cálculo no backend.' });
    } finally {
      setLoading(false);
    }
  };

  // Botão Salvar: persiste no banco (POST /atividades)
  const handleSalvar = async () => {
    const payload = validarEntrada();
    if (!payload) return;

    setLoading(true);
    try {
      await atividadeService.inserir(payload);
      setFeedback({ tipo: 'sucesso', texto: 'Atividade salva com sucesso no banco de dados!' });
      carregarRegistros();
    } catch (err) {
      setFeedback({ tipo: 'erro', texto: 'Erro ao salvar atividade no banco de dados.' });
    } finally {
      setLoading(false);
    }
  };

  // Botão Limpar Tabela: exclui todos os registros no banco (DELETE /atividades)
  const handleLimparTabela = async () => {
    if (!window.confirm('Deseja realmente excluir todos os registros no banco de dados?')) {
      return;
    }
    try {
      await atividadeService.excluirTodos();
      setRegistros([]);
      setResultadoCalculo(null);
      setFeedback({ tipo: 'sucesso', texto: 'Todos os registros foram excluídos do banco de dados.' });
    } catch (err) {
      setFeedback({ tipo: 'erro', texto: 'Erro ao limpar registros no banco.' });
    }
  };

  const getFaixaBadgeClass = (faixa) => {
    if (faixa === 'Caminhada') return 'badge-caminhada';
    if (faixa === 'Trote') return 'badge-trote';
    return 'badge-corrida';
  };

  return (
    <div className="page-wrapper">
      {/* Requisito: Header usado em todas as páginas com o título parametrizável ("Formulário") */}
      <Header titulo="Monitoramento de Atividade Física" subtitulo="Formulário" />

      <main className="container main-content">
        <div className="window-card">
          <div className="window-bar">
            <span className="window-tab">Atividade</span>
            <div className="window-controls">
              <span className="btn-win"></span>
              <span className="btn-win"></span>
              <span className="btn-win"></span>
            </div>
          </div>

          <div className="window-body">
            {feedback && (
              <div className={`feedback-box ${feedback.tipo}`}>
                {feedback.tipo === 'sucesso' ? <CheckCircle size={16} /> : <AlertCircle size={16} />}
                {feedback.texto}
              </div>
            )}

            {/* Campos de entrada */}
            <div className="form-layout">
              <div className="form-group">
                <label>Distância percorrida (em km):</label>
                <input 
                  type="number" 
                  step="0.01"
                  value={distancia}
                  onChange={(e) => setDistancia(e.target.value)}
                  placeholder="Ex: 5.0"
                />
              </div>

              <div className="form-group">
                <label>Tempo gasto (em minutos):</label>
                <input 
                  type="number" 
                  step="0.1"
                  value={tempo}
                  onChange={(e) => setTempo(e.target.value)}
                  placeholder="Ex: 30"
                />
              </div>

              <div className="form-buttons-row">
                <button onClick={handleCalcular} className="btn-green-main" disabled={loading}>
                  <Play size={16} /> {loading ? 'Calculando...' : 'Calcular'}
                </button>
              </div>

              {resultadoCalculo && (
                <div className="calc-result-box">
                  Velocidade Média: <strong>{resultadoCalculo.velocidadeMedia} km/h</strong> &nbsp;|&nbsp; 
                  Classificação: <span className={`badge-pill ${getFaixaBadgeClass(resultadoCalculo.classificacao)}`}>
                    {resultadoCalculo.classificacao}
                  </span>
                </div>
              )}

              <div className="form-buttons-row">
                <button onClick={handleSalvar} className="btn-green-secondary" disabled={loading}>
                  <Save size={16} /> Salvar
                </button>
              </div>
            </div>

            {/* Tabela de Registros com todas as colunas solicitadas */}
            <div className="table-wrapper">
              <div className="table-title">TABELA DE REGISTROS DE ATIVIDADES</div>
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Distância (km)</th>
                    <th>Tempo (min)</th>
                    <th>Velocidade (km/h)</th>
                    <th>Classificação</th>
                    <th>Data/Hora</th>
                  </tr>
                </thead>
                <tbody>
                  {registros.length > 0 ? (
                    registros.map((item) => (
                      <tr key={item.id}>
                        <td>{item.distancia}</td>
                        <td>{item.tempo}</td>
                        <td className="text-bold">{item.velocidadeMedia} km/h</td>
                        <td>
                          <span className={`badge-pill ${getFaixaBadgeClass(item.classificacao)}`}>
                            {item.classificacao}
                          </span>
                        </td>
                        <td>{item.dataHora ? new Date(item.dataHora).toLocaleString('pt-BR') : '-'}</td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="5" className="empty-row">Nenhuma atividade registrada ainda.</td>
                    </tr>
                  )}
                </tbody>
              </table>

              <div className="table-actions">
                <button onClick={handleLimparTabela} className="btn-red-clear">
                  <Trash2 size={15} /> Limpar Tabela
                </button>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
