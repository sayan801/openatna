<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" type="text/css" href="css/style.css" title="Default">
    <link rel="stylesheet" type="text/css" href="css/smoothness/jquery-ui-1.7.2.custom.css" title="Default">

    <script type="text/javascript" language="javascript" src="js/jquery-1.3.2.min.js"></script>
    <script type="text/javascript" language="javascript" src="js/jquery-ui-1.7.2.custom.min.js"></script>
    <script type="text/javascript" language="javascript">
        //<![CDATA[
        function toggle(id) {
            var viz = $('#' + id).is(':visible');
            $('#' + id).toggle(0);
            $('html, body').animate({
                scrollTop: ($('#' + id).offset().top) - 40}, 200);
            if (!viz) {
                $('#' + id + "-link").text("-");
            } else {
                $('#' + id + "-link").text("+");
            }
            return false;
        }
        $(function() {
            $("#startdatepicker").datepicker({maxDate: '+0M +0D', dateFormat: "yy-mm-dd"});
            $("#enddatepicker").datepicker({maxDate: '+0M +0D', dateFormat: "yy-mm-dd"});
        });
        //]]>

    </script>
    <title>Messages</title>
</head>
<body>
<div class="main">
<h1>OpenATNA Audit Message Viewer</h1>

<div class="nav"><a href="errors">Error Viewer</a></div>

<form:form action="query" commandName="queryBean">
<fieldset>
<legend>Constraints</legend>
<table>
<tr>
    <td colspan="6"><span class="emphed">At least one constraint must be specified.</span></td>
</tr>
<tr>
    <td colspan="6"><span class="emphed">A '*' can be used for Ids and Type Codes as a wildcard at the begining and end of values.</span>
    </td>
</tr>
<tr>
    <td>Event Id Code :</td>
    <td><form:input path="eventIdCode"/></td>
    <td>Event Type Code :</td>
    <td><form:input path="eventTypeCode"/></td>
    <td>Start Date :</td>
    <td><form:input path="startDate" id="enddatepicker"/></td>
</tr>
<tr>
    <td>Event Time :</td>
    <td><form:input path="eventTime"/></td>
    <td>Audit Source Id :</td>
    <td><form:input path="sourceId"/></td>
    <td>Start Time :</td>
    <td><form:select path="startHour" cssClass="smallInput">
        <form:option value="00" label="00"/>
        <form:option value="01" label="01"/>
        <form:option value="02" label="02"/>
        <form:option value="03" label="03"/>
        <form:option value="04" label="04"/>
        <form:option value="05" label="05"/>
        <form:option value="06" label="06"/>
        <form:option value="07" label="07"/>
        <form:option value="08" label="08"/>
        <form:option value="09" label="09"/>
        <form:option value="10" label="10"/>
        <form:option value="11" label="11"/>
        <form:option value="12" label="12"/>
        <form:option value="13" label="13"/>
        <form:option value="14" label="14"/>
        <form:option value="15" label="15"/>
        <form:option value="16" label="16"/>
        <form:option value="17" label="17"/>
        <form:option value="18" label="18"/>
        <form:option value="19" label="19"/>
        <form:option value="20" label="20"/>
        <form:option value="21" label="21"/>
        <form:option value="22" label="22"/>
        <form:option value="23" label="23"/>
        <form:option value="24" label="24"/>
    </form:select>
        :
        <form:select path="startMin" cssClass="smallInput">
            <form:option value="00" label="00"/>
            <form:option value="01" label="01"/>
            <form:option value="02" label="02"/>
            <form:option value="03" label="03"/>
            <form:option value="04" label="04"/>
            <form:option value="05" label="05"/>
            <form:option value="06" label="06"/>
            <form:option value="07" label="07"/>
            <form:option value="08" label="08"/>
            <form:option value="09" label="09"/>
            <form:option value="10" label="10"/>
            <form:option value="11" label="11"/>
            <form:option value="12" label="12"/>
            <form:option value="13" label="13"/>
            <form:option value="14" label="14"/>
            <form:option value="15" label="15"/>
            <form:option value="16" label="16"/>
            <form:option value="17" label="17"/>
            <form:option value="18" label="18"/>
            <form:option value="19" label="19"/>
            <form:option value="20" label="20"/>
            <form:option value="21" label="21"/>
            <form:option value="22" label="22"/>
            <form:option value="23" label="23"/>
            <form:option value="24" label="24"/>
            <form:option value="25" label="25"/>
            <form:option value="26" label="26"/>
            <form:option value="27" label="27"/>
            <form:option value="28" label="28"/>
            <form:option value="29" label="29"/>
            <form:option value="30" label="30"/>
            <form:option value="31" label="31"/>
            <form:option value="32" label="32"/>
            <form:option value="33" label="33"/>
            <form:option value="34" label="34"/>
            <form:option value="35" label="35"/>
            <form:option value="36" label="36"/>
            <form:option value="37" label="37"/>
            <form:option value="38" label="38"/>
            <form:option value="39" label="39"/>
            <form:option value="40" label="40"/>
            <form:option value="41" label="41"/>
            <form:option value="42" label="42"/>
            <form:option value="43" label="43"/>
            <form:option value="44" label="44"/>
            <form:option value="45" label="45"/>
            <form:option value="46" label="46"/>
            <form:option value="47" label="47"/>
            <form:option value="48" label="48"/>
            <form:option value="49" label="49"/>
            <form:option value="50" label="50"/>
            <form:option value="51" label="51"/>
            <form:option value="52" label="52"/>
            <form:option value="53" label="53"/>
            <form:option value="54" label="54"/>
            <form:option value="55" label="55"/>
            <form:option value="56" label="56"/>
            <form:option value="57" label="57"/>
            <form:option value="58" label="58"/>
            <form:option value="59" label="59"/>
        </form:select>
    </td>

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
    <td>End Date :</td>
    <td><form:input path="endDate" id="startdatepicker"/></td>
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
    <td>End Time :</td>
    <td><form:select path="endHour" cssClass="smallInput">
        <form:option value="00" label="00"/>
        <form:option value="01" label="01"/>
        <form:option value="02" label="02"/>
        <form:option value="03" label="03"/>
        <form:option value="04" label="04"/>
        <form:option value="05" label="05"/>
        <form:option value="06" label="06"/>
        <form:option value="07" label="07"/>
        <form:option value="08" label="08"/>
        <form:option value="09" label="09"/>
        <form:option value="10" label="10"/>
        <form:option value="11" label="11"/>
        <form:option value="12" label="12"/>
        <form:option value="13" label="13"/>
        <form:option value="14" label="14"/>
        <form:option value="15" label="15"/>
        <form:option value="16" label="16"/>
        <form:option value="17" label="17"/>
        <form:option value="18" label="18"/>
        <form:option value="19" label="19"/>
        <form:option value="20" label="20"/>
        <form:option value="21" label="21"/>
        <form:option value="22" label="22"/>
        <form:option value="23" label="23"/>
        <form:option value="24" label="24"/>
    </form:select>
        :
        <form:select path="endMin" cssClass="smallInput">
            <form:option value="00" label="00"/>
            <form:option value="01" label="01"/>
            <form:option value="02" label="02"/>
            <form:option value="03" label="03"/>
            <form:option value="04" label="04"/>
            <form:option value="05" label="05"/>
            <form:option value="06" label="06"/>
            <form:option value="07" label="07"/>
            <form:option value="08" label="08"/>
            <form:option value="09" label="09"/>
            <form:option value="10" label="10"/>
            <form:option value="11" label="11"/>
            <form:option value="12" label="12"/>
            <form:option value="13" label="13"/>
            <form:option value="14" label="14"/>
            <form:option value="15" label="15"/>
            <form:option value="16" label="16"/>
            <form:option value="17" label="17"/>
            <form:option value="18" label="18"/>
            <form:option value="19" label="19"/>
            <form:option value="20" label="20"/>
            <form:option value="21" label="21"/>
            <form:option value="22" label="22"/>
            <form:option value="23" label="23"/>
            <form:option value="24" label="24"/>
            <form:option value="25" label="25"/>
            <form:option value="26" label="26"/>
            <form:option value="27" label="27"/>
            <form:option value="28" label="28"/>
            <form:option value="29" label="29"/>
            <form:option value="30" label="30"/>
            <form:option value="31" label="31"/>
            <form:option value="32" label="32"/>
            <form:option value="33" label="33"/>
            <form:option value="34" label="34"/>
            <form:option value="35" label="35"/>
            <form:option value="36" label="36"/>
            <form:option value="37" label="37"/>
            <form:option value="38" label="38"/>
            <form:option value="39" label="39"/>
            <form:option value="40" label="40"/>
            <form:option value="41" label="41"/>
            <form:option value="42" label="42"/>
            <form:option value="43" label="43"/>
            <form:option value="44" label="44"/>
            <form:option value="45" label="45"/>
            <form:option value="46" label="46"/>
            <form:option value="47" label="47"/>
            <form:option value="48" label="48"/>
            <form:option value="49" label="49"/>
            <form:option value="50" label="50"/>
            <form:option value="51" label="51"/>
            <form:option value="52" label="52"/>
            <form:option value="53" label="53"/>
            <form:option value="54" label="54"/>
            <form:option value="55" label="55"/>
            <form:option value="56" label="56"/>
            <form:option value="57" label="57"/>
            <form:option value="58" label="58"/>
            <form:option value="59" label="59"/>
        </form:select>
    </td>
</tr>
<tr>
    <td>Source Type Code :</td>
    <td><form:input path="sourceTypeCode"/></td>
    <td>Participant Type Code :</td>
    <td><form:input path="participantTypeCode"/></td>
    <td>Object Type Code :</td>
    <td><form:input path="objectTypeCode"/></td>
</tr>
<tr>
    <td>Source IP :</td>
    <td><form:input path="sourceAddress"/></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
</tr>
<tr>
    <td colspan="5"><input type="submit" value="List"></td>
    <td></td>
</tr>
</table>
</fieldset>
</form:form>


<table>
    <tr>
        <td align="left" width="100%">
            <c:if test="${offset > 0}">
                <a href="query?start=${offset - 1}">Previous</a>
            </c:if>
        </td>
        <td align="right">
            <c:if test="${fn:length(messages) > 0 && fn:length(messages) == queryBean.maxResults}">
                <a href="query?start=${offset + 1}" class="rightlink">Next</a>
            </c:if>
        </td>
    </tr>
</table>
<c:set var="currBean" scope="session" value="${queryBean}"/>

<c:if test="${fn:length(messages) > 0}">
    <c:forEach items="${messages}" var="message" varStatus="status">
        <table cellpadding="3" cellspacing="0">
            <tr>
                <td class="header leftSide">Event Time</td>
                <td class="header">Event Action</td>
                <td class="header">Event Outcome</td>
                <td class="header">Event ID</td>
                <td class="headerLink rightSide"><a href="#" onclick="toggle('${message.id}')" id="${message.id}-link">+</a></td>
            </tr>
            <tr>
                <td class="headerContent">${message.eventDateTime}</td>
                <td class="headerContent">${message.eventActionCode}</td>
                <td class="headerContent">${message.eventOutcome}</td>
                <td class="headerContent">${message.eventId.code}</td>
                <td></td>
            </tr>
        </table>

        <div class="hidden" id="${message.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <table cellpadding="3" cellspacing="0">
                            <tr>
                                <td class="header evt leftSide">Event ID Code</td>
                                <td class="header evt"><span class="subHeader">Code System</span></td>
                                <td class="header evt"><span class="subHeader">Code System Name</span></td>
                                <td class="header evt rightSide"><span class="subHeader">Display Name</span></td>
                            </tr>
                            <tr>
                                <td class="headerContent">${message.eventId.code}</td>
                                <td class="headerContent">${message.eventId.codeSystem}</td>
                                <td class="headerContent">${message.eventId.codeSystemName}</td>
                                <td class="headerContent">${message.eventId.displayName}</td>
                            </tr>

                            <c:if test="${fn:length(message.eventTypeCodes) > 0}">
                                <c:forEach items="${message.eventTypeCodes}" var="evtType">

                                    <tr>
                                        <td class="header evt leftSide">Event Type Code</td>
                                        <td class="header evt"><span class="subHeader">Code System</span></td>
                                        <td class="header evt"><span class="subHeader">Code System Name</span></td>
                                        <td class="header evt rightSide"><span class="subHeader">Display Name</span></td>
                                    </tr>
                                    <tr>
                                        <td class="headerContent">${evtType.code}</td>
                                        <td class="headerContent">${evtType.codeSystem}</td>
                                        <td class="headerContent">${evtType.codeSystemName}</td>
                                        <td class="headerContent">${evtType.displayName}</td>
                                    </tr>

                                </c:forEach>
                            </c:if>
                            <c:if test="${fn:length(message.messageSources) > 0}">
                                <c:forEach items="${message.messageSources}" var="msgSource">

                                    <tr>
                                        <td colspan="2" class="srcHeader leftSide">Source ID</td>
                                        <td colspan="2" class="srcHeader rightSide">Enterprise Site ID</td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">${msgSource.source.sourceId}</td>
                                        <td colspan="2">${msgSource.source.enterpriseSiteId}</td>
                                    </tr>

                                    <c:forEach items="${msgSource.source.sourceTypeCodes}" var="sourceCode">
                                        <tr>
                                            <td class="header src leftSide">Source Type Code</td>
                                            <td class="header src"><span class="subHeader">Code System</span></td>
                                            <td class="header src"><span class="subHeader">Code System Name</span></td>
                                            <td class="header src rightSide"><span class="subHeader">Display Name</span></td>
                                        </tr>
                                        <tr>
                                            <td class="headerContent">${sourceCode.code}</td>
                                            <td class="headerContent">${sourceCode.codeSystem}</td>
                                            <td class="headerContent">${sourceCode.codeSystemName}</td>
                                            <td class="headerContent">${sourceCode.displayName}</td>
                                        </tr>
                                    </c:forEach>
                                </c:forEach>
                            </c:if>
                            <c:if test="${fn:length(message.messageParticipants) > 0}">
                                <c:forEach items="${message.messageParticipants}" var="msgP">
                                    <tr>
                                        <td colspan="2" class="prtHeader leftSide">User ID</td>
                                        <td class="header prtHeader">Alt User ID</td>
                                        <td class="header prtHeader rightSide">User Name</td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">${msgP.participant.userId}</td>
                                        <td class="headerContent">${msgP.participant.alternativeUserId}</td>
                                        <td class="headerContent">${msgP.participant.userName}</td>
                                    </tr>
                                    <c:forEach items="${msgP.participant.participantTypeCodes}" var="pCode">
                                        <tr>
                                            <td class="header prt leftSide">Participant Type Code</td>
                                            <td class="header prt"><span class="subHeader">Code System</span></td>
                                            <td class="header prt"><span class="subHeader">Code System Name</span></td>
                                            <td class="header prt rightSide"><span class="subHeader">Display Name</span></td>
                                        </tr>
                                        <tr>
                                            <td class="headerContent">${pCode.code}</td>
                                            <td class="headerContent">${pCode.codeSystem}</td>
                                            <td class="headerContent">${pCode.codeSystemName}</td>
                                            <td class="headerContent">${pCode.displayName}</td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${msgP.networkAccessPoint != null}">
                                        <tr>
                                            <td colspan="2" class="header prt leftSide">Network Access Point ID</td>
                                            <td colspan="2" class="header prt rightSide"><span class="subHeader">Type</span></td>
                                        </tr>
                                        <tr>
                                            <td colspan="2">${msgP.networkAccessPoint.identifier}</td>
                                            <td colspan="2">${msgP.networkAccessPoint.type}</td>
                                        </tr>
                                    </c:if>

                                </c:forEach>
                            </c:if>

                            <c:if test="${fn:length(message.messageObjects) > 0}">
                                <c:forEach items="${message.messageObjects}" var="msgObj">
                                    <tr>
                                        <td colspan="2" class="objHeader leftSide">Object ID</td>
                                        <td colspan="2" class="objHeader rightSide">Object Name</td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">${msgObj.object.objectId}</td>
                                        <td colspan="2">${msgObj.object.objectName}</td>
                                    </tr>
                                    <c:if test="${msgObj.object.objectIdTypeCode != null}">
                                        <tr>
                                            <td class="header obj leftSide">Object ID Type Code</td>
                                            <td class="header obj"><span class="subHeader">Code System</span></td>
                                            <td class="header obj"><span class="subHeader">Code System Name</span></td>
                                            <td class="header obj rightSide"><span class="subHeader">Display Name</span></td>
                                        </tr>
                                        <tr>
                                            <td class="headerContent">${msgObj.object.objectIdTypeCode.code}</td>
                                            <td class="headerContent">${msgObj.object.objectIdTypeCode.codeSystem}</td>
                                            <td class="headerContent">${msgObj.object.objectIdTypeCode.codeSystemName}</td>
                                            <td class="headerContent">${msgObj.object.objectIdTypeCode.displayName}</td>
                                        </tr>
                                    </c:if>
                                    <tr>
                                        <td class="header obj leftSide">Object Type Code</td>
                                        <td class="header obj">Object Type Code Role</td>
                                        <td class="header obj">Object Sensitivity</td>
                                        <td class="header obj rightSide">&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td class="headerContent">${msgObj.object.objectTypeCode}</td>
                                        <td class="headerContent">${msgObj.object.objectTypeCodeRole}</td>
                                        <td class="headerContent">${msgObj.object.objectSensitivity}</td>
                                        <td>&nbsp;</td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </table>
			            <table cellpadding="3" cellspacing="0" width="45%">
			                <tr>
			                    <td class="header evt">Message Content</td>
			                </tr>
			                <tr>
			                    <td class="innerTable">
			                        <PRE>${message.messageContent}</PRE>
			                    </td>
			                </tr>
			            </table>
                    </td>
                </tr>
            </table>
        </div>
    </c:forEach>
</c:if>
</div>
</body>
</html>