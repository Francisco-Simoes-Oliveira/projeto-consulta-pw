import React from "react";
import { useLocation, useNavigate } from "react-router-dom";

const Header = ({ titulo }) => {
    const navigate = useNavigate();
    const location = useLocation();

    return (
        <header className="cabecalho">
            <div className="cabecalho-conteudo">
                <h1>{titulo}</h1>
                <nav className="cabecalho-menu">
                    <button
                        type="button"
                        className="botao botao-menu"
                        disabled={location.pathname === "/"}
                        onClick={() => navigate("/")}
                    >
                        Home
                    </button>
                    <button
                        type="button"
                        className="botao botao-menu"
                        disabled={location.pathname === "/calculo"}
                        onClick={() => navigate("/calculo")}
                    >
                        Simulador
                    </button>
                </nav>
            </div>
        </header>
    );
};

export default Header;
