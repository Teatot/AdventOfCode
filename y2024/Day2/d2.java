package y2024.Day2;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;

public class d2 {
    private final ArrayList<String[]> board;

    public d2() {
        this.board = readFile();
    }
    // Task 1
    public int safeReports() {
        int count = 0;
        for ( int num = 0; num < this.board.size(); ++num ) {
            String[] report = this.board.get( num );
            if ( is_safe(report) ) {
                count++;
            }
        }
        return count;
    }
    // Task 2
    public int actualSafeReports() {
        int count = 0;
        for ( int num = 0; num < this.board.size(); ++num ) {
            String[] report = this.board.get( num );
            if ( is_safe(report) ) {
                ++count;
                continue;
            }
            for ( int j = 0; j < report.length; ++j ) {
                if ( is_safe( remove_element( report, j ) ) ) {
                    ++count;
                    break;
                }
            }
        }
        return count;
    }

    private static String[] remove_element(String[] report, int index ) {
        String[] copy = new String[report.length - 1];
        int k = 0;
        for ( int i = 0; i < report.length; ++i ) {
            if ( i != index ) {
                copy[k] = report[i];
                ++k;
            }
        }
        return copy;
    }

    private static boolean is_safe(String[] report) {
        int increase = 0;
        int decrease = 0;
        for ( int i = 1; i < report.length; ++i ) {
            int curKey = Integer.parseInt(report[i - 1]);
            int nextKey = Integer.parseInt(report[i]);
            int difference = Math.abs(nextKey - curKey);
            if ( difference >= 1 && difference <= 3 ) {
                if ( curKey < nextKey ) {
                    ++increase;
                }
                else if ( curKey > nextKey ) {
                    ++decrease;
                }
            }
        }
        return increase == report.length - 1 || decrease == report.length - 1;
    }


    private ArrayList<String[]> readFile() {
        String file_name = "";

        ArrayList<String[]> collection = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String line;
            String[] spliced_line;
            while ( ( line = br.readLine() ) != null ) {
                spliced_line = line.split(" ");
                collection.add(spliced_line);
            }
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        return collection;
    }

}
