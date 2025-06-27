.data
correct_msg:
    .ascii "Correct!\n"
    .set correct_len, . - correct_msg

wrong_msg:
    .ascii "Wrong input\n"
    .set wrong_len, . - wrong_msg

prime_msg:
    .ascii "Prime!\n"
    .set prime_len, . - prime_msg

not_prime_msg:
    .ascii "Not prime\n"
    .set not_prime_len, . - not_prime_msg

.text
.globl _start
_start:
    # Get argc from stack (number of arguments)
    mov (%rsp), %rax        # argc is at the top of the stack
    cmp $2, %rax            # Check if exactly 2 args (program name + one argument)
    jne wrong_input         # If not, print "Wrong input"

    # Get pointer to argv[1] (first argument after program name)
    mov 16(%rsp), %rsi      # argv[1] is at rsp+16 (rsp+8 is argv[0], rsp+16 is argv[1])

    # Check if argument is empty
    movb (%rsi), %al        # Get first character
    test %al, %al           # Check for null terminator (empty string)
    jz wrong_input          # If empty, print "Wrong input"

check_digits:
    # Loop through each character of the argument
next_char:
    movb (%rsi), %al        # Get current character
    test %al, %al           # Check for null terminator
    jz print_correct        # If null, all chars were digits, so print "Correct!"

    # Check if character is a digit (0-9)
    cmpb $'0', %al          # Compare with '0'
    jb wrong_input          # If less than '0', not a digit
    cmpb $'9', %al          # Compare with '9'
    ja wrong_input          # If greater than '9', not a digit

    inc %rsi                # Move to next character
    jmp next_char

print_correct:
    # Write "Correct!" to stdout
    mov $1, %rax            # syscall: write
    mov $1, %rdi            # file descriptor: stdout
    mov $correct_msg, %rsi  # buffer: address of correct_msg
    mov $correct_len, %rdx  # length of message
    syscall

    # Reset rsi to start of input string (argv[1])
    mov 16(%rsp), %rsi

    # Convert string to number
    xor %rbx, %rbx          # Clear rbx to store the number
convert_loop:
    movb (%rsi), %al        # Get current character
    test %al, %al           # Check for null terminator
    jz check_prime          # If null, done converting

    # Convert ASCII digit to number (subtract '0')
    sub $'0', %al
    movzx %al, %rax         # Zero-extend byte to quadword
    imul $10, %rbx          # Multiply current number by 10
    add %rax, %rbx          # Add current digit
    inc %rsi                # Move to next character
    jmp convert_loop

check_prime:
    # rbx contains the number to check
    cmp $1, %rbx            # 1 is not prime
    jbe not_prime

    cmp $2, %rbx            # 2 is prime
    je is_prime

    test $1, %rbx           # Check if number is odd
    jz not_prime            # Even numbers (except 2) are not prime

    mov $3, %rcx            # Start divisor from 3
prime_loop:
    mov %rcx, %rax          # Copy divisor to rax
    imul %rax, %rax         # Square the divisor
    cmp %rbx, %rax          # If divisor^2 > number, stop
    ja is_prime             # Number is prime if no divisors found

    mov %rbx, %rax          # Copy number to rax
    xor %rdx, %rdx          # Clear rdx for division
    div %rcx                # Divide rax by rcx
    test %rdx, %rdx         # Check if remainder is 0
    jz not_prime            # If divisible, not prime

    add $2, %rcx            # Next odd divisor
    jmp prime_loop

is_prime:
    # Write "Prime!" to stdout
    mov $1, %rax            # syscall: write
    mov $1, %rdi            # file descriptor: stdout
    mov $prime_msg, %rsi    # buffer: address of prime_msg
    mov $prime_len, %rdx    # length of message
    syscall
    jmp exit

not_prime:
    # Write "Not prime" to stdout
    mov $1, %rax            # syscall: write
    mov $1, %rdi            # file descriptor: stdout
    mov $not_prime_msg, %rsi # buffer: address of not_prime_msg
    mov $not_prime_len, %rdx # length of message
    syscall
    jmp exit

wrong_input:
    # Write "Wrong input" to stdout
    mov $1, %rax            # syscall: write
    mov $1, %rdi            # file descriptor: stdout
    mov $wrong_msg, %rsi    # buffer: address of wrong_msg
    mov $wrong_len, %rdx    # length of message
    syscall

exit:
    # Exit program
    mov $60, %rax           # syscall: exit
    xor %rdi, %rdi          # status: 0
    syscall

