<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="css/style.css"	title="Default">
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
<title>Error</title>
</head>
<body>
	<div class="main">
		<h1>OpenATNA Viewer Error</h1>
		<div class="nav">
			<p><a href="query">Message Viewer</a></p>
			<p><a href="errors">Errors Viewer</a></p>
		</div>
		<form:form action="error" commandName="errorBean">
			Error occurred while attempting to write log. Error message was: ${errorBean.message}
		</form:form>
	</div>
</body>
</html>
