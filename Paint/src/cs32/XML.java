package eg.edu.alexu.csd.oop.draw.cs32;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import eg.edu.alexu.csd.oop.draw.Shape;

public class XML {
	 public void writeIntoFile(ArrayList<Shape> shapes, String path)
	            throws Exception {
	        XMLEncoder en = new XMLEncoder(new BufferedOutputStream(
	                new FileOutputStream(path)));
	        en.writeObject(shapes);
	        en.close();
	    }
	 public ArrayList<Shape> readFile(String path) throws Exception {
	        XMLDecoder de = new XMLDecoder(new BufferedInputStream(
	                new FileInputStream(path)), null, null, this.getClass()
	                .getClassLoader());

	        @SuppressWarnings("unchecked")
			ArrayList<Shape> shapes = (ArrayList<Shape>) de.readObject();
	        de.close();
	        return shapes;
	    }
	
	 
	 
}
