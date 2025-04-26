/* SINGLETON QUE GUARDA CUAL ES EL USUARIO QUE ESTA OPERANDO EN EL SISTEMA */
object SessionManager {
    var usuarioActual: Personas? = null
}


/* SINGLETON QUE GUARDA TODOS LOS USUARIOS DEL SISTEMA Y + - ESTILO BD */
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
fun menuPrincipal() {
    while (true) {
        println("\n--- MENÚ PRINCIPAL ---")
        println("1 - Iniciar sesión")
        println("2 - Registrarse como Cliente")
        println("3 - Salir del programa")

        when (readLine()?.toIntOrNull()) {
            1 -> {

                /* iniciar sesion! */
                print("Ingrese email:")
                val email = readLine() ?: ""
                print("Ingrese contraseña:")
                val pass = readLine() ?: ""

                val usuario = SessionBD.sistemaUsuarios.getListausuarios().find {
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
                SessionBD.sistemaUsuarios.agregarUsuario(nuevoCliente)
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
    while (true) {
        println("\n--- MENÚ CLIENTE: " + (SessionManager.usuarioActual?.getNombre() ?: "N/A") + " ---")
        println("1 - Hacer un pedido")
        println("2 - Cancelar un pedido (atencion! solo en estado Pendiente)")
        println("3 - Modificar un pedido (atencion! solo en estado Pendiente)")
        println("4 - Ver mis pedidos")
        println("0 - Cerrar sesión")

        when (readLine()?.toIntOrNull()) {
            1 -> {
                println("hecho el pedido!")
                }
            2 -> println("cancelado")
            3 -> println("modificado")
            4 -> println("vistos")
            0 -> {
                println("Sesión de administrador finalizada.")
                break
            }
            else -> println("Opción inválida")
        }
    }
}

fun menuVendedor() {
    while (true) {
        println("\n--- MENÚ VENDEDOR: " + (SessionManager.usuarioActual?.getNombre() ?: "N/A") + " ---")
        println("1 - Crear nuevo producto")
        println("2 - modificar un producto")
        println("3 - modificar el estado de un pedido existente")
        println("0 - Cerrar sesión")

        when (readLine()?.toIntOrNull()) {
            1 -> {
                println("creado el producto!")
                }
            2 -> println("modificado el producto!")
            3 -> println("modificar el estado de un pedido")
            0 -> {
                println("Sesión de administrador finalizada.")
                break
            }
            else -> println("Opción inválida")
        }
    }
}
fun menuAdministrador() {
    while (true) {
        println("\n--- MENÚ ADMINISTRADOR " + (SessionManager.usuarioActual?.getNombre() ?: "N/A") + "---")
        println("1 - Crear nuevo usuario (admin o vendedor)")
        println("2 - Eliminar un usuario (admin o vendedor)")
        println("3 - Ver todos los usuarios")
        println("4 - Ver reportes")
        println("0 - Cerrar sesión")

        when (readLine()?.toIntOrNull()) {
            1 -> {
                val nuevoUsuario = UsuarioFactory.crearUsuarioComoAdmin()
                if (nuevoUsuario != null) {
                    SessionBD.sistemaUsuarios.agregarUsuario(nuevoUsuario)
                }
            }
            2 -> println("eliminado el usuario!")
            3 -> SessionBD.sistemaUsuarios.mostrarUsuarios()
            4 -> println("armar submenu con 3 opciones de reportes: 1) Listar los pedidos de un cliente, ordenados por fecha.\n" +
                    "Reportar cuáles clientes hicieron más de un pedido.\n" +
                    "Mostrar total recaudado por productos vendidos (suma de precios de productos en pedidos entregados).\n")
            0 -> {
                println("Sesión de administrador finalizada.")
                break
            }
            else -> println("Opción inválida")
        }
    }
}
