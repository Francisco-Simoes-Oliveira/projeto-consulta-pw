// Lado do navegador (não dá para ser Java).
// npm install @stomp/stompjs sockjs-client

import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

/**
 * Hook que conecta no WebSocket e devolve o último evento de saldo da carteira.
 *
 * Uso na tela:
 *   const evento = useSaldoAoVivo(carteiraId);
 *   {evento && <p>Saldo agora: {evento.saldoAtual}</p>}
 */
const useSaldoAoVivo = (carteiraId) => {
    const [evento, setEvento] = useState(null);

    useEffect(() => {
        if (!carteiraId) {
            return undefined;
        }

        const cliente = new Client({
            webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
            reconnectDelay: 5000, // se cair, tenta de novo em 5s
            onConnect: () => {
                cliente.subscribe(`/topic/carteira/${carteiraId}`, (mensagem) => {
                    setEvento(JSON.parse(mensagem.body));
                });
            },
        });

        cliente.activate();

        // ao sair da tela, fecha a conexão
        return () => cliente.deactivate();
    }, [carteiraId]);

    return evento;
};

export default useSaldoAoVivo;
