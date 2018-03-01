CREATE TABLE FIRST_TABLE (somekey integer primary key, one_attribute varchar(20) not null, snd_attrib date null);

INSERT INTO FIRST_TABLE VALUES (100, 'HUNDRED', SYSDATE()), (200, 'TWO HUNDRED', NULL), (300, 'THREE HUNDRED', SYSDATE());

CREATE TABLE SECOND_TABLE (sndkey varchar(5) primary key, disposable_date date null, a_number decimal(10,2) not null);

INSERT INTO SECOND_TABLE VALUES ('1ST', SYSDATE(), 200.3), ('2ND', SYSDATE(), 0.4), ('3TH', SYSDATE(), -1.0);
