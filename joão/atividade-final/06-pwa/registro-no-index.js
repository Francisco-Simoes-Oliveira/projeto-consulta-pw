// Trecho para colar no FIM do front/src/index.js

// Registra o service worker. Sem isso o arquivo em public/ nunca roda
// e o Chrome não oferece a instalação do app.
if ("serviceWorker" in navigator) {
    window.addEventListener("load", () => {
        navigator.serviceWorker
            .register("/service-worker.js")
            .then((registro) => console.log("Service worker registrado:", registro.scope))
            .catch((erro) => console.error("Falha ao registrar o service worker:", erro));
    });
}

/*
 * O index.js completo fica assim:
 *
 * import React from "react";
 * import ReactDOM from "react-dom/client";
 * import "./index.css";
 * import App from "./App";
 *
 * const root = ReactDOM.createRoot(document.getElementById("root"));
 * root.render(
 *     <React.StrictMode>
 *         <App />
 *     </React.StrictMode>
 * );
 *
 * if ("serviceWorker" in navigator) { ... }   <- o trecho acima
 */
