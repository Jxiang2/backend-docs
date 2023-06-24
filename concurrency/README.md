
# Concurrency

## Multithreaded app process
![Screenshot 2023-06-21 214728](https://github.com/Jxiang2/tech-docs/assets/46456200/5003f88e-8431-4fea-9e03-fa6f1c4ccf10)


### Each thread has it's own stack and instruction ptr

**Stack**: place in memory where local variables are stored. and passed into functions

**Instruction ptr**: address of next instruction to execute

**Context switch**: There are way more threads than cores, each process has many threads, each thread compete with each other to be executed on CPU. The series of actions of (1. finish execute thread A 2. schedule thread A out 3. schedule thread B in 4. finish execute thread B) is called context switch. It's the cost of multithreading.

### Thread vs Process 
1. Threads share resources inside 1 process, majorly the data in heap
2. Context switching between threads is faster than processes


### Thread fundamentals
**Police hacker problem**

![IMG_0873](https://github.com/Jxiang2/tech-docs/assets/46456200/f0ea58cb-4b5b-4a0a-980d-93d0407dedfc)
