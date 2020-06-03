package eg.edu.alexu.csd.oop.draw.cs32;

import java.awt.Point;
import java.util.HashMap;

public class Circle extends Ellipse {
	
	private double radius = 50.0;
	
	public Circle(){
		position = new Point();
		properties = new HashMap<String, Double>();
		properties.put("BR width", radius);
		properties.put("BR height", radius);
	}

}
