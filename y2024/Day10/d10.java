package y2024.Day10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class d10 {

    private final ArrayList<ArrayList<Integer>> map;

    public d10() {
        this.map = getMap();
    }

    public int part1() {

        int numPaths = 0;
        ArrayList<ArrayList<Integer>> bottomLocations = extractBottom( map );

        for ( ArrayList<Integer> cur_location : bottomLocations ) {

            int score = computePaths( map, cur_location );
            numPaths += score;
        }

        return numPaths;
    }

    public int part2() {

        int numPaths = 0;
        ArrayList<ArrayList<Integer>> bottomLocations = extractBottom( map );

        for ( ArrayList<Integer> cur_location : bottomLocations ) {

            int score = computePathRatings( map, cur_location );
            numPaths += score;

        }

        return numPaths;
    }

    private static int computePathRatings( ArrayList<ArrayList<Integer>> map, ArrayList<Integer> start_pos ) {
        // The Path Queue Contents will be ordered in the following: Cur_Height, cur_x, cur_y
        start_pos.addFirst( 0 );
        int[][] possibleDirections = { {0, 1}, {0, -1}, {1, 0}, {-1, 0} };

        int viablePaths = 0;
        ArrayList<ArrayList<Integer>> pathQueue = new ArrayList<>(Collections.singletonList( start_pos ) );

        while (!pathQueue.isEmpty()) {

            ArrayList<Integer> curPackage = pathQueue.getFirst();
            pathQueue.removeFirst();
            int curHeight = curPackage.getFirst();
            int curRow = curPackage.get( 1 );
            int curCol = curPackage.getLast();
            // If the Current Position is already at the Peak
            if ( curHeight == 9 ) {

                ++viablePaths;
                continue;
            }

            for ( int[] curDirection : possibleDirections ) {

                if ( validAscension( map, curRow + curDirection[0], curCol + curDirection[1], curHeight ) ) {

                    ArrayList<Integer> newPackage = new ArrayList<>(Arrays.asList( curHeight + 1, curRow + curDirection[0], curCol + curDirection[1] ) );
                    pathQueue.add( newPackage );
                }
            }

        }

        return viablePaths;
    }

    private static int computePaths( ArrayList<ArrayList<Integer>> map, ArrayList<Integer> start_pos ) {
        // The Path Queue Contents will be ordered in the following: Cur_Height, cur_x, cur_y
        start_pos.addFirst( 0 );
        int[][] possibleDirections = { {0, 1}, {0, -1}, {1, 0}, {-1, 0} };

        int viablePaths = 0;
        ArrayList<ArrayList<Integer>> pathQueue = new ArrayList<>(Collections.singletonList( start_pos ) );

        while (!pathQueue.isEmpty()) {

            ArrayList<Integer> curPackage = pathQueue.getFirst();
            pathQueue.removeFirst();
            int curHeight = curPackage.getFirst();
            int curRow = curPackage.get( 1 );
            int curCol = curPackage.getLast();
            // If the Current Position is already at the Peak
            if ( curHeight == 9 ) {

                ++viablePaths;
                continue;
            }

            for ( int[] curDirection : possibleDirections ) {

                if ( validAscension( map, curRow + curDirection[0], curCol + curDirection[1], curHeight ) ) {

                    ArrayList<Integer> newPackage = new ArrayList<>(Arrays.asList( curHeight + 1, curRow + curDirection[0], curCol + curDirection[1] ) );
                    if ( !pathQueue.contains( newPackage ) ) {

                        pathQueue.add( newPackage );
                    }
                }
            }

        }

        return viablePaths;
    }

    private static boolean validAscension( ArrayList<ArrayList<Integer>> map, int x, int y, int height ) {

        boolean withinRowRestrictions = 0 <= x && x < map.size();
        boolean withinColRestrictions = 0 <= y && y < map.getFirst().size();

        if ( withinColRestrictions && withinRowRestrictions ) {

            return height + 1 == map.get( x ).get( y );
        }
        return false;
    }

    private static ArrayList<ArrayList<Integer>> extractBottom( ArrayList<ArrayList<Integer>> map ) {
        // Array that will store all the locations of the bottom ('0')
        ArrayList<ArrayList<Integer>> locations = new ArrayList<>();
        // Method: Check each cell and if '0', add their location to the array
        for ( int r = 0; r < map.size(); ++r ) {

            for ( int c = 0; c < map.get( r ).size(); ++c ) {

                if ( map.get( r ).get( c ) == 0 ) {

                    locations.add( new ArrayList<>(Arrays.asList( r, c ) ) );
                }
            }
        }

        return locations;
    }

    private static ArrayList<ArrayList<Integer>> getMap() {

        String file_name = "/Users/tonychen/Documents/AdventofCode/src/y2024/Day10/pzleinput.txt";
        ArrayList<ArrayList<Integer>> map = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {

            String line;

            while ( ( line = br.readLine() ) != null ) {

                ArrayList<Integer> row = new ArrayList<>();
                for ( char c : line.toCharArray() ) {

                    row.add( c - '0' );
                }

                map.add( row );
            }
        }
        catch (IOException e ) {
            e.printStackTrace();
        }
        return map;
    }
}
