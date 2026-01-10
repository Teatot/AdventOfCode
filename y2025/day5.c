#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>  
#include <fcntl.h>   

#define BUF_SIZE 4096
#define MAX_RANGES 500
#define MAX_VALUES 1000

typedef struct {
    int range_size; 
    int values_size;
    long range[MAX_RANGES];
    long values[MAX_VALUES];

} data_t; 

int main( int argc, char** argv ); 
int merge( long* arr_ranges, int* size, const char* range );
void part1( data_t input, long* output ); 
void part2( data_t input, long* output ); 
int isOverlap( long* arr_ranges, int size, int init, long start, long end ); 
void mergeElements( long* arr_ranges, int* size, int new_pos, int old_pos ); 


int main( int argc, char** argv ) {

    const char* fname = "./inputs/day5.txt";

    FILE* fp = fopen(fname, "r"); 
    if ( fp == NULL ) {
        perror("File DNE."); 
        return 1; 
    }

    /* Buffer Creation */
    char* buf = malloc(BUF_SIZE); 
    memset( buf, 0, BUF_SIZE ); 

    /* Data Buffer Struct Creation */
    data_t input; 
    input.range_size = 0; 
    input.values_size = 0; 

    /* Data Extraction */

    int status_val = 0; 

    while ( fgets( buf, BUF_SIZE, fp) != NULL ) {

        char* token = strtok( buf, "\n" ); /* Removing the newline */
        
        /* Empty Line Signifies Transistion */
        if ( !token ) {
            status_val = 1; 
            continue;
        } 

        /* Fetching the Ranges */
        if ( !status_val ) {
            
            int status_merge = merge( input.range, &input.range_size, token ); 
        }
        /* Fetching the Values */
        else {
            input.values[input.values_size] = atol(token); 
            input.values_size++; 
        }

    }

    /* Buffer Clean Up */
    free( buf ); 
    fclose(fp); 

    /* -- Algorithmn Section -- */
    long count[2];
    count[0] = 0; 
    count[1] = 0;  

    part1( input, count ); 
    part2( input, count + 1 ); 


    /* Output */
    printf("DAY 1 RESULT: %ld\n", count[0] ); 
    printf("DAY 2 RESULT: %ld\n", count[1] ); 

    return 0; 
}

void part1( data_t input, long* output ) {

    for ( int i = 0; i < input.values_size; i++ ) {

        long cur_val = input.values[i]; 

        /* Comparing the Value to Each Range */
        for ( int j = 0; j < 2*input.range_size; j += 2 ) {

            if ( input.range[j] <= cur_val && input.range[j + 1] >= cur_val ) {

                (*output)++; 
                break; 
            }
        }
    }
    return;
}


void part2( data_t input, long* output ) {

    int ptr = 0; 

    while ( ptr < 2*input.range_size ) {

        long cur_start = input.range[ptr]; 
        long cur_end = input.range[ptr + 1];

        int select; 

        if ( ( select = isOverlap( input.range, input.range_size, ptr, cur_start, cur_end ) ) != -1 ) {
            mergeElements( input.range, &input.range_size, select, ptr );
        }
        else {
            ptr += 2;
        } 
    }

    for ( int i = 0; i < 2*input.range_size; i+=2 ) {
        *output += (input.range[i + 1] - input.range[i]) + 1;
    }

    return; 
}


int merge( long* arr_ranges, int* size, const char* range ) {

    char* token = strtok((char*) range, "-" );
    long start = atol(token); 
    long end = atol(strtok( NULL, "-" ) ); 

    int ptr = 0;

    /* Checking Each Ranges to See if we can Extend a current one before
        expanding 
    */
    for ( ;ptr < 2*(*size); ptr += 2 ) {
    
        /* Range Does not Overlap */
        if ( arr_ranges[ptr + 1] < start || arr_ranges[ptr] > end ) continue;
        
        /* Case 1: Lower Start */
        if ( arr_ranges[ptr] > start && arr_ranges[ptr + 1] >= end ) {
            
            arr_ranges[ptr] = start; 
            return 1; 
        }

        /* Case 2: Higher End */
        if ( arr_ranges[ptr + 1] < end && arr_ranges[ptr] <= start  ) {

            arr_ranges[ptr + 1] = end; 
            return 1; 
        }
        
        /* Case 3: Consumes Old Range */
        if ( arr_ranges[ptr] > start && arr_ranges[ptr + 1] < end ) {

            arr_ranges[ptr] = start; 
            arr_ranges[ptr + 1] = end; 
            return 1; 
        }

        /* Case 4: Sub Range */
        if ( arr_ranges[ptr] <= start && end <= arr_ranges[ptr + 1] ) return 1; 
    }

    /* Adding a New Entry */
    arr_ranges[2 * (*size)] = start; 
    arr_ranges[( 2*(*size) ) + 1] = end; 
    (*size)++; 


    return 0;
}

int isOverlap( long* arr_ranges, int size, int init, long start, long end ) {

    for ( int i = 0; i < 2*size; i += 2 ) {

        const long cur_start = arr_ranges[i]; 
        const long cur_end = arr_ranges[i + 1]; 

        /* Range Does not Overlap */
        if ( cur_end < start || cur_start > end || i == init) continue;

        /* 1: Lower Start */
        if ( start < cur_start &&  end <= cur_end ) {
            return i; 
        }

        /* 2: Higher End */
        if ( cur_start <= start && cur_end < end ) {
            return i; 
        }

        /* 3: Lower Start && Higher End */
        if ( start < cur_start && cur_end < end ) {
            return i; 
        }

        /* 4: Entire Overlap */
        if ( cur_start <= start && end <= cur_end ) {
            return i; 
        }
    }
    return -1; 
}


void mergeElements( long* arr_ranges, int* size, int new_pos, int old_pos ) {

    const long new_start = arr_ranges[new_pos]; 
    const long new_end = arr_ranges[new_pos + 1]; 
    
    const long old_start = arr_ranges[old_pos]; 
    const long old_end = arr_ranges[old_pos + 1]; 


    /* Determining Type */
    if ( old_start < new_start && new_end < old_end ) {

        arr_ranges[new_pos] = old_start; 
        arr_ranges[new_pos + 1] = old_end; 
    }

    else if ( old_start < new_start && old_end <= new_end ) {

        arr_ranges[new_pos] = old_start; 
    }

    else if ( new_start <= old_start && new_end < old_end ) {

        arr_ranges[new_pos + 1] = old_end; 
    }

    /* 'Deletion' Process */
    arr_ranges[old_pos] = arr_ranges[(2*(*size)) - 2];
    arr_ranges[old_pos + 1] = arr_ranges[(2*(*size)) - 1];
    (*size)--; 

    return; 
}
