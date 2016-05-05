
public class Cliente implements Runnable {
	
	private Servidor servidor;
	private int id;
	Cliente(Servidor servidor, int id){
		this.servidor = servidor;
		this.id = id;
	}
	
	private void hacerTrabajo(Trabajo actual){
		Mandelbrot.realizarTrabajo(actual);
		try {
			servidor.entregarTrabajo(actual);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		Trabajo actual = servidor.pedirTrabajo(this);
		while(actual != null){
			hacerTrabajo(actual);
			actual = servidor.pedirTrabajo(this);
		}
		System.out.println("Hemos acabado el cliente " + id);
	}

}
