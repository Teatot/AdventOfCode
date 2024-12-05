package y2024.Day3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class d3 {
    private final String[] mem_arr;

    public d3() {
        this.mem_arr = readFile();
        String t = String.join("", this.mem_arr);
    }
    // Part 1
    private static int calculateScore( String mem ) {
        int total = 0;
        // Defining the Regular Expression
        String regex = "mul\\(\\d{1,3},\\d{1,3}\\)";

        // Compile the Pattern
        Pattern pattern = Pattern.compile(regex);

        // Creating a Matcher
        Matcher matcher = pattern.matcher(mem);

        while ( matcher.find() ) {
            total += extractNum( matcher.group() );
        }
        return total;
    }

    private static int extractNum( String str ) {
        String[] num = str.replaceAll("mul|\\(|\\)", "").split(",");
        return Integer.parseInt(num[0]) * Integer.parseInt(num[1]);
    }
    // Part 2
    private static int deepCalculateScore( String mem ) {
        int total = 0;
        for ( String segment : mem.split("do\\(\\)") ) {
            total += calculateScore( segment.split("don't\\(\\)")[0] );
        }
        return total;
    }

    private static String[] readFile() {
        String file_name = "/Users/tonychen/Documents/AdventofCode/src/y2024/Day3/pzleinput.txt";

        String[] collection = new String[6];

        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String line;
            int i = 0;
            while ( ( line = br.readLine() ) != null ) {
                collection[i] = line.replaceAll("\n", "");
                ++i;
            }
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        return collection;
    }
}
