import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * Implementa la conexión individual con el Servidor y realiza las acciones oportunas con los objetos Trabajo
 * @author marmol
 *
 */
public class Cliente implements Runnable {
	
	public static final int NUMERO_CLIENTES = 16;
	private int id;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	/**
	 * Constructor
	 * @param host El nombre del host servidor (o Ip)
	 * @param puerto El puerto del servidor
	 * @param id Un identificador único para el cliente
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	Cliente(String host, int puerto, int id) throws UnknownHostException, IOException {
		this.socket = new Socket(host, puerto);
		this.oos = new ObjectOutputStream(this.socket.getOutputStream());
		this.ois = new ObjectInputStream(this.socket.getInputStream());
		this.id = id;
	}
	
	/**
	 * Realiza un trabajo
	 * Procesa el conjunto de Mandelbrot
	 * @param actual El trabajo que se quiere realizar
	 */
	private void hacerTrabajo(Trabajo actual){
		Mandelbrot.realizarTrabajo(actual);
	}

	@Override
	/**
	 * Códigod el Thread
	 */
	public void run() {
		int intentos = 0;
		
		Accion accionRespuesta = null;
		boolean finalizado = false;
		
		do {
			try {
				
				/*if (id == 5){
					throw new RuntimeException("Intencionada");
				}*/
				
				this.oos.writeObject(new Accion(Accion.PEDIR_TRABAJO));
				accionRespuesta = (Accion) this.ois.readObject();
				
				
				if (accionRespuesta.getNombre().equals(Accion.ENVIAR_TRABAJO)){
					System.out.println("Hemos recibido trabajo");
					hacerTrabajo(accionRespuesta.getTrabajo());
					this.oos.writeObject(new Accion(Accion.ENVIAR_TRABAJO_TERMINADO, accionRespuesta.getTrabajo()));
				} else {
					finalizado = true;
				}
				
				intentos = 0;
			} catch (Exception e){
				intentos++;
				e.printStackTrace();
			}
		} while(intentos < 5 && !finalizado);
		
		try {
			this.oos.close();
			this.ois.close();
			this.socket.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		System.out.println("Cliente " + id + " finalizado");
	}
	
	/**
	 * Programa principal de los clientes
	 * Crea tantos clientes como se especifique y se conectan al servidor
	 * @param args Un vector con: hostname puerto [nClientes]
	 */
	public static void main(String[] args){
		
		try {
			if (args.length < 2){
				throw new RuntimeException("Argumentos: hostname puerto [nClientes]");
			}
			
			String hostname = args[0];
			int port = Integer.parseInt(args[1]);
			int nClientes = -1;
			
			if (args.length > 2){
				try {
					nClientes = Integer.parseInt(args[2]);
				} catch (Exception e){}
			}
			
			if (nClientes < 1){
				nClientes = NUMERO_CLIENTES;
			}
			
			LinkedList<Thread> threads = new LinkedList<Thread>();
			
			try {
				for(int i = 0; i < nClientes; i++){
					threads.push(new Thread(new Cliente(hostname, port, i)));
					threads.getFirst().start();
				}
				
				for(Thread t: threads){
					t.join();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		System.out.println("Programa cliente finalizado");
	}

}
