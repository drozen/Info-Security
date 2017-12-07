int main(int argc, char *argv[]) {
    int valid = 0;
    char str1[8];
    char str2[8];

    strcopy(str1,"1234567");
    gets(str2);
    if (strncmp(str1, str2, 8) == 0)
        valid = 1;
    printf("buffer1: str1(%s), str2(%s), valid(%d)\n", str1, str2, valid);
}
