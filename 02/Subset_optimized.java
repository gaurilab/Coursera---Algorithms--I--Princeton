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
                int count = 0;
                while (!StdIn.isEmpty()) {
                    String item = StdIn.readString();

                    if (count < K) {
                        ++count;
                        set.enqueue(item);
                    } else {
                        int drop = StdRandom.uniform(0 , 2);
                        if (drop == 1) continue;
                        set.dequeue();
                        set.enqueue(item);
                    }
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
