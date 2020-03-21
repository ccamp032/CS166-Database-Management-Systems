DROP TABLE Pilot CASCADE;
DROP TABLE Flight CASCADE;
DROP TABLE Schedule CASCADE;
DROP TABLE Reservation CASCADE;
DROP TABLE Waitlisted CASCADE;
DROP TABLE Confirmed CASCADE;
DROP TABLE Reserved CASCADE;
DROP TABLE Customer CASCADE;
DROP TABLE Plane CASCADE;
DROP TABLE Technician CASCADE;
--DROP TABLE has1 CASCADE;
--DROP TABLE repair_request CASCADE;
DROP TABLE uses CASCADE;
DROP TABLE has CASCADE;
DROP TABLE repairs CASCADE;

CREATE TABLE Flight (
                      flight_num INTEGER NOT NULL,
                      destination text,
                      source text,
                      actual_depart_time text,
                      actual_depart_date text,
                      actual_arrive_time text,
                      actual_arrive_date text,
                      num_stops INTEGER,
                      num_sold INTEGER,
                      cost FLOAT,
                      PRIMARY KEY(flight_num)
                        );
CREATE TABLE Schedule (
                       flight_num INTEGER NOT NULL,
                       day text,
                       depart_time text,
                       arrive_time text,
                       FOREIGN KEY (flight_num) REFERENCES Flight(flight_num)
                      );
CREATE TABLE Customer (
                       ID INTEGER NOT NULL,
                       first_name text,
                       last_name text,
                       gender text, 
                       date_of_birth text,
                       address text,
                       contact_num INTEGER,
                       ZIP_code INTEGER,
                       PRIMARY KEY(ID)
                      );
CREATE TABLE has (
                 flight_num INTEGER NOT NULL,
                 ID INTEGER NOT NULL,
				         Rnum INTEGER NOT NULL,
                 PRIMARY KEY (flight_num,ID,Rnum),
                 FOREIGN KEY (flight_num) REFERENCES Flight(flight_num),
                 FOREIGN KEY (ID) REFERENCES Customer(ID)
				         FOREIGN KEY (Rnum) REFERENCES Reservation(Rnum)
                 );
CREATE TABLE Pilot (  
                      ID INTEGER,
                      name text,
                      nationality text,
					  PRIMARY KEY(ID)
                   );
-- primary key on id ?
CREATE TABLE Plane  (
                      model text,
                      ID INTEGER,
                      make text,
                      age  INTEGER,
                      num_seats INTEGER,
                      PRIMARY KEY (ID)
                    );
CREATE TABLE Techician(
                      ID INTEGER NOT NULL,
                      PRIMARY KEY (ID)

                      );
CREATE TABLE Reservation(
                          Rnum INTEGER NOT NULL,
                          PRIMARY KEY (Rnum)
                         );
CREATE TABLE Waitlisted (
                        Rnum INTEGER REFERENCES Reservation(Rnum),
                        PRIMARY KEY(Rnum) 
                        );
CREATE TABLE Confirmed (
                        Rnum INTEGER REFERENCES Reservation(Rnum),
                        PRIMARY KEY(Rnum) 
                        );
CREATE TABLE Reserved (
                        Rnum INTEGER REFERENCES Reservation(Rnum),
                        PRIMARY KEY(Rnum) 
                      );
CREATE TABLE repairs(
                     date text,
                     code text,
                     PRIMARY KEY (ID,ID),
                     FOREIGN KEY (ID) REFERENCES Techician(ID),
                     FOREIGN KEY (ID) REFERENCES Plane(ID)
                    ); 



set client_min_messages="warning";

DROP TABLE IF EXISTS plane, pilot, flight, schedule, reservation, waitlisted, confirmed,
                     reserved, customer, plane, technician, repair_request, uses, has, repairs CASCADE;

CREATE TABLE plane ( ID text NOT NULL,
                     model text,
                     make text,
                     age text,
                     num_seats integer,
                     PRIMARY KEY(ID));

CREATE TABLE pilot ( ID text NOT NULL,
                     pilot_name text,
                     nationality text,
                     plane_id text,
                     PRIMARY KEY(ID),
                     FOREIGN KEY (plane_id) REFERENCES plane(ID));

CREATE TABLE flight ( flight_num text NOT NULL,
                      cost float,
                      num_sold integer,
                      num_stops integer,
                      actual_arrive_date date,
                      actual_arrive_time time,
                      actual_depart_date date,
                      actual_depart_time time,
                      source text,
                      destination text,
                      pilot_id text,
                      PRIMARY KEY(flight_num),
                      FOREIGN KEY (pilot_id) REFERENCES pilot(ID) ON DELETE NO ACTION);

CREATE TABLE schedule ( flight_num text NOT NULL,
                        day text,
                        depart_time time,
                        arrive_time time,
                        PRIMARY KEY(flight_num),
                        FOREIGN KEY (flight_num) REFERENCES flight(flight_num) ON DELETE CASCADE);

------------------------------------------------------------------------------------------

CREATE TABLE reservation ( Rnum text NOT NULL,
                           PRIMARY KEY(Rnum));

CREATE TABLE waitlisted ( Rnum TEXT REFERENCES reservation(Rnum),
                          PRIMARY KEY(Rnum));

CREATE TABLE confirmed ( Rnum TEXT REFERENCES reservation(Rnum),
                          PRIMARY KEY(Rnum));

CREATE TABLE reserved ( Rnum TEXT REFERENCES reservation(Rnum),
                          PRIMARY KEY(Rnum));

------------------------------------------------------------------------------------------

CREATE TABLE customer ( ID text NOT NULL,
                        first_name text,
                        last_name text,
                        gender text,
                        data_of_birth date,
                        customer_address text,
                        contact_num text,
                        ZIP_code text,
                        PRIMARY KEY(ID));

CREATE TABLE technician ( ID text NOT NULL,
                          PRIMARY KEY(ID));

------------------------------------------------------------------------------------------

CREATE TABLE repair_request ( pilot_id text,
                              plane_id text,
                              technician_id text,
                              repair_request_ID text,
                              PRIMARY KEY(pilot_id, plane_id, technician_id),
                              FOREIGN KEY (pilot_id) REFERENCES pilot(ID),
                              FOREIGN KEY (plane_id) REFERENCES plane(ID),
                              FOREIGN KEY (technician_id) REFERENCES technician(ID));

CREATE TABLE uses ( flight_num text,
                    pilot_id text,
                    plane_id text,
                    PRIMARY KEY(flight_num, pilot_id, plane_id),
                    FOREIGN KEY (flight_num) REFERENCES flight(flight_num),
                    FOREIGN KEY (pilot_id) REFERENCES pilot(ID),
                    FOREIGN KEY (plane_id) REFERENCES plane(ID));

CREATE TABLE has ( flight_num text,
                   customer_id text,
                   Rnum text,
                   PRIMARY KEY (flight_num, customer_id, Rnum),
                   FOREIGN KEY (flight_num) REFERENCES flight(flight_num),
                   FOREIGN KEY (customer_id) REFERENCES customer(ID),
                   FOREIGN KEY (Rnum) REFERENCES reservation(Rnum));


CREATE TABLE repairs ( plane_id text,
                       technician_id text,
                       pilot_id text,
                       repair_date date,
                       repair_code text,
                       PRIMARY KEY(plane_id, technician_id, pilot_id),
                       FOREIGN KEY (plane_id) REFERENCES plane(ID),
                       FOREIGN KEY (technician_id) REFERENCES technician(ID),
                       FOREIGN KEY (pilot_id) REFERENCES pilot(ID) ON DELETE NO ACTION)