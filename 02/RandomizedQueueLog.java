import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.UnsupportedOperationException;
import java.lang.NullPointerException;



public class RandomizedQueue<Item> implements Iterable<Item> {
    private int capacity;
    private Item[] CQ;
    private int top;

    // construct an empty randomized queue
    public RandomizedQueue() {
        capacity = 2;
        CQ = (Item[]) new Object[capacity + 1];
        top = 0;
    }

    private void deflate() {
        int newSz = capacity / 2;
        if (capacity > 2 && size() < newSz) {
            StdOut.printf("Changing the capacity from %d  to %d\n",capacity, newSz);
            Item[] newN = (Item[]) new Object[newSz + 1];
            //copy
            for (int k = 0 ; k < size() ; ++k) {
                newN[k] = CQ[k];
            }
            //update
            capacity = newSz;
            CQ = newN;
        }
    }

    private void inflate() {
        int newSz = capacity * 2;
        StdOut.printf("Changing the capacity from %d  to %d\n",capacity, newSz);
        Item[] newN = (Item[]) new Object[newSz + 1];
        //copy
        for (int k = 0 ; k < size() ; ++k) {
            newN[k] = CQ[k];
        }
        //update
        capacity = newSz;
        CQ = newN;
    }

    // is the queue empty?
    public boolean isEmpty() {
        return (top == 0) ;
    }

    private boolean isFull() {
        return (top == capacity);
    }

    // return the number of items on the queue
    public int size() {
        return top;
    }

    // add the item
    public void enqueue(Item item)  {
        if (item == null ) {
            throw new NullPointerException("Cannot add a null element");
        }
        if (!isFull()) {
            CQ[top++] = item;
        } else {
            inflate();
            CQ[top++] = item;
        }
    }


	// remove and return a random item
    public Item dequeue() {
        if(isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }

        for (int z = 0 ; z < size() ; ++z) {
            StdOut.println(this);
        }
       
        int k =  StdRandom.uniform(0 , top);
        Item item = CQ[k];
        //if k == top  this swap is no op
        CQ[k] = CQ[top-1];
        --top;
        deflate();

        return item;
    }

   
	// return (but do not remove) a random item
    public Item sample() {
        int k =  StdRandom.uniform(0 , top);
        return CQ[k];
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator()  {
        return new RandomDequeIterator();

    }

    private class RandomDequeIterator implements Iterator<Item> {
        private boolean[] mark;
        private int count;
        public RandomDequeIterator() {
            mark = new boolean[top];
            count = 0;
        }

        public boolean hasNext()  { return count < top;  }

        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            int k =  StdRandom.uniform(0 , top);

            while (mark[k] == true) {
                k =  StdRandom.uniform(0 , top);
            }
            Item item = CQ[k];
            mark[k] = true;
            count++;
            return item;
        }
    }

    public static void main(String[] args) {

        RandomizedQueue<String> s = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) s.enqueue(item);
            else if (!s.isEmpty()) StdOut.print(s.dequeue() + " ");
        }
        StdOut.println("(" + s.size() + " left on deque)");

    }  // unit testing
}
