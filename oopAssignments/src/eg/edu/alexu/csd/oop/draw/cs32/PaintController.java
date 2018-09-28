package eg.edu.alexu.csd.oop.draw.cs32;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import eg.edu.alexu.csd.oop.draw.Shape;

public class PaintController {
	private static PaintController instance = null;
	private DrawingCanvas engine = new DrawingCanvas();
	private HashMap<String, Integer> actions = new HashMap<>();
	private MakeShape make = MakeShape.getInstance();

	public void setActions(HashMap<String, Integer> a) {
		this.actions = a;
	}

	/**
	 * slop angle in radians
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public double angle(Point p1, Point p2) {
		double s1 = Math.abs(p2.getY() - p1.getY()) / Math.abs(p2.getX() - p1.getX());
		double a1 = Math.atan(s1);
		return a1;

	}

	/**
	 * distance between two points
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */

	public double sideL(Point p1, Point p2) {
		return Math.hypot(p1.getX() - p2.getX(), p1.getY() - p2.getY());
	}

	/**
	 * point on line knowing the distance and the slope and another point on
	 * line
	 * 
	 * @param p1
	 * @param a1
	 * @param l1
	 * @return
	 */
	public Point getPoint(Point p1, double a1, double l1) {
		Point pN = new Point();

		pN.setLocation(p1.getX() + (l1 * Math.cos(a1)), p1.getY() + (l1 * Math.sin(a1)));
		return pN;
	}

	/**
	 * setting new shape while drawing
	 * 
	 * @param n
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public Shape setNewShape(int n, int x1, int y1, int x2, int y2) {
		switch (n) {
		case 5:
			return setCircle(x1, y1, x2, y2);
		case 4:
			return setLine(x1, y1, x2, y2);
		case 6:
			return setEllipse(x1, y1, x2, y2);
		case 8:
			return setSquare(x1, y1, x2, y2);
		case 9:
			return setTriangle(x1, y1, x2, y2);
		case 7:
			return setRectangle(x1, y1, x2, y2);
		default:
			return null;

		}
	}

	/**
	 * get shape action ID
	 * 
	 * @param string
	 * @return
	 */
		public int shapeAction(String string) {
		return actions.get(string);
	}

	/**
	 * Setting circle
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private Shape setCircle(int x1, int y1, int x2, int y2) {
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);

		Double radius = (double) Math.abs(x1 - x2);
		Shape cir = new Circle();
		cir.setPosition(new Point(x, y));
		HashMap<String, Double> prop = new HashMap<>();
		prop.put("BR width", radius);
		prop.put("BR height", radius);
		cir.setProperties(prop);
		return cir;
	}

	/**
	 * setting line
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private Shape setLine(int x1, int y1, int x2, int y2) {
		Shape line = new LineSegment();
		line.setPosition(new Point(x1, y1));
		HashMap<String, Double> prop = new HashMap<>();
		prop.put("endPointX", (double) x2);
		prop.put("endPointY", (double) y2);
		line.setProperties(prop);
		return line;
	}

	/**
	 * setting ellipse
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private Shape setEllipse(int x1, int y1, int x2, int y2) {
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);

		Double width = (double) Math.abs(x1 - x2);
		Double height = (double) Math.abs(y1 - y2);

		Shape cir = new Ellipse();
		cir.setPosition(new Point(x, y));
		HashMap<String, Double> prop = new HashMap<>();
		prop.put("BR width", width);
		prop.put("BR height", height);
		cir.setProperties(prop);
		return cir;
	}

	/**
	 * setting square
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private Shape setSquare(int x1, int y1, int x2, int y2) {
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);

		Double width = (double) Math.abs(x1 - x2);
		Shape sq = new Square();
		sq.setPosition(new Point(x, y));
		HashMap<String, Double> prop = new HashMap<>();
		prop.put("width", width);
		prop.put("height", width);
		sq.setProperties(prop);
		return sq;

	}

	/**
	 * setting triangle
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private Shape setTriangle(int x1, int y1, int x2, int y2) {
		Shape Tri = new Triangle();
		Tri.setPosition(new Point(x1, y1));
		HashMap<String, Double> prop = new HashMap<>();
		prop.put("X2", (double) x2);

		prop.put("Y2", (double) y2);
		prop.put("X3", (double) x1);

		prop.put("Y3", (double) y2);
		Tri.setProperties(prop);

		return Tri;

	}

	/**
	 * setting triangle
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private Shape setRectangle(int x1, int y1, int x2, int y2) {
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);

		Double width = (double) Math.abs(x1 - x2);
		Double height = (double) Math.abs(y1 - y2);
		Shape rec = new Rectangle();
		rec.setPosition(new Point(x, y));
		HashMap<String, Double> prop = new HashMap<>();
		prop.put("width", width);
		prop.put("height", height);
		rec.setProperties(prop);
		return rec;
	}

	/**
	 * refresh canvas
	 * 
	 * @param g
	 */
	public void redrawAll(Graphics g) {
		if (engine.getShapes().length == 0) {
			return;
		}
		engine.refresh(g);
	}

	/**
	 * adding new shape
	 * 
	 * @param n
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param fill
	 * @param stroke
	 * @return
	 */
	public Shape addNewShape(int n, int x1, int y1, int x2, int y2, Color fill, Color stroke) {
		Shape shape = null;
		switch (n) {
		case 5:
			shape = setCircle(x1, y1, x2, y2);
			break;
		case 4:
			shape = setLine(x1, y1, x2, y2);
			break;
		case 6:
			shape = setEllipse(x1, y1, x2, y2);
			break;
		case 8:
			shape = setSquare(x1, y1, x2, y2);
			break;
		case 9:
			shape = setTriangle(x1, y1, x2, y2);
			break;
		case 7:
			shape = setRectangle(x1, y1, x2, y2);
			break;
		default:
			shape = null;
		}
		if (shape != null) {
			shape.setColor(stroke);
			shape.setFillColor(fill);
			addShape(shape);
			return shape;
		}
		return null;
	}

	/**
	 * get drawn shapes
	 * 
	 * @return
	 */
	public Shape[] getShapes() {

		return engine.getShapes();
	}

	/**
	 * 
	 * @return
	 */
	public static PaintController getInstance() {
		if (instance == null) {
			instance = new PaintController();
		}
		return instance;
	}

	/**
	 * check if can undo
	 * 
	 * @return
	 */
	public boolean canUndo() {
		return engine.checkCanUndo();
	}

	/**
	 * check if can redo
	 * 
	 * @return
	 */
	public boolean canRedo() {
		return engine.checkCanRedo();
	}

	/**
	 * undo
	 */
	public void undo() {
		engine.undo();
	}

	/**
	 * redo
	 */
	public void redo() {
		engine.redo();
	}

	/**
	 * get selected shape
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Shape shapeSelected(int x, int y) {
		Shape sel = null;
		for (Shape s : this.getShapes()) {

			if (engine.contains(s, new Point(x, y))) {
				sel = s;
				break;
			}
		}
		return sel;
	}

	/**
	 * update shape
	 * 
	 * @param currentShape
	 * @param newShape
	 */
	public void updateShape(Shape currentShape, Shape newShape) {
		engine.updateShape(currentShape, newShape);

	}

	/**
	 * remove shape
	 * 
	 * @param currentShape
	 */
	public void remove(Shape currentShape) {
		engine.removeShape(currentShape);

	}

	/**
	 * add new Shape
	 * 
	 * @param newShape
	 */
	public void addShape(Shape newShape) {
		engine.addShape(newShape);

	}

	/**
	 * remove shape without update
	 * 
	 * @param currentShape
	 */
	public void removeWithoutUpdate(Shape currentShape) {
		engine.removeDraggedShape(currentShape);

	}

	/**
	 * open file
	 * 
	 * @param path
	 */
	public void openFile(String path) {
		engine.load(path);

	}

	/**
	 * save file
	 * 
	 * @param path
	 */
	public void saveFile(String path) {
		engine.save(path);

	}

	/**
	 * load class
	 * 
	 * @param path
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Class<?>> loadClass(String path) {

		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		JarFile jarFile;
		try {
			jarFile = new JarFile(path);
			Enumeration<JarEntry> e = jarFile.entries();
			try {

			 URL[] urls = { new URL("jar:file:" + path +"!/") };;
				URLClassLoader cl = URLClassLoader.newInstance(urls);
				while (e.hasMoreElements()) {
					boolean add = true;
					JarEntry je = e.nextElement();
					if (je.isDirectory() || !je.getName().endsWith(".class")) {
						continue;
					}
					String className = je.getName().substring(0, je.getName().length() - 6);
					className = className.replace('/', '.');
					Class<? extends Shape> c;
					try {
						c = (Class<? extends Shape>)  cl.loadClass(className);
						if (!c.isInterface()
                                && !Modifier.isAbstract(c.getModifiers())) {
							Method[] methods = c.getMethods();
							  for (Method method:methods) {
							    if (method.getName().equals("main")) {
							    	add = false;
							    }
							  }
							  if(add){
								  classes.add(c);
							  }
                        }
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			jarFile.close();

			} catch (MalformedURLException e2) {
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		for (Class<?> ch : classes) {
			System.out.println(ch.getName());
		}

		for (Class<?> ch : classes) {
			make.addnewClass(ch, ch.getSimpleName());
		}
		
		return classes;

	}

	public Rectangle2D getBounds(Shape currentShape) {
		return engine.getBounds(currentShape);
	}

	public Shape makeNewShape(String simpleName) {
		
		return make.newShape(simpleName);
	}

	public Class<? extends Shape> getClass(String text) {
		return (Class<? extends Shape>) make.getNewClass(text);
	}

	public void clearAll() {
		engine.clearAll();
	}

	
}
