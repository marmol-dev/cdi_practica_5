
import java.util.Queue;
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
	private Map<Trabajo, Cliente> trabajosRealizando;
	private Queue<Trabajo> trabajosRealizados;
	
	Servidor(int puerto){
		this.puerto = puerto;
	}
	
	private boolean estanTrabajosCompletados(){
		return trabajosPorRealizar.isEmpty() && trabajosRealizando.isEmpty();
	}
	
	public synchronized Trabajo pedirTrabajo(Cliente c){
		Trabajo actual = trabajosPorRealizar.poll();
		trabajosRealizando.put(actual, c);
		return actual;
	}
	
	public synchronized void entregarTrabajo(Trabajo t) throws Exception {
		if (trabajosRealizando.containsKey(t)){
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
					switch(accion.getNombre()){
						case Accion.PEDIR_TRABAJO:
							Trabajo.enviar(s, pedirTrabajo(null));
							break;
						case Accion.ENVIAR_TRABAJO_TERMINADO:
							entregarTrabajo(accion.getTrabajo());
							break;
						default:
							throw new Exception("Accion inválida:" + accion.getNombre());
					}
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
				procesarConexion(s);
			}
			
			ss.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Servidor serv = new Servidor(3000);
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
