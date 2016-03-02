<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="cz.inited.ninemads.ApiWeb"%>
<%
String username = request.getParameter("username");
if (username == null) {
	username = "";
}

String castka = request.getParameter("castka");
if (castka == null) {
	castka = "100";
}

String msg = null;

String akce = request.getParameter("akce");
if (akce != null) {
	ApiWeb aw = new ApiWeb();
	Map res = aw.doAction("addKredit", request, response);
	String status = (String)res.get("status");
	String kredit = (String)res.get("credit");
	msg = "Aktuální kredit: " + kredit + "EUR";
}


%><!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="format-detection" content="telephone=no" />
    <meta name="viewport" content="user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, height=device-height, target-densitydpi=160" />
    <title>Dobití kreditu</title>
</head>
<body>
<%
  if (msg != null) out.println(msg);
%>
<form>
	Username: <input type="text" name="username" value="<%=username %>" /><br />
	Kredit: <input type="number" name="castka" value="<%=castka %>"/> EUR<br />
	<input type="submit" value="Odeslat" name="akce" />
</form>
</body>
</html>
