
```sql
CREATE DATABASE IF NOT EXISTS book_catalog;
USE book_catalog;

CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    publication_year INT,
    price DECIMAL(10, 2)
);
```


