create table if not exists posts(
  id bigserial primary key,
  title varchar(50) not null,
  text varchar(256) not null,
  likesCount bigserial not null,
  commentsCount integer not null);

insert into posts(title, text, likesCount, commentsCount) values ('Пост 1', 'Текст поста', 30, 0);
insert into posts(title, text, likesCount, commentsCount) values ('Пост 2', 'Текст поста', 25, 5);
insert into posts(title, text, likesCount, commentsCount) values ('Пост 4', 'Текст поста', 28, 15);