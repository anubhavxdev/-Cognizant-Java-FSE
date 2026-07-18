CREATE TABLE country (
    co_code VARCHAR(2) PRIMARY KEY,
    co_name VARCHAR(50)
);

CREATE TABLE stock (
  st_id INT NOT NULL AUTO_INCREMENT,
  st_code VARCHAR(10), 
  st_date DATE,
  st_open NUMERIC(10,2),
  st_close NUMERIC(10,2), 
  st_volume NUMERIC,
  PRIMARY KEY (st_id)
);

CREATE TABLE department (
  dp_id INT NOT NULL AUTO_INCREMENT,
  dp_name VARCHAR(50),
  PRIMARY KEY (dp_id)
);

CREATE TABLE employee (
  em_id INT NOT NULL AUTO_INCREMENT,
  em_name VARCHAR(50),
  em_salary NUMERIC(10,2),
  em_permanent BOOLEAN,
  em_date_of_birth DATE,
  em_dp_id INT,
  PRIMARY KEY (em_id),
  FOREIGN KEY (em_dp_id) REFERENCES department(dp_id)
);

CREATE TABLE skill (
  sk_id INT NOT NULL AUTO_INCREMENT,
  sk_name VARCHAR(50),
  PRIMARY KEY (sk_id)
);

CREATE TABLE employee_skill (
  es_em_id INT NOT NULL,
  es_sk_id INT NOT NULL,
  PRIMARY KEY (es_em_id, es_sk_id),
  FOREIGN KEY (es_em_id) REFERENCES employee(em_id),
  FOREIGN KEY (es_sk_id) REFERENCES skill(sk_id)
);
