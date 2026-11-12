# SmartBarrier - Control de Barrera Vehicular IoT

Estudiante: Daniel Aceitón
Institución: INACAP
Sección: P TI3042

## Descripción
Aplicación Android desarrollada en Kotlin y Jetpack Compose para monitorear y controlar una barrera vehicular simulada mediante un servidor Python.

## Funcionalidades
- Inicio de sesión demostrativo
- Apertura y cierre de barrera
- Consulta del estado
- Historial de comandos
- Detección de vehículo simulada

## Ejecutar servidor
Desde la carpeta principal del proyecto, ejecutar: py servidor/servidor.py

## Ejecutar aplicación
Abrir el proyecto en Android Studio y ejecutar en un emulador Android.

Para conectar el emulador al servidor local, ejecutar: adb reverse tcp:8000 tcp:8000

En la aplicación ingresar la IP: 127.0.0.1

## Pruebas
Se comprobó la comunicación entre Android y Python mediante ADB reverse y la comunicación Wi-Fi entre un iPhone y el servidor Python. No se ha demostrado comunicación inalámbrica entre dos dispositivos Android.

## Seguridad
Prototipo académico que utiliza HTTP y credenciales demostrativas. No apto para producción.

## Autor
Daniel Aceitón - 2026.