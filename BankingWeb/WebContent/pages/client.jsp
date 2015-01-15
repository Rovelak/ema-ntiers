<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="domain.model.Bank"%>
<%@page import="domain.model.Customer"%>
<%@page import="domain.model.City"%>
<%@page import="java.util.Iterator"%>
<jsp:useBean id="currentbank" scope="session"
	class="domain.model.Bank" />
<jsp:useBean id="currentcustomer" scope="session"
	class="domain.model.Customer" />
<jsp:useBean id="customers" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="cities" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="error" scope="request" class="java.lang.String" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
<title>Bank Application - Customers</title>
</head>
<body>
<a href="index.jsp">accueil</a><br/>
<a href="bank.do">banque</a><br>
<h1><%=currentbank.getName() %> - Clients - </h1>
<div><%=error%>
<form action="customer.do">
<input name="page" type="hidden" value="<%=request.getParameter("page") %>" />
<input name="id" type="hidden" value="<%=currentcustomer.getId() %>" /> 
<input name="formbeanclass" type="hidden"
	value="<%=currentcustomer.getClass().getName()%>" /> 
<input name="bank" type="hidden" value="<%=currentbank.getId()%>" /> 
id:<%=currentcustomer.getId() %><br />
name:<input name="name" type="text" value="<%=currentcustomer.getName() %>" /><br />
address:<input name="address" type="text" value="<%=currentcustomer.getAddress() %>" /><br />


commune: <select name="commune">
<option value=" ">no city</option>
<%for (int c=0;c<cities.size();c++){%>      
	<option <% 
	if (cities.get(c).equals(currentcustomer.getCity())){
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
<input name="cmd" type="submit" value="comptes" /><br />
</form>
</div>
<hr />
<div>liste des clients
<table border="1">
	<tr>
		<td>id</td>
		<td>&nbsp;</td>
		<td>name</td>
		<td>address</td>
		<td>city</td>
	</tr>
	<%
		for (int i = 0; i < customers.size(); i++) {
			Customer c = (Customer) customers.get(i);
	%>
	<tr>
		<%
			if (c.getId() == currentcustomer.getId()) {
		%>
		<td bgcolor="red">
		<%
			} else {
		%>
		
		<td>
		<%
			}
		%> <a href="customer.do?cmd=editer&id=<%=c.getId()%>&formbeanclass=<%=currentcustomer.getClass().getName()%>&bank=<%=currentcustomer.getBank().getId()%>"><%=c.getId()%></a></td>
		<td><a href="customer.do?cmd=supprimer&id=<%=c.getId()%>&formbeanclass=<%=currentcustomer.getClass().getName()%>&bank=<%=currentcustomer.getBank().getId()%>">supprimer</a></td>
		<td><%=c.getName()%></td>
		<td><%=c.getAddress()%></td>
		<td><%=c.getCity()!=null?c.getCity().getName():""%></td>

	</tr>
	<%
		}
	 %>
</table>
</div>



</body>
</html>