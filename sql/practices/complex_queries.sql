#1 all products more expensive than id = 3
USE sql_store;

SELECT *
FROM products
WHERE unit_price > (
    SELECT unit_price
    FROM products
    WHERE product_id = 3
);


#2 employees earn more than avg
USE sql_hr;

SELECT *
FROM employees
WHERE salary > (
    SELECT AVG(salary)
    FROM employees
);


#3 IN keyword, find products never ordered
USE sql_store;

SELECT *
FROM products
WHERE product_id NOT IN (
    SELECT DISTINCT product_id
    FROM order_items
);


#4 find clients without invoices (two ways)
USE sql_invoicing;

SELECT c.client_id, c.name
FROM clients c
LEFT OUTER JOIN invoices i
    ON c.client_id = i.client_id
WHERE i.invoice_id IS NULL;

SELECT c.client_id, c.name
FROM clients c
WHERE c.client_id NOT IN (
    SELECT DISTINCT i.client_id
    FROM invoices i
);


#5 find customers who have ordered lettuce (id = 3)
USE sql_store;

SELECT DISTINCT c.customer_id, c.first_name, c.last_name
FROM customers c
LEFT OUTER JOIN orders o
    ON c.customer_id = o.customer_id
INNER JOIN order_items oi
    ON o.order_id = oi.order_id
WHERE oi.product_id = 3;

SELECT DISTINCT customer_id, first_name, last_name
FROM customers
WHERE customer_id IN (
    SELECT o.customer_id
    FROM order_items oi
    INNER JOIN orders o
        ON oi.order_id = o.order_id
    WHERE oi.product_id = 3
);


#6 All keyword. Select invoices and their owners larger than all invoices of client 3
USE sql_invoicing;

SELECT c.name, i.invoice_id
FROM invoices  i
LEFT OUTER JOIN clients c
    ON i.client_id = c.client_id
WHERE i.invoice_total > (
    SELECT MAX(i1.invoice_total)
    FROM invoices i1
    WHERE i1.client_id = 3
);

SELECT c.name, i.invoice_id
FROM invoices i
LEFT OUTER JOIN clients c
    ON i.client_id = c.client_id
WHERE i.invoice_total > ALL (
    SELECT invoice_total
    FROM invoices
    WHERE client_id = 3
);


#7 select clients with at least 2 invoices
USE sql_invoicing;

SELECT *
FROM clients c
WHERE c.client_id = ANY (
    SELECT i.client_id
    FROM invoices i
    GROUP BY i.client_id
    HAVING COUNT(*) >= 2
);


#8 correlated sub-query: select employees whose salary is above the avg in their office
# sub-query is executed in each row of main query
USE sql_hr;

SELECT e1.first_name, e1.last_name, e1.salary
FROM employees e1
WHERE e1.salary > (
    SELECT AVG(e2.salary)
    FROM employees e2
    WHERE e2.office_id = e1.office_id
);


#9 get invoices that are larger than the client's average invoice amount
USE sql_invoicing;

SELECT i1.invoice_id, i1.number, i1.invoice_total
FROM invoices i1
WHERE i1.invoice_total > (
    SELECT AVG(i2.invoice_total)
    FROM invoices i2
    WHERE i1.client_id = i2.client_id
);


#10 EXIST Operator, Select clients that have an invoice
# when the sub-query search space is large, use EXISTS
USE sql_invoicing;

SELECT *
FROM clients
WHERE client_id IN (
    SELECT DISTINCT client_id
    FROM invoices
);

SELECT *
FROM clients c
WHERE EXISTS (
    SELECT i.client_id
    FROM invoices i
    WHERE i.client_id = c.client_id
);


#11 find products never ordered
USE sql_store;

SELECT *
FROM products p
WHERE NOT EXISTS(
    SELECT *
    FROM order_items oi
    WHERE oi.product_id = p.product_id
);


#12 sub-query in select clause
USE sql_invoicing;

SELECT
    invoice_id,
    invoice_total,
    (SELECT AVG(invoice_total) FROM invoices) AS invoice_avg,
    ABS(invoice_total - (SELECT invoice_avg)) AS diff
FROM invoices;


#13 find clients and their total sales, avg of all sales and diff (total sales, avg sales)
USE sql_invoicing;

SELECT
    c.client_id, c.name,
    (SELECT SUM(i.invoice_total) FROM invoices i WHERE i.client_id = c.client_id) AS total_sales,
    (SELECT AVG(i.invoice_total) FROM invoices i) AS avg_sales,
    (SELECT ABS(total_sales - avg_sales)) AS diff
FROM clients c;


#14 (IF, CASE, GROUP BY) select products and return their ids, names number of orders and order frequencies
USE sql_store;

SELECT p.product_id,
       p.name,
       COUNT(*) as count,
       CASE
           WHEN COUNT(*) > 1 THEN 'Many'
           WHEN COUNT(*) = 1 THEN 'Once'
           ELSE 'Never'
        END AS frequency
       # IF(COUNT(*) > 1, 'Many time', 'Once') AS freq
FROM products p
LEFT OUTER JOIN order_items oi
    ON p.product_id = oi.product_id
GROUP BY p.product_id, p.name;


#15 (GROUP BY) EX
USE sql_invoicing;

SELECT p.date, pm.name AS pmt_method, SUM(p.amount) AS total_pmts
FROM payments p
JOIN payment_methods pm
    ON p.payment_method = pm.payment_method_id
GROUP BY p.date, pm.name
ORDER BY total_pmts DESC






