DROP TABLE IF EXISTS LIKELIST;
DROP TABLE IF EXISTS FRIENDLIST;
DROP TABLE IF EXISTS FILMGENRE;
DROP TABLE IF EXISTS FILMCATEGORY;
DROP TABLE IF EXISTS "Favorites_films";
DROP TABLE IF EXISTS GENRE;
DROP TABLE IF EXISTS CATEGORY;
DROP TABLE IF EXISTS "StatusList";
DROP TABLE IF EXISTS FILMBASE;
DROP TABLE IF EXISTS USERBASE;



create table USERBASE
(
    USER_ID  INTEGER                not null,
    EMAIL    CHARACTER VARYING(200) not null
        constraint "uc_UserBase_email"
            unique,
    LOGIN    CHARACTER VARYING(200) not null
        constraint "uc_UserBase_login"
            unique,
    NAME     CHARACTER VARYING(200) not null,
    BIRTHDAY DATE                   not null,
    constraint "pk_UserBase"
        primary key (USER_ID)
);
create table FILMBASE
(
    FILM_ID     INTEGER                 not null,
    NAME        CHARACTER VARYING(100)  not null,
    DESCRIPTION CHARACTER VARYING(1000) not null,
    RELEASEDATE DATE                    not null,
    DURATION    INTEGER                 not null,
    LIKES       INTEGER default 0       not null,
    constraint "pk_FilmBase"
        primary key (FILM_ID)
);
create table "StatusList"
(
    STATUS_ID       INTEGER                not null,
    "invite_status" CHARACTER VARYING(100) not null,
    constraint STATUSLIST_PK
        primary key (STATUS_ID)
);
create table CATEGORY
(
    CATEGORY_ID INTEGER                not null,
    NAME        CHARACTER VARYING(100) not null
        constraint "uc_Category_name"
            unique,
    constraint CATEGORY_PK
        primary key (CATEGORY_ID)
);
create table GENRE
(
    GENRE_ID INTEGER                not null,
    NAME     CHARACTER VARYING(100) not null
        constraint "uc_Genre_name"
            unique,
    constraint GENRE_PK
        primary key (GENRE_ID)
);
create table "Favorites_films"
(
    "user_id" INTEGER not null,
    "film_id" INTEGER not null,
    constraint FAVORITES_FILMS_USERBASE_USER_ID_FK
        foreign key ("user_id") references USERBASE,
    constraint "fk_Favorites_films_film_id"
        foreign key ("film_id") references FILMBASE
);

create table FILMCATEGORY
(
    FILM_ID     INTEGER not null,
    CATEGORY_ID INTEGER not null,
    constraint FILMCATEGORY_CATEGORY_CATEGORY_ID_FK
        foreign key (CATEGORY_ID) references CATEGORY,
    constraint "fk_FilmCategory_film_id"
        foreign key (FILM_ID) references FILMBASE
);
create table FILMGENRE
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILMGENRE_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE,
    constraint "fk_FilmGenre_film_id"
        foreign key (FILM_ID) references FILMBASE
);
create table FRIENDLIST
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    STATUS_ID INTEGER default 1,
    constraint FRIENDLIST_STATUSLIST_STATUS_ID_FK
        foreign key (STATUS_ID) references "StatusList",
    constraint FRIENDLIST_USERBASE_USER_ID_FK
        foreign key (USER_ID) references USERBASE,
    constraint "fk_FriendList_friend_id"
        foreign key (FRIEND_ID) references USERBASE
);

create table LIKELIST
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint "fk_Likelist_film_id"
        foreign key (FILM_ID) references FILMBASE,
    constraint "fk_Likelist_user_id"
        foreign key (USER_ID) references USERBASE
);

