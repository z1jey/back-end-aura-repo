-- =============================================================================
-- Banking Management System
-- reset.sql — Drop All Tables and Rebuild from Scratch
-- MySQL 8.0+
--
-- WARNING: This script deletes ALL data permanently.
--          Only use in development / testing environments.
--
-- Usage:
--   mysql -u root -p < sql/reset.sql
--
-- After running this file, re-run:
--   mysql -u root -p < sql/schema.sql
--   mysql -u root -p < sql/seed.sql
-- =============================================================================

USE banking_db;

-- Disable FK checks so tables can be dropped in any order
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;

SET FOREIGN_KEY_CHECKS = 1;

SELECT 'All tables dropped. Run schema.sql then seed.sql to rebuild.' AS status;
