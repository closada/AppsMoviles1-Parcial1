
# Sistema de Gestión de Pedidos - Kotlin Console App

## Participantes
- **Camila Losada** - Desarrollo de funciones de administrador.
- **Mailen Acosta Vera** - Desarrollo de funciones de cliente.
- **Matias Mangilvaore** - Desarrollo de funciones de vendedor e integración general.

---

## Organización del trabajo
El proyecto se dividió en módulos:
- **Administrador** (`func_administrador.kt`): Crear, eliminar usuarios, ver reportes.
- **Cliente** (`func_cliente.kt`): Realizar, modificar, cancelar pedidos.
- **Vendedor** (`func_vendedor.kt`): Crear/modificar productos, cambiar estado de pedidos.
- **Clases principales** (`Personas.kt`, `Pedidos.kt`, `Productos.kt`): Representan usuarios, productos, pedidos.
- **Gestión del sistema** (`Menu.kt`, `Main.kt`): Manejo de sesiones, usuarios, productos precargados.
- **Manejo de excepciones** (`excepciones.kt`): Definición de errores personalizados.

Se trabajo en conjunto en los modulos y en funcionalidades del programa

---

## Instrucciones de ejecución


**Interacción a través del menú principal:**
   - Iniciar sesión (usuarios precargados).
   - Registrarse como cliente.
   - Salir del programa.

---

## Ejemplo de Ejecución General

```bash
--- MENÚ PRINCIPAL ---
1 - Iniciar sesión
2 - Registrarse como Cliente
3 - Salir del programa

> 1
Ingrese email: juan@admin.com
Ingrese contraseña: adminpass
Bienvenido/a AdminJuan!

--- MENÚ ADMINISTRADOR ---
1 - Crear nuevo usuario (admin o vendedor)
2 - Eliminar un usuario (admin o vendedor)
3 - Ver todos los usuarios
4 - Ver reportes
0 - Cerrar sesión
```

---

## Ejemplos de funcionalidades

### 1. Crear usuario como administrador
```bash
> 1 (Crear nuevo usuario)
Seleccione tipo de usuario a crear:
1 - Administrador
2 - Vendedor
0 - Cancelar
> 2
Ingrese email: nuevo_vendedor@email.com
Ingrese contraseña: pass123
Ingrese nombre: VendedorNuevo
Ingrese teléfono: 123456789
✅ Usuario creado con éxito.
```

### 2. Crear pedido como cliente
```bash
> 1 (Hacer un pedido)
Productos disponibles:
ID: 1 | Nombre: Milanesa con papas | Precio: $22500.00 | Stock: 10
...
📦 Ingresá el ID del producto que querés agregar:
> 1
🔢 ¿Cuántas unidades querés agregar?
> 2
✅ Producto agregado.
> 0 (Finalizar pedido)
¿Deseás confirmar este pedido? (S para confirmar)
> S
Pedido confirmado exitosamente.
```

### 3. Modificar un producto como vendedor
```bash
> 2 (Modificar un producto existente)
Ingrese el ID del producto a modificar:
> 1
¿Qué desea modificar?
1 - Nombre
2 - Precio
...
> 2 (Modificar precio)
Ingrese el nuevo precio:
> 26000
✅ Precio actualizado.
```

### 4. Cambiar estado de un pedido como vendedor
```bash
> 3 (Modificar el estado de un pedido)
Ingrese el ID del Pedido que desea modificar:
> 1
Seleccione:
1 - Pasar al siguiente estado
2 - Cancelar pedido
0 - Cancelar acción
> 1
✅ Estado actualizado.
```

### 5. Ver reportes como administrador
```bash
> 4 (Ver reportes)
Seleccione reporte:
1 - Pedidos de un cliente
2 - Clientes con múltiples pedidos
3 - Total recaudado
> 3
💰 Total recaudado en pedidos entregados: $50000.00
```

---

## Autores por archivo

| Archivo | Autor/es |
|:--------|:---------|
| `func_administrador.kt` | Compartido entre todos |
| `func_cliente.kt` | Compartido entre todos |
| `func_vendedor.kt` | Compartido entre todos |
| `Menu.kt`, `Main.kt` | Compartido entre todos |
| `Personas.kt`, `Pedidos.kt`, `Productos.kt`, `excepciones.kt` | Compartido entre todos |

---

# 📚 Fin del README
