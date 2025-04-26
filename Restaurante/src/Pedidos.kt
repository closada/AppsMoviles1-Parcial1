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
    private val estado: EstadoPedido,
    private var montoTotal: Float = 0.0f
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



    fun obtenerProductos(): Map<Producto, Int> {
        return productosConCantidad
    }

    fun calcularTotal(): Float {
        var total = 0.0f
        for ((producto, cantidad) in productosConCantidad) {
            total += producto.getPrecio() * cantidad
        }
        return total
    }

    fun mostrarPedido() {
        println("Pedido ID: ${id} fecha: ${fechaPedido} estado: ${estado}")
        productosConCantidad.forEach { (producto, cantidad) ->
            println("${producto.getNombre()} x $cantidad")
        }
        println("Total: ${calcularTotal()}\n-------------\n")
    }

    fun getFechaPedido() : String = fechaPedido

    fun getEstado(): EstadoPedido = estado

}

