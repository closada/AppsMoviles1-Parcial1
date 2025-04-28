fun menuVendedor() {
    while (true) {
        println("\n--- MENÚ VENDEDOR: ${SessionManager.usuarioActual?.getNombre() ?: "N/A"} ---")
        println("1 - Crear nuevo producto")
        println("2 - Modificar un producto existente")
        println("3 - Modificar el estado de un pedido existente")
        println("4 - Ver todos los productos")
        println("5 - Ver todos los pedidos")
        println("0 - Cerrar sesión")

        when (readLine()?.toIntOrNull()) {
            1 -> crearNuevoProducto()
            2 -> { try {
                modificarProducto()
            }catch (e: ProductoNoEncontradoException) {
                println(e.message)
            }
            }
            3 -> {
                try {
                    modificarEstadoPedido()
                } catch(e: PedidoNoEncontradoException) {
                    println(e.message)
                }
            }

            4 -> SessionBD.mostrarProductos()
            5 -> SessionBD.mostrarPedido()
            0 -> {
                cerrarSesion()
                break
            }
            else -> println("⚠ Opción inválida.")
        }
    }
}
fun crearNuevoProducto() {
    println("Ingrese nombre del producto:")
    val nombre = readLine() ?: ""

    println("Ingrese precio del producto:")
    val precio = readLine()?.toFloatOrNull() ?: run {
        println("⚠ Precio inválido. Creación cancelada.")
        return
    }

    println("Ingrese stock inicial:")
    val stock = readLine()?.toIntOrNull() ?: run {
        println("⚠ Stock inválido. Creación cancelada.")
        return
    }

    println("Ingrese una breve descripción del producto:")
    val descripcion = readLine() ?: ""

    println("Seleccione el tipo de producto:")
    TipoProducto.values().forEachIndexed { index, tipo ->
        println("${index + 1} - ${tipo.name}")
    }
    val tipoSeleccionado = readLine()?.toIntOrNull()?.let {
        TipoProducto.values().getOrNull(it - 1)
    } ?: run {
        println("⚠ Tipo inválido. Creación cancelada.")
        return
    }

    println("Ingrese el descuento (en porcentaje, por ejemplo 20 para 20%):")
    val descuentoPorcentaje = readLine()?.toFloatOrNull()
    val descuento = if (descuentoPorcentaje != null && descuentoPorcentaje in 0.0..100.0) {
        descuentoPorcentaje / 100f // lo pasamos a decimal
    } else {
        println("⚠ Descuento inválido. Se aplicará 0%.")
        0.0f
    }

    val nuevoProducto = Producto(nombre, precio, descripcion, stock, tipoSeleccionado, descuento)
    SessionBD.productosDisponibles.add(nuevoProducto)

    println("✅ Producto '${nuevoProducto.getNombre()}' creado con éxito.")
}


fun modificarProducto() {
    SessionBD.mostrarProductos()

    println("Ingrese el ID del producto que desea modificar:")
    val idProducto = readLine()?.toIntOrNull()

    val producto = SessionBD.productosDisponibles.find { it.getId() == idProducto }

    if (producto == null) {
        throw ProductoNoEncontradoException("⚠ Producto no encontrado.")
    }

    println("¿Qué desea modificar?")
    println("1 - Nombre")
    println("2 - Precio")
    println("3 - Descripción")
    println("4 - Stock")
    println("5 - Tipo de producto")
    println("6 - Descuento")
    println("0 - Cancelar")


    when (readLine()?.toIntOrNull()) {
        1 -> {
            print("Ingrese el nuevo nombre: ")
            val nuevoNombre = readLine() ?: ""
            producto.setNombre(nuevoNombre)
            println("✅ Nombre actualizado.")
        }

        2 -> {
            print("Ingrese el nuevo precio: ")
            val nuevoPrecio = readLine()?.toFloatOrNull()
            if (nuevoPrecio != null) {
                producto.setPrecio(nuevoPrecio)
                println("✅ Precio actualizado.")
            } else {
                println("⚠ Precio inválido.")
            }
        }

        3 -> {
            print("Ingrese la nueva descripción: ")
            val nuevaDescripcion = readLine() ?: ""
            producto.setDescripcion(nuevaDescripcion)
            println("✅ Descripción actualizada.")
        }

        4 -> {
            print("Ingrese el nuevo stock: ")
            val nuevoStock = readLine()?.toIntOrNull()
            if (nuevoStock != null) {
                producto.setStock(nuevoStock)
                println("✅ Stock actualizado.")
            } else {
                println("⚠ Stock inválido.")
            }
        }

        5 -> {
            println("Seleccione el nuevo tipo de producto:")
            TipoProducto.values().forEachIndexed { index, tipo ->
                println("${index + 1} - ${tipo.name}")
            }
            val tipoSeleccionado = readLine()?.toIntOrNull()?.let {
                TipoProducto.values().getOrNull(it - 1)
            }

            if (tipoSeleccionado != null) {
                producto.setTipo(tipoSeleccionado)
                println("✅ Tipo de producto actualizado.")
            } else {
                println("⚠ Tipo inválido.")
            }
        }

        6 -> {
            print("Ingrese el nuevo descuento (en porcentaje, por ejemplo 20 para 20%): ")
            val nuevoDescuentoPorcentaje = readLine()?.toFloatOrNull()
            if (nuevoDescuentoPorcentaje != null && nuevoDescuentoPorcentaje in 0.0..100.0) {
                // Como en tu clase Producto el descuento es val (inmutable),
                // para cambiarlo deberías agregar un setter en la clase Producto
                producto.setDescuento(nuevoDescuentoPorcentaje / 100f)
                println("✅ Descuento actualizado.")
            } else {
                println("⚠ Descuento inválido.")
            }
        }

        0 -> println("Modificación cancelada.")

        else -> println("⚠ Opción inválida.")
    }
}

fun modificarEstadoPedido() {
    // mostrar la lista de pedidos
    SessionBD.mostrarPedido()

    println("Ingrese el ID del Pedido que desea modificar:")
    val idPedido = readLine()?.toIntOrNull()

    if (idPedido == null || idPedido <= 0) {
        println("⚠ ID inválido.")
        return
    }

    val pedido = SessionBD.obtenerTodosLosPedidos().find { it.getId() == idPedido }
        ?: throw PedidoNoEncontradoException("⚠ Pedido no encontrado.")

    println(
        "Seleccione el estado a cambiar \n" +
                "(Pendiente -> EnPreparacion -> Enviado -> Entregado) \n" +
                "    Siguiente estado marque 1,\n" +
                "    Cancelado marque 2,\n" +
                "    Cancelar acción marque 0"
    )

    when (readLine()?.toIntOrNull()) {
        1 -> {
            val estados = EstadoPedido.values()
            val estadoActual = pedido.getEstado()
            val siguienteIndice = estadoActual.ordinal + 1
            if (siguienteIndice < estados.size - 1) {
                pedido.cambiarEstado(estados[siguienteIndice])   // pasa al siguiente
                println("✅ Estado actualizado a: ${estados[siguienteIndice]}")
            } else {
                println("⚠ Ya estás en el último estado.")
            }
        }
        2 -> {
            try {
                cancelarPedido(pedido)
                println("✅ Pedido cancelado.")
            } catch (e: NosePudoCancelar) {
                println("❌ ${e.message}")
            }
        }
        0 -> println("⏩ Modificación cancelada.")
        else -> println("⚠ Opción inválida.")
    }
}

fun cancelarPedido(pedido:Pedido){
    if(pedido.getEstado()==EstadoPedido.Pendiente){
        pedido.cancelarPedido()
    }else{
        throw NosePudoCancelar("⚠ No se puede cancelar un pedido que no esté en estado Pendiente.");
    }
}
