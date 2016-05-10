import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServidorThread implements Runnable {
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	ServidorThread(Socket s) throws IOException{
		this.socket = s;
		this.oos = new ObjectOutputStream(s.getOutputStream());
		this.ois = new ObjectInputStream(s.getInputStream());
	}
	
	public void run(){
		
	}
}
