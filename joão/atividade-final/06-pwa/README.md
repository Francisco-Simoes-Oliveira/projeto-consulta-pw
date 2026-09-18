# 06 — PWA (app instalável e com offline)

**Requisito 1.5.** Esta é a única parte que **não** tem Java: PWA é 100% navegador.

Para o Chrome mostrar o botão "Instalar", três coisas precisam existir:

1. `manifest.json` com nome, ícones (192px e 512px) e `display: standalone`;
2. um **service worker** registrado, com um `fetch` dentro dele;
3. o site em **HTTPS** (no `localhost` funciona sem HTTPS, por isso dá para testar em casa).

## Passo a passo (projeto Create React App)

1. Copie [manifest.json](manifest.json) para `front/public/manifest.json`.
2. Copie [service-worker.js](service-worker.js) para `front/public/service-worker.js`.
3. Coloque dois ícones em `front/public/`: `icon-192.png` e `icon-512.png`
   (qualquer imagem quadrada serve; dá para gerar em <https://realfavicongenerator.net>).
4. Confira se o `public/index.html` tem, dentro do `<head>`:

```html
<link rel="manifest" href="%PUBLIC_URL%/manifest.json" />
<meta name="theme-color" content="#4338ca" />
```

5. Registre o service worker no `src/index.js` — veja [registro-no-index.js](registro-no-index.js).

## Como testar

```bash
npm run build
npx serve -s build
```

Abra o endereço que aparecer e, no Chrome: **F12 → Application**.

- Em *Manifest*, confira nome e ícones.
- Em *Service Workers*, veja se está "activated and running".
- Marque **Offline** e recarregue: a tela precisa continuar aparecendo.
- O ícone de instalar aparece na barra de endereço.

> O `npm start` (modo dev) atrapalha o service worker. Sempre teste com o `build`.

## O que dá para fazer offline (e o que não dá)

| Funciona offline | Não funciona |
|---|---|
| Abrir o app e ver a última tela visitada | Salvar transação (precisa do backend) |
| CSS, JS e ícones (ficam no cache) | Relatório novo, login, IA |

Na apresentação, seja honesto: "offline eu garanto o *app shell* e a última tela; operações de
escrita exigem conexão". Isso já cumpre "pelo menos um nível de funcionamento offline".

## Se quiser ir além

- Guardar as transações criadas offline no **IndexedDB** e enviar quando a conexão voltar
  (*background sync*).
- Notificações push (precisa de chaves VAPID e de um service de push no backend).
