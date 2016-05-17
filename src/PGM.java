import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PGM {
	FileWriter fstream;
	BufferedWriter out;
	PGM(String dir, int width, int height, int maxValue) throws IOException{
		fstream = new FileWriter(dir);
	     //we create a new BufferedWriter
	    out = new BufferedWriter(fstream);
		out.write("P3\n"+width+" "+height+"\n"+maxValue+"\n");
	}
	
	public void anhadir(Color[][] content) throws IOException{		 
		for(int i = 0 ; i<content.length;i++)
			for(int j = 0 ; j<content[0].length;j++){
				out.write(content[i][j].getRed() +" " + content[i][j].getGreen() + " " + content[i][j].getBlue() + "\t");
			}
	}
	
	public void cerrar() throws IOException {
		out.close();
	}
}
