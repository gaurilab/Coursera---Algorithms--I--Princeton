import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
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

                String[] set = new String[K];
                int count = 0 ;
                while (!StdIn.isEmpty()) {
                    String item = StdIn.readString();

                    if (count < K) {
                        set[count++] = item;
                    } else {
                        int drop = StdRandom.uniform(0 , 2);
                        if (drop == 1) continue;
                        int c = StdRandom.uniform(0, K);
                        set[c] = item;
                    }

                    StdRandom.shuffle(set,0, count-1);
                }
                for (int z = 0 ; z < count ; ++z) {
                    StdOut.println(set[z]);
                }
            }

        } catch (NumberFormatException e) {
            System.err.println("Argument" + args[0] + " must be an integer.");
            System.exit(1);
        }

    }  // unit testing
}
