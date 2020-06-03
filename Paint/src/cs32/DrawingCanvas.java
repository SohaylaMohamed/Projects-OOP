package eg.edu.alexu.csd.oop.draw.cs32;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;

public class DrawingCanvas implements DrawingEngine {

	ArrayList<Shape> shapes = new ArrayList<>();
	ChangesTracker tracker;
	/**
	 *  engine 
	 */
	public DrawingCanvas() {
		tracker = new ChangesTracker(shapes);
	}
	/**
	 * refresh the canvas
	 */
	public void refresh(Graphics canvas) {
		for (Shape shape : shapes) {
			shape.draw(canvas);
		}

	}
	/**
	 * add new shape
	 * @param shape new shape
	 */
	@Override
	public void addShape(Shape shape) {
		if (shape == null) {
			throw new RuntimeException();
		}
		shapes.add(shape);
		tracker.newChange(shapes);

	}
	/**
	 * remove existing shape
	 * @param shape shape to be removed
	 */
	@Override
	public void removeShape(Shape shape) {
		if (shape == null) {
			return;
		}
		shapes.remove(shape);
		tracker.newChange(shapes);
	}
	/**
	 * update existing shape with new one
	 * @param oldShape existing shape
	 * @param newShape the updated shape
	 */
	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		if (newShape == null || oldShape == null) {
			return;
		}

		int index = shapes.indexOf(oldShape);
		if (index == -1) {
			throw new RuntimeException("Element not found");
		}
		shapes.remove(index);
		shapes.add(index, newShape);
		tracker.newChange(shapes);

	}
	/**
	 * get drawn shape
	 * @return drawn shapes
	 */
	@Override
	public Shape[] getShapes() {
		if (shapes.size() == 0) {
			return new Shape[] {};
		}
		Shape[] shape = shapes.toArray(new Shape[shapes.size()]);
		return shape;
	}
	/**
	 * get supported classes
	 * @return list of suppported classes
	 */
	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		List<Class<? extends Shape>> classes = new ArrayList<>();
		classes.add(LineSegment.class);
		classes.add(Rectangle.class);
		classes.add(Square.class);
		classes.add(Ellipse.class);
		classes.add(Circle.class);
		classes.add(Triangle.class);

		return classes;
	}
	/**
	 * undo
	 */
	@Override
	public void undo() {
		shapes.clear();
		ArrayList<Shape> temp = tracker.undo();
		shapes = new ArrayList<>(temp);
	}
	/**
	 * redo
	 */
	@Override
	public void redo() {
		shapes.clear();
		ArrayList<Shape> temp = tracker.redo();
		shapes = new ArrayList<>(temp);
	}
	/**
	 * save to file
	 */
	@Override
	public void save(String path) {
		String[] parts = path.split("\\.");
		String ext = parts[parts.length - 1];

		if (ext.equalsIgnoreCase("json")) {
			Json s = new Json();
			s.writeIntoFile(shapes, path);
		} else if (ext.equalsIgnoreCase("xml")) {
			XML s = new XML();
			try {
				s.writeIntoFile(shapes, path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}else{
		throw new RuntimeException();
	}
	}
	/**
	 * load from file
	 */
	@Override
	public void load(String path) {
		String[] parts = path.split("\\.");
		String ext = parts[parts.length - 1];

		if (ext.equalsIgnoreCase("json")) {
			Json s = new Json();
			shapes.clear();
			tracker.clearAll();
			shapes = new ArrayList<>(s.readFile(path));
			tracker.newChange(shapes);

		} else if (ext.equalsIgnoreCase("xml")) {
			XML s = new XML();

			shapes.clear();
			tracker.clearAll();
			try {
				shapes = new ArrayList<>(s.readFile(path));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tracker.newChange(shapes);

		} else {
			throw new RuntimeException(Integer.toString(shapes.size()));
		}

	}
	/**
	 * check if the user can make undo
	 * @return true if yes
	 */
	public boolean checkCanUndo() {
		return tracker.checkUndo();
	}
	/**
	 * check if the user can make redo
	 * @return true if yes
	 */
	public boolean checkCanRedo() {
		return tracker.checkRedo();
	}
	/**
	 * remove shape while dragging without making any changes in history
	 * @param currentShape shape to be removed
	 */
	public void removeDraggedShape(Shape currentShape) {
		shapes.remove(currentShape);
		
	}
	 
	private java.awt.Shape findShape(Shape sh) {
		String s = sh.getClass().getSimpleName();
		switch(s){
			case "Rectangle" : return new Rectangle2D.Double(sh.getPosition().getX(), 
				sh.getPosition().getY(),
				sh.getProperties().get("width"),
				sh.getProperties().get("height"));
		case "Square" :	 return new Rectangle2D.Double(sh.getPosition().getX(), 
				sh.getPosition().getY(),
				sh.getProperties().get("width"),
				sh.getProperties().get("height"));
		case "Triangle" : {
			int[] x = {(int) sh.getPosition().getX(), sh.getProperties().get("X2").intValue(),
					sh.getProperties().get("X3").intValue()

			};
			int[] y = {(int) sh.getPosition().getY(), sh.getProperties().get("Y2").intValue(),
					sh.getProperties().get("Y3").intValue()

			};
			return new java.awt.Polygon(x,y,3);
		}
		case "Ellipse" : 	return new Ellipse2D.Double(sh.getPosition().getX(), 
				sh.getPosition().getY(),
				sh.getProperties().get("BR width"),
				sh.getProperties().get("BR height"));
		case "Circle" :	return new Ellipse2D.Double(sh.getPosition().getX(), 
				sh.getPosition().getY(),
				sh.getProperties().get("BR width"),
				sh.getProperties().get("BR height"));
		default : return null;
		}
	
	}
	public boolean contains(Shape sh, Point p){
		if(sh.getClass().getSimpleName().equals("LineSegment")){
			java.awt.geom.Point2D.Double c = new Point2D.Double(sh.getProperties().get("endPointX"), sh.getProperties().get("endPointY"));
			return distance(sh.getPosition(),  p) + distance(new Point((int)c.x, (int)c.y), p) == distance(sh.getPosition(), new Point((int)c.x, (int)c.y));
	}	else {
		java.awt.Shape shape = findShape(sh);
		return shape.contains(p);
	}
		
	}
	private int distance(Point position, Point point) {
				return (int) Point2D.distance(position.getX(), position.getY(), point.getX(), point.getY());
	          
	}
	public Rectangle2D getBounds(Shape sh) {
		String s = sh.getClass().getSimpleName();
		switch(s){
		case "LineSegment" : return new Line2D.Double(sh.getPosition().getX(),sh.getPosition().getY(),
				sh.getProperties().get("endPointX"),
				sh.getProperties().get("endPointY")).getBounds2D();
		case "Rectangle" : return new Rectangle2D.Double(sh.getPosition().getX(), 
				sh.getPosition().getY(),
				sh.getProperties().get("width"),
				sh.getProperties().get("height")).getBounds2D();
		case "Square" :	 return new Rectangle2D.Double(sh.getPosition().getX(), 
				sh.getPosition().getY(),
				sh.getProperties().get("width"),
				sh.getProperties().get("height")).getBounds2D();
		case "Triangle" : {
			int[] x = {(int) sh.getPosition().getX(), sh.getProperties().get("X2").intValue(),
					sh.getProperties().get("X3").intValue()

			};
			int[] y = {(int) sh.getPosition().getY(), sh.getProperties().get("Y2").intValue(),
					sh.getProperties().get("Y3").intValue()

			};
			return new java.awt.Polygon(x,y,3).getBounds2D();
		}
		case "Ellipse" : 	return new Ellipse2D.Double(sh.getPosition().getX(), 
				sh.getPosition().getY(),
				sh.getProperties().get("BR width"),
				sh.getProperties().get("BR height")).getBounds2D();
		case "Circle" :	return new Ellipse2D.Double(sh.getPosition().getX(), 
				sh.getPosition().getY(),
				sh.getProperties().get("BR width"),
				sh.getProperties().get("BR height")).getBounds2D();
		default : return null;
		}
	}
	public void clearAll() {
		tracker.clearAll();
		shapes = new ArrayList<Shape>();
		tracker.canRedo = false;
		tracker.canUndo = false;
	}

}
