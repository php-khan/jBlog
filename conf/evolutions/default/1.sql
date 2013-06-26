# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table blog (
  id                        bigint not null,
  title                     varchar(255),
  create_time               timestamp,
  publish                   boolean,
  content                   TEXT,
  author_id                 bigint,
  constraint pk_blog primary key (id))
;

create table Comment (
  id                        bigint not null,
  parent_blog_id            bigint,
  body                      varchar(255),
  create_time               timestamp,
  author_id                 bigint,
  constraint pk_Comment primary key (id))
;

create table user (
  id                        bigint not null,
  username                  varchar(255),
  password                  varchar(255),
  create_date               timestamp,
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id))
;

create sequence blog_seq;

create sequence Comment_seq;

create sequence user_seq;

alter table blog add constraint fk_blog_author_1 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_blog_author_1 on blog (author_id);
alter table Comment add constraint fk_Comment_parentBlog_2 foreign key (parent_blog_id) references blog (id) on delete restrict on update restrict;
create index ix_Comment_parentBlog_2 on Comment (parent_blog_id);
alter table Comment add constraint fk_Comment_author_3 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_Comment_author_3 on Comment (author_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists blog;

drop table if exists Comment;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists blog_seq;

drop sequence if exists Comment_seq;

drop sequence if exists user_seq;

