package eg.edu.alexu.csd.oop.draw.cs32;

import java.awt.Color;
import java.awt.List;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eg.edu.alexu.csd.oop.draw.Shape;

public class Json {

	public void writeIntoFile(ArrayList<Shape> shapes, String path) {

		PrintWriter wr;
		try {
			wr = new PrintWriter(new FileWriter(path));
			wr.println("{");
			wr.println(" \"Shapes\":[");
			boolean startShape = true;
			for (Shape shape : shapes) {
				if(shape == null){
					shape = new Rectangle();
				}
				
				if(shape.getProperties() == (null)){
					if(MakeShape.getInstance().exist(shape.getClass().getName())){
				shape = MakeShape.getInstance().newShape(shape.getClass().getName());
					}else{
						shape = new Rectangle();;
					}
					
				}
				if (startShape) {
					wr.println("\t{");
					startShape = false;
				} else {
					wr.println("\t{");
				}
				
				String[] arr = shape.getClass().getName().split("\\.");
				String clasS = arr[arr.length-1];
				
				
				wr.println("\t\t\"class\": " + "\"" + clasS + "\",");
				
				wr.println("\t\t\"position\": " + "\"" +  Objects.toString(shape.getPosition(), null) + "\",");

				wr.println("\t\t\"color\": " + "\"" + Objects.toString(shape.getColor(), null)+ "\",");
				
				wr.println("\t\t\"fillColor\": " + "\"" + Objects.toString(shape.getFillColor(), null) + "\",");
				
				String properties = shape.getProperties().toString();
				
				wr.println("\t\t\"properties\": {");
				int len = properties.length();
				properties = properties.substring(1, len - 1);
				len = len - 2;

				for (int i = 0; i < len; i++) {

					StringBuilder name = new StringBuilder();
					StringBuilder prop = new StringBuilder();

					while (properties.charAt(i) != '=') {
						name.append(properties.charAt(i));
						i++;
					}
					i++;
					while (properties.charAt(i) != ',') {
						prop.append(properties.charAt(i));
						i++;
						if (i >= len) {
							break;
						}
					}
					i++;
					if (i >= len) {
						wr.println("\t\t\t\"" + name.toString() + "\": \"" + prop.toString() + "\"");
						wr.println("\t\t}");
						wr.println("\t},");
						break;
					}
					wr.println("\t\t\t\"" + name.toString() + "\": \"" + prop.toString() + "\",");

				}

			}
			wr.println(" ]");
			wr.print("}");
			wr.close();
			Scanner sc = new Scanner(new File(path));
			ArrayList<String> lines = new ArrayList<String>();
			while (sc.hasNextLine()) {
				String s = sc.nextLine();
				s = s.replaceAll("\t", "");
				lines.add(s);

			}
			for (String s : lines) {
				System.out.println(s);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("resource")
	public ArrayList<Shape> readFile(String path) {
		Scanner sc;
		try {
			sc = new Scanner(new File(path));
			ArrayList<Shape> shapes = new ArrayList<>();
			ArrayList<String> lines = new ArrayList<String>();
			Map<String, Double> pr = new HashMap<>();
			while (sc.hasNextLine()) {
				lines.add(sc.nextLine().replaceAll("\t", ""));

			}
			Shape shape = null;
			boolean prop = false;
			for (String s : lines) {
				if (s.contains("\"properties\"")) {
					prop = true;
					continue;
				} else if (s.charAt(0) == '}') {
					prop = false;
					if(shape!= null){
					shape.setProperties(pr);
					shapes.add(shape);
					pr = new HashMap<>();
					shape = null;
					}
					continue;
				} else if (prop) {
					pr.putAll(properties(s));
					continue;
				} else if (s.contains("\"class\"")) {
					shape = setNewShape(s);
				} else if (s.contains("\"position\"")) {
					shape.setPosition(findPos(s));
				} else if (s.contains("\"color\"")) {
					shape.setColor(color(s));
				} else if (s.contains("\"fillColor\"")) {
					shape.setFillColor(color(s));
				}
			}
			return shapes;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	private Map<String, Double> properties(String s) {
		String[] temp = s.split(":");
		String key = temp[0];
		String value = temp[1];
		value = value.replaceAll("\"", "");
		value = value.replaceAll(" ", "");
		value = value.replaceAll(",", "");

		Map<String, Double> prop = new HashMap<>();
		prop.put(key.replaceAll("\"", ""), Double.valueOf(value));
		return prop;
	}

	private Shape setNewShape(String s) {
		String[] temp = s.split(":");
		String sent = temp[1];
		sent = sent.replaceAll("\"", "");
		sent = sent.replaceAll(" ", "");
		sent = sent.replaceAll(",", "");
		Shape shape = MakeShape.getInstance().newShape(sent);
		return shape;
	}

	private Point findPos(String s) {
		String[] temp = s.split(":");
		String sent = temp[1];
		sent = sent.replaceAll("\"", "");
		sent = sent.replaceAll(" ", "");
		sent = sent.replaceAll(",", "");

		Pattern p = Pattern.compile("java.awt.Point\\[x=(.*?)y=(.*?)\\]");
		Matcher m = p.matcher(sent);
		Point point = null;
		if (m.find()) {
			String x = m.group(1);
			String y = m.group(2);
			point = new Point(Integer.parseInt(x), Integer.parseInt(y));
		}
		return point;
	}

	private Color color(String s) {
		String[] temp = s.split(":");
		String sent = temp[1];
		sent = sent.replaceAll("\"", "");
		sent = sent.replaceAll(" ", "");

		Pattern p = Pattern.compile("java.awt.Color\\[r=(.*?),g=(.*?),b=(.*?)\\]");
		Matcher m = p.matcher(sent);
		Color c = null;
		if (m.find()) {
			int r = Integer.parseInt(m.group(1));
			int b = Integer.parseInt(m.group(2));
			int g = Integer.parseInt(m.group(3));
			c = new Color(r, b, g);

		}
		return c;
	}

}
