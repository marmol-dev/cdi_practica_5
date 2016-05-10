
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Servidor implements Runnable {
	private int puerto;
	private Queue<Trabajo> trabajosPorRealizar;
	private Map<UUID, Trabajo> trabajosRealizando;
	private Map<UUID, Trabajo> trabajosRealizados;
	private LinkedList<Thread> clientes;
	private ServerSocket serverSocket;
	private boolean finalizado;
	
	Servidor(int puerto, int divisiones, double xC, double yC, int size, int N, int maxIt) throws Exception {
		trabajosPorRealizar = Trabajo.generarCola(divisiones, xC, yC, size, N, maxIt);
		trabajosRealizando = new HashMap<UUID, Trabajo>();
		trabajosRealizados = new HashMap<UUID, Trabajo>();
		clientes = new LinkedList<Thread>();
		
		this.puerto = puerto;
		
		this.serverSocket = new ServerSocket(this.puerto);
		this.finalizado = false;
	}
	
	public synchronized boolean estanTrabajosCompletados(){
		return trabajosPorRealizar.isEmpty() && trabajosRealizando.isEmpty();
	}
	
	public synchronized Trabajo sacarTrabajoSinRealizar(){
		if (trabajosPorRealizar.isEmpty()){
			return null;
		} else {
			Trabajo actual = trabajosPorRealizar.poll();
			trabajosRealizando.put(actual.id, actual);
			return actual;
		}
	}
	
	public synchronized void anhadirTrabajoRealizado(Trabajo t) throws Exception {
		if (trabajosRealizando.containsKey(t.id)){
			trabajosRealizados.put(t.id, t);
			trabajosRealizando.remove(t.id);
			if (estanTrabajosCompletados()){
				this.notify();
			}
		} else {
			throw new Exception("El trabajo no se está realizando");
		}
	}
	
	private void integrarTrabajosRealizados(){
		
	}
	
	public void finalizar() throws UnknownHostException, IOException{
		if (!finalizado){
			finalizado = true;
			this.serverSocket.close();
		}
	}
	
	public void run() {
		int intentos = 0;
		Socket s;
		Thread nuevoThread;
			
		while(!estanTrabajosCompletados() && intentos < 5){
			try {
				try {
					s = this.serverSocket.accept();
				} catch (Exception e){
					if (!this.finalizado) throw e;
					else break;
				}
				nuevoThread = new Thread(new ServidorThread(s, this));
				clientes.add(nuevoThread);
				nuevoThread.start();
				intentos=0;
			} catch (Exception e){
				intentos++;
				e.printStackTrace();
			}
		}
		
		integrarTrabajosRealizados();
			
		try {
			for (Thread t : clientes) {
				t.join();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		Servidor serv = new Servidor(3000, Cliente.NUMERO_CLIENTES * 1, 0, 0, 512, 512, 512);
		Thread serverThread = new Thread(serv);
		serverThread.start();
		
		try {
			serverThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Servidor finalizado");
	}
}
