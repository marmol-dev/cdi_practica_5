import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Clase que se utiliza para enviar los objetos entre servidor-cliente
 * 
 * @author marmol
 *
 */
public class Accion implements Serializable {
	public static final String PEDIR_TRABAJO = "pedir_trabajo";
	public static final String ENVIAR_TRABAJO = "enviar_trabajo";
	public static final String ENVIAR_TRABAJO_TERMINADO = "enviar_trabajo_terminado";
	public static final String FINALIZAR_CLIENTE = "finalizar_cliente";
	
	private String nombre;
	private Trabajo trabajo = null;
	
	/**
	 * Constructor
	 * @param nombre Nombre de la acción. El nombre es uno de los predefinidos de la clase.
	 */
	Accion(String nombre){
		this.nombre = nombre;
	}
	
	/**
	 * Constructor con nombre y trabajo
	 * @param nombre Nombre de la acción. El nombre es uno de los predefinidos de la clase.
	 * @param trabajo Trabajo que se quiere enviar. 
	 */
	Accion(String nombre, Trabajo trabajo){
		this.nombre = nombre;
		this.trabajo = trabajo;
	}
	
	/**
	 * Devuelve el nombre de la Acción
	 * @return
	 */
	public String getNombre(){
		return nombre;
	}
	
	
	/**
	 * Devuelve el nombre del trabajo
	 * @return
	 */
	public Trabajo getTrabajo(){
		return trabajo;
	}
}
