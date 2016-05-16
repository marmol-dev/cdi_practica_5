import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class Cliente implements Runnable {
	
	public static final int NUMERO_CLIENTES = 16;
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

	@Override
	public void run() {
		int intentos = 0;
		
		Accion accionRespuesta = null;
		
		do {
			try {
				this.oos.writeObject(new Accion(Accion.PEDIR_TRABAJO));
				accionRespuesta = (Accion) this.ois.readObject();
				
				if (accionRespuesta.getNombre().equals(Accion.ENVIAR_TRABAJO)){
					hacerTrabajo(accionRespuesta.getTrabajo());
					this.oos.writeObject(new Accion(Accion.ENVIAR_TRABAJO_TERMINADO, accionRespuesta.getTrabajo()));
				}
				
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
		
		LinkedList<Thread> threads = new LinkedList<Thread>();
		
		try {
			for(int i = 0; i < NUMERO_CLIENTES; i++){
				threads.push(new Thread(new Cliente("localhost", 3000, 1)));
				threads.getFirst().start();
			}
			
			for(Thread t: threads){
				t.join();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Programa finalizado");
	}

}
