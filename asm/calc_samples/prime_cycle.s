.data
    # Buffer for decimal output (max 20 digits for 64-bit number + newline)
    buffer: .space 21
    newline: .ascii "\n"

.text
.globl _start
_start:
    # Initialize number to check (max 64-bit signed int: 9223372036854775807)
    mov $9223372036854775807, %rbx

check_next_number:
    # Check if number is <= 0 (end condition)
    cmp $0, %rbx
    jle exit

    # Check if number is prime
    cmp $1, %rbx            # 1 is not prime
    jbe not_prime

    cmp $2, %rbx            # 2 is prime
    je print_number

    test $1, %rbx           # Check if number is odd
    jz not_prime            # Even numbers (except 2) are not prime

    mov $3, %rcx            # Start divisor from 3
prime_loop:
    mov %rcx, %rax          # Copy divisor to rax
    imul %rax, %rax         # Square the divisor
    cmp %rbx, %rax          # If divisor^2 > number, stop
    ja print_number         # Number is prime if no divisors found

    mov %rbx, %rax          # Copy number to rax
    xor %rdx, %rdx          # Clear rdx for division
    div %rcx                # Divide rax by rcx
    test %rdx, %rdx         # Check if remainder is 0
    jz not_prime            # If divisible, not prime

    add $2, %rcx            # Next odd divisor
    jmp prime_loop

print_number:
    # Convert number in rbx to decimal string
    mov %rbx, %rax          # Number to convert
    mov $buffer+20, %rsi    # Point to end of buffer
    movb $0, (%rsi)         # Null terminator
    dec %rsi
    movb $10, (%rsi)        # Add newline
    mov $10, %rcx           # Divisor for decimal conversion

convert_loop:
    xor %rdx, %rdx          # Clear rdx for division
    div %rcx                # Divide rax by 10
    add $'0', %dl           # Convert remainder to ASCII
    dec %rsi                # Move buffer pointer left
    movb %dl, (%rsi)        # Store digit
    test %rax, %rax         # Check if quotient is 0
    jnz convert_loop        # Continue if not zero

    # Write number to stdout
    mov $1, %rax            # syscall: write
    mov $1, %rdi            # file descriptor: stdout
    mov %rsi, %rsi          # buffer: start of number string
    mov $buffer+21, %rdx
    sub %rsi, %rdx          # length: buffer end - start
    syscall

not_prime:
    dec %rbx                # Move to next number
    jmp check_next_number

exit:
    # Exit program
    mov $60, %rax           # syscall: exit
    xor %rdi, %rdi          # status: 0
    syscall
