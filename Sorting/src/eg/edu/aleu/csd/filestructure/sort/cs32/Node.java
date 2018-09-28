package eg.edu.aleu.csd.filestructure.sort.cs32;

import eg.edu.alexu.csd.filestructure.sort.INode;

/**
 * Created by HP on 2/24/2018.
 */
public class Node implements INode {

  private INode leftChild;
  private INode rightChild;
  private INode parent;
  private Comparable value;

  public Node(Object element) {
    this.parent = null;
    this.value = (Comparable) element;
    leftChild = null;
    rightChild = null;
  }

  public void setParent(Node p) {
    Node grand = (Node) p.getParent();
    this.parent = grand;
    if (grand.getLeftChild() != null) {
      if (grand.getLeftChild().equals(p)) {
        grand.setLeft(this);
      } else {
        grand.setRight(this);
      }
    }
  }

  public void setLeft(INode left) {
    this.leftChild = left;
  }

  public void setRight(INode right) {
    this.rightChild = right;
  }

  @Override
  public INode getLeftChild() {
    return leftChild;
  }

  @Override
  public INode getRightChild() {
    return rightChild;
  }

  @Override
  public INode getParent() {
    return parent;
  }

  @Override
  public Comparable getValue() {
    return value;
  }

  @Override
  public void setValue(Comparable value) {
    this.value = value;
  }

  public void setParentChild(Node iNode) {
    this.parent = iNode;
    if (iNode.getLeftChild() == (null)) {
      iNode.setLeft(this);
    } else {
      iNode.setRight(this);
    }
  }

  public void removeChild(Node last) {
    if (this.getLeftChild() != null) {
      if (this.getLeftChild().equals(last)) {
        this.setLeft(null);
      } else {
        this.setRight(null);
      }
    }
  }
}
