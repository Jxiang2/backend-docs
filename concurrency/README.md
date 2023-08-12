# Concurrency

## Multithreaded app process

![Screenshot 2023-06-21 214728](https://github.com/Jxiang2/tech-docs/assets/46456200/5003f88e-8431-4fea-9e03-fa6f1c4ccf10)

### Each thread has it's own stack and instruction ptr

**Stack**: place in memory where local variables are stored. and passed into functions. Functions are also called here.

**Heap**: place in memory where objects and their class members, static members are stored. Objects are shared between
threads. Heap is managed by GC.

* objects stay in heap until GC decides to collect them.
* members of objects are collected when the object is collected.
* references declared as local variables are allocated on the stack, and the objects they refer to are allocated on the
  heap.
* references declared as class members are allocated on the heap, and the objects they refer to are allocated on the
  heap.

**Instruction ptr**: address of next instruction to execute

**Context switch**: There are way more threads than cores, each process has many threads, each thread compete with each
other to be executed on CPU. The series of actions of (1. finish execute thread A 2. schedule thread A out 3. schedule
thread B in 4. finish execute thread B) is called context switch. It's the cost of multithreading.

### Thread vs Process

1. Threads share resources inside 1 process, majorly the data in heap
2. Context switching between threads is faster than processes

### Thread fundamentals

**Police hacker problem**

![IMG_0873](https://github.com/Jxiang2/tech-docs/assets/46456200/f0ea58cb-4b5b-4a0a-980d-93d0407dedfc)

### Thread Termination

1. If a program has 1 thread running, it won't terminate.
2. Daemon threads allow program finishes once the main thread terminates, worker threads will contine to run in
   background.
3. interruptions must be HANDLED to stop a worker thread, unless the worker thread is daemon.

3. ### Thread Coordination

![IMG_0923](https://github.com/Jxiang2/tech-docs/assets/46456200/7a0a2b94-4e12-4878-893a-15c3648773f0)

1. `thread.join(n)` allow the thread to run for n seconds, if it's not finished, the main thread will continue to run.

### Optimisation

1. Max performance: number of physical cores <= number of thread <= number of physical cores

#### Latency

* Latency is the time it takes to perform some action or to produce some result.
* By splitting a task into multiple threads, we can reduce the latency of the task.

#### Throughput

* Number of tasks completed per unit of time.
* Improve throughput by 1) Thread pooling => create threads at beginning to be reused, if threads are busy, task wait a
  thread to be available in the pool

### Resource Sharing

1. Atomic operation: if this operation happens, rest of the system won't be able to access the resource
    * assignment to primitive types except long double are atomic
    * assignment to reference types are atomic
    * assignment to volatile long double are atomic
    * all other operations are not atomic
2. Race condition: when 2 or more threads are trying to access the same resource, and one of them is modifying it.
3. Data race: re-ordering of instructions inside methods that cause unexpected results
    * solution: use volatile keyword, now the variable can act like a fence, and prevent re-ordering of instructions
    ![datarace](https://github.com/Jxiang2/tech-docs/assets/46456200/99e3ede6-e0cc-44c3-b92f-afd47d854851)
4. Locking strategies:
    * coarse-grained lock: lock the whole object, only 1 thread can access the object at a time
    * performance is bad, but easy to implement
      ![coarse](https://github.com/Jxiang2/tech-docs/assets/46456200/e1f43888-f9a4-403e-831a-2dc290cb3106)
    * fine-grained lock: lock only the part of the object that is being accessed, multiple threads can access the
      object, at the same time, but only 1 thread can access the member that is being locked
    * performance is good, but hard to implement, may cause deadlock
      ![IMG_0932](https://github.com/Jxiang2/tech-docs/assets/46456200/f2f1690f-dc0f-4a1c-8238-f1d2055f1209)
5. Deadlock: avoid circular locking, use tryLock() to avoid deadlock

### Locking API
1. Withou using Lock API:
![IMG_0937](https://github.com/Jxiang2/tech-docs/assets/46456200/0100ce91-6be1-406d-90d6-4050ea3344a8)
  * the current thread try to lock the object
  * if other threads has got it, the current thread is suspeneded and become not responsive
2. With Lock API:
  * With tryLock, if the object is locked by other thread, the current thread can do something else

