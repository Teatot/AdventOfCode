package y2024.Day1;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;


public class d1 {
    private final int len;
    private ArrayList<int[]>[] collection;
    private HashMap<Integer, Integer> trackingListTwo;
    // Constructor
    public d1() {
        this.collection = readFile();
        this.len = left_list().size();
        this.trackingListTwo = getTrackingListTwo();
        sort_list(left_list());
        sort_list(right_list());
    }
    // Task 1
    public final int totalDistance() {
        int sum = 0;
        ArrayList<int[]> leftList = left_list();
        ArrayList<int[]> rightList = right_list();
        for ( int i = 0; i < this.len; ++i ) {
            int[] l_content = leftList.get(i);
            int[] r_content = rightList.get(i);
            sum += Math.abs( l_content[1] - r_content[1] );
        }
        return sum;
    }
    // Task 2
    public final int similarityScore() {
        int sum = 0;
        ArrayList<int[]> lstLeft = this.left_list();
        HashMap<Integer, Integer> trackRight = this.trackingListTwo;
        for ( int i = 0; i < this.len; ++i ) {
            int key = lstLeft.get(i)[1];
            if ( trackRight.containsKey( key ) ) {
                sum += key * trackRight.get( key );
            }
        }
        return sum;
    }

    private ArrayList<int[]> left_list() {
        return this.collection[0];
    }

    private ArrayList<int[]> right_list() {
        return this.collection[1];
    }

    private ArrayList<int[]>[] readFile() {
        String file_name = "";

        ArrayList[] collection = new ArrayList[2];
        collection[0] = new ArrayList<Integer>();
        collection[1] = new ArrayList<Integer>();

        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String line;
            String[] spliced_line;
            int i = 0;
            while ( ( line = br.readLine() ) != null ) {
                spliced_line = line.split(" ");
                ArrayList list1 = collection[0];
                ArrayList list2 = collection[1];
                // List 1: Value Mapped
                int[] temp1 = {i, Integer.parseInt(spliced_line[0])};
                list1.add(temp1);
                // List 2: Value Mapped
                int[] temp2 = {i, Integer.parseInt(spliced_line[spliced_line.length - 1])};
                list2.add(temp2);
                ++i;
            }
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        return collection;
    }

    private void sort_list(ArrayList<int[]> list) {
        list.sort(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[1] - o2[1]; // Ascending Order
            }
        });
    }

    public void print_list() {
        ArrayList<int[]> list1 = left_list();
        ArrayList<int[]> list2 = right_list();
        for ( int i = 0; i < this.len; ++i ) {
            int[] content1 = list1.get(i);
            int[] content2 = list2.get(i);
            System.out.println("1: " + content1[0] + ", " + content1[1]);
            System.out.println("2: " + content2[0] + ", " + content2[1] + "\n");
        }
    }

    private HashMap<Integer, Integer> getTrackingListTwo() {
        ArrayList<int[]> lst = this.right_list();
        HashMap<Integer, Integer> tracking = new HashMap<>();
        for ( int i = 0; i < this.len; ++i ) {
            int new_key = lst.get(i)[1];
            if ( tracking.containsKey( new_key ) ) {
                tracking.put( new_key, tracking.get( new_key ) + 1 );
                continue;
            }
            tracking.put( new_key, 1 );
        }
        return tracking;
    }
}
