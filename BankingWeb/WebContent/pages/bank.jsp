<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="domain.model.Bank"%>
<%@page import="domain.model.City"%>
<%@page import="java.lang.String"%>
<%@page import="java.util.ArrayList"%>


<jsp:useBean id="cities" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="currentbank" scope="session" class="domain.model.Bank" />
<jsp:useBean id="banks" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="error" scope="request" class="java.lang.String" />



<!-- jsp:setProperty doesn't work with mvc pattern, setting bean properties is done by a servlet
jsp:setProperty name="currentbank" property="*" 
 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">

<title>Bank Application - scriptlet</title>
</head>
<body>
<a href="index.jsp">retour</a>
<h1>Application bancaire</h1>
<div><%=error%>
<form action="bank.do">
<input name="page" type="hidden" value="<%=request.getParameter("page") %>" />
<input name="formbeanclass" type="hidden"
	value="<%=currentbank.getClass().getName()%>" /> 
<input name="id" type="hidden"
	value="<%=currentbank.getId()%>" /> id:<%=currentbank.getId()%><br />

nom:<input name="name" type="text"
	value="<%=currentbank.getName()%>" /><br />
adresse:<input name="address" type="text"
	value="<%=currentbank.getAddress()%>" /><br />
téléphone:<input name="phone" type="text"
	value="<%=currentbank.getPhone()%>" /><br />
code postal:<input name="zipCode" type="text"
	value="<%=currentbank.getZipCode()%>" /><br />
commune: <select name="commune">
<option value=" ">no city</option>
<%for (int c=0;c<cities.size();c++){%>      
	<option <% 
	if (cities.get(c).equals(currentbank.getCity())){
		%> selected <%	
	}
	%>value="<%=((City)(cities.get(c))).getId() %>"><%=((City)(cities.get(c))).getName() %></option>
	<%}%>
</select>	
<br />	
	
<input name="cmd" type="submit" value="modifier" />
<input name="cmd" type="submit" value="ajouter" /> 
<input name="cmd" type="submit" value="suivant" /> 
<input name="cmd" type="submit" value="precedent" />
<input name="cmd" type="submit" value="dupliquer" /> 
<input name="cmd" type="submit" value="foobar" /> 
<input name="cmd" type="submit" value="annuler" /> 
<input name="cmd" type="submit" value="clients" /> <br />
</form>
</div>
<hr />
<div>liste des banques
<table border="1">
	<tr>
		<th>id</th>
		<th>&nbsp;</th>
		<th>name</th>
		<th>address</th>
		<th>phone</th>
		<th>zipcode</th>
		<th>commune</th>
	</tr>
	<%
		for (int i = 0; i < banks.size(); i++) {
			Bank b = (Bank) banks.get(i);
	%>
	<tr>
		<%
			if (b.getId() == currentbank.getId()) {
		%>
		<td bgcolor="red">
		<%
			} else {
		%>
		<td>
		<%
			}
		%> <a href="bank.do?cmd=editer&id=<%=b.getId()%>&page=<%=request.getParameter("page")%>&formbeanclass=<%=currentbank.getClass().getName()%>"><%=b.getId()%></a></td>
		<td><a href="bank.do?cmd=supprimer&id=<%=b.getId()%>&page=<%=request.getParameter("page")%>&formbeanclass=<%=currentbank.getClass().getName()%>">supprimer</a></td>
		<td><%=b.getName()%></td>
		<td><%=b.getAddress()%></td>
		<td><%=b.getPhone()%></td>
		<td><%=b.getZipCode()%></td>
		<td><%=b.getCity()!=null?b.getCity().getName():""%></td>
	</tr>
	<%
		}
	%>
</table>
</div>
</body>
</html>