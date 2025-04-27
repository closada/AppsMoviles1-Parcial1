/* Enum creado para los tipos de producto */
enum class TipoProducto {
    PlatoPrincipal,
    Postre,
    Entrada,
    Bebida
}

class Producto (
    private var nombre: String,
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
    fun getId(): Int = id


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

    fun setNombre(nuevoNombre: String) {
        if (nuevoNombre.isNotBlank()) {
            // (opcionalmente podrías validar otras cosas)
            this.nombre = nuevoNombre
        }
    }

    fun setPrecio(nuevoPrecio: Float) {
        if (nuevoPrecio >= 0) {
            this.precio = nuevoPrecio
        }
    }

    fun setDescripcion(nuevaDescripcion: String) {
        if (nuevaDescripcion.isNotBlank()) {
            this.descripcion = nuevaDescripcion
        }
    }

    fun setStock(nuevoStock: Int) {
        if (nuevoStock >= 0) {
            this.stock = nuevoStock
        }
    }

    fun setTipo(nuevoTipo: TipoProducto) {
        this.tipo = nuevoTipo
    }


    fun mostrarProducto() {
        println("ID: $id")
        println("Nombre: $nombre")
        println("Descripción: $descripcion")
        println("Precio: $${"%.2f".format(precio)}")
        println("Stock: $stock")
        println("Tipo: $tipo")

        // Mostrar el descuento en porcentaje (por ejemplo, 20% en vez de 0.2)
        println("Descuento aplicado: ${"%.2f".format(descuento * 100)}%")

        // Mostrar el precio con descuento
        println("Precio con descuento: $${"%.2f".format(getPrecioConDescuento())}")

        println("------------")
    }
}