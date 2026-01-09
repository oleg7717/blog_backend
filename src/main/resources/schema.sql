create table if not exists posts(
  id bigserial primary key,
  title varchar(50) not null,
  text varchar(10000) not null,
  likesCount bigserial not null,
  commentsCount integer not null
);

create table if not exists tags(
  postid bigserial not null references posts(id) on delete cascade,
  tagname varchar(50) not null,
  UNIQUE (postid, tagname)
);

create index if not exists idx_tags_postid on tags (postid);

create table if not exists comments(
    id bigserial primary key,
    text varchar(256) not null,
    postid bigserial not null references posts(id) on delete cascade
);

/*-- Create rule for INSERT
create or replace rule comment_insert_rule as
    on insert to comments
    do also
update posts
set commentsCount = commentsCount + 1
WHERE id = NEW.postid;

-- Create rule for DELETE
create or replace rule comment_delete_rule as
    on delete to comments
    do also
update posts
set commentsCount = commentsCount - 1
WHERE id = OLD.postid;*/


/*truncate posts restart identity cascade;

insert into posts(title, text, likesCount, commentsCount)
  values ('Пост про спорт', 'Нет ничего проще, ' ||
'чем составить символическую сборную лучших баскетболистов XXI века в рамках подведения итогов первых 25 лет. ' ||
'И именно поэтому одновременно нет и ничего сложнее: Леброн, Кобе и Шак – это имена нарицательные, и как-то ' ||
'бессмысленно объяснять, почему их присутствие в такой команде обязательно. Кроме того, есть и другие смущающие ' ||
'моменты: что это за сборная мира, если в ней только люди с американскими паспортами? В какой баскетбол должна играть' ||
' такая команда, если современная игра существует по другим законам, чем та, что еще 10 лет назад? Что важнее для ' ||
'представителей такой сборной – выступление собственно за национальную сборную или их личный уровень как таковой? ' ||
'И так далее.', 0, 1);
insert into posts(title, text, likesCount, commentsCount)
  values ('Пост про финансы', 'Текст поста', 0, 0);
insert into posts(title, text, likesCount, commentsCount)
  values ('Пост про политику', 'Текст поста', 0, 2);

insert into tags(postid, tagname) values (1, 'sport');
insert into tags(postid, tagname) values (2, 'finance');
insert into tags(postid, tagname) values (2, 'politic');
insert into tags(postid, tagname) values (3, 'sport');
insert into tags(postid, tagname) values (3, 'politic');

insert into comments(postid, text) values (1, 'Комментарий к посту 1');
insert into comments(postid, text) values (3, 'Первый комментарий к посту 3');
insert into comments(postid, text) values (3, 'Второй комментарий к посту 3');*/
