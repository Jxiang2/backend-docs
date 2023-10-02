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
    1. Read Uncommitted
        * Dirty Read: YES
        * Non-Repeatable Read: YES
        * Phantom Read: YES
    2. Read Committed
        * Dirty Read: NO
        * Non-Repeatable Read: YES
        * Phantom Read: YES
    3. Repeatable Read
        * Dirty Read: NO
        * Non-Repeatable Read: NO
        * Phantom Read: YES
    4. Serializable
        * Dirty Read: NO
        * Non-Repeatable Read: NO
        * Phantom Read: NO

## Consistency

#### Todo

## Durability

#### Todo

## Transaction

#### A collection of operations that performs a single logical function in a database application.

* Concurrent Updates: if 1 transaction is updating a row, it puts a lock on the row and prevent
  other transactions modifying the row until first transaction commits or rollback

## Dead lock

#### When 2 or more transactions are waiting for each other to release the lock

* Example (T1 started first, after T1 finished update <row_1>, T2 started):

        T1: update <row_1>✅ ; update <row2>           T2: update <row_2> ; update <row_1>
        T1: update <row_1>✅ ; update <row2>           T2: update <row_2>✅ ; update <row_1>
        T1: update <row_1>✅ ; update <row2>🔄         T2: update <row_2>✅ ; update <row_1>
        T1: update <row_1>✅ ; update <row2>🔄         T2: update <row_2>✅ ; update <row_1>🔄 —> Error 1213DeadLock!