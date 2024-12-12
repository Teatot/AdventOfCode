package y2024.Day8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class d8 {

    private ArrayList<ArrayList<Character>> map;

    public d8() {
        this.map = getMap();
    }

    public int part1() {

        int antiNodeCount = 0;
        HashMap<Character, ArrayList<ArrayList<Integer>>> nodeFrequencyPositions = extractNodePositions( getMap() );

        for ( Character cur_node : nodeFrequencyPositions.keySet() ) {

            antiNodeCount += placeAntiNode( nodeFrequencyPositions.get( cur_node ) );

        }

        return antiNodeCount;
    }

    public int part2() {

        this.map = getMap();  // RESETS THE MAP
        int antiNodeCount = 0;
        HashMap<Character, ArrayList<ArrayList<Integer>>> nodeFrequencyPositions = extractNodePositions( map );

        for ( Character cur_node : nodeFrequencyPositions.keySet() ) {

            antiNodeCount += placeContinuousAntiNode( nodeFrequencyPositions.get( cur_node ) );
        }

        return antiNodeCount;
    }

    private int placeContinuousAntiNode(ArrayList<ArrayList<Integer>> locations ) {

        int local_amount = 0;

        for ( int i = 0; i < locations.size(); ++i ) {

            int selectedX = locations.get( i ).getFirst();
            int selectedY = locations.get( i ).getLast();

            for ( int j = i + 1; j < locations.size(); ++j ) {

                int curX = locations.get( j ).getFirst();
                int curY = locations.get( j ).getLast();

                int curSel_x = selectedX;
                int curSel_y = selectedY;

                int deltaX = curX - selectedX;
                int deltaY = curY - selectedY;

                int factor = deltaX >= 0 ? 1 : -1;

                // Dealing with One Side of the AntiNodes
                deltaX *= factor;
                deltaY *= factor;

                while ( withinBoundaries( map, curX, curY ) ) {

                    if ( map.get( curX ).get( curY ) != '#' ) {

                        ++local_amount;

                        replaceElement( map, curX, curY, '#');

                    }

                    curX += deltaX;
                    curY += deltaY;

                }

                // Dealing with the Other Side of the AntiNodes
                while ( withinBoundaries( map, curSel_x, curSel_y ) ) {

                    if ( map.get( curSel_x ).get( curSel_y ) != '#' ) {

                        ++local_amount;

                        replaceElement( map, curSel_x, curSel_y, '#' );

                    }

                    curSel_x -= deltaX;
                    curSel_y -= deltaY;

                }

            }
        }

        return local_amount;
    }

    private int placeAntiNode( ArrayList<ArrayList<Integer>> locations ) {

        int local_amount = 0;

        for ( int i = 0; i < locations.size(); ++i ) {

            int selectedX = locations.get( i ).getFirst();
            int selectedY = locations.get( i ).getLast();

            for ( int j = i + 1; j < locations.size(); ++j ) {

                int curX = locations.get( j ).getFirst();
                int curY = locations.get( j ).getLast();

                int deltaX = curX - selectedX;
                int deltaY = curY - selectedY;

                boolean curInFront = deltaX >= 0;
                int factor = curInFront ? 1 : -1;
                // ANTI-Node 1
                int run = curX + ( factor * deltaX );
                int raise = curY + ( factor * deltaY );

                if ( withinBoundaries( map, run , raise ) && ( map.get( run ).get( raise ) == '.' || map.get( run ).get( raise ) != '#' ) ) {
                    local_amount += 1;
                    map.get( run ).add( raise, '#' );
                    map.get( run ).remove( raise + 1);
                }
                // ANTI-Node 2
                run = selectedX - ( factor * deltaX );
                raise = selectedY - ( factor * deltaY );

                if ( withinBoundaries( map, run , raise ) && ( map.get( run ).get( raise ) == '.' || map.get( run ).get( raise ) != '#' ) ) {
                    local_amount += 1;
                    map.get( run ).add( raise, '#' );
                    map.get( run ).remove( raise + 1);
                }

            }

        }

        return local_amount;
    }

    private static boolean withinBoundaries( ArrayList<ArrayList<Character>> map,  int run, int raise ) {
        boolean row_boundary = 0 <= run && run < map.size();
        boolean column_boundary = 0 <= raise && raise < map.getFirst().size();
        return row_boundary &&  column_boundary;
    }

    private static HashMap<Character, ArrayList<ArrayList<Integer>>> extractNodePositions( ArrayList<ArrayList<Character>> map ) {

        HashMap<Character, ArrayList<ArrayList<Integer>>> nodes = new HashMap<>();

        for ( int r = 0; r < map.size(); ++r ) {

            for ( int c = 0; c < map.get(r).size(); ++c ) {

                Character cur_cell = map.get(r).get(c);

                if ( cur_cell == '.' ) {
                    continue;
                }

                if ( nodes.containsKey( cur_cell ) ) {
                    nodes.get( cur_cell ).add( new ArrayList<>( Arrays.asList( r, c ) ) );
                }
                else {
                    nodes.put( cur_cell, new ArrayList<>( List.of( new ArrayList<>(Arrays.asList( r, c ) ) ) ) );
                }
            }

        }

        return nodes;
    }

    private static void replaceElement( ArrayList<ArrayList<Character>> map, int x, int y, char c ) {
        map.get( x ).add( y, c );
        map.get( x ).remove( y + 1 );
    }

    private static ArrayList<ArrayList<Character>> getMap() {

        String file_name = "";
        ArrayList<ArrayList<Character>> map = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file_name) ) ) {

            String line;

            while ( ( line = br.readLine() ) != null ) {

                ArrayList<Character> row = new ArrayList<>();

                for ( int i = 0; i < line.length(); ++i ) {
                    row.add( line.charAt( i ) );
                }

                map.add( row );
            }
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }

        return map;
    }
}
