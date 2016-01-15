import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;



public class Subset {

    public static void main(String[] args) {
        try {
            int K = Integer.parseInt(args[0]);
            int x = 0;
            if (K <= 0) {
                while (!StdIn.isEmpty()) {
                    StdIn.readString();
                }
            } else {

                RandomizedQueue<String> set = new RandomizedQueue<String>();
                while (!StdIn.isEmpty()) {
                    String item = StdIn.readString();
                    set.enqueue(item);
                }

                while(set.size() > K) {
                    set.dequeue();
                }
                for (String s : set) {
                    System.out.println(s);
                }

            }
        } catch (NumberFormatException e) {
            System.err.println("Argument" + args[0] + " must be an integer.");
        }

    }  // unit testing
}
