fun menuVendedor() {
    while (true) {
        println("\n--- MENÚ VENDEDOR: ${SessionManager.usuarioActual?.getNombre() ?: "N/A"} ---")
        println("1 - Crear nuevo producto")
        println("2 - Modificar un producto existente")
        println("3 - Modificar el estado de un pedido existente")
        println("0 - Cerrar sesión")

        when (readLine()?.toIntOrNull()) {
            1 -> crearNuevoProducto()
            2 -> { try {
                modificarProducto()
            }catch (e: ProductoNoEncontradoException) {
                println(e.message)
            }
            }
            3 -> modificarEstadoPedido()
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

    val nuevoProducto = Producto(nombre, precio, descripcion, stock, tipoSeleccionado)
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

        0 -> println("Modificación cancelada.")

        else -> println("⚠ Opción inválida.")
    }
}

fun modificarEstadoPedido() {
    println("cambiado")
}

