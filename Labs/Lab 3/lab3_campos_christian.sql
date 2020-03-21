set client_min_messages="warning";
DROP TABLE IF EXISTS Graduate, Project, Professor, Dept CASCADE;

DROP TABLE IF EXISTS Works_Proj;
DROP TABLE IF EXISTS Work_In;
DROP TABLE IF EXISTS Work_Dept;

CREATE TABLE Professor (SSN CHAR(11) NOT NULL,name text,age INTEGER,rank text,specialty text, PRIMARY KEY(SSN));

CREATE TABLE Dept (dno INTEGER NOT NULL,dname text,office text,SSN CHAR(11),PRIMARY KEY(dno), FOREIGN KEY (SSN) REFERENCES Professor(SSN));


CREATE TABLE Project (pno INTEGER NOT NULL, sponsor text, start_date text, end_date text, budget float,SSN CHAR(11),
			PRIMARY KEY(pno),FOREIGN KEY (SSN) REFERENCES Professor(SSN));

CREATE TABLE Graduate (SSN CHAR(11) NOT NULL, name text, age INTEGER, dept_pg text,advisor_SSN CHAR(11),dno INTEGER, 
			PRIMARY KEY(SSN),FOREIGN KEY (advisor_SSN) REFERENCES Graduate(SSN),FOREIGN KEY (dno) REFERENCES Dept(dno));


CREATE TABLE Works_Proj (since text, pno INTEGER NOT NULL, GSSN CHAR(11) NOT NULL,PSSN CHAR(11) NOT NULL,
	PRIMARY KEY(pno, GSSN),FOREIGN KEY (pno) REFERENCES Project(pno),FOREIGN KEY (GSSN) REFERENCES Graduate(SSN),FOREIGN KEY (PSSN) REFERENCES Professor(SSN));

CREATE TABLE Work_In (SSN CHAR(11) NOT NULL,pno INTEGER NOT NULL,
			PRIMARY KEY(SSN, pno),FOREIGN KEY (SSN) REFERENCES Professor(SSN),FOREIGN KEY (pno) REFERENCES Project(pno));

CREATE TABLE Work_Dept (time_pc text,SSN CHAR(11) NOT NULL,dno INTEGER NOT NULL,
			PRIMARY KEY(SSN, dno),FOREIGN KEY (SSN) REFERENCES Professor(SSN),FOREIGN KEY (dno) REFERENCES Dept(dno));


set client_min_messages="warning";
DROP TABLE IF EXISTS Place, Telephone, Musicians, Album, Instrument, Songs CASCADE;

DROP TABLE IF EXISTS Lives;
DROP TABLE IF EXISTS Perform;
DROP TABLE IF EXISTS Plays;

CREATE TABLE Place(address text NOT NULL,PRIMARY KEY(address));

CREATE TABLE Telephone(phone_no text,adress text,FOREIGN KEY (adress) REFERENCES Place(address));

CREATE TABLE Musicians(SSN CHAR(11) NOT NULL,name text,PRIMARY KEY(SSN));

CREATE TABLE Album(albumIdentifier text NOT NULL,copyrightDate text,speed text,title text,SSN CHAR(11),
			PRIMARY KEY(albumIdentifier),FOREIGN KEY (SSN) REFERENCES Musicians(SSN));

CREATE TABLE Instrument(intrId text NOT NULL,ddname text,key text,PRIMARY KEY(intrId));

CREATE TABLE Songs(songId text NOT NULL,title text,author text,albumIdentifier text,
		PRIMARY KEY(songId),FOREIGN KEY (albumIdentifier) REFERENCES Album(albumIdentifier));


CREATE TABLE Lives(SSN CHAR(11) NOT NULL,address text NOT NULL,
		PRIMARY KEY(SSN, address),FOREIGN KEY (SSN) REFERENCES Musicians(SSN),FOREIGN KEY (address) REFERENCES Place(address));


CREATE TABLE Perform(SSN CHAR(11) NOT NULL,songId text NOT NULL,
		    PRIMARY KEY(SSN, songId),FOREIGN KEY (SSN) REFERENCES Musicians(SSN),FOREIGN KEY (songId) REFERENCES Songs(songId));

CREATE TABLE Plays(SSN CHAR(11) NOT NULL,intrId text NOT NULL,
		   PRIMARY KEY(SSN, intrId),FOREIGN KEY (SSN) REFERENCES Musicians(SSN),FOREIGN KEY (intrId) REFERENCES Instrument(intrId));



