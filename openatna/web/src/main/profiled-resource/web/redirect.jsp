<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="css/style.css" title="Default">
    <link rel="stylesheet" type="text/css" href="css/smoothness/jquery-ui-1.7.2.custom.css" title="Default">
    <title>eHDSI OpenATNA</title>
</head>
<body>

<div class="main">
    <h1>eHDSI OpenATNA Audit Message and Error Viewer</h1>
    <p>Click <a href="<c:url value="query"/>" here</a> to view messages</p>
    <p>Click <a href="<c:url value="errors"/>">here</a> to view errors</p>
</div>
</body>
</html>