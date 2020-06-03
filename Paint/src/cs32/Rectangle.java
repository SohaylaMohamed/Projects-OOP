package eg.edu.alexu.csd.oop.draw.cs32;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

public class Rectangle extends MyShape {
	protected double width;
	protected double height;

	
	public Rectangle(){
		width = 100.0;
		height = 20.0;
		position = new Point(25, 25);
		properties = new HashMap<String, Double>();
		properties.put("width", width);
		properties.put("height", height);
	}
	
	@Override
	public void draw(Graphics canvas) {
		( canvas).setColor(super.getFillColor());
		((Graphics2D) canvas).fill(new Rectangle2D.Double(position.getX(), 
				position.getY(),
				properties.get("width"),
				properties.get("height")));
		((Graphics2D) canvas).setStroke(new BasicStroke(2));
		( canvas).setColor(super.getColor());
		((Graphics2D) canvas).draw(new Rectangle2D.Double(position.getX(),
				position.getY(),
				properties.get("width"),
				properties.get("height")));
		
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Rectangle temp = new Rectangle();
		temp.setColor(super.getColor());
		temp.setFillColor(super.getFillColor());
		temp.setPosition(super.getPosition());
		
        temp.setProperties(super.getProperties());		
		return temp;
	}

}
