<%--

 * Copyright (c) 2009-2010 University of Cardiff and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cardiff University - intial API and implementation

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" type="text/css" href="css/style.css" title="Default">
    <script type="text/javascript" language="javascript" src="js/script.js"></script>
    <title>Messages</title>
</head>
<body>
<div class="main">
<form:form action="query.html" commandName="queryBean">
    <fieldset>
        <legend>Constraints</legend>
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
            <div class="header">Event ID Code</div>
            <div class="headerLink"><a href="javascript:toggle('${message.id}')" id="${message.id}-link">+</a></div>
        </div>
        <div class="content">
            <div class="headerContent">${message.eventDateTime}</div>
            <div class="headerContent">${message.eventActionCode}</div>
            <div class="headerContent">${message.eventOutcome}</div>
            <div class="headerContent">${message.eventId.code}</div>
        </div>

        <div class="hidden" id="${message.id}">
            <div class="subContent">
                <div class="header evt">Event ID Code</div>
                <div class="header evt"><span class="subHeader">Code System</span></div>
                <div class="header evt"><span class="subHeader">Code System Name</span></div>
                <div class="header evt"><span class="subHeader">Display Name</span></div>
            </div>
            <div class="subContent">
                <div class="headerContent">${message.eventId.code}</div>
                <div class="headerContent">${message.eventId.codeSystem}</div>
                <div class="headerContent">${message.eventId.codeSystemName}</div>
                <div class="headerContent">${message.eventId.displayName}</div>
            </div>
            <c:if test="${fn:length(message.eventTypeCodes) > 0}">
                <c:forEach items="${message.eventTypeCodes}" var="evtType">
                    <div class="subContent">
                        <div class="header evt">Event Type Code</div>
                        <div class="header evt"><span class="subHeader">Code System</span></div>
                        <div class="header evt"><span class="subHeader">Code System Name</span></div>
                        <div class="header evt"><span class="subHeader">Display Name</span></div>
                    </div>
                    <div class="subContent">
                        <div class="headerContent">${evtType.code}</div>
                        <div class="headerContent">${evtType.codeSystem}</div>
                        <div class="headerContent">${evtType.codeSystemName}</div>
                        <div class="headerContent">${evtType.displayName}</div>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${fn:length(message.messageSources) > 0}">
                <c:forEach items="${message.messageSources}" var="msgSource">
                    <div class="subContent">
                        <div class="longHeader srcHeader">Source ID</div>
                        <div class="longHeader srcHeader">Enterprise Site ID</div>
                    </div>
                    <div class="subContent">
                        <div class="longHeaderContent">${msgSource.source.sourceId}</div>
                        <div class="longHeaderContent">${msgSource.source.enterpriseSiteId}</div>
                    </div>
                    <c:forEach items="${msgSource.source.sourceTypeCodes}" var="sourceCode">
                        <div class="subContent">
                            <div class="header src">Source Type Code</div>
                            <div class="header src"><span class="subHeader">Code System</span></div>
                            <div class="header src"><span class="subHeader">Code System Name</span></div>
                            <div class="header src"><span class="subHeader">Display Name</span></div>
                        </div>
                        <div class="subContent">
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
                    <div class="subContent">
                        <div class="longHeader prtHeader">User ID</div>
                        <div class="header prtHeader">Alt User ID</div>
                        <div class="header prtHeader">User Name</div>
                    </div>
                    <div class="subContent">
                        <div class="longHeaderContent">${msgP.participant.userId}</div>
                        <div class="headerContent">${msgP.participant.alternativeUserId}</div>
                        <div class="headerContent">${msgP.participant.userName}</div>
                    </div>
                    <c:forEach items="${msgP.participant.participantTypeCodes}" var="pCode">
                        <div class="subContent">
                            <div class="header prt">Participant Type Code</div>
                            <div class="header prt"><span class="subHeader">Code System</span></div>
                            <div class="header prt"><span class="subHeader">Code System Name</span></div>
                            <div class="header prt"><span class="subHeader">Display Name</span></div>
                        </div>
                        <div class="subContent">
                            <div class="headerContent">${pCode.code}</div>
                            <div class="headerContent">${pCode.codeSystem}</div>
                            <div class="headerContent">${pCode.codeSystemName}</div>
                            <div class="headerContent">${pCode.displayName}</div>
                        </div>
                    </c:forEach>
                    <c:if test="${msgP.networkAccessPoint != null}">
                        <div class="subContent">
                            <div class="longHeader prt">Network Access Point ID</div>
                            <div class="longHeader prt"><span class="subHeader">Type</span></div>

                        </div>
                        <div class="subContent">
                            <div class="longHeaderContent">${msgP.networkAccessPoint.identifier}</div>
                            <div class="longHeaderContent">${msgP.networkAccessPoint.type}</div>
                        </div>
                    </c:if>

                </c:forEach>
            </c:if>

            <c:if test="${fn:length(message.messageObjects) > 0}">
                <c:forEach items="${message.messageObjects}" var="msgObj">
                    <div class="subContent">
                        <div class="longHeader objHeader">Object ID</div>
                        <div class="longHeader objHeader">Object Name</div>
                    </div>
                    <div class="subContent">
                        <div class="longHeaderContent">${msgObj.object.objectId}</div>
                        <div class="longHeaderContent">${msgObj.object.objectName}</div>
                    </div>
                    <c:if test="${msgObj.object.objectIdTypeCode != null}">
                        <div class="subContent">
                            <div class="header obj">Object ID Type Code</div>
                            <div class="header obj"><span class="subHeader">Code System</span></div>
                            <div class="header obj"><span class="subHeader">Code System Name</span></div>
                            <div class="header obj"><span class="subHeader">Display Name</span></div>
                        </div>
                        <div class="subContent">
                            <div class="headerContent">${msgObj.object.objectIdTypeCode.code}</div>
                            <div class="headerContent">${msgObj.object.objectIdTypeCode.codeSystem}</div>
                            <div class="headerContent">${msgObj.object.objectIdTypeCode.codeSystemName}</div>
                            <div class="headerContent">${msgObj.object.objectIdTypeCode.displayName}</div>
                        </div>
                    </c:if>
                    <div class="subContent">
                        <div class="header obj">Object Type Code</div>
                        <div class="header obj">Object Type Code Role</div>
                        <div class="header obj">Object Sensitivity</div>
                        <div class="header obj">&nbsp;</div>
                    </div>
                    <div class="subContent">
                        <div class="headerContent">${msgObj.object.objectTypeCode}</div>
                        <div class="headerContent">${msgObj.object.objectTypeCodeRole}</div>
                        <div class="headerContent">${msgObj.object.objectSensitivity}</div>
                    </div>
                </c:forEach>
            </c:if>


        </div>
    </c:forEach>

</c:if>
</div>
</body>
</html>