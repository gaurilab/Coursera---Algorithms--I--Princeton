import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int N;          // size of the queue
    private Node front, rear;     // top of stack

    // helper linked list class
    private class Node {
        private Item item;
        private Node prev;
        private Node next;
        public Node() {
            prev = null;
            next = null;
        }
    }

    // construct an empty deque
    public Deque() {
        front = null;
        rear = null;
        N = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (front == null && rear == null);
    }
    public int size() {
        return N;
    }                       // return the number of items on the deque

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException("Cannot add a null element");
        }
        Node t = new Node();
        t.item = item;
        t.next = null;
        t.prev = null;

        if (isEmpty()) {
            front = t;
            rear = t;
        } else {
            t.next = front;
            //assert(front.prev == null);
            front.prev = t;
            front = t;
        }
        N++;

    } 
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException("Cannot add a null element");
        }
        Node t = new Node();
        t.item = item;
        t.next = null;
        t.prev = null;

        if (isEmpty()) {
            front = t;
            rear = t;
        } else {
            t.prev = rear;
            rear.next = t;
            rear = t;
        }
        N++;

    }
    public Item removeFirst()  {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }

        Item item = front.item;
        if (front == rear) {
            front = null;
            rear = null;
        } else {
            front = front.next;
            front.prev = null;
        }
        --N;
        return item;
    }
    // remove and return the item from the front
    public Item removeLast()   {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }

        Item item = rear.item;
        if (front == rear) {
            front = null;
            rear = null;
        } else {
            rear = rear.prev;
            rear.next = null;
        }
        --N;
        return item;
    }              // remove and return the item from the end
    public Iterator<Item> iterator()  { 
        return new DequeIterator();
    }       // return an iterator over items in order from front to end

    private class DequeIterator implements Iterator<Item> {
        private Node current = front;
        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }


    public static void main(String[] args) {
        Deque<String> s = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) s.addLast(item);
            else if (!s.isEmpty()) StdOut.print(s.removeFirst() + " ");
        }
        StdOut.println("(" + s.size() + " left on deque)");

    }   // unit testing

}
