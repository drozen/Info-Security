#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
 * a toy program for learning stack buffer
 * overflow exploiting
 * It reads a list of hex data from the
 * specified file, and performs bubble sorting
 */

long n = 0, c = 0, d = 0;

FILE *fp = NULL;

void bubble_sort()
{
  long swap = 0;
  long array[12];

// loading data to array 
printf("Source list:\n"); //Self-explanatory-prints the unsorted list.
char line[sizeof(long)*2+1]={0}; //In effect, builds a 9 character array (char line[9])
while(fgets(line,sizeof(line), fp)){ //for each line in fp, read into "line"
	if(strlen((char*)line)>1){ //If this line holds any data or characters
		sscanf(line,"%lx",&(array[n]));  //sscanf takes line as input, converts to format
						 //%lx which is long hexadecimal and writes it to
						 //address of array index n
		printf("0x%lx\n", array[n]);	 //Prints 0x plus the previously written line
		++n;				 //Increments n
	}
} fclose(fp);					 //Closes the file

  // do bubble sorting
  for (c = 0 ; c < ( n - 1 ); c++)
  {
    for (d = 0 ; d < n - c - 1; d++)
    {
      if (array[d] > array[d+1])
      {
        swap       = array[d];
        array[d]   = array[d+1];
        array[d+1] = swap;
      }
    }
  }
 
  // output sorting result
  printf("\nSorted list in ascending order:\n");
  for ( c = 0 ; c < n ; c++ )
     printf("%lx\n", array[c]);
 
}

int main(int argc, char **argv)
{
    if(argc!=2)
    {
        printf("Usage: ./sort file_name\n");
        return -1;
    }
    
    fp = fopen(argv[1], "rb");
    bubble_sort();

    return 0;
}
