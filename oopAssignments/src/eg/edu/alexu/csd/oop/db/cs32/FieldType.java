package eg.edu.alexu.csd.oop.db.cs32;

public class FieldType {

	private boolean integer;
	private boolean string;
	private String name;
	

	public FieldType(boolean isInteger, boolean isString, String Name) {
		this.integer = isInteger;
		this.string = isString;
		this.name = Name;
	}

	public boolean isInteger() {
		return this.integer;
	}

	public boolean isString() {
		return this.string;
	}

	public String getName() {
		return this.name;
	}

}
