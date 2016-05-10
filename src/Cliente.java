import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente implements Runnable {
	
	public static final int NUMERO_CLIENTES = 64;
	private int id;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	Cliente(String host, int puerto, int id) throws UnknownHostException, IOException {
		this.socket = new Socket(host, puerto);
		this.oos = new ObjectOutputStream(this.socket.getOutputStream());
		this.ois = new ObjectInputStream(this.socket.getInputStream());
		this.id = id;
	}
	
	private void hacerTrabajo(Trabajo actual){
		Mandelbrot.realizarTrabajo(actual);
	}
	
	private Trabajo pedirTrabajo() throws IOException, ClassNotFoundException{
		System.out.println("Cero");
				
		this.oos.writeObject(new Accion(Accion.PEDIR_TRABAJO));
		Accion a = (Accion) this.ois.readObject();
		Trabajo t = a.getTrabajo();
		
		return t;
	}

	@Override
	public void run() {
		int intentos = 0;
		
		Accion accionRespuesta = null;
		
		try {
			this.oos.writeObject(new Accion(Accion.PEDIR_TRABAJO));
			accionRespuesta = (Accion) this.ois.readObject();
		} catch (Exception e){
			intentos++;
			e.printStackTrace();
		}
		
		do {
			try {
				if (accionRespuesta != null && accionRespuesta.getNombre().equals(Accion.ENVIAR_TRABAJO)){
					hacerTrabajo(accionRespuesta.getTrabajo());
					this.oos.writeObject(new Accion(Accion.ENVIAR_TRABAJO_TERMINADO, accionRespuesta.getTrabajo()));
				}
				
				this.oos.writeObject(new Accion(Accion.PEDIR_TRABAJO));
				accionRespuesta = (Accion) this.ois.readObject();
				
				intentos = 0;
			} catch (Exception e){
				intentos++;
				e.printStackTrace();
			}
		} while(intentos < 5 && !accionRespuesta.getNombre().equals(Accion.FINALIZAR_CLIENTE));
		
		try {
			this.oos.close();
			this.ois.close();
			this.socket.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		System.out.println("Cliente " + id + " finalizado");
	}
	
	public static void main(String[] args){
		
		try {
			Cliente c = new Cliente("localhost", 3000, 1);
			Thread t = new Thread(c);
			t.start();
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Programa finalizado");
	}

}
