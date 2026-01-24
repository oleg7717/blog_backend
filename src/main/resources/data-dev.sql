delete from posts;
alter table posts alter column id restart with 1;
alter table comments alter column id restart with 1;

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
insert into comments(postid, text) values (3, 'Второй комментарий к посту 3');