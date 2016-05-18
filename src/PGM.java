import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Implementa el tratamiento de archivos PGM a partir de matrices de Color
 * @author marmol
 *
 */
public class PGM {
	FileWriter fstream;
	BufferedWriter out;
	
	/**
	 * Constructor
	 * @param dir La ruta del archivo
	 * @param width El ancho del archivo
	 * @param height El alto del archivo
	 * @param maxValue El valor máximo de cada componente
	 * @throws IOException
	 */
	PGM(String dir, int width, int height, int maxValue) throws IOException{
		fstream = new FileWriter(dir);
	     //we create a new BufferedWriter
	    out = new BufferedWriter(fstream);
		out.write("P3\n"+width+" "+height+"\n"+maxValue+"\n");
	}
	
	/**
	 * Añade una matriz de Color a la imagen
	 * @param content La matriz los colores
	 * @throws IOException
	 */
	public void anhadir(Color[][] content) throws IOException{		 
		for(int i = 0 ; i<content.length;i++)
			for(int j = 0 ; j<content[0].length;j++){
				out.write(content[i][j].getRed() +" " + content[i][j].getGreen() + " " + content[i][j].getBlue() + "\t");
			}
	}
	
	/**
	 * Cierra el archivo cuando se finalizan las operaciones
	 * @throws IOException
	 */
	public void cerrar() throws IOException {
		out.close();
	}
}
