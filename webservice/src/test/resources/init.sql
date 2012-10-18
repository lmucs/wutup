insert into user (id, firstName, lastName, email, nickname) values (1, 'Honda', 'Prius', '40mpg@gmail.com', 'hybrid');
insert into user (id, firstName, lastName, email, nickname) values (2, 'Ned', 'Stark', 'naked@winterfell.com', 'headless');
insert into user (id, firstName, lastName, email, nickname) values (3, 'Jack', 'Handy', 'jh1942@lion.lmu.edu', 'DeepThoughts');
insert into user (id, firstName, lastName, email, nickname) values (4, 'Heather', 'Northington', 'h.north@lion.lmu.edu', 'hnorth');
insert into user (id, firstName, lastName, email, nickname) values (5, 'Ignatius', 'Krumpkin', 'iggy@hotmail.com', 'IKOK');
insert into user (id, firstName, lastName, email, nickname) values (6, 'Eva', 'Sandleborn', 'eva@htomail.com', 'sandy');
insert into user (id, firstName, lastName, email, nickname) values (7, 'Olga', 'Shoopa', 'olga@gmail.com', 'gaah');
insert into user (id, firstName, lastName, email, nickname) values (8, 'Katrina', 'Sherbina', 'ksherbina@gmail.com', 'Kat');
insert into user (id, firstName, lastName, email, nickname) values (3503, 'John', 'Lennon', 'jlennon@gmail.com', 'John');

insert into event (name, description, ownerId) values ('Poker Night', 'Cards with the guys', 8);
insert into event (name, description, ownerId) values ('Billiards with Prince Harry', 'Pool and drinks', 7);
insert into event (name, description, ownerId) values ('Dinner with Barack and Michelle', 'A presidential meal', 6);
insert into event (name, description, ownerId) values ('Sunset Strip Music Festival', 'West Hollywood hosts music event', 5);
insert into event (name, description, ownerId) values ('Ballyshannon Music Festival', 'Irish music festival', 4);
insert into event (name, description, ownerId) values ('2012 Olympic Women''s Soccer Final', 'The final Women''s Soccer game', 3);
insert into event (name, description, ownerId) values ('Weekly Hackathon', 'Show your hackin'' skeelz', 2);
insert into event (name, description, ownerId) values ('Ironman Triathlon Practice', 'Don''t miss out', 1);

insert into venue (id, name, address, latitude, longitude) values (1, 'Pantages Theater', '6233 Hollywood Bl, Los Angeles, CA', 34.1019444, -118.3261111);
insert into venue (id, name, address, latitude, longitude) values (2, 'Hollywood Bowl', '2301 North Highland Ave, Hollywood, CA', 34.1127863, -118.3392439);
insert into venue (id, name, address, latitude, longitude) values (3, 'Tochka', '8915 Sunset Bl, West Hollywood, CA', 34.090608, -118.386178);
insert into venue (id, name, address, latitude, longitude) values (4, 'Griffith Observatory', '2800 East Observatory Rd, Los Angeles, CA 90027', 0, 0);
insert into venue (id, name, address, latitude, longitude) values (5, 'The Roxy', '9009 West Sunset Bl, West Hollywood, CA 90069', 34.123408, -118.302409);
insert into venue (id, name, address, latitude, longitude) values (6, 'The Viper Room', '8852 West Sunset Bl, West Hollywood, CA 90069', 34.090512, -118.384657);
insert into venue (id, name, address, latitude, longitude) values (7, 'House of Blues Sunset Strip', '8430 Sunset Bl, West Hollywood, CA', 34.094950, -118.373779);
insert into venue (id, name, address, latitude, longitude) values (8, 'Carousel Restaurant', '304 N Brand Bl, Glendale, CA 91203', 34.149885, -118.255108);

insert into venue_property (venueId, key, value) values (1, 'seating capacity', '2703');
insert into venue_property (venueId, key, value) values (1, 'since', '1930-06-04');
insert into venue_property (venueId, key, value) values (1, 'cross street', 'Argyle');
insert into venue_property (venueId, key, value) values (3, 'cuisine', 'mediterranean');
insert into venue_property (venueId, key, value) values (3, 'parking', 'street');
insert into venue_property (venueId, key, value) values (5, 'fax', '310-278-2447');
insert into venue_property (venueId, key, value) values (5, 'twitter', '@theroxy');
insert into venue_property (venueId, key, value) values (7, 'url', 'http://www.houseofblues.com/');

insert into occurrence (id, venueId, start, end) values (1, 4, '2012-03-15 20:00:00', '2012-03-16 02:30:00');

insert into category (id, name, parentId) values (1, 'Theater', null);
insert into category (id, name, parentId) values (2, 'Club', null);

insert into event_category (eventId, categoryId) values (1, 1);

insert into attendee (occurrenceId, userId) values (1, 1);

insert into event_comment (id, eventId, authorId, text, timestamp) values (1, 1, 1, 'Boo, sux', '2012-03-17');
