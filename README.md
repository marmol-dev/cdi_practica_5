# Práctica 5

> Martín Molina Álvarez

> Víctor Malvárez Filgueira


## Modo de empleo
1. Compilar los `.java`.
2. Ir al directorio donde están los `.class`.
3. Abrir el Servidor con los argumentos necesarios:
	- puerto 
	- divisiones 
	- xCentro 
	- yCentro 
	- tamaño 
	- iteraciones
	- archivo
4. Ejemplo: `java Servidor 3000 16 512 512 1024 512 imagen.pgm`
5. Abrir los clientes: `java Cliente ...` Los argumentos son:
	- hostname: la dirección del servidor
	- puerto
	- nClientes: Opcional.
6. Ejemplo: `java Cliente localhost 3000 16`

## Arquitectura
### Servidor
Clase principal del servidor que se encarga de abrir las conexiones con los clientes y de gestionar el proceso de organización y control de las colas.

### ServidorThread
Clase cuyas instancias son creadas cada vez que se conecta un nueco cliente. Gestiona y controla una conexión individual con un cliente.

### Cliente
Clase cuyas instancias son creadas una vez que se ha abierto el servidor. Hacen las llamadas al algoritmo de Mandelbrot.

### Trabajo
Clase que contiene los atributos necesarios para procesar una región de la imagen del Mandelbrot.

### Acción
Clase vehicular para comunicarse entre Cliente y Servidor.

### Mandelbrot
Clase cuyo método implementa el algoritmo de Mandelbrot.

### PGM
Clase que se encarga de la creación de imágenes con el mismo formato que el nombre de la clase y escribe matrices de colores en formato RGB.


