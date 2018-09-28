package eg.edu.alexu.csd.oop.calculator.cs32;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

import eg.edu.alexu.csd.oop.calculator.Calculator;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

/**.
 * @author sohayla Mohamed
 *
 */
final class GUI {

	/**.
	 *
	 */
	private JFrame frame;
	/**.
	 *
	 */
	private JTextField textField;
	/**.
	 *
	 */
	private Calculator calc = new CalculatorOop();

	/**.
	 * @param args : to run the program
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	private GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		  final int sOMENUMBER = 100;
		  final int sOMENUMBER1 = 450;
		  final int sOMENUMBER2 = 300;
		  final int sOMENUMBER3 = 66;
		  final int sOMENUMBER4 = 53;
		  final int sOMENUMBER5 = 102;
		  final int sOMENUMBER6 = 14;
		  final int sOMENUMBER7 = 89;
		  final int sOMENUMBER8 = 122;
		  final int sOMENUMBER9 = 115;
		  final int sOMENUMBER10 = 240;
		  final int sOMENUMBER11 = 14;
		  final int sOMENUMBER12 = 87;
		  final int sOMENUMBER14 = 140;
		  final int sOMENUMBER15 = 390;
		  final int sOMENUMBER16 = 27;
		  final int sOMENUMBER17 = 23;
		  final int sOMENUMBER18 = 11;
		  final int sOMENUMBER19 = 45;
		  final int sOMENUMBER20 = 31;
		  final int sOMENUMBER21 = 345;
		  final int sOMENUMBER23 = 46;
		  final int sOMENUMBER24 = 273;
		  final int sOMENUMBER25 = 83;
		  final int sOMENUMBER26 = 18;
		  final int sOMENUMBER27 = 370;
		  final int sOMENUMBER28 = 10;
		frame = new JFrame();
		frame.setBounds(sOMENUMBER, sOMENUMBER,
				sOMENUMBER1, sOMENUMBER2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JLabel lblNewLabel = new JLabel("Formula");
		lblNewLabel.setBounds(sOMENUMBER3, sOMENUMBER4,
				sOMENUMBER23, sOMENUMBER6);
		frame.getContentPane().add(lblNewLabel);
		final JLabel label = new JLabel(" ");
		label.setBounds(sOMENUMBER8, sOMENUMBER9,
				sOMENUMBER10, sOMENUMBER6);
		frame.getContentPane().add(label);
		JLabel lblNewLabel2 = new JLabel("=");
		lblNewLabel2.setBounds(sOMENUMBER3, sOMENUMBER12,
				sOMENUMBER23, sOMENUMBER6);
		frame.getContentPane().add(lblNewLabel2);
		final JLabel lblNewLabel3 = new JLabel("Answer");
		lblNewLabel3.setBounds(sOMENUMBER8, sOMENUMBER12,
				sOMENUMBER9, sOMENUMBER11);
		frame.getContentPane().add(lblNewLabel3);

		JButton btnNewButton = new JButton(">>");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD,
				sOMENUMBER18));
		btnNewButton.setMargin(new java.awt.Insets(1, 1, 1, 1));

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				String next = calc.next();
				if (!(next == null)) {
					textField.setText(next);
				}
			}
		});
		btnNewButton.setBounds(sOMENUMBER15, sOMENUMBER14,
				sOMENUMBER16, sOMENUMBER17);
		frame.getContentPane().add(btnNewButton);

		JButton btnNewButton1 = new JButton("<<");
		btnNewButton1.setFont(new Font("Tahoma", Font.BOLD,
				sOMENUMBER18));
		btnNewButton1.setMargin(new java.awt.Insets(1, 1, 1, 1));

		btnNewButton1.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				String prev = calc.prev();
				if (!(prev == null)) {
					textField.setText(prev);
				}
			}
		});

		textField = new JTextField();
		textField.setBounds(sOMENUMBER8, sOMENUMBER19,
				sOMENUMBER10, sOMENUMBER20);
		frame.getContentPane().add(textField);
		textField.setColumns(sOMENUMBER28);
		btnNewButton1.setBounds(sOMENUMBER21, sOMENUMBER14,
				sOMENUMBER16, sOMENUMBER17);
		frame.getContentPane().add(btnNewButton1);

		JButton btnNewButton2 = new JButton("Load");
		btnNewButton2.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				calc.load();
				textField.setText(calc.current());
			}
		});
		btnNewButton2.setBounds(sOMENUMBER11, sOMENUMBER14,
				sOMENUMBER7, sOMENUMBER17);
		frame.getContentPane().add(btnNewButton2);

		JButton btnNewButton3 = new JButton("save");
		btnNewButton3.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				calc.save();
			}
		});
		btnNewButton3.setBounds(sOMENUMBER5, sOMENUMBER14,
				sOMENUMBER7, sOMENUMBER17);
		frame.getContentPane().add(btnNewButton3);

		JButton btnNewButton4 = new JButton("Calculate");
		btnNewButton4.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				String result = textField.getText();
			if (result.equals(null) || result.equals("")) {
				label.setText("Please enter a valid formula.");
					label.setForeground(Color.RED);
				} else {
					label.setText(" ");
					label.setForeground(Color.WHITE);
					if (!result.equals(calc.current())) {
						calc.input(result);
					}
					result = calc.getResult();
					lblNewLabel3.setText(result);
					textField.setText(null);
					textField.requestFocusInWindow();
				}
			}
		});
		btnNewButton4.setBounds(sOMENUMBER24, sOMENUMBER25,
				sOMENUMBER7, sOMENUMBER17);
		frame.getContentPane().add(btnNewButton4);

		JButton btnNewButton5 = new JButton(".");
		btnNewButton5.setFont(new Font("Tahoma", Font.BOLD,
				sOMENUMBER26));
		btnNewButton5.setMargin(new java.awt.Insets(1, 1, 1, 1));

		btnNewButton5.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				textField.setText(calc.current());
			}
		});
		btnNewButton5.setBounds(sOMENUMBER27, sOMENUMBER14,
				sOMENUMBER16, sOMENUMBER17);
		frame.getContentPane().add(btnNewButton5);
	}
}
