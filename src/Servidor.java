
import java.util.Queue;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

public class Servidor implements Runnable {
	private int puerto;
	private Queue<Trabajo> trabajosPorRealizar;
	private Map<UUID, Trabajo> trabajosRealizando;
	private TreeSet<Trabajo> trabajosRealizados;
	private LinkedList<Thread> clientes;
	private ServerSocket serverSocket;
	private boolean finalizado;
	int nClientesFinalizados = 0;
	int nClientesTotales = 0;
	int N;
	int divisiones;
	
	Servidor(int puerto, int divisiones, double xC, double yC, int size, int N, int maxIt) throws Exception {
		this.N = N;
		
		trabajosPorRealizar = Trabajo.generarCola(divisiones, xC, yC, size, N, maxIt);
		trabajosRealizando = new HashMap<UUID, Trabajo>();
		
		Comparator<Trabajo> comparador = new Comparator<Trabajo>(){
			public int compare(Trabajo t1, Trabajo t2){
				return t1.getPosicion() < t2.getPosicion() ? -1 :
					(t1.getPosicion() == t2.getPosicion() ? 0 :
						1);
			}
		};
		
		trabajosRealizados = new TreeSet<Trabajo>(comparador);
		
		clientes = new LinkedList<Thread>();
		
		this.puerto = puerto;
		this.divisiones = divisiones;
		
		this.serverSocket = new ServerSocket(this.puerto);
		this.finalizado = false;
	}
	
	public synchronized boolean estanTrabajosCompletados(){
		return trabajosPorRealizar.isEmpty() && trabajosRealizando.isEmpty();
	}
	
	public synchronized boolean hayTrabajosPorRealizar(){
		return !trabajosPorRealizar.isEmpty();
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
			trabajosRealizados.add(t);
			trabajosRealizando.remove(t.id);
			if (estanTrabajosCompletados()){
				this.notify();
			}
		} else {
			throw new Exception("El trabajo no se está realizando");
		}
	}
	
	private synchronized void  integrarTrabajosRealizados() throws Exception {
		PGM imagen = new PGM("imagen.pgm", this.N, this.N, 255);
		
		for (Trabajo t: trabajosRealizados){
			imagen.anhadir(t.getMatriz());
		}
		
		imagen.cerrar();
	}
	
	public synchronized void finalizar() throws UnknownHostException, IOException {
		nClientesFinalizados++;
		if (nClientesFinalizados == nClientesTotales){
			finalizado = true;
			this.serverSocket.close();
		}
	}
	
	private synchronized void nuevoCliente(){
		nClientesTotales++;
	}
	
	private boolean podemosFinalizar(){
		return finalizado; 
	}
	
	public void run() {
		int intentos = 0;
		Socket s;
		Thread nuevoThread;
			
		while(!podemosFinalizar() && intentos < 5){
			try {
				try {
					s = this.serverSocket.accept();
				} catch (Exception e){
					if (!this.finalizado) throw e;
					else break;
				}
				nuevoCliente();
				nuevoThread = new Thread(new ServidorThread(s, this));
				clientes.add(nuevoThread);
				nuevoThread.start();
				intentos=0;
			} catch (Exception e){
				intentos++;
				e.printStackTrace();
			}
		}
		
		if (intentos < 5) {
			try {
				integrarTrabajosRealizados();
			} catch (Exception e){
				System.out.println("Se ha producido un error al integrar los trabajos realizados:");
				e.printStackTrace();
			}
		}
			

		for (Thread t : clientes) {
			try {t.join();}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		
		System.out.println("Servidor thread finalizado");
	}
	
	public static void main(String[] args) throws Exception {
		Servidor serv = new Servidor(3000, Cliente.NUMERO_CLIENTES * 1, 512, 512, 1024, 1024, 512);
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
