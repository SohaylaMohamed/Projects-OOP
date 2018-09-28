package eg.edu.alexu.csd.oop.calculator.cs32;

import eg.edu.alexu.csd.oop.calculator.Calculator;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


/**
 * @author Sohayla Mohamed
 *
 */
public class CalculatorOop implements Calculator {

    /**.
     * data structure to hold the current inputs
     */
    private ArrayList<String> formulas = new ArrayList<>();

    /**.
     * index to the current
     */
    private int pointer = -1;
    /**.
     * max number of formulas in memory
     */
    private static final int MAX_SIZE = 5;

    /**.
     * @param s : input string
	 * Take input from user
	 */
	public final void input(final String s) {
		if (s == null) {
			throw null;
		}
		if (pointer == -1) {
			pointer = 0;
			formulas.add(s);
		} else {
			if (formulas.size() == MAX_SIZE) {
				formulas.remove(0);
			}
			formulas.add(s);
			pointer = formulas.size() - 1;
		}
	}

	/**.
	 * @return the result of the current
	 *  operations or throws a runtime exception
	 */
	public final String getResult() {
		String temp = formulas.get(pointer);
		if (temp == null) {
			return null;
		}
		double num1, num2;
		int i = 0;
		char op;
		char tmp = temp.charAt(i);
		StringBuilder str = new StringBuilder();
		str.append(tmp);
		i++;
		tmp = temp.charAt(i);
		while (tmp != '+' && tmp != '*' && tmp != '-' && tmp != '/') {
			str.append(tmp);
			i++;
			tmp = temp.charAt(i);
		}
		op = tmp;
		i++;
		tmp = temp.charAt(i);
		num1 = Double.parseDouble(str.toString());
		str = new StringBuilder();
		while (i < temp.length()) {
			str.append(tmp);
			i++;
			if (i == temp.length()) {
				break;
			}
			tmp = temp.charAt(i);
		}
		i++;
		num2 = Double.parseDouble(str.toString());
		double result = evaluate(num1, num2, op);
		String s = Double.toString(result);
		return s;
	}
	/**.
	 * @param num1 : first operand
	 * @param num2 : second operand
	 * @param op : operation
	 * @return evaluation
	 */
	private double evaluate(final double num1,
			final double num2, final char op) {
		switch (op) {
		case '+' : return num1 + num2;
		case '-' : return num1 - num2;
		case'*' : return num1 * num2;
		case '/' : return num1 / num2;
		default : throw null;
		}
	}

	/**.
	 * @return the current formula
	 */
	public final String current() {
		if (pointer == -1) {
			return null;
		}
		return formulas.get(pointer);
	}
	/**.
	 * @return the last operation in String format,
	 *  or Null if no more history
	 * available
	 */
	public final String prev() {
		// TODO Auto-generated method stub
		if (pointer > 0 && pointer < formulas.size()) {
			pointer--;
			return formulas.get(pointer);
		}
		return null;
	}

	/**.
	 * @return the next operation in String format,
	 *  or Null if no more history
	 * available
	 */
	public final String next() {
		// TODO Auto-generated method stub
		if (pointer >= 0 && pointer < formulas.size() - 1) {
			pointer++;
			return formulas.get(pointer);
		}
		return null;
	}

	/**.
	 * Save in file the last 5 done Operations
	 */
	public final void save() {
		clearTheFile();
		if (pointer == -1) {
			return;
		}
		try {
			PrintWriter out = new PrintWriter("formulas.txt");
			out.println(Integer.toString(pointer));
			for (int i = 0; i < formulas.size(); i++) {
				out.println(formulas.get(i));
				}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**.
	 * Load from file the saved operations
	 */
	public final void load() {
		formulas.clear();
	    try {
			BufferedReader br =
			new BufferedReader(new FileReader("formulas.txt"));
			 String line;
			try {
				line = br.readLine();
				if (line != null) {
					pointer = Integer.parseInt(line);
				} else {
					pointer = -1;
					return;
				}
				line = br.readLine();
			    while (line != null) {
		            formulas.add(line);
		            line = br.readLine();
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
		    br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**.
	 * clear data from file
	 */
	private void clearTheFile() {
        FileWriter fwOb = null;
		try {
			fwOb = new FileWriter("formulas.txt", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        try {
			fwOb.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
