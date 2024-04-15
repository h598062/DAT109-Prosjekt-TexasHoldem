<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="nb">
<head>
    <title>Mainpage</title>
</head>
<body>
<h1>Velkommen til hovedsiden</h1>

<%--@elvariable id="errors" type="java.util.ArrayList<java.lang.String>"--%>
<c:if test="${not empty errors}">
    <fieldset>
        <legend>Errors:</legend>
        <ul>
            <c:forEach items="${errors}" var="error">
                <li style="color: red">${error}</li>
            </c:forEach>
        </ul>
    </fieldset>
</c:if>

<fieldset>
    <legend>Create Lobby</legend>
    <form action="${pageContext.request.contextPath}/createLobby" method="post">
        <label for="lobbyIdCreate">Lobby ID:</label><br>
        <input type="text" id="lobbyIdCreate" name="lobbyIdCreate"><br>
        <label for="lobbyLeder">Lobby Leader navn:</label><br>
        <input type="text" id="lobbyLeder" name="lobbyLeder"><br>
        <input type="submit" value="Create Lobby">
    </form>
</fieldset>
<fieldset>
    <legend>Join Lobby</legend>
    <%--@elvariable id="lobbies" type="java.util.ArrayList<java.lang.String>"--%>
    <c:if test="${not empty lobbies}">
        <form action="${pageContext.request.contextPath}/joinLobby" method="post">
            <label for="spillerNavn">Spiller navn:</label><br>
            <input type="text" id="spillerNavn" name="spillerNavn"><br>
            <ul>
                <c:forEach items="${lobbies}" var="lobby" varStatus="status">
                    <li><label for="lobbySelect${status.count}">${lobby}</label>
                        <input type="radio" id="lobbySelect${status.count}" name="lobbySelect" value="${lobby}">
                    </li>
                </c:forEach>
            </ul>
            <input type="submit" value="Join Lobby">
        </form>
    </c:if>
</fieldset>
</body>
</html>
