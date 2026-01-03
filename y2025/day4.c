#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>  
#include <fcntl.h>   

#define BUF_SIZE 4096
#define MAX_ROWS 150
#define MAX_CHARS 150


const char symbols[2] = {'I', 'R'};

typedef struct {
    int size; 
    char buf[MAX_ROWS][MAX_CHARS]; 
} data_t; 


int main( int argc, char** argv ); 
void func_day1( data_t input, int* output ); 
void func_day2( data_t input, int* output ); 
int valid_forklift( int maxx, int maxy, data_t src, int x, int y );
int valid_forklift2( int maxx, int maxy, data_t src, int x, int y, int status );

int main( int argc, char** argv ) {

    const char* fname = "./inputs/day4.txt";

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
    input.size = 0; 

    /* Data Extraction */
    while ( fgets( buf, BUF_SIZE, fp) != NULL ) {

        char* token = strtok( buf, "\n" ); /* Removing the newline */

        strncpy( (char*) input.buf[input.size], token, MAX_CHARS - 1 );
        input.buf[input.size][MAX_CHARS - 1] = '\0';
        input.size++; 
    }

    /* Buffer Clean Up */
    free( buf ); 
    fclose(fp); 

    /* -- Algorithmn Section -- */
    int count[2]; 
    count[0] = 0; 
    count[1] = 0; 

    func_day1( input, count );
    func_day2( input, count + 1 ); 

    /* Output Section */
    printf("DAY 1 RESULTS: %d\n", count[0] ); 
    printf("DAY 2 RESULTS: %d\n", count[1] ); 

    return 0; 
}


void func_day1( data_t input, int* output ) {

    const int max_row = input.size; 
    const int max_col = strlen(input.buf[0]);

    /* Transfering Content to the Structures */    

    for ( int row = 0; row < max_row; row++ ) {
        
        for ( int col = 0; col < max_col; col++ ) {

            /* Checking if Point is Paper */
            if ( input.buf[row][col] == '@' ) {
                /* Determining if It is Valid*/
                if ( valid_forklift( max_row, max_col, input, row, col ) ) (*output)++; 
            }
        }
    }

    return; 
}


void func_day2( data_t input, int* output ) {

    const int max_row = input.size; 
    const int max_col = strlen(input.buf[0]);

    int changes = 1; 
    int status = 0; 

    while ( changes > 0 ) {

        changes = 0; // Reset

        for ( int row = 0; row < max_row; row++  ) {
            for ( int col = 0; col < max_col; col++ ) {

                /* Replacing Prev Symbol with Empty*/
                if ( input.buf[row][col] == symbols[( status + 1) % 2]) {

                    input.buf[row][col] = '.'; 
                }
                /* Checking if Paper */
                else if ( input.buf[row][col] == '@' ) {
                    /* Determines if is Valid */
                    if ( valid_forklift2( max_row, max_col, input, row, col, status ) ) {

                        input.buf[row][col] = symbols[ status % 2 ];
                        (*output)++; 
                        changes++; 
                    }
                }
            }
        }

        status++; 
    }

    return; 
}


int valid_forklift( int maxx, int maxy, data_t src, int x, int y ) {

    /* Checks all Possible Combinations */
    int comb[3] = {-1, 0, 1}; 
    int count = 0; 

    for ( int i = 0; i < 3; i++ ) {
        
        for ( int j = 0; j < 3; j++ ) {

            if ( i == 1 && j == 1 ) continue; /* Ignore Centre Point */

            int dx = x + comb[i]; 
            int dy = y + comb[j]; 

            /* Out of the Bounds */
            if ( dx < 0 || dx >= maxx || dy < 0 || dy >= maxy ) continue; 

            if ( src.buf[dx][dy] == '@' ) count++; 

            /* Checking to See if Count is Less than 4 */
            if ( count >= 4 ) return 0; 
        }
    }

    return count < 4; 
}

int valid_forklift2( int maxx, int maxy, data_t src, int x, int y, int status ) {

    int comb[3] = {-1, 0, 1}; 
    int count = 0; 

    for ( int i = 0; i < 3; i++ ) {
        
        for ( int j = 0; j < 3; j++ ) {

            if ( i == 1 && j == 1 ) continue; /* Ignore Centre Point */

            int dx = x + comb[i]; 
            int dy = y + comb[j]; 

            /* Out of the Bounds */
            if ( dx < 0 || dx >= maxx || dy < 0 || dy >= maxy ) continue; 

            if ( src.buf[dx][dy] == '@' || src.buf[dx][dy] == symbols[status % 2] ) count++; 

            /* Checking to See if Count is Less than 4 */
            if ( count >= 4 ) return 0; 
        }
    }

    return count < 4;
}