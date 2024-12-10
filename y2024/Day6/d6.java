package y2024.Day6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class d6 {

    private final ArrayList<ArrayList<Character>> map;
    private final int[] max_limits;
    private final String[] DIRECTIONS = {"UP", "RIGHT", "DOWN", "LEFT"};
    private final int[] start_coordinates;

    public d6() {
        this.map = getMap();
        this.max_limits = new int[] { map.size(), map.getFirst().size() } ;
        this.start_coordinates = findStart( map );
    }

    public int part1() {
        boolean search = true;
        int total_covered = 0;

        int cur_direct = 0;
        int cur_x = start_coordinates[0];
        int cur_y = start_coordinates[1];

        while ( search ) {

            Character cur_cell = map.get( cur_x ).get( cur_y );

            if ( cur_cell == '#' ) {
                // Taking a Step Backwards
                if ( cur_direct == 0 ) { // DIRECTION: UP
                    ++cur_x;
                }
                else if ( cur_direct == 1 ) { // DIRECTION: RIGHT
                    --cur_y;
                }
                else if ( cur_direct == 2 ) { // DIRECTION: DOWN
                    --cur_x;
                }
                else if ( cur_direct == 3 ) { // DIRECTION: LEFT
                    ++cur_y;
                }
                // Changes Direction Accordingly
                cur_direct = ( cur_direct + 1 ) % DIRECTIONS.length;

            }
            else if ( cur_cell != 'X' ) {
                ++total_covered;
            }
            // Indicating a Visited Cell
            map.get( cur_x ).remove( cur_y );
            map.get( cur_x ).add( cur_y, 'X' );

            if ( continueMoving(max_limits, cur_x, cur_y, cur_direct ) ) {
                // UP
                if ( cur_direct == 0 ) {
                    --cur_x;
                }
                // RIGHT
                else if ( cur_direct == 1 ) {
                    ++cur_y;
                }
                // DOWN
                else if ( cur_direct == 2 ) {
                    ++cur_x;
                }
                // LEFT
                else if ( cur_direct == 3 ) {
                    --cur_y;
                }

            }
            else {
                search = false;
            }

        }

        return total_covered;
    }

    public int part2() {

        int total_obstructions = 0;
        Set<ArrayList<Integer>> path = computePath( map, start_coordinates );

        for ( ArrayList<Integer> cur_coordinates : path ) {

            if ( inLoop( getMap(), cur_coordinates, start_coordinates ) ) {
                ++total_obstructions;
            }
        }

        return total_obstructions;

    }

    private static boolean inLoop(ArrayList<ArrayList<Character>> grid, ArrayList<Integer> new_obstacleCoordinates, int[] start_coordinates ) {

        Set<ArrayList<Integer>> pathHistory = new HashSet<>();
        boolean search = true;
        int[] dimensions = new int[] { grid.size(), grid.getFirst().size() };

        grid.get(new_obstacleCoordinates.get(0)).add(new_obstacleCoordinates.get(1), '#' );
        grid.get(new_obstacleCoordinates.get(0)).remove( new_obstacleCoordinates.get(1) + 1 );

        int cur_x = start_coordinates[0];
        int cur_y = start_coordinates[1];
        int cur_direct = 0;

        while ( search ) {

            ArrayList<Integer> cur_cell = new ArrayList<>(Arrays.asList( cur_direct, cur_x, cur_y) );

            if ( pathHistory.contains( cur_cell ) ) {

                return true;

            }
            pathHistory.add( cur_cell );

            if ( continueMoving( dimensions, cur_x, cur_y, cur_direct ) ) {

                if ( isNextObstacle( grid, cur_x, cur_y, cur_direct ) ) {

                    cur_direct = ( cur_direct + 1 ) % 4;

                }
                else {
                    // UP
                    if ( cur_direct == 0 ) {
                        --cur_x;
                    }
                    // RIGHT
                    else if ( cur_direct == 1 ) {
                        ++cur_y;
                    }
                    // DOWN
                    else if ( cur_direct == 2 ) {
                        ++cur_x;
                    }
                    // LEFT
                    else if ( cur_direct == 3 ) {
                        --cur_y;
                    }

                }

            }
            else {
                search = false;
            }


        }

        return false;
    }

    private static Set<ArrayList<Integer>> computePath( ArrayList<ArrayList<Character>> grid, int[] start_coords) {

        Set<ArrayList<Integer>> visited = new HashSet<>();
        boolean search = true;
        int[] dimensions = new int[] { grid.size(), grid.getFirst().size() };

        int cur_x = start_coords[0];
        int cur_y = start_coords[1];
        int cur_direct = 0;

        while ( search ) {

            visited.add( new ArrayList<>( Arrays.asList( cur_x, cur_y ) ) );

            if ( continueMoving(dimensions, cur_x, cur_y, cur_direct) ) {

                if ( isNextObstacle( grid, cur_x, cur_y, cur_direct ) ) {

                    cur_direct = ( cur_direct + 1 ) % 4;

                }
                else {
                    // UP
                    if ( cur_direct == 0 ) {
                        --cur_x;
                    }
                    // RIGHT
                    else if ( cur_direct == 1 ) {
                        ++cur_y;
                    }
                    // DOWN
                    else if ( cur_direct == 2 ) {
                        ++cur_x;
                    }
                    // LEFT
                    else if ( cur_direct == 3 ) {
                        --cur_y;
                    }
                }

            }
            else {
                search = false;
            }

        }
        return visited;
    }

    private static boolean isNextObstacle(ArrayList<ArrayList<Character>> grid, int cur_x, int cur_y, int cur_direct ) {
        // UP
        if ( cur_direct == 0 ) {
            return grid.get( cur_x - 1 ).get( cur_y ) == '#';
        }
        // RIGHT
        else if ( cur_direct == 1 ) {
            return grid.get( cur_x ).get( cur_y + 1) == '#';
        }
        // DOWN
        else if ( cur_direct == 2 ) {
            return grid.get( cur_x + 1 ).get( cur_y ) == '#';
        }
        // LEFT
        return grid.get( cur_x ).get( cur_y - 1 ) == '#';
    }

    private static int[] findStart( ArrayList<ArrayList<Character>> map ) {
        int[] pair = {Integer.MAX_VALUE, Integer.MAX_VALUE};

        for ( int row = 0; row < map.size(); ++row ) {
            for ( int col = 0; col < map.get( row ).size(); ++col ) {

                Character cur_seq = map.get( row ).get( col );

                if ( cur_seq == '^' ) {
                    pair[0] = row;
                    pair[1] = col;
                    return pair;
                }

            }
        }

        return pair;
    }

    private static boolean continueMoving( int[] dimensions, int cur_x, int cur_y, int cur_direction ) {
        // UP
        if ( cur_direction == 0 ) {
            return cur_x - 1 >= 0;
        }
        // RIGHT
        else if ( cur_direction == 1 ) {
            return cur_y + 1 < dimensions[1];
        }
        // DOWN
        else if ( cur_direction == 2 ) {
            return cur_x + 1 < dimensions[0];
        }
        // LEFT
        return cur_y - 1 >= 0;
    }

    private static ArrayList<ArrayList<Character>> getMap() {

        String file_name = "";
        ArrayList<ArrayList<Character>> map = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file_name) ) ) {

            String line;
            int max_col = 0;

            while ( ( line = br.readLine() ) != null ) {

                if ( max_col == 0 ) {
                    max_col = line.length();
                }
                ArrayList<Character> temp = new ArrayList<>();

                for ( int i = 0; i < line.length(); ++i ) {
                    temp.add( line.charAt(i) );
                }

                map.add( temp );
            }

        }
        catch (IOException e) {

            e.printStackTrace();

        }

        return map;
    }

}
