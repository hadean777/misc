.data
correct_msg:
    .ascii "Correct!\n"
    .set correct_len, . - correct_msg

wrong_msg:
    .ascii "Wrong input\n"
    .set wrong_len, . - wrong_msg

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

