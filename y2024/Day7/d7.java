package y2024.Day7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class d7 {

    private HashMap<Long, ArrayList<Long>> calibrations;

    public d7() {
        this.calibrations = getCalibrations();
    }

    public void resetCalibrations() {
        this.calibrations = getCalibrations();
    }

    public long part1() {

        long sumCalibrationValues = 0L;

        for ( Long key : calibrations.keySet() ) {

            if ( validCalibration_TwoOperators( key ) ) {
                sumCalibrationValues += key;
            }

        }

        return sumCalibrationValues;
    }
    // Not the most Optimal Solution but gets the job done
    public long part2() {

        long sumCalibrationValues = 0L;

        for ( Long key : calibrations.keySet() ) {

            if ( validCalibration_ThreeOperators( key ) ) {
                sumCalibrationValues += key;
            }
        }

        return sumCalibrationValues;

    }

    private boolean validCalibration_TwoOperators( Long key ) {

        ArrayList<Long> uncheck_nums = calibrations.get( key );
        ArrayList<Long> checked_nums = new ArrayList<>( Collections.singletonList( uncheck_nums.getFirst() ) );
        uncheck_nums.removeFirst();

        for ( Long num : computeNum_DoubleOperators( checked_nums, uncheck_nums) ) {
            if ( Objects.equals(num, key) ) {
                return true;
            }
        }
        return false;
    }

    private boolean validCalibration_ThreeOperators( Long key ) {

        ArrayList<Long> unchecked_nums = calibrations.get( key );
        ArrayList<String> checked_nums = new ArrayList<>( Collections.singletonList( unchecked_nums.getFirst().toString() ) );
        unchecked_nums.removeFirst();

        for ( String equation : computeNum_TripleOperators( checked_nums, unchecked_nums ) ) {

            if ( Objects.equals( key, decodeString( equation ) ) ) {
                return true;
            }

        }
        return false;
    }

    private static ArrayList<Long> computeNum_DoubleOperators( ArrayList<Long> used_num, ArrayList<Long> remaining_num ) {

        while ( !remaining_num.isEmpty() ) {

            Long cur_long = remaining_num.getFirst();
            remaining_num.removeFirst();

            int cur_size = used_num.size();

            for ( int i = 0; i < cur_size; ++i ) {

                Long sel = used_num.getFirst();

                used_num.add( cur_long + sel );
                used_num.add( cur_long * sel );

                used_num.removeFirst();

            }

        }

        return used_num;
    }

    private static ArrayList<String> computeNum_TripleOperators( ArrayList<String> used_num, ArrayList<Long> remaining_num ) {

        while ( !remaining_num.isEmpty() ) {

            String cur_string = remaining_num.getFirst().toString();
            remaining_num.removeFirst();

            int cur_size = used_num.size();

            for ( int i = 0; i < cur_size; ++i ) {

                String sel = used_num.getFirst();

                used_num.add( sel + "+" + cur_string );
                used_num.add( sel + "*" + cur_string );
                used_num.add( sel + "|" + cur_string );

                used_num.removeFirst();
            }
        }

        return used_num;
    }

    private static long decodeString( String code ) {

        long codedValue = Long.MAX_VALUE;
        ArrayList<Character> operations = new ArrayList<>( Arrays.asList( '|', '+', '*' ) );
        int prev_symbol = -1;
        int cur_pos = 0;

        while ( cur_pos < code.length() ) {

            char cur_char = code.charAt( cur_pos );

            if ( operations.contains( cur_char ) ) {

                long num = Long.parseLong( code.substring( prev_symbol + 1, cur_pos ) );

                if ( Objects.equals( codedValue, Long.MAX_VALUE ) ) {
                    codedValue = num;
                }
                else if ( code.charAt( prev_symbol ) == '|' ) {
                    codedValue = Long.parseLong( Long.toString(codedValue) + Long.toString(num));
                }
                else if ( code.charAt( prev_symbol ) == '+' ) {
                    codedValue += num;
                }
                else if ( code.charAt( prev_symbol ) == '*' ) {
                    codedValue *= num;
                }

                prev_symbol = cur_pos;

            }

            ++cur_pos;
        }
        // Extracting Final Number
        long final_num = Long.parseLong( code.substring( prev_symbol + 1, cur_pos ) );

        if ( code.charAt( prev_symbol ) == '|' ) {
            codedValue = Long.parseLong( Long.toString(codedValue) + Long.toString(final_num));
        }
        else if ( code.charAt( prev_symbol ) == '+' ) {
            codedValue += final_num;
        }
        else if ( code.charAt( prev_symbol ) == '*' ) {
            codedValue *= final_num;
        }

        return codedValue;
    }

    private static HashMap<Long, ArrayList<Long>> getCalibrations() {

        String file_name = "";
        HashMap<Long, ArrayList<Long>> calibrations = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file_name) ) ) {

            String line;

            while ( ( line = br.readLine() ) != null ) {

                String[] spliced_line = line.split(":");
                Long key = Long.parseLong( spliced_line[0] );
                ArrayList<Long> values = new ArrayList<>();

                for ( String element : spliced_line[1].split(" ") ) {

                    if ( !element.isBlank() ) {
                        values.add(Long.parseLong(element));
                    }

                }

                calibrations.put( key, values );
            }

        }
        catch ( IOException e ) {
            e.printStackTrace();
        }

        return calibrations;

    }

}
