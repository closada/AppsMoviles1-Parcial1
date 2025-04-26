//esta clase hereda a todos los tipos de usuarios la id mail usuario y contraseña
// para poder guardarlos despues en una coleccion y poder iniciar sesion
open class Personas(
    private val nombre: String,
    private val password: String = "1234",
    private val telefono: String,
    private val email: String
) {
    val id: Int

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
        println("Usuario agregado: ${usuario.getNombre()}")
        mostrarUsuarios()
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
}


