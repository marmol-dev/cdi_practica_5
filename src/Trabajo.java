import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class Trabajo {
	public double xC;
	public double yC;
	public double size;
	public int N;
	public int maxIt;
	public int xI;
	public int yI;
	public int xF;
	public int yF;
	private int[][] matriz;
	Trabajo(double xC, double yC, double size, int N, int maxIt, int xI, int yI, int xF, int yF){
		this.xC = xC;
		this.yC = yC;
		this.size = size;
		this.N = N;
		this.maxIt = maxIt;
		this.xI = xI;
		this.yI = yI;
		this.xF = xF;
		this.yF = yF;
		matriz = new int[(int) (yF - yI)][(int) (xF - xI)];
	}
	
	public void set(double x, double y, int value){
		this.matriz[(int) y][(int) x] = value;
	}
	
	public static void enviar(Socket s, Trabajo t) throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(t);
	}
	
	public static Trabajo pedir(Socket s) throws IOException, ClassNotFoundException {
		(new PrintStream(s.getOutputStream())).println(Accion.PEDIR_TRABAJO);
		ObjectInputStream isr = new ObjectInputStream(s.getInputStream());
		return (Trabajo) isr.readObject();
	}
	
	
}
