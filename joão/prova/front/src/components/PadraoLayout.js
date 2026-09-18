import React from "react";
import Header from "./Header";

const PadraoLayout = ({ titulo, children }) => {

    return (
        <>
            <Header titulo={titulo} />
            <main className="conteudo">
                {children}
            </main>
        </>
    );
};

export default PadraoLayout;
