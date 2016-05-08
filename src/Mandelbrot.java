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

        for (int y = yI; y < yF; y++) {
            for (int x = xI; x < xF; x++) {
                double x0 = xc - size/2 + size*y/N;
                double y0 = yc - size/2 + size*x/N;
                Complex z0 = new Complex(x0, y0);
                int gray = max - mand(z0, max);
                trabajo.set(x, /*N-1-*/y, gray);
            }
        }
    }
}