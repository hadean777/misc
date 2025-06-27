Compile (more details at helloworld/compile.txt):

as --64 is_prime.s -o is_prime.o
ld -melf_x86_64 -s is_prime.o -o is_prime


Usage:

./is_number number - checks if input contains only digits
./is_prime number - do the same but also check if number is prime

Example:

./is_number 9999999659
./is_prime 9999999661
