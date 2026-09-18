import React, { useState, useEffect } from 'react';
import Header from '../components/Header';
import investimentoService from '../services/investimentoService';
import { Calculator, Save, Trash2, Search } from 'lucide-react';

export default function InvestimentoCalculo() {
  const [form, setForm] = useState({ valorInicial: '', prazoMeses: '', juroMensal: '' });
  const [resultadoCalculo, setResultadoCalculo] = useState(null);
  const [registros, setRegistros] = useState([]);
  const [filtroData, setFiltroData] = useState('');
  const [filtroTermo, setFiltroTermo] = useState('');
  const [loading, setLoading] = useState(false);
  const [feedback, setFeedback] = useState(null);

  useEffect(() => {
    carregarRegistros();
  }, []);

  const carregarRegistros = async () => {
    try {
      const dados = await investimentoService.buscarTodos();
      setRegistros(dados);
    } catch (err) {
      setFeedback({ tipo: 'erro', texto: 'Erro ao carregar cálculos salvos do backend.' });
    }
  };

  const handleInputChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const validarCampos = () => {
    const v = parseFloat(form.valorInicial);
    const p = parseInt(form.prazoMeses);
    const j = parseFloat(form.juroMensal);

    if (isNaN(v) || v <= 0) {
      setFeedback({ tipo: 'erro', texto: 'O valor inicial deve ser um número maior que zero.' });
      return null;
    }
    if (isNaN(p) || p <= 0) {
      setFeedback({ tipo: 'erro', texto: 'O prazo em meses deve ser de pelo menos 1 mês.' });
      return null;
    }
    if (isNaN(j) || j <= 0) {
      setFeedback({ tipo: 'erro', texto: 'A taxa de juros mensal deve ser maior que zero.' });
      return null;
    }
    return { valorInicial: v, prazoMeses: p, juroMensal: j };
  };

  // Botão Calcular: requisição ao backend (O Cálculo NÃO é realizado no frontend)
  const handleCalcular = async () => {
    const payload = validarCampos();
    if (!payload) return;

    setLoading(true);
    setFeedback(null);
    try {
      const res = await investimentoService.calcular(payload);
      setResultadoCalculo(res.valorFinal);
    } catch (err) {
      setFeedback({ tipo: 'erro', texto: 'Erro na comunicação com o backend ao calcular.' });
    } finally {
      setLoading(false);
    }
  };

  // Botão Salvar: persiste no banco de dados e exibe na tabela
  const handleSalvar = async () => {
    const payload = validarCampos();
    if (!payload) return;

    setLoading(true);
    try {
      await investimentoService.inserir(payload);
      setFeedback({ tipo: 'sucesso', texto: 'Cálculo salvo com sucesso no banco de dados!' });
      carregarRegistros();
    } catch (err) {
      setFeedback({ tipo: 'erro', texto: 'Erro ao salvar cálculo no banco de dados.' });
    } finally {
      setLoading(false);
    }
  };

  // Botão Limpar Dados: exclui todos os registros correspondentes no banco
  const handleLimparDados = async () => {
    if (!window.confirm('Deseja realmente excluir todos os registros da tabela no banco de dados?')) {
      return;
    }
    try {
      await investimentoService.excluirTodos();
      setRegistros([]);
      setResultadoCalculo(null);
      setFeedback({ tipo: 'sucesso', texto: 'Todos os registros foram excluídos com sucesso!' });
    } catch (err) {
      setFeedback({ tipo: 'erro', texto: 'Erro ao limpar dados no banco.' });
    }
  };

  const handlePesquisarData = async () => {
    if (!filtroData) {
      carregarRegistros();
      return;
    }
    try {
      const dados = await investimentoService.filtrarPorData(filtroData);
      setRegistros(dados);
    } catch (err) {
      setFeedback({ tipo: 'erro', texto: 'Erro ao pesquisar por data.' });
    }
  };

  const handlePesquisarTermo = async () => {
    if (!filtroTermo) {
      carregarRegistros();
      return;
    }
    try {
      const dados = await investimentoService.filtrarPorTermo(filtroTermo);
      setRegistros(dados);
    } catch (err) {
      setFeedback({ tipo: 'erro', texto: 'Erro ao pesquisar por prazo ou juro.' });
    }
  };

  return (
    <div className="page-wrapper">
      {/* Requisito: A palavra "Listagem" é passada por parâmetro */}
      <Header titulo="Cálculo de Investimento" subtitulo="Listagem" />

      <main className="container main-content">
        <div className="window-card">
          <div className="window-bar">
            <span className="window-tab">Calculo</span>
            <div className="window-controls">
              <span className="btn-win"></span>
              <span className="btn-win"></span>
              <span className="btn-win"></span>
            </div>
          </div>

          <div className="window-body">
            {feedback && (
              <div className={`feedback-box ${feedback.tipo}`}>
                {feedback.texto}
              </div>
            )}

            {/* Formulário de Entrada */}
            <div className="form-layout">
              <div className="form-group">
                <label>Valor Inicial</label>
                <input 
                  type="number" 
                  name="valorInicial"
                  value={form.valorInicial}
                  onChange={handleInputChange}
                  placeholder="100"
                />
              </div>

              <div className="form-group">
                <label>Prazo em Meses</label>
                <input 
                  type="number" 
                  name="prazoMeses"
                  value={form.prazoMeses}
                  onChange={handleInputChange}
                  placeholder="12"
                />
              </div>

              <div className="form-group">
                <label>Juro Mensal</label>
                <input 
                  type="number" 
                  step="0.01"
                  name="juroMensal"
                  value={form.juroMensal}
                  onChange={handleInputChange}
                  placeholder="5"
                />
              </div>

              <div className="form-buttons-row">
                <button onClick={handleCalcular} className="btn-green-main" disabled={loading}>
                  <Calculator size={16} /> {loading ? 'Calculando...' : 'Calcular'}
                </button>
              </div>

              {resultadoCalculo !== null && (
                <div className="calc-result-box">
                  o valor final será de <strong>{resultadoCalculo.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</strong>
                </div>
              )}

              <div className="form-buttons-row">
                <button onClick={handleSalvar} className="btn-green-secondary" disabled={loading}>
                  <Save size={16} /> Salvar Cálculo
                </button>
              </div>
            </div>

            {/* Filtros da Tabela */}
            <div className="filters-grid">
              <div className="filter-group">
                <label>Filtro por data</label>
                <div className="filter-input-row">
                  <input 
                    type="date" 
                    value={filtroData}
                    onChange={(e) => setFiltroData(e.target.value)}
                  />
                  <button onClick={handlePesquisarData} className="btn-blue-search">
                    <Search size={14} /> Pesquisar
                  </button>
                </div>
              </div>

              <div className="filter-group">
                <label>Filtro por prazo ou juro</label>
                <div className="filter-input-row">
                  <input 
                    type="text" 
                    value={filtroTermo}
                    onChange={(e) => setFiltroTermo(e.target.value)}
                    placeholder="Ex: 12 ou 5"
                  />
                  <button onClick={handlePesquisarTermo} className="btn-blue-search">
                    <Search size={14} /> Pesquisar
                  </button>
                </div>
              </div>
            </div>

            {/* Tabela de Cálculos Realizados */}
            <div className="table-wrapper">
              <div className="table-title">ÚLTIMOS CÁLCULOS REALIZADOS</div>
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Data Cálculo</th>
                    <th>Prazo</th>
                    <th>Juro</th>
                    <th>Valor Final</th>
                  </tr>
                </thead>
                <tbody>
                  {registros.length > 0 ? (
                    registros.map((item) => (
                      <tr key={item.id}>
                        <td>{item.dataCalculo}</td>
                        <td>{item.prazoMeses}</td>
                        <td>{item.juroMensal}</td>
                        <td className="text-bold">
                          {item.valorFinal?.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="4" className="empty-row">Nenhum cálculo registrado ainda.</td>
                    </tr>
                  )}
                </tbody>
              </table>

              <div className="table-actions">
                <button onClick={handleLimparDados} className="btn-red-clear">
                  <Trash2 size={15} /> Limpar Dados
                </button>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
