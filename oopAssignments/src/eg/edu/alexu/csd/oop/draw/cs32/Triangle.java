package eg.edu.alexu.csd.oop.draw.cs32;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class Triangle extends MyShape {
	private Point second;
	private Point third;
	private double l1, l2, l3;
	
	
	public Triangle(){
		position = new Point(25, 25);
		second = new Point(100, 50);
		third = new Point(15, 15);
		
		
		properties = new HashMap<String, Double>();
		properties.put("X2", second.getX());
		
		properties.put("Y2", second.getY());
		properties.put("X3", third.getX());

		properties.put("Y3", third.getY());
	}
	@Override
	public void draw(Graphics canvas) {
		int[] x = {(int) position.getX(), properties.get("X2").intValue(),
				properties.get("X3").intValue()

		};
		int[] y = {(int) position.getY(), properties.get("Y2").intValue(),
				properties.get("Y3").intValue()

		};
		((Graphics2D) canvas).setColor(getFillColor());
		((Graphics2D) canvas).fillPolygon(x, y, 3);
		((Graphics2D) canvas).setStroke(new BasicStroke(2));
		
		((Graphics2D) canvas).setColor(getColor());
		((Graphics2D) canvas).draw(new java.awt.Polygon(x,y,3));
	}
	
	

	@Override
	public Object clone() throws CloneNotSupportedException {
		Triangle temp = new Triangle();
		temp.setColor(this.color);
		temp.setFillColor(this.fillColor);
		temp.setPosition(this.position);
		temp.setProperties(this.properties);
		
		return temp;
	}


}
