package eg.edu.alexu.csd.oop.draw.cs32;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import eg.edu.alexu.csd.oop.draw.*;

public class PaintView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Shape currentShape;
	private Shape newShape;
	private Color stroke;
	private Color fill;
	private HashMap<String, Integer> actions;
	private int action;
	PaintController controller = PaintController.getInstance();
	private int currentAction = 0;
	ArrayList<JButton> buttons = new ArrayList<JButton>();
	JButton circle, line, square, ellipse, rec, triangle, strokeC, fillC, undo, redo, select, save, load, delete,
			resize, loadC, newPage, copy;
	JPanel toolsP;
	Box tools;
	Box loaded; // vertical box
	JPanel loadedClass;

	public static void main(String[] arg0) {
		new PaintView();

	}

	public PaintView() {

		actions = new HashMap<>();
		action = 10;
		stroke = new Color(0, 0, 0);
		fill = new Color(255, 255, 255);
		this.setSize(1500, 700);
		this.setTitle("Paint");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		toolsP = new JPanel();
		loadedClass = new JPanel();
		loaded = Box.createVerticalBox(); // vertical

		tools = Box.createHorizontalBox();
		save = makeSaveButton("save.png");
		load = makeLoadButton("open.png");
		newPage = makeNewDoc("newD.png");

		undo = makeUndoButton("undo.png");
		redo = makeRedoButton("redo.png");

		strokeC = makeColorButtons("stroke.png", 2, true, "stroke");
		fillC = makeColorButtons("fillColor.png", 3, false, "fill");

		select = makeButtons("select.png", 1, "select");
		delete = makeDeleteButtons("delete.png");
		resize = makeResizeButtons("resize.png");
		
		copy = makeCopyButton("copy.png");
		line = makeButtons("line.png", 4, "LineSegment");
		ellipse = makeButtons("ellipse.png", 6, "Ellipse");
		circle = makeButtons("circle.png", 5, "Circle");
		rec = makeButtons("rectangle.png", 7, "Rectangle");
		square = makeButtons("square.png", 8, "Square");
		triangle = makeButtons("triangle.png", 9, "Triangle");

		loadC = makeClassLoaderButton("plus.png", 15);
		loadedClass.add(loaded);
		toolsP.add(tools);
		this.add(toolsP, BorderLayout.NORTH);
		this.add(loadedClass, BorderLayout.WEST);

		toolsP.setBackground(Color.lightGray);
		controller.setActions(actions);
		this.add(new DrawingBoard(), BorderLayout.CENTER);
		this.setVisible(true);

	}

	private JButton makeCopyButton(String iconPath) {
		JButton btn = new JButton();
		Icon icon = new ImageIcon(this.getClass().getResource(iconPath));
		btn.setIcon(icon);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if(currentShape != null){
					try {
						newShape = (Shape) currentShape.clone();
						controller.addShape(newShape);
						currentAction = 1;
					} catch (CloneNotSupportedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					currentAction = 1;
				}
				
			}
		});
		tools.add(btn);
		return btn;
	}

	private JButton makeNewDoc(String iconPath) {
		JButton btn = new JButton();
		Icon icon = new ImageIcon(this.getClass().getResource(iconPath));
		btn.setIcon(icon);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.clearAll();
				currentAction = 1;
				currentShape = null;
				newShape = null;
				loaded.removeAll();
				loaded.add(loadC);
				repaint();
			}
		});
		tools.add(btn);
		return btn;
	}

	private JButton makeDeleteButtons(String iconPath) {
		JButton btn = new JButton();
		Icon icon = new ImageIcon(this.getClass().getResource(iconPath));
		btn.setIcon(icon);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentShape != null) {
					controller.remove(currentShape);
					currentShape = null;
					repaint();

				} else {
					currentAction = 1;
				}
				repaint();
			}
		});
		tools.add(btn);
		return btn;
	}

	private JButton makeResizeButtons(String iconPath) {
		JButton btn = new JButton();
		Icon icon = new ImageIcon(this.getClass().getResource(iconPath));
		btn.setIcon(icon);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentShape != null) {
					JPanel p = new JPanel();
					try {
						newShape = (Shape) currentShape.clone();
					} catch (CloneNotSupportedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					JOptionPane pane = new JOptionPane();
					List<JTextField> options = new LinkedList<>();
					List<JLabel> o = new ArrayList<>();
					Set<String> keys = new HashSet<>();
					keys = currentShape.getProperties().keySet();
					int i = 0;
					Point point = currentShape.getPosition();
					p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
					o.add(new JLabel("PositionX"));
					options.add(new JTextField(Integer.toString(point.x)));
					i++;
					o.add(new JLabel("PositionY"));
					options.add(new JTextField(Integer.toString(point.y)));
					i++;
					p.add(o.get(0));
					p.add(options.get(0));
					p.add(o.get(1));
					p.add(options.get(1));
					for (String s : keys) {
						o.add(new JLabel(s));
						options.add(new JTextField(currentShape.getProperties().get(s).toString()));
						p.add(o.get(i));
						p.add(options.get(i));
						i++;
					}

					@SuppressWarnings("static-access")
					int n = pane.showConfirmDialog(null, p, "Enter new Coordinates", JOptionPane.OK_CANCEL_OPTION);
					if (n == JOptionPane.OK_OPTION) {
						Point position = new Point(Integer.parseInt(options.get(0).getText()),
								Integer.parseInt(options.get(1).getText()));
						HashMap<String, Double> m = new HashMap<>();
						for (int l = 2; l < options.size(); l++) {
							String key = o.get(l).getText();
							m.put(key, Double.valueOf(options.get(l).getText()));
						}
						newShape.setPosition(position);
						newShape.setProperties(m);
						controller.updateShape(currentShape, newShape);
						currentShape = null;
					}

				} else {
					currentAction = actions.get("select");
				}
				repaint();
			}
		});
		tools.add(btn);
		return btn;
	}

	/**
	 * Open file
	 * 
	 * @param iconPath
	 * @param actionNum
	 * @return
	 */
	private JButton makeLoadButton(String iconPath) {
		JButton btn = new JButton();
		Icon icon = new ImageIcon(this.getClass().getResource(iconPath));
		btn.setIcon(icon);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XML & JSON Files", "xml", "json");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					controller.openFile(chooser.getSelectedFile().getPath());
					repaint();
				}

			}
		});
		tools.add(btn);

		return btn;
	}

	/**
	 * save file
	 * 
	 * @param iconPath
	 * @param actionNum
	 * @return
	 */
	private JButton makeSaveButton(String iconPath) {
		JButton btn = new JButton();
		Icon icon = new ImageIcon(this.getClass().getResource(iconPath));
		btn.setIcon(icon);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XML & JSON Files", "xml", "json");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					controller.saveFile(chooser.getSelectedFile().getPath());

				}

			}

		});
		tools.add(btn);

		return btn;
	}

	/**
	 * Color buttons
	 * 
	 * @param iconPath
	 * @param actionNum
	 * @param s
	 * @param key
	 * @return
	 */
	private JButton makeColorButtons(String iconPath, int actionNum, boolean s, String key) {
		JButton btn = new JButton();
		Icon icon = new ImageIcon(this.getClass().getResource(iconPath));
		btn.setIcon(icon);
		actions.put(key, actionNum);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JColorChooser colorChooser = new JColorChooser();
				AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
				for (AbstractColorChooserPanel col : panels) {
					if (col.getDisplayName().equals("HSV")) {
						JOptionPane.showMessageDialog(new JFrame(), col, "Choose Color", JOptionPane.OK_OPTION);
					}
				}
				if (s) {
					stroke = colorChooser.getColor();
					currentAction = actions.get(key);

				} else {
					fill = colorChooser.getColor();
					currentAction = actions.get(key);

				}
			}
		});
		tools.add(btn);

		return btn;
	}

	/**
	 * Buttons
	 * 
	 * @param iconPath
	 * @param actionNum
	 * @param string
	 * @return
	 */
	private JButton makeButtons(String iconPath, int actionNum, String key) {
		JButton btn = new JButton();
		Icon icon = new ImageIcon(this.getClass().getResource(iconPath));
		btn.setIcon(icon);
		actions.put(key, actionNum);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentAction = actionNum;
			}
		});
		tools.add(btn);

		return btn;
	}

	/**
	 * redo button
	 * 
	 * @param iconPath
	 * @param actionNum
	 * @return
	 */
	public JButton makeRedoButton(String iconPath) {
		JButton btn = new JButton();
		Icon icon = new ImageIcon(this.getClass().getResource(iconPath));
		btn.setIcon(icon);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (controller.canRedo()) {
					controller.redo();
					repaint();
				}
			}
		});
		tools.add(btn);

		return btn;
	}

	/**
	 * undo button
	 * 
	 * @param iconPath
	 * @param actionNum
	 * @return
	 */
	public JButton makeUndoButton(String iconPath) {
		JButton btn = new JButton();
		Icon icon = new ImageIcon(this.getClass().getResource(iconPath));
		btn.setIcon(icon);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (controller.canUndo()) {
					controller.undo();
					repaint();
				}
			}
		});
		tools.add(btn);

		return btn;
	}

	/**
	 * dynamic loading
	 * 
	 * @param iconPath
	 * @param actionNum
	 * @return
	 */
	private JButton makeClassLoaderButton(String iconPath, int actionNum) {
		JButton btn = new JButton();
		Icon icon = new ImageIcon(this.getClass().getResource(iconPath));
		btn.setIcon(icon);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JAR Files", "jar");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					ArrayList<Class<?>> classes = controller.loadClass(chooser.getSelectedFile().getPath());
					for (Class<?> c : classes) {
						JButton btn1 = new JButton(c.getSimpleName());
						actions.put(c.getSimpleName(), action);
						btn1.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								currentAction = action;
								Class<?> cs;
								try {
									cs = controller.getClass(btn1.getText());
									currentShape = (Shape) cs.newInstance();
									
				
								} catch (SecurityException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (InstantiationException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IllegalAccessException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IllegalArgumentException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						});
						loaded.add(btn1);
						loadedClass.revalidate();
						action++;
					}
				}

			}

		});
		loaded.add(btn);
		return btn;
	}
	/*
	 * private JButton makeNewLoadedShapeButton(int action, Class<?> c) {
	 * JButton btn = new JButton(); JFileChooser chooser = new JFileChooser();
	 * FileNameExtensionFilter filter = new
	 * FileNameExtensionFilter("JAR Files","jar");
	 * chooser.setFileFilter(filter); int returnVal =
	 * chooser.showOpenDialog(btn); if (returnVal ==
	 * JFileChooser.APPROVE_OPTION) { ArrayList<Class<?>> classes =
	 * controller.loadClass(chooser.getSelectedFile().getPath()); for(Class<?> c
	 * : classes){ JButton btn1 = new JButton();
	 * btn1.setText(c.getSimpleName()); btn.addActionListener(new
	 * ActionListener() { public void actionPerformed(ActionEvent e) {
	 * currentAction = action; } }); loaded.add(btn1); action++; } } return btn;
	 * }
	 */

	/**
	 * Drawing Area
	 * 
	 * @author Sohayla Mohamed
	 *
	 */
	private class DrawingBoard extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Point drawStart, drawEnd, dragP;
		Point ext1, ext2;
		boolean dragging, canDrag;
		boolean newS;
		double a1, a2, l1, l2;
		int dragX, dragY;
		// java.awt.geom.Rectangle2D sel ;
		/**
		 * contoller between veiw and model
		 */
		PaintController controller = PaintController.getInstance();

		/**
		 * constructor
		 */
		public DrawingBoard() {
			this.setBackground(new Color(255, 255, 255));
			// Mouth listener
			this.addMouseListener(new MouseAdapter() {
				// on press the drawing area
				public void mousePressed(MouseEvent e) {
					// select
					if (currentAction > 9) {
						if (currentShape != null) {
							JPanel p = new JPanel();
							try {
								currentShape.setPosition(new Point(e.getX(), e.getY()));
								newShape = (Shape) currentShape.clone();
							} catch (CloneNotSupportedException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							JOptionPane pane = new JOptionPane();
							List<JTextField> options = new LinkedList<>();
							List<JLabel> o = new ArrayList<>();
							Set<String> keys = new HashSet<>();
							keys = currentShape.getProperties().keySet();
							int i = 0;
							Point point = currentShape.getPosition();
							p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
							o.add(new JLabel("PositionX"));
							options.add(new JTextField(Integer.toString(point.x)));
							i++;
							o.add(new JLabel("PositionY"));
							options.add(new JTextField(Integer.toString(point.y)));
							i++;
							p.add(o.get(0));
							p.add(options.get(0));
							p.add(o.get(1));
							p.add(options.get(1));
							for (String s : keys) {
								o.add(new JLabel(s));
								options.add(new JTextField(currentShape.getProperties().get(s).toString()));
								p.add(o.get(i));
								p.add(options.get(i));
								i++;
							}

							@SuppressWarnings("static-access")
							int n = pane.showConfirmDialog(null, p, "Enter new Coordinates",
									JOptionPane.OK_CANCEL_OPTION);
							if (n == JOptionPane.OK_OPTION) {
								Point position = new Point(Integer.parseInt(options.get(0).getText()),
										Integer.parseInt(options.get(1).getText()));
								HashMap<String, Double> m = new HashMap<>();
								for (int l = 2; l < options.size(); l++) {
									String key = o.get(l).getText();
									m.put(key, Double.valueOf(options.get(l).getText()));
								}
								newShape.setPosition(position);
								newShape.setProperties(m);
								controller.addShape(newShape);
								currentShape = null;
								currentAction = actions.get("select");
								}
						}
					} else	if (currentAction == actions.get("select")) {
						currentShape = controller.shapeSelected(e.getX(), e.getY());
						newS = false;
						if (currentShape == null) {
							dragging = false;
							canDrag = false;
						} else {

							dragP = new Point(e.getX(), e.getY());
							try {
								newShape = (Shape) currentShape.clone();
							} catch (CloneNotSupportedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							dragX = (int) newShape.getPosition().getX();
							dragY = (int) newShape.getPosition().getY();
							// special case trinagle & line
							if (controller.shapeAction(newShape.getClass().getSimpleName()) == 9) {
								ext1 = new Point(newShape.getProperties().get("X2").intValue(),
										newShape.getProperties().get("Y2").intValue());
								ext2 = new Point(newShape.getProperties().get("X3").intValue(),
										newShape.getProperties().get("Y3").intValue());
								a1 = controller.angle(new Point(dragX, dragY), (Point) ext1);
								a2 = controller.angle(new Point(dragX, dragY), (Point) ext2);
								l1 = controller.sideL(new Point(dragX, dragY), (Point) ext1);
								l2 = controller.sideL(new Point(dragX, dragY), (Point) ext2);

							} else if (controller.shapeAction(newShape.getClass().getSimpleName()) == 4) {
								ext1 = new Point(newShape.getProperties().get("endPointX").intValue(),
										newShape.getProperties().get("endPointY").intValue());
								a1 = controller.angle(new Point(dragX, dragY), (Point) ext1);
								l1 = controller.sideL(new Point(dragX, dragY), (Point) ext1);

							}
							canDrag = true;
						}

					} else if (currentAction == 2) {// Stroke color
						currentShape = controller.shapeSelected(e.getX(), e.getY());
						if (currentShape != null) {
							try {
								newShape = (Shape) currentShape.clone();
							} catch (CloneNotSupportedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							newShape.setColor(stroke);
							controller.updateShape(currentShape, newShape);
						}
						// color fill
					} else if (currentAction == 3) {
						currentShape = controller.shapeSelected(e.getX(), e.getY());

						if (currentShape != null) {
							try {
								newShape = (Shape) currentShape.clone();
							} catch (CloneNotSupportedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							newShape.setFillColor(fill);
							controller.updateShape(currentShape, newShape);
						}
						// move shape
					} else {// draw new shape
						newS = true;
						drawStart = new Point(e.getX(), e.getY());
						drawEnd = drawStart;
					}

					repaint();
				}

				// releasing the mouse
				public void mouseReleased(MouseEvent e) {
					// move shape
					if (currentAction == 1) {
						newS = false;
						if (dragging) {
							controller.addShape(newShape);
						}
					} else {// check if not deleting shape
						if (currentAction != 2 && currentAction != 3 && !(currentAction > 9)) {
							currentShape = controller.addNewShape(currentAction, drawStart.x, drawStart.y, e.getX(),
									e.getY(), fill, stroke);
							// sel = controller.getBounds(currentShape);

						}

					}
					// re-intialize everything
					dragP = null;
					drawStart = null;
					dragging = false;
					canDrag = false;
					drawEnd = null;
					ext1 = null;
					ext2 = null;
					currentAction = 1;
					newS = false;
					repaint();
				}
			});
			// dragging mouse
			this.addMouseMotionListener(new MouseAdapter() {
				public void mouseDragged(MouseEvent e) {
					// if move the shape
					if (canDrag) {
						dragging = true;
						newS = false;
						int dx = e.getX() - dragP.x;
						int dy = e.getY() - dragP.y;

						dragX += dx;
						dragY += dy;
						controller.removeWithoutUpdate(currentShape);
						// special case triangle
						if (actions.get(newShape.getClass().getSimpleName()) == 9) {
							ext1 = controller.getPoint(new Point(dragX, dragY), a1, l1);
							ext2 = controller.getPoint(new Point(dragX, dragY), a2, l2);
							// special case line
						} else if (actions.get(newShape.getClass().getSimpleName()) == 4) {
							ext1 = controller.getPoint(new Point(dragX, dragY), a1, l1);
						}
						dragP = e.getPoint();
					} else {// drawing the shape
						drawEnd = e.getPoint();
						newS = true;
					}
					repaint();
				}
			});

		}

		public void paint(Graphics g) {
			super.paint(g);
			/*
			 * if(sel != null){ ((Graphics2D) g).setColor(Color.LIGHT_GRAY);;
			 * ((Graphics2D) g).fill(sel); }
			 */
			// refresh
			controller.redrawAll(g);
			// dragging new shape, in process
			if (drawStart != null && drawEnd != null && newS) {
				newShape = null;
				newShape = controller.setNewShape(currentAction, drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
				if (newShape != null) {
					newShape.setColor(stroke);
					newShape.setFillColor(fill);
					newShape.draw(g);
				}
			}
			// moving shape
			if (dragging) {
				newShape.setPosition(new Point(dragX, dragY));
				// special case of movings triangle
				if (actions.get(newShape.getClass().getSimpleName()) == 9) {
					HashMap<String, Double> m = new HashMap<String, Double>();
					m.put("X2", ext1.getX());
					m.put("Y2", ext1.getY());
					m.put("X3", ext2.getX());
					m.put("Y3", ext2.getY());
					newShape.setProperties(m);
				} else if (actions.get(newShape.getClass().getSimpleName()) == 4) {// special
																					// case
																					// of
																					// moving
																					// line
					HashMap<String, Double> m = new HashMap<String, Double>();
					m.put("endPointX", ext1.getX());
					m.put("endPointY", ext1.getY());
					newShape.setProperties(m);

				}

				newShape.draw(g);
			}

		}
	}

}