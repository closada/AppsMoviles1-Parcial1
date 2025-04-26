
fun menuCliente() {
    while (true) {
        println("\n--- MENÚ CLIENTE: " + (SessionManager.usuarioActual?.getNombre() ?: "N/A") + " ---")
        println("1 - Hacer un pedido")
        println("2 - Cancelar un pedido (atencion! solo en estado Pendiente)")
        println("3 - Modificar un pedido (atencion! solo en estado Pendiente)")
        println("4 - Ver mis pedidos")
        println("0 - Cerrar sesión")

        when (readLine()?.toIntOrNull()) {
            1 -> {
                println("hecho el pedido!")
            }
            2 -> println("cancelado")
            3 -> println("modificado")
            4 -> println("vistos")
            0 -> {
                cerrarSesion()
                break
            }
            else -> println("Opción inválida")
        }
    }
}
