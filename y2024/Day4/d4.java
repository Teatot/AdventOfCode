package y2024.Day4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class d4 {
    private final ArrayList<ArrayList<Character>> grid;
    private final int max_row;
    private final int max_col;

    public d4() {
        this.grid = readFile();
        this.max_row = grid.size();
        this.max_col = grid.getFirst().size();
    }

    public int part1() {
        int num_of_xmas = 0;
        for ( int row = 0; row < max_row; ++row ) {

            ArrayList<Character> cur_row = this.grid.get(row);
            String str_curRow = convertString( cur_row );
            num_of_xmas += checkHorizontal( str_curRow );

            for ( int col = 0; col < max_col; ++col ) {
                if ( cur_row.get(col) == 'X' ) {
                    // Diagonals
                    num_of_xmas += checkUpperDiagonals(row, col);
                    num_of_xmas += checkLowerDiagonals(row, col);
                    // Verticals
                    num_of_xmas += checkUpperVertical(row, col);
                    num_of_xmas += checkLowerVertical(row, col);
                }
            }
        }
        return num_of_xmas;
    }

    public int part2() {
        int num_of_x_mas = 0;
        for ( int row = 0; row < max_row; ++row ) {

            ArrayList<Character> cur_row = this.grid.get(row);

            for ( int col = 0; col < max_col; ++col ) {
                if ( cur_row.get(col) == 'A' ) {
                    num_of_x_mas += valid_XMAS(row, col);
                }
            }
        }
        return num_of_x_mas;
    }

    public void printGrid() {
        for ( ArrayList<Character> row : this.grid ) {
            for ( Character ch : row ) {
                System.out.print(ch + " ");
            }
            System.out.println();
        }

    }

    private static int checkHorizontal( String line ) {
        int cur_count = 0;
        // Setting up REGEX
        String regex = ".{4}"; // Finds Characters (not including spaces) that are 4 characters long

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher( line );

        while ( matcher.find() ) {
            if ( matcher.group().equals("XMAS") || matcher.group().equals("SAMX") ) {
                ++cur_count;
            }
            // Following a Sliding Window Technique
            matcher.region(matcher.start() + 1, line.length());
        }
        return cur_count;
    }

    private int checkUpperDiagonals( int row, int col ) {
        int cur_count = 0;
        char[] letters = {'M', 'A', 'S'};
        // Upper Diagonals
        // Top Restrictions
        if ( row - letters.length >= 0 ) {

            int left_diagonalCounter = 0;
            int right_diagonalCounter = 0;

            for ( int i = 1; i < letters.length + 1; ++i ) {
                // Left Diagonal
                if ( col - i >= 0 && this.grid.get(row - i).get(col - i) == letters[i - 1] ) {
                    ++left_diagonalCounter;
                }
                // Right Diagonal
                if ( col + i < max_col && this.grid.get(row - i).get(col + i) == letters[i - 1] ) {
                    ++right_diagonalCounter;
                }
            }

            // Checking for Matches
            if ( left_diagonalCounter == letters.length ) {
                ++cur_count;
            }
            if ( right_diagonalCounter == letters.length ) {
                ++cur_count;
            }
        }
        return cur_count;
    }

    private int checkLowerDiagonals( int row, int col ) {
        int cur_count = 0;
        char[] letters = {'M', 'A', 'S'};
        // Lower Diagonals
        // Lower Restrictions
        if ( row + letters.length < max_row ) {

            int left_diagonalCounter = 0;
            int right_diagonalCounter = 0;

            for ( int i = 1; i < letters.length + 1; ++i ) {
                // Left Diagonal
                if ( col - i >= 0 && this.grid.get(row + i).get(col - i) == letters[i - 1] ) {
                    ++left_diagonalCounter;
                }
                // Right Diagonal
                if ( col + i < max_col && this.grid.get(row + i).get(col + i) == letters[i - 1] ) {
                    ++right_diagonalCounter;
                }
            }

            // Checking for Matches
            if ( left_diagonalCounter == letters.length ) {
                ++cur_count;
            }
            if ( right_diagonalCounter == letters.length ) {
                ++cur_count;
            }
        }
        return cur_count;
    }

    private int checkUpperVertical( int row, int col ) {
        char[] letters = {'M', 'A', 'S'};
        // Upper Restriction
        if ( row - letters.length >= 0 ) {

            int verticalCount = 0;

            for ( int i = 1; i <= letters.length; ++i ) {
                if ( this.grid.get(row - i).get(col) == letters[i - 1] ) {
                    ++verticalCount;
                }
            }

            // Checking for Matches
            if ( verticalCount == letters.length ) {
                return 1;
            }
        }
        return 0;
    }

    private int checkLowerVertical( int row, int col ) {
        char[] letters = {'M', 'A', 'S'};
        // Lower Restrictions
        if ( row + letters.length < max_row ) {

            int verticalCount = 0;

            for ( int i = 1; i <= letters.length; ++i ) {
                if ( this.grid.get(row + i).get(col) == letters[i - 1] ) {
                    ++verticalCount;
                }
            }

            // Checking for Matches
            if ( verticalCount == letters.length ) {
                return 1;
            }
        }
        return 0;
    }

    private int valid_XMAS( int row, int col ) {
        // Original Format
        char topLeft = 'M';
        char topRight = 'S';
        char bottomLeft = 'M';
        char bottomRight = 'S';

        for ( int i = 0; i < 4; ++i ) {
            System.out.println(topLeft);
            int cur_count = 0;
            // Upper Restriction
            if (row - 1 >= 0) {

                boolean upperLeft = col - 1 >= 0 && this.grid.get(row - 1).get(col - 1) == topLeft;
                boolean upperRight = col + 1 < max_col && this.grid.get(row - 1).get(col + 1) == topRight;

                if (upperLeft && upperRight) {
                    ++cur_count;
                }
            }
            // Lower Restriction
            if (row + 1 < max_row) {

                // Forward Position
                boolean lowerLeft = col - 1 >= 0 && this.grid.get(row + 1).get(col - 1) == bottomLeft;
                boolean lowerRight = col + 1 < max_col && this.grid.get(row + 1).get(col + 1) == bottomRight;

                if (lowerLeft && lowerRight) {
                    ++cur_count;
                }
            }

            // Checking for Matches
            if ( cur_count == 2 ) {
                return 1;
            }
            // Swap the Values - CounterClockwise
            char temp = topLeft;
            topLeft = topRight;
            topRight = bottomRight;
            bottomRight = bottomLeft;
            bottomLeft = temp;
        }
        return 0;
    }


    private static String convertString( ArrayList<Character> arr ) {
        StringBuilder s = new StringBuilder();
        for ( Character c : arr ) {
            s.append(c);
        }
        return s.toString();
    }

    private static ArrayList<ArrayList<Character>> readFile() {
        String file_name = "/Users/tonychen/Documents/AdventOfCode/src/y2024/Day4/pzleinput.txt";

        ArrayList<ArrayList<Character>> new_grid = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<Character> temp = new ArrayList<>();
                for (int i = 0; i < line.length(); ++i) {
                    temp.add(line.charAt(i));
                }
                new_grid.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new_grid;
    }

}
