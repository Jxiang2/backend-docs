#1 basics
USE sql_store;

SELECT
    first_name,
    last_name,
    points,
    (points * 10 + 100) AS discount_factor
FROM customers
-- WHERE customer_id = 1
ORDER BY first_name;


#2 distinct
USE sql_store;

SELECT DISTINCT state
FROM customers;


#3 (EX) return all the products
USE sql_store;

SELECT
    name,
    unit_price,
    unit_price * 1.1 AS new_price
FROM products;


#4 data types
USE sql_store;

SELECT *
FROM customers
WHERE birth_date > '1990-01-01'
ORDER BY customer_id
DESC;


#5 (EX) get orders placed this year
USE sql_store;

SELECT *
FROM orders
WHERE order_date >= '2019-01-01';


#6 and or not in operator
USE sql_store;

SELECT *
FROM customers
WHERE (
    (birth_date > '1990-01-01') OR
    (points > 1000 AND state IN ('VA','GA'))
);


#7 (EX) from order_items table, get order items for order #6 where total price is greater than 30
USE sql_store;

SELECT * FROM order_items
WHERE
    order_id = 6 AND
    unit_price * quantity > 30
;


#8 like
USE sql_store;

SELECT *
FROM customers
WHERE last_name LIKE '%y';


#9 (EX) customers with phone end with 9
USE sql_store;

SELECT *
FROM customers
WHERE phone NOT LIKE '%9';


#10 RegExp for strings
USE sql_store;

SELECT *
FROM customers
WHERE last_name REGEXP '[gim]e';


#11 (EX) get customers with names matching ...
USE sql_store;

SELECT *
FROM customers
WHERE first_name REGEXP 'elka|ambur';

SELECT *
FROM customers
WHERE last_name REGEXP 'ey$|on$';


SELECT *
FROM customers
WHERE last_name REGEXP '^my|se';


SELECT *
FROM customers
WHERE last_name REGEXP 'b[ru]';


#12 NULL
USE sql_store;

SELECT *
FROM customers
WHERE phone IS NULL;


#13 (EX) orders not shipped yet
USE sql_store;

SELECT *
FROM orders
WHERE shipped_date IS NULL;


#14 limit. Skip first 6 records and select 3 records onwards
USE sql_store;

SELECT *
FROM customers
LIMIT 6, 3;


#15 inner join
USE sql_store;

SELECT o.customer_id, c.customer_id, order_id, first_name, last_name
FROM orders o
INNER JOIN customers c ON o.customer_id = c.customer_id;


#16 self join (employees reports to managers)
USE sql_hr;

SELECT e.employee_id, e.first_name, m.employee_id AS managerId, m.first_name AS manager
FROM employees e
LEFT OUTER JOIN employees m
    ON e.reports_to = m.employee_id;


#17 multi-table join (find all orders and their orderers, as well as order statues, )
USE sql_store;

SELECT c.last_name, c.first_name, o.order_id, os.name AS status
FROM orders o
INNER JOIN customers c
    ON o.customer_id = c.customer_id
INNER JOIN order_statuses os
    ON o.status = os.order_status_id


#18 (EX) retrieve payments and their corresponding clients' names and payment methods
USE sql_invoicing;

SELECT p.payment_id, c.name, pm.name AS method, p.amount
FROM payments p
JOIN clients c
    ON p.client_id = c.client_id
INNER JOIN payment_methods pm
    ON p.payment_method = pm.payment_method_id;


#19 composite pk join
USE sql_store;

SELECT oi.order_id, oi.product_id, oi.quantity, oin.note
FROM order_items oi
INNER JOIN order_item_notes oin
    ON oi.order_id = oin.order_Id
    AND oi.product_id = oin.product_id;


#20 outer join
USE sql_store;

SELECT c.customer_id, c.first_name, o.order_id, shipped_date, s.name AS shipper
FROM customers c
LEFT OUTER JOIN orders o
    ON c.customer_id = o.customer_id
LEFT OUTER JOIN shippers s
    ON o.shipper_id = s.shipper_id;


#21 (EX) return each product's productId, name and quantity been ordered
USE sql_store;

SELECT p.product_id, p.name, oi.quantity
FROM products p
LEFT OUTER JOIN
    order_items oi ON p.product_id = oi.product_id


#22 (EX) return orders with their order dates, orderer names, shipper if any and statuses
USE sql_store;

SELECT o.order_id, c.first_name, c.last_name, os.name AS status, s.shipper_id, s.name AS shipper
FROM orders o
LEFT OUTER JOIN customers c
    ON o.customer_id = c.customer_id
LEFT OUTER JOIN order_statuses os
    ON o.status = os.order_status_id
LEFT OUTER JOIN shippers s
    ON o.shipper_id = s.shipper_id;


#23 unions (combine rows instead of cols)
USE sql_store;

SELECT o.order_id, o.order_date, 'ACTIVE' AS status
FROM orders o
WHERE o.order_date >= '2019-01-01'

UNION

SELECT o.order_id, o.order_date, 'ARCHIVED' AS status
FROM orders o
WHERE o.order_date < '2019-01-01';


#24 give all customers a points rank (Gold, Sliver, Bronze)
USE sql_store;

SELECT customer_id, first_name, points, 'Bronze' AS type
FROM customers
WHERE points < 2000

UNION

SELECT customer_id, first_name, points, 'Silver' AS type
FROM customers
WHERE  points >= 2000 AND points < 3000

UNION

SELECT customer_id, first_name, points, 'Gold' AS type
FROM customers
WHERE points >= 3000

ORDER BY first_name;


# 25 insert row
USE sql_store;

INSERT INTO customers (first_name,
   last_name,
   birth_date,
   address,
   city,
   state)
VALUES (
    'John',
    'Smith',
    '1990-01-01',
    'address',
    'city',
    'CA'
        );


# 25 insert multiple rows
USE sql_store;

INSERT INTO shippers (name)
VALUES
    ('Shipper 1'),
    ('Shipper 2'),
    ('Shipper 3');


#26 insert a new row into related tables (1 order -- * order_item)
USE sql_store;

INSERT orders (customer_id, order_date)
VALUES (1, '2019-01-02');

INSERT INTO order_items
VALUES
    (LAST_INSERT_ID(), 1, 1, 2.95),
    (LAST_INSERT_ID(), 2, 1, 3.99);


#27 quickly copy a table
USE sql_store;

CREATE TABLE orders_archived AS
SELECT * FROM orders;


#28 create a new table of invoices that have payment_date,
# replacing client_id by their name
USE sql_invoicing;

CREATE TABLE invoices_archived AS
SELECT
    i.invoice_id,
    i.number,
    c.name as client,
    i.invoice_total,
    i.payment_total,
    i.invoice_date,
    i.payment_date,
    i.due_date
FROM invoices i
JOIN clients c ON i.client_id = c.client_id
WHERE i.payment_date IS NOT NULL;


#29 update a row/ multiple rows
USE sql_invoicing;

UPDATE invoices
SET payment_total = 10, payment_date = '2019-03-01'
WHERE invoice_id = 1;


#30 (EX) give any customers born before 1990 50 points
USE sql_store;

UPDATE customers c
SET c.points = c.points + 50
WHERE c.birth_date < '1990-01-01';


#31 update row(s) with sub-queries
USE sql_invoicing;

UPDATE invoices
SET payment_total = invoice_total * 0.5,
    payment_date = due_date
WHERE client_id IN (
    SELECT client_id
    FROM clients
    WHERE state IN ('CA', 'NY')
);


#32 update comments of orders for users with more than 3000 points
USE sql_store;

UPDATE orders o
SET o.comments = 'Gold Customer'
WHERE o.customer_id IN (
    SELECT c.customer_id
    FROM customers c
    WHERE c.points > 3000
);


#33 delete
USE sql_invoicing;

DELETE FROM invoices
WHERE client_id = (
    SELECT c.client_id
    FROM clients c
    WHERE name = 'Myworks'
);


# 34 create views for re-usability. Views are isolated from tables
# views are updatable (CRUD) if it does not have DISTINCT, Aggregate Functions, GROUP BY/ HAVING, UNIONS
use sql_invoicing;

CREATE VIEW sales_by_clients AS
SELECT c.client_id, c.name,
       SUM(i.invoice_total) AS total_sales
FROM clients c
JOIN invoices i ON c.client_id = i.client_id
GROUP BY c.client_id, c.name;

SELECT *
FROM sales_by_clients
WHERE total_sales > 500;

SELECT *
FROM sales_by_clients sbc
INNER JOIN clients c ON sbc.client_id = c.client_id


# 35 (Ex) create view for balance(invoice_total - payment_total) for each client
USE sql_invoicing;

CREATE VIEW clients_balance AS
SELECT
    c.client_id,
    c.name,
    SUM(invoice_total - i.payment_total) AS balance
FROM clients c
INNER JOIN invoices i
    ON c.client_id = i.client_id
GROUP BY c.client_id, c.name






