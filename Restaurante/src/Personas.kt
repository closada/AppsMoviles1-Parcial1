//esta clase hereda a todos los tipos de usuarios la id mail usuario y contraseña
// para poder guardarlos despues en una coleccion y poder iniciar sesion
open class Personas(
    private val nombre: String,
    private val password: String = "1234",
    private val telefono: String,
    private val email: String
) {
    private val id: Int

    /* se encarga de sumar el contador cada vez que se crea un nuevo objeto */
    init {
        id = ++contadorIds
    }

    /* es una instancia unica que puede almacenar informacion que sean COMUNES EN TODAS LAS INSTANCIAS DE LA CLASE*/
    companion object {
        private var contadorIds = 0
    }

    fun getId(): Int = id
    fun getNombre(): String = nombre
    fun getPassword(): String = password
    fun getTelefono(): String = telefono
    fun getEmail(): String = email
}


class Administrador (nombre: String,password: String,tel: String, email: String): Personas(nombre,password,tel,email)
class Vendedor(nombre: String,password: String,tel: String, email: String): Personas(nombre,password,tel,email)

/* la clase cliente suma el listado de pedidos dentro de su composicion */
class Cliente(
    nombre: String,
    password: String,
    tel: String,
    email: String
) : Personas(nombre, password, tel, email) {

    private val listaDePedidos = mutableListOf<Pedido>()

    fun agregarPedido(pedido: Pedido) {
        listaDePedidos.add(pedido)
    }

    fun obtenerPedidos(): List<Pedido> {
        return listaDePedidos
    }
}

/* Clase Usuarios que guarda el listado de usuarios de cualquier tipo */
class Usuarios {
    private val listaUsuarios = mutableListOf<Personas>()

    fun agregarUsuario(usuario: Personas) {
        listaUsuarios.add(usuario)
    }

    fun getListausuarios(): List<Personas> {
    return listaUsuarios
    }

    fun mostrarUsuarios() {
        println("----- LISTA DE USUARIOS -----")
        listaUsuarios.forEach { usuario ->
            val tipo = when (usuario) {
                is Cliente -> "Cliente"
                is Vendedor -> "Vendedor"
                is Administrador -> "Administrador"
                else -> "Desconocido"
            }

            println("Tipo: $tipo")
            println("Nombre: ${usuario.getNombre()}")
            println("Email: ${usuario.getEmail()}")
            println("Teléfono: ${usuario.getTelefono()}")
            println("-----------------------------")
        }
    }

    fun eliminarUsuarioPorEmail(email: String): Boolean {
        val usuarioAEliminar = listaUsuarios.find { it.getEmail() == email }
        return if (usuarioAEliminar != null) {
            listaUsuarios.remove(usuarioAEliminar)
            println("✅ Usuario con email $email eliminado correctamente.")
            true
        } else {
            println("❌ No se encontró un usuario con ese email.")
            false
        }
    }

}

/* este factory crea manualmente el tipo de usuario que se quiera crear - ESTA FUNCIONALIDAD SOLO LO VA A PODER HACER EL USUARIO ADMINISTRADOR */
object UsuarioFactory {

    fun crearCliente(): Cliente {
        print("Ingrese nombre:")
        val nombre = readLine() ?: ""
        print("Ingrese contraseña:")
        val pass = readLine() ?: ""
        print("Ingrese teléfono:")
        val tel = readLine() ?: ""
        print("Ingrese email:")
        val email = readLine() ?: ""
        return Cliente(nombre, pass, tel, email)
    }

    fun crearUsuarioComoAdmin(): Personas? {
        println("Seleccione tipo de usuario a crear:")
        println("1 - Administrador")
        println("2 - Vendedor")
        println("0 - Cancelar")

        return when (readLine()?.toIntOrNull()) {
            1 -> crearAdministrador()
            2 -> crearVendedor()
            else -> {
                println("Cancelando creación...")
                null
            }
        }
    }

    private fun crearAdministrador(): Administrador {
        print("Nombre:")
        val nombre = readLine() ?: ""
        print("Contraseña:")
        val pass = readLine() ?: ""
        print("Teléfono:")
        val tel = readLine() ?: ""
        print("Email:")
        val email = readLine() ?: ""
        return Administrador(nombre, pass, tel, email)
    }

    private fun crearVendedor(): Vendedor {
        print("Nombre:")
        val nombre = readLine() ?: ""
        print("Contraseña:")
        val pass = readLine() ?: ""
        print("Teléfono:")
        val tel = readLine() ?: ""
        print("Email:")
        val email = readLine() ?: ""
        return Vendedor(nombre, pass, tel, email)
    }
}



