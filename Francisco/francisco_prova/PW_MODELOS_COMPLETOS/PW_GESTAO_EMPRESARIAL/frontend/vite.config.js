import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],

  // sockjs-client ainda referencia a global do Node em alguns módulos.
  // No navegador, globalThis é o equivalente adequado.
  define: {
    global: 'globalThis'
  },

  server: { port: 5173 }
})
