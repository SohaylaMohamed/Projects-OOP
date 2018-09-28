package eg.edu.aleu.csd.filestructure.sort.cs32;

import eg.edu.alexu.csd.filestructure.sort.IHeap;
import eg.edu.alexu.csd.filestructure.sort.INode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public class Heap implements IHeap {

	private ArrayList<Node> heapArray;

	public Heap() {
		heapArray = new ArrayList<Node>();
	}

	@Override
	public INode getRoot() {
		if (this.size() > 0) {
			return heapArray.get(0);
		}
		return null;

	}

	@Override
	public int size() {
		return heapArray.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void heapify(INode node) {

		INode leftChild = node.getLeftChild();
		INode rightChild = node.getRightChild();
		INode maxChild;
		if (leftChild == null) {
			return;
		}
		int comapre;
		if (rightChild != null) {
			comapre = leftChild.getValue().compareTo(rightChild.getValue());
			if (comapre < 0) {
				maxChild = rightChild;
			} else {
				maxChild = leftChild;
			}
		} else {
			maxChild = leftChild;
		}

		comapre = maxChild.getValue().compareTo(node.getValue());
		if (comapre > 0) {
			swap(maxChild, node);
			heapify(maxChild);
		}

	}

	@SuppressWarnings("unchecked")
	private void swap(INode node1, INode node2) {

		Comparable value = node1.getValue();
		node1.setValue(node2.getValue());
		node2.setValue(value);

	}

	@Override
	public Comparable extract() {
		if (this.size() == 0) {
			return null;
		}
		Comparable value = heapArray.get(0).getValue();
		Node last = heapArray.get(this.size() - 1);
		Comparable valueR = last.getValue();
		if (this.size() == 1) {
			heapArray.remove(0);
			return value;
		}
		((Node) last.getParent()).removeChild(last);
		heapArray.remove(last);
		heapArray.get(0).setValue(valueR);
		heapify(heapArray.get(0));
		return value;

	}

	@Override
	public void insert(Comparable element) {
		if (heapArray.size() == 0) {
			Node newNode = new Node(element);
			heapArray.add(newNode);
			return;
		}
		int parentIndex = (heapArray.size() - 1) / 2;
		Node newNode = new Node(/* heapArray.get(parentIndex), */ element);
		newNode.setParentChild(heapArray.get(parentIndex));
		heapArray.add(newNode);
		UpHeapify(newNode);

	}

	private void UpHeapify(INode newNode) {
		if (this.getRoot().equals(newNode)) {
			return;
		}
		INode parent = newNode.getParent();
		@SuppressWarnings("unchecked")
		int compare = newNode.getValue().compareTo(parent.getValue());
		if (compare > 0) {
			swap(parent, newNode);
			UpHeapify(parent);
		}

	}

	@Override
	public void build(Collection unordered) {
		@SuppressWarnings("unchecked")
		List list = new ArrayList(unordered);

		if (list.size() == 0) {
			return;
		}
		int i = 0;
		Node n = new Node(list.get(i));
		heapArray.add(n);
		i++;
		while (i < list.size()) {
			n = new Node(list.get(i));
			n.setParentChild(heapArray.get((i - 1) / 2));
			heapArray.add(n);
			i++;
		}
		makeMaxHeap(heapArray.size());
	}

	private void makeMaxHeap(int size) {
		for (int i = (size - 1) / 2; i >= 0; i--) {
			heapify(heapArray.get(i));
		}

	}

	  public void printHeap() {
		    for (int i = 0; i < heapArray.size(); i++) {
		      System.out
		          .println(heapArray.get(i).getValue() + "," + heapArray.get(i).getLeftChild().getValue()
		              + "," + heapArray.get(i).getRightChild().getValue());
		    }
		    System.out.println("-------------------");

		  }

	

	public void sort() {
		int size = this.size();
		for (int i = size - 1; i >= 1; i--) {
			Node last = heapArray.get(i);
			Comparable tempV = last.getValue();

			last.setValue(heapArray.get(0).getValue());
			((Node) last.getParent()).removeChild(last);

			heapArray.get(0).setValue(tempV);
			makeMaxHeap(i);
		}
		childParentRelations();

	}

	private void childParentRelations() {
		for(int i = 0; i < this.size(); i++) {
			int j = 2*i+1;
			if(j >= this.size()) {
				break;
			}
			Node n = heapArray.get(j);
			n.setParentChild(heapArray.get(i));
			j = 2*i+2;
			if(j >= this.size()) {
				break;
			}
			n = heapArray.get(j);
			n.setParentChild(heapArray.get(i));
		}
		
	}



}
