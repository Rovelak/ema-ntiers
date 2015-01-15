<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="currentbank" scope="session"
	class="domain.model.Bank" />
<jsp:useBean id="banks" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="error" scope="request" class="java.lang.String" />
<!-- jsp:setProperty doesn't work with mvc pattern, setting bean properties is done by a servlet
jsp:setProperty name="currentbank" property="*" 
 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Expires" content="-1" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />

<link rel="stylesheet" href="./css/print.css" type="text/css"
	media="print" />
<link rel="stylesheet" href="./css/screen.css" type="text/css" />
<title>Bank Application - el</title>
</head>
<body>
<div id="bodyColumn">
<a href="index.jsp">retour</a>
<h1>Application bancaire</h1>
<div><%=error%></div>
<span class="pageform">
<form action="bank.do">
<input name="page" type="hidden" value="${param.page}" />
<input name="formbeanclass" type="hidden"
	value="<%=currentbank.getClass().getName()%>" /> 
<input name="id" type="hidden"
	value="${currentbank.id}" />
<table border="0" cellspacing="1">
	<tr>
		<td class="label" width="100">id:</td>
		<td class="data" width="100">${currentbank.id}</td>
		<td class="label" width="100">name:</td>
		<td class="data" width="100">
		<input name="name" type="text" value="${currentbank.name}" /></td>
		<td class="empty" width="350">&nbsp;</td>
	</tr>
	<tr>
		<td class="label" width="100">address:</td>
		<td class="data" width="100">
		<input name="address" type="text" value="${currentbank.address}" /></td>
		<td class="label" width="100">phone</td>
		<td class="data" width="100">
		<input name="phone" type="text" value="${currentbank.phone}" /></td>
		<td class="empty" width="350">&nbsp;</td>
	</tr>
	<tr>
		<td class="label" width="100">zipCode:</td>
		<td class="data" width="100">
		<input name="zipCode" type="text" value="${currentbank.zipCode}" /></td>
		<td class="label" width="100">&nbsp;</td>
		<td class="data" width="100">&nbsp;</td>
		<td class="empty" width="350">&nbsp;</td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="100"><input name="cmd" type="submit" value="modifier" /></td>
		<td width="100"><input name="cmd" type="submit" value="ajouter" /></td>
		<td width="100"><input name="cmd" type="submit" value="suivant" /></td>
		<td width="100"><input name="cmd" type="submit" value="precedent" /></td>
		<td width="100"><input name="cmd" type="submit" value="dupliquer" /></td>
		<td width="100"><input name="cmd" type="submit" value="foobar" /></td>
		<td width="100"><input name="cmd" type="submit" value="annuler" /></td>
		<td width="100"><input name="cmd" type="submit" value="clients" /></td>
		<td width="50">&nbsp;</td>
	</tr>
</table>
</form>
</span>
<h3>Banques</h3>
<table>
	<thead>
		<tr>
			<th>id</th>
			<th>&nbsp;</th>
			<th>name</th>
			<th>address</th>
			<th>phone</th>
			<th>zipcode</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${banks}" var="bank">
			<tr class="even">
				<c:if test="${bank.id==currentbank.id}">
					<td bgcolor="red">
					<a href="bank.do?cmd=editer&id=${bank.id}&page=${param.page}&formbeanclass=${currentbank.class.name}">${bank.id}</a></td>
				</c:if>
				<c:if test="${bank.id!=currentbank.id}">
					<td>
					<a href="bank.do?cmd=editer&id=${bank.id}&page=${param.page}&formbeanclass=${currentbank.class.name}">${bank.id}</a>
					</td>
				</c:if>
				<td><a href="bank.do?cmd=supprimer&id=${bank.id}&page=${param.page}&formbeanclass=${currentbank.class.name}">supprimer</a></td>
				<td>${bank.name}</td>
				<td>${bank.address}</td>
				<td>${bank.phone}</td>
				<td>${bank.zipCode}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
</body>
</html>