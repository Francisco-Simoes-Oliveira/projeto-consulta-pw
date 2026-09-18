import React, { useEffect, useState } from "react";
import Header from "../../components/Header/Header";
import { useNavigate } from "react-router-dom";
import { Button } from "primereact/button";
import InvestimentoService from "../../service/InvestimentoService";

const Home = () => {
    const service = new InvestimentoService();
    const navigate = useNavigate();

    const [resumo, setResumo] = useState(null);

    useEffect(() => {
        carregarResumo();
    }, []);
    
        const carregarResumo = async () => {
            try{
                const dados = await service.resumo();
                setResumo(dados);
            } catch (error) {
                console.error("Erro ao carregar resumo", error);
            }
        };

    return(
        <>
            <Header titulo="Home" />
            <div style={{marginTop: "20px", padding: "15px", display: "flex", flexDirection: "column", alignItems: "center"}}>
                <h2>Bem-vindo ao Simulador de Investimentos!</h2><br></br>
                {resumo &&(
                    <div>
                        <h3>Data Hora de Acesso: <strong>{resumo.dataHora}</strong></h3>
                        <h3>Quantidade de Simulações: <strong>{resumo.quantidadeSimulacoes}</strong></h3>
                        <h3>Valor Final Médio: <strong>{resumo.valorFinalMedio}</strong></h3>
                        <h3>Última Simulação: <strong>{resumo.ultimaSimulacao}</strong></h3>
                    </div>
                )}
            </div>
            <div style={{ marginTop: "20px", padding: "15px", display: "flex", flexDirection: "column", alignItems: "center" }}>
                <Button 
                    label="Investir"
                    onClick={() => navigate("/investimento")}
                />
            </div>
        </>
    );
}

export default Home;
