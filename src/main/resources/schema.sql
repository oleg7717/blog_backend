create table if not exists posts(
  id bigserial primary key,
  title varchar(50) not null,
  text varchar(10000) not null,
  likesCount bigserial not null,
  commentsCount integer not null
);

create table if not exists tags(
  postid bigserial not null,
  tagname varchar(50) not null,
  unique (postid, tagname)
);

CREATE INDEX IF NOT EXISTS idx_tags_postid ON tags (postid);

TRUNCATE posts RESTART IDENTITY;
insert into posts(title, text, likesCount, commentsCount) values ('Пост про спорт', 'Нет ничего проще, чем составить символическую сборную лучших баскетболистов XXI века в рамках подведения итогов первых 25 лет. ' ||
'И именно поэтому одновременно нет и ничего сложнее: Леброн, Кобе и Шак – это имена нарицательные, и как-то бессмысленно объяснять, почему их присутствие в такой команде обязательно. ' ||
'Кроме того, есть и другие смущающие моменты: что это за сборная мира, если в ней только люди с американскими паспортами? В какой баскетбол должна играть такая команда, если современная игра существует по другим законам, чем та, что еще 10 лет назад? Что важнее для представителей такой сборной – выступление собственно за национальную сборную или их личный уровень как таковой? ' ||
'И так далее.' ||
'Поэтому одной символической сборной просто невозможно ограничиться. Ниже сразу семь конфигураций символических сборных, тот самый минимум миниморум, что может оставить всеобъемлющее представление об этом спорте в XXI веке.', 30, 0);
insert into posts(title, text, likesCount, commentsCount) values ('Пост про финансы', 'Текст поста', 25, 5);
insert into posts(title, text, likesCount, commentsCount) values ('Пост про политику', 'Текст поста', 28, 15);

TRUNCATE tags RESTART IDENTITY;
insert into tags(postid, tagname) values (1, 'sport');
insert into tags(postid, tagname) values (2, 'finance');
insert into tags(postid, tagname) values (2, 'politic');
insert into tags(postid, tagname) values (3, 'sport');
insert into tags(postid, tagname) values (3, 'politic');
