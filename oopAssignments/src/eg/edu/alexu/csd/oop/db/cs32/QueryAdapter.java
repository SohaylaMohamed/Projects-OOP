package eg.edu.alexu.csd.oop.db.cs32;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryAdapter {
	/**
	 * instance of class
	 */
	private static QueryAdapter instance = null;
	/**
	 * condition field
	 */
	private String where;
	/**
	 * condition field value
	 */
	private String whereValue;
	/**
	 * condition operation (=,<,>)
	 */
	private char oper;
	/**
	 * selected fields
	 */
	private ArrayList<String> select;
	/**
	 * updated fields values
	 */
	private ArrayList<String> updateValue;
	/**
	 * create table fields
	 */
	private ArrayList<String> field;
	/**
	 * create table fields type
	 */
	private ArrayList<String> Type;

	/**
	 * choosen table
	 */
	private String table;
	/**
	 * choosen database
	 */
	private String database;
	/**
	 * pattern
	 */
	private Pattern p;
	/**
	 * pattern matcher
	 */
	private Matcher m;
	/**
	 * executing the query
	 */
	private ExecuteQuery ex;
	/**
	 * Values to insert
	 */
	private ArrayList<String> insertValue;
	/**
	 * type of fields to insert
	 */
	private ArrayList<Boolean> iNtype;
	/**
	 * some fields from all
	 */
	private ArrayList<FieldType> someFields;

	public static QueryAdapter getInstance() {
		if (instance == null) {
			instance = new QueryAdapter();
		}
		return instance;
	}

	private QueryAdapter() {
		ex = new ExecuteQuery();
		where = null;
		select = new ArrayList<>();
		field = new ArrayList<>();
		Type = new ArrayList<>();
		iNtype = new ArrayList<>();
		someFields = new ArrayList<>();
		updateValue = new ArrayList<>();
		insertValue = new ArrayList<>();
		table = null;
		database = null;


	}

	/**
	 * @param query
	 *            sent query
	 * @return true if the creation happened
	 * @throws SQLException 
	 */
	public boolean createQuery(String query) throws SQLException {
		if (query == null) {
			throw new SQLException();
		}
		p = Pattern.compile("^(CREATE\\s*DATABASE)\\s+(\\w+)\\s*;?$", Pattern.CASE_INSENSITIVE);
		m = p.matcher(query.trim());
		if (m.matches()) {
			database = m.group(2).trim();
			return ex.createDatabase(database.toLowerCase());
		} else {

			p = Pattern.compile(
					"^(CREAte\\s+TABLE)\\s+(\\w+)\\s*\\(("
							+ "((\\s*[a-zA-Z0-9_\\s.]+\\s+(int|varchar\\s*(\\(\\d+\\s*\\))?\\s*))\\s*,)*"
							+ "\\s*[a-zA-Z0-9_\\s.]+\\s+(int|varchar\\s*(\\(\\d+\\s*\\))?)\\s*)\\)" + "\\s*;?$",
					Pattern.CASE_INSENSITIVE);
			m = p.matcher(query.trim());
			if (m.matches()) {
				table = m.group(2).trim();
				// get the fields and their type(varchar, int)
				getTypeField(m.group(3));
				return ex.createTable(table.toLowerCase(), field, Type);

			}
		}
		throw new SQLException(query);
	}

	/**
	 * @param group
	 *            gets fields and type of fields for create table
	 */
	private void getTypeField(String group) {

		p = Pattern.compile("^\\s*(\\w+)\\s+(int|varchar\\s*(\\(\\d+\\s*\\))?)\\s*$",
				Pattern.CASE_INSENSITIVE);
		field.clear();
		Type.clear();
		String[] str = group.split(",");
		for (String s1 : str) {
			Matcher m2 = p.matcher(s1);
			if (m2.matches()) {
				field.add(m2.group(1).trim());
				Type.add(m2.group(2).replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("255", "").trim());
			}
		}

	}

	/**
	 * @param query
	 *            sent query
	 * @return true if dropped
	 * @throws SQLException 
	 */
	public boolean dropQuery(String query) throws SQLException {
		if (query == null) {
			throw new SQLException();
		}
		p = Pattern.compile("^(DROP\\s*DATABASE|drop\\s*database)\\s+(\\w+)\\s*;?$", Pattern.CASE_INSENSITIVE);
		m = p.matcher(query.trim());

		if (m.matches()) {
			database = m.group(2).trim();
			if (ex.checkDatabaseExist(database.toLowerCase())) {
				return ex.dropDataBase(database.toLowerCase());
			} else {
				return false;
			}
		} else {
			p = Pattern.compile("^(DROP\\s*TABLE|drop\\s*table)\\s+(\\w+)\\s*;?$", Pattern.CASE_INSENSITIVE);
			m = p.matcher(query.trim());

			if (m.matches()) {
				table = m.group(2).trim();
				if (ex.checkTableExist(table.toLowerCase())) {
					return ex.dropTable(table.toLowerCase());
				} else {
					return false;
				}
			}
		}
		throw new SQLException(query);
	}

	/**
	 * @param query
	 *            sent query
	 * @return list of selected
	 * @throws SQLException 
	 */
	public List<List<Object>> selectQuery(String query) throws SQLException {
		if (query == null) {
			throw new SQLException();
		}
		table = "";
		select.clear();
		where = "";
		whereValue = "";
		p = Pattern.compile("^(SELECT|select)\\s\\*\\s(FROM|from)\\s(\\w+)\\s*;?$", Pattern.CASE_INSENSITIVE);
		m = p.matcher(query.trim());
		if (m.matches()) {
			if (ex.checkTableExist(m.group(3).trim().toLowerCase())) {
				return ex.selectAll(m.group(3).trim().toLowerCase());
			}
		} else {
			p = Pattern.compile(
					"^(SELECT|select)\\s+((\\s*[a-zA-Z0-9_\\s]+\\s*\\,)*\\s*[a-zA-Z0-9_\\s]+)\\s+(FROM|from)(\\s+(\\w+))\\s*;?$",
					Pattern.CASE_INSENSITIVE);
			m = p.matcher(query.trim());
			if (m.matches()) {
				table = m.group(5).trim();
				if (ex.checkTableExist(table.toLowerCase())) {
					getFields(m.group(2));
					if (fieldsExist(table.toLowerCase(), select))
						return ex.selectDistinct(table.toLowerCase(), select);
				}
			} else {
				p = Pattern.compile(
						"^(SELECT|select)\\s+" + "((\\s*[a-zA-Z0-9_\\s]+\\s*\\,)*\\s*[a-zA-Z0-9_\\s]+)\\s+"
								+ "(FROM|from)\\s+(\\w+)\\s+"
								+ "(WHERE|where)\\s+((\\w+)\\s*(=|>|<)\\s*(('(\\w+\\s*.*)+')|(\\d+)))\\s*;?$",
						Pattern.CASE_INSENSITIVE);
				m = p.matcher(query.trim());
				if (m.matches()) {
					table = m.group(5).trim();
					if (ex.checkTableExist(table.toLowerCase())) {
						getFields(m.group(2));
						if (fieldsExist(table.toLowerCase(), select)) {
							if (getCondition(m.group(7)))
								return ex.selectDisWithCondition(table.toLowerCase(), select, where, whereValue, oper);
						}
					}
				} else {
					p = Pattern.compile(
							"^(SELECT|select)\\s\\*\\s(FROM|from)\\s(\\w+)\\s+(WHERE|where)\\s+((\\w+)\\s*(=|>|<)\\s*(('(\\w+\\s*.*)+')|(\\d+)))\\s*;?$",
							Pattern.CASE_INSENSITIVE);
					m = p.matcher(query.trim());
					if (m.matches()) {
						table = m.group(3).replaceAll("\\s+", "");
						if (ex.checkTableExist(table.toLowerCase())) {
							if (getCondition(m.group(5))) {
								return  ex.selectAllWithCondition(table.toLowerCase(), where, oper, whereValue);
							}
						}
					}
				}
			}
		}
		throw new SQLException(query);
	}

	/**
	 * @param f
	 *            sent fields
	 */
	private void getFields(String f) {
		String[] t = f.split(",");
		select.clear();

		p = Pattern.compile("\\s*(\\w+\\s*)*\\s*");
		for (String x : t) {
			Matcher m2 = p.matcher(x);
			if (m2.matches())
				select.add(x.trim());
		}

	}

	/**
	 * @param group
	 *            condition
	 * @return true if condition is valid
	 */
	private boolean getCondition(String group) {
		String[] temp = null;
		if (group.contains("=")) {
			temp = group.split("=");
			oper = '=';
		} else if (group.contains(">")) {
			temp = group.split(">");
			oper = '>';

		} else if (group.contains("<")) {
			temp = group.split("<");
			oper = '<';

		}
		where = temp[0].trim();
		if (!ex.ifExist(where, table.toLowerCase())) {
			return false;
		}
		if (!temp[1].contains("'")) {
			if (!ex.checkInteger(where, table)) {
				return false;
			}
		} else {
			if (oper != '=') {
				return false;
			}
			if (!ex.checkString(where.replaceAll("'", ""), table.toLowerCase())) {
				return false;
			}
		}
		whereValue = temp[1].replaceAll("'", "").trim();
		return true;

	}

	/**
	 * @param query
	 * @return
	 * @throws SQLException 
	 */
	public int insertQuery(String query) throws SQLException {
		if (query == null) {
			throw new SQLException();
		}
		p = Pattern.compile("^(INSERT INTO|insert into)\\s+" + "(\\w+)\\s+" + "(VALUES|values)\\s*"
				+ "\\(((\\s*(('[a-zA-Z0-9_\\s_.]+')|(\\d+))\\s*\\,)*\\s*(('[a-zA-Z0-9_\\s]+')|(\\d+)))\\)" + "\\s*;?$",
				Pattern.CASE_INSENSITIVE);
		m = p.matcher(query.trim());
		if (m.matches()) {
			table = m.group(2).trim();
			if (ex.checkTableExist(table.toLowerCase())) {
				getInsertValues(m.group(4));
				if (checkAllFieldsType(table.toLowerCase())) {
					return ex.insertInAll(table.toLowerCase(), insertValue);
				}
			}
		} else {
			p = Pattern.compile(
					"^(INSERT INTO|insert into)\\s+" + "(\\w+)\\s*" + "\\(((\\s*(\\w+)\\s*\\,)*\\s*(\\w+))\\s*\\)\\s*"
							+ "(VALUES|values)\\s*"
							+ "\\(((\\s*(('[a-zA-Z0-9_\\s.]+')|(\\d+))\\s*\\,)*\\s*(('[a-zA-Z0-9_\\s.]+')|(\\d+)))\\)\\s*;?$",
					Pattern.CASE_INSENSITIVE);

			m = p.matcher(query.trim());
			if (m.matches()) {
				table = m.group(2).trim();

				if (ex.checkTableExist(table.toLowerCase())) {
					getInsertValues(m.group(8));
					getFields(m.group(3));
					if (checkSomeFieldsType(select, table.toLowerCase())) {
						return ex.insertInDist(table.toLowerCase(), someFields, insertValue);
					}

				}
			}
		}
		throw new SQLException(query);
	}

	/**
	 * @param select2 selected fields from query
	 * @param table2 table
	 * @return 
	 */
	private boolean checkSomeFieldsType(ArrayList<String> select2, String table2) {
		if (!fieldsExist(table2.toLowerCase(), select2)) {
			return false;
		}
		for (int i = 0; i < iNtype.size(); i++) {
			if (iNtype.get(i)) {
				if (!someFields.get(i).isInteger()) {
					return false;
				}
			} else {
				if (!someFields.get(i).isString()) {
					return false;
				}
			}

		}
		return true;
	}

	private boolean fieldsExist(String table2, ArrayList<String> select2) {
		ArrayList<FieldType> fields = ex.getFields(table2.toLowerCase());
		someFields.clear();
		ArrayList<Boolean> type = new ArrayList<>();
		ArrayList<String> insertV = new ArrayList<>();
		for (FieldType ft : fields) {
			for (String f : select2) {
				if (f.equalsIgnoreCase(ft.getName())) {
					someFields.add(ft);
					type.add(iNtype.get(select2.indexOf(f)));
					insertV.add(insertValue.get(select2.indexOf(f)));

				}
			}
		}
		if (someFields.size() != select2.size()) {
			return false;
		}
		insertValue.clear();
		iNtype.clear();
		insertValue = new ArrayList<String>(insertV);
		iNtype = new ArrayList<Boolean>(type);
		return true;
	}

	private boolean checkAllFieldsType(String table) {
		ArrayList<FieldType> fields = ex.getFields(table.toLowerCase());
		for (int i = 0; i < iNtype.size(); i++) {
			if (iNtype.get(i)) {
				if (!fields.get(i).isInteger()) {
					return false;
				}
			} else {
				if (!fields.get(i).isString()) {
					return false;
				}
			}

		}
		return true;

	}

	private void getInsertValues(String group) {
		String[] t = group.split(",");
		insertValue.clear();
		iNtype.clear();
		for (String x : t) {
			if (x.contains("'")) {
				insertValue.add(x.replaceAll("'", "").trim());
				iNtype.add(false);
			} else {
				insertValue.add(x.trim());
				iNtype.add(true);
			}

		}

	}

	public int deleteQuery(String query) throws SQLException {
		if (query == null) {
			throw new SQLException();
		}
		p = Pattern.compile(
				"^(DELETE\\s+FROM)\\s+(\\w+)\\s+(WHERE|where)\\s+((\\w+)\\s*(=|>|<)\\s*(('(\\w+\\s*.*)+')|(\\d+)))\\s*;?$",
				Pattern.CASE_INSENSITIVE);
		m = p.matcher(query.trim());
		if (m.matches()) {
			table = m.group(2).trim();
			if (ex.checkTableExist(table.toLowerCase())) {
				if (getCondition(m.group(4))) {
					return ex.deleteFromTableWithCondition(table.toLowerCase(), oper, where, whereValue);
				}
			}
		} else {
			p = Pattern.compile("^(DELETE(\\*)?\\s+FROM|)\\s+(\\w+)\\s*$", Pattern.CASE_INSENSITIVE);
			m = p.matcher(query.trim());
			if (m.matches()) {
				table = m.group(3).trim();
				if (ex.checkTableExist(table.toLowerCase())) {
					return ex.deleteFromTable(table.toLowerCase());
				}
			}
		}
		throw new SQLException(query);
	}

	public int updateQuery(String query) throws SQLException {
		if (query == null) {
			throw new SQLException();
		}
		p = Pattern.compile(
				"^(UPDATE)\\s+(\\w+)\\s+(SET)\\s+((\\s*(\\w+)\\s*=\\s*(('[a-zA-Z0-9_\\s.]+')|(\\d+))\\s*\\,)*(\\s*(\\w+)\\s*=\\s*(('[a-zA-Z0-9_\\s.]+')|(\\d+))))\\s*;?$",
				Pattern.CASE_INSENSITIVE);
		m = p.matcher(query.trim());

		if (m.matches()) {
			table = m.group(2).trim();
			if (ex.checkTableExist(table.toLowerCase())) {
				if (getUpdate(m.group(4))) {
					return ex.updateTable(table.toLowerCase(), updateValue, someFields);
				}
			}
		} else {
			p = Pattern.compile(
					"^(UPDATE|update)\\s+(\\w+)\\s+(SET)\\s+((\\s*(\\w+)\\s*=\\s*(('[a-zA-Z0-9_\\s.]+')|(\\d+))\\s*\\,)*(\\s*(\\w+)\\s*=\\s*(('[a-zA-Z0-9_\\s.]+')|(\\d+))))\\s+(WHERE|where)\\s+((\\w+)\\s*(=|>|<)\\s*(('[a-zA-Z0-9_\\s.]+')|(\\d+)))\\s*;?$");
			m = p.matcher(query.trim());

			if (m.matches()) {
				table = m.group(2).trim();
				if (ex.checkTableExist(table.toLowerCase())) {

					if (getUpdate(m.group(4))) {
						if(getCondition(m.group(16)))
						return ex.updateTableWithCondition(table.toLowerCase(), updateValue, someFields, where, whereValue, oper);
					}
				}
			}
		}
		throw new SQLException(query);
	}

	private boolean getUpdate(String group) {
		String[] t = group.split(",");
		ArrayList<String> f = new ArrayList<>();
		ArrayList<String> v = new ArrayList<>();
		updateValue.clear();

		for (String x : t) {
			String[] temp = x.split("=");
			f.add(temp[0].trim());
			v.add(temp[1].trim());
		}
		if (!fieldsExist(table.toLowerCase(), f)) {
			return false;
		}
		if (f.size() != v.size()) {
			return false;
		}
		for (FieldType ft : someFields) {
			for (String ft1 : f) {
				if (ft1.equalsIgnoreCase(ft.getName())) {
					updateValue.add(v.get(f.indexOf(ft1)));
				}
			}
		}

		for (int i = 0; i < updateValue.size(); i++) {
			if (!updateValue.get(i).contains("'")) {
				if (!someFields.get(i).isInteger()) {
					return false;
				}
			} else {
				if (!someFields.get(i).isString()) {
					return false;
				}
				updateValue.set(i, updateValue.get(i).replaceAll("'", ""));
			}

		}

		return true;

	}

}
