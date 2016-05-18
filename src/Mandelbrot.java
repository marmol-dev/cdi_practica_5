import java.awt.Color;

/**
 * Implementa el método necesario para crear un conjunto de Mandelbrot
 * @author marmol
 *
 */
public class Mandelbrot {
    
	/**
	 * Procesa un conjunto de Mandelbrot en base a unos argumentos recibidos en un Trabajo
	 * @param trabajo Contiene los argumentos necesarios para procesar el conjunto de Mandelbrot. Provee de una estructura para guardar los cambios.
	 */
    public static void realizarTrabajo(Trabajo trabajo)  {
        double xc   = trabajo.xC;
        double yc   = trabajo.yC;
        int xI = trabajo.xI;
        int xF = trabajo.xF;
        int yI = trabajo.yI;
        int yF = trabajo.yF;
        double size = trabajo.size;

        int N   = trabajo.N;   // create N-by-N image
        int max = trabajo.maxIt;   // maximum number of iterations

        Color black = Color.BLACK;
        Color[] colors = new Color[max];
        for (int i = 0; i<max; i++) {
        	colors[i] = new Color(Color.HSBtoRGB(i/256f, 1, i/(i+8f)));
        }
        
        for (int row = yI; row < yF; row++) {
            for (int col = xI; col < xF; col++) {
            	
                double c_re = (col - xc)*4.0/N;
                double c_im = (row - yc)*4.0/N;
                
                double x = 0, y = 0;
                int iteration = 0;
                
                while (x*x+y*y <= 4 && iteration < max) {
                    double x_new = x*x - y*y + c_re;
                    y = 2*x*y + c_im;
                    x = x_new;
                    iteration++;
                }
                
                if (iteration < max) trabajo.set(col, row, colors[iteration]);
                else trabajo.set(col, row, black);
            }
        }
    }
}