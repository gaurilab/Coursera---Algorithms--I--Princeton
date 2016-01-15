import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.UnsupportedOperationException;
import java.lang.NullPointerException;



public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] CQ;
    private int front;
    private int rear;
    private int sz;
    private int capacity;

    // construct an empty randomized queue
    public RandomizedQueue() {
        capacity = 2;
        CQ = new Item[capacity];
        front = capacity - 1;
        rear = capacity - 1;
        sz = 0 ;
    }

    private void squeeze() {
        int newSz = capacity / 2;
        if (sz > 2 && sz <= newSz) {

            Item[] newN = new Item[newSz];
            int i = 0;
            int curr = rear ;
            while (cur != front) {
                newN[i++] = CQ[curr];
                cur = (cur + 1 ) % capacity;
            }
            //can shuffle the array
            CQ = newN;
            rear = 0 ;
            front = i - 1;
        }
    }

    private void inflate() {
        int newSz = capacity * 2;
        if (sz + 1 == capacity) {
            Item[] newN = new Item[newSz];

            int i = 0;
            int curr = rear ;
            while (cur != front) {
                newN[i++] = CQ[curr];
                cur = (cur + 1 ) % capacity;
            }
            //can shuffle the array
            CQ = newN;
            rear = 0 ;
            front = i - 1;
            //copy
        }

    }

    public boolean isEmpty() {
        return (front == rear) ;
    }                 // is the queue empty?
    private boolean isFull() {
        return ((front+1) % capacity == rear %capacity);
    }

    // return the number of items on the queue
    public int size() {
        return sz;
    }

    // add the item
    public void enqueue(Item item)  {
        if (item == null ) {
            throw new NullPointerException("Cannot add a null element");
        }

        front = (front + 1) % capacity;
        CQ[front] = item;
        sz++;	
        inflate();

    }

	// remove and return a random item
    public Item dequeue() {
        if(isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        
        Item item = CQ[rear];
        rear = (rear + 1) % capacity;
        --sz;
        squeeze();
        return item;
    }

	// return (but do not remove) a random item
    public Item sample() {
        int i = (rear + StdRandom.uniform(0 , sz)) % capacity;
        return CQ[i];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator()  {
        return new RandomDequeIterator();

    }
    private class RandomDequeIterator implements Iterator<Item> {
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
    }  // unit testing
}
