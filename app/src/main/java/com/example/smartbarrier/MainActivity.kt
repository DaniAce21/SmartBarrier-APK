package com.example.smartbarrier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity () {
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
        MaterialTheme {
            SmartBarrierApp()
             }
        }
    }
}
@Composable
fun SmartBarrierApp(){
    var sesion by remember { mutableStateOf(false) }
    var usuario by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var ip by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("CERRADA") }
    var vehiculo by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    val historial = remember { mutableStateListOf<String>() }
    val scope = rememberCoroutineScope()

    fun enviar(comando: String) {
        scope.launch {
            try {
                val respuesta = kotlinx.coroutines.withContext(
                    kotlinx.coroutines.Dispatchers.IO
                ) {
                    val url = URL("http://$ip:8000/$comando")
                    val conexion = url.openConnection(java.net.Proxy.NO_PROXY) as HttpURLConnection
                    conexion.requestMethod = "GET"
                    conexion.connectTimeout = 5000
                    conexion.readTimeout = 5000
                    try {
                        conexion.inputStream.bufferedReader().use {
                            it.readText()
                        }
                    } finally {
                        conexion.disconnect()
                    }
                }
                estado = respuesta
                historial.add("$comando: $respuesta")
                mensaje = "Conectado"
            } catch (e: Exception) {
                mensaje = "${e.javaClass.simpleName}: ${e.message}"
            }
        }
    }

    Column(
        Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("SmartBarrier",
           style = MaterialTheme.typography.headlineLarge)

        Spacer(Modifier.height(20.dp))

        if (!sesion) {
            OutlinedTextField(
                usuario, { usuario = it },
                label = { Text("Usuario") }
            )
            OutlinedTextField(
                clave, { clave = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(onClick = {
                if (usuario == "admin" && clave == "1234") {
                    sesion = true
                    mensaje = ""
                } else mensaje = "Datos incorrectos"
            }) {
                Text("Ingresar")
            }
        } else {
            Text("Barrera: $estado")

            OutlinedTextField(
                ip, { ip = it },
                label = { Text("IP del computador") },
                singleLine = true
            )

            Button(onClick = { enviar("")}) {
                Text("Consultar estado")
            }

            Button(onClick = {
                enviar("abrir")
            }) {
                Text("Abrir barrera")
            }

            Button(onClick = {
                if (!vehiculo) enviar("cerrar")
                else mensaje = "Vehiculo detectado"
            }) {
                Text("Cerrar barrera")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Vehiculo detectado")
                Switch(vehiculo, onCheckedChange = {vehiculo = it})
            }

            Text("Historial")
            historial.takeLast(5).forEach { Text(it) }

            Button(onClick = {
                sesion = false
                clave = ""
                mensaje = ""
            }) {
                Text("Cerrar sesion")
            }
        }

        Text(mensaje)
    }
}
