import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;
import java.util.Iterator;



public class RandomizedQueue<Item> implements Iterable<Item> {
    private int capacity;
    private Item[] cQ;
    private int top;

    // construct an empty randomized queue
    public RandomizedQueue() {
        capacity = 1;
        cQ = (Item[]) new Object[capacity + 1];
        top = 0;
    }

    private void deflate() {
        int newSz = capacity / 2;
        if (capacity > 1 && size() < newSz) {
            Item[] newN = (Item[]) new Object[newSz + 1];
            //copy
            for (int k = 0; k < size(); ++k) {
                newN[k] = cQ[k];
            }

            for(int l = 0 ;  l < capacity ; ++l) {
                cQ[l] = null;
            }

            //update
            capacity = newSz;
            cQ = newN;
        }
    }

    private void inflate() {
        int newSz = capacity * 2;
        Item[] newN = (Item[]) new Object[newSz + 1];
        //copy
        for (int k = 0; k < size(); ++k) {
            newN[k] = cQ[k];
        }
        for(int l = 0 ;  l < size() ; ++l) {
            cQ[l] = null;
        }

        //update
        capacity = newSz;
        cQ = newN;
    }

    // is the queue empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    private boolean isFull() {
        return (size() == capacity);
    }

    // return the number of items on the queue
    public int size() {
        return top;
    }

    // add the item
    public void enqueue(Item item)  {
        if (item == null) {
            throw new NullPointerException("Cannot add a null element");
        }
        if (!isFull()) {
            cQ[top++] = item;
        } else {
            inflate();
            cQ[top++] = item;
        }
    }


    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }

        int k =  StdRandom.uniform(0 , top);
        Item item = cQ[k];
        cQ[k] = null;
        //if k == top  this swap is no op
        cQ[k] = cQ[top-1];
        //to prevent loiteing
        cQ[top] = null;
        --top;
        deflate();

        return item;
    }

   
    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        int k =  StdRandom.uniform(0 , top);
        return cQ[k];
    }


    // return an independent iterator over items in random order
    public Iterator<Item> iterator()  {
        return new RandomDequeIterator();

    }

    private class RandomDequeIterator implements Iterator<Item> {
        private int[] indices;
        private int p;
        public RandomDequeIterator() {
            indices = new int[top];
            p = 0;
            for(int k = 0 ; k < top ; ++k) {
                indices[k] = k;
            }
            StdRandom.shuffle(indices);
        }

        public boolean hasNext()  {
            return p < top; 
        }

        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            Item item = cQ[p++];
            return item;
        }
    }

    public static void main(String[] args) {

        RandomizedQueue<String> s = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) s.enqueue(item);
            else {
                for(String t : s) {
                    StdOut.print(t);
                }
            }
        }
        StdOut.println("(" + s.size() + " left on deque)");

    }  // unit testing
}
