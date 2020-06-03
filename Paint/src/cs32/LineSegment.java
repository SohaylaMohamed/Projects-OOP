package eg.edu.alexu.csd.oop.draw.cs32;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;

public class LineSegment extends MyShape {
	protected Point endPoint;
	
	public LineSegment(){
		endPoint = new Point(0,0);
		position = new Point(0,0);
		properties = new HashMap<String, Double>();
		properties.put("endPointX", endPoint.getX());
		properties.put( "endPointY", endPoint.getY());
	}

	@Override
	public void draw(Graphics canvas) {
	
		((Graphics2D) canvas).setColor(getFillColor());
		((Graphics2D) canvas).fill(new Line2D.Double(this.position.getX(),this.position.getY(),
				this.properties.get("endPointX"),
				this.properties.get("endPointY")));
		((Graphics2D) canvas).setStroke(new BasicStroke(2));
		((Graphics2D) canvas).setColor(getColor());
		((Graphics2D) canvas).draw(new Line2D.Double(this.position.getX(),this.position.getY(),
						this.properties.get("endPointX"),
						this.properties.get("endPointY")));
		
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		LineSegment line = new LineSegment();
		line.setColor(this.color);
		line.setFillColor(this.fillColor);
		line.setPosition(this.position);
		line.setProperties(this.properties);
		return line;
	}

/*	@Override
	public boolean contains(Point point) {
		java.awt.geom.Point2D.Double p = new Point2D.Double(this.getProperties().get("endPointX"), this.getProperties().get("endPointY"));
		return distance(position,  point) + distance(new Point((int)p.x, (int)p.y), point) == distance(position, new Point((int)p.x, (int)p.y));
	}

	private int distance(Point position, Point point) {
		// TODO Auto-generated method stub
		
				return (int) Point2D.distance(position.getX(), position.getY(), point.getX(), point.getY());
	          
	}
*/
	
}
