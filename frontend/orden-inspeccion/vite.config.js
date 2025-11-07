import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        proxy: {
            // Redirige las solicitudes de /api al backend de Spring Boot
            '/api': {
                target: 'http://localhost:8080', // El puerto donde corre tu backend
                changeOrigin: true, // Necesario para hosts virtuales
                secure: false,      // No es necesario si tu backend no usa HTTPS
            }
        }
    }
})
