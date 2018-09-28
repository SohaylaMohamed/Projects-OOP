package eg.edu.aleu.csd.filestructure.sort.cs32;

import eg.edu.alexu.csd.filestructure.sort.IHeap;
import eg.edu.alexu.csd.filestructure.sort.ISort;

import java.util.ArrayList;

public class Sort<T extends Comparable<T>> implements ISort<T> {

  @Override
  public IHeap heapSort(ArrayList<T> unordered) {
    Heap heap = new Heap();
    heap.build(unordered);
    heap.sort();
    return heap;
  }

  @Override
  public void sortSlow(ArrayList<T> unordered) {
    for (int i = 0; i < unordered.size() - 1; i++) {
      for (int j = 0; j < unordered.size() - 1 - i; j++) {
        if (unordered.get(j).compareTo(unordered.get(j + 1)) > 0) {
          // Object temp = unordered.get(j);
          unordered.add(j, unordered.remove(j + 1));
          // unordered.add(j , (T) temp);
        }
      }
    }
  }

  @Override
  public void sortFast(ArrayList<T> unordered) {
    mergeSort(unordered, 0, unordered.size() - 1);
  }

  private ArrayList<T> mergeSort(ArrayList<T> list, int left, int right) {
    int mid = ((right - left) / 2) + left;
    if (right - (mid) > 1) {
      list = mergeSort(list, mid + 1, right);
    }
    if (mid - left >= 1) {
      list = mergeSort(list, left, mid);
    }
    list = sortingTwoOrderedLists(list, left, right, mid);
    return list;

  }

  private ArrayList<T> sortingTwoOrderedLists(ArrayList<T> list, int left, int right, int mid) {
    ArrayList<T> temp = new ArrayList<>();
    int i = left, j = mid + 1;
    while (i <= mid && j <= right) {
      if (list.get(i).compareTo(list.get(j)) < 1) {
        temp.add(list.get(i));
        i++;
      } else {
        temp.add(list.get(j));
        j++;
      }
    }

    if (i <= mid) {
      while (i <= mid) {
        temp.add(list.get(i));
        i++;
      }
    } else if (j <= right) {
      while (j <= right) {
        temp.add(list.get(j));
        j++;
      }
    }

    for (int k = 0; k < temp.size(); k++) {
      list.set(left + k, temp.get(k));
    }
    return list;
  }

}
