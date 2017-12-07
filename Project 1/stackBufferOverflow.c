/*
 ============================================================================
 Name        : stackBufferOverflow.c
 Author      : Daniel Rozen
 Description : Program that demonstrated a Stack Buffer Overflow
 ============================================================================
 */

#include <strings.h>
#include <stdio.h>

int main (void)
{
   char storedPwd[6] = "111111";
   char enterPwd[6];

   printf("Please enter your password:\n");
   gets(enterPwd);

   printf("You have entered (%s)\n", enterPwd);

   if (strncmp(storedPwd, enterPwd, 6) == 0)
       printf("stored Pwd: (%s), entered Pwd (%s), Password valid!!!\n", storedPwd, enterPwd);
   else
       printf("stored Pwd: (%s), entered Pwd(%s), Password invalid\n", storedPwd, enterPwd);

}
