import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente implements Runnable {
	
	private int id;
	private Socket serverSocket;
	
	Cliente(String host, int puerto, int id) throws UnknownHostException, IOException {
		this.serverSocket = new Socket(host, puerto);
		this.id = id;
	}
	
	private void hacerTrabajo(Trabajo actual){
		Mandelbrot.realizarTrabajo(actual);
	}

	@Override
	public void run() {
		try {
			Trabajo trabajo = Trabajo.pedir(this.serverSocket);
			while(trabajo != null){
				hacerTrabajo(trabajo);
				Trabajo.enviar(this.serverSocket, trabajo);
				trabajo = Trabajo.pedir(this.serverSocket);
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
