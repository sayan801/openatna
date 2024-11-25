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
<h1>OpenATNA Error Viewer</h1>

<div class="nav"><a href="query">Message Viewer</a></div>
    <form:form method="post" action="errors" modelAttribute="queryBean">
        <fieldset>
            <legend>Constraints</legend>
            <table>
                <tr>
                    <td>Source IP (required):</td>
                    <td><form:input path="sourceAddress"/></td>
                </tr>
                <tr>
                    <td>Start Date :</td>
                    <td><form:input path="startDate" id="enddatepicker"/></td>

                </tr>
                <tr>

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
                    <td>End Date :</td>
                    <td><form:input path="endDate" id="startdatepicker"/></td>
                </tr>
                <tr>
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
                    <td><input type="submit" value="List"></td>
                    <td></td>
                </tr>
            </table>
        </fieldset>
    </form:form>

<c:if test="${fn:length(errors) > 0}">
    <c:forEach items="${errors}" var="error" varStatus="status">
        <table cellpadding="3" cellspacing="0">
            <tr>
                <td class="header">Time</td>
                <td class="header">Source IP</td>
                <td class="header">&nbsp;</td>
                <td class="header">&nbsp;</td>
                <td class="headerLink"><a href="#" onclick="toggle('${error.id}')" id="${error.id}-link">+</a></td>
            </tr>
            <tr>
                <td class="headerContent">${error.time}</td>
                <td class="headerContent">${error.ip}</td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </table>
        <div class="hidden" id="${error.id}">
            <table cellpadding="3" cellspacing="0">
                <tr>
                    <td class="header evt">Error Message</td>
                <tr>
                    <td class="innerTable">
                            ${error.message}
                    </td>
                </tr>
                <tr>
                    <td class="header evt">Stack Trace</td>
                </tr>
                <tr>
                    <td class="innerTable">
                        <code>
                                ${error.stackTrace}
                        </code>
                    </td>
                </tr>
                <tr>
                    <td class="header evt">Payload</td>
                </tr>
                <tr>
                    <td class="innerTable">
                        <PRE>${error.content}</PRE>
                    </td>
                </tr>
            </table>
        </div>
    </c:forEach>

</c:if>
</div>
</body>
</html>