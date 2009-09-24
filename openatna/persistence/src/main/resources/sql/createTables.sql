-- Table: codes

-- DROP TABLE codes;

CREATE TABLE codes
(
  "type" character varying(31) NOT NULL,
  id bigint NOT NULL,
  code character varying(255),
  originaltext character varying(255),
  codesystem character varying(255),
  codesystemname character varying(255),
  displayname character varying(255),
  "version" integer,
  CONSTRAINT codes_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE codes OWNER TO atna;


-- Table: messages

-- DROP TABLE messages;

CREATE TABLE messages
(
  id bigint NOT NULL,
  eventactioncode character varying(255),
  eventdatetime timestamp without time zone,
  eventoutcome integer,
  eventid_id bigint,
  CONSTRAINT messages_pkey PRIMARY KEY (id),
  CONSTRAINT fke475014c187cd873 FOREIGN KEY (eventid_id)
      REFERENCES codes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE messages OWNER TO atna;

-- Table: network_access_points

-- DROP TABLE network_access_points;

CREATE TABLE network_access_points
(
  id bigint NOT NULL,
  identifier character varying(255),
  "type" smallint,
  "version" integer,
  CONSTRAINT network_access_points_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE network_access_points OWNER TO atna;


-- Table: objects

-- DROP TABLE objects;

CREATE TABLE objects
(
  id bigint NOT NULL,
  objectid character varying(255),
  objectname character varying(255),
  objecttypecode smallint,
  objecttypecoderole smallint,
  objectsensitivity character varying(255),
  "version" integer,
  objectidtypecode_id bigint,
  CONSTRAINT objects_pkey PRIMARY KEY (id),
  CONSTRAINT fk9d13c514a22cf2b4 FOREIGN KEY (objectidtypecode_id)
      REFERENCES codes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE objects OWNER TO atna;

-- Table: participants

-- DROP TABLE participants;

CREATE TABLE participants
(
  id bigint NOT NULL,
  userid character varying(255),
  alternativeuserid character varying(255),
  "version" integer,
  username character varying(255),
  CONSTRAINT participants_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE participants OWNER TO atna;

-- Table: participants_to_codes

-- DROP TABLE participants_to_codes;

CREATE TABLE participants_to_codes
(
  participant bigint NOT NULL,
  code bigint NOT NULL,
  CONSTRAINT participants_to_codes_pkey PRIMARY KEY (participant, code),
  CONSTRAINT fk7df147213de66d5a FOREIGN KEY (participant)
      REFERENCES participants (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk7df1472167e8d5d9 FOREIGN KEY (code)
      REFERENCES codes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE participants_to_codes OWNER TO atna;

-- Table: sources

-- DROP TABLE sources;

CREATE TABLE sources
(
  id bigint NOT NULL,
  sourceid character varying(255),
  enterprisesiteid character varying(255),
  "version" integer,
  CONSTRAINT sources_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sources OWNER TO atna;

-- Table: sources_to_codes

-- DROP TABLE sources_to_codes;

CREATE TABLE sources_to_codes
(
  source bigint NOT NULL,
  code bigint NOT NULL,
  CONSTRAINT sources_to_codes_pkey PRIMARY KEY (source, code),
  CONSTRAINT fkdf4fbe0958ed46cf FOREIGN KEY (code)
      REFERENCES codes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkdf4fbe09c204f428 FOREIGN KEY (source)
      REFERENCES sources (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sources_to_codes OWNER TO atna;

-- Table: detail_types

-- DROP TABLE detail_types;

CREATE TABLE detail_types
(
  id bigint NOT NULL,
  "type" character varying(255),
  "version" integer,
  CONSTRAINT detail_types_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE detail_types OWNER TO atna;


-- Table: objects_detail_types

-- DROP TABLE objects_detail_types;

CREATE TABLE objects_detail_types
(
  objects_id bigint NOT NULL,
  objectdetailtypes_id bigint NOT NULL,
  CONSTRAINT objects_detail_types_pkey PRIMARY KEY (objects_id, objectdetailtypes_id),
  CONSTRAINT fk81f018569b918057 FOREIGN KEY (objects_id)
      REFERENCES objects (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk81f01856b3274e8e FOREIGN KEY (objectdetailtypes_id)
      REFERENCES detail_types (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT objects_detail_types_objectdetailtypes_id_key UNIQUE (objectdetailtypes_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE objects_detail_types OWNER TO atna;


-- Table: event_types_to_codes

-- DROP TABLE event_types_to_codes;

CREATE TABLE event_types_to_codes
(
  event_type bigint NOT NULL,
  code bigint NOT NULL,
  CONSTRAINT event_types_to_codes_pkey PRIMARY KEY (event_type, code),
  CONSTRAINT fk90d5fead1601463a FOREIGN KEY (event_type)
      REFERENCES messages (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk90d5fead3b73f89a FOREIGN KEY (code)
      REFERENCES codes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE event_types_to_codes OWNER TO atna;

-- Table: message_objects

-- DROP TABLE message_objects;

CREATE TABLE message_objects
(
  id bigint NOT NULL,
  objectquery character varying(255),
  objectdatalifecycle smallint,
  object_id bigint,
  CONSTRAINT message_objects_pkey PRIMARY KEY (id),
  CONSTRAINT fk556d74dc3236e4c FOREIGN KEY (object_id)
      REFERENCES objects (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE message_objects OWNER TO atna;


-- Table: message_participants

-- DROP TABLE message_participants;

CREATE TABLE message_participants
(
  id bigint NOT NULL,
  userisrequestor boolean,
  participant_id bigint,
  networkaccesspoint_id bigint,
  CONSTRAINT message_participants_pkey PRIMARY KEY (id),
  CONSTRAINT fk16a4e4d81a08cfce FOREIGN KEY (participant_id)
      REFERENCES participants (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk16a4e4d89da1a7ec FOREIGN KEY (networkaccesspoint_id)
      REFERENCES network_access_points (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE message_participants OWNER TO atna;

-- Table: message_sources

-- DROP TABLE message_sources;

CREATE TABLE message_sources
(
  id bigint NOT NULL,
  source_id bigint,
  CONSTRAINT message_sources_pkey PRIMARY KEY (id),
  CONSTRAINT fk3fd64b809238dbcc FOREIGN KEY (source_id)
      REFERENCES sources (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE message_sources OWNER TO atna;


-- Table: messages_message_objects

-- DROP TABLE messages_message_objects;

CREATE TABLE messages_message_objects
(
  messages_id bigint NOT NULL,
  messageobjects_id bigint NOT NULL,
  CONSTRAINT messages_message_objects_pkey PRIMARY KEY (messages_id, messageobjects_id),
  CONSTRAINT fk14fb3e98fb37a07 FOREIGN KEY (messageobjects_id)
      REFERENCES message_objects (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk14fb3e9a7583d69 FOREIGN KEY (messages_id)
      REFERENCES messages (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT messages_message_objects_messageobjects_id_key UNIQUE (messageobjects_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE messages_message_objects OWNER TO atna;

-- Table: messages_message_participants

-- DROP TABLE messages_message_participants;

CREATE TABLE messages_message_participants
(
  messages_id bigint NOT NULL,
  messageparticipants_id bigint NOT NULL,
  CONSTRAINT messages_message_participants_pkey PRIMARY KEY (messages_id, messageparticipants_id),
  CONSTRAINT fk1f8105eba7583d69 FOREIGN KEY (messages_id)
      REFERENCES messages (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk1f8105ebcde1e891 FOREIGN KEY (messageparticipants_id)
      REFERENCES message_participants (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT messages_message_participants_messageparticipants_id_key UNIQUE (messageparticipants_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE messages_message_participants OWNER TO atna;

-- Table: messages_message_sources

-- DROP TABLE messages_message_sources;

CREATE TABLE messages_message_sources
(
  messages_id bigint NOT NULL,
  messagesources_id bigint NOT NULL,
  CONSTRAINT messages_message_sources_pkey PRIMARY KEY (messages_id, messagesources_id),
  CONSTRAINT fkebb88a8da7583d69 FOREIGN KEY (messages_id)
      REFERENCES messages (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkebb88a8da96520bf FOREIGN KEY (messagesources_id)
      REFERENCES message_sources (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT messages_message_sources_messagesources_id_key UNIQUE (messagesources_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE messages_message_sources OWNER TO atna;

-- Table: object_details

-- DROP TABLE object_details;

CREATE TABLE object_details
(
  id bigint NOT NULL,
  "value" text,
  "type" character varying(255),
  CONSTRAINT object_details_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE object_details OWNER TO atna;


-- Table: message_objects_object_details

-- DROP TABLE message_objects_object_details;

CREATE TABLE message_objects_object_details
(
  message_objects_id bigint NOT NULL,
  details_id bigint NOT NULL,
  CONSTRAINT message_objects_object_details_pkey PRIMARY KEY (message_objects_id, details_id),
  CONSTRAINT fk8930b3053b3671f8 FOREIGN KEY (message_objects_id)
      REFERENCES message_objects (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk8930b305e7f7ed9a FOREIGN KEY (details_id)
      REFERENCES object_details (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT message_objects_object_details_details_id_key UNIQUE (details_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE message_objects_object_details OWNER TO atna;


