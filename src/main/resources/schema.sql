create table if not exists posts(
  id bigserial primary key,
  title varchar(50) not null,
  text varchar(10000) not null,
  likesCount bigint not null,
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
