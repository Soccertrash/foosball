# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table player (
  id                        bigint not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  acronym                   varchar(255),
  constraint pk_player primary key (id))
;

create table team (
  id                        bigint not null,
  player1_id                bigint,
  player2_id                bigint,
  constraint pk_team primary key (id))
;

create sequence player_seq;

create sequence team_seq;

alter table team add constraint fk_team_player1_1 foreign key (player1_id) references player (id) on delete restrict on update restrict;
create index ix_team_player1_1 on team (player1_id);
alter table team add constraint fk_team_player2_2 foreign key (player2_id) references player (id) on delete restrict on update restrict;
create index ix_team_player2_2 on team (player2_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists player;

drop table if exists team;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists player_seq;

drop sequence if exists team_seq;

