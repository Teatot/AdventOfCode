#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>  
#include <fcntl.h>   

#define BUF_SIZE 16384

typedef struct {
    int size; 
    char buf[1024][512];
} input_t; 


int main( int argc, char** argv ); 
int repeating( const char* string );
int repeating2( const char* string );
int substrcmp( const char* str, int start, int width ); 
int invalid( const char* str, int width, int n);  


int main( int argc, char** argv ) {

    const char* fname = "./inputs/day2.txt"; 

    FILE* fp = fopen(fname, "r"); 
    if ( fp == NULL) {
        perror("File DNE."); 
        return 1; 
    }

    /* Buffer for Extracting the Data */
    char *buf = malloc(BUF_SIZE); 
    memset( buf, 0, BUF_SIZE ); 

    /* Struct to Hold Extracted Data */
    input_t data; 
    data.size = 0;

    /* Extracting the Data */
    while ( fgets( buf, BUF_SIZE, fp ) != NULL ) {
        
        char* token = strtok( buf, ",");

        /* Delimiting by Comma */
        while ( token != NULL ) {
            /* Copying String Into Data Struct */
            strncpy(data.buf[data.size], token, 511);
            data.buf[data.size][511] = '\0';
            data.size++;

            /* Next */
            token = strtok( NULL, "," ); 
        }
    }
    
    /* Clean Up - Buffer Extraction */
    free( buf ); 

    /* Analyzing Each Case */
    long score[2];
    score[0] = 0; 
    score[1] = 0; 

    for ( int i = 0; i < data.size; i++ ) {

        char* in_seq = data.buf[i]; 

        /* Extracting the Start & End of the Range */
        long start = atol(strtok(in_seq, "-"));
        long end = atol(strtok(NULL, "-"));

        char str_num[128];

        for ( ;start <= end; start++ ) {
            
            /* Converting to String */
            snprintf( str_num, sizeof(str_num), "%ld", start ); 

            if ( repeating( str_num ) ) score[0] += start; 

            if ( repeating2( str_num ) ) score[1] += start; 
        }

    }

    fclose(fp); 

    /* Printing Result */
    printf("DAY 1 RESULT: %ld\n", score[0] ); 
    printf("DAY 2 RESULT: %ld\n", score[1] ); 

    return 0; 
}

int repeating( const char* string ) {

    int len = strlen(string); 

    /* Odd Parity */
    if ( len % 2 != 0 ) return 0; 

    int width = len / 2; 

    for ( int i = width; i < len; i++ ) {
        if ( string[i - width] != string[i] ) return 0; 
    }
    return 1; 
}

int repeating2( const char* string ) {

    int len = strlen(string); 

    int start = 0; 
    int width = 1; 

    while ( width < (len / 2) + 1 ) {

        /* Step 1 - Check for Occurance */
        if ( string[start] == string[start + width] ) {

            /* Step 2 - Compare Occurances */   
            if ( substrcmp( string, start, width ) ) {

                /* Checking if the Width is a factor of the length */
                if ( len % width == 0 ) {

                    /* Step 3 - Double Checking Repeatition for Substring */                    
                    if ( invalid( string, width, (len / width) - 1 ) ) return 1; 
                }
            }


        }
        
        width++; 
    }

    return 0; 
}

int substrcmp( const char* str, int start, int width ) {

    int start2 = start + width; 

    for ( int i = 0; i < width; i++ ) {

        if ( str[start + i] != str[start2 + i] ) return 0; 
    }

    return 1; 
}

int invalid( const char* str, int width, int n) {

    for ( int i = 1; i < n; i++ ) {

        if ( !substrcmp( str, ( i * width ), width) ) return 0;
    }
    return 1; 
}

