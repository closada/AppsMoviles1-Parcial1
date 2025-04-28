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
            2 -> {
                try {
                    clienteCancelaPedido()
                }catch(e: PedidoNoEncontradoException) {println(e.message)}

                catch(e: NosePudoCancelar) {println(e.message)}
            }
            3 -> {
                try {
                    clienteModificaPedido()
                }catch(e: PedidoNoEncontradoException) {println(e.message)}

                catch(e: NoSePuedeModificarPedidoException) {println(e.message)}
            }
            4 -> {
                try {
                    clienteVerSusPedidos()
                } catch(e: UsuarioNoEncontradoException)
                {
                    println(e.message)
                }
            }
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

fun clienteCancelaPedido() {

    val usuarioActual = SessionManager.usuarioActual
    if (usuarioActual !is Cliente) {
        println("❌ Solo los clientes pueden cancelar pedidos.")
        return
    }

    val pedidos = usuarioActual.obtenerPedidos()

    if (pedidos.isEmpty()) {
        println("⚠ No tienes pedidos para cancelar.")
        return
    }

    println("\n----- TUS PEDIDOS -----")
    val pedidosOrdenados = pedidos.sortedBy { it.getFechaPedido() }
    pedidosOrdenados.forEach { pedido ->
        println("ID: ${pedido.getId()} - Estado: ${pedido.getEstado()} - Fecha: ${pedido.getFechaPedido()}")
    }

    println("Seleccione el número del pedido a cancelar:")

    val idPedido = readLine()?.toIntOrNull()

    val pedidoSeleccionado = pedidos.find { it.getId() == idPedido }


    if (pedidoSeleccionado == null) {
        throw PedidoNoEncontradoException("⚠ Pedido no encontrado.")
    }

    if (pedidoSeleccionado.getEstado() == EstadoPedido.Pendiente)
    {
        pedidoSeleccionado.cancelarPedido()
        println("Pedido cancelado con éxito.")
    }
     else {
         throw NosePudoCancelar("No se puede cancelar un pedido que no esté en estado Pendiente.")
     }
}

fun clienteModificaPedido() {
    val usuarioActual = SessionManager.usuarioActual
    if (usuarioActual !is Cliente) {
        println("❌ Solo los clientes pueden modificar pedidos.")
        return
    }

    val pedidos = usuarioActual.obtenerPedidos()

    if (pedidos.isEmpty()) {
        println("⚠ No tienes pedidos para modificar.")
        return
    }

    println("\n----- TUS PEDIDOS -----")
    val pedidosOrdenados = pedidos.sortedBy { it.getFechaPedido() }
    pedidosOrdenados.forEach { pedido ->
        println("ID: ${pedido.getId()} - Estado: ${pedido.getEstado()} - Fecha: ${pedido.getFechaPedido()}")
    }

    println("\nSeleccione el número del pedido a modificar:")
    val idPedido = readLine()?.toIntOrNull()

    val pedidoSeleccionado = pedidos.find { it.getId() == idPedido }


    if (pedidoSeleccionado == null) {
        throw PedidoNoEncontradoException("⚠ Pedido no encontrado.")
    }

    if (pedidoSeleccionado.getEstado() != EstadoPedido.Pendiente) {
        throw NoSePuedeModificarPedidoException("⚠ No se puede modificar un pedido que no esté en estado Pendiente.")
    }

    println("¿Qué desea modificar en el pedido?")
    println("1 - Modificar cantidad de productos")
    println("2 - Modificar forma de pago")
    val opcionModificar = readLine()?.toIntOrNull()

    when (opcionModificar) {
        1 -> {
            println("\nProductos en tu pedido:")
            val productos = pedidoSeleccionado.obtenerProductos()

            if (productos.isEmpty()) {
                println("⚠ El pedido no tiene productos para modificar.")
                return
            }

            productos.forEachIndexed { index, producto ->
                println("${index + 1} - ${producto.getNombre()} (Cantidad: ${pedidoSeleccionado.obtenerCantidadProducto(producto)})")
            }

            println("\nIngrese el número del producto que desea modificar:")
            val opcionProducto = readLine()?.toIntOrNull()

            if (opcionProducto == null || opcionProducto !in 1..productos.size) {
                println("⚠ Producto no válido.")
                return
            }

            val productoSeleccionado = productos[opcionProducto - 1]

            println("Ingrese la nueva cantidad para ${productoSeleccionado.getNombre()} (0 para eliminarlo):")
            val nuevaCantidad = readLine()?.toIntOrNull()

            if (nuevaCantidad == null || nuevaCantidad < 0) {
                println("⚠ Cantidad no válida.")
                return
            }

            try {
                pedidoSeleccionado.modificarCantidadProducto(productoSeleccionado, nuevaCantidad)
            } catch (e: NoSePuedeModificarPedidoException) {
                println(e.message)
            }
        }
        2 -> {
            println("Seleccione la nueva forma de pago:")
            FormaDePago.values().forEachIndexed { index, forma ->
                println("${index + 1} - ${forma.name}")
            }
            val nuevaFormaPago = readLine()?.toIntOrNull()

            if (nuevaFormaPago != null && nuevaFormaPago in 1..FormaDePago.values().size) {
                pedidoSeleccionado.elegirFormaDePago(FormaDePago.values()[nuevaFormaPago - 1])
                println("✅ Forma de pago modificada con éxito.")
            } else {
                println("⚠ Opción de forma de pago no válida.")
            }
        }
        else -> println("⚠ Opción no válida.")
    }
}

fun clienteVerSusPedidos() {
    val usuarioActual = SessionManager.usuarioActual
    if (usuarioActual !is Cliente) {
        println("❌ Solo los clientes pueden ver sus pedidos.")
        return
    }

    val pedidos = usuarioActual.obtenerPedidos()

    if (pedidos.isEmpty()) {
        println("No tienes pedidos registrados.")
        return
    }

    println("\n----- TUS PEDIDOS -----")
    val pedidosOrdenados = pedidos.sortedBy { it.getFechaPedido() }
    pedidosOrdenados.forEach { pedido ->
        pedido.mostrarPedido()
    }
}
