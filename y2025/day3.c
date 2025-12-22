#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>  
#include <fcntl.h>  
#include <math.h>


#define BUF_SIZE 4096
#define MAX_ROWS 500
#define MAX_CHARS 512

typedef struct {
    int size; 
    unsigned char buf[MAX_ROWS][MAX_CHARS]; 
} data_t; 


int main( int argc, char** argv ); 
long find_joltage( const char* number ); 
void find_max_tenth( const char* number, int* tenth, int* index ); 
long find_joltage2( const char* number ); 
void find_max_n( const char* number, int* num, int* index, const int start, const int end );


int main( int argc, char** argv ) {

    const char* fname = "./inputs/day3.txt";

    FILE* fp = fopen(fname, "r"); 
    if ( fp == NULL ) {
        perror("File DNE");
        return 1; 
    }

    /* Buffer Creation */
    char* buf = malloc(BUF_SIZE); 
    memset( buf, 0, BUF_SIZE ); 

    /* Data Buffer Struct Creation */
    data_t input; 
    input.size = 0; 

    /* Data Extraction */
    while ( fgets( buf, BUF_SIZE, fp) != NULL ) {

        char* token = strtok( buf, "\n" ); /* Removing the newline */

        strcpy( (char*) input.buf[input.size], token ); 
        input.size++; 
    }

    /* Buffer Clean Up */
    free( buf ); 
    fclose(fp); 

    /* -- Algorithmn Section -- */
    long sum[2]; 
    sum[0] = 0; 
    sum[1] = 0; 

    for ( int i = 0; i < input.size; i++ ) {

        long joltage = find_joltage( (const char*) input.buf[i] );
        long joltage2 = find_joltage2( (const char*) input.buf[i] ); 
        sum[0] += joltage; 
        sum[1] += joltage2; 
    }

    /* Output */
    printf("DAY 1 RESULTS: %ld\n", sum[0]); 
    printf("DAY 2 RESULTS: %ld\n", sum[1]);


    return 0; 
}

long find_joltage( const char* number ) {

    int tenth; 
    int t_index; /* Index for Where the Tenth value is found */

    /* Finding the Max Value for the Tenth Position - Left Bias */
    find_max_tenth( number, &tenth, &t_index ); 

    int ones = 0; 

    /* Finding the Max Value within the Subarray for the Ones Position */
    for ( int i = t_index + 1; i < strlen(number); i++ ) {

        if ( number[i] - '0' > ones ) ones = number[i] - '0';
    }


    return ( tenth * 10 ) + ones; 
}

void find_max_tenth( const char* number, int* tenth, int* index ) {

    *tenth = 0; 

    for ( int i = 0; i < strlen(number) - 1; i++ ) {

        if ( (number[i] - '0') > *tenth ) {
            *tenth = number[i] - '0'; 
            *index = i; 
        }
    }
    return; 
}

long find_joltage2( const char* number ) {

    int jolt[12]; 
    memset( &jolt, 0, sizeof(int) * 12 );

    int index = -1; 
    long sum = 0; 

    for ( int i = 0; i < 12; i++ ) {

        int start = index + 1; 

        find_max_n( number, &jolt[i], &index, (const int) start,  (const int) strlen(number) - ( 12 - i ));
        sum += jolt[i] * pow(10, ( 12 - i - 1 )); 

    }
    
    return sum; 
}

void find_max_n( const char* number, int* num, int* index, const int start, const int end ) {

    for ( int i = start; i <= end; i++ ) {

        if ( number[i] - '0' > *num ) {
            *num = number[i] - '0'; 
            *index = i; 
        }
    }
    return; 
}