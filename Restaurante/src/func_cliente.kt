/* para cargar pedido nuevo con fecha de hoy */
import java.time.LocalDate

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
                try {
                    clienteHacePedido()
                } catch (e: ProductoNoEncontradoException) {
                    println("❌ ${e.message}")
                } catch (e: StockInsuficienteException) {
                    println("❌ ${e.message}")
                } catch (e: Exception) { // Por si salta cualquier otro error inesperado
                    println("❌ Error inesperado: ${e.message}")
                }
            }
            2 -> println("Cancelar pedido")
            3 -> println("Modificar pedido")
            4 -> println("Ver mis pedidos")
            0 -> {
                SessionManager.usuarioActual = null
                println("Sesión de cliente finalizada.")
                break
            }
            else -> println("Opción inválida")
        }
    }
}

fun clienteHacePedido() {
    println("Seleccione un producto para hacer el pedido:")
    SessionBD.productosDisponibles
        .filter { it.getStock() > 0 }
        .forEach { println("ID: ${it.getId()} | Nombre: ${it.getNombre()} | Stock: ${it.getStock()}") }

    val idProducto = readLine()?.toIntOrNull() ?: throw ProductoNoEncontradoException("ID de producto inválido.")

    val productoSeleccionado = SessionBD.productosDisponibles
        .filter { it.getStock() > 0 }
        .find { it.getId() == idProducto }
        ?: throw ProductoNoEncontradoException("Producto no encontrado o sin stock disponible.")

    println("¿Cuántas unidades desea comprar de ${productoSeleccionado.getNombre()}?")
    val cantidad = readLine()?.toIntOrNull() ?: throw StockInsuficienteException("Cantidad inválida.")

    if (cantidad <= 0) {
        throw StockInsuficienteException("Debe ingresar una cantidad mayor a 0.")
    }
    if (cantidad > productoSeleccionado.getStock()) {
        throw StockInsuficienteException("No hay suficiente stock para la cantidad solicitada. Disponible: ${productoSeleccionado.getStock()}.")
    }

    // Mostrar opciones de forma de pago
    println("Seleccione una forma de pago:")
    FormaDePago.values().forEachIndexed { index, forma ->
        println("${index + 1} - ${forma.name}")
    }
    val opcionPago = readLine()?.toIntOrNull()
    val formaDePagoSeleccionada = if (opcionPago != null && opcionPago in 1..FormaDePago.values().size) {
        FormaDePago.values()[opcionPago - 1]
    } else {
        println("⚠ Forma de pago no válida, se asignará EFECTIVO por defecto.")
        FormaDePago.EFECTIVO
    }

    // Crear el pedido
    val cliente = SessionManager.usuarioActual as? Cliente
        ?: throw Exception("Usuario actual no es un cliente.")

    val fechaActual = LocalDate.now().toString() // 🗓️ fecha de hoy en formato YYYY-MM-DD
    val pedido = Pedido(cliente, fechaActual, EstadoPedido.Pendiente)
    pedido.agregarProducto(productoSeleccionado, cantidad)
    pedido.elegirFormaDePago(formaDePagoSeleccionada)
    cliente.agregarPedido(pedido)

    println("✅ Pedido realizado con éxito.")
}
