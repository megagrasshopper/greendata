CREATE DATABASE bank;
CREATE ROLE greendata login;
ALTER ROLE greendata PASSWORD 'greendata';
GRANT CONNECT ON DATABASE bank TO greendata;
GRANT ALL PRIVILEGES ON DATABASE bank TO greendata;