import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import PadraoLayout from "../../components/PadraoLayout";
import Alerta from "../../components/Alerta";
import InvestimentoService from "../../api/apiInvestimento";
import extrairMensagemErro from "../../api/erro";
import { formatarDataHora, formatarMoeda } from "../../utils/formato";

const investimentoService = new InvestimentoService();

const Home = () => {
    const navigate = useNavigate();
    const [dataHora, setDataHora] = useState("");
    const [resumo, setResumo] = useState(null);
    const [erro, setErro] = useState("");
    const [carregando, setCarregando] = useState(true);

    useEffect(() => {
        const carregar = async () => {
            try {
                const [dataHoraApi, resumoApi] = await Promise.all([
                    investimentoService.buscarDataHora(),
                    investimentoService.buscarResumo(),
                ]);
                setDataHora(dataHoraApi.dataHoraFormatada);
                setResumo(resumoApi);
            } catch (error) {
                setErro(extrairMensagemErro(error, "Não foi possível carregar as informações da página."));
            } finally {
                setCarregando(false);
            }
        };

        carregar();
    }, []);

    return (
        <PadraoLayout titulo="Simulador de Investimentos">
            {carregando && <p className="carregando">Carregando...</p>}

            <Alerta tipo="erro" texto={erro} />

            {!carregando && !erro && (
                <>
                    <section className="cartao">
                        <h2>Olá, investidor!</h2>
                        <p>Você acessou a página em: <strong>{dataHora}</strong></p>
                    </section>

                    <h2 className="titulo-secao">Resumo dos seus cálculos</h2>
                    <div className="grade-resumo">
                        <section className="cartao">
                            <h3>Simulações realizadas</h3>
                            <span className="valor-destaque">{resumo.quantidadeSimulacoes}</span>
                        </section>
                        <section className="cartao">
                            <h3>Valor final médio</h3>
                            <span className="valor-destaque">{formatarMoeda(resumo.valorFinalMedio)}</span>
                        </section>
                        <section className="cartao">
                            <h3>Última simulação</h3>
                            <span className="valor-destaque">{formatarDataHora(resumo.dataUltimaSimulacao)}</span>
                        </section>
                    </div>

                    <button
                        type="button"
                        className="botao botao-primario botao-principal"
                        onClick={() => navigate("/calculo")}
                    >
                        Realizar cálculo de investimento
                    </button>
                </>
            )}
        </PadraoLayout>
    );
};

export default Home;
