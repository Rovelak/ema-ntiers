<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
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
<link rel="stylesheet" href="./css/screen.css" type="text/css" />
<title>Bank Application - tag -</title>
</head>
<body>
<div id="bodyColumn">
<a href="index.jsp">retour</a>
<h1>Application bancaire</h1>


<div><%=error%></div>
<span class="pageform">
<form action="bank.do">
<input name="id" type="hidden" value="${currentbank.id}" /> <input name="page" type="hidden" value="${param.page}" />
<input name="formbeanclass" type="hidden" value="${currentbank.class.name}" /> 
<table border="0" cellspacing="1">
	<tr>
		<td class="label" width="100">nom:</td>
		<td class="data" width="100">
		<input name="name" type="text" value="${currentbank.name}" /></td>
		<td class="label" width="100">id:</td>
		<td class="data" width="100">${currentbank.id}</td>
		<td class="empty" width="350">&nbsp;</td>
	</tr>
	<tr>
        <td class="label" width="100">adresse:</td>
		<td class="data" width="100">
		<input name="address" type="text" value="${currentbank.address}" /></td>
		<td class="label" width="100">téléphone</td>
		<td class="data" width="100">
		<input name="phone" type="text" value="${currentbank.phone}" /></td>
		<td class="empty" width="350">&nbsp;</td>
	</tr>
	<tr>
		<td class="label" width="100">code postal:</td>
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
		<td width="100"><input name="cmd" type="submit" value="supprimer" /></td>
		<td width="100"><input name="cmd" type="submit" value="clients" /></td>
	

	</tr>

</table>

</form>
</span>
<%
	class RowHighlighter extends org.displaytag.decorator.TableDecorator {
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
		public RowHighlighter(int selectedId) {
	super();
	this.selectedId = selectedId;
		}
	}

	request.setAttribute("rowHighlihter", new RowHighlighter(currentbank
	.getId()));
%>
<div class="section">
<div id="bodyColumn">
<display:table id="currentRow"
	decorator="rowHighlihter" list="${banks}" requestURI="bank.do">
	<display:column title="id">
		<a href="bank.do?cmd=editer&id=${currentRow.id}&page=${param.page}&formbeanclass=${currentbank.class.name}">${currentRow.id}</a>
	</display:column>
	<display:column title="&nbsp">
		<a href="bank.do?cmd=supprimer&id=${currentRow.id}&page=${param.page}&formbeanclass=${currentbank.class.name}">supprimer</a>
	</display:column>
	<display:column title="nom" property="name" />
	<display:column title="adresse" property="address" />
	<display:column title="téléphone" property="phone" />
	<display:column title="code postal" property="zipCode" />
</display:table></div>
</div>
</body>
</html>