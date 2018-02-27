PRAGMA foreign_keys = OFF;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS messages;

PRAGMA foreign_keys = ON;

CREATE TABLE users(
username TEXT,
password TEXT,
PRIMARY KEY(username)
);

CREATE TABLE messages(
id INTEGER,
username TEXT, 
message TEXT,
time TEXT,
timeLong INTEGER,
PRIMARY KEY(id)
FOREIGN KEY (username) REFERENCES users(username)

);


INSERT INTO users(username, password)
VALUES("anton", "123"),("olafur", "456"), ("andreas", "789");


