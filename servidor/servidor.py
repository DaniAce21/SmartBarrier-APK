import os
import hmac

from http.server  import BaseHTTPRequestHandler, HTTPServer


estado = "CERRADA"
TOKEN = os.environ.get("SMARTBARRIER_TOKEN")

if not TOKEN:
    raise SystemExit("ERROR : configura SMARTBARRIER_TOKEN antes de iniciar.")

class Servidor(BaseHTTPRequestHandler):
    def do_GET(self):
        global estado

        token_recibido = self.headers.get("Authorization", "")
        token_esperado = f"Bearer {TOKEN}"

        if not hmac.compare_digest(token_recibido, token_esperado):
            self.send_error(401,"No autorizado")
            return

        if self.path == "/abrir":
            estado = "ABIERTA"
        elif self.path == "/cerrar":
            estado = "CERRADA"
            self.send_error(404, "Ruta no encontrada")
            return

        self.send_response(200)
        self.send_header("Content-Type," "text/plain; charset=utf-8")
        self.end_headers()
        self.wfile.write(estado.encode("utf-8"))


        if __name__ == "__main__":
            print("SmartBarrier iniciado en puerto 8000")
            HTTPServer(("0.0.0.0", 8000), Servidor).serve_forever()
