package eg.edu.aleu.csd.filestructure.sort.cs32;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

  public static void main(String[] args) {
    int[] at = {6, 3, 5, 7, 1, 4, 2};
    Set mySet = new HashSet<>();
    Sort s = new Sort();
    ArrayList a =  new ArrayList<>();
    a.add(6);
    a.add(3);
    a.add(5);
    a.add(7);
    a.add(1);
    a.add(4);
    a.add(2);
    
    
    Heap h = (Heap) s.heapSort(a);
    h.printHeap();  
// 0h.build(a);
 
    //h.extract();

    //h.sort();
  }

}
