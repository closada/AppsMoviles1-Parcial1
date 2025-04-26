/* SINGLETON QUE GUARDA CUAL ES EL USUARIO QUE ESTA OPERANDO EN EL SISTEMA */
object SessionManager {
    var usuarioActual: Personas? = null
}


/* SINGLETON QUE GUARDA TODOS LOS USUARIOS DEL SISTEMA - ESTILO BD */
object SessionBD {
    val sistemaUsuarios = Usuarios()

    init {
        // Crear 2 clientes
        val cliente1 = Cliente("Sofía", "clave1", "1111111111", "sofia@email.com")
        val cliente2 = Cliente("Tomás", "clave2", "2222222222", "tomas@email.com")

        // Crear 1 vendedor
        val vendedor1 = Vendedor("Lucía", "clave3", "3333333333", "lucia@email.com")

        // Crear 1 administrador
        val admin1 = Administrador("AdminJuan", "adminpass", "4444444444", "juan@admin.com")

        // Agregarlos al sistema
        sistemaUsuarios.agregarUsuario(cliente1)
        sistemaUsuarios.agregarUsuario(cliente2)
        sistemaUsuarios.agregarUsuario(vendedor1)
        sistemaUsuarios.agregarUsuario(admin1)
    }

    fun mostrarUsuarios() {
        sistemaUsuarios.mostrarUsuarios()
    }
}



/****** MENU PRINCIPAL *********/
fun menuPrincipal(usuarios: Usuarios) {
    while (true) {
        println("\n--- MENÚ PRINCIPAL ---")
        println("1 - Iniciar sesión")
        println("2 - Registrarse como Cliente")
        println("3 - Salir del programa")

        when (readLine()?.toIntOrNull()) {
            1 -> {

                /* iniciar sesion! */
                println("Ingrese email:")
                val email = readLine() ?: ""
                println("Ingrese contraseña:")
                val pass = readLine() ?: ""

                val usuario = usuarios.getListausuarios().find {
                    it.getEmail() == email && it.getPassword() == pass
                }

                if (usuario != null) {
                    SessionManager.usuarioActual = usuario
                    println("Bienvenido/a ${usuario.getNombre()}!")

                    when (usuario) {
                        is Administrador -> menuAdministrador()
                        is Vendedor -> menuVendedor()
                        is Cliente -> menuCliente()
                    }
                } else {
                    println("Credenciales inválidas.")
                }
            }

            2 -> {
                val nuevoCliente = UsuarioFactory.crearCliente()
                usuarios.agregarUsuario(nuevoCliente)
                println("Cliente registrado con éxito. Ahora puede iniciar sesión.")
            }

            3 -> {
                println("¡Gracias por usar el sistema!")
                break
            }

            else -> println("Opción no válida.")
        }
    }
}

fun menuCliente() {
    println("menu cliente!")
}

fun menuVendedor() {
    println("menu cliente!")
}
fun menuAdministrador() {
    while (true) {
        println("\n--- MENÚ ADMINISTRADOR ---")
        println("1 - Crear nuevo usuario (admin o vendedor)")
        println("2 - Ver todos los usuarios")
        println("0 - Cerrar sesión")

        when (readLine()?.toIntOrNull()) {
            1 -> {
                val nuevoUsuario = UsuarioFactory.crearUsuarioComoAdmin()
                if (nuevoUsuario != null) {
                    SessionBD.sistemaUsuarios.agregarUsuario(nuevoUsuario)
                }
            }
            2 -> SessionBD.sistemaUsuarios.mostrarUsuarios()
            0 -> {
                println("Sesión de administrador finalizada.")
                break
            }
            else -> println("Opción inválida")
        }
    }
}
