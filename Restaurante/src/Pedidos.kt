enum class EstadoPedido {
    Pendiente,
    EnPreparacion,
    Enviado,
    Entregado,
    Cancelado
}

class Pedido(
    private val clienteAsociado: Cliente,
    private val fechaPedido: String,
    private var estado: EstadoPedido
) {
    private val productosConCantidad = mutableMapOf<Producto, Int>()

    //  aca se agrega productos al pedido, asegurándose de que haya suficiente stock
    fun agregarProducto(producto: Producto, cantidad: Int = 1) {
        if (producto.getStock() >= cantidad) {
            // se suma la cantidad al pedido
            productosConCantidad[producto] = productosConCantidad.getOrDefault(producto, 0) + cantidad
            producto.disminuirStock(cantidad)
        } else {
            println("❌ No se pudo agregar ${producto.getNombre()} x$cantidad. Stock insuficiente.")
        }
    }

    // se calcula el total del pedido con descuentos aplicados
    fun calcularTotal(): Float {
        var total = 0.0f
        for ((producto, cantidad) in productosConCantidad) {
            total += producto.getPrecioConDescuento() * cantidad
        }
        // aplica el descuento del cliente (si tiene)
        val descuentoCliente = clienteAsociado.getDescuentoCliente()
        return total * (1 - descuentoCliente)
    }

    // mostrar el pedido con todos los detalles, incluyendo el total con descuento aplicado
    fun mostrarPedido() {
        println("Pedido del cliente: ${clienteAsociado.getNombre()}")
        println("Fecha del pedido: $fechaPedido")
        println("Estado: $estado")

        // mostrar productos con sus cantidades y precios con descuento
        productosConCantidad.forEach { (producto, cantidad) ->
            val precioConDescuento = producto.getPrecioConDescuento()
            println("${producto.getNombre()} x $cantidad - Precio con descuento: $${"%.2f".format(precioConDescuento)}")
        }

        // mostrar el total del pedido con descuento aplicado
        val total = calcularTotal()
        println("Total con descuento: $${"%.2f".format(total)}")
    }
}

fun realizarPedido(cliente: Cliente, productosDisponibles: List<Producto>) {
    println("----- REALIZAR PEDIDO -----")

    val nuevoPedido = Pedido(
        clienteAsociado = cliente,
        fechaPedido = java.time.LocalDate.now().toString(),   //Te da la fecha actual como texto
        estado = EstadoPedido.Pendiente
    )

    var seguirAgregando = true

    while (seguirAgregando) {
        println("Productos disponibles:")
        productosDisponibles.forEachIndexed { index, producto ->
            println("${index + 1}. ${producto.getNombre()} - $${"%.2f".format(producto.getPrecio())} - Stock: ${producto.getStock()}")
        }

        print("Ingrese el número del producto que desea agregar (o 0 para finalizar): ")
        val seleccion = readLine()?.toIntOrNull()

        if (seleccion == null || seleccion !in 1..productosDisponibles.size) {
            if (seleccion == 0) {
                seguirAgregando = false
            } else {
                println("❌ Selección inválida. Intente de nuevo.")
            }
        } else {
            val productoSeleccionado = productosDisponibles[seleccion - 1]
            print("Ingrese la cantidad que desea: ")
            val cantidad = readLine()?.toIntOrNull() ?: 1
            nuevoPedido.agregarProducto(productoSeleccionado, cantidad)
        }
    }

    if (nuevoPedido.calcularTotal() > 0) {
        cliente.agregarPedido(nuevoPedido)
        println("✅ Pedido realizado exitosamente.")
        nuevoPedido.mostrarPedido()
    } else {
        println("❌ No se agregó ningún producto al pedido. Pedido cancelado.")
    }
}
