<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search</title>
<script>
	function validateSearch() {
		var search = document.getElementById("searchbar");
		var pattern = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/i;
		
		if (pattern.test(search.value)) {
			alert("Illegal search input. Please check your input.");
			return false;
		}
		return true;
	}
</script>
</head>
<body>
	<h3>Main search page</h3>
	<p><a href="mydetails">My personal details and search history</a></p>
	<form action="dosearch" method="get" onsubmit="return validateSearch()">
		<input type="hidden" name="user" value="${user.username}" /> 
		<p>Dictionary search (enter word, e.g. Car): 
			<input id="searchbar" type="text" name="searchkey" />
			<input type="submit" value="Go!"/></p>
	</form>
	<p><b>Last 5 searches done by anyone (Only visible to super Admins)</b></p>
	<p>
	<c:forEach varStatus="counter" var="searchItem" items="${top5history}">
		<b>${counter.count}:</b> ${searchItem.datetime} 
		<a href="dosearch?user=${user.username}&searchkey=${searchItem.searchkey}">
		<c:out value="${searchItem.searchkey}" /></a><br>
	</c:forEach><br>
	<p><b>You are logged in as <c:out value="${user.username}" />. <a href="logout">Log out</a></b></p>
</body>
</html>