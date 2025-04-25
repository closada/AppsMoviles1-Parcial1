fun main() {
    val name = "Kotlin"
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, " + name + "!")
    val x = Administrador("admin","1234","1","mati@gmial.com")
    var usuario = Usuarios()
    while(true){
        inicioPantalla(usuario)
    }
}

fun inicioPantalla(usuario: Usuarios) {
    // iniciar o registrar secion llamando a la funcion que correspodna segun el imput
    // 1 para iniciar sesion
    // 2 para registrar usuario cliente
    // 3 para cerrar el programa
    // cualquier otro valor sera rechasado y notificado de que no es calido



    println("selecione 1 para iniciar sesion")
    println("selecione 2 para registrarsee")
    println("selecione 3 para salir del programa")
    var inputSeleccion = readLine()
    val datoSano = inputSeleccion?.toIntOrNull()
    when(datoSano) {
        1 -> inicioSesion(usuario)
        2 -> registroUsuarioCliente(usuario)
        3 -> println("cerre xd")
        else -> println("numero no valido vuelva a intentar")
    }
}

fun inicioSesion(usuario: Usuarios) {
    //validar el usuario y contraseña guardados en algun arreglo de usuarios
    // con el for hago que pueda recorrer a mano los usuarios guardados
    // utilizo el orEmpty() por si llega hacer null
    println("Ingrese nombre de usuario:")
    val nombre = readLine().orEmpty()
    println("Ingrese contraseña:")
    val password = readLine().orEmpty()

    var encontrado = false

    for (persona in usuario.listaUsuarios) {
        if (persona.getNombre() == nombre && persona.getPassword() == password) {
            println("Bienvenido, ${persona.getNombre()}!")
            println("Tus datos personales:")
            println("Nombre: ${persona.getNombre()}")
            println("Email: ${persona.getEmail()}")
            println("Telefono: ${persona.getTelefono()}")
            encontrado = true
            break
        }
    }

    if (!encontrado) {
        println("Usuario o contraseña incorrectos.")
    }
}

fun registroUsuarioCliente(usuario: Usuarios) {
    //guarda los datos en el arreglo de usuarios
    //pido todos los datos y luego llamo a la funcion agregarUsuario de la clase Usuarios


    println("Ingrese nombre:")
    val nombre = readLine().orEmpty()
    println("Ingrese contraseña:")
    val password = readLine().orEmpty()
    println("Ingrese teléfono:")
    val tel = readLine().orEmpty()
    println("Ingrese email:")
    val email = readLine().orEmpty()

    val x = Cliente(nombre,password,tel,email);
    //println(x.getNombre())
    usuario.agregarUsuario(x)
}

class Usuarios(){
    var listaUsuarios = mutableListOf<Personas>()

    fun agregarUsuario(x:Personas){
        //println(x.getNombre())
        listaUsuarios.add(x)
        //x.setID() = listaUsuarios.lastIndexOf(x)+1
        MostrarUsuarios()
    }

    fun MostrarUsuarios(){
        println("llegue hasta MostrarUsuarios")
        listaUsuarios.forEach { println("Nombre: " + it.getNombre()) }
    }

}

//esta clase hereda a todos los tipos de usuarios la id mail usuario y contraseña
// para poder guardarlos despues en una coleccion y poder iniciar secion
open class Personas(nombre: String,password: String,tel: String, email: String){
    //a revisar
    //val id
    private val nombre = nombre
    private val password= password
    private val telefono=tel
    private val email=email
    private var listaDePedidos = listOf<Pedido>()


    fun getNombre(): String = nombre
    fun getPassword(): String = password
    fun getTelefono(): String = telefono
    fun getEmail(): String = email
}


class Administrador (nombre: String,password: String,tel: String, email: String): Personas(nombre,password,tel,email)
class Vendedor(nombre: String,password: String,tel: String, email: String): Personas(nombre,password,tel,email)
class Cliente(nombre: String,password: String,tel: String, email: String): Personas(nombre,password,tel,email)


class Pedido(clienteAsociado: Int,date: String){}