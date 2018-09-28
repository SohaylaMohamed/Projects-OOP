package eg.edu.alexu.csd.oop.db.cs32;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eg.edu.alexu.csd.oop.db.Database;

public class DataBase implements Database {

	private File d;
	private String Path = System.getProperty("user.dir") + File.separator + "Database";
	public HashMap<String, String> dataDirectory = new HashMap<>();
	public HashMap<String, ArrayList<String>> dataTables = new HashMap<>();
	public HashMap<String, ArrayList<FieldType>> tableFields = new HashMap<>();
	private String currentDB = null;
	private List<List<Object>> selectedObjects = new ArrayList<>();

	public DataBase() {
		d = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "Database");
		d.mkdirs();
	}

	public String createDatabase(String databaseName, boolean dropIfExists) {
		try {
			if (!dropIfExists) {
				executeStructureQuery("create database" + " " + databaseName);
			} else {
				executeStructureQuery("DROP DATABASE" + " " + databaseName);
				executeStructureQuery("CREATE DATABASE" + " " + databaseName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Path;
	}

	public boolean executeStructureQuery(String query) throws SQLException {
		String s = query.toLowerCase();
		if (s.contains("create")) {
			return QueryAdapter.getInstance().createQuery(query);
		}
		if (s.contains("drop")) {
			return QueryAdapter.getInstance().dropQuery(query);
		}
		return false;
	}

	@Override
	public Object[][] executeQuery(String query) throws SQLException {

		selectedObjects = QueryAdapter.getInstance().selectQuery(query);
		if (selectedObjects.isEmpty()) {

			return new Object[0][0];
		}
		Object[][] ob = new Object[selectedObjects.size()][selectedObjects.get(0).size()];
		for (int j = 0; j < selectedObjects.size(); j++) {
			for (int i = 0; i < selectedObjects.get(j).size(); i++) {
				ob[j][i] = selectedObjects.get(j).get(i);
			}
		}
		return ob;
	}

	@Override
	public int executeUpdateQuery(String query) throws SQLException {
		String s = query.toLowerCase();
		if (s.contains("insert")) {
			return QueryAdapter.getInstance().insertQuery(query);
		}
		if (s.contains("delete")) {
			return QueryAdapter.getInstance().deleteQuery(query);
		}
		if (s.contains("update")) {
			return QueryAdapter.getInstance().updateQuery(query);
		}
		return 0;
	}

	public boolean createNewTable(String table, ArrayList<String> field, ArrayList<String> Type) throws SQLException {
		if (currentDB == null) {
			return false;
		}
		if (currentDB != null) {
			String path = dataDirectory.get(currentDB.toLowerCase()) + File.separator;
			File xFile = new File(path, table.toLowerCase() + ".xml");
			File dFile = new File(path, table.toLowerCase() + ".dtd");
			try {
				if (!tableExist(table.toLowerCase())) {
					try {
						PrintWriter writer = new PrintWriter(new FileWriter(path + table.toLowerCase() + ".txt"));
						dFile.createNewFile();
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = null;
						dBuilder = dbFactory.newDocumentBuilder();
						Document doc = dBuilder.newDocument();

						Element rootElement = doc.createElement(table.toLowerCase());
						doc.appendChild(rootElement);

						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(doc);
						StreamResult result = new StreamResult(xFile);
						transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
						transformer.transform(source, result);
						tableFields.put(table.toLowerCase(), new ArrayList<FieldType>());

						for (int s = 0; s < field.size(); s++) {
							boolean isInteger = false, isString = false;
							if (Type.get(s).equalsIgnoreCase("int")) {
								isInteger = true;
							} else if (Type.get(s).equalsIgnoreCase("varchar")) {
								isString = true;
							}
							FieldType f = new FieldType(isInteger, isString, field.get(s));
							writer.println(f.getName() + "," + f.isInteger() + "," + f.isString());
							tableFields.get(table.toLowerCase()).add(f);
						}
						writer.close();
						dataTables.get(currentDB.toLowerCase()).add(table.toLowerCase());
						return true;
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (!this.checkTableExistance(table.toLowerCase())) {
					dataTables.get(currentDB.toLowerCase()).add(table.toLowerCase());
					addTableFields(table.toLowerCase(), field, Type);
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return false;
	}

	private void addTableFields(String table, ArrayList<String> field, ArrayList<String> Type) {
		ArrayList<FieldType> fields = new ArrayList<>();
		for (int i = 0; i < field.size(); i++) {
			boolean isInteger = false, isString = false;
			if (Type.get(i).equalsIgnoreCase("int")) {
				isInteger = true;
			} else {
				isString = true;
			}
			FieldType f = new FieldType(isInteger, isString, field.get(i));
			fields.add(f);
		}
		tableFields.put(table.toLowerCase(), fields);
	}

	private boolean tableExist(String table) {
		File folder = new File(dataDirectory.get(currentDB.toLowerCase()));
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				if (listOfFiles[i].getName().split("\\.")[0].equalsIgnoreCase(table)) {
					return true;
				}
			}
		}
		return false;
	}

	public String getParentFolder() {
		return "Database";
	}

	public void setPath(String path2) {
		this.Path = path2;

	}

	public void addNewDatabase(String database, String path2) {
		dataDirectory.put(database.toLowerCase(), path2);
		dataTables.put(database.toLowerCase(), new ArrayList<String>());
		currentDB = database.toLowerCase();

	}

	public boolean deleteDB(String database) {
		String path = System.getProperty("user.dir") + File.separator + getParentFolder() + File.separator
				+ database.toLowerCase();
		File file = new File(path);
		if (!file.exists()) {

			return false;

		}
		File[] listOfFiles = file.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {

			listOfFiles[i].delete();

		}
		dataDirectory.remove(database.toLowerCase());
		dataTables.remove(database.toLowerCase());
		if (currentDB == database.toLowerCase()) {
			currentDB = null;
		}
		return file.delete();

	}

	public boolean checkTableExistance(String tableName) {
		if (currentDB == null) {
			return false;
		}
		return dataTables.get(currentDB.toLowerCase()).contains(tableName.toLowerCase());
	}

	public boolean deleteTable(String table) {
		if (checkTableExistance(table.toLowerCase())) {
			String path = dataDirectory.get(currentDB.toLowerCase());
			File file = new File(path);
			File[] listOfFiles = file.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String name = listOfFiles[i].getName().split("\\.")[0];
					if (name.equalsIgnoreCase(table.toLowerCase())) {
						listOfFiles[i].delete();
					}
				}
			}
			dataTables.get(currentDB.toLowerCase()).remove(table.toLowerCase());
			tableFields.remove(table.toLowerCase());
			return true;
		}

		return false;
	}

	public ArrayList<FieldType> getTableFields(String table) {
		/*
		 * String path = dataDirectory.get(currentDB); File f = new File(path +
		 * File.separator + table + ".txt"); ArrayList<FieldType> ta = new
		 * ArrayList<>(); try { BufferedReader br = new BufferedReader(new
		 * FileReader(f)); StringBuilder sb = new StringBuilder(); String line =
		 * br.readLine();
		 * 
		 * while (line != null) { String[] split = line.split(","); boolean
		 * isInteger = false , isString = false; String Name = split[0];
		 * if(split[1].equalsIgnoreCase("true") ) { isInteger = true; } else {
		 * isString = true; } FieldType field = new FieldType(isInteger,
		 * isString, Name); ta.add(field); } tableFields.put(table, ta);
		 * br.close();
		 * 
		 * } catch (FileNotFoundException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		return tableFields.get(table.toLowerCase());
	}

	public String getDirectory() {
		return dataDirectory.get(currentDB.toLowerCase());
	}

	public void setDatabase(String string) {
		currentDB = dataDirectory.get(string);
	}

}
