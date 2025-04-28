// Excepción de USUARIO NO ENCONTRADO
class UsuarioNoEncontradoException(message: String) : Exception(message)

// NO SE PUEDE ELIMINAR A SI MISMO
class NoSePuedeEliminarASiMismoException(message: String) : Exception(message)

// STOCK INSUFICIENTE EN PRODUCTO
class StockInsuficienteException(message: String) : Exception(message)

// PRODUCTO NO ENCONTRADO
class ProductoNoEncontradoException(message: String) : Exception(message)

// PEDIDO NO ENCONTRADO
class PedidoNoEncontradoException(message: String) : Exception(message)

// ESTADO NO CANCEABLE
class NosePudoCancelar(message: String) : Exception(message)

// PEDIDO NO SE PUEDE MODIFICAR
class NoSePuedeModificarPedidoException (message: String) : Exception(message)
