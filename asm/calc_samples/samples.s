.data
msg:
  .string "Hello, world!\n"
  .set len, . - msg

outmsg:
	.space 1048576, 0x31
  
.text

.globl main
main:
#.globl _start
#_start:

	mov $0xffffffffffffffff, %rax
	mov $3, %rbx
	#mulq %rbx
	#mov $4, %rdx
	xor %rdx, %rdx
	divq %rbx
	
	leaq outmsg, %r12
	
	movq %rax, (%r12)

	# write
	mov  $1,   %rax
	mov  $1,   %rdi
	mov  $outmsg, %rsi
	mov  $50, %rdx
	syscall

	# exit
	mov  $60, %rax
	xor  %rdi, %rdi
	syscall
