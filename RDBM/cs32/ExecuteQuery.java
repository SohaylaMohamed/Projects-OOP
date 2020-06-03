package eg.edu.alexu.csd.oop.db.cs32;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class ExecuteQuery {
	private DataBase db = new DataBase();
	private List<List<Object>> list = new ArrayList<List<Object>>();

	public int insertInAll(String table, ArrayList<String> set) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		File t = new File(db.getDirectory() + System.getProperty("file.separator") + table.toLowerCase() + ".xml");
		try {
			ArrayList<FieldType> fields = this.getFields(table.toLowerCase());
			Document document = dbf.newDocumentBuilder().parse(t);
			Element parent = document.getDocumentElement();
			parent.getFirstChild();
			Element address = document.createElement("row");
			NodeList n1 = parent.getElementsByTagName("row");
			if (n1.getLength() > 0) {
				int n = Integer.parseInt(n1.item(0).getAttributes().getNamedItem("id").getNodeValue());
				address.setAttribute("id", Integer.toString(n + 1));
				n1.item(0).getParentNode().insertBefore(address, null);
			} else {
				address.setAttribute("id", Integer.toString(1));
				parent.appendChild(address);
			}
			for (int k = 0; k < fields.size(); k++) {
				Element firstname = document.createElement(fields.get(k).getName().toLowerCase());
				Text x = document.createTextNode(set.get(k).toLowerCase());
				firstname.appendChild(x);
				address.appendChild(firstname);
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(t);
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			transformer.transform(source, result);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
		// TODO Auto-generated method stub

	}

	public boolean createDatabase(String database) {

		String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "Database"
				+ System.getProperty("file.separator") + database.toLowerCase();
		File d = new File(path);
		if (d.mkdirs()) {
			db.setPath(d.getAbsolutePath());
			db.addNewDatabase(database.toLowerCase(), d.getAbsolutePath());
			return true;
		} else {
			if (d.exists()) {
				db.setPath(d.getPath());
				db.addNewDatabase(database.toLowerCase(), d.getPath());
			}

			return false;
		}

	}

	public boolean createTable(String table, ArrayList<String> field, ArrayList<String> Type) throws SQLException {
		return db.createNewTable(table.toLowerCase(), field, Type);

	}

	public boolean dropDataBase(String database) {

		return db.deleteDB(database.toLowerCase());

	}

	public boolean dropTable(String table) {

		return db.deleteTable(table.toLowerCase());

	}

	public int deleteFromTableWithCondition(String table, char oper, String where, String whereValue) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		int rows = 0;
		File t = new File(db.getDirectory() + System.getProperty("file.separator") + table.toLowerCase() + ".xml");

		Document document;
		try {
			document = dbf.newDocumentBuilder().parse(t);
			Element parent = document.getDocumentElement();
			NodeList n = parent.getElementsByTagName("row");
			int i = 0;

			for (i = 0; i < n.getLength(); i++) {
				Node row3 = n.item(i);
				NodeList l = n.item(i).getChildNodes();
				for (int k = 0; k < l.getLength(); k++) {
					if (l.item(k).getNodeType() == Node.ELEMENT_NODE) {
						Element e1 = (Element) l.item(k);
						NodeList child = e1.getChildNodes();
						if (e1.getTagName().equalsIgnoreCase(where)) {
							if (child.getLength() == 0) {
								continue;
							}
							if (checkInteger(where, table.toLowerCase())) {
								int value = Integer.parseInt(whereValue);
								int nodeValue = Integer.parseInt(child.item(0).getNodeValue());
								if ((oper == '=' && nodeValue == value) || (oper == '>' && nodeValue > value)
										|| (oper == '<' && nodeValue < value)) {
									parent.removeChild(row3);
									rows++;

								}
							} else if (child.item(0).getNodeValue().equalsIgnoreCase(whereValue)) {
								parent.removeChild(row3);
								rows++;

							}
						}
					}

				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(t);
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			transformer.transform(source, result);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rows;

	}

	public int deleteFromTable(String table) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		int rows = 0;
		File t = new File(db.getDirectory() + System.getProperty("file.separator") + table.toLowerCase() + ".xml");

		Document document;

		try {
			document = dbf.newDocumentBuilder().parse(t);
			Element parent = document.getDocumentElement();
			NodeList n = parent.getElementsByTagName("row");
			while (n.getLength() > 0) {
				Node row3 = parent.getElementsByTagName("row").item(0);
				parent.removeChild(row3);
				rows++;
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(t);
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			transformer.transform(source, result);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rows;

	}

	public int updateTableWithCondition(String table, ArrayList<String> updateValue, ArrayList<FieldType> someFields,
			String where, String whereValue, char oper) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		File t = new File(db.getDirectory() + System.getProperty("file.separator") + table.toLowerCase() + ".xml");
		int rows = 0;
		Document document;
		try {
			document = dbf.newDocumentBuilder().parse(t);
			NodeList n = document.getElementsByTagName("row");
			if (n.getLength() == 0) {
				return 0;
			}
			for (int i = 0; i < n.getLength(); i++) {
				Node node = n.item(i);
				NodeList list = node.getChildNodes();
				for (int j = 0; j < list.getLength(); j++) {
					if (list.item(j).getNodeType() == Node.ELEMENT_NODE) {
						Element e = (Element) list.item(j);
						if (checkInteger(where, table.toLowerCase())) {
							if (e.getTagName().equalsIgnoreCase(where)) {

								NodeList l = e.getChildNodes();
								if (l.getLength() == 0) {
									continue;
								}
								int value = Integer.parseInt(whereValue);
								int nodeValue = Integer.parseInt(l.item(0).getTextContent());
								if ((oper == '=' && nodeValue == value) || (oper == '>' && nodeValue > value)
										|| (oper == '<' && nodeValue < value)) {
									boolean found = false;
									for (int k = 0; k < list.getLength(); k++) {
										if (list.item(k).getNodeType() == Node.ELEMENT_NODE) {
											Element e2 = (Element) list.item(k);
											for (int p = 0; p < someFields.size(); p++) {
												if (e2.getTagName().equalsIgnoreCase(someFields.get(p).getName())) {
													e2.setTextContent(updateValue.get(p).toLowerCase());
													found = true;
												}
											}
										}
									}
									if (found) {
										rows++;
									}
								}
							}
						} else if (e.getTagName().equalsIgnoreCase(where)) {
							NodeList l = e.getChildNodes();
							if (l.getLength() == 0) {
								continue;
							}
							if (l.item(0).getTextContent().equalsIgnoreCase(whereValue)) {
								boolean found = false;
								for (int k = 0; k < list.getLength(); k++) {
									if (list.item(k).getNodeType() == Node.ELEMENT_NODE) {
										Element e2 = (Element) list.item(k);
										for (int p = 0; p < someFields.size(); p++) {
											if (e2.getTagName().equalsIgnoreCase(someFields.get(p).getName())) {
												e2.setTextContent(updateValue.get(p).toLowerCase());
												found = true;
											}
										}
									}
								}
								if (found) {
									rows++;
								}
							}
						}
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(t);
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			transformer.transform(source, result);

		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rows;

	}

	public boolean checkInteger(String where, String table) {

		ArrayList<FieldType> arrF = db.getTableFields(table.toLowerCase());
		for (FieldType f : arrF) {
			if (f.getName().equalsIgnoreCase(where)) {
				if (f.isInteger()) {
					return true;
				}
			}
		}

		return false;

	}

	public boolean ifExist(String where, String table) {
		ArrayList<FieldType> arrF = db.getTableFields(table.toLowerCase());
		for (FieldType f : arrF) {
			if (f.getName().equalsIgnoreCase(where)) {

				return true;
			}
		}
		return false;
	}

	public List<List<Object>> selectAllWithCondition(String table, String where, char oper, String whereValue) {

		list = new ArrayList<List<Object>>();
		ArrayList<FieldType> fields = db.getTableFields(table.toLowerCase());
		if (db.checkTableExistance(table.toLowerCase())) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = null;
			try {
				File text = new File(db.getDirectory() + System.getProperty("file.separator") + table.toLowerCase() + ".xml");
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(text);

				Node objects = doc.getDocumentElement();
				for (Node object = objects.getFirstChild(); object != null; object = object.getNextSibling()) {
					List<Object> subList = new ArrayList<Object>();
					if (object instanceof Element) {
						Element e = (Element) object;

						if (e.getTagName().equalsIgnoreCase("row")) {
							NodeList elements = e.getElementsByTagName("*");
							for (int i = 0; i < elements.getLength(); i++) {
								NodeList l = elements.item(i).getChildNodes();
								Element e1 = (Element) elements.item(i);
								if (e1.getTagName().equalsIgnoreCase(where)) {
									if (l.getLength() == 0) {
										continue;
									}
									if (checkInteger(where, table.toLowerCase())) {
										int value = Integer.parseInt(whereValue);

										int nodeValue = Integer.parseInt(l.item(0).getNodeValue());
										if ((oper == '=' && nodeValue == value) || (oper == '>' && nodeValue > value)
												|| (oper == '<' && nodeValue < value)) {
											for (int j = 0; j < elements.getLength(); j++) {

												NodeList list = elements.item(j).getChildNodes();
												if (list.getLength() == 0) {
													subList.add(null);
												} else if (fields.get(j).isInteger()) {

													subList.add(Integer.parseInt(list.item(0).getNodeValue()));
												} else {
													subList.add(list.item(0).getNodeValue());
												}

											}

											list.add(subList);

										}
									} else if (l.item(0).getNodeValue().equalsIgnoreCase(whereValue)) {
										for (int j = 0; j < elements.getLength(); j++) {

											NodeList list = elements.item(j).getChildNodes();
											if (list.getLength() == 0) {
												subList.add(null);
											} else if (fields.get(j).isInteger()) {

												subList.add(Integer.parseInt(list.item(0).getNodeValue()));
											} else {
												subList.add(list.item(0).getNodeValue());
											}

										}

										list.add(subList);

									}
								}
							}
						}
					}
				}
			}

			catch (ParserConfigurationException e1) { // TODO Auto-generated
														// catch block
				e1.printStackTrace();
			} catch (SAXException e1) { // TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) { // TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;

	}

	public List<List<Object>> selectDisWithCondition(String table, ArrayList<String> select, String where,
			String whereValue, char oper) {
		list = new ArrayList<List<Object>>();
		if (db.checkTableExistance(table.toLowerCase())) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = null;

			File text = new File(db.getDirectory() + System.getProperty("file.separator") + table.toLowerCase() + ".xml");
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(text);

				Node objects = doc.getDocumentElement();
				for (Node object = objects.getFirstChild(); object != null; object = object.getNextSibling()) {
					List<Object> subList = new ArrayList<Object>();
					if (object instanceof Element) {
						Element e = (Element) object;
						NodeList elements = e.getElementsByTagName("*");
						for (int i = 0; i < elements.getLength(); i++) {

							NodeList l = elements.item(i).getChildNodes();
							Element e1 = (Element) elements.item(i);
							if (e1.getTagName().equalsIgnoreCase(where)) {
								if (l.getLength() == 0) {
									continue;
								}
								if (checkInteger(where, table.toLowerCase())) {
									int value = Integer.parseInt(whereValue);

									int nodeValue = Integer.parseInt(l.item(0).getNodeValue());
									if ((oper == '=' && nodeValue == value) || (oper == '>' && nodeValue > value)
											|| (oper == '<' && nodeValue < value)) {
										for (String s : select) {

											elements = e.getElementsByTagName(s.toLowerCase());
											l = elements.item(0).getChildNodes();
											if (l.getLength() == 0) {
												subList.add(null);
											} else if (checkInteger(s, table.toLowerCase())) {

												subList.add(Integer.parseInt(l.item(0).getNodeValue()));
											} else {
												subList.add(l.item(0).getNodeValue());
											}

										}

										list.add(subList);

									}
								} else if (l.item(0).getNodeValue().equalsIgnoreCase(whereValue)) {
									for (String s : select) {

										elements = e.getElementsByTagName(s.toLowerCase());
										l = elements.item(0).getChildNodes();
										if (l.getLength() == 0) {
											subList.add(null);
										} else if (checkInteger(s, table.toLowerCase())) {

											subList.add(Integer.parseInt(l.item(0).getNodeValue()));
										} else {
											subList.add(l.item(0).getNodeValue());
										}

									}

									list.add(subList);
								}
							}

						}

					}
				}
			} catch (ParserConfigurationException e2) { // TODO Auto-generated
														// catch block
				e2.printStackTrace();
			} catch (SAXException e2) { // TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2)

			{ // TODO Auto-generated catch block
				e2.printStackTrace();
			}

		}
		return list;

	}

	public List<List<Object>> selectDistinct(String table, ArrayList<String> select) {
		list = new ArrayList<List<Object>>();
		if (db.checkTableExistance(table.toLowerCase())) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = null;

			File text = new File(db.getDirectory() + System.getProperty("file.separator") + table.toLowerCase() + ".xml");

			try {
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(text);

				Node objects = doc.getDocumentElement();
				for (Node object = objects.getFirstChild(); object != null; object = object.getNextSibling()) {
					List<Object> subList = new ArrayList<Object>();
					if (object instanceof Element) {
						Element e = (Element) object;
						for (String s : select) {
							NodeList elements = e.getElementsByTagName(s.toLowerCase());
							NodeList l = elements.item(0).getChildNodes();
							if (l.getLength() == 0) {
								subList.add(null);
							} else if (checkInteger(s, table.toLowerCase())) {

								subList.add(Integer.parseInt(l.item(0).getNodeValue()));
							} else {
								subList.add(l.item(0).getNodeValue());
							}
						}
						list.add(subList);

					}
				}
			} catch (ParserConfigurationException e1) { // TODO Auto-generated
														// catch block
				e1.printStackTrace();
			} catch (SAXException e1) { // TODOAuto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) { // TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return list;

	}

	public List<List<Object>> selectAll(String tableName) {
		list = new ArrayList<List<Object>>();
		if (db.checkTableExistance(tableName.toLowerCase())) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = null;
			try {
				File text = new File(db.getDirectory() + System.getProperty("file.separator") + tableName.toLowerCase() + ".xml");
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(text);

				Node objects = doc.getDocumentElement();
				for (Node object = objects.getFirstChild(); object != null; object = object.getNextSibling()) {
					List<Object> subList = new ArrayList<Object>();
					if (object instanceof Element) {
						Element e = (Element) object;

						if (e.getTagName().equalsIgnoreCase("row")) {

							NodeList elements = e.getElementsByTagName("*");

							for (int i = 0; i < elements.getLength(); i++) {
								// Get element
								NodeList l = elements.item(i).getChildNodes();
								Element el = (Element) elements.item(i);

								if (l.getLength() == 0) {
									subList.add(null);
								} else if (checkInteger(el.getTagName(), tableName.toLowerCase())) {

									subList.add(Integer.parseInt(l.item(0).getNodeValue()));
								} else {
									subList.add(l.item(0).getNodeValue());
								}
							}
							list.add(subList);
						}
					}
				}

			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;

	}

	public boolean checkTableExist(String table) {

		return db.checkTableExistance(table.toLowerCase());
	}

	public ArrayList<FieldType> getFields(String table) {

		return db.getTableFields(table.toLowerCase());
	}

	public int insertInDist(String table, ArrayList<FieldType> someFields, ArrayList<String> insertValue) {
		ArrayList<FieldType> fields = db.getTableFields(table.toLowerCase());
		ArrayList<FieldType> excessFields = getExcessFields(fields, someFields);
		if (excessFields.isEmpty()) {
			return insertInAll(table.toLowerCase(), insertValue);
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		File t = new File(db.getDirectory() + System.getProperty("file.separator") + table.toLowerCase() + ".xml");
		try {
			Document document = dbf.newDocumentBuilder().parse(t);
			Element parent = document.getDocumentElement();
			parent.getFirstChild();
			Element address = document.createElement("row");
			NodeList n1 = parent.getElementsByTagName("row");
			if (n1.getLength() > 0) {
				int n = Integer.parseInt(n1.item(0).getAttributes().getNamedItem("id").getNodeValue());
				address.setAttribute("id", Integer.toString(n + 1));
				n1.item(0).getParentNode().insertBefore(address, null);
			} else {
				address.setAttribute("id", Integer.toString(1));
				parent.appendChild(address);
			}
			int i = 0;
			int j = 0;
			for (int k = 0; k < fields.size(); k++) {
				if (inSelected(fields.get(k), someFields)) {
					Element el = document.createElement(someFields.get(j).getName().toLowerCase());
					Text x = document.createTextNode(insertValue.get(j).toLowerCase());
					el.appendChild(x);
					address.appendChild(el);
					j++;
				} else {
					Element el = document.createElement(excessFields.get(i).getName().toLowerCase());
					if (excessFields.get(i).isInteger()) {
						Text x = document.createTextNode("");
						el.appendChild(x);

					} else {
						Text x = document.createTextNode("");
						el.appendChild(x);

					}
					address.appendChild(el);
					i++;
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(t);
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			transformer.transform(source, result);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 1;

	}

	private boolean inSelected(FieldType fieldType, ArrayList<FieldType> someFields) {
		for (FieldType ft2 : someFields) {

			if (ft2.getName().equalsIgnoreCase(fieldType.getName())) {
				return true;
			}
		}

		return false;
	}

	private ArrayList<FieldType> getExcessFields(ArrayList<FieldType> fields, ArrayList<FieldType> someFields) {
		ArrayList<FieldType> excessFields = new ArrayList<>();
		for (FieldType ft : fields) {
			boolean found = false;
			for (FieldType ft2 : someFields) {
				if (ft2.getName().equalsIgnoreCase(ft.getName())) {
					found = true;
					break;
				}
			}
			if (!found) {
				excessFields.add(ft);
			}
		}
		return excessFields;
	}

	public int updateTable(String table, ArrayList<String> updateValue, ArrayList<FieldType> someFields) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		int rows = 0;
		File t = new File(db.getDirectory() + System.getProperty("file.separator") + table.toLowerCase() + ".xml");
		Document document;
		try {
			document = dbf.newDocumentBuilder().parse(t);
			NodeList n = document.getElementsByTagName("row");
			if (n.getLength() == 0) {
				return 0;
			}
			for (int i = 0; i < n.getLength(); i++) {
				Node node = n.item(i);
				NodeList list = node.getChildNodes();
				boolean found = false;
				for (int k = 0; k < list.getLength(); k++) {
					if (list.item(k).getNodeType() == Node.ELEMENT_NODE) {
						Element e2 = (Element) list.item(k);
						for (int p = 0; p < someFields.size(); p++) {
							if (e2.getTagName().equalsIgnoreCase(someFields.get(p).getName())) {
								e2.setTextContent(updateValue.get(p).toLowerCase());
								found = true;
							}
						}
					}
				}
				if (found) {
					rows++;
				}

			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(t);
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			transformer.transform(source, result);

		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rows;
	}

	public boolean checkDatabaseExist(String database) {
		String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "Database"
				+ System.getProperty("file.separator") + database.toLowerCase();
		File d = new File(path);
		return d.exists();
	}

	public boolean checkString(String where, String table) {
		ArrayList<FieldType> arrF = db.getTableFields(table.toLowerCase());
		for (FieldType f : arrF) {
			if (f.getName().equalsIgnoreCase(where)) {
				if (f.isString()) {
					return true;
				}
			}
		}

		return false;
	}

}
