DROP TABLE IF EXISTS supplier CASCADE;

CREATE TABLE supplier
(
  id SERIAL PRIMARY KEY,
  name varchar(40)
);

DROP TABLE IF EXISTS category CASCADE;

CREATE TABLE category
(
  id SERIAL PRIMARY KEY,
  name varchar(40)
);

DROP TABLE IF EXISTS product CASCADE;

CREATE TABLE product
(
  id SERIAL PRIMARY KEY,
  name varchar(40),
  description text,
  price FLOAT,
  currency varchar(3),
  category_id INT NOT NULL,
  CONSTRAINT category_id FOREIGN KEY (category_id)
  REFERENCES public.category (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  supplier_id int NOT NULL,
  CONSTRAINT supplier_id FOREIGN KEY (supplier_id)
  REFERENCES public.supplier (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);


DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
  id SERIAL PRIMARY KEY,
  name text,
  email text,
  password text,
  shipping_info text,
  billing_info text
);

DROP TABLE IF EXISTS orders CASCADE;

CREATE TABLE orders
(
  id SERIAL PRIMARY KEY,
  status text,
  user_id INT NOT NULL,
  CONSTRAINT user_id FOREIGN KEY (user_id)
  REFERENCES public.users (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);



DROP TABLE IF EXISTS lineitem CASCADE;

CREATE TABLE lineitem
(
  id SERIAL PRIMARY KEY,
  quantity INT,
  order_id INT NOT NULL,
  CONSTRAINT order_id FOREIGN KEY (order_id)
  REFERENCES public.orders (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  product_id INT NOT NULL,
  CONSTRAINT product_id FOREIGN KEY (product_id)
  REFERENCES public.product (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION



);



