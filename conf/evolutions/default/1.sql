# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table game (
  id                        bigint not null,
  goals_team_red            integer,
  goals_team_blue           integer,
  team_blue_id              bigint,
  team_red_id               bigint,
  constraint pk_game primary key (id))
;

create table player (
  id                        bigint not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  nickname                  varchar(255),
  constraint pk_player primary key (id))
;

create table team (
  id                        bigint not null,
  player1_id                bigint,
  player2_id                bigint,
  constraint pk_team primary key (id))
;

create sequence game_seq;

create sequence player_seq;

create sequence team_seq;

alter table game add constraint fk_game_teamBlue_1 foreign key (team_blue_id) references team (id) on delete restrict on update restrict;
create index ix_game_teamBlue_1 on game (team_blue_id);
alter table game add constraint fk_game_teamRed_2 foreign key (team_red_id) references team (id) on delete restrict on update restrict;
create index ix_game_teamRed_2 on game (team_red_id);
alter table team add constraint fk_team_player1_3 foreign key (player1_id) references player (id) on delete restrict on update restrict;
create index ix_team_player1_3 on team (player1_id);
alter table team add constraint fk_team_player2_4 foreign key (player2_id) references player (id) on delete restrict on update restrict;
create index ix_team_player2_4 on team (player2_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists game;

drop table if exists player;

drop table if exists team;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists game_seq;

drop sequence if exists player_seq;

drop sequence if exists team_seq;

