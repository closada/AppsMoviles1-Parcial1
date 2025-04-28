enum class EstadoPedido {
    Pendiente,
    EnPreparacion,
    Enviado,
    Entregado,
    Cancelado
}

/* enum para la realizar la forma de pago */
enum class FormaDePago {
    EFECTIVO,
    TARJETA_CREDITO,
    TARJETA_DEBITO,
    MERCADO_PAGO
}

class Pedido(
    private val clienteAsociado: Cliente,
    private val fechaPedido: String,
    private var estado: EstadoPedido,
    private var montoTotal: Float = 0.0f,
    private var formaDePago: FormaDePago? = null


) {
    private val productosConCantidad = mutableMapOf<Producto, Int>()
    private val id: Int

    /* se encarga de sumar el contador cada vez que se crea un nuevo objeto */
    init {
        id = ++contadorIds
    }

    /* es una instancia unica que puede almacenar informacion que sean COMUNES EN TODAS LAS INSTANCIAS DE LA CLASE*/
    companion object {
        private var contadorIds = 0
    }


    fun getFechaPedido() : String = fechaPedido
    fun getEstado(): EstadoPedido = estado
    fun getId(): Int = id
    fun estaVacio(): Boolean {
        return productosConCantidad.isEmpty()
    }

    fun cambiarEstado(nuevoEstado: EstadoPedido) {
        estado = nuevoEstado
    }


    fun agregarProducto(producto: Producto, cantidad: Int = 1) {
        try {
            // Intenta disminuir el stock, si hay un error, lo lanza
            producto.disminuirStock(cantidad)

            // Si se pudo disminuir el stock, agregamos el producto al pedido
            productosConCantidad[producto] = productosConCantidad.getOrDefault(producto, 0) + cantidad
        } catch (e: StockInsuficienteException) {
            println("❌ No se pudo agregar ${producto.getNombre()} x$cantidad. ${e.message}")
        }
    }


    fun calcularTotal(): Float {
        var total = 0.0f

        // Mostrar pedidos de productos
        //println("Detalle de su pedido:")
        for ((producto, cantidad) in productosConCantidad) {
            val precioConDescuento = producto.getPrecioConDescuento()
            val precioTotalProducto = precioConDescuento * cantidad
            //println("${producto.getNombre()} x$cantidad: \$${"%.2f".format(precioConDescuento)} c/u - Total: \$${"%.2f".format(precioTotalProducto)}")
            total += precioTotalProducto
        }

        // Obtener descuento por cliente
        val descuentoCliente = clienteAsociado.getDescuentoCliente()
        val totalConDescuento = total * (1 - descuentoCliente)

        // Mostrar el descuento aplicado y el total final
        //println("Descuento aplicado por ser cliente: ${"%.2f".format(descuentoCliente * 100)}%")
        //println("El monto total a pagar es: \$${"%.2f".format(totalConDescuento)}")

        montoTotal = totalConDescuento
        return totalConDescuento
    }

    fun elegirFormaDePago(forma: FormaDePago) {
        formaDePago = forma
    }


    // mostrar el pedido con todos los detalles, incluyendo el total con descuento aplicado
    fun mostrarPedido() {
        println("Nro del pedido: $id")
        println("Fecha del pedido: $fechaPedido")
        println("Estado: $estado")
        println("Forma de pago: ${formaDePago ?: "No definida"}")
        mostrarProductosdelPedido()

        // mostrar el total del pedido con descuento aplicado
        val total = calcularTotal()
        println("Total con descuento: $${"%.2f".format(total)}\n")
    }

    fun mostrarProductosdelPedido() {
        // mostrar productos con sus cantidades y precios con descuento
        productosConCantidad.forEach { (producto, cantidad) ->
            val precioConDescuento = producto.getPrecioConDescuento()
            println("${producto.getNombre()} x $cantidad - Precio con descuento: $${"%.2f".format(precioConDescuento)}")
        }
    }
    // Metodo para cancelar un pedido
    fun cancelarPedido() {
        if (estado == EstadoPedido.Pendiente) {
            /* modificar estado*/
            estado = EstadoPedido.Cancelado

            // Devolver el stock de todos los productos del pedido
            productosConCantidad.forEach { (producto, cantidad) ->
                producto.aumentarStock(cantidad)
            }

        } else {
            throw NosePudoCancelar("No se puede cancelar un pedido que no está en estado Pendiente.")
        }
    }

    // Metodo para obtener la lista de productos en el pedido
    fun obtenerProductos(): List<Producto> {
        return productosConCantidad.keys.toList()  // Devuelve solo los productos
    }


    // Modificar la cantidad de un producto en el pedido
    fun modificarCantidadProducto(producto: Producto, nuevaCantidad: Int) {
        if (productosConCantidad.containsKey(producto)) {
            val cantidadActual = productosConCantidad[producto] ?: 0
            val diferencia = nuevaCantidad - cantidadActual

            if (nuevaCantidad <= 0) {
                // Si la nueva cantidad es 0 o menos, eliminar el producto del pedido
                eliminarProducto(producto)
                println("Producto eliminado del pedido: ${producto.getNombre()}")
                return
            }

            if (producto.getStock() >= diferencia) {
                productosConCantidad[producto] = nuevaCantidad
                producto.disminuirStock(diferencia)
                println("✅ Producto actualizado: ${producto.getNombre()} x$nuevaCantidad")
            } else {
                throw NoSePuedeModificarPedidoException("❌ No se puede modificar. Stock insuficiente.")
            }
        } else {
            throw NoSePuedeModificarPedidoException("❌ El producto no está en el pedido.")
        }
    }

    // Eliminar un producto del pedido
    fun eliminarProducto(producto: Producto) {
        productosConCantidad.remove(producto)
    }

    fun obtenerCantidadProducto(producto: Producto): Int {
        return productosConCantidad[producto] ?: 0
    }


}
