/* menu administrador */
fun menuAdministrador() {
    while (true) {
        println("\n--- MENÚ ADMINISTRADOR " + (SessionManager.usuarioActual?.getNombre() ?: "N/A") + "---")
        println("1 - Crear nuevo usuario (admin o vendedor)")
        println("2 - Eliminar un usuario (admin o vendedor)")
        println("3 - Ver todos los usuarios")
        println("4 - Ver reportes")
        println("0 - Cerrar sesión")

        when (readLine()?.toIntOrNull()) {
            1 -> admiCreaUsuario()
            2 -> adminEliminaUsuario()
            3 -> adminMuestraUsuarios()
            4 -> adminReportes()
            0 -> {
                cerrarSesion()
                break
            }
            else -> println("Opción inválida")
        }
    }
}

/* funciones de cada una de las opciones del menu */
fun admiCreaUsuario() {
    val nuevoUsuario = UsuarioFactory.crearUsuarioComoAdmin()
    if (nuevoUsuario != null) {
        SessionBD.sistemaUsuarios.agregarUsuario(nuevoUsuario)
    }
}

fun adminEliminaUsuario() {
        println("Ingrese el email del usuario a eliminar:")
        val emailAEliminar = readLine() ?: ""

        val usuarioActual = SessionManager.usuarioActual

        if (usuarioActual is Administrador) {
            if (usuarioActual.getEmail() == emailAEliminar) {
                println("❌ No puedes eliminarte a ti mismo mientras estás logueado.")
            } else {
                val eliminado = SessionBD.sistemaUsuarios.eliminarUsuarioPorEmail(emailAEliminar)
                if (eliminado) {
                    println("✅ Usuario eliminado exitosamente.")
                } else {
                    println("❌ No se encontró un usuario con ese email.")
                }
            }
        } else {
            println("❌ Solo un administrador puede eliminar usuarios.")
        }


}

fun  adminMuestraUsuarios () {
    SessionBD.sistemaUsuarios.mostrarUsuarios()
}

fun adminReportes() {
    while (true) {
        println("\n--- Elija el reporte ---")
        println("1 - Listar pedidos de un cliente (ordenados por fecha)")
        println("2 - Listar clientes que hicieron más de un pedido")
        println("3 - Mostrar total recaudado por productos vendidos")
        println("0 - Volver al menú anterior")

        when (readLine()?.toIntOrNull()) {
            1 -> PedidosxCliente()
            2 -> ClientesConPedidos()
            3 -> TotalRecaudado()
            0 -> break
            else -> println("Opción inválida")
        }
    }
}

/* Funciones especiales de reporteria */
fun PedidosxCliente() {
    print("Ingrese el email del cliente:")
    val emailCliente = readLine() ?: ""

    val cliente = SessionBD.sistemaUsuarios.getListausuarios()
        .filterIsInstance<Cliente>()
        .find { it.getEmail() == emailCliente }

    if (cliente != null) {
        println("\nPedidos de ${cliente.getNombre()}:")
        val pedidosOrdenados = cliente.obtenerPedidos().sortedBy { it.getFechaPedido() }
        pedidosOrdenados.forEach { it.mostrarPedido() }
    } else {
        println("❌ Cliente no encontrado.")
    }
}

fun ClientesConPedidos() {
    val clientesConMuchosPedidos = SessionBD.sistemaUsuarios.getListausuarios()
        .filterIsInstance<Cliente>()
        .filter { it.obtenerPedidos().size > 1 }

    if (clientesConMuchosPedidos.isNotEmpty()) {
        println("\nClientes que realizaron más de un pedido:")
        clientesConMuchosPedidos.forEach { println("${it.getNombre()} (cant: ${it.obtenerPedidos().size} | total: $${it.obtenerTotaldePedidos()})") }
    } else {
        println("❌ No hay clientes con múltiples pedidos.")
    }
}

fun TotalRecaudado() {
    val pedidosEntregados = SessionBD.sistemaUsuarios.getListausuarios()
        .filterIsInstance<Cliente>()
        .flatMap { it.obtenerPedidos() }
        .filter { it.getEstado() == EstadoPedido.Entregado }

    val totalRecaudado = pedidosEntregados.sumOf { it.calcularTotal().toDouble() }

    println("\n💰 Total recaudado en pedidos entregados: $totalRecaudado")
}

