import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente implements Runnable {
	
	public static final int NUMERO_CLIENTES = 64;
	private int id;
	private Socket serverSocket;
	
	Cliente(String host, int puerto, int id) throws UnknownHostException, IOException {
		this.serverSocket = new Socket(host, puerto);
		this.id = id;
	}
	
	private void hacerTrabajo(Trabajo actual){
		Mandelbrot.realizarTrabajo(actual);
	}
	
	private Trabajo pedirTrabajo() throws IOException, ClassNotFoundException{
		System.out.println("Cero");
				
		Accion.enviar(this.serverSocket, new Accion(Accion.PEDIR_TRABAJO));
		ObjectInputStream isr = new ObjectInputStream(this.serverSocket.getInputStream());
		Accion a = (Accion) isr.readObject();
		Trabajo t = a.getTrabajo();
		
		System.out.println("Segundo");
		
		return t;
	}

	@Override
	public void run() {
		try {
			int i = 0;
			Trabajo trabajo = this.pedirTrabajo();
			System.out.println("Hemos pedido el trabajo");
			while(trabajo != null){
				hacerTrabajo(trabajo);
				Accion.enviar(this.serverSocket, new Accion(Accion.ENVIAR_TRABAJO_TERMINADO, trabajo));
				trabajo = this.pedirTrabajo();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			this.serverSocket.close();
		} catch (Exception e){}
		
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
