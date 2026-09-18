/* eslint-disable no-restricted-globals */
// Vai em front/public/service-worker.js
//
// O service worker é um script que roda FORA da página, no navegador.
// Ele fica no meio do caminho de toda requisição e pode responder com o cache.

const NOME_DO_CACHE = "financas-v1";

// arquivos que garantem a tela abrindo offline
const ARQUIVOS_BASICOS = [
    "/",
    "/index.html",
    "/manifest.json",
    "/icon-192.png",
    "/icon-512.png",
];

// 1) INSTALL: acontece uma vez, quando o service worker é instalado
self.addEventListener("install", (evento) => {
    evento.waitUntil(
        caches.open(NOME_DO_CACHE).then((cache) => cache.addAll(ARQUIVOS_BASICOS))
    );
    self.skipWaiting(); // assume o controle sem esperar fechar as abas
});

// 2) ACTIVATE: limpa caches de versões antigas (quando você troca o financas-v1 por v2)
self.addEventListener("activate", (evento) => {
    evento.waitUntil(
        caches.keys().then((nomes) =>
            Promise.all(
                nomes
                    .filter((nome) => nome !== NOME_DO_CACHE)
                    .map((nome) => caches.delete(nome))
            )
        )
    );
    self.clients.claim();
});

// 3) FETCH: intercepta as requisições da página
self.addEventListener("fetch", (evento) => {
    const requisicao = evento.request;

    // só mexe em GET; POST/PUT/DELETE vão direto para a rede
    if (requisicao.method !== "GET") {
        return;
    }

    // chamadas da API nunca são servidas do cache (dado financeiro velho engana o usuário)
    if (requisicao.url.includes("/api/")) {
        return;
    }

    // navegação (abrir/recarregar a página): tenta a rede, se falhar entrega o index do cache
    if (requisicao.mode === "navigate") {
        evento.respondWith(
            fetch(requisicao).catch(() => caches.match("/index.html"))
        );
        return;
    }

    // CSS, JS, imagens: primeiro o cache (abre instantâneo), senão busca e guarda
    evento.respondWith(
        caches.match(requisicao).then((resposta) => {
            if (resposta) {
                return resposta;
            }
            return fetch(requisicao).then((respostaDaRede) => {
                const copia = respostaDaRede.clone();
                caches.open(NOME_DO_CACHE).then((cache) => cache.put(requisicao, copia));
                return respostaDaRede;
            });
        })
    );
});
