<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="nb">
<head>
    <title>Texas hold’em</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <h1>TEXAS HOLD’EM</h1>

    <div>
        <fieldset style="float:left;">
            <c:forEach var="i" begin="1" items="${lobbies}" step="1">
                <p>Lobby ${i}</p>
            </c:forEach>
        </fieldset>

        <div style="float:left; margin-left:20px;">
            <button>Lag ny lobby</button>
        </div>

        <div style="clear:both;"></div> <!-- Dette er for å rydde opp i floating elementer -->
    </div>
</body>
</html>
