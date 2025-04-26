
fun menuVendedor() {
    while (true) {
        println("\n--- MENÚ VENDEDOR: " + (SessionManager.usuarioActual?.getNombre() ?: "N/A") + " ---")
        println("1 - Crear nuevo producto")
        println("2 - modificar un producto")
        println("3 - modificar el estado de un pedido existente")
        println("0 - Cerrar sesión")

        when (readLine()?.toIntOrNull()) {
            1 -> {
                println("creado el producto!")
            }
            2 -> println("modificado el producto!")
            3 -> println("modificar el estado de un pedido")
            0 -> {
                cerrarSesion()
                break
            }
            else -> println("Opción inválida")
        }
    }
}
