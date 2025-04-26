// Excepción de USUARIO NO ENCONTRADO
class UsuarioNoEncontradoException(message: String) : Exception(message)

// NO SE PUEDE ELIMINAR A SI MISMO
class NoSePuedeEliminarASiMismoException(message: String) : Exception(message)

// STOCK INSUFICIENTE EN PRODUCTO
class StockInsuficienteException(message: String) : Exception(message)
