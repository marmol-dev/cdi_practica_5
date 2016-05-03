
public class Servidor implements Runnable {
	private int puerto;
	Servidor(int puerto){
		this.puerto = puerto;
	}
	
	private boolean estaTrabajoCompletado(){
		return false;
	}
	
	public synchronized Trabajo pedirTrabajo(){
		return null;
	}
	
	public synchronized void entregarTrabajo(Trabajo t){
		this.notify();
	}
	
	private void procesarTrabajos(){
		
	}
	
	public void run(){
		synchronized(this){
			while(estaTrabajoCompletado()){
				try {
					this.wait();
				} catch (InterruptedException e){}
			}
		}
		procesarTrabajos();
	}
}
