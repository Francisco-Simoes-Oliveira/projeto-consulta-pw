import React from "react";

const Alerta = ({ tipo, texto, aoFechar }) => {

    if (!texto) {
        return null;
    }

    return (
        <div className={`alerta alerta-${tipo}`}>
            <span>{texto}</span>
            {aoFechar && (
                <button type="button" className="alerta-fechar" onClick={aoFechar} aria-label="Fechar">
                    &times;
                </button>
            )}
        </div>
    );
};

export default Alerta;
