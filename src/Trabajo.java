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
import java.util.UUID;
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
	int posicion;
	public UUID id;
	
	Trabajo(double xC, double yC, double size, int N, int maxIt, int xI, int yI, int xF, int yF, int posicion){
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
		id = UUID.randomUUID();
		this.posicion = posicion;
	}
	
	public int getPosicion(){
		return this.posicion;
	}
	
	public void set(double x, double y, int value){
		int saveY = (int) (y - yI);
		int saveX = (int) (x - xI);
		this.matriz[saveY][saveX] = value;
	}
	
	public int getXI(){
		return this.xI;
	}
	
	public int getYI(){
		return this.yI;
	}
	
	public int[][] getMatriz(){
		return this.matriz;
	}
	
	
	public static Queue<Trabajo> generarCola(int divisiones, double xC, double yC, double size, int N, int maxIt) throws Exception {
		Queue<Trabajo> cola = new LinkedBlockingQueue<Trabajo>();
		
		if (N % divisiones != 0){
			throw new Exception("Número de divisiones no divide a N");
		}
		
		int tamDivision = N / divisiones;
		int cnt = 0;
				
		for(int i = 0; i < N; i+= tamDivision){
			cola.add(new Trabajo(xC, yC, size, N, maxIt, 0, i, N, i+tamDivision, cnt));
			cnt++;
		}
		
		
		return cola;
	}
	
	
}
