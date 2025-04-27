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
    val cliente = SessionManager.usuarioActual as? Cliente
        ?: throw Exception("Usuario actual no es un cliente.")

    val fechaActual = LocalDate.now().toString()
    val pedido = Pedido(cliente, fechaActual, EstadoPedido.Pendiente)

    println("👉 Vamos a armar tu pedido. Podés agregar varios productos.")
    var totalParcial = 0.0f

    while (true) {
        println("\nProductos disponibles:")
        SessionBD.productosDisponibles
            .filter { it.getStock() > 0 }
            .forEach {
                println("ID: ${it.getId()} | Nombre: ${it.getNombre()} | Precio: $${"%.2f".format(it.getPrecioConDescuento())} | Stock: ${it.getStock()}")
            }

        println("\n📦 Ingresá el ID del producto que querés agregar:")
        println("(0 para terminar el pedido, -1 para cancelar todo y salir)")
        val idProducto = readLine()?.toIntOrNull() ?: run {
            println("⚠ Entrada inválida. Cancelando operación.")
            return
        }

        when (idProducto) {
            -1 -> {
                println("❌ Pedido cancelado por el usuario.")
                return
            }
            0 -> {
                break
            }
        }

        val productoSeleccionado = SessionBD.productosDisponibles
            .filter { it.getStock() > 0 }
            .find { it.getId() == idProducto }

        if (productoSeleccionado == null) {
            println("⚠ Producto no encontrado o sin stock.")
            continue
        }

        println("🔢 ¿Cuántas unidades de ${productoSeleccionado.getNombre()} querés agregar? (0 para cancelar este producto)")
        val cantidad = readLine()?.toIntOrNull() ?: run {
            println("⚠ Entrada inválida. Cancelando operación.")
            return
        }

        if (cantidad == 0) {
            println("✅ No se agregó ninguna unidad de este producto.")
            continue
        }

        if (cantidad < 0) {
            println("⚠ La cantidad debe ser mayor a 0.")
            continue
        }

        if (cantidad > productoSeleccionado.getStock()) {
            println("⚠ Stock insuficiente. Solo quedan ${productoSeleccionado.getStock()}.")
            continue
        }

        pedido.agregarProducto(productoSeleccionado, cantidad)
        val subtotalProducto = productoSeleccionado.getPrecioConDescuento() * cantidad
        totalParcial += subtotalProducto

        println("✅ ${cantidad}x ${productoSeleccionado.getNombre()} agregados al pedido. Subtotal: $${"%.2f".format(subtotalProducto)}")
        println("🧾 Total parcial actual: $${"%.2f".format(totalParcial)}")
    }

    if (pedido.estaVacio()) {
        println("⚠ No agregaste ningún producto. Pedido cancelado.")
        return
    }

    // Mostrar resumen completo antes de confirmar
    println("\n🧾 RESUMEN DE TU PEDIDO:")
    pedido.mostrarPedido()
    println("💵 Total a pagar (sin forma de pago aplicada todavía): $${"%.2f".format(totalParcial)}")

    println("\n¿Deseás confirmar este pedido? (S para confirmar / cualquier otra tecla para cancelar)")
    val confirmacion = readLine()?.trim()?.lowercase()
    if (confirmacion != "s") {
        println("❌ Pedido cancelado.")
        return
    }

    // Selección de forma de pago
    println("\n💳 Seleccioná una forma de pago:")
    FormaDePago.values().forEachIndexed { index, forma ->
        println("${index + 1} - ${forma.name}")
    }
    val opcionPago = readLine()?.toIntOrNull()
    val formaDePagoSeleccionada = if (opcionPago != null && opcionPago in 1..FormaDePago.values().size) {
        FormaDePago.values()[opcionPago - 1]
    } else {
        println("⚠ Forma de pago no válida. Se asignará EFECTIVO por defecto.")
        FormaDePago.EFECTIVO
    }

    pedido.elegirFormaDePago(formaDePagoSeleccionada)
    cliente.agregarPedido(pedido)

    println("\n🎉 Pedido confirmado exitosamente. Detalles finales:")
    pedido.mostrarPedido()
}

