import React, { useCallback, useEffect, useState } from "react";
import PadraoLayout from "../../components/PadraoLayout";
import Alerta from "../../components/Alerta";
import ModalConfirmacao from "../../components/ModalConfirmacao";
import InvestimentoService from "../../api/apiInvestimento";
import extrairMensagemErro from "../../api/erro";
import { formatarDataHora, formatarMoeda, formatarPorcentagem, formatarTipoRendimento } from "../../utils/formato";

const investimentoService = new InvestimentoService();

const formularioInicial = {
    valorInicial: "",
    prazoMeses: "",
    taxaMensal: "",
    tipoRendimento: "COMPOSTO",
};

const filtrosIniciais = {
    dataInicio: "",
    dataFim: "",
    prazoMinimo: "",
    prazoMaximo: "",
    taxaMinima: "",
    taxaMaxima: "",
};

const paraNumero = (valor) => {
    return valor === "" ? null : Number(valor);
};

const montarInvestimento = (formulario) => {
    return {
        valorInicial: paraNumero(formulario.valorInicial),
        prazoMeses: paraNumero(formulario.prazoMeses),
        taxaMensal: paraNumero(formulario.taxaMensal),
        tipoRendimento: formulario.tipoRendimento,
    };
};

const montarParametros = (filtros) => {
    const parametros = {};

    Object.keys(filtros).forEach((campo) => {
        if (filtros[campo] !== "") {
            parametros[campo] = filtros[campo];
        }
    });

    return parametros;
};

const Calculo = () => {
    const [formulario, setFormulario] = useState(formularioInicial);
    const [resultado, setResultado] = useState(null);
    const [investimentos, setInvestimentos] = useState([]);
    const [filtros, setFiltros] = useState(filtrosIniciais);
    const [carregando, setCarregando] = useState(true);
    const [mensagem, setMensagem] = useState(null);
    const [confirmacao, setConfirmacao] = useState(null);

    const mostrarErro = (error, mensagemPadrao) => {
        setMensagem({ tipo: "erro", texto: extrairMensagemErro(error, mensagemPadrao) });
    };

    const carregarInvestimentos = useCallback(async (filtrosAtuais) => {
        setCarregando(true);
        try {
            const dados = await investimentoService.listar(montarParametros(filtrosAtuais));
            setInvestimentos(dados);
        } catch (error) {
            setMensagem({
                tipo: "erro",
                texto: extrairMensagemErro(error, "Não foi possível carregar o histórico de simulações."),
            });
        } finally {
            setCarregando(false);
        }
    }, []);

    useEffect(() => {
        carregarInvestimentos(filtrosIniciais);
    }, [carregarInvestimentos]);

    const atualizarFormulario = (evento) => {
        setFormulario({ ...formulario, [evento.target.name]: evento.target.value });
    };

    const atualizarFiltro = (evento) => {
        setFiltros({ ...filtros, [evento.target.name]: evento.target.value });
    };

    const calcular = async () => {
        setMensagem(null);
        try {
            const dados = await investimentoService.calcular(montarInvestimento(formulario));
            setResultado(dados);
        } catch (error) {
            setResultado(null);
            mostrarErro(error, "Não foi possível realizar o cálculo.");
        }
    };

    const salvar = async () => {
        setMensagem(null);
        try {
            const investimentoSalvo = await investimentoService.salvar(montarInvestimento(formulario));
            setResultado({
                valorInicial: investimentoSalvo.valorInicial,
                prazoMeses: investimentoSalvo.prazoMeses,
                taxaMensal: investimentoSalvo.taxaMensal,
                tipoRendimento: investimentoSalvo.tipoRendimento,
                valorFinal: investimentoSalvo.valorFinal,
                rendimento: investimentoSalvo.valorFinal - investimentoSalvo.valorInicial,
            });
            setMensagem({ tipo: "sucesso", texto: "Simulação salva com sucesso!" });
            carregarInvestimentos(filtros);
        } catch (error) {
            mostrarErro(error, "Não foi possível salvar a simulação.");
        }
    };

    const excluir = async (investimento) => {
        setConfirmacao(null);
        setMensagem(null);
        try {
            await investimentoService.excluir(investimento.id);
            setMensagem({ tipo: "sucesso", texto: "Simulação removida com sucesso." });
            carregarInvestimentos(filtros);
        } catch (error) {
            mostrarErro(error, "Não foi possível remover a simulação.");
        }
    };

    const limparTabela = async () => {
        setConfirmacao(null);
        setMensagem(null);
        try {
            await investimentoService.excluirTodos();
            setMensagem({ tipo: "sucesso", texto: "Todas as simulações foram removidas." });
            carregarInvestimentos(filtros);
        } catch (error) {
            mostrarErro(error, "Não foi possível limpar a tabela.");
        }
    };

    const confirmarExclusao = (investimento) => {
        setConfirmacao({
            titulo: "Excluir simulação",
            texto: `Remover a simulação de ${formatarMoeda(investimento.valorInicial)} feita em ${formatarDataHora(investimento.dataCalculo)}?`,
            acao: () => excluir(investimento),
        });
    };

    const confirmarLimpezaTabela = () => {
        setConfirmacao({
            titulo: "Limpar tabela",
            texto: "Todos os registros do histórico serão removidos do banco. Deseja continuar?",
            acao: () => limparTabela(),
        });
    };

    const limparFiltros = () => {
        setFiltros(filtrosIniciais);
        carregarInvestimentos(filtrosIniciais);
    };

    return (
        <PadraoLayout titulo="Cálculo de Investimento - Listagem">
            <Alerta tipo={mensagem ? mensagem.tipo : ""} texto={mensagem ? mensagem.texto : ""} aoFechar={() => setMensagem(null)} />

            <section className="cartao">
                <h2>Dados da simulação</h2>
                <div className="formulario">
                    <div className="campo">
                        <label htmlFor="valorInicial">Valor inicial (R$)</label>
                        <input
                            id="valorInicial"
                            name="valorInicial"
                            type="number"
                            min="0"
                            step="0.01"
                            placeholder="Ex.: 1000"
                            value={formulario.valorInicial}
                            onChange={atualizarFormulario}
                        />
                    </div>
                    <div className="campo">
                        <label htmlFor="prazoMeses">Prazo em meses</label>
                        <input
                            id="prazoMeses"
                            name="prazoMeses"
                            type="number"
                            step="1"
                            placeholder="1 a 600"
                            value={formulario.prazoMeses}
                            onChange={atualizarFormulario}
                        />
                    </div>
                    <div className="campo">
                        <label htmlFor="taxaMensal">Taxa mensal (%)</label>
                        <input
                            id="taxaMensal"
                            name="taxaMensal"
                            type="number"
                            step="0.01"
                            placeholder="0 a 100"
                            value={formulario.taxaMensal}
                            onChange={atualizarFormulario}
                        />
                    </div>
                    <div className="campo">
                        <span className="campo-titulo">Tipo de rendimento</span>
                        <div className="opcoes">
                            <label className="opcao">
                                <input
                                    type="radio"
                                    name="tipoRendimento"
                                    value="SIMPLES"
                                    checked={formulario.tipoRendimento === "SIMPLES"}
                                    onChange={atualizarFormulario}
                                />
                                Juros simples
                            </label>
                            <label className="opcao">
                                <input
                                    type="radio"
                                    name="tipoRendimento"
                                    value="COMPOSTO"
                                    checked={formulario.tipoRendimento === "COMPOSTO"}
                                    onChange={atualizarFormulario}
                                />
                                Juros compostos
                            </label>
                        </div>
                    </div>
                </div>

                <div className="acoes">
                    <button type="button" className="botao botao-primario" onClick={calcular}>Calcular</button>
                    <button type="button" className="botao botao-sucesso" onClick={salvar}>Salvar</button>
                </div>

                {resultado && (
                    <div className="resultado">
                        <h3>Resultado da simulação</h3>
                        <p>Valor final: <strong>{formatarMoeda(resultado.valorFinal)}</strong></p>
                        <p>Rendimento no período: <strong>{formatarMoeda(resultado.rendimento)}</strong></p>
                        <p>
                            {formatarTipoRendimento(resultado.tipoRendimento)} de {formatarPorcentagem(resultado.taxaMensal)} ao mês
                            durante {resultado.prazoMeses} meses.
                        </p>
                    </div>
                )}
            </section>

            <section className="cartao">
                <h2>Filtros do histórico</h2>
                <div className="formulario">
                    <div className="campo">
                        <label htmlFor="dataInicio">Data inicial</label>
                        <input id="dataInicio" name="dataInicio" type="date" value={filtros.dataInicio} onChange={atualizarFiltro} />
                    </div>
                    <div className="campo">
                        <label htmlFor="dataFim">Data final</label>
                        <input id="dataFim" name="dataFim" type="date" value={filtros.dataFim} onChange={atualizarFiltro} />
                    </div>
                    <div className="campo">
                        <label htmlFor="prazoMinimo">Prazo mínimo (meses)</label>
                        <input id="prazoMinimo" name="prazoMinimo" type="number" step="1" value={filtros.prazoMinimo} onChange={atualizarFiltro} />
                    </div>
                    <div className="campo">
                        <label htmlFor="prazoMaximo">Prazo máximo (meses)</label>
                        <input id="prazoMaximo" name="prazoMaximo" type="number" step="1" value={filtros.prazoMaximo} onChange={atualizarFiltro} />
                    </div>
                    <div className="campo">
                        <label htmlFor="taxaMinima">Taxa mínima (%)</label>
                        <input id="taxaMinima" name="taxaMinima" type="number" step="0.01" value={filtros.taxaMinima} onChange={atualizarFiltro} />
                    </div>
                    <div className="campo">
                        <label htmlFor="taxaMaxima">Taxa máxima (%)</label>
                        <input id="taxaMaxima" name="taxaMaxima" type="number" step="0.01" value={filtros.taxaMaxima} onChange={atualizarFiltro} />
                    </div>
                </div>

                <div className="acoes">
                    <button type="button" className="botao botao-primario" onClick={() => carregarInvestimentos(filtros)}>Filtrar</button>
                    <button type="button" className="botao botao-secundario" onClick={limparFiltros}>Limpar filtros</button>
                    <button type="button" className="botao botao-perigo" onClick={confirmarLimpezaTabela}>Limpar tabela</button>
                </div>
            </section>

            <section className="cartao">
                <h2>Histórico de simulações</h2>
                <div className="tabela-rolagem">
                    <table className="tabela">
                        <thead>
                            <tr>
                                <th>Data do cálculo</th>
                                <th>Tipo de rendimento</th>
                                <th>Valor inicial</th>
                                <th>Prazo em meses</th>
                                <th>Juro mensal</th>
                                <th>Valor final</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            {carregando && (
                                <tr>
                                    <td colSpan="7">Carregando...</td>
                                </tr>
                            )}

                            {!carregando && investimentos.length === 0 && (
                                <tr>
                                    <td colSpan="7">Nenhuma simulação encontrada.</td>
                                </tr>
                            )}

                            {!carregando && investimentos.map((investimento) => (
                                <tr key={investimento.id}>
                                    <td>{formatarDataHora(investimento.dataCalculo)}</td>
                                    <td>{formatarTipoRendimento(investimento.tipoRendimento)}</td>
                                    <td>{formatarMoeda(investimento.valorInicial)}</td>
                                    <td>{investimento.prazoMeses}</td>
                                    <td>{formatarPorcentagem(investimento.taxaMensal)}</td>
                                    <td>{formatarMoeda(investimento.valorFinal)}</td>
                                    <td>
                                        <button
                                            type="button"
                                            className="botao botao-perigo botao-pequeno"
                                            onClick={() => confirmarExclusao(investimento)}
                                        >
                                            Excluir
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </section>

            {confirmacao && (
                <ModalConfirmacao
                    titulo={confirmacao.titulo}
                    texto={confirmacao.texto}
                    aoConfirmar={confirmacao.acao}
                    aoCancelar={() => setConfirmacao(null)}
                />
            )}
        </PadraoLayout>
    );
};

export default Calculo;
