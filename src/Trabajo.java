import java.awt.Color;
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

/**
 * Contiene los argumentos necesarios para procesar el conjunto de Mandelbrot y ofrece una estructura para guardar los resultados
 * @author marmol
 *
 */
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
	private Color[][] matriz;
	int posicion;
	public UUID id;
	
	/**
	 * Constructor
	 * @param xC Coordenada x de centro
	 * @param yC Coordenada y de centro
	 * @param size Tamaño
	 * @param N Tamaño
	 * @param maxIt Máximo de iteraciones
	 * @param xI Coordenada de x del punto superior izquierda de la región
	 * @param yI Coordenada de y del punto superior izquierda de la región
	 * @param xF Coordenada de x del punto inferior derecha de la región
	 * @param yF Coordenada de y del punto inferior derecha de la región
	 * @param posicion Posición que ocupa la región en la imagen
	 */
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
		matriz = new Color[(int) (yF - yI)][(int) (xF - xI)];
		id = UUID.randomUUID();
		this.posicion = posicion;
	}
	
	/**
	 * Obtiene la posición de la zona en la imagen final
	 * @return
	 */
	public int getPosicion(){
		return this.posicion;
	}
	
	/**
	 * Establece un color de un píxel
	 * @param x
	 * @param y
	 * @param color
	 */
	public void set(double x, double y, Color color){
		int saveY = (int) (y - yI);
		int saveX = (int) (x - xI);
		this.matriz[saveY][saveX] = color;
	}
	
	/**
	 * Obtiene la coordenada X del punto inicial
	 * @return
	 */
	public int getXI(){
		return this.xI;
	}
	
	/**
	 * Obtiene la coordenada y del punto inicial
	 * @return
	 */
	public int getYI(){
		return this.yI;
	}
	
	/**
	 * Obtiene la matriz de colores
	 * @return
	 */
	public Color[][] getMatriz(){
		return this.matriz;
	}
	
	/**
	 * Genera una cola de trabajos en base a los argumentos
	 * @param divisiones Divisiones de la imagen
	 * @param xC Coordenada x del punto central
	 * @param yC Coordenada y del punto central
	 * @param size Tamaño de la imagen
	 * @param N Tamaño de la imagen
	 * @param maxIt Número máximo de iteraciones en el procesado del conjunto de Mandelbrot
	 * @return
	 * @throws Exception
	 */
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
