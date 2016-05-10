import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServidorThread implements Runnable {
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Servidor servidor;
	
	ServidorThread(Socket s, Servidor serv) throws IOException{
		this.socket = s;
		this.oos = new ObjectOutputStream(s.getOutputStream());
		this.ois = new ObjectInputStream(s.getInputStream());
		this.servidor = serv;
	}
	
	public void run(){
		int intentos = 0;
		
		while(!servidor.estanTrabajosCompletados() && intentos < 5){
			try {
				Accion accion = (Accion) this.ois.readObject();
				System.out.println("Recibimos:" + accion.getNombre());
				
				switch(accion.getNombre()){
					case Accion.PEDIR_TRABAJO:
						this.oos.writeObject(new Accion(Accion.ENVIAR_TRABAJO, this.servidor.sacarTrabajoSinRealizar()));
						break;
					case Accion.ENVIAR_TRABAJO_TERMINADO:
						this.servidor.anhadirTrabajoRealizado(accion.getTrabajo());
						break;
					default:
						throw new Exception("Accion inválida:" + accion.getNombre());
				}
				
				intentos = 0;
			} catch (Exception e){
				intentos++;
				e.printStackTrace();
			}
			
		}
		
		if (intentos >= 5){
			System.out.println("Límite de intentos de conexión con el cliente alcanzados. Finalizando thread.");
		}
		
		try {
			this.ois.readObject();
			this.oos.writeObject(new Accion(Accion.FINALIZAR_CLIENTE));
		} catch (Exception e){
			e.printStackTrace();
		}
		
		try {
			this.ois.close();
			this.oos.close();
			this.socket.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		try {
			this.servidor.finalizar();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
