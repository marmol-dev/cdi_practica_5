
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Map;
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
	
	public void run(){
		synchronized(this){
			while(estanTrabajosCompletados()){
				try {
					this.wait();
				} catch (InterruptedException e){}
			}
		}
		integrarTrabajosRealizados();
	}
}
