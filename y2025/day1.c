#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>  // read, write, close, fork, exec
#include <fcntl.h>   // open flags

#define SIZE 1024
#define ROTATE_SIZE 100

int main( int argc, char** argv ); 
void part1( int* password, int* cur_pos, char cur_direct, int cur_rot ); 
void part2( int* password, int* cur_pos, char cur_direct, int cur_rot ); 


int main( int argc, char** argv ) {

    const char* input_file = "./inputs/day1.txt"; 

    /* Opening the File to Fetch the Inputs */
    FILE* fp = fopen( input_file, "r" ); 
    if ( fp == NULL ) {
        perror("File DNE."); 
        return 1; 
    }

    /* Buffer */
    char buf[SIZE]; 

    /* Tracker */
    int password[2];
    memset( password, 0, 2 * sizeof(int) ); 

    /* From Question, Starts at 50 */
    int position[2]; 
    position[0] = 50 + ( 100 * ROTATE_SIZE );
    position[1] = 50 + ( 100 * ROTATE_SIZE );

    /* Reading File Line by Line */
    while ( fgets( buf, sizeof(buf), fp ) != NULL ) {

        char direction; 
        int rotate; 

        /* Extracting the Data */
        if ( sscanf( buf, "%c%d", &direction, &rotate ) == 2 ) {

            part1( &password[0], &position[0], direction, rotate );
            part2( &password[1], &position[1], direction, rotate ); 
        }
        else {

            fprintf(stderr, "Unable to Extract\n" );
            return 1; 
        }

    }

    /* Closing the File */
    fclose(fp); 

    /* Printing Results */
    printf("\nPART 1 PASSWORD: %d\n", password[0] ); 
    printf("PART 2 PASSWORD: %d\n", password[1] ); 

    return 0; 
}

void part1( int* password, int* cur_pos, char cur_direct, int cur_rot ) {

    /* Applying Directions */
    if ( cur_direct == 'L' ) *cur_pos -= cur_rot; 
    if ( cur_direct == 'R' ) *cur_pos += cur_rot; 

    if ( *cur_pos % ROTATE_SIZE == 0 ) (*password)++;

    return; 
}


void part2( int* password, int* cur_pos, char cur_direct, int cur_rot ) {

    int rot = cur_rot;
    int old_pos = *cur_pos % ROTATE_SIZE; 

    /* Normalizing the Rotation */
    if ( cur_rot % ROTATE_SIZE != cur_rot ) {

        /* Accounting for Passing 0 */
        *password += ( cur_rot - (cur_rot % ROTATE_SIZE) ) / ROTATE_SIZE; 

        rot = cur_rot % ROTATE_SIZE; 
        
        /* Edge Case */
        if ( old_pos == 0 && rot == 0 ) {
            return; 
        }
    }

    /* Applying Directions */
    if ( cur_direct == 'L' ) *cur_pos -= rot; 
    if ( cur_direct == 'R' ) *cur_pos += rot; 

    if ( *cur_pos % ROTATE_SIZE == 0 ) {
        (*password)++;
        return; 
    }

    /* Edge Case - Moving from 0, we do not include this 0 as passing */
    if ( old_pos == 0 ) return; 

    /* Determining if 0 was Passed in the Process */
    if ( cur_direct == 'L' && *cur_pos % ROTATE_SIZE > old_pos ) (*password)++;
    if ( cur_direct == 'R' && *cur_pos % ROTATE_SIZE < old_pos ) (*password)++; 

    return; 
}