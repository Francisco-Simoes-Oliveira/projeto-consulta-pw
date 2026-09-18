import React from "react";

const ModalConfirmacao = ({ titulo, texto, aoConfirmar, aoCancelar }) => {

    return (
        <div className="modal-fundo" onClick={aoCancelar}>
            <div className="modal" onClick={(evento) => evento.stopPropagation()}>
                <h3>{titulo}</h3>
                <p>{texto}</p>
                <div className="modal-acoes">
                    <button type="button" className="botao botao-secundario" onClick={aoCancelar}>
                        Não
                    </button>
                    <button type="button" className="botao botao-perigo" onClick={aoConfirmar}>
                        Sim
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ModalConfirmacao;
