<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.net.URLDecoder" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Role Update for Existing User</title>
</head>
<body>
	<h3>Update role for existing user</h3>
	<p><font color="red">${message}</font></p>
	<form action="updaterole" method="post">
	<input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
	<table>
	<tr><td><p>Username</td> 
		<td>
		<p>
		<select name="username">
			<c:forEach var="username" items="${usernames}">
				<option value="${username}"><c:out value="${URLDecoder.decode(user.username, 'UTF-8')}" /></option>
			</c:forEach>
		</select>
		<p>
	</tr>
	<tr><td><p>New Role </td></tr>
	<tr><td><input type="radio" name="role" value="USER">USER<br></td></tr>
	<tr><td><input type="radio" name="role" value="ADMIN" checked="checked">ADMIN<br></td></tr>
	
	<tr>	
		<td><p><input type="submit" value="Update Role"/></p></td>
	</tr>
	</table>
	</form>
	
	<br>
	<p><a href="searchpage">Back to Main search page</a></p>
	<p><b>You are logged in as <c:out value="${URLDecoder.decode(user.username, 'UTF-8')}" />. <a href="logout">Log out</a></b></p>

</body>
</html>