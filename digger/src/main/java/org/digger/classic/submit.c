#include <stdio.h>
#include <string.h>
#include <stdlib.h>


#define MAX 10
#define MAX_INITIAL 3
#define RESULTS_FILE "dig.dat"
#define DEFAULT_INITIAL "..."
#define DEFAULT_HEADER "Content-type: text/html\n\n"


struct entry {
  char initials[MAX_INITIAL+1];
  int score, rand;
}; 


int main (int argc, char** argv) {

  struct entry results[MAX+1];
  char newinitials[MAX_INITIAL+1] = DEFAULT_INITIAL;
  int i, newscore = -1, newrand, newcheck;
  FILE* fd;

  if (argc>4) {
    strncpy (newinitials, argv[1], 3);
    strncat (newinitials, DEFAULT_INITIAL, 3-strlen (newinitials));
    newscore = atoi (argv[2]);
    newrand = atoi (argv[3]);
    newcheck = atoi (argv[4]);
  }

  for (i=0;i<MAX;i++) {			// init with zeros
    strncpy (results[i].initials, DEFAULT_INITIAL, MAX_INITIAL+1); 
    results[i].score = 0;
    results[i].rand = 0;
  }

  if ((fd = fopen (RESULTS_FILE, "rb")) != NULL) {
    if (fread (results, sizeof (struct entry), MAX, fd)!=MAX) {
      fclose(fd);
      return 0;
    }
    fclose (fd);
  }
  else {
    /* Now what do we do? If we try to save the score then the entire table
       will be erased. We'll have to leave it. Unless there genuinely isn't a
       dig.dat file, in which case comment out the following line (e.g. if
       installing the script and running it for the first time). Once the file
       has been created, put it back so the score table doesn't accidentally
       get erased if anything strange happens. */
    return 0;
  }

  if ((newscore>=0) && (((newrand+32768)*newscore)%65536 == newcheck) && (newrand>15)) {
    for (i=0;i<MAX;i++)
      if ((results[i].score==newscore) && (results[i].rand==newrand))
        break;
    if (i==MAX) {
      for (i=MAX;i>0;i--)
        if (results[i-1].score<newscore)
          memcpy (&results[i], &results[i-1], sizeof (struct entry)); 
        else
          break;
      strncpy (results[i].initials, newinitials, MAX_INITIAL+1); 
      results[i].score = newscore;
      results[i].rand = newrand;
      if ((fd = fopen (RESULTS_FILE, "wb")) != NULL) {
        if (fwrite (results, sizeof (struct entry), MAX, fd)!=MAX) {
          fclose(fd);
          return 0;
        }
        fclose (fd);
      }
    }
  }

  printf (DEFAULT_HEADER);
  for (i=0;i<MAX;i++)
    if (i<MAX)
      printf ("%s\n%d\n", results[i].initials, results[i].score);

  return 0;

}
