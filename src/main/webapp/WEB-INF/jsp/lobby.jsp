<%--@elvariable id="lobbyId" type="java.lang.String"--%>
<%--@elvariable id="spillerNavn" type="java.lang.String"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="nb">
<head>
    <title>Lobby</title>
    <!--
    JSPM Generator Import Map
    Edit URL: https://generator.jspm.io/#U2NgYGBkDM0rySzJSU1hcCguyc8t0AeTWcUO5noGega6SakliaYAYTzJAykA
  -->
    <script type="importmap">
        {
          "imports": {
            "@stomp/stompjs": "https://ga.jspm.io/npm:@stomp/stompjs@7.0.0/esm6/index.js"
          }
        }
    </script>

    <!-- ES Module Shims: Import maps polyfill for modules browsers without import maps support (all except Chrome 89+) -->
    <script
            async
            src="https://ga.jspm.io/npm:es-module-shims@1.5.1/dist/es-module-shims.js"
            crossorigin="anonymous"
    ></script>
    <link href="resources/static/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<h1>Lobby ${lobbyId}</h1>
<fieldset>
    <legend>Spillere</legend>
    <ul id="spillere"></ul>
</fieldset>
<fieldset>
    <legend>Trekk</legend>
    <button id="call">Call</button>
    <button id="fold">Fold</button>
    <button id="raise">Raise</button>
    <label for="raiseNum">Number:</label>
    <input type="number" id="raiseNum" name="numberInput" value="0">
</fieldset>
<fieldset>
    <legend>Actions</legend>
    <button id="join">Join</button>
    <button id="ready">Ready</button>
    <button id="unready">Unready</button>
    <button id="leave">Leave</button>
    <button id="start">Start</button>
    <button id="end">End</button>
</fieldset>

<fieldset>
    <div class="div1">

    </div>
    <div class="div2">

    </div>
</fieldset>

<script type="module">
    import {Client} from '@stomp/stompjs';

    const lobbyId = '${lobbyId}';
    const spillerNavn = '${spillerNavn}';
    console.log('Lobby: ' + lobbyId);
    console.log('Spiller: ' + spillerNavn);
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    console.log('Protocol: ' + protocol);
    const host = window.location.host;
    console.log('Host: ' + host);
    const client = new Client({brokerURL: protocol + "//" + host + "/lobby-ws"});
    client.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        client.subscribe('/lobbystatus/' + lobbyId, handleMessage);
        client.subscribe('/spiller/' + spillerNavn, handleUserMessage);
        let body = {"action": "JOIN", "spillerNavn": spillerNavn};
        client.publish({destination: '/lobby/action/' + lobbyId, body: JSON.stringify(body)});
    }

    function handleMessage(message) {
        console.log('Received on lobby status channel: ' + message.body);
        const status = JSON.parse(message.body);
        const spillere = status.spillere;
        if (spillere) {
            const spillereListe = document.getElementById('spillere');
            spillereListe.innerHTML = '';
            for (let spiller of spillere) {
                const li = document.createElement('li');
                li.textContent = spiller;
                spillereListe.appendChild(li);
            }
        }
    }

    function handleUserMessage(message) {
        console.log('Received on user channel: ' + message.body);
    }

    client.onWebSocketError = (error) => {
        console.error('Error with websocket', error);
    };

    client.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    };

    client.activate();

    function raise() {
        const number = document.getElementById('raiseNum').value;
        let body = {"trekk": "RAISE", "spillerNavn": spillerNavn, "verdi": number};
        console.log('Raising with ' + number);
        console.log(body);
        client.publish({destination: '/lobby/trekk/' + lobbyId, body: JSON.stringify(body)});
    }

    function call() {
        let body = {"trekk": "CALL", "spillerNavn": spillerNavn, "verdi": 0};
        console.log('Calling');
        console.log(body);
        client.publish({destination: '/lobby/trekk/' + lobbyId, body: JSON.stringify(body)});
    }

    function fold() {
        let body = {"trekk": "FOLD", "spillerNavn": spillerNavn, "verdi": 0};
        console.log('Folding');
        console.log(body);
        client.publish({destination: '/lobby/trekk/' + lobbyId, body: JSON.stringify(body)});
    }

    function start() {
        let body = {"action": "START", "spillerNavn": spillerNavn};
        console.log('Starting');
        console.log(body);
        client.publish({destination: '/lobby/action/' + lobbyId, body: JSON.stringify(body)});
    }

    document.getElementById('raise').addEventListener('click', raise);
    document.getElementById('call').addEventListener('click', call);
    document.getElementById('fold').addEventListener('click', fold);

    document.getElementById('start').addEventListener('click', start);
</script>
</body>
</html>
