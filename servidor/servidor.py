from http.server  import BaseHTTPRequestHandler, HTTPServer

estado = "CERRADA"

class Servidor(BaseHTTPRequestHandler):
    def do_GET(self):
        global estado


        if self.path == "/abrir":
            estado = "ABIERTA"
        elif self.path == "/cerrar":
            estado = "CERRADA"

        self.send_response(200)
        self.send_header("Content-Type", "text/plain")
        self.end_headers()
        self.wfile.write(estado.encode())

print("SmartBarrier iniciado en puerto 8000")
HTTPServer(("0.0.0.0", 8000), Servidor).serve_forever()