package y2024.Day11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class d11 {

    public long part1() {

        ArrayList<String> stones = getSequence();
        HashMap<String, Long> stonesList = convertTracking( stones );
        int numOfBlinks = 25;

        for ( int i = 0; i < numOfBlinks; ++i ) {

            blinkStone( stonesList );
        }

        return countValues( stonesList );
    }

    public long part2() {

        ArrayList<String> stones = getSequence();
        HashMap<String, Long> stonesList = convertTracking( stones );
        int numOfBlinks = 75;

        for ( int i = 0; i < numOfBlinks; ++i ) {

            blinkStone( stonesList );
        }

        return countValues( stonesList );
    }

    private static void blinkStone( HashMap<String, Long> oldStoneList ) {

        HashMap<String, Long> newStoneList = new HashMap<>();

        for ( String curKey : oldStoneList.keySet() ) {

            long numOfOccurrence = oldStoneList.get( curKey );

            if ( Objects.equals( curKey, "0" ) ) {

                newStoneList.put( "1", newStoneList.getOrDefault( "1", 0L ) + numOfOccurrence );
            }
            else if ( curKey.length() % 2 == 0 ) {

                ArrayList<String> twoStones = splitStone( curKey );
                // Adding Each stone into the List
                newStoneList.put( twoStones.getFirst(), newStoneList.getOrDefault( twoStones.getFirst(), 0L ) + numOfOccurrence );
                newStoneList.put( twoStones.getLast(), newStoneList.getOrDefault( twoStones.getLast(), 0L ) + numOfOccurrence );
            }
            else {

                Long temp = Long.parseLong( curKey ) * 2024;
                newStoneList.put( temp.toString(), newStoneList.getOrDefault( temp.toString(), 0L ) + numOfOccurrence );
            }

        }
        // Overwrites the old with the new
        oldStoneList.clear();
        mergeContent( oldStoneList, newStoneList );
    }

    private static HashMap<String, Long> convertTracking( ArrayList<String> sequence ) {

        HashMap<String, Long> tracking = new HashMap<>();

        for ( String curElement : sequence ) {

            tracking.put( curElement, tracking.getOrDefault( curElement, 0L ) + 1 );
        }

        return tracking;
    }

    private static void mergeContent( HashMap<String, Long> original, HashMap<String, Long> update ) {

        for ( String updateKey : update.keySet() ) {

            if (original.containsKey( updateKey ) ) {

                original.put( updateKey, value( original, updateKey ) + update.get( updateKey ) );
            }
            else {

                original.put( updateKey, update.get( updateKey ) );
            }
        }
    }

    private static long countValues( HashMap<String, Long> map ) {

        long count = 0L;

        for ( Long ele : map.values() ) {

            count += ele;
        }

        return count;
    }

    private static long value( HashMap<String, Long> count, String curKey ) {

        if ( count.get( curKey ) == null ) {

            return 0L;
        }

        return count.get( curKey );
    }

    private static ArrayList<String> splitStone( String stone ) {

        int centrePoint = stone.length() / 2;
        String upperHalf = stone.substring( 0, centrePoint );
        String lowerHalf = stone.substring( centrePoint );
        // Removing Redundancy, if there exists any
        Long temp = Long.parseLong( lowerHalf );
        lowerHalf = temp.toString();

        return new ArrayList<>( Arrays.asList( upperHalf, lowerHalf ) );
    }


    private static ArrayList<String> getSequence() {

        String file_name = "";
        ArrayList<String> sequence = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {

            String line;

            while ( ( line = br.readLine() ) != null ) {

                sequence.addAll(Arrays.asList(line.split(" ")));
            }
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }

        return sequence;
    }
}
