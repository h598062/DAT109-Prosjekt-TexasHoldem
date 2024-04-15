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
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<%--<main>--%>
<h1>Lobby ${lobbyId}</h1>
<fieldset>
    <legend>Spillere</legend>
    <ul id="spillere"></ul>
</fieldset>
<fieldset>
    <div>
        <h3 id="forrigeTrekk">
            Forrige trekk:
        </h3>
        <h3 id="nesteSpiller">

        </h3>
    </div>
</fieldset>

<fieldset id="game" class="game">
    <div id="left-player-bar" class="player-bar">
        <div class="other-player">
            <div class="other-cardContainer">
                <div class="other-card card"></div>
                <div class="other-card card"></div>
            </div>
        </div>
        <div class="other-player">
            <div class="other-cardContainer">
                <div class="other-card card"></div>
                <div class="other-card card"></div>
            </div>
        </div>
        <div class="other-player">
            <div class="other-cardContainer">
                <div class="other-card card"></div>
                <div class="other-card card"></div>
            </div>
        </div>
        <div class="other-player">
            <div class="other-cardContainer">
                <div class="other-card card"></div>
                <div class="other-card card"></div>
            </div>
        </div>
        <div class="other-player">
            <div class="other-cardContainer">
                <div class="other-card card"></div>
                <div class="other-card card"></div>
            </div>
        </div>
    </div>
    <div id="center-bar" class="center-bar">
        <div class="top"></div>
        <div class="board">
            <div class="board-cardContainer">
                <div class="board-card card flipped"></div>
                <div class="board-card card flipped"></div>
                <div class="board-card card flipped"></div>
                <div class="board-card card"></div>
                <div class="board-card card"></div>
            </div>
        </div>
        <div class="player" style="">
            <div id="cards" class="player-cardContainer">
                <div class="player-card card"></div>
                <div class="player-card card"></div>
            </div>
        </div>
    </div>
    <div id="right-player-bar" class="player-bar">
        <div class="other-player">
            <div class="other-cardContainer">
                <div class="other-card card"></div>
                <div class="other-card card"></div>
            </div>
        </div>
        <div class="other-player">
            <div class="other-cardContainer">
                <div class="other-card card"></div>
                <div class="other-card card"></div>
            </div>
        </div>
        <div class="other-player">
            <div class="other-cardContainer">
                <div class="other-card card"></div>
                <div class="other-card card"></div>
            </div>
        </div>
        <div class="other-player">
            <div class="other-cardContainer">
                <div class="other-card card"></div>
                <div class="other-card card"></div>
            </div>
        </div>
        <div class="other-player">
            <div class="other-cardContainer">
                <div class="other-card card"></div>
                <div class="other-card card"></div>
            </div>
        </div>
    </div>
</fieldset>
<fieldset id="gameControls">
    <button id="check">Check</button>
    <button id="call">Call</button>
    <button id="fold">Fold</button>
    <button id="allin">All In</button>
    <button id="raise">Raise</button>
    <input id="raiseNum" type="number" placeholder="Amount" value="5">
</fieldset>
<fieldset id="actions">
<%--    <button id="join">Join</button>--%>
<%--    <button id="ready">Ready</button>--%>
<%--    <button id="unready">Unready</button>--%>
    <button id="leave">Leave</button>
    <button id="start">Start Game</button>
    <button id="end">End Lobby</button>
    <button id="restart" class="hidden">Start new Game</button>
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
    const path = window.location.pathname;
    console.log('Path: ' + path);
    const wsBrokerURL = protocol + "//" + host + path + "-ws";
    console.log('Broker URL: ' + wsBrokerURL)
    const client = new Client({brokerURL: wsBrokerURL});
    client.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        client.subscribe('/lobbystatus/' + lobbyId, handleMessage);
        client.subscribe('/spiller/' + spillerNavn, handleUserMessage);
        let body = {"action": "JOIN", "spillerNavn": spillerNavn};
        client.publish({destination: '/lobby/action/' + lobbyId, body: JSON.stringify(body)});
    }

    function handleUserMessage(message) {
        const msg = JSON.parse(message.body);
        console.log('Received on player private channel:');
        console.log(msg);
        if (msg.hand) {
            const hand = Array.from(msg.hand.hand);

            console.log('Player\'s hand:', hand);

            const playerCardContainer = document.querySelectorAll('#cards > div');

            for (let i = 0; i < playerCardContainer.length; i++) {
                playerCardContainer[i].textContent = hand[i].korttype + " " + hand[i].verdi;
            }
        }

        if (msg.msg) {
            console.log('Received message:', msg.msg);
        }
    }

    function handleMessage(message) {
        const msg = JSON.parse(message.body);
        console.log('Received on lobby status channel:');
        console.log(msg);

        if (msg.action) {
            if (msg.action === 'START') {
                document.getElementById('start').classList.add('hidden');
                document.getElementById('restart').classList.remove('hidden');
            }
            else if (msg.action === 'END') {
                const elem = document.querySelector('h1');
                elem.textContent = 'Lobby har blitt avsluttet, returnerer til hovedsiden...';
                setTimeout(() => {
                    leave();
                }, 5000);
            }
        }
        if (msg.trekk) {
            let lastMove = document.getElementById('forrigeTrekk');
            let trekkTekst = '';
            switch (msg.trekk) {
                case 'ALL_IN':
                    trekkTekst = 'gikk all in';
                    break;
                case 'CHECK':
                    trekkTekst = 'valgte å checke';
                    break;
                case 'CALL':
                    trekkTekst = 'valgte å calle';
                    break;
                case 'FOLD':
                    trekkTekst = 'valgte å folde';
                    break;
                case 'RAISE':
                    trekkTekst = 'høynet til ' + msg.mengde;
                    break;
                default:
                    trekkTekst = 'gjorde et ukjent trekk';
            }
            lastMove.textContent = 'Forrige trekk: ' + msg.spillerNavn + ' ' + trekkTekst;
        }

        const bordkort = msg.bordKort;
        if (bordkort) {
            const boardCardContainer = document.querySelector('.board-cardContainer');
            boardCardContainer.innerHTML = '';
            bordkort.forEach(card => {
                const cardDiv = document.createElement('div');
                cardDiv.className = 'board-card card flipped';
                cardDiv.textContent = card.korttype + " " + card.verdi;
                boardCardContainer.appendChild(cardDiv);
            });
        }
        const spillere = msg.spillere;
        if (spillere) {
            const spillereListe = document.getElementById('spillere');
            spillereListe.innerHTML = '';
            for (let spiller of spillere) {
                const li = document.createElement('li');
                li.textContent = spiller.navn === undefined ? spiller : spiller.navn;
                spillereListe.appendChild(li);
            }
        }
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
        let body = {"trekk": "RAISE", "spillerNavn": spillerNavn, "mengde": parseInt(number)};
        console.log('Raising with ' + number);
        console.log(body);
        client.publish({destination: '/lobby/trekk/' + lobbyId, body: JSON.stringify(body)});
    }

    function call() {
        let body = {"trekk": "CALL", "spillerNavn": spillerNavn, "mengde": 0};
        console.log('Calling');
        console.log(body);
        client.publish({destination: '/lobby/trekk/' + lobbyId, body: JSON.stringify(body)});
    }

    function fold() {
        let body = {"trekk": "FOLD", "spillerNavn": spillerNavn, "mengde": 0};
        console.log('Folding');
        console.log(body);
        client.publish({destination: '/lobby/trekk/' + lobbyId, body: JSON.stringify(body)});
    }

    function check() {
        let body = {"trekk": "CHECK", "spillerNavn": spillerNavn, "mengde": 0};
        console.log('Checking');
        console.log(body);
        client.publish({destination: '/lobby/trekk/' + lobbyId, body: JSON.stringify(body)});
    }

    function allin() {
        let body = {"trekk": "ALL_IN", "spillerNavn": spillerNavn, "mengde": 0};
        console.log('All in');
        console.log(body);
        client.publish({destination: '/lobby/trekk/' + lobbyId, body: JSON.stringify(body)});
    }

    function leave() {
        let body = {"action": "LEAVE", "spillerNavn": spillerNavn};
        console.log('Leaving the lobby');
        console.log(body);
        client.publish({destination: '/lobby/action/' + lobbyId, body: JSON.stringify(body)});
        window.location.href = window.location.origin + '/TexasHoldem';
    }

    function start() {
        let body = {"action": "START", "spillerNavn": spillerNavn};
        console.log('Starting');
        console.log(body);
        client.publish({destination: '/lobby/action/' + lobbyId, body: JSON.stringify(body)});
    }

    function end() {
        let body = {"action": "END", "spillerNavn": spillerNavn};
        console.log('Starting');
        console.log(body);
        client.publish({destination: '/lobby/action/' + lobbyId, body: JSON.stringify(body)});
    }

    function restart() {
        let body = {"action": "RESTART", "spillerNavn": spillerNavn};
        console.log('Starting');
        console.log(body);
        client.publish({destination: '/lobby/action/' + lobbyId, body: JSON.stringify(body)});
    }

    document.getElementById('raise').addEventListener('click', raise);
    document.getElementById('call').addEventListener('click', call);
    document.getElementById('fold').addEventListener('click', fold);
    document.getElementById('check').addEventListener('click', check);
    document.getElementById('allin').addEventListener('click', allin);

    document.getElementById('leave').addEventListener('click', leave);
    document.getElementById('start').addEventListener('click', start);
    document.getElementById('end').addEventListener('click', end);
    document.getElementById('restart').addEventListener('click', restart);
</script>
<%--</main>--%>
</body>
</html>
