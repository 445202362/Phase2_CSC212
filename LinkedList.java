package PhaseTwo;



public class LinkedList<T> implements List<T> {
    private Node<T> head;
    private Node<T> current;

    public LinkedList() {
        head = current = null;
    }

    // Operation 1: findFirst
    public void findFirst() {
        current = head;
    }

    // Operation 2: findNext
    public void findNext() {
        if (current != null) {
            current = current.next;
        }
    }

    // Operation 3: retrieve
    public T retrieve() {
        if (current != null) {
            return current.data;
        }
        return null;
    }

 
    public void update(T e) {
        if (current != null) {
            current.data = e;
        }
    }

    public void insert(T e) {
        Node<T> tmp;
        if (empty()) {
            current = head = new Node<T>(e);
        } else {
            tmp = current.next;
            current.next = new Node<T>(e);
            current = current.next;
            current.next = tmp;
        }
    }

  
    public void remove() {
        if (current == head) {
            head = head.next;
        } else {
            Node<T> tmp = head;
            while (tmp.next != current) {
                tmp = tmp.next;
            }
            tmp.next = current.next;
        }

        if (current.next == null) {
            current = head;
        } else {
            current = current.next;
        }
    }

   
    public boolean full() {
        return false; // Linked list is never full
    }

    
    public boolean empty() {
        return head == null;
    }


    public boolean last() {
        return current != null && current.next == null;
    }
    
  
    public int size() {
        int count = 0;
        Node<T> tmp = head;
        while (tmp != null) {
            count++;
            tmp = tmp.next;
        }
        return count;
    }
    public T findLast() {
        if (empty()) {
            return null;
        }
        
        findFirst();
        while (!last()) {
            findNext();
        }
        return retrieve();
    }
   
  
}
