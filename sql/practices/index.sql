# 1. index basics
USE sql_store;

CREATE INDEX idx_state ON customers (state);

EXPLAIN SELECT customer_id
FROM customers
WHERE state = 'CA';


# 2. (EX) find customers with more than 1000 points
USE sql_store;

CREATE INDEX idx_points ON customers (points);

EXPLAIN SELECT customer_id
FROM customers
WHERE points > 1000;


# 3. show existing indexes
USE sql_store;

SHOW INDEX in customers;


# 4. optimise string index length
USE sql_store;

CREATE INDEX idx_lastname ON customers (last_name(5));

# maximise the following sql statement to find the best substring length of last_name for indexing
SELECT COUNT(DISTINCT LEFT(last_name, 1)) AS unique_char FROM customers ;

SELECT COUNT(DISTINCT LEFT(last_name, 5)) AS unique_char FROM customers; # 5 is best choice

SELECT COUNT(DISTINCT LEFT(last_name, 9)) AS unique_char FROM customers;


# 5. full text indexes (build search engines for text)
USE sql_blog;

# normal query (bad)
EXPLAIN SELECT *
FROM posts
WHERE title LIKE '%react redux%' OR body LIKE '%react redux%';

# use full text index
CREATE FULLTEXT INDEX idx_title_body ON posts (title, body);

EXPLAIN SELECT *
FROM posts
# WHERE MATCH(title, body) AGAINST('react redux');
WHERE MATCH(title, body) AGAINST('react -redux' IN BOOLEAN MODE);


# 6. composite indexes (index multiple columns(max 16), used mostly in reality)
USE sql_store;

CREATE INDEX idx_state_points ON customers (state, points);
EXPLAIN SELECT customer_id FROM customers
WHERE state = 'CA' AND points > 1000;


# 7. order of cols in composite index
# * put most frequently used col first
# * usually put cols with higher cardinality first (number of unique values in a col)


# 8. (EX) optimise
EXPLAIN SELECT customer_id FROM customers
WHERE state = 'CA' OR points > 1000;

EXPLAIN
    SELECT customer_id
    FROM customers
    WHERE state = 'CA'

    UNION

    SELECT customer_id
    FROM customers
    WHERE points > 1000;


# 9. Index does not work with expressions on indexed col
CREATE INDEX idx_points ON customers (points);

EXPLAIN SELECT  customer_id FROM customers WHERE points + 10 > 2010; # bad
EXPLAIN SELECT customer_id FROM customers WHERE points > 2000; # good


# 10. index to sort data by "Using index", which is much better than "fileSort"
SHOW INDEX in customers;

EXPLAIN SELECT customer_id FROM customers ORDER BY state;
EXPLAIN SELECT customer_id FROM customers ORDER BY state, points;
EXPLAIN SELECT customer_id FROM customers ORDER BY state DESC , points DESC;
EXPLAIN SELECT customer_id FROM customers WHERE state = 'CA' ORDER BY points;
#Note: for idx_state_points, the following sorts will be fast (not use fileSort)
# * SORT BY state
# * SORT BY state, points
# * SORT BY state DESC, points DESC
# * WHERE state = 'XX' ORDER BY points (DESC)


# 11. conclusion
# 一、construct indexes using commonly used query-columns ; And query those columns in SELECT and ORDER BY clauses.
# 二、 Indexes' orders are important!. If we do CREATE INDEX idx_state_points ON customers (state, points);
# Then CREATE INDEX idx_state_points ON customers (state) is redundant;
# But CREATE INDEX idx_state_points (points, state) or CREATE INDEX idx_state_points (points) are not redundant;
