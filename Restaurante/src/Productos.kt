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
    private var tipo: TipoProducto
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
        println("Precio: $precio")
        println("Stock: $stock")
        println("Tipo: $tipo")
        println("------------")
    }
}
