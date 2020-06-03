package eg.edu.alexu.csd.oop.draw.cs32;
import eg.edu.alexu.csd.oop.draw.Shape;

import java.awt.Color;


public abstract  class MyShape implements Shape {

	protected java.awt.Point position;
	protected java.util.Map<String, Double> properties;
	protected java.awt.Color color = new Color(255,255,255);
	protected java.awt.Color fillColor = new Color(0,0,0);
	
	public void setPosition(java.awt.Point position) {
		this.position = position;
	}

	public java.awt.Point getPosition() {
		return position;
	}

	/* update shape specific properties (e.g., radius) */
	public void setProperties(java.util.Map<String, Double> properties) {
		this.properties = properties;
	}

	public java.util.Map<String, Double> getProperties() {
		return properties;
	}

	public void setColor(java.awt.Color color) {
		this.color = color;
	}

	public java.awt.Color getColor() {
		return this.color;
	}

	public void setFillColor(java.awt.Color color) {
		this.fillColor = color;
	}

	public java.awt.Color getFillColor() {
		return this.fillColor;
	}

	public abstract void draw(java.awt.Graphics canvas);

	public abstract Object clone() throws CloneNotSupportedException;


}
