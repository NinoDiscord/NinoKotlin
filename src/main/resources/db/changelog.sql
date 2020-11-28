--liquibase formatted sql

--changeset oded.shapira:1
CREATE TABLE GuildGeneralSettings
(
    id          serial primary key,
    guild_id    varchar(18) unique,
    prefix      varchar(20),
    locale_code varchar(8) not null
);
--changeset oded.shapira:2
CREATE TABLE MutedRoles
(
    id            serial primary key,
    guild_id      varchar(18) unique,
    muted_role_id varchar(18) unique
);
--changeset auguwu:3
CREATE TABLE ModLogSettings
(
    id                   serial primary key,
    guild_id             varchar(18) unique,
    mod_log_channel_id   varchar(18),
    guild_log_channel_id varchar(18)
);
--changeset auguwu:4
--the guild log channel will have its own table
ALTER TABLE ModLogSettings DROP guild_log_channel_id;
--changeset oded.shapira:5
CREATE TABLE CaseIds
(
    id serial primary key,
    guild_id varchar(18) unique,
    last_case_id int
)