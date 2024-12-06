/*
Not the most efficient solution for part2 lol
 */

package y2024.Day5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class d5 {

    private final HashMap<Integer, ArrayList<Integer>> restrictions;
    private final ArrayList<ArrayList<Integer>> sequences;

    public d5() {
        this.restrictions = getRestrictions();
        this.sequences = getSequences();

    }

    public int part1() {
        ArrayList<ArrayList<Integer>> valid_sequences = new ArrayList<>();

        for ( ArrayList<Integer> cur_sequence : sequences ) {

            ArrayList<int[]> keyPositions = getKeyPositions( cur_sequence );

            int i = 0;

            for (; i < cur_sequence.size(); ++i ) {

                int cur_val = cur_sequence.get(i);

                if ( validPlacement( keyPositions, i, cur_val ) != cur_val ) {
                    break;
                }
            }

            if ( i == cur_sequence.size() ) {
                valid_sequences.add( cur_sequence );
            }
        }

        return computeMiddle( valid_sequences );
    }

    public int part2() {
        ArrayList<ArrayList<Integer>> valid_sequences;
        ArrayList<ArrayList<Integer>> invalid_sequences = new ArrayList<>();

        for ( ArrayList<Integer> cur_sequence : sequences ) {

            ArrayList<int[]> keyPositions = getKeyPositions( cur_sequence );
            int i = 0;

            for (; i < cur_sequence.size(); ++i ) {
                int cur_val = cur_sequence.get(i);
                if (validPlacement(keyPositions, i, cur_val) != cur_val){
                    break;
                }

            }
            if ( i < cur_sequence.size() ) {
                invalid_sequences.add( cur_sequence );
            }
        }
        valid_sequences = convertValidSequences( invalid_sequences );
        return computeMiddle( valid_sequences );
    }

    private ArrayList<ArrayList<Integer>> convertValidSequences( ArrayList<ArrayList<Integer>> invalid_sequences ) {
        ArrayList<ArrayList<Integer>> refined_sequences = new ArrayList<>();

        for ( ArrayList<Integer> cur_sequence : invalid_sequences ) {

            ArrayList<Integer> lst = new ArrayList<>();
            lst.add( cur_sequence.getFirst() );

            // Uses an Insertion Sort Idea
            for ( int i = 1; i < cur_sequence.size(); ++i ) {

                int cur_element = cur_sequence.get( i );
                int j = 0;

                while ( j < lst.size() ) {

                    if ( restrictions.get( lst.get(j) ).contains( cur_element ) ) {
                        lst.add( j, cur_element );
                        break;
                    }
                    ++j;

                }
                if ( j == lst.size() ) {
                    lst.add( cur_element );
                }

            }

            refined_sequences.add(lst);

        }
        return refined_sequences;
    }

    private ArrayList<int[]> getKeyPositions( ArrayList<Integer> seq ) {
        ArrayList<int[]> positions = new ArrayList<>();
        for ( int i = 0; i < seq.size(); ++i ) {
            if ( restrictions.containsKey( seq.get(i) ) ) {
                int[] temp = {i, seq.get(i)};
                positions.add( temp );
            }
        }
        return positions;
    }

    private int validPlacement( ArrayList<int[]> keyPositions, int cur_ind, int cur_val ) {
        for ( int[] pair : keyPositions ) {

            int keyInd = pair[0];
            int key = pair[1];
            
            if ( keyInd > cur_ind && restrictions.get(key).contains( cur_val ) ) {
                return keyInd;
            }
        }
        return cur_val;
    }

    private static int computeMiddle( ArrayList<ArrayList<Integer>> sequences ) {
        int total = 0;
        for ( ArrayList<Integer> seq : sequences ) {

            int middle = seq.size() / 2;

            total += seq.get(middle);
        }
        return total;
    }

    private static HashMap<Integer, ArrayList<Integer>> getRestrictions() {
        String file_name = "/Users/tonychen/Documents/AdventOfCode/src/y2024/Day5/pzleinput.txt";

        HashMap<Integer, ArrayList<Integer>> restrictions = new HashMap<>();

        try ( BufferedReader br = new BufferedReader( new FileReader( file_name) ) ) {
            String line;
            while ( ( line = br.readLine() ) != null ) {
                if ( line.isBlank() ) {
                    break;
                }
                String[] data = line.split("\\|");
                if ( !restrictions.containsKey( Integer.parseInt( data[0] ) ) ) {

                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add( Integer.parseInt( data[1] ) );

                    restrictions.put( Integer.parseInt(data[0] ), temp );
                }
                else {
                    restrictions.get( Integer.parseInt( data[0] ) ).add( Integer.parseInt( data[1] ) );
                }
            }
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }

        return restrictions;
    }

    private static ArrayList<ArrayList<Integer>> getSequences() {
        String file_name = "";

        ArrayList<ArrayList<Integer>> collection = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file_name)) ) {
            String line;
            boolean begin = false;
            while ( ( line = br.readLine() ) != null ) {
                if ( line.isBlank() ) {
                    begin = true;
                }
                else if ( begin ) {
                    ArrayList<Integer> sequence = new ArrayList<>();
                    for ( String c : line.split(",") ) {
                        sequence.add( Integer.parseInt(c) );
                    }
                    collection.add(sequence);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return collection;
    }

}
