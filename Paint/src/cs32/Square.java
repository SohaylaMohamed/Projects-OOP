package eg.edu.alexu.csd.oop.draw.cs32;

import java.awt.Point;
import java.util.HashMap;

public class Square extends Rectangle{
	private double side = 50.0;
	
	public Square(){
		position = new Point(0,0);
		properties = new HashMap<String, Double>();
		properties.put("width", side);
		properties.put("height", side);
	}

}
