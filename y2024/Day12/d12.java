package y2024.Day12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class d12 {

    public long part1() {

        long score = 0;
        ArrayList<ArrayList<Character>> map = getMap();
        ArrayList<Character> cropList = getAllCrops( map );

        for ( char curCrop : cropList ) {

            int[] start;

            while (!Arrays.equals( start = findInstance(map, curCrop) , new int[]{map.size(), map.getFirst().size()} ) ) {

                score += computePrice( map, start, curCrop );
            }
        }

        return score;
    }

    private static int computePrice( ArrayList<ArrayList<Character>> map, int[] start_position, char crop ) {

        int area = 0;
        int perimeter = 0;
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        // Adding the Start Position to the path queue
        path.add( new ArrayList<>( Arrays.asList( start_position[0], start_position[1] ) ) );

        int[][] directions = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };

        while ( !path.isEmpty() ) {
            // Gets the Next Path Coordinates.
            ArrayList<Integer> curPos = path.getFirst();
            int curX = curPos.getFirst();
            int curY = curPos.getLast();

            path.removeFirst();
            // Removing the Crop to indicate that it has been visited
            replaceCell( map, curX, curY, crop );

            ++area;

            for ( int[] curDirection : directions ) {

                int newX = curX + curDirection[0];
                int newY = curY + curDirection[1];

                if ( validNextPath( map, newX, newY ) ) {

                    if ( map.get( newX ).get( newY ) == crop && !path.contains( new ArrayList<>( Arrays.asList( newX, newY ) ) ) ) {

                        path.add( new ArrayList<>( Arrays.asList( newX, newY ) ) );
                    }
                    else if ( map.get( newX ).get( newY ) != crop && map.get( newX ).get( newY ) != Character.toLowerCase( crop ) ) {

                        ++perimeter;
                    }
                    continue;
                }

                ++perimeter;
            }

        }

        return area * perimeter;
    }

    public static boolean validNextPath( ArrayList<ArrayList<Character>> map, int x, int y ) {

        boolean xBoundary = 0 <= x && x < map.size();
        boolean yBoundary = 0 <= y && y < map.getFirst().size();

        return xBoundary && yBoundary;
    }

    private static void replaceCell( ArrayList<ArrayList<Character>> map, int curX, int curY, char symbol ) {

        map.get( curX ).add( curY, Character.toLowerCase( symbol ) );
        map.get( curX ).remove( curY + 1 );
    }

    private static int[] findInstance( ArrayList<ArrayList<Character>> map, char symbol ) {

        for ( int r = 0; r < map.size(); ++r ) {

            for ( int c = 0; c < map.get( r ).size(); ++c ) {

                if ( map.get( r ).get( c ) == symbol ) {

                    return new int[] {r, c};
                }
            }
        }

        return new int[] {map.size(), map.getFirst().size()};
    }

    private static ArrayList<Character> getAllCrops( ArrayList<ArrayList<Character>> map ) {

        ArrayList<Character> cropList = new ArrayList<>();

        for ( ArrayList<Character> row : map ) {

            for ( char curCell : row ) {

                if ( !cropList.contains( curCell ) ) {

                    cropList.add( curCell );
                }
            }
        }

        return cropList;
    }

    private static ArrayList<ArrayList<Character>> getMap() {

        String file_name = "";
        ArrayList<ArrayList<Character>> map = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {

            String line;

            while ( ( line = br.readLine() ) != null ) {

                ArrayList<Character> row = new ArrayList<>();

                for ( char curCell : line.toCharArray() ) {

                    row.add( curCell );
                }

                map.add( row );
            }
        }
        catch( IOException e ) {

            e.printStackTrace();
        }

        return map;
    }
}
