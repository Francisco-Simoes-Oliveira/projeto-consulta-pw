import { useState, useEffect } from "react";
import Header from "../components/Header";
import FreteService from "../servise/FreteService";

import "./Calculadora.css"

const Calculadora = () =>{

    const service = new FreteService();

    const [erro, setErro] = useState();

    const [kg, setKg] = useState();
    const [km, setKm] = useState();
    const [adicDeUrgencia, setAdicDeUrgencia] = useState();
    const [tipo, setTipo] = useState("ECONOMICO");

    const [fretes, setFretes] = useState([]);


    const [valorFrete,setValorFrete] = useState();

     useEffect( () => {
          
            carregar()
            
        }, []);

    const carregar = async () => {
        const response = await service.list();
        setFretes(response);
    }

    async function calcular()  {
       
        const response = await service.calcular({kg,km,adicDeUrgencia,tipo}, setErro);
        
        if(response != null) setValorFrete(response.valorFrete); 
    }

    async function salvar() {
        if(adicDeUrgencia == null)setAdicDeUrgencia(1);
        const response = await service.salvar({ kg, km, adicDeUrgencia, tipo }, setErro);
        
        carregar();
        
    }

    async function limparTabela() {
        await service.limparTabela()

        carregar();
    }
    async function deleteId(id) {
        await service.delete(id)

        carregar();
    }

    const handleChange = (event) => {
        setTipo(event.target.value);
    };


   

    return <div className="page">
        <Header titulo="Cálculo de Frete - Linstagem"></Header>
            <section className="secao_inputs">
            <div className="cont_inputs">
                <label htmlFor="">peso em kg:</label>
                <input type="number" min={0} value={kg} onChange={(e) => {setKg(Number(e.target.value))}}  /> <br />
            </div>
            <div className="cont_inputs">
            <label htmlFor="">distancia em km</label>
            <input type="number" min={1} max={5000} value={km} onChange={(e) => { setKm(e.target.value) }} /> <br />
            </div>
            <div>
                <input type="radio" name="tipo" checked={tipo === "EXPRESSO"} id="" value="EXPRESSO" onChange={handleChange} />
                <label htmlFor="">EXPRESSO</label>
                <input type="radio" name="tipo" checked={tipo === "ECONOMICO"} id="" value="ECONOMICO" onChange={handleChange} />
                <label htmlFor="">ECONOMICO</label>
            </div>
            <br />
            <div className="cont_inputs">
            <label htmlFor="">Adicional de urgência (%)</label>
            <input type="number" min={0} max={100} value={adicDeUrgencia} onChange={(e) => { setAdicDeUrgencia(e.target.value)} } /> <br />
            </div>
            <br />
            
        </section>
        
        <p>{erro}</p>

        <div>
            <div>
                <button onClick={() => {calcular()}}>Calcular</button>
                <button onClick={() => {salvar()}}>Salvar</button>
            </div>

            <p>E o valor final e {valorFrete}</p>
        </div>

        <button onClick={() => {limparTabela()}}>Limpar tabela</button>
        <table border="1">
            <thead>
                <tr>
                    <th>Data</th>
                    <th>Tipo</th>
                    <th>peso(kg)</th>
                    <th>Distância (km)</th>
                    <th>Valor do Frete</th>
                    <th>deletar</th>
                </tr>
            </thead>

            <tbody>
                {fretes.map((frete) => (
                    <tr key={frete.id}>
                        <td>{frete.calculadoEm}</td>
                        <td>{frete.tipo}</td>
                        <td>{frete.kg}</td>
                        <td>{frete.km}</td>
                        <td>{frete.valorFrete}</td>
                        <td><button onClick={() => {deleteId(frete.id)}}>Deletar</button></td>
                    </tr>
                ))}
            </tbody>
        </table>

    </div>
}

export default Calculadora;