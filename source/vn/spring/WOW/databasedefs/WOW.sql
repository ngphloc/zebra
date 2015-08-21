DROP DATABASE WOW;
CREATE DATABASE WOW;
USE WOW;

CREATE TABLE profile(
  id int not null unique auto_increment,
  primary key (id));

CREATE TABLE profrec(
  id int not null unique auto_increment,
  user int not null,
  name varchar(150) not null,
  type int not null,
  persistent int not null,
  value text,
  primary key (id));
  
CREATE TABLE concept(
  id int not null unique auto_increment,
  name varchar(150) not null unique,
  description text,
  expr text,
  resource text,
  primary key (id));

CREATE TABLE attribute(
  id int not null unique auto_increment,
  concept int not null,
  name varchar(150) not null,
  description text,
  def text,
  type int not null,
  readonly int not null,
  system int not null,
  persistent int not null,
  primary key (id));

CREATE TABLE action(
  id int not null unique auto_increment,
  attribute int not null,
  concept int not null,
  trigger int not null,
  condition text,
  primary key (id));

CREATE TABLE assignment(
  id int not null unique auto_increment,
  action int not null,
  attribute int not null,
  concept int not null,
  truestat int not null,
  var text,
  expr text,
  primary key (id));
  