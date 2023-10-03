# ACID

## Atomicity

#### All queries must succeed, if one fails, all should roll back.

## Isolation

#### Can my in-progress transaction be seen by other transactions?

* Read Phenomena
    1. Dirty Read
        * Transaction A reads a row; then that row is changed but NOT COMMITTED yet in another
          transaction; and then transaction A re-reads the row, getting different data
          the second time.
        * Transaction A's reads are said to be "dirty reads", which lead to incorrect results.
    2. Non-Repeatable Read
        * Transaction A reads <row_1>, before it’s next read on <row_1>, transaction B is COMMITTED
          and updated <row_1>. So transaction A reads 2 values of <row_1>
        * Transaction A's reads are not dirty because it reads committed data, but it is
          inconsistent because it reads different values of the same row.
    3. Phantom Read
        * Transaction A selecting some rows; Then, Transaction B INSERT a row that matches the
          search condition of Transaction A; Then, Transaction A re-selects the rows, getting
          different results.
        * Transaction A's reads are not dirty because it reads committed data, but it is
          inconsistent because it reads different number of rows. Those new rows are "phantom"

* Isolation Levels: implemented by DB to fix above problems
    1. Read Uncommitted: No isolation, any change from the outside is visible to a transaction
        * Dirty Read: YES
        * Non-Repeatable Read: YES
        * Phantom Read: YES
    2. Read Committed: Each query in a transaction only sees committed stuff before the query
        * Dirty Read: NO
        * Non-Repeatable Read: YES
        * Phantom Read: YES
    3. Repeatable Read: Each query in a transaction only sees UPDATES committed at the start of the
       transaction
        * Dirty Read: NO
        * Non-Repeatable Read: NO
        * Phantom Read: YES (You cannot foresee newly inserted rows)
    4. Serializable: Each transaction is executed as if it is the only transaction in the system
        * Dirty Read: NO
        * Non-Repeatable Read: NO
        * Phantom Read: NO

## Consistency

* Data consistency
    1. Defined by DB schema, enforced by foreign key constraints, triggers, etc.
    2. Atomicity and Isolation are needed to ensure consistency
* Reads consistency
    1. If a transaction committed a change will a new transaction see the change immediately?
        * Yes, for a single DB server
        * No, for a distributed DB system

## Durability

* Committed transactions must be persisted in a durable storage (disk) and not lost because of
  system failure
* Caching database (Redis) does not have guarantee durability

## Transaction

#### A collection of operations that performs a single logical function in a database application.

* Transaction Process:
    1. Scan transaction see if locks are already applied on rows it wants to operate
    2. Process transaction and put locks on operated rows until transaction ends
* Concurrent Updates: if 1 transaction is updating a row, it puts a lock on the row and prevent
  other transactions modifying the row until first transaction commits or rollback
* Concurrent Reads: No lock is put on rows that are being read, so multiple transactions can read
  the same row at the same time

## Dead lock

#### When 2 or more transactions are waiting for each other to release the lock

* Example (T1 started first, after T1 finished update <row_1>, T2 started):

        T1: update <row_1>✅ ; update <row2>           T2: update <row_2> ; update <row_1>
        T1: update <row_1>✅ ; update <row2>           T2: update <row_2>✅ ; update <row_1>
        T1: update <row_1>✅ ; update <row2>🔄         T2: update <row_2>✅ ; update <row_1>
        T1: update <row_1>✅ ; update <row2>🔄         T2: update <row_2>✅ ; update <row_1>🔄 —> Error 1213DeadLock!