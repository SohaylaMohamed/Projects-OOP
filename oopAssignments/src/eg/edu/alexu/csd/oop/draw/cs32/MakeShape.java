package eg.edu.alexu.csd.oop.draw.cs32;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;

public class MakeShape {
	List<Class<? extends Shape>> classes = new ArrayList<>();
	static Map<String, Class<?>> shapeObject = new HashMap<>();
	private static MakeShape instance = null;
	/**
	 * constructor for class
	 */
	private MakeShape() {
		DrawingCanvas d = new DrawingCanvas();
		classes = d.getSupportedShapes();
		InitializeShapeObjects();
	}
	/**
	 * Add new class 
	 * @param clasS new class
	 * @param s key to the class, simple name
	 */
	public void addnewClass(Class<?> clasS, String s) {
		shapeObject.put(s, clasS);
	}
	/**
	 * Initializing the classes with the original supported classes
	 */
	private void InitializeShapeObjects() {
		for (Class<?> clasS : classes) {
			StringBuilder path = new StringBuilder();
			String s = clasS.getName();
			String[] arr = s.split("\\.");
			s = new String(arr[arr.length - 1]);
			path.append(s);
			shapeObject.put(path.toString(), clasS);
		}
	}
	/**
	 * class is supported or not 
	 * @param name of the class
	 * @return true if supported
	 */
	public boolean exist(String str) {
		String[] arr = str.split("\\.");
		String s = new String(arr[arr.length - 1]);
		return shapeObject.containsKey(s);
	}
	/**
	 * create make shape instance
	 * @return
	 */
	public static MakeShape getInstance() {
		if (instance == null) {
			instance = new MakeShape();
		}
		return instance;
	}
	/**
	 * make new shape given it's class name
	 * @param name class name
	 * @return new Shape instance of this class
	 */
	public Shape newShape(String name) {
		String[] arr = name.split("\\.");
		String s = new String(arr[arr.length - 1]);
		Class<?> shape = shapeObject.get(s);
		Shape newShape;
		try {
			newShape = (Shape) shape.newInstance();
			return newShape;

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public HashMap<String, Class<?>> getSupportedShapes() {
		return new HashMap<String, Class<?>>(shapeObject) ;
	}
	public Class<?> getNewClass(String text) {
		return shapeObject.get(text);
	}

}
