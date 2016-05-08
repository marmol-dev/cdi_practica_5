import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Trabajo implements Serializable {
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
		int saveY = (int) (y - yI);
		int saveX = (int) (x - xI);
		System.out.println("Guardamos en " + x + "," + y + " el inicial " + xI + "," + yI);
		this.matriz[saveY][saveX] = value;
	}
	
	public static void enviar(Socket s, Trabajo t) throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(t);
	}
	
	public static Trabajo pedir(Socket s) throws IOException, ClassNotFoundException {
		Accion pedir_trabajo = new Accion(Accion.PEDIR_TRABAJO);
		(new ObjectOutputStream(s.getOutputStream())).writeObject(pedir_trabajo);
		ObjectInputStream isr = new ObjectInputStream(s.getInputStream());
		return (Trabajo) isr.readObject();
	}
	
	public static Queue<Trabajo> generarCola(int divisiones, double xC, double yC, double size, int N, int maxIt) throws Exception {
		Queue<Trabajo> cola = new LinkedBlockingQueue<Trabajo>();
		
		if (N % divisiones != 0){
			throw new Exception("Número de divisiones no divide a N");
		}
		
		int tamDivision = N / divisiones;
				
		for(int i = 0; i < divisiones; i++){
			for(int j = 0; j < divisiones; j++){
				cola.add(new Trabajo(xC, yC, size, N, maxIt, i*tamDivision, j*tamDivision, (i+1)*tamDivision, (j+1)*tamDivision));
			}
		}
		
		
		return cola;
	}
	
	
}
