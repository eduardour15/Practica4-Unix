#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
void fun(int*, int);
int main(int argc, char const *argv[])
{
    int v1=10,v2=20;
    fun(&v1,v2);
    printf("v=%d,v2=%d",v1,v2);
    return 0;
}
void fun(int *v1,int v2){
    *v1=30;
    v2=50;
}
