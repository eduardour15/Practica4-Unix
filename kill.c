#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
void main(int argc, char *argv[])
{
	int pid, sig;
	if(argc!=3)
		{
			printf("Parametros incorrectos\n");
			exit(-1);
		}
	pid= atoi(argv[1]);
sig=atoi(argv[2]);

kill(pid,sig);
}



