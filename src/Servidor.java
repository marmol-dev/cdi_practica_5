
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.PriorityQueue;
import java.util.Map;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Servidor implements Runnable {
	private int puerto;
	private Queue<Trabajo> trabajosPorRealizar;
	private Queue<Trabajo> trabajosRealizando;
	private Queue<Trabajo> trabajosRealizados;
	
	Servidor(int puerto, int divisiones, double xC, double yC, int size, int N, int maxIt) throws Exception {
		trabajosPorRealizar = Trabajo.generarCola(divisiones, xC, yC, size, N, maxIt);
		trabajosRealizando = new LinkedBlockingQueue<Trabajo>();
		trabajosRealizados = new LinkedBlockingQueue<Trabajo>();
		
		this.puerto = puerto;
	}
	
	private boolean estanTrabajosCompletados(){
		return trabajosPorRealizar.isEmpty() && trabajosRealizando.isEmpty();
	}
	
	public synchronized Trabajo pedirTrabajo(){
		if (trabajosPorRealizar.isEmpty()){
			return null;
		} else {
			Trabajo actual = trabajosPorRealizar.poll();
			trabajosRealizando.add(actual);
			return actual;
		}
	}
	
	public synchronized void entregarTrabajo(Trabajo t) throws Exception {
		if (trabajosRealizando.contains(t)){
			trabajosRealizados.add(t);
			trabajosRealizando.remove(t);
			if (estanTrabajosCompletados()){
				this.notify();
			}
		} else {
			throw new Exception("El trabajo no se está realizando");
		}
	}
	
	private void integrarTrabajosRealizados(){
		
	}
	
	public void procesarConexion(Socket s) {
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					ObjectInputStream isr = new ObjectInputStream(s.getInputStream());
					Accion accion = (Accion) isr.readObject();
					System.out.println("Recibimos:" + accion.getNombre());
					switch(accion.getNombre()){
						case Accion.PEDIR_TRABAJO:
							Trabajo.enviar(s, pedirTrabajo());
							break;
						case Accion.ENVIAR_TRABAJO_TERMINADO:
							entregarTrabajo(accion.getTrabajo());
							break;
						default:
							throw new Exception("Accion inválida:" + accion.getNombre());
					}
					s.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				
		});
		
		t.start();
		
	}
	
	public void run() {
		int i = 0;
		try {
			ServerSocket ss = new ServerSocket(this.puerto);
			
			while(i++ < 10){
				Socket s = ss.accept();
				System.out.println("Vamos");
				procesarConexion(s);
			}
			
			ss.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		Servidor serv = new Servidor(3000, Cliente.NUMERO_CLIENTES * 1, 0, 0, 2048, 2048, 512);
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
