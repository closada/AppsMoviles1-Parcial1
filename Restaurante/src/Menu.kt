/* SINGLETON QUE GUARDA CUAL ES EL USUARIO QUE ESTA OPERANDO EN EL SISTEMA */
object SessionManager {
    var usuarioActual: Personas? = null
}


/* SINGLETON QUE GUARDA TODOS LOS USUARIOS DEL SISTEMA Y + - ESTILO BD */
object SessionBD {
    val sistemaUsuarios = Usuarios()
    val productosDisponibles = mutableListOf<Producto>()

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

        // Crear productos (algunos sin stock)
        val producto1 = Producto("Milanesa con papas", 25000f, "Clásica milanesa con guarnición", 10, TipoProducto.PlatoPrincipal)
        val producto2 = Producto("Ensalada César", 18000f, "Lechuga, pollo, croutons y aderezo", 5, TipoProducto.Entrada)
        val producto3 = Producto("Coca Cola", 2500f, "Botella 500ml", 5, TipoProducto.Bebida) // sin stock
        val producto4 = Producto("Flan con dulce", 6200f, "Postre casero", 3, TipoProducto.Postre)
        val producto5 = Producto("Agua mineral", 1200f, "Botella 500ml", 2, TipoProducto.Bebida)

        // Agregarlos a la lista de productos
        productosDisponibles.addAll(listOf(producto1, producto2, producto3, producto4, producto5))

        // Crear pedidos
        val pedido1 = Pedido(cliente1, "2025-04-25", EstadoPedido.Entregado)
        pedido1.agregarProducto(producto1, 2) /* 2 milanesas */
        pedido1.agregarProducto(producto5, 1) /* 1 agua*/
        cliente1.agregarPedido(pedido1)

        val pedido2 = Pedido(cliente2, "2025-04-24", EstadoPedido.Pendiente)
        pedido2.agregarProducto(producto2, 1) /* 1 ensalada */
        pedido2.agregarProducto(producto4, 1) /* 1 flan */
        pedido2.agregarProducto(producto3, 1) /* 1 coca */
        cliente2.agregarPedido(pedido2)

        val pedido3 = Pedido(cliente2, "2025-04-25", EstadoPedido.Pendiente)
        pedido3.agregarProducto(producto1, 4) /* 4 milanesas */
        pedido3.agregarProducto(producto5, 1) /* 1 agua*/
        pedido3.agregarProducto(producto3, 3) /* 3 cocas*/
        cliente2.agregarPedido(pedido3)

    }

    fun mostrarUsuarios() {
        sistemaUsuarios.mostrarUsuarios()
    }

    fun mostrarProductos() {
        println("----- PRODUCTOS DISPONIBLES -----")
        productosDisponibles.forEach { it.mostrarProducto() }
    }

    fun mostrarPedidosCliente(cliente: Cliente) {
        println("----- PEDIDOS DE ${cliente.getNombre()} -----")
        cliente.obtenerPedidos().forEach { it.mostrarPedido() }
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
            1 -> iniciarSesion()
            2 -> registrarse()
            3 -> {
                println("¡Gracias por usar el sistema!")
                break
            }

            else -> println("Opción no válida.")
        }
    }
}

/* funcion para cerrar sesion el usuario logueado */
fun cerrarSesion() {
    SessionManager.usuarioActual = null
    println("Sesión finalizada.")
}

fun registrarse() {
    val nuevoCliente = UsuarioFactory.crearCliente()
    SessionBD.sistemaUsuarios.agregarUsuario(nuevoCliente)
    println("Cliente registrado con éxito. Ahora puede iniciar sesión.")
}

fun iniciarSesion() {
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