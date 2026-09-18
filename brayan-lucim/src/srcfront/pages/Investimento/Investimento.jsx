import React, { useState, useEffect } from "react";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Button } from "primereact/button";
import { InputText } from "primereact/inputtext";
import InvestimentoService from "../../service/InvestimentoService";
import Header from "../../components/Header/Header";

const Investimento = () => {

    const service = new InvestimentoService();

    const [tipoRendimento, setTipoRendimento] = useState('');
    const [investimento, setInvestimento] = useState([]);
    const [valorInicial, setValorInicial] = useState('');
    const [prazoMeses, setPrazoMeses] = useState('');
    const [taxaJuros, setTaxaJuros] = useState('');
    const [valorFinal, setValorFinal] = useState(null);
    const [dataInicial, setDataInicial] = useState('');
    const [dataFinal, setDataFinal] = useState('');
    const [prazoInicial, setPrazoInicial] = useState('');
    const [prazoFinal, setPrazoFinal] = useState('');

    useEffect(() => {
        carregarInvestimentos();
    }, []);

    const carregarInvestimentos = async () => {
        try{
            const dados = await service.list();
            setInvestimento(dados);
        } catch (error){
            console.error("Erro ao buscar investimentos", error);
        }
    };

    /*const handleCalcular = async () => {
        try{
            const calculo = await service.calcular(investimento);
            setValorFinal(calculo);
        } catch (error){
            console.error("Erro ao calcular investimento", error);
        }
    };*/

    const handleCalcular = async () => {
        try{
            const dados = {
                valorInicial: parseFloat(valorInicial),
                prazoMeses: parseInt(prazoMeses),
                taxaJuros: parseFloat(taxaJuros),
                tipoRendimento: tipoRendimento
            }

            const calculo = await service.calcular(dados);

            setValorFinal(calculo.valorFinal);
            
        } catch (error){
            console.error("Erro ao calcular investimento", error);
        }
    };

    const handleFiltrarPorData = async () => {
        try{
            const dados = await service.filtrarPorData(dataInicial, dataFinal)

            setInvestimento(dados);
        } catch (error) {
            console.error("Erro ao filtrar por data", error);
        }
    }

    const handleFiltrarPorPrazo = async () => {
        try {
            const dados = await service.filtrarPorPrazo(prazoInicial, prazoFinal)

            setInvestimento(dados);
        } catch (error) {
            console.error("Erro ao filtrar por prazo", error);
        }
    }

    const handleSalvar = async () => {
        try{
            const salvo = {
                valorInicial: parseFloat(valorInicial),
                prazoMeses: parseInt(prazoMeses),
                taxaJuros: parseFloat(taxaJuros),
                tipoRendimento: tipoRendimento,
                valorFinal: valorFinal
            }
            await service.insert(salvo);

            setValorInicial('');
            setPrazoMeses('');
            setTaxaJuros('');
            setTipoRendimento('');
            setValorFinal(null);
            carregarInvestimentos();

        } catch (error) {
            console.error("Erro completo:", error);
            console.error("Resposta do backend:", error.response);
            console.error("Dados do erro:", error.response?.data);
        }
    };

    const handleExcluir = async (investimento) => {
        try {
            await service.delete(investimento.id);
            carregarInvestimentos();
        } catch (error) {
            console.error("Erro ao carregar investimentos");
        }
    }

    const botaoexcluir = (investimento) => {
        return(
            <Button 
                label="Excluir"
                onClick={() => handleExcluir(investimento)}
            />
        )
    }
            

    const handleLimpar = async () => {
        const confirmar = window.confirm("Deseja realmente excluir todos os investimentos?")

        if(!confirmar){
            return
        }

        try{
            await service.deleteAll();
            carregarInvestimentos();
        } catch (error) {
            console.error("Erro ao excluir tabela");
        }
    };

    return(
        <>
            <Header titulo="Cálculo de Investimento - Listagem" />
            <div style={{ marginTop: "20px", padding: "15px", display: "flex", flexDirection: "column", alignItems: "center" }}>
                <InputText 
                    type="number"
                    value={valorInicial}
                    placeholder="Digite o Valor Inicial"
                    onChange={e => setValorInicial(e.target.value)}
                />
                <InputText
                    type="number"
                    value={prazoMeses}
                    placeholder="Digite o Prazo em Meses"
                    onChange={e => setPrazoMeses(e.target.value)}
                />
                <InputText
                    type="number"
                    value={taxaJuros}
                    placeholder="Digite a Taxa de Juros"
                    onChange={e => setTaxaJuros(e.target.value)}
                />
                <select
                    value={tipoRendimento}
                    onChange={e => setTipoRendimento(e.target.value)}
                >
                    <option value="">Selecione</option>
                    <option value="SIMPLES">Simples</option>
                    <option value="COMPOSTO">Composto</option>
                </select>
            </div>

            <div style={{ marginTop: "20px", padding: "15px", display: "flex", flexDirection: "column", alignItems: "center" }}>
                <Button label="Calcular" onClick={handleCalcular} />
            </div>

                <div style={{ marginTop: "20px", padding: "15px", display: "flex", flexDirection: "column", alignItems: "center" }}>
                    {valorFinal && <h2>
                    Valor Final do Investimento: R$<strong>{valorFinal}</strong>
                    </h2>}
                    <Button label="Salvar" onClick={handleSalvar} />
                </div>

            <div style={{ marginTop: "20px", padding: "15px", display: "flex", flexDirection: "column", alignItems: "center" }}>
                <Button label="Limpar" onClick={handleLimpar} />
            </div>

            <div>
                <h3>Filtro por Data</h3>
                <InputText
                    type="datetime-local"
                    value={dataInicial}
                    onChange={e => setDataInicial(e.target.value)}
                />
                <InputText
                    type="datetime-local"
                    value={dataFinal}
                    onChange={e => setDataFinal(e.target.value)}
                />
                <Button
                    label="Filtrar por Data"
                    onClick={handleFiltrarPorData}
                />
            </div>

            <div>
                <h3>Filtro por Prazo</h3>
                <InputText
                    type="number"
                    value={prazoInicial}
                    onChange={e => setPrazoInicial(e.target.value)}
                />
                <InputText
                    type="number"
                    value={prazoFinal}
                    onChange={e => setPrazoFinal(e.target.value)}
                />
                <Button
                    label="Filtrar por Prazo"
                    onClick={handleFiltrarPorPrazo}
                />
            </div>

            <DataTable value={investimento}>
                <Column field="dataCalculo" header="Data do Cálculo"></Column>
                <Column field="tipoRendimento" header="Tipo de Rendimento"></Column>
                <Column field="prazoMeses" header="Prazo em Meses"></Column>
                <Column field="taxaJuros" header="Taxa de Juros"></Column>
                <Column field="valorFinal" header="Valor Final R$"></Column>
                <Column body={botaoexcluir} header="Excluir"></Column>
            </DataTable>
        </>
    );

}

export default Investimento;