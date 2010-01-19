<%--
  Created by IntelliJ IDEA.
  User: scmabh
  Date: Jan 19, 2010
  Time: 10:41:50 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <style type="text/css">
        body, td {
            padding: 10px 10px 10px 10px;
            font-family: Verdana, Helvetica, Arial, sans-serif;
            font-size: 0.8em;
        }

        a {
            text-decoration: none;
            color: #000000;
        }

        a:link {
            color: #000000;
        }

        a:visited {
            color: #000000;
        }

        a:active, a:hover {
            color: #6600ff;
        }

        th {
            background-color: #ccccff;
            font-weight: normal;
        }

        td, th {
            padding: 0.2em;
        }

        fieldset {
            border: 1px solid #6666ff;
            width: 50em;
            margin-top: 5px;
        }

        input {
            margin-right: 4em;
            width: 12em;
        }

        select {
            margin-right: 4em;
            width: 12em;
        }

        legend {
            color: #3333ff;
            padding: 2px 6px;
        }

        .header, .longHeader, .headerContent, .longHeaderContent, .headerLink {
            width: 20em;
            float: left;
            padding: 0.2em;
            overflow: hidden;
        }

        .header, .longHeader, .headerLink {
            background: #9999ff;
        }

        .obj {
            background: #ffcccc;
        }

        .objHeader {
            background: #d55972;
        }

        .src {
            background: #cccc00;
        }

        .srcHeader {
            background: #999900;
        }

        .prt {
            background: #ffccff;
        }

        .prtHeader {
            background: #ac68bd;
        }

        .longHeader, .longHeaderContent {
            width: 40.35em;
        }

        .headerLink {
            width: 2em;
        }

        .content {
            clear: left;
            float: left;
        }

        .hidden {
            visibility: hidden;
            display: none;
            float: left;
            clear: left;
            margin-left: 1em;
        }

        .sep {
            width:80.70em;
            margin: 0em;
            height: 1px;
            background: #9999ff;
        }


    </style>
    <script type="text/javascript" language="javascript">
        function toggle(id) {
            var e = document.getElementById(id);
            if (e.style.display == 'block') {
                e.style.display = 'none';
                e.style.visibility = 'hidden';
                document.getElementById(id + "-link").innerHTML = "+";
            } else {
                e.style.display = 'block';
                e.style.visibility = 'visible';
                document.getElementById(id + "-link").innerHTML = "-";
            }
        }
    </script>
    <title>Messages</title>
</head>
<body>

<form:form action="query.html" commandName="queryBean">
    <fieldset>
        <legend>Query</legend>
        <table>

            <tr>
                <td>Event Id Code :</td>
                <td><form:input path="eventIdCode"/></td>
                <td>Event Type Code :</td>
                <td><form:input path="eventTypeCode"/></td>
            </tr>
            <tr>
                <td>Event Time :</td>
                <td><form:input path="eventTime"/></td>
                <td>Audit Source Id :</td>
                <td><form:input path="sourceId"/></td>
            </tr>
            <tr>
                <td>Event Outcome :</td>
                <td><form:select path="eventOutcome">
                    <form:option value="" label="Select"/>
                    <form:option value="0" label="Success"/>
                    <form:option value="4" label="Minor Failure"/>
                    <form:option value="8" label="Serious Failure"/>
                    <form:option value="12" label="Major Failure"/>
                </form:select></td>
                <td>Participant Object Id :</td>
                <td><form:input path="objectId"/></td>
            </tr>
            <tr>
                <td>Event Action :</td>
                <td><form:select path="eventAction">
                    <form:option value="" label="Select"/>
                    <form:option value="C" label="Create"/>
                    <form:option value="R" label="Read"/>
                    <form:option value="U" label="Update"/>
                    <form:option value="D" label="Delete"/>
                    <form:option value="E" label="Execute"/>
                </form:select></td>
                <td>Active Participant Id :</td>
                <td><form:input path="participantId"/></td>
            </tr>
            <tr>
                <td colspan="4"><input type="submit" value="List"></td>
            </tr>
        </table>
    </fieldset>
</form:form>
<c:if test="${fn:length(messages) > 0}">


    <c:forEach items="${messages}" var="message" varStatus="status">
        <div class="content">
            <div class="header">Event Time</div>
            <div class="header">Event Action</div>
            <div class="header">Event Outcome</div>
            <div class="header">Event ID</div>
            <div class="headerLink"><a href="javascript:toggle('${message.id}')" id="${message.id}-link">+</a></div>
        </div>
        <div class="content">
            <div class="headerContent">${message.eventDateTime}</div>
            <div class="headerContent">${message.eventActionCode}</div>
            <div class="headerContent">${message.eventOutcome}</div>
            <div class="headerContent">${message.eventId.code}</div>
        </div>

        <div class="hidden" id="${message.id}">
            <c:if test="${fn:length(message.messageSources) > 0}">
                <c:forEach items="${message.messageSources}" var="msgSource">
                    <div class="content">
                        <div class="longHeader srcHeader">Source ID</div>
                        <div class="longHeader srcHeader">Enterprise Site ID</div>
                    </div>
                    <div class="content">
                        <div class="longHeaderContent">${msgSource.source.sourceId}</div>
                        <div class="longHeaderContent">${msgSource.source.enterpriseSiteId}</div>
                    </div>
                    <c:forEach items="${msgSource.source.sourceTypeCodes}" var="sourceCode">
                        <div class="content">
                            <div class="header src">Source Type Code</div>
                            <div class="header src">Source Type CodeSystem</div>
                            <div class="header src">Source Type CodeSystem Name</div>
                            <div class="header src">Source Type Code Display</div>
                        </div>
                        <div class="content">
                            <div class="headerContent">${sourceCode.code}</div>
                            <div class="headerContent">${sourceCode.codeSystem}</div>
                            <div class="headerContent">${sourceCode.codeSystemName}</div>
                            <div class="headerContent">${sourceCode.displayName}</div>
                        </div>

                    </c:forEach>
                </c:forEach>
            </c:if>
            <c:if test="${fn:length(message.messageParticipants) > 0}">
                <c:forEach items="${message.messageParticipants}" var="msgP">
                    <div class="content">
                        <div class="longHeader prtHeader">User ID</div>
                        <div class="header prtHeader">Alt User ID</div>
                        <div class="header prtHeader">User Name</div>
                    </div>
                    <div class="content">
                        <div class="longHeaderContent">${msgP.participant.userId}</div>
                        <div class="headerContent">${msgP.participant.alternativeUserId}</div>
                        <div class="headerContent">${msgP.participant.userName}</div>
                    </div>
                    <c:forEach items="${msgP.participant.participantTypeCodes}" var="pCode">
                        <div class="content">
                            <div class="header prt">Participant Type Code</div>
                            <div class="header prt">Participant Type CodeSystem</div>
                            <div class="header prt">Participant Type CodeSystem Name</div>
                            <div class="header prt">Participant Type Code Display</div>
                        </div>
                        <div class="content">
                            <div class="headerContent">${pCode.code}</div>
                            <div class="headerContent">${pCode.codeSystem}</div>
                            <div class="headerContent">${pCode.codeSystemName}</div>
                            <div class="headerContent">${pCode.displayName}</div>
                        </div>
                    </c:forEach>
                    <c:if test="${msgP.networkAccessPoint != null}">
                        <div class="content">
                            <div class="longHeader prt">Network Access Point ID</div>
                            <div class="longHeader prt">Network Access Point Type</div>

                        </div>
                        <div class="content">
                            <div class="longHeaderContent">${msgP.networkAccessPoint.identifier}</div>
                            <div class="longHeaderContent">${msgP.networkAccessPoint.type}</div>
                        </div>
                    </c:if>

                </c:forEach>
            </c:if>

            <c:if test="${fn:length(message.messageObjects) > 0}">
                <c:forEach items="${message.messageObjects}" var="msgObj">
                    <div class="content">
                        <div class="longHeader objHeader">Object ID</div>
                        <div class="longHeader objHeader">Object Name</div>
                    </div>
                    <div class="content">
                        <div class="longHeaderContent">${msgObj.object.objectId}</div>
                        <div class="longHeaderContent">${msgObj.object.objectName}</div>
                    </div>
                    <c:if test="${msgObj.object.objectIdTypeCode != null}">
                        <div class="content">
                            <div class="header obj">Object Id Type Code</div>
                            <div class="header obj">Object Id Type CodeSystem</div>
                            <div class="header obj">Object Id Type CodeSystem Name</div>
                            <div class="header obj">Object Id Type Code Display</div>
                        </div>
                        <div class="content">
                            <div class="headerContent">${msgObj.object.objectIdTypeCode.code}</div>
                            <div class="headerContent">${msgObj.object.objectIdTypeCode.codeSystem}</div>
                            <div class="headerContent">${msgObj.object.objectIdTypeCode.codeSystemName}</div>
                            <div class="headerContent">${msgObj.object.objectIdTypeCode.displayName}</div>
                        </div>
                    </c:if>
                    <div class="content">
                        <div class="header obj">Object Type Code</div>
                        <div class="header obj">Object Type Code Role</div>
                        <div class="header obj">Object Sensitivity</div>
                        <div class="header obj">&nbsp;</div>
                    </div>
                    <div class="content">
                        <div class="headerContent">${msgObj.object.objectTypeCode}</div>
                        <div class="headerContent">${msgObj.object.objectTypeCodeRole}</div>
                        <div class="headerContent">${msgObj.object.objectSensitivity}</div>
                    </div>
                </c:forEach>
            </c:if>


        </div>
    </c:forEach>

</c:if>
</body>
</html>