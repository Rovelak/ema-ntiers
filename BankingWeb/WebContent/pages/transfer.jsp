<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="domain.model.Bank"%>
<%@page import="domain.model.Customer"%>
<%@page import="domain.model.Account"%>
<%@page import="domain.model.City"%>
<%@page import="domain.model.Transfer"%>
<%@page import="java.util.Iterator"%>
<jsp:useBean id="currentbank" scope="session"
	class="domain.model.Bank" />
<jsp:useBean id="currentcustomer" scope="session"
	class="domain.model.Customer" />
<jsp:useBean id="currentaccount" scope="session"
	class="domain.model.Account" />
<jsp:useBean id="currenttransfer" scope="session"
	class="domain.model.Transfer" />
<jsp:useBean id="accounts" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="transfers" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="customers" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="cities" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="error" scope="request" class="java.lang.String" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bank Application - Transfers</title>
</head>
<body>
<a href="index.jsp">accueil</a><br/>
<a href="account.do">Comptes</a><br>
<h1><%=currentaccount.getId() %> - Compte - </h1>
<div><%=error%>
<form action="transfer.do">
<input name="page" type="hidden" value="<%=request.getParameter("page") %>" />
<input name="id" type="hidden" value="<%=currenttransfer.getId() %>" /> 
<input name="formbeanclass" type="hidden"
	value="<%=currenttransfer.getClass().getName()%>" /> 
<input name="account" type="hidden" value="<%=currentaccount.getId() %>" /> 
id:<%=currenttransfer.getId() %><br />
date:<input name="transferNumber" type="text" value="<%=currenttransfer.getDate() %>" /><br />
montant:<input name="balance" type="text" value="<%=currenttransfer.getAmount() %>" /><br />
de:<input name="interestRate" type="text" value="<%=currenttransfer.getFromAccount() %>" /><br />   
vers:<input name="overdraftPenalty" type="text" value="<%=currenttransfer.getToAccount() %>" /><br /> 

    
<input name="cmd" type="submit" value="modifier" /> 
<input name="cmd" type="submit" value="ajouter" /> 
<input name="cmd" type="submit" value="suivant" /> 
<input name="cmd" type="submit" value="precedent" />
<input name="cmd" type="submit" value="dupliquer" /> 
<input name="cmd" type="submit" value="foobar" /> 
<input name="cmd" type="submit" value="annuler" /> <br />
</form>
</div>
<hr />
<div>liste des transferts
<table border="1">
	<tr>
		<td>id</td>
		<td>&nbsp;</td>
		<td>date</td>
		<td>montant</td>
		<td>de</td>
		<td>vers</td>
	</tr>
	<% 
		for (int i = 0; i < transfers.size(); i++) {
			Transfer c = (Transfer) transfers.get(i);
	%>
	<tr>
		<%
			if (c.getId() == currenttransfer.getId()) {
		%>
		<td bgcolor="red">
		<%
			} else {
		%>
		
		<td>
		<%
			}
		%> <a href="transfer.do?cmd=editer&id=<%=c.getId()%>&formbeanclass=<%=currenttransfer.getClass().getName()%>&account=<%=currenttransfer.getAccount().getId()%>"><%=c.getId()%></a></td>
		<td><a href="transfer.do?cmd=supprimer&id=<%=c.getId()%>&formbeanclass=<%=currenttransfer.getClass().getName()%>&account=<%=currenttransfer.getAccount().getId()%>">supprimer</a></td>
        <td><%=c.getDate()%></td>
		<td><%=c.getAmount()%></td>
		<td><%=c.getFromAccount()%></td>
		<td><%=c.getToAccount()%></td>
	</tr>
	<%
		}
	%>

</table>
</div>



</body>
</html>