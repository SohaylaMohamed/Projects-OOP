package eg.edu.alexu.csd.oop.db.cs32;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class UI {
	private DataBase db = new DataBase();
	private boolean dbExists;
	private ArrayList<String> tables;
	private String query = "";
	private Scanner sc;

	public UI() {
		dbExists = false;
		tables = new ArrayList<>();
		sc = new Scanner(System.in);
		main();
		sc.close();
		return;
	}

	private void main() {
		tables.clear();
		if (!dbExists) {
			System.out.println("Enter the Name of the database");
			dbExists = true;
			startingDatabase();
		} else {
			System.out.println("Enter the Name of the database");
			createNewDatabase();
		}
	}

	private void createNewDatabase() {
		String d = "";
		String path = "";
		while (sc.hasNextLine()) {
			d = sc.next();
			path = db.createDatabase(d, false);
			break;
		}
		System.out.println("Database : " + d + "\nis created at -> " + path);
		startTable(d);
		return;

	}

	private void startingDatabase() {
		String d = "";
		String path = "";
		while (sc.hasNextLine()) {
			d = sc.next();
			path = db.createDatabase(d, false);
			break;
		}
		System.out.println("Database : " + d + "\nis created at -> " + path);
		startTable(d);
		return;
	}

	private void startTable(String d) {
		System.out.println("You're connected to " + d);
		if (tables.isEmpty()) {
			System.out.print("You currently have no tables in this database, create one.");
			createTable(d);
		}

	}

	private void createTable(String d) {
		System.out.println("Insert -2 to check the existing table");
		System.out.print("Table's name : ");
		String tableName = "";
		while (sc.hasNext()) {
			tableName = sc.next();
			if (tableName.equals("-2")) {
				chooser(d);
				return;
			}
			break;
		}
		System.out
				.println("Enter the fields and the types on the form each field followed by its type, -1 to eleminate");
		ArrayList<String> str = new ArrayList<String>();
		while (true) {
			String sT = sc.next();
			if (sT.equals("-1")) {
				break;
			}
			str.add(sT);
		}
		System.out.print(str.toString());
		StringBuilder b = new StringBuilder();
		b.append("(");
		for (String v : str) {
			b.append(" " + v);
			if (v.equalsIgnoreCase("int") || v.equalsIgnoreCase("varchar") || v.equalsIgnoreCase("varchar(255)")) {
				b.append(",");
			}
		}
		b = new StringBuilder(b.substring(0, b.length() - 1));
		b.append(")");

		String fields = b.toString();

		try {
			boolean suc = db.executeStructureQuery("create table " + tableName + fields);
			if (suc) {
				System.out.println("Table " + tableName + " is created successfuly in " + d);
				tables.add(tableName);
				chooser(d);
			} else {
				System.out.println("Table " + tableName + " was not created successfuly try again " + d);
				createTable(d);
				return;

			}
		} catch (SQLException e) {
			System.out.println("Invalid query");
		}
	}

	private void chooser(String d) {
		System.out.println("You're connected to " + d);
		System.out.println(
				"1-Choose a table\n" + "2-Add new table\n" + "3-Add new Database");
		int n = sc.nextInt();
		switch (n) {
		case 1:
			if (tables.isEmpty()) {
				System.out.println("No tables");
				chooser(d);
				return;
			}
			tableChooser(d);
			return;
		case 2:
			createTable(d);
			return;
		case 3:
			main();
			return;
		default:
			System.out.println("Invalid");
			chooser(d);
			return;
		}

	}

	private void tableChooser(String d) {
		for (int i = 0; i < tables.size(); i++) {
			System.out.println(i + "-> " + tables.get(i));
		}
		int choosen = sc.nextInt();
		while (!(choosen >= 0 && choosen < tables.size())) {
			System.out.println("please choose a number from 0 to " + Integer.toString(tables.size() - 1));
			choosen = sc.nextInt();
		}
		System.out.println("Current Table is " + tables.get(choosen));
		tableOperations(tables.get(choosen), d);

	}

	private void tableOperations(String string, String d) {
		System.out.println("Table ->" + string);
		System.out.println("1-Select query\n" + "2-Insert query\n" + "3-Delete query\n" + "4- Update query\n"
				+ "5-Another table\n" + "6-New Database\n" + "7.New table");
		int n = sc.nextInt();
		switch (n) {
		case 1:
			select(string, d);
			return;
		case 2:
			insert(string, d);
			return;
		case 3:
			delete(string, d);
			return;
		case 4:
			update(string, d);
			return;
		case 5:
			tableChooser(d);
			return;
		case 6:
			main();
			return;
		case 7:
			createTable(d);
			return;
		default:
			System.out.println("Invalid");
			tableOperations(string, d);
			return;
		}

	}

	

	private void update(String string, String d) {
		queryScanner();
		try {
			if (query.toLowerCase().contains("update")) {
				int ins = db.executeUpdateQuery(query);
				System.out.println("One rows updated " + ins);
				tableOperations(string, d);
			} else {
				System.out.println("ivalid");
				update(string, d);
				return;
			}
			tableOperations(string, d);
			return;
		} catch (SQLException e) {
			System.out.println("Invalid query");
			update(string, d);
			return;
		}
	}

	private void delete(String string, String d) {
		queryScanner();
		try {
			if (query.toLowerCase().contains("delete")) {
				int ins = db.executeUpdateQuery(query);
				System.out.println("Number of rows deleted " + ins);
			} else {
				System.out.println("ivalid");
				delete(string, d);
				return;
			}
			tableOperations(string, d);
			return;
		} catch (SQLException e) {
			System.out.println("Invalid query");
			delete(string, d);
			return;
		}

	}

	private void insert(String string, String d) {
		queryScanner();
		try {
			if (query.toLowerCase().contains("insert")) {
				int ins = db.executeUpdateQuery(query);
				System.out.println("One row inserted successfuly " + ins);
			} else {
				System.out.println("invalid");
				insert(string, d);
				return;
			}
			tableOperations(string, d);
			return;
		} catch (SQLException e) {
			System.out.println("Invalid query");
			insert(string, d);
			return;
		}
	}

	private void queryScanner() {
		sc.nextLine();
		System.out.print("Enter your query");
		while (sc.hasNextLine()) {
			query = sc.nextLine();
			break;
		}
	}

	private void select(String string, String d) {
		queryScanner();
		try {
			if (query.toLowerCase().contains("select")) {
				Object[][] ob = db.executeQuery(query);
				printSelected(ob);
			} else {
				System.out.println("invalid");
				select(string, d);
				return;
			}
			tableOperations(string, d);
			return;

		} catch (SQLException e) {
			System.out.println("Invalid query");
			select(string, d);
			return;
		}

	}

	private void printSelected(Object[][] ob) {
		int size = 0 ;
		for (int i = 0; i < ob.length; i++) {
			System.out.println("row # " + (i + 1));
			for (int j = 0; j < ob[i].length; j++) {
				System.out.println(ob[i][j]);
				size++;

			}
		}
		if(size == 0) {
			System.out.println("No rows selected");
		}

	}

}
