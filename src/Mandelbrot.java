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
                
                if (iteration < max) trabajo.set(col, row, 255);
                else trabajo.set(col, row, 0);
            }
        }
    }
}