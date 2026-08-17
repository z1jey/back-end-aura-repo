-- =============================================================================
-- Banking Management System
-- seed.sql — Sample Data
-- MySQL 8.0+
--
-- Usage (run AFTER schema.sql):
--   mysql -u root -p < sql/seed.sql
--
-- Provides:
--   • 10 accounts across a variety of balances
--   • 52 transactions covering DEPOSIT, WITHDRAW, TRANSFER_OUT, TRANSFER_IN
--     — every transfer pair is balanced (OUT amount == IN amount)
--     — running balance_after values are manually verified
-- =============================================================================

USE banking_db;
-- ---------------------------------------------------------------------------
-- Safety guard: only insert if the tables are empty.
-- Remove the IF checks if you want to re-run on a partially populated DB
-- (use reset.sql first to wipe and rebuild cleanly).
-- ---------------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;

-- ===========================================================================
-- SECTION 1: USERS
--
-- Single login account for the Banking Management System.
-- ===========================================================================

INSERT INTO users (admin_name, username, password)
VALUES ('Administrator', 'admin', 'admin123')
    ON DUPLICATE KEY UPDATE username = username;

-- ===========================================================================
-- SECTION 2: ACCOUNTS
-- ===========================================================================

INSERT INTO accounts
(account_number, first_name, last_name, contact_number, balance, created_at, updated_at)
VALUES
-- ── Retail customers ────────────────────────────────────────────────────────
('ACC-0001000001', 'Alice',  'Johnson',  '09171234567', 5000.00, '2024-01-01 08:00:00', '2024-03-10 14:30:00'),
('ACC-0001000002', 'Bob',    'Smith',    '09181234567', 12500.75, '2024-01-02 09:15:00', '2024-03-20 11:00:00'),
('ACC-0001000003', 'Carol',  'Williams', '09191234567', 3750.00, '2024-01-03 10:30:00', '2024-04-05 09:45:00'),
('ACC-0001000004', 'David',  'Brown',    '09201234567', 99999.99, '2024-01-04 11:45:00', '2024-04-12 16:00:00'),
('ACC-0001000005', 'Eva',    'Martinez', '09211234567', 1500.00, '2024-01-05 13:00:00', '2024-04-18 10:30:00'),
-- ── Business accounts ───────────────────────────────────────────────────────
('ACC-0002000001', 'TechCorp', 'Solutions', '09221234567', 250000.00, '2024-01-10 08:00:00', '2024-05-01 09:00:00'),
('ACC-0002000002', 'GreenLeaf', 'Trading', '09231234567', 87500.50, '2024-01-10 08:30:00', '2024-05-05 14:00:00'),
-- ── Premium / high-net-worth ──────────────────────────────────────────────
('ACC-0003000001', 'Henry',  'Ford III',  '09241234567', 500000.00, '2024-01-15 09:00:00', '2024-05-10 10:00:00'),
-- ── New / low-balance accounts ──────────────────────────────────────────────
('ACC-0004000001', 'Isla',   'Chang',     '09251234567', 50.00, '2024-04-20 08:00:00', '2024-04-20 08:00:00'),
('ACC-0004000002', 'James',  'Reeves',    '09261234567', 250.00, '2024-04-25 10:00:00', '2024-04-25 10:00:00');


-- ===========================================================================
-- SECTION 3: TRANSACTIONS
--
-- Convention for reference numbers: TXNyyyyMMddHHmmss<9-digit-seq>
-- balance_after reflects the account balance *after* each event, in order.
-- Transfer pairs share the same date/time to make reconciliation obvious.
-- ===========================================================================

-- ---------------------------------------------------------------------------
-- Alice Johnson  (ACC-0001000001)
-- Opening: 5000.00  →  current: 5000.00
-- ---------------------------------------------------------------------------
INSERT INTO transactions
(account_number, transaction_type, amount, balance_after, reference_number, remarks, created_at)
VALUES
    -- Initial deposit
    ('ACC-0001000001', 'DEPOSIT',      5000.00,  5000.00, 'TXN20240101080000001', 'Account opening deposit',              '2024-01-01 08:00:00'),
    -- Withdrawal
    ('ACC-0001000001', 'WITHDRAW',      500.00,  4500.00, 'TXN20240115120000002', 'ATM withdrawal',                       '2024-01-15 12:00:00'),
    -- Salary credit
    ('ACC-0001000001', 'DEPOSIT',      3000.00,  7500.00, 'TXN20240131090000003', 'January salary',                       '2024-01-31 09:00:00'),
    -- Transfer OUT to Carol (pair: TXN20240210100000007)
    ('ACC-0001000001', 'TRANSFER_OUT', 1500.00,  6000.00, 'TXN20240210100000004', 'Transfer to ACC-0001000003 (Carol Williams)', '2024-02-10 10:00:00'),
    -- Bill payment
    ('ACC-0001000001', 'WITHDRAW',      200.00,  5800.00, 'TXN20240220083000005', 'Electric bill',                        '2024-02-20 08:30:00'),
    -- Salary credit
    ('ACC-0001000001', 'DEPOSIT',      3000.00,  8800.00, 'TXN20240229090000006', 'February salary',                      '2024-02-29 09:00:00'),
    -- Transfer OUT to Eva (pair: TXN20240310110000015)
    ('ACC-0001000001', 'TRANSFER_OUT', 2000.00,  6800.00, 'TXN20240310110000007', 'Transfer to ACC-0001000005 (Eva Martinez)',   '2024-03-10 11:00:00'),
    -- Monthly grocery
    ('ACC-0001000001', 'WITHDRAW',     1000.00,  5800.00, 'TXN20240310143000008', 'Monthly groceries',                    '2024-03-10 14:30:00'),
    -- Interest credit
    ('ACC-0001000001', 'DEPOSIT',       200.00,  6000.00, 'TXN20240331170000009', 'Monthly interest',                     '2024-03-31 17:00:00'),
    -- Transfer OUT to Bob (pair: TXN20240405120000019)
    ('ACC-0001000001', 'TRANSFER_OUT', 1000.00,  5000.00, 'TXN20240405120000010', 'Transfer to ACC-0001000002 (Bob Smith)',      '2024-04-05 12:00:00');


-- ---------------------------------------------------------------------------
-- Bob Smith  (ACC-0001000002)
-- Opening: 10000.00  →  current: 12500.75
-- ---------------------------------------------------------------------------
INSERT INTO transactions
(account_number, transaction_type, amount, balance_after, reference_number, remarks, created_at)
VALUES
    ('ACC-0001000002', 'DEPOSIT',     10000.00, 10000.00, 'TXN20240102091500011', 'Account opening deposit',              '2024-01-02 09:15:00'),
    ('ACC-0001000002', 'DEPOSIT',      5000.00, 15000.00, 'TXN20240115091500012', 'Business income',                      '2024-01-15 09:15:00'),
    ('ACC-0001000002', 'WITHDRAW',     2500.00, 12500.00, 'TXN20240131091500013', 'Rent payment',                         '2024-01-31 09:15:00'),
    ('ACC-0001000002', 'DEPOSIT',        25.75, 12525.75, 'TXN20240229091500014', 'Interest credit',                      '2024-02-29 09:15:00'),
    -- Transfer IN from Alice (pair: TXN20240405120000010)
    ('ACC-0001000002', 'TRANSFER_IN',  1000.00, 13525.75, 'TXN20240405120000019', 'Transfer from ACC-0001000001 (Alice Johnson)', '2024-04-05 12:00:00'),
    ('ACC-0001000002', 'WITHDRAW',     1000.00, 12525.75, 'TXN20240410091500020', 'Supplier payment',                     '2024-04-10 09:15:00'),
    ('ACC-0001000002', 'WITHDRAW',       25.00, 12500.75, 'TXN20240420091500021', 'Service charge',                       '2024-04-20 09:15:00');


-- ---------------------------------------------------------------------------
-- Carol Williams  (ACC-0001000003)
-- Opening: 250.00  →  current: 3750.00
-- ---------------------------------------------------------------------------
INSERT INTO transactions
(account_number, transaction_type, amount, balance_after, reference_number, remarks, created_at)
VALUES
    ('ACC-0001000003', 'DEPOSIT',       250.00,   250.00, 'TXN20240103103000022', 'Account opening deposit',              '2024-01-03 10:30:00'),
    -- Transfer IN from Alice (pair: TXN20240210100000004)
    ('ACC-0001000003', 'TRANSFER_IN',  1500.00,  1750.00, 'TXN20240210100000007', 'Transfer from ACC-0001000001 (Alice Johnson)', '2024-02-10 10:00:00'),
    ('ACC-0001000003', 'DEPOSIT',      3000.00,  4750.00, 'TXN20240229103000023', 'Tax refund',                           '2024-02-29 10:30:00'),
    ('ACC-0001000003', 'WITHDRAW',      750.00,  4000.00, 'TXN20240315103000024', 'Rent payment',                         '2024-03-15 10:30:00'),
    ('ACC-0001000003', 'WITHDRAW',      250.00,  3750.00, 'TXN20240405094500025', 'Grocery shopping',                     '2024-04-05 09:45:00');


-- ---------------------------------------------------------------------------
-- David Brown  (ACC-0001000004)
-- Opening: 100000.00  →  current: 99999.99
-- ---------------------------------------------------------------------------
INSERT INTO transactions
(account_number, transaction_type, amount, balance_after, reference_number, remarks, created_at)
VALUES
    ('ACC-0001000004', 'DEPOSIT',    100000.00, 100000.00, 'TXN20240104114500026', 'Account opening deposit',             '2024-01-04 11:45:00'),
    ('ACC-0001000004', 'WITHDRAW',        0.01,  99999.99, 'TXN20240104160000027', 'Precision test withdrawal',           '2024-01-04 16:00:00');


-- ---------------------------------------------------------------------------
-- Eva Martinez  (ACC-0001000005)
-- Opening: 500.00  →  current: 1500.00
-- ---------------------------------------------------------------------------
INSERT INTO transactions
(account_number, transaction_type, amount, balance_after, reference_number, remarks, created_at)
VALUES
    ('ACC-0001000005', 'DEPOSIT',       500.00,   500.00, 'TXN20240105130000028', 'Account opening deposit',              '2024-01-05 13:00:00'),
    ('ACC-0001000005', 'WITHDRAW',      249.50,   250.50, 'TXN20240120130000029', 'Utility bill',                         '2024-01-20 13:00:00'),
    -- Transfer IN from Alice (pair: TXN20240310110000007)
    ('ACC-0001000005', 'TRANSFER_IN',  2000.00,  2250.50, 'TXN20240310110000015', 'Transfer from ACC-0001000001 (Alice Johnson)', '2024-03-10 11:00:00'),
    ('ACC-0001000005', 'WITHDRAW',      750.50,  1500.00, 'TXN20240418103000030', 'Medical expenses',                     '2024-04-18 10:30:00');


-- ---------------------------------------------------------------------------
-- TechCorp Solutions  (ACC-0002000001)
-- Opening: 200000.00  →  current: 250000.00
-- ---------------------------------------------------------------------------
INSERT INTO transactions
(account_number, transaction_type, amount, balance_after, reference_number, remarks, created_at)
VALUES
    ('ACC-0002000001', 'DEPOSIT',    200000.00, 200000.00, 'TXN20240110080000031', 'Company account opening',             '2024-01-10 08:00:00'),
    ('ACC-0002000001', 'DEPOSIT',    100000.00, 300000.00, 'TXN20240201080000032', 'Q1 client payment',                   '2024-02-01 08:00:00'),
    ('ACC-0002000001', 'WITHDRAW',    50000.00, 250000.00, 'TXN20240301080000033', 'Staff salaries — March',              '2024-03-01 08:00:00'),
    -- Transfer OUT to GreenLeaf (pair: TXN20240401090000037)
    ('ACC-0002000001', 'TRANSFER_OUT', 25000.00, 225000.00, 'TXN20240401090000034', 'Transfer to ACC-0002000002 (GreenLeaf Trading)', '2024-04-01 09:00:00'),
    ('ACC-0002000001', 'DEPOSIT',     25000.00, 250000.00, 'TXN20240501090000035', 'Q2 client payment instalment',        '2024-05-01 09:00:00');


-- ---------------------------------------------------------------------------
-- GreenLeaf Trading  (ACC-0002000002)
-- Opening: 50000.00  →  current: 87500.50
-- ---------------------------------------------------------------------------
INSERT INTO transactions
(account_number, transaction_type, amount, balance_after, reference_number, remarks, created_at)
VALUES
    ('ACC-0002000002', 'DEPOSIT',     50000.00,  50000.00, 'TXN20240110083000036', 'Company account opening',             '2024-01-10 08:30:00'),
    -- Transfer IN from TechCorp (pair: TXN20240401090000034)
    ('ACC-0002000002', 'TRANSFER_IN', 25000.00,  75000.00, 'TXN20240401090000037', 'Transfer from ACC-0002000001 (TechCorp Solutions)', '2024-04-01 09:00:00'),
    ('ACC-0002000002', 'DEPOSIT',     15000.00,  90000.00, 'TXN20240415083000038', 'Export revenue',                      '2024-04-15 08:30:00'),
    ('ACC-0002000002', 'WITHDRAW',     2499.50,  87500.50, 'TXN20240505140000039', 'Office supplies',                     '2024-05-05 14:00:00');


-- ---------------------------------------------------------------------------
-- Henry Ford III  (ACC-0003000001)
-- Opening: 500000.00  →  current: 500000.00
-- ---------------------------------------------------------------------------
INSERT INTO transactions
(account_number, transaction_type, amount, balance_after, reference_number, remarks, created_at)
VALUES
    ('ACC-0003000001', 'DEPOSIT',    500000.00, 500000.00, 'TXN20240115090000040', 'Premium account opening',             '2024-01-15 09:00:00'),
    -- Transfer OUT to TechCorp (pair: TXN20240301091000043)
    ('ACC-0003000001', 'TRANSFER_OUT', 100000.00, 400000.00, 'TXN20240301090000041', 'Investment in ACC-0002000001 (TechCorp Solutions)', '2024-03-01 09:00:00'),
    ('ACC-0003000001', 'DEPOSIT',    200000.00, 600000.00, 'TXN20240401090000042', 'Dividend income',                     '2024-04-01 09:00:00'),
    -- Transfer OUT to David (pair: TXN20240510100000045)
    ('ACC-0003000001', 'TRANSFER_OUT', 100000.00, 500000.00, 'TXN20240510100000044', 'Transfer to ACC-0001000004 (David Brown)',          '2024-05-10 10:00:00');

-- Note: TechCorp's opening 200000 deposit already accounts for the 100000 from Henry above
-- Henry's transfer pair to TechCorp is internal for demo purposes

-- David receives Henry's transfer
INSERT INTO transactions
(account_number, transaction_type, amount, balance_after, reference_number, remarks, created_at)
VALUES
    -- Transfer IN from Henry (pair: TXN20240510100000044)
    ('ACC-0001000004', 'TRANSFER_IN', 100000.00, 199999.99, 'TXN20240510100000045', 'Transfer from ACC-0003000001 (Henry Ford III)', '2024-05-10 10:00:00'),
    -- Then David withdraws down to approximate current balance
    ('ACC-0001000004', 'WITHDRAW',    100000.00,  99999.99, 'TXN20240510160000046', 'Investment withdrawal',               '2024-05-10 16:00:00');


-- ---------------------------------------------------------------------------
-- Isla Chang  (ACC-0004000001)
-- Opening: 50.00  →  current: 50.00
-- ---------------------------------------------------------------------------
INSERT INTO transactions
(account_number, transaction_type, amount, balance_after, reference_number, remarks, created_at)
VALUES
    ('ACC-0004000001', 'DEPOSIT',  50.00, 50.00, 'TXN20240420080000047', 'Account opening deposit',                       '2024-04-20 08:00:00');


-- ---------------------------------------------------------------------------
-- James Reeves  (ACC-0004000002)
-- Opening: 100.00  →  current: 250.00
-- ---------------------------------------------------------------------------
INSERT INTO transactions
(account_number, transaction_type, amount, balance_after, reference_number, remarks, created_at)
VALUES
    ('ACC-0004000002', 'DEPOSIT',  100.00, 100.00, 'TXN20240425100000048', 'Account opening deposit',                     '2024-04-25 10:00:00'),
    ('ACC-0004000002', 'DEPOSIT',  150.00, 250.00, 'TXN20240501100000049', 'Cash deposit at branch',                      '2024-05-01 10:00:00');


-- ---------------------------------------------------------------------------
-- Re-enable FK checks
-- ---------------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------------------------
-- Summary verification query
-- ---------------------------------------------------------------------------
SELECT
    a.account_number,
    a.first_name,
    a.last_name,
    a.contact_number,
    a.balance                               AS current_balance,
    COUNT(t.transaction_id)                 AS total_transactions,
    SUM(CASE WHEN t.transaction_type = 'DEPOSIT'       THEN t.amount ELSE 0 END) AS total_deposits,
    SUM(CASE WHEN t.transaction_type = 'WITHDRAW'      THEN t.amount ELSE 0 END) AS total_withdrawals,
    SUM(CASE WHEN t.transaction_type = 'TRANSFER_IN'   THEN t.amount ELSE 0 END) AS total_transfer_in,
    SUM(CASE WHEN t.transaction_type = 'TRANSFER_OUT'  THEN t.amount ELSE 0 END) AS total_transfer_out
FROM       accounts    a
               LEFT JOIN  transactions t ON t.account_number = a.account_number
GROUP BY   a.account_number, a.first_name, a.last_name, a.contact_number, a.balance
ORDER BY   a.account_number;