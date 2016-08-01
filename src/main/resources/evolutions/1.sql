create table "ADDRESSE" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"STREET" VARCHAR NOT NULL,"CITY" VARCHAR NOT NULL,"COUNTRY" VARCHAR NOT NULL,"ZIP_CODE" VARCHAR NOT NULL);
create unique index "idx_a_sccz_u" on "ADDRESSE" ("STREET","CITY","COUNTRY","ZIP_CODE");
create table "PERSON_INFO" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"FIRST_NAME" VARCHAR NOT NULL,"LAST_NAME" VARCHAR NOT NULL,"EMAIL" VARCHAR NOT NULL,"ADDRESS_ID" BIGINT);
create unique index "idx_pi_e_u" on "PERSON_INFO" ("EMAIL");
alter table "PERSON_INFO" add constraint "PI_A_FK" foreign key("ADDRESS_ID") references "ADDRESSE"("id") on update NO ACTION on delete NO ACTION;
create table "USERS" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"EMAIL" VARCHAR NOT NULL,"PERSON_INFO_ID" BIGINT NOT NULL);
alter table "USERS" add constraint "U_PI_FK" foreign key("PERSON_INFO_ID") references "PERSON_INFO"("id") on update NO ACTION on delete NO ACTION;