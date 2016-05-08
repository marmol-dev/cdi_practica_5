import java.io.Serializable;

public class Accion implements Serializable {
	public static final String PEDIR_TRABAJO = "pedir_trabajo";
	public static final String ENVIAR_TRABAJO = "enviar_trabajo";
	public static final String ENVIAR_TRABAJO_TERMINADO = "enviar_trabajo_terminado";
	
	private String nombre;
	private Trabajo trabajo = null;
	Accion(String nombre){
		this.nombre = nombre;
	}
	
	Accion(String nombre, Trabajo trabajo){
		this.nombre = nombre;
		this.trabajo = trabajo;
	}
	
	public String getNombre(){
		return nombre;
	}
	
	public Trabajo getTrabajo(){
		return trabajo;
	}
}
