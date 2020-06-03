package eg.edu.alexu.csd.oop.calculator.cs32;

public class main {

	public static void main(String[] args) {
		CalculatorOop test = new CalculatorOop();
		test.input("6+7");
		test.input("21.5*3");
		test.input("12/5");
		test.input("-1+6");
		test.save();
		String st = test.getResult();
		System.out.println(st);
		test.input("3+2");
		test.input("-1+6");
		test.input("21.5*6");
		test.input("25/5");
		 st = test.getResult();
			System.out.println(st);
		test.load();
		st = test.getResult();
		System.out.println(test.prev());
		System.out.println(test.prev());
		System.out.println(test.next());
		System.out.println(test.next());
		System.out.println(test.next());

		System.out.println(test.getResult());
		
		
		

		

	}

}
