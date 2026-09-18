import { useEffect, useState } from "react";
import Header from "../components/Header"
import CardResumo from "../components/CardResumo"
import HomeService from "../servise/HomeService";
import { useNavigate } from "react-router-dom";

const Home = () => {
    const service = new HomeService();
    const navigate = useNavigate();
    const [data, setData] = useState();
    const [qtdeSimu, setQtdeSimu] = useState("00");
   
    const tituloQtde = "Quantidade de Simulaçoes";

    useEffect( () => {
        const carregar = async () => {
            const response = await service.list();
            setData(response.data);
            setQtdeSimu(response.qtdeSimu);
        }
        carregar()
    }, []);


   
    return(
        <div>
            <Header titulo="Fretes"/>
            <p>Olá! você acessou esta página em {data} </p>

            <CardResumo titulo={tituloQtde} valor={qtdeSimu ?? "0"} ></CardResumo>

            <button onClick={() => {navigate("/calculadora")
            }}>Realizar calculo de Investimento</button>
        </div>
    )
}

export default Home;