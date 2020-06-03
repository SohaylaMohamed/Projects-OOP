package eg.edu.alexu.csd.oop.draw.cs32;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class Ellipse extends MyShape{
	
	public Ellipse(){
		position = new Point(0,0);
		properties = new HashMap<String, Double>();
		properties.put("BR width", 100.0);
		properties.put("BR height", 60.0);

	}

	@Override
	public void draw(Graphics canvas) {
		((Graphics2D) canvas).setColor(getFillColor());
		((Graphics2D) canvas).fill(new Ellipse2D.Double(position.getX(), 
				position.getY(),
				properties.get("BR width"),
				properties.get("BR height")));
		((Graphics2D) canvas).setStroke(new BasicStroke(2));
		((Graphics2D) canvas).setColor(getColor());
		((Graphics2D) canvas).draw(new Ellipse2D.Double(position.getX(), 
				position.getY(),
				properties.get("BR width"),
				properties.get("BR height")));
		
		
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Ellipse temp = new Ellipse();
		temp.setColor(this.color);
		temp.setFillColor(this.fillColor);
		temp.setPosition(this.position);
		temp.setProperties(this.properties);
		
		return temp;
	}

	

}
