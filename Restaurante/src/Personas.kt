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

    fun obtenerTotaldePedidos(): Float {
        var total = 0.0f
        for (pedido in listaDePedidos) {
            total += pedido.calcularTotal()
        }
        return total
    }

    fun getDescuentoCliente(): Float {
        return if (listaDePedidos.size > 3) 0.05f else 0.0f
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

    fun eliminarUsuarioPorEmail(email: String) {
        if (email == (SessionManager.usuarioActual?.getEmail() ?: "")) {
            throw NoSePuedeEliminarASiMismoException("No puedes eliminarte a ti mismo.")
        }

        val usuarioAEliminar = listaUsuarios.find { it.getEmail() == email }
        if (usuarioAEliminar != null) {
            listaUsuarios.remove(usuarioAEliminar)
            println("✅ Usuario con email $email eliminado correctamente.")
        } else {
            throw UsuarioNoEncontradoException("No se encontró un usuario con el email: $email")
        }
    }

}
object UsuarioFactory {

    private fun esEmailValido(email: String): Boolean {
        val regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$".toRegex()
        return regex.matches(email)
    }

    private fun textoSeguro(entrada: String): Boolean {
        val regex = "^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ@._ -]*$".toRegex()
        return regex.matches(entrada)
    }

    private fun leerCampoSeguro(nombreCampo: String, validarEmail: Boolean = false, validarTelefono: Boolean = false): String {
        var entrada: String
        do {
            print("Ingrese $nombreCampo: ")
            entrada = readLine()?.trim() ?: ""

            if (entrada.isEmpty()) {
                println("❌ $nombreCampo no puede estar vacío.")
                continue
            }

            if (validarTelefono) {
                if (!entrada.matches("^\\d{8,15}$".toRegex())) {
                    println("❌ El teléfono debe contener solo números y tener entre 8 y 15 dígitos.")
                    continue
                }
            } else {
                if (!textoSeguro(entrada)) {
                    println("❌ $nombreCampo contiene caracteres no permitidos.")
                    continue
                }
                if (validarEmail && !esEmailValido(entrada)) {
                    println("❌ Formato de email no válido.")
                    continue
                }
            }

            break
        } while (true)

        return entrada
    }


    fun crearCliente(): Cliente {
        val email = leerCampoSeguro("email", validarEmail = true)
        val pass = leerCampoSeguro("contraseña")
        val nombre = leerCampoSeguro("nombre")
        val tel = leerCampoSeguro("teléfono", validarTelefono = true)

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
        val email = leerCampoSeguro("email", validarEmail = true)
        val pass = leerCampoSeguro("contraseña")
        val nombre = leerCampoSeguro("nombre")
        val tel = leerCampoSeguro("teléfono", validarTelefono = true)

        return Administrador(nombre, pass, tel, email)
    }


    private fun crearVendedor(): Vendedor {
        val email = leerCampoSeguro("email", validarEmail = true)
        val pass = leerCampoSeguro("contraseña")
        val nombre = leerCampoSeguro("nombre")
        val tel = leerCampoSeguro("teléfono", validarTelefono = true)

        return Vendedor(nombre, pass, tel, email)
    }

}
