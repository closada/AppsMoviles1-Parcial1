/* Enum creado para los tipos de producto */
enum class TipoProducto {
    PlatoPrincipal,
    Postre,
    Entrada,
    Bebida
}

class Producto (
    private val nombre: String,
    private var precio: Float = 0.0f,
    private var descripcion: String,
    private var stock: Int,
    private var tipo: TipoProducto,
    private val descuento: Float = 0.0f
) {
    private val id: Int

    init {
        id = ++contadorIds
    }

    companion object {
        private var contadorIds = 0
    }

    fun getNombre(): String = nombre
    fun getPrecio(): Float = precio
    fun getStock(): Int = stock

    fun getId(): Int {
        return id
    }

    fun getPrecioConDescuento(): Float {
        return precio * (1 - descuento)
    }

    fun disminuirStock(cantidad: Int) {
        if (cantidad <= stock) {
            stock -= cantidad
        } else {
            throw StockInsuficienteException("No hay suficiente stock de $nombre. Disponible: $stock, requerido: $cantidad.")
        }
    }



    fun mostrarProducto() {
        println("ID: $id")
        println("Nombre: $nombre")
        println("Descripción: $descripcion")
        println("Precio: $${"%.2f".format(precio)}")
        println("Stock: $stock")
        println("Tipo: $tipo")
        println("Descuento aplicado: ${"%.2f".format(descuento * 100)}%")
        println("------------")
    }
}