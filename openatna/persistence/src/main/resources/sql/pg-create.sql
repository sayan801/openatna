--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: atna; Type: DATABASE; Schema: -; Owner: atna
--

CREATE DATABASE atna WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C';


ALTER DATABASE atna OWNER TO atna;

\connect atna

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
-- Name: codes; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE codes (
    type character varying(31) NOT NULL,
    id bigint NOT NULL,
    code character varying(255),
    originaltext character varying(255),
    codesystem character varying(255),
    codesystemname character varying(255),
    displayname character varying(255),
    version integer
);


ALTER TABLE public.codes OWNER TO atna;

--
-- Name: detail_types; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE detail_types (
    id bigint NOT NULL,
    type character varying(255),
    version integer
);


ALTER TABLE public.detail_types OWNER TO atna;

--
-- Name: event_types_to_codes; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE event_types_to_codes (
    event_type bigint NOT NULL,
    code bigint NOT NULL
);


ALTER TABLE public.event_types_to_codes OWNER TO atna;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: atna
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO atna;

--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: atna
--

SELECT pg_catalog.setval('hibernate_sequence', 1509, true);


--
-- Name: message_objects; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE message_objects (
    id bigint NOT NULL,
    objectquery character varying(255),
    objectdatalifecycle smallint,
    object_id bigint
);


ALTER TABLE public.message_objects OWNER TO atna;

--
-- Name: message_objects_object_details; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE message_objects_object_details (
    message_objects_id bigint NOT NULL,
    details_id bigint NOT NULL
);


ALTER TABLE public.message_objects_object_details OWNER TO atna;

--
-- Name: message_participants; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE message_participants (
    id bigint NOT NULL,
    userisrequestor boolean,
    participant_id bigint,
    networkaccesspoint_id bigint
);


ALTER TABLE public.message_participants OWNER TO atna;

--
-- Name: message_sources; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE message_sources (
    id bigint NOT NULL,
    source_id bigint
);


ALTER TABLE public.message_sources OWNER TO atna;

--
-- Name: messages; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE messages (
    id bigint NOT NULL,
    eventactioncode character varying(255),
    eventdatetime timestamp without time zone,
    eventoutcome integer,
    eventid_id bigint
);


ALTER TABLE public.messages OWNER TO atna;

--
-- Name: messages_message_objects; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE messages_message_objects (
    messages_id bigint NOT NULL,
    messageobjects_id bigint NOT NULL
);


ALTER TABLE public.messages_message_objects OWNER TO atna;

--
-- Name: messages_message_participants; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE messages_message_participants (
    messages_id bigint NOT NULL,
    messageparticipants_id bigint NOT NULL
);


ALTER TABLE public.messages_message_participants OWNER TO atna;

--
-- Name: messages_message_sources; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE messages_message_sources (
    messages_id bigint NOT NULL,
    messagesources_id bigint NOT NULL
);


ALTER TABLE public.messages_message_sources OWNER TO atna;

--
-- Name: network_access_points; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE network_access_points (
    id bigint NOT NULL,
    identifier character varying(255),
    type smallint,
    version integer
);


ALTER TABLE public.network_access_points OWNER TO atna;

--
-- Name: object_details; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE object_details (
    id bigint NOT NULL,
    value text,
    type character varying(255)
);


ALTER TABLE public.object_details OWNER TO atna;

--
-- Name: objects; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
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


ALTER TABLE public.objects OWNER TO atna;

--
-- Name: objects_detail_types; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE objects_detail_types (
    objects_id bigint NOT NULL,
    objectdetailtypes_id bigint NOT NULL
);


ALTER TABLE public.objects_detail_types OWNER TO atna;

--
-- Name: participants; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE participants (
    id bigint NOT NULL,
    userid character varying(255),
    alternativeuserid character varying(255),
    version integer,
    username character varying(255)
);


ALTER TABLE public.participants OWNER TO atna;

--
-- Name: participants_to_codes; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE participants_to_codes (
    participant bigint NOT NULL,
    code bigint NOT NULL
);


ALTER TABLE public.participants_to_codes OWNER TO atna;

--
-- Name: sources; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE sources (
    id bigint NOT NULL,
    sourceid character varying(255),
    enterprisesiteid character varying(255),
    version integer
);


ALTER TABLE public.sources OWNER TO atna;

--
-- Name: sources_to_codes; Type: TABLE; Schema: public; Owner: atna; Tablespace: 
--

CREATE TABLE sources_to_codes (
    source bigint NOT NULL,
    code bigint NOT NULL
);


ALTER TABLE public.sources_to_codes OWNER TO atna;

--
-- Data for Name: codes; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY codes (type, id, code, originaltext, codesystem, codesystemname, displayname, version) FROM stdin;
ACTIVE_PARTICIPANT	1429	110153	\N	\N	DCM	Source	0
EVENT_ID	1430	110104	\N	\N	DCM	DICOM Instances Transferred	0
EVENT_ID	1431	ITI-30	\N	\N	IHE Transactions	Patient Identity Management	0
EVENT_ID	1432	ITI-41	\N	\N	IHE Transactions	Provide and Register Document Set-b	0
EVENT_ID	1433	ITI-9	\N	\N	IHE Transactions	PIX Query	0
EVENT_ID	1434	ITI-10	\N	\N	IHE Transactions	PIX Update Notification	0
EVENT_ID	1435	110107	\N	\N	DCM	Import	0
EVENT_ID	1436	ITI-8	\N	\N	IHE Transactions	Patient Identity Feed	0
EVENT_ID	1437	110114	\N	\N	DCM	User Authentication	0
EVENT_ID	1438	ITI-21	\N	\N	IHE Transactions	Patient Demographics Query	0
EVENT_ID	1439	110103	\N	\N	DCM	DICOM Instances Accessed	0
EVENT_TYPE	1440	110131	\N	\N	DCM	Software Configuration	0
PARTICIPANT_OBJECT_ID_TYPE	1441	11	\N	\N	RFC-3881	User Identifier	0
EVENT_ID	1442	ITI-15	\N	\N	IHE Transactions	Provide and Register Document Set	0
ACTIVE_PARTICIPANT	1443	110155	\N	\N	DCM	Source Media	0
EVENT_ID	1444	110111	\N	\N	DCM	Proceedure Record	0
AUDIT_SOURCE	1445	7	\N	\N	RFC-3881	Network Component	0
PARTICIPANT_OBJECT_ID_TYPE	1446	6	\N	\N	RFC-3881	Account Number	0
EVENT_TYPE	1447	110132	\N	\N	DCM	Use of Restritcted Function	0
EVENT_ID	1448	ITI-39	\N	\N	IHE Transactions	Cross Gateway Retrieve	0
EVENT_ID	1449	ITI-32	\N	\N	IHE Transactions	Distribute Document Set on Media	0
EVENT_ID	1450	110101	\N	\N	DCM	Audit Log Used	0
AUDIT_SOURCE	1451	4	\N	\N	RFC-3881	Application Server Process	0
AUDIT_SOURCE	1452	1	\N	\N	RFC-3881	End User Display Device	0
PARTICIPANT_OBJECT_ID_TYPE	1453	110180	\N	\N	DCM	Study Instance UID	0
EVENT_TYPE	1454	110123	\N	\N	DCM	Logout	0
EVENT_TYPE	1455	110122	\N	\N	DCM	Login	0
EVENT_ID	1456	110108	\N	\N	DCM	Network Entry	0
EVENT_ID	1457	ITI-16	\N	\N	IHE Transactions	Registry SQL Query	0
ACTIVE_PARTICIPANT	1458	110152	\N	\N	DCM	Destination	0
PARTICIPANT_OBJECT_ID_TYPE	1459	2	\N	\N	RFC-3881	Patient Number	0
EVENT_TYPE	1460	110120	\N	\N	DCM	Application Start	0
EVENT_TYPE	1461	110128	\N	\N	DCM	Network Configuration	0
EVENT_TYPE	1462	110124	\N	\N	DCM	Attach	0
PARTICIPANT_OBJECT_ID_TYPE	1463	12	\N	\N	RFC-3881	URI	0
EVENT_ID	1464	110113	\N	\N	DCM	Security Alert	0
ACTIVE_PARTICIPANT	1465	110151	\N	\N	DCM	Application Launcher	0
EVENT_TYPE	1466	110127	\N	\N	DCM	Emergency Override	0
EVENT_ID	1467	110112	\N	\N	DCM	Query	0
EVENT_TYPE	1468	110126	\N	\N	DCM	Node Authentication	0
PARTICIPANT_OBJECT_ID_TYPE	1469	110182	\N	\N	DCM	NodeID	0
AUDIT_SOURCE	1470	9	\N	\N	RFC-3881	External Source	0
ACTIVE_PARTICIPANT	1471	110150	\N	\N	DCM	Application	0
EVENT_ID	1472	ITI-48	\N	\N	IHE Transactions	Retrieve Value Set	0
AUDIT_SOURCE	1473	8	\N	\N	RFC-3881	Operating Software	0
PARTICIPANT_OBJECT_ID_TYPE	1474	110181	\N	\N	DCM	SOP Class UID	0
EVENT_TYPE	1475	110125	\N	\N	DCM	Detach	0
EVENT_TYPE	1476	110129	\N	\N	DCM	Security Configuration	0
EVENT_TYPE	1477	110136	\N	\N	DCM	Security Roles Changed	0
PARTICIPANT_OBJECT_ID_TYPE	1478	5	\N	\N	RFC-3881	Social Security Number	0
EVENT_ID	1479	urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd	\N	\N	IHE XDS Metadata	SubmissionSet ClassificationNode	0
EVENT_ID	1480	110100	\N	\N	DCM	Application Activity	0
EVENT_ID	1481	110109	\N	\N	DCM	Order Record	0
EVENT_ID	1482	ITI-14	\N	\N	IHE Transactions	Register Document Set	0
EVENT_ID	1483	ITI-38	\N	\N	IHE Transactions	Cross Gateway Query	0
ACTIVE_PARTICIPANT	1484	110154	\N	\N	DCM	Destination Media	0
PARTICIPANT_OBJECT_ID_TYPE	1485	10	\N	\N	RFC-3881	Search Criteria	0
EVENT_TYPE	1486	110133	\N	\N	DCM	Audit Recording Stopped	0
EVENT_TYPE	1487	110130	\N	\N	DCM	Hardware Configuration	0
EVENT_TYPE	1488	110137	\N	\N	DCM	User Security Attributes Changed	0
EVENT_ID	1489	110110	\N	\N	DCM	Patient Record	0
EVENT_ID	1490	ITI-42	\N	\N	IHE Transactions	Register Document Set-b	0
EVENT_ID	1491	110102	\N	\N	DCM	Being Transfering DICOM Instances	0
AUDIT_SOURCE	1492	3	\N	\N	RFC-3881	Web Server Process	0
EVENT_ID	1493	110106	\N	\N	DCM	Export	0
EVENT_TYPE	1494	110135	\N	\N	DCM	Object Security Attributes Changed	0
EVENT_ID	1495	ITI-18	\N	\N	IHE Transactions	Registry Stored Query	0
EVENT_TYPE	1496	110121	\N	\N	DCM	Application Stop	0
EVENT_ID	1497	ITI-22	\N	\N	IHE Transactions	Patient Demographics and Visit Query	0
EVENT_ID	1498	110105	\N	\N	DCM	DICOM Study Deleted	0
EVENT_TYPE	1499	110134	\N	\N	DCM	Audit Recording Started	0
EVENT_ID	1500	ITI-17	\N	\N	IHE Transactions	Retrieve Document	0
EVENT_ID	1501	ITI-43	\N	\N	IHE Transactions	Retrieve Document Set	0
\.


--
-- Data for Name: detail_types; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY detail_types (id, type, version) FROM stdin;
1509	version	0
\.


--
-- Data for Name: event_types_to_codes; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY event_types_to_codes (event_type, code) FROM stdin;
\.


--
-- Data for Name: message_objects; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY message_objects (id, objectquery, objectdatalifecycle, object_id) FROM stdin;
\.


--
-- Data for Name: message_objects_object_details; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY message_objects_object_details (message_objects_id, details_id) FROM stdin;
\.


--
-- Data for Name: message_participants; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY message_participants (id, userisrequestor, participant_id, networkaccesspoint_id) FROM stdin;
\.


--
-- Data for Name: message_sources; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY message_sources (id, source_id) FROM stdin;
\.


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY messages (id, eventactioncode, eventdatetime, eventoutcome, eventid_id) FROM stdin;
\.


--
-- Data for Name: messages_message_objects; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY messages_message_objects (messages_id, messageobjects_id) FROM stdin;
\.


--
-- Data for Name: messages_message_participants; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY messages_message_participants (messages_id, messageparticipants_id) FROM stdin;
\.


--
-- Data for Name: messages_message_sources; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY messages_message_sources (messages_id, messagesources_id) FROM stdin;
\.


--
-- Data for Name: network_access_points; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY network_access_points (id, identifier, type, version) FROM stdin;
1502	192.168.0.2	2	0
1503	192.168.0.1	2	0
1504	voldemort	1	0
\.


--
-- Data for Name: object_details; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY object_details (id, value, type) FROM stdin;
\.


--
-- Data for Name: objects; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY objects (id, objectid, objectname, objecttypecode, objecttypecoderole, objectsensitivity, version, objectidtypecode_id) FROM stdin;
1508	obj1	machine	\N	\N	N	0	1453
\.


--
-- Data for Name: objects_detail_types; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY objects_detail_types (objects_id, objectdetailtypes_id) FROM stdin;
1508	1509
\.


--
-- Data for Name: participants; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY participants (id, userid, alternativeuserid, version, username) FROM stdin;
1507	scmabh	\N	0	andrew
\.


--
-- Data for Name: participants_to_codes; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY participants_to_codes (participant, code) FROM stdin;
1507	1471
\.


--
-- Data for Name: sources; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY sources (id, sourceid, enterprisesiteid, version) FROM stdin;
1505	ls	\N	0
1506	cat	\N	0
\.


--
-- Data for Name: sources_to_codes; Type: TABLE DATA; Schema: public; Owner: atna
--

COPY sources_to_codes (source, code) FROM stdin;
1505	1473
1506	1473
\.


--
-- Name: codes_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY codes
    ADD CONSTRAINT codes_pkey PRIMARY KEY (id);


--
-- Name: detail_types_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY detail_types
    ADD CONSTRAINT detail_types_pkey PRIMARY KEY (id);


--
-- Name: event_types_to_codes_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY event_types_to_codes
    ADD CONSTRAINT event_types_to_codes_pkey PRIMARY KEY (event_type, code);


--
-- Name: message_objects_object_details_details_id_key; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY message_objects_object_details
    ADD CONSTRAINT message_objects_object_details_details_id_key UNIQUE (details_id);


--
-- Name: message_objects_object_details_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY message_objects_object_details
    ADD CONSTRAINT message_objects_object_details_pkey PRIMARY KEY (message_objects_id, details_id);


--
-- Name: message_objects_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY message_objects
    ADD CONSTRAINT message_objects_pkey PRIMARY KEY (id);


--
-- Name: message_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY message_participants
    ADD CONSTRAINT message_participants_pkey PRIMARY KEY (id);


--
-- Name: message_sources_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY message_sources
    ADD CONSTRAINT message_sources_pkey PRIMARY KEY (id);


--
-- Name: messages_message_objects_messageobjects_id_key; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY messages_message_objects
    ADD CONSTRAINT messages_message_objects_messageobjects_id_key UNIQUE (messageobjects_id);


--
-- Name: messages_message_objects_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY messages_message_objects
    ADD CONSTRAINT messages_message_objects_pkey PRIMARY KEY (messages_id, messageobjects_id);


--
-- Name: messages_message_participants_messageparticipants_id_key; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY messages_message_participants
    ADD CONSTRAINT messages_message_participants_messageparticipants_id_key UNIQUE (messageparticipants_id);


--
-- Name: messages_message_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY messages_message_participants
    ADD CONSTRAINT messages_message_participants_pkey PRIMARY KEY (messages_id, messageparticipants_id);


--
-- Name: messages_message_sources_messagesources_id_key; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY messages_message_sources
    ADD CONSTRAINT messages_message_sources_messagesources_id_key UNIQUE (messagesources_id);


--
-- Name: messages_message_sources_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY messages_message_sources
    ADD CONSTRAINT messages_message_sources_pkey PRIMARY KEY (messages_id, messagesources_id);


--
-- Name: messages_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: network_access_points_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY network_access_points
    ADD CONSTRAINT network_access_points_pkey PRIMARY KEY (id);


--
-- Name: object_details_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY object_details
    ADD CONSTRAINT object_details_pkey PRIMARY KEY (id);


--
-- Name: objects_detail_types_objectdetailtypes_id_key; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY objects_detail_types
    ADD CONSTRAINT objects_detail_types_objectdetailtypes_id_key UNIQUE (objectdetailtypes_id);


--
-- Name: objects_detail_types_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY objects_detail_types
    ADD CONSTRAINT objects_detail_types_pkey PRIMARY KEY (objects_id, objectdetailtypes_id);


--
-- Name: objects_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT objects_pkey PRIMARY KEY (id);


--
-- Name: participants_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY participants
    ADD CONSTRAINT participants_pkey PRIMARY KEY (id);


--
-- Name: participants_to_codes_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY participants_to_codes
    ADD CONSTRAINT participants_to_codes_pkey PRIMARY KEY (participant, code);


--
-- Name: sources_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY sources
    ADD CONSTRAINT sources_pkey PRIMARY KEY (id);


--
-- Name: sources_to_codes_pkey; Type: CONSTRAINT; Schema: public; Owner: atna; Tablespace: 
--

ALTER TABLE ONLY sources_to_codes
    ADD CONSTRAINT sources_to_codes_pkey PRIMARY KEY (source, code);


--
-- Name: fk14fb3e98fb37a07; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY messages_message_objects
    ADD CONSTRAINT fk14fb3e98fb37a07 FOREIGN KEY (messageobjects_id) REFERENCES message_objects(id);


--
-- Name: fk14fb3e9a7583d69; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY messages_message_objects
    ADD CONSTRAINT fk14fb3e9a7583d69 FOREIGN KEY (messages_id) REFERENCES messages(id);


--
-- Name: fk16a4e4d81a08cfce; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY message_participants
    ADD CONSTRAINT fk16a4e4d81a08cfce FOREIGN KEY (participant_id) REFERENCES participants(id);


--
-- Name: fk16a4e4d89da1a7ec; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY message_participants
    ADD CONSTRAINT fk16a4e4d89da1a7ec FOREIGN KEY (networkaccesspoint_id) REFERENCES network_access_points(id);


--
-- Name: fk1f8105eba7583d69; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY messages_message_participants
    ADD CONSTRAINT fk1f8105eba7583d69 FOREIGN KEY (messages_id) REFERENCES messages(id);


--
-- Name: fk1f8105ebcde1e891; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY messages_message_participants
    ADD CONSTRAINT fk1f8105ebcde1e891 FOREIGN KEY (messageparticipants_id) REFERENCES message_participants(id);


--
-- Name: fk3fd64b809238dbcc; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY message_sources
    ADD CONSTRAINT fk3fd64b809238dbcc FOREIGN KEY (source_id) REFERENCES sources(id);


--
-- Name: fk556d74dc3236e4c; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY message_objects
    ADD CONSTRAINT fk556d74dc3236e4c FOREIGN KEY (object_id) REFERENCES objects(id);


--
-- Name: fk7df147213de66d5a; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY participants_to_codes
    ADD CONSTRAINT fk7df147213de66d5a FOREIGN KEY (participant) REFERENCES participants(id);


--
-- Name: fk7df1472167e8d5d9; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY participants_to_codes
    ADD CONSTRAINT fk7df1472167e8d5d9 FOREIGN KEY (code) REFERENCES codes(id);


--
-- Name: fk81f018569b918057; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY objects_detail_types
    ADD CONSTRAINT fk81f018569b918057 FOREIGN KEY (objects_id) REFERENCES objects(id);


--
-- Name: fk81f01856b3274e8e; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY objects_detail_types
    ADD CONSTRAINT fk81f01856b3274e8e FOREIGN KEY (objectdetailtypes_id) REFERENCES detail_types(id);


--
-- Name: fk8930b3053b3671f8; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY message_objects_object_details
    ADD CONSTRAINT fk8930b3053b3671f8 FOREIGN KEY (message_objects_id) REFERENCES message_objects(id);


--
-- Name: fk8930b305e7f7ed9a; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY message_objects_object_details
    ADD CONSTRAINT fk8930b305e7f7ed9a FOREIGN KEY (details_id) REFERENCES object_details(id);


--
-- Name: fk90d5fead1601463a; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY event_types_to_codes
    ADD CONSTRAINT fk90d5fead1601463a FOREIGN KEY (event_type) REFERENCES messages(id);


--
-- Name: fk90d5fead3b73f89a; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY event_types_to_codes
    ADD CONSTRAINT fk90d5fead3b73f89a FOREIGN KEY (code) REFERENCES codes(id);


--
-- Name: fk9d13c514a22cf2b4; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c514a22cf2b4 FOREIGN KEY (objectidtypecode_id) REFERENCES codes(id);


--
-- Name: fkdf4fbe0958ed46cf; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY sources_to_codes
    ADD CONSTRAINT fkdf4fbe0958ed46cf FOREIGN KEY (code) REFERENCES codes(id);


--
-- Name: fkdf4fbe09c204f428; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY sources_to_codes
    ADD CONSTRAINT fkdf4fbe09c204f428 FOREIGN KEY (source) REFERENCES sources(id);


--
-- Name: fke475014c187cd873; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT fke475014c187cd873 FOREIGN KEY (eventid_id) REFERENCES codes(id);


--
-- Name: fkebb88a8da7583d69; Type: FK CONSTRAINT; Schema: public; Owner: atna
--

ALTER TABLE ONLY messages_message_sources
    ADD CONSTRAINT fkebb88a8da7583d69 FOREIGN KEY (messages_id) REFERENCES messages(id);


--
-- Name: fkebb88a8da96520bf; Type: FK CONSTRAINT; Schema: public; Owner: atna
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

