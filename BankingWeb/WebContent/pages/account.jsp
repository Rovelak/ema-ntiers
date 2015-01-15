<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="domain.model.Bank"%>
<%@page import="domain.model.Customer"%>
<%@page import="domain.model.Account"%>
<%@page import="domain.model.City"%>
<%@page import="java.util.Iterator"%>
<jsp:useBean id="currentbank" scope="session"
	class="domain.model.Bank" />
<jsp:useBean id="currentcustomer" scope="session"
	class="domain.model.Customer" />
<jsp:useBean id="currentaccount" scope="session"
	class="domain.model.Account" />
<jsp:useBean id="accounts" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="customers" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="cities" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="error" scope="request" class="java.lang.String" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bank Application - Accounts</title>
</head>
<body>
<a href="index.jsp">accueil</a><br/>
<a href="customer.do">Client</a><br>
<h1><%=currentcustomer.getName() %> - Comptes - </h1>
<div><%=error%>
<form action="account.do">
<input name="page" type="hidden" value="<%=request.getParameter("page") %>" />
<input name="id" type="hidden" value="<%=currentaccount.getId() %>" /> 
<input name="formbeanclass" type="hidden"
	value="<%=currentaccount.getClass().getName()%>" /> 
<input name="customer" type="hidden" value="<%=currentcustomer.getId() %>" /> 
id:<%=currentaccount.getId() %><br />
numéro de compte:<input name="accountNumber" type="text" value="<%=currentaccount.getAccountNumber() %>" /><br />
solde:<input name="balance" type="text" value="<%=currentaccount.getBalance() %>" /><br />
taux d'intérêt:<input name="interestRate" type="text" value="<%=currentaccount.getInterestRate() %>" /><br />   
frais de découvert:<input name="overdraftPenalty" type="text" value="<%=currentaccount.getOverdraftPenalty() %>" /><br /> 
autorisation de découvert:<input name="overdraftLimit" type="text" value="<%=currentaccount.getOverdraftLimit() %>" /><br />

    
<input name="cmd" type="submit" value="modifier" /> 
<input name="cmd" type="submit" value="ajouter" /> 
<input name="cmd" type="submit" value="suivant" /> 
<input name="cmd" type="submit" value="precedent" />
<input name="cmd" type="submit" value="dupliquer" /> 
<input name="cmd" type="submit" value="foobar" /> 
<input name="cmd" type="submit" value="annuler" />
<input name="cmd" type="submit" value="transferts" /> <br />
</form>
</div>
<hr />
<div>liste des comptes
<table border="1">
	<tr>
		<td>id</td>
		<td>&nbsp;</td>
		<td>accountNumber</td>
		<td>balance</td>
		<td>interestRate</td>
		<td>overdraftPenalty</td>
		<td>overdraftLimit</td>
	</tr>
	<% 
		for (int i = 0; i < accounts.size(); i++) {
			Account c = (Account) accounts.get(i);
	%>
	<tr>
		<%
			if (c.getId() == currentaccount.getId()) {
		%>
		<td bgcolor="red">
		<%
			} else {
		%>
		
		<td>
		<%
			}
		%> <a href="account.do?cmd=editer&id=<%=c.getId()%>&formbeanclass=<%=currentaccount.getClass().getName()%>&customer=<%=currentaccount.getOwner().getId()%>"><%=c.getId()%></a></td>
		<td><a href="account.do?cmd=supprimer&id=<%=c.getId()%>&formbeanclass=<%=currentaccount.getClass().getName()%>&customer=<%=currentaccount.getOwner().getId()%>">supprimer</a></td>
        <td><%=c.getAccountNumber()%></td>
		<td><%=c.getBalance()%></td>
		<td><%=c.getInterestRate()%></td>
		<td><%=c.getOverdraftPenalty()%></td>
		<td><%=c.getOverdraftLimit()%></td>
	</tr>
	<%
		}
	%>

</table>
</div>



</body>
</html>