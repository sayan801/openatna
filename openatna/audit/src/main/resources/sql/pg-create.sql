--
--  Copyright (c) 2009-2010 University of Cardiff and others
--
--  Licensed under the Apache License, Version 2.0 (the "License");
--  you may not use this file except in compliance with the License.
--  You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
--  Unless required by applicable law or agreed to in writing, software
--  distributed under the License is distributed on an "AS IS" BASIS,
--  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
--  implied. See the License for the specific language governing
--  permissions and limitations under the License.
--
--  Contributors:
--    University of Cardiff - initial API and implementation
--    -
--

--
-- PostgreSQL database dump
-- pg_dump -U openatna -s -f pg-create.sql openatna
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: postgres
--

CREATE PROCEDURAL LANGUAGE plpgsql;


ALTER PROCEDURAL LANGUAGE plpgsql OWNER TO postgres;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: codes; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE codes (
    codetype character varying(31) NOT NULL,
    id bigint NOT NULL,
    code character varying(255),
    originaltext character varying(255),
    codesystem character varying(255),
    codesystemname character varying(255),
    type integer,
    displayname character varying(255),
    version integer
);


ALTER TABLE public.codes OWNER TO openatna;

--
-- Name: detail_types; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE detail_types (
    id bigint NOT NULL,
    type character varying(255),
    version integer
);


ALTER TABLE public.detail_types OWNER TO openatna;

--
-- Name: event_types_to_codes; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE event_types_to_codes (
    event_type bigint NOT NULL,
    code bigint NOT NULL
);


ALTER TABLE public.event_types_to_codes OWNER TO openatna;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: openatna
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO openatna;

--
-- Name: message_objects; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE message_objects (
    id bigint NOT NULL,
    objectquery character varying(255),
    objectdatalifecycle smallint,
    object_id bigint
);


ALTER TABLE public.message_objects OWNER TO openatna;

--
-- Name: message_objects_object_details; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE message_objects_object_details (
    message_objects_id bigint NOT NULL,
    details_id bigint NOT NULL
);


ALTER TABLE public.message_objects_object_details OWNER TO openatna;

--
-- Name: message_participants; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE message_participants (
    id bigint NOT NULL,
    userisrequestor boolean,
    participant_id bigint,
    networkaccesspoint_id bigint
);


ALTER TABLE public.message_participants OWNER TO openatna;

--
-- Name: message_sources; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE message_sources (
    id bigint NOT NULL,
    source_id bigint
);


ALTER TABLE public.message_sources OWNER TO openatna;

--
-- Name: messages; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE messages (
    id bigint NOT NULL,
    eventactioncode character varying(255),
    eventdatetime timestamp without time zone,
    eventoutcome integer,
    eventid_id bigint
);


ALTER TABLE public.messages OWNER TO openatna;

--
-- Name: messages_message_objects; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE messages_message_objects (
    messages_id bigint NOT NULL,
    messageobjects_id bigint NOT NULL
);


ALTER TABLE public.messages_message_objects OWNER TO openatna;

--
-- Name: messages_message_participants; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE messages_message_participants (
    messages_id bigint NOT NULL,
    messageparticipants_id bigint NOT NULL
);


ALTER TABLE public.messages_message_participants OWNER TO openatna;

--
-- Name: messages_message_sources; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE messages_message_sources (
    messages_id bigint NOT NULL,
    messagesources_id bigint NOT NULL
);


ALTER TABLE public.messages_message_sources OWNER TO openatna;

--
-- Name: network_access_points; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE network_access_points (
    id bigint NOT NULL,
    identifier character varying(255),
    type smallint,
    version integer
);


ALTER TABLE public.network_access_points OWNER TO openatna;

--
-- Name: object_details; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE object_details (
    id bigint NOT NULL,
    value text,
    type character varying(255)
);


ALTER TABLE public.object_details OWNER TO openatna;

--
-- Name: objects; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE objects (
    id bigint NOT NULL,
    objectid character varying(255),
    objectname character varying(255),
    objecttypecode smallint,
    objecttypecoderole smallint,
    objectsensitivity character varying(255),
    version integer,
    objectidtypecode_id bigint
);


ALTER TABLE public.objects OWNER TO openatna;

--
-- Name: objects_detail_types; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE objects_detail_types (
    objects_id bigint NOT NULL,
    objectdetailtypes_id bigint NOT NULL
);


ALTER TABLE public.objects_detail_types OWNER TO openatna;

--
-- Name: participants; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE participants (
    id bigint NOT NULL,
    userid character varying(255),
    alternativeuserid character varying(255),
    version integer,
    username character varying(255)
);


ALTER TABLE public.participants OWNER TO openatna;

--
-- Name: participants_to_codes; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE participants_to_codes (
    participant bigint NOT NULL,
    code bigint NOT NULL
);


ALTER TABLE public.participants_to_codes OWNER TO openatna;

--
-- Name: sources; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE sources (
    id bigint NOT NULL,
    sourceid character varying(255),
    enterprisesiteid character varying(255),
    version integer
);


ALTER TABLE public.sources OWNER TO openatna;

--
-- Name: sources_to_codes; Type: TABLE; Schema: public; Owner: openatna; Tablespace: 
--

CREATE TABLE sources_to_codes (
    source bigint NOT NULL,
    code bigint NOT NULL
);


ALTER TABLE public.sources_to_codes OWNER TO openatna;

--
-- Name: codes_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY codes
    ADD CONSTRAINT codes_pkey PRIMARY KEY (id);


--
-- Name: detail_types_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY detail_types
    ADD CONSTRAINT detail_types_pkey PRIMARY KEY (id);


--
-- Name: event_types_to_codes_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY event_types_to_codes
    ADD CONSTRAINT event_types_to_codes_pkey PRIMARY KEY (event_type, code);


--
-- Name: message_objects_object_details_details_id_key; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY message_objects_object_details
    ADD CONSTRAINT message_objects_object_details_details_id_key UNIQUE (details_id);


--
-- Name: message_objects_object_details_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY message_objects_object_details
    ADD CONSTRAINT message_objects_object_details_pkey PRIMARY KEY (message_objects_id, details_id);


--
-- Name: message_objects_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY message_objects
    ADD CONSTRAINT message_objects_pkey PRIMARY KEY (id);


--
-- Name: message_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY message_participants
    ADD CONSTRAINT message_participants_pkey PRIMARY KEY (id);


--
-- Name: message_sources_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY message_sources
    ADD CONSTRAINT message_sources_pkey PRIMARY KEY (id);


--
-- Name: messages_message_objects_messageobjects_id_key; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY messages_message_objects
    ADD CONSTRAINT messages_message_objects_messageobjects_id_key UNIQUE (messageobjects_id);


--
-- Name: messages_message_objects_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY messages_message_objects
    ADD CONSTRAINT messages_message_objects_pkey PRIMARY KEY (messages_id, messageobjects_id);


--
-- Name: messages_message_participants_messageparticipants_id_key; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY messages_message_participants
    ADD CONSTRAINT messages_message_participants_messageparticipants_id_key UNIQUE (messageparticipants_id);


--
-- Name: messages_message_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY messages_message_participants
    ADD CONSTRAINT messages_message_participants_pkey PRIMARY KEY (messages_id, messageparticipants_id);


--
-- Name: messages_message_sources_messagesources_id_key; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY messages_message_sources
    ADD CONSTRAINT messages_message_sources_messagesources_id_key UNIQUE (messagesources_id);


--
-- Name: messages_message_sources_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY messages_message_sources
    ADD CONSTRAINT messages_message_sources_pkey PRIMARY KEY (messages_id, messagesources_id);


--
-- Name: messages_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: network_access_points_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY network_access_points
    ADD CONSTRAINT network_access_points_pkey PRIMARY KEY (id);


--
-- Name: object_details_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY object_details
    ADD CONSTRAINT object_details_pkey PRIMARY KEY (id);


--
-- Name: objects_detail_types_objectdetailtypes_id_key; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY objects_detail_types
    ADD CONSTRAINT objects_detail_types_objectdetailtypes_id_key UNIQUE (objectdetailtypes_id);


--
-- Name: objects_detail_types_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY objects_detail_types
    ADD CONSTRAINT objects_detail_types_pkey PRIMARY KEY (objects_id, objectdetailtypes_id);


--
-- Name: objects_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT objects_pkey PRIMARY KEY (id);


--
-- Name: participants_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY participants
    ADD CONSTRAINT participants_pkey PRIMARY KEY (id);


--
-- Name: participants_to_codes_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY participants_to_codes
    ADD CONSTRAINT participants_to_codes_pkey PRIMARY KEY (participant, code);


--
-- Name: sources_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY sources
    ADD CONSTRAINT sources_pkey PRIMARY KEY (id);


--
-- Name: sources_to_codes_pkey; Type: CONSTRAINT; Schema: public; Owner: openatna; Tablespace: 
--

ALTER TABLE ONLY sources_to_codes
    ADD CONSTRAINT sources_to_codes_pkey PRIMARY KEY (source, code);


--
-- Name: fk14fb3e98fb37a07; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY messages_message_objects
    ADD CONSTRAINT fk14fb3e98fb37a07 FOREIGN KEY (messageobjects_id) REFERENCES message_objects(id);


--
-- Name: fk14fb3e9a7583d69; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY messages_message_objects
    ADD CONSTRAINT fk14fb3e9a7583d69 FOREIGN KEY (messages_id) REFERENCES messages(id);


--
-- Name: fk16a4e4d81a08cfce; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY message_participants
    ADD CONSTRAINT fk16a4e4d81a08cfce FOREIGN KEY (participant_id) REFERENCES participants(id);


--
-- Name: fk16a4e4d89da1a7ec; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY message_participants
    ADD CONSTRAINT fk16a4e4d89da1a7ec FOREIGN KEY (networkaccesspoint_id) REFERENCES network_access_points(id);


--
-- Name: fk1f8105eba7583d69; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY messages_message_participants
    ADD CONSTRAINT fk1f8105eba7583d69 FOREIGN KEY (messages_id) REFERENCES messages(id);


--
-- Name: fk1f8105ebcde1e891; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY messages_message_participants
    ADD CONSTRAINT fk1f8105ebcde1e891 FOREIGN KEY (messageparticipants_id) REFERENCES message_participants(id);


--
-- Name: fk3fd64b809238dbcc; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY message_sources
    ADD CONSTRAINT fk3fd64b809238dbcc FOREIGN KEY (source_id) REFERENCES sources(id);


--
-- Name: fk556d74dc3236e4c; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY message_objects
    ADD CONSTRAINT fk556d74dc3236e4c FOREIGN KEY (object_id) REFERENCES objects(id);


--
-- Name: fk7df147213de66d5a; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY participants_to_codes
    ADD CONSTRAINT fk7df147213de66d5a FOREIGN KEY (participant) REFERENCES participants(id);


--
-- Name: fk7df1472167e8d5d9; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY participants_to_codes
    ADD CONSTRAINT fk7df1472167e8d5d9 FOREIGN KEY (code) REFERENCES codes(id);


--
-- Name: fk81f018569b918057; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY objects_detail_types
    ADD CONSTRAINT fk81f018569b918057 FOREIGN KEY (objects_id) REFERENCES objects(id);


--
-- Name: fk81f01856b3274e8e; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY objects_detail_types
    ADD CONSTRAINT fk81f01856b3274e8e FOREIGN KEY (objectdetailtypes_id) REFERENCES detail_types(id);


--
-- Name: fk8930b3053b3671f8; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY message_objects_object_details
    ADD CONSTRAINT fk8930b3053b3671f8 FOREIGN KEY (message_objects_id) REFERENCES message_objects(id);


--
-- Name: fk8930b305e7f7ed9a; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY message_objects_object_details
    ADD CONSTRAINT fk8930b305e7f7ed9a FOREIGN KEY (details_id) REFERENCES object_details(id);


--
-- Name: fk90d5fead1601463a; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY event_types_to_codes
    ADD CONSTRAINT fk90d5fead1601463a FOREIGN KEY (event_type) REFERENCES messages(id);


--
-- Name: fk90d5fead3b73f89a; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY event_types_to_codes
    ADD CONSTRAINT fk90d5fead3b73f89a FOREIGN KEY (code) REFERENCES codes(id);


--
-- Name: fk9d13c514a22cf2b4; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c514a22cf2b4 FOREIGN KEY (objectidtypecode_id) REFERENCES codes(id);


--
-- Name: fkdf4fbe0958ed46cf; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY sources_to_codes
    ADD CONSTRAINT fkdf4fbe0958ed46cf FOREIGN KEY (code) REFERENCES codes(id);


--
-- Name: fkdf4fbe09c204f428; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY sources_to_codes
    ADD CONSTRAINT fkdf4fbe09c204f428 FOREIGN KEY (source) REFERENCES sources(id);


--
-- Name: fke475014c187cd873; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT fke475014c187cd873 FOREIGN KEY (eventid_id) REFERENCES codes(id);


--
-- Name: fkebb88a8da7583d69; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY messages_message_sources
    ADD CONSTRAINT fkebb88a8da7583d69 FOREIGN KEY (messages_id) REFERENCES messages(id);


--
-- Name: fkebb88a8da96520bf; Type: FK CONSTRAINT; Schema: public; Owner: openatna
--

ALTER TABLE ONLY messages_message_sources
    ADD CONSTRAINT fkebb88a8da96520bf FOREIGN KEY (messagesources_id) REFERENCES message_sources(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

