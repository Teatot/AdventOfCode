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
    int count = 0; 

    for ( int i = 0; i < input.values_size; i++ ) {

        long cur_val = input.values[i]; 

        /* Comparing the Value to Each Range */
        for ( int j = 0; j < 2*input.range_size; j += 2 ) {

            if ( input.range[j] <= cur_val && input.range[j + 1] >= cur_val ) {

                count++; 
                break; 
            }
        }
    }

    /* Output */
    printf("DAY 1 RESULT: %d\n", count ); 

    return 0; 
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
    }

    /* Adding a New Entry */
    arr_ranges[2 * (*size)] = start; 
    arr_ranges[( 2*(*size) ) + 1] = end; 
    (*size)++; 

    return 0;
}
