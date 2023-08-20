#1 stored procedure basics
USE sql_invoicing;

DROP PROCEDURE IF EXISTS get_clients;

DELIMITER $$
CREATE PROCEDURE get_clients()

BEGIN
    SELECT * FROM clients;
END $$

DELIMITER ;

CALL get_clients();


#2 (EX) create procedure to get all invoices with a balance > 0
USE sql_invoicing;

DROP PROCEDURE IF EXISTS get_invoices_with_balance;

DELIMITER $$
CREATE PROCEDURE get_invoices_with_balance()

BEGIN
    SELECT *
    FROM invoices
    WHERE invoice_total - payment_total > 0;
END $$

DELIMITER ;

CALL get_invoices_with_balance();


#3 procedures with parameters and default parameter to select all rows
USE sql_invoicing;

DROP PROCEDURE IF EXISTS get_clients_by_states;

DELIMITER $$
CREATE PROCEDURE get_clients_by_state(state CHAR(2))

BEGIN
    SELECT *
    FROM clients c
    WHERE c.state = IFNULL(state, c.state);
END $$

DELIMITER ;

CALL get_clients_by_state('CA');


#4 procedure to return invoices for a given client
USE sql_invoicing;

DROP PROCEDURE IF EXISTS get_invoices_by_client;

DELIMITER $$
CREATE PROCEDURE get_invoices_by_client(client_id INT)

BEGIN
    SELECT *
    FROM invoices i
    WHERE i.client_id = IFNULL(client_id, i.client_id);
END $$

DELIMITER ;

CALL get_invoices_by_client(1);


#5 procedure parameters for crud
USE sql_invoicing;

DROP PROCEDURE IF EXISTS make_payment;

DELIMITER $$
CREATE PROCEDURE make_payment(
    id INT,
    amount DECIMAL(9, 2),
    date DATE
)

BEGIN
    UPDATE invoices i
    SET i.payment_total = amount,
        i.payment_date = date
    WHERE i.invoice_id = id;
END $$

DELIMITER ;

CALL make_payment(2, 100, '2019-01-01');


#6 triggers basics
USE sql_invoicing;

DROP TRIGGER IF EXISTS payments_after_insert;

DELIMITER $$
CREATE TRIGGER payments_after_insert
    AFTER INSERT ON payments # the inserted row is NEW
    FOR EACH ROW

BEGIN
    UPDATE invoices # can not set trigger on the operated table
    SET payment_total = payment_total + NEW.amount
    WHERE invoice_id = NEW.invoice_id;

    INSERT INTO payments_audit # log changes in another table
    VALUES (NEW.client_id, NEW.date, NEW.amount, 'INSERT', NOW());
END $$

DELIMITER ;


#7 (EX) create a trigger gets fired when we delete a trigger
USE sql_invoicing;

DROP TRIGGER IF EXISTS payments_after_delete;

DELIMITER $$
CREATE TRIGGER payments_after_delete
    AFTER DELETE ON payments # the deleted row is OLD
    FOR EACH ROW

BEGIN
    UPDATE invoices # can not set trigger on the operated table
    SET payment_total = payment_total - OLD.amount
    WHERE invoice_id = OLD.invoice_id;

    INSERT INTO payments_audit # log changes in another table
    VALUES (OLD.client_id, OLD.date, OLD.amount, 'DELETE', NOW());
END $$

DELIMITER ;


#8 test triggers
INSERT INTO payments
VALUES (DEFAULT, 5, 3, '2019-01-01', 10, 1);

DELETE FROM payments
WHERE payment_id = 11;


#9 view triggers
SHOW TRIGGERS;


#10 event: block of SQL code gets executed according to a schedule (first need to turn it on)
SHOW VARIABLES LIKE 'event%';
SET GLOBAL event_scheduler = ON;

USE sql_invoicing;

DROP EVENT IF EXISTS yearly_delete_stale_audit_rows;

DELIMITER $$
CREATE EVENT yearly_delete_stale_audit_rows
    ON SCHEDULE EVERY 1 YEAR STARTS NOW() ENDS '2025-01-01'

DO BEGIN
    DELETE FROM payments_audit pa
    WHERE pa.action_date < NOW() - INTERVAL 0.5 YEAR;
END $$

DELIMITER ;


#11 view events or temporarily alter events
SHOW EVENTS;
ALTER EVENT yearly_delete_stale_audit_rows DISABLE;