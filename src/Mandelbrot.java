import java.awt.Color;

public class Mandelbrot {

    // return number of iterations to check if c = a + ib is in Mandelbrot set
    public static int mand(Complex z0, int max) {
        Complex z = z0;
        for (int t = 0; t < max; t++) {
            if (z.abs() > 2.0) return t;
            z = z.times(z).plus(z0);
        }
        return max;
    }
    
    public static int[] hsvToRgb(float hue, float saturation, float value) {
    	
        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
          case 0: return rgbToArray(value, t, p);
          case 1: return rgbToArray(q, value, p);
          case 2: return rgbToArray(p, value, t);
          case 3: return rgbToArray(p, q, value);
          case 4: return rgbToArray(t, p, value);
          case 5: return rgbToArray(value, p, q);
          default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
    }
    
    private static int[] rgbToArray(float a, float b, float c){
    	int[] toret = {(int) a,(int) b, (int) c};
    	return toret;
    }

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