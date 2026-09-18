// Lado do navegador. EventSource é nativo: não precisa instalar nada.

import { useEffect, useState } from "react";

/**
 * Escuta as notificações da carteira e devolve a última recebida.
 *
 * Uso na tela:
 *   const notificacao = useNotificacoes(carteiraId);
 *   {notificacao && <Alerta texto={`Nova transação: ${notificacao.descricaoTransacao}`} />}
 */
const useNotificacoes = (carteiraId) => {
    const [notificacao, setNotificacao] = useState(null);

    useEffect(() => {
        if (!carteiraId) {
            return undefined;
        }

        const fonte = new EventSource(
            `http://localhost:8080/api/notificacoes/stream?carteiraId=${carteiraId}`
        );

        // o nome "transacao" é o mesmo do SseEmitter.event().name("transacao")
        fonte.addEventListener("transacao", (evento) => {
            setNotificacao(JSON.parse(evento.data));
        });

        fonte.onerror = () => {
            // o EventSource tenta reconectar sozinho; aqui é só para você ver no console
            console.warn("Conexão de notificações caiu, tentando reconectar...");
        };

        return () => fonte.close();
    }, [carteiraId]);

    return notificacao;
};

export default useNotificacoes;
