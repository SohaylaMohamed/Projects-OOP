package eg.edu.aleu.csd.filestructure.sort.cs32;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Main {

  public static void main(String[] args) {
    int[] at = {5,6,7,8,9,10};
    Collection a = new ArrayList<Integer>();
    a.add(6);
    a.add(3);
    a.add(5);
    a.add(7);
    a.add(1);
    a.add(4);
    a.add(2);

    Heap h = new Heap();
    h.build(a);
    h.printHeap();
  }

}
