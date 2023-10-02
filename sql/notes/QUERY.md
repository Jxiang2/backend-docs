# Databases

## MYSQL

DataBase 1—* Schema 1—* Table

### Table

1. Foregink

### Logics

1. AND always evaluates before OR
2. LIKE and REGEX are used in the same position
3. HAVING does not allow name alias after it
4. IN is used for comparing multiple rows; = is used for comparing 1 row
5. ALL can be used to compare the return values of subquieries iteratively in an “AND” pattern.
   e.g.: price > ALL (SELECT …) <==> price > ALL (100, 105, 99 ….)
6. ANY can be used to compare the return values of subquieries iteratively in an “OR” pattern. e.g.:
   price > ANY (SELECT …) <==> price > ANY (100, 105, 99 ….)
7. correlated query: A query consists of 1 main query and 1 sub query, and the subquery uses fields
   of main query as it’s own query-conditions.
8. EXIST results in a correlated subquery. It can be used to replace IN if the subquery has a
   strictly larger search space. than the main query.
9. AGGREGATE functions and ordinary rows can not be selected in the same SELECT clause
10. Views can
    1. Simplify queries by using already computed results
    2. Reduce impact of changes
    3. restrict access to underlying table
11. triggers: a block of SQL code that automatically gets excuted before or after an insert, update
    or delete statement
12. procedures: store, organize secure and optimize (internally) SQL commands outside project
    sourcecode
13. events: event: block of SQL code gets executed according to a schedule (first need to turn it
    on)

### C Syntax

1. insert a row:  INSERT <table_name> (<col_name1>, <col_name2>) VALUES (<val1>, <val2>);
2. create a table copy: CREATE TABLE <new_table_name> AS SELECT * FROM <old_table> WHERE ...

### R Syntax

1. select a schema: USE <schema_name>
2. order of SQL in one statement: SELECT <col_name> FROM <table_name> WHERE … ORDER BY…
3. % any number of characters (%y);; _ single character(___y);;
4. REGEXP:
    1. ‘xxx’: any record that ‘xxx’ is a substring
    2. ^: begin with, ‘^a’
    3. $: end with, ‘abc$’
    4. |: or, ‘filed | mac’
    5. [gim]e: ge & ie & me
    6. [a-e]e: equivalent to [abcde]e
5. LIMIT: <offset>, <number of records>
6. JOIN:
    1. INNER JOIN: select multiple tables based on a column <col_name>, return all rows that share
       the same <col_name> value in all those tables (no null)
    2. SELF JOIN: inner join a table with itself on a column <col_name>, return all rows that share
       the same <col_name> value in all those tables (no null)
    3. LEFT OUTER JOIN:
        1. if <table1> joins <table2> on <col_name>, which is a <table1> non-null fk and a <table2>
           pk. Then LEFT OUTER JOIN <table1> to <table2> is equivalent to INNER JOIN of them
        2. if <table1> joins <table2> on <col_name>, which is a <table1> nullable fk and a <table2>
           pk. Then LEFT OUTER JOIN <table1> to <table2> keep all rows of <table1>, including nulls
    4. RIGTH OUTER JOIN: use LEFT OUTER JOIN instead in practice

### U Syntax

1. update single/ multiple row(s): UPDATE <table_name> SET <col_name1> = <val1>, <
   col_name2> = <val2> WHERE <col_name> = XXX;
2. update with subqueries (example):  UPDATE orders o SET o.comments = 'Gold Customer' WHERE
   o.customer_id IN (
   SELECT c.customer_id
   FROM customers c
   WHERE c.points > 3000
   );

### D_Syntax

1. delete single/ multiple row(s): DELETE FROM invoices WHERE client_id = (
   SELECT *
   FROM clients
   WHERE name = 'Myworks'
   );

### MySQL Functions (Functions need to be put after SELECT or WHERE!)

* Numeric Functions

1. ROUND(5.7345, 2)
2. TRUNCATE(5.7345, 2)
3. CEILING(5.7)
4. FLOOR(5.7)
5. ABS(-10)
6. RAND()

* String Functions

1. LOWER(‘HELLO’)
2. LTRIM(‘ SKY’)
3. RTRIM(‘SKY ’)
4. TRIM(‘ SKY ‘)
5. LEFT(‘Kindergarten’, 4), return first 4
6. RIGHT(‘Kindergarten’, 4), return last 4
7. SUBSTRING(‘Kindergarten’, 2, 5)
8. LOCATE(‘k’, ‘Kindergarten’), return 3 since MySQL STRINGS start with index 1
9. REPLACE(‘Kindergarten’, ‘garten’, ‘garden’)
10. CONCAT(‘str1’, ‘ ‘, ‘str2’)

* Datetimes

1. NOW()
2. CURDATE()
3. CURTIME()
4. DATE_FORMAT(NOW(), ‘%M %d %Y’)
5. DATE_FORMAT(NOW(), ‘%H:%i %p’)
6. DATE_ADD(NOW(), INTERVAL +/-1 YEAR)
7. DATE_SUB(NOW(), INTERVAL +/-1 YEAR)
8. DATEDIFF(NOW(), DATE_ADD(NOW(), INTERVAL 1 YEAR)), return differences in days

### Index (quickly find data by grouping similar data together, but it increases db size and slows write operations)

* Create prefix index: CREATE INDEX <idx_col_name> ON <table_name> (<col_name>(<length>));

1. To reduce the number of rows to search, we need to experiment on <length> with <col_name>’s
   cardinality, which is number of unique values in the column of <col_name>. The higher the
   cardinality, the more efficient the index is:

CREATE INDEX idx_lastname ON customers (last_name(5)); ⬇️

SELECT COUNT(DISTINCT LEFT(last_name, 1)) AS unique_char FROM customers ; ❎

SELECT COUNT(DISTINCT LEFT(last_name, 5)) AS unique_char FROM customers; ✅

SELECT COUNT(DISTINCT LEFT(last_name, 9)) AS unique_char FROM customers; ❎

* Full text indexes (build search engines for text)

1. Example code:
   CREATE FULLTEXT INDEX <idx_col_1_col_2> ON posts (<col_1>, <col_2>);
   SELECT title body FROM posts
   WHERE MATCH(title, body) AGAINST('react redux');
   #WHERE MATCH(title, body) AGAINST('react -redux' IN BOOLEAN MODE);

* Composite indexes (indexes using multiple columns, max is 16, used mostly in reality)

1. Index does not work with expressions on indexed col:
   CREATE INDEX idx_points ON customers (points);
   EXPLAIN SELECT customer_id FROM customers WHERE points + 10 > 2010; # bad
   EXPLAIN SELECT customer_id FROM customers WHERE points > 2000; # good
2. Indexes' orders are important:
   If we do CREATE INDEX idx_state_points ON customers (state, points);
   Then CREATE INDEX idx_state ON customers (state) is redundant;
   But CREATE INDEX idx_points_state (points, state) and CREATE INDEX idx_points (points) are not
   redundant;
3. We need to USUALLY construct indexes based on the order of most commonly used query-columns with
   high cardinalities ; And query those columns in SELECT and ORDER BY clauses.
4. Indexes we defined always contain the indexed columns plus primary key. CREATE INDEX
   idx_state_points ON customers (state, points) ==> id, state, points are indexed

* IndexSort (use index to sort data by "Using index", which is much better than "fileSort"

1. Example: for idx_<col_1>_<col_2>, the following sorts will be fast (not use fileSort)

… SORT BY col_1

… SORT BY col_1, col_2

… SORT BY col_1 DESC, col_2 DESC

.. WHERE col_1 = 'XX' ORDER BY col_2

.. WHERE col_1 = 'XX' ORDER BY col_2 DESC

## MONGODB

Database 1——* Collection 1——* Document
Scenario:
DB: bookstore
Collection: books

### INSERTION

1. Insert One: db.books.insertOne({title:"The Color of Magic", author:"Terry Pratchett", pages:300,
   rating:7, genres: ["fantasy", "magic"]})
2. Insert Many: db.books.insertMany([{title:"The Color of Magic", author:"Terry Pratchett", pages:
   300, rating:7, genres: ["fantasy", "magic”]}, {title:"The Name of King”, author:”Peter Por",
   pages:500, rating:4, genres: ["fantasy", "war"]}])

### RETRIEVAL

1. Find all documents: db.books.find()
2. Filter documents: db.books.find({author: "Terry Pratchett", rating: 7})
3. Filter documents & sort by title: db.books.find({author: "Brandon Sanderson"}).sort({title: 1})
4. Filter documents & only return title and author property: db.books.find({author: "Brandon
   Sanderson"}, {title: 1, author: 1})
5. Find all documents & return only title & author property: db.books.find({}, {title: 1, author:
   1})
6. Filter the first matching document: db.books.findOne({author: "Brandon Sanderson"}, {title: 1,
   author: 1})
7. Filter documents with limited returned docs: db.books.find({author: "Brandon Sanderson"}).limit(
    1)
8. Filter books with rating > 7: db.books.find({rating: {$gt: 7}})
9. Filter books with rating > 7 & author is Branden: db.books.find({rating: {$gt: 7}, author: "
   Brandon Sanderson"})
10. Filter books with rating > 7 | author is Branden: db.books.find(
    {$or: [{rating: {$gt: 7}}, {author: "Brandon Sanderson"}]})
11. Filter books with (rating > 7 | author is Branden) & page is 500: db.books.find(
    {$or: [{rating: {$gt: 7}}, {author: "Brandon Sanderson"}], $and: [{pages: 400}]})
12. Filter books with rating is(n’t) from 8 - 10: db.books.find({rating: {$(n)in: [8,9,10]}})
13. Filter books with geners array including fantasy: db.books.find({genres: "fantasy"})
14. Filter books with geners array inclding fantasy & magic: db.books.find({genres:
    {$all: ["fantasy", "sci-fi"]}})
15. Filter books with geners array including exactly fantasy & magic: db.books.find(
    {genres: [“fantasy”, “magic”]})
16. Filter nested docs with name property is “Yoshi”: db.books.find({"reviews.name": "Yoshi"})

### UPDATE:

1. Update rating and pages property for 1 doc: db.books.updateOne({_id: ObjectId("
   6258faf1fc72a43027fce2c0")}, {$set: {rating: 8, pages: 360}})
2. Update the pages property for all docs with author is Erren Yaeger: db.books.updateMany(
   {author: "Erren Yaeger"}, {$set: {rating: 10, pages: 3600}})
3. Increment the pages property by 2 for 1 doc: db.books.updateOne({_id: ObjectId("
   6258faf1fc72a43027fce2c0")}, {$inc: {pages: 2}})
4. remove/ add a string “fantasy” to the genres array of a doc: db.books.updateOne({_id: ObjectId("
   6258faf1fc72a43027fce2c0")}, {$(pull)push: {genres: "fantasy"}})
5. remove/ add multiple string to the generes array of a doc: db.books.updateOne({_id: ObjectId("
   6258faf1fc72a43027fce2c0")}, {$(pull)push: {genres: {$each: ["fantasy", "horror"]}}})

### DELETE:

1. delete 1 book based on id: db.books.deleteOne({_id: ObjectId("6258f9b7fc72a43027fce2bd")})
2. delete all books with author Erren Yaeger: db.books.deleteMany({author: "Erren Yaeger"})

### AGGREGATE

1. Count all docs: db.books.countDocuments()
2. Count all docs with matching author property: db.books.find({author: "Brandon Sanderson"})
   .count()

### Advanced Queries

1. Update book with title specified, if the book DNE, insert a new one with that title (upsert):
   db.books.updateOne({ title: upsertData.title, }, { $set: upsertData }, { upsert: true }) 
