package eg.edu.alexu.csd.oop.draw.cs32;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import eg.edu.alexu.csd.oop.draw.Shape;

public class ChangesTracker {

	private Stack<ArrayList<Shape>> prev = new Stack<ArrayList<Shape>>();
	private Stack<ArrayList<Shape>> next = new Stack<ArrayList<Shape>>();
	boolean canUndo = true, canRedo = false;
	/**
	 * tracker to the changes that happen during drawing
	 * @param shapes initial shapes list
	 */
	public ChangesTracker(ArrayList<Shape> shapes) {
		prev.add(shapes);
	}
	/**
	 * any changes occuring to the shapes
	 * @param shapes new shapes list
	 */
	public void newChange(ArrayList<Shape> shapes) {
		next.clear();
		ArrayList<Shape> temp = new ArrayList<>(shapes);
		prev.push(temp);
		if (prev.size() > 21) {
			prev.remove(0);
		}
		canUndo = true;
		canRedo = false;
	}
	/**
	 * undo to the last 20 changes made
	 * @return last change if exist
	 */
	public ArrayList<Shape> undo() {

		if (prev.size() > 1) {
			canUndo = true;
			next.push(prev.pop());
			canRedo = true;
		}else{
			canUndo = false;
		}
		return prev.peek();

	}
	/**
	 * redo the changes after undo
	 * @return next changes 
	 */
	public ArrayList<Shape> redo() {
		if (!next.isEmpty()) {
			canRedo = true;
			prev.push(next.pop());
			canUndo = true;

		}else{
			canRedo = false;
		}
		return prev.peek();

	}
	/**
	 * clear all history made, after loading or new file
	 */
	public void clearAll() {
		prev.clear();
		next.clear();
		canRedo = false;
		canUndo = true;
	}
	/**
	 * check if undo is available 
	 * @return true if can undo
	 */
	public boolean checkUndo() {
		
		return canUndo;
	}
	/**
	 * check if any more redo can be made
	 * @return true if yes
	 */
	public boolean checkRedo() {
		
		return canRedo;
	}

}
