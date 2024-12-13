package y2024.Day9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class d9 {

    private ArrayList<Integer> sequence;

    public d9() {
        this.sequence = getSequence();
    }

    public long part1() {

        ArrayList<ArrayList<String>> file_struct = convertDiskMap( sequence );

        compressDisk( file_struct );

        return computeChecksum( file_struct );
    }

    public long part2() {

        this.sequence = getSequence(); // RESET SEQUENCE
        ArrayList<ArrayList<String>> file_struct = convertDiskMap( sequence );
        HashMap<Integer, ArrayList<ArrayList<Integer>>> freeSpace = locateFreeSpace( sequence );

        for ( int cur_ind = file_struct.size() - 1; cur_ind >= 0; --cur_ind ) {

            ArrayList<Integer> fileStorage = nextFileStorage( file_struct, cur_ind );
            int l_fileStorage = fileStorage.getLast() - fileStorage.getFirst();

            // There exists a File to be moved
            if ( l_fileStorage > 0 ) {

                ArrayList<Integer> possibleFreeSpace = extractPossibleFreeSpace( freeSpace, l_fileStorage );

                if ( possibleFreeSpace.getLast() - 1 < fileStorage.getFirst() ) {

                    swap( file_struct, fileStorage, possibleFreeSpace );
                    updateFreeSpace( freeSpace, possibleFreeSpace.getLast() - possibleFreeSpace.getFirst(), l_fileStorage );
                }

            }

            cur_ind = fileStorage.getFirst();

        }

        return computeChecksum( file_struct );
    }

    private static void compressDisk( ArrayList<ArrayList<String>> file_struct ) {

        int p_front = 0;
        int p_back = file_struct.size() - 1;

        while ( p_front < p_back) {

            String frontElement = file_struct.get( p_front ).getFirst();
            String backElement = file_struct.get( p_back ).getFirst();

            if (!Objects.equals(frontElement, ".")) {

                while ( p_front < file_struct.size() && !Objects.equals(file_struct.get(p_front).getFirst(), ".")) {
                    ++p_front;
                }

            }

            if (Objects.equals(backElement, ".")) {

                while ( p_back >= 0 && Objects.equals(file_struct.get(p_back).getLast(), ".")) {
                    --p_back;
                }

            }
            if ( p_back >= 0 && p_front < file_struct.size() ) {
                Collections.swap(file_struct, p_front, p_back);
            }
        }
        if ( p_back >= 0 && p_front < file_struct.size() ) {
            Collections.swap(file_struct, p_front, p_back);
        }

    }

    private static void updateFreeSpace( HashMap<Integer, ArrayList<ArrayList<Integer>>> map, int theory, int actual ) {

        ArrayList<Integer> modified_position = map.get( theory ).getFirst();
        // Removes Original Entry from the Map
        map.get( theory ).removeFirst();
        // Modifies the Entry based on the Actual Length
        if ( actual < theory ) { // Reduces the Original Entry Coords

            int new_value = modified_position.getLast() - ( theory - actual );
            // Remove Old Entry
            modified_position.removeFirst();
            // Adds Updated Entry
            modified_position.addFirst( new_value );
            // Finds a new Key to Live Under
            if ( map.containsKey( theory - actual ) ) {
                // If the Key already exists, Place it in the right order, based ascending
                for ( int i = 0; i < map.get( theory - actual ).size(); ++i ) {

                    if ( modified_position.getFirst() < map.get( theory - actual ).get( i ).getFirst() ) {
                        // Adds Entry in the Proper Position
                        map.get( theory - actual ).add( i, modified_position );
                        break;
                    }
                }
            }
            else {
                // Creates a new Entry for the Map
                map.put(theory - actual, new ArrayList<>(Collections.singletonList(modified_position)));
            }
        }
    }

    private static ArrayList<Integer> extractPossibleFreeSpace( HashMap<Integer, ArrayList<ArrayList<Integer>>> map, int length ) {

        ArrayList<Integer> lowest = new ArrayList<>( Arrays.asList( Integer.MAX_VALUE, Integer.MAX_VALUE ) );

        for ( int key : map.keySet() ) {

            if ( key >= length && !map.get( key ).isEmpty() ) {

                ArrayList<Integer> cur_lowest = map.get( key ).getFirst();

                if ( cur_lowest.getFirst() < lowest.getFirst() ) {

                    lowest = cur_lowest;
                }
            }
        }

        return lowest;
    }

    private static void swap( ArrayList<ArrayList<String>> file_struct, ArrayList<Integer> file_pos, ArrayList<Integer> space_pos ) {

        for ( int i = 0; i < file_pos.getLast() - file_pos.getFirst(); ++i ) {

            Collections.swap( file_struct, file_pos.getFirst() + i, space_pos.getFirst() + i );
        }
    }

    private static ArrayList<Integer> nextFileStorage( ArrayList<ArrayList<String>> file_struct, int init_pos ) {

        if ( Objects.equals( file_struct.get( init_pos ).getFirst(), "." ) ) {
            return new ArrayList<>( Arrays.asList( init_pos, init_pos ) );
        }

        int len = 0;
        while ( init_pos - len >= 0 && Objects.equals( file_struct.get( init_pos - len ), file_struct.get( init_pos ) ) ) {
            ++len;
        }

        return new ArrayList<>( Arrays.asList( init_pos - len + 1, init_pos + 1 ) );
    }

    private static HashMap<Integer, ArrayList<ArrayList<Integer>>> locateFreeSpace( ArrayList<Integer> diskMap ) {

        int cur_ind = 0;
        HashMap<Integer, ArrayList<ArrayList<Integer>>> freeSpace = new HashMap<>();

        for ( int i = 0; i < diskMap.size(); ++i ) {

            int cur_num = diskMap.get( i );
            // For Spacing
            if ( i % 2 != 0 && cur_num != 0 ) {

                ArrayList<Integer> range = new ArrayList<>( Arrays.asList( cur_ind, cur_ind + cur_num ) );

                // Checking if a key of exact size exists
                if ( !freeSpace.containsKey( cur_num ) ) {

                    freeSpace.put( cur_num, new ArrayList<>( Collections.singletonList( range ) ) );
                }
                else {
                    // Sorting by Ascending Order
                    ArrayList<ArrayList<Integer>> arr = freeSpace.get( cur_num );
                    int j = 0;

                    for ( ; j < arr.size(); ++j ) {

                        if ( range.getFirst() < arr.get( j ).getFirst() ) {
                            arr.add( j, range );
                            break;
                        }

                    }
                    // Checked all sequences, thrown onto the back.
                    if ( j == arr.size() ) {
                        arr.add( range );
                    }

                }

            }

            cur_ind += cur_num;
        }

        return freeSpace;
    }

    private static long computeChecksum( ArrayList<ArrayList<String>> file_struct) {

        long checksum = 0L;
        int id = 0;

        while (id < file_struct.size() )  {

            if ( !Objects.equals(file_struct.get(id).getFirst(), "." ) ) {

                checksum += Long.parseLong(file_struct.get(id).getFirst()) * id;
            }
            ++id;

        }

        return checksum;
    }

    private static ArrayList<ArrayList<String>> convertDiskMap(ArrayList<Integer> diskMap ) {

        Integer id = 0;
        boolean toggle = true;
        ArrayList<ArrayList<String>> file = new ArrayList<>();

        for ( int cur_element : diskMap ) {

            for ( int i = 0; i < cur_element; ++i ) {

                if ( toggle ) {

                    String c = id.toString();
                    file.add( new ArrayList<>( List.of( c ) ) );

                }
                else {
                    file.add( new ArrayList<>( List.of( "." ) ) );
                }

            }

            toggle = !toggle;
            if ( !toggle ) {
                ++id;
            }

        }

        return file;

    }

    private static ArrayList<Integer> getSequence() {

        String file_name = "";
        ArrayList<Integer> sequence = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {

            String line;

            while ( ( line = br.readLine() ) != null ) {

                for ( char c : line.toCharArray() ) {
                    sequence.add(c - '0');
                }

            }
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }

        return sequence;
    }
}
