<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@page import="domain.model.City"%>
<jsp:useBean id="currentbank" scope="session"
	class="domain.model.Bank" />
<jsp:useBean id="cities" scope="session" class="java.util.ArrayList" />
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
<link rel="stylesheet" href="./css/screen.css" type="text/css" />
<title>Bank Application - tag - paging - sorting</title>
</head>
<body>
<div id="bodyColumn"><a href="index.jsp">retour</a>
<h1>Application bancaire</h1>
</div>
<div><%=error%></div>
<span class="pageform">
<form action="bank.do">
<input name="page" type="hidden" value="${param.page}" /> 
<input name="id" type="hidden" value="<jsp:getProperty name="currentbank" property="id"/>" />
<input name="formbeanclass" type="hidden" value="<%=currentbank.getClass().getName()%>" /> 

<table border="0" cellspacing="1">
	<tr>
		<td class="label" width="100">id:</td>
		<td class="data" width="100"><jsp:getProperty name="currentbank"
			property="id" /></td>
		<td class="label" width="100">name:</td>
		<td class="data" width="100"><input name="name" type="text"
			value="<jsp:getProperty name="currentbank" property="name"/>" /></td>
		<td class="empty" width="350">&nbsp;</td>
	</tr>
	<tr>
		<td class="label" width="100">address:</td>
		<td class="data" width="100"><input name="address" type="text"
			value="<jsp:getProperty name="currentbank" property="address"/>" /></td>
		<td class="label" width="100">phone</td>
		<td class="data" width="100"><input name="phone" type="text"
			value="<jsp:getProperty name="currentbank" property="phone"/>" /></td>
		<td class="empty" width="350">&nbsp;</td>
	</tr>
	
	
	
	
	<tr>
		<td class="label" width="100">zipCode:</td>
		<td class="data" width="100"><input name="zipCode" type="text"
			value="<jsp:getProperty name="currentbank" property="zipCode"/>" /></td>
		<td class="label" width="100">&nbsp;</td>
		<td class="data" width="100">&nbsp;</td>
		<td class="empty" width="350">&nbsp;</td>
	</tr>
	
	<tr>
		<td class="label" width="100">commune:</td>
		<td class="data" width="100">	
<select name="commune" style=" width : 144px;">
<option value=" ">no city</option>
<%for (int c=0;c<cities.size();c++){%>      
<option <% 
	if (cities.get(c).equals(currentbank.getCity())){
		%> selected <%	
	}
	%>value="<%=((City)(cities.get(c))).getId() %>"><%=((City)(cities.get(c))).getName() %></option>
	<%}%>
</select>	
		</td>
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

<%
 	class RowHighlihter extends org.displaytag.decorator.TableDecorator {
  		private int selectedId;
  		@Override
  		public String addRowClass() {
  			boolean t = ((domain.model.Bank) getCurrentRowObject())
  					.getId() == selectedId;
  			if (t)
  				return "highlight";
  			else
  				return "";
  		}
  		public RowHighlihter(int selectedId) {
  			super();
  			this.selectedId = selectedId;
  		}

  	}
  	request.setAttribute("rowHighlihter", new RowHighlihter(currentbank
  			.getId()));
 %>

<div class="section">
<div id="bodyColumn">
<display:table decorator="rowHighlihter"
	id="currentRow" list="${banks}" pagesize="4" defaultsort="3"
	defaultorder="descending" requestURI="bank.do">
	<display:column title="id">
		<a href="bank.do?cmd=editer&id=${currentRow.id}&page=${param.page}&formbeanclass=${currentbank.class.name}">${currentRow.id}</a>
	</display:column>
	<display:column title="&nbsp">
		<a
			href="bank.do?cmd=supprimer&id=${currentRow.id}&page=${param.page}&formbeanclass=${currentbank.class.name}">supprimer</a>
	</display:column>
	<display:column property="name" sortable="true" headerClass="sortable" />
	<display:column property="address" />
	<display:column property="phone" />
	<display:column property="zipCode" />
	<display:column title="city">
		${currentRow.city==null?"":currentRow.city.name}
	</display:column>
</display:table>
</div>
</div>
</body>
</html>