//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val x = Administrador("admin","1234","1","mati@gmial.com")
    var usuario = Usuarios()
    while(true){
        inicioPantalla(usuario)
    }
}

fun inicioPantalla(usuario: Usuarios) {
    // iniciar o registrar secion llamando a la funcion que correspodna segun el imput
    // 1 para iniciar secion
    // 2 para registrar usuario cliente
    // 3 para cerrar el programa
    // cualquier otro valor sera rechasado y notificado de que no es calido

    println("selecione 1 para iniciar secion")
    println("selecione 2 para registrarce")
    println("selecione 3 para salir del programa")
    var inputSeleccion = readLine()
    val datoSano = inputSeleccion?.toIntOrNull()
    when(datoSano) {
        1 -> inicioSecion(usuario)
        2 -> registroUsuarioCliente(usuario)
        3 -> println("cerre xd")
        else -> println("numero no valido vuelva a intentar")
    }
}

fun inicioSecion(usuario: Usuarios) {
    //validar el usuario y contraseña guardados en algun arreglo de usuarios

    println("hola mundo")
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
    var listaUsuarios = listOf<Personas>()

    fun agregarUsuario(x:Personas){
        //println(x.getNombre())
        listaUsuarios.plus(x)
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

    fun getNombre():String{
        return nombre
    }
    fun getPassword():String{
        return password
    }
    fun getTelefono():String{
        return telefono
    }
    fun getEmail():String{
        return email
    }
}

class Administrador (nombre: String,password: String,tel: String, email: String): Personas(nombre,password,tel,email)
class Vendedor(nombre: String,password: String,tel: String, email: String): Personas(nombre,password,tel,email)
class Cliente(nombre: String,password: String,tel: String, email: String): Personas(nombre,password,tel,email)


class Pedido(clienteAsociado: Int,date: String){}