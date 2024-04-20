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
<main>
    <h1>Lobby ${lobbyId}</h1>
    <fieldset>
        <legend>Spillere</legend>
        <ul id="spillere" class="ul-horizontal"></ul>
    </fieldset>

    <fieldset id="game" class="game">
        <div id="left-player-bar" class="player-bar">
            <div class="other-player-container">
                <div class="other-player hidden">
                    <div class="other-player-navn">
                    </div>
                    <div class="other-cardContainer">
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                    </div>
                    <div class="other-player-info">
                        <%--                <div class="other-player-status"></div>--%>
                        <div class="other-player-chips"></div>
                        <div class="other-player-bet"></div>
                    </div>
                </div>
            </div>
            <div class="other-player-container">
                <div class="other-player hidden">
                    <div class="other-player-navn">
                    </div>
                    <div class="other-cardContainer">
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                    </div>
                    <div class="other-player-info">
                        <%--                <div class="other-player-status"></div>--%>
                        <div class="other-player-chips"></div>
                        <div class="other-player-bet"></div>
                    </div>
                </div>
            </div>
            <div class="other-player-container">
                <div class="other-player hidden">
                    <div class="other-player-navn">
                    </div>
                    <div class="other-cardContainer">
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                    </div>
                    <div class="other-player-info">
                        <%--                <div class="other-player-status"></div>--%>
                        <div class="other-player-chips"></div>
                        <div class="other-player-bet"></div>
                    </div>
                </div>
            </div>
            <div class="other-player-container">
                <div class="other-player hidden">
                    <div class="other-player-navn">
                    </div>
                    <div class="other-cardContainer">
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                    </div>
                    <div class="other-player-info">
                        <%--                <div class="other-player-status"></div>--%>
                        <div class="other-player-chips"></div>
                        <div class="other-player-bet"></div>
                    </div>
                </div>
            </div>
            <div class="other-player-container">
                <div class="other-player hidden">
                    <div class="other-player-navn">
                    </div>
                    <div class="other-cardContainer">
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                    </div>
                    <div class="other-player-info">
                        <%--                <div class="other-player-status"></div>--%>
                        <div class="other-player-chips"></div>
                        <div class="other-player-bet"></div>
                    </div>
                </div>
            </div>
        </div>
        <div id="center-bar" class="center-bar">
            <%--<div class="top">--%>
            <div class="top game-info-container">
                <div id="forrigeTrekk" class="game-info">
                    <div class="info-txt">Forrige trekk</div>
                    <div class="info-val"></div>
                </div>
                <div id="nesteSpiller" class="game-info">
                    <div class="info-txt">Neste spiller</div>
                    <div class="info-val"></div>
                </div>
                <div id="pott" class="game-info">
                    <div class="info-txt">Pott</div>
                    <div class="info-val"></div>
                </div>
                <div id="raiseTarget" class="game-info">
                    <div class="info-txt">Raise target</div>
                    <div class="info-val"></div>
                </div>
                <div id="vinner" class="game-info hidden">
                    <div class="info-txt">Vinner</div>
                    <div class="info-val"></div>
                </div>
            </div>
            <%--</div>--%>
            <div class="board">
                <div class="board-cardContainer">
                    <div class="board-card card">
                        <div class="symbol"></div>
                        <div class="value"></div>
                    </div>
                    <div class="board-card card">
                        <div class="symbol"></div>
                        <div class="value"></div>
                    </div>
                    <div class="board-card card">
                        <div class="symbol"></div>
                        <div class="value"></div>
                    </div>
                    <div class="board-card card">
                        <div class="symbol"></div>
                        <div class="value"></div>
                    </div>
                    <div class="board-card card">
                        <div class="symbol"></div>
                        <div class="value"></div>
                    </div>
                </div>
            </div>
            <div class="player">
                <div class="player-info">
                    <div id="player-name">
                        Navn: ${spillerNavn}
                    </div>
                    <div id="player-chips">
                        Chips:
                    </div>
                    <div id="player-bet">
                        Current Bet:
                    </div>
                    <div id="dinTur" class="hidden" style="color: red; background-color: yellow">
                        Din tur
                    </div>
                </div>
                <div id="cards" class="player-cardContainer">
                    <div class="player-card card">
                        <div class="symbol"></div>
                        <div class="value"></div>
                    </div>
                    <div class="player-card card">
                        <div class="symbol"></div>
                        <div class="value"></div>
                    </div>
                </div>
            </div>
        </div>
        <div id="right-player-bar" class="player-bar">
            <div class="other-player-container">
                <div class="other-player hidden">
                    <div class="other-player-navn">
                    </div>
                    <div class="other-cardContainer">
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                    </div>
                    <div class="other-player-info">
                        <%--                <div class="other-player-status"></div>--%>
                        <div class="other-player-chips"></div>
                        <div class="other-player-bet"></div>
                    </div>
                </div>
            </div>
            <div class="other-player-container">
                <div class="other-player hidden">
                    <div class="other-player-navn">
                    </div>
                    <div class="other-cardContainer">
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                    </div>
                    <div class="other-player-info">
                        <%--                <div class="other-player-status"></div>--%>
                        <div class="other-player-chips"></div>
                        <div class="other-player-bet"></div>
                    </div>
                </div>
            </div>
            <div class="other-player-container">
                <div class="other-player hidden">
                    <div class="other-player-navn">
                    </div>
                    <div class="other-cardContainer">
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                    </div>
                    <div class="other-player-info">
                        <%--                <div class="other-player-status"></div>--%>
                        <div class="other-player-chips"></div>
                        <div class="other-player-bet"></div>
                    </div>
                </div>
            </div>
            <div class="other-player-container">
                <div class="other-player hidden">
                    <div class="other-player-navn">
                    </div>
                    <div class="other-cardContainer">
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                    </div>
                    <div class="other-player-info">
                        <%--                <div class="other-player-status"></div>--%>
                        <div class="other-player-chips"></div>
                        <div class="other-player-bet"></div>
                    </div>
                </div>
            </div>
            <div class="other-player-container">
                <div class="other-player hidden">
                    <div class="other-player-navn">
                    </div>
                    <div class="other-cardContainer">
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                        <div class="other-card card">
                            <div class="symbol"></div>
                            <div class="value"></div>
                        </div>
                    </div>
                    <div class="other-player-info">
                        <%--                <div class="other-player-status"></div>--%>
                        <div class="other-player-chips"></div>
                        <div class="other-player-bet"></div>
                    </div>
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
            if (msg.hand !== undefined) {
                const hand = Array.from(msg.hand.hand);

                console.log('Player\'s hand:', hand);

                setPlayerCards(hand);
            }
        }

        function setPlayerCards(cards) {
            const playerCardContainer = document.querySelectorAll('#cards > div');
            for (let i = 0; i < playerCardContainer.length; i++) {
                playerCardContainer[i].querySelector('.symbol').textContent = getKortSymbol(cards[i].korttype);
                playerCardContainer[i].querySelector('.value').textContent = getKortVerdi(cards[i].verdi);
            }
        }

        function getKortSymbol(korttype) {
            let retur = "";
            switch (korttype) {
                case "SPAR": {
                    retur += "♠";
                    break;
                }
                case "RUTER": {
                    retur += "♦";
                    break;
                }
                case "HJERTE": {
                    retur += "♥";
                    break;
                }
                case "KLOVER": {
                    retur += "♣";
                    break;
                }
            }
            return retur;
        }
        function getKortVerdi(verdi) {
            let retur = "";
            switch (verdi) {
                case 11: {
                    retur += "J";
                    break;
                }
                case 12: {
                    retur += "Q";
                    break;
                }
                case 13: {
                    retur += "K";
                    break;
                }
                case 14: {
                    retur += "A";
                    break;
                }
                default: {
                    retur += verdi;
                }
            }
            return retur;
        }

        function handleMessage(message) {
            const msg = JSON.parse(message.body);
            console.log('Received on lobby status channel:');
            console.log(msg);

            if (msg.action !== undefined) {
                for (let spiller of msg.spillere) {
                    if (spiller.navn === spillerNavn) continue;
                    const spElm = document.getElementById('spiller-' + spiller.navn);
                    if (spElm) {
                        spElm.querySelector('.other-player-navn').textContent = spiller.navn;
                    } else {
                        console.log('Adding player to lobby:', spiller.navn)
                        const otherPlayerElms = document.querySelectorAll('.other-player.hidden');
                        const elm = otherPlayerElms[0];
                        elm.classList.remove('hidden');
                        elm.id = "spiller-" + spiller.navn;
                        elm.querySelector('.other-player-navn').textContent = spiller.navn;
                    }
                }

                if (msg.action === 'START') {
                    document.getElementById('start').classList.add('hidden');
                    document.getElementById('restart').classList.remove('hidden');
                } else if (msg.action === 'RESTART') {
                    document.getElementById('vinner').classList.add('hidden');
                    document.getElementById('raiseTarget').classList.remove('hidden');
                    document.getElementById('nesteSpiller').classList.remove('hidden');
                    const cardElms = document.querySelectorAll('.board-card');
                    for (let i = 0; i < cardElms.length; i++) {
                        cardElms[i].classList.remove("flipped");
                        cardElms[i].querySelector('.symbol').textContent = "";
                        cardElms[i].querySelector('.value').textContent = "";
                    }
                } else if (msg.action === 'END') {
                    const elem = document.querySelector('h1');
                    elem.textContent = 'Lobby har blitt avsluttet, returnerer til hovedsiden...';
                    setTimeout(() => {
                        leave();
                    }, 5000);
                }
            }
            if (msg.trekk !== undefined) {
                let lastMove = document.querySelector('#forrigeTrekk > .info-val');
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
                lastMove.textContent = msg.spillerNavn + ' ' + trekkTekst;
            }

            if (msg.pott !== undefined) {
                let pottElement = document.querySelector('#pott > .info-val');
                pottElement.textContent = msg.pott;
            }

            if (msg.raiseTarget !== undefined) {
                let raiseTargetElement = document.querySelector('#raiseTarget > .info-val');
                raiseTargetElement.textContent = msg.raiseTarget;
            }

            if (msg.spillerSinTur !== undefined) {
                let nesteSpillerElement = document.querySelector('#nesteSpiller .info-val');
                nesteSpillerElement.textContent = msg.spillerSinTur;
                if (msg.spillerSinTur === spillerNavn) {
                    let dinTur = document.getElementById("dinTur");
                    dinTur.textContent = 'Din tur!';
                    dinTur.classList.remove('hidden');
                } else {
                    document.getElementById('dinTur').classList.add('hidden');
                }
            }

            if (msg.vinner !== undefined) {
                let vinnerElement = document.querySelector('#vinner > .info-val');
                vinnerElement.textContent = msg.vinner;
                document.getElementById('vinner').classList.remove('hidden');
                document.getElementById('raiseTarget').classList.add('hidden');
                document.getElementById('nesteSpiller').classList.add('hidden');
                document.querySelector('#forrigeTrekk > .info-val').textContent = msg.vinner + ' vant potten!';
            }

            const bordkort = msg.bordKort;
            if (bordkort !== undefined) {
                const cardElms = document.querySelectorAll('.board-card');
                for (let i = 0; i < bordkort.length; i++) {
                    cardElms[i].classList.add("flipped");
                    cardElms[i].querySelector('.symbol').textContent = getKortSymbol(bordkort[i].korttype);
                    cardElms[i].querySelector('.value').textContent = getKortVerdi(bordkort[i].verdi);
                }
            }
            const spillere = msg.spillere;
            if (spillere !== undefined) {
                const spillereListe = document.getElementById('spillere');
                spillereListe.innerHTML = '';
                for (let spiller of spillere) {
                    const li = document.createElement('li');
                    li.textContent = spiller.navn === undefined ? spiller : spiller.navn;
                    spillereListe.appendChild(li);
                    if (spiller.navn === spillerNavn) {
                        document.getElementById('player-chips').textContent = 'Chips: ' + spiller.chips;
                        document.getElementById('player-bet').textContent = 'Current Bet: ' + spiller.currentBet;
                        continue;
                    }
                    const spElm = document.getElementById('spiller-' + spiller.navn);
                    if (spElm) {
                        switch (spiller.status) {
                            case "WAITING": {
                                spElm.querySelectorAll('.other-card').forEach(card => card.className = 'other-card card status-waiting');
                                break;
                            }
                            case "DONE": {
                                spElm.querySelectorAll('.other-card').forEach(card => card.className = 'other-card card status-done');
                                break;
                            }
                            case "ALLIN": {
                                spElm.querySelectorAll('.other-card').forEach(card => card.className = 'other-card card status-allin');
                                break;
                            }
                            case "FOLD": {
                                spElm.querySelectorAll('.other-card').forEach(card => card.className = 'other-card card status-fold');
                                break;
                            }
                        }
                        // trenger ikke denne, kortene bytter farge når status endrer seg
                        // spElm.querySelector('.other-player-status').textContent = spiller.status;
                        spElm.querySelector('.other-player-chips').textContent = 'Chips: ' + spiller.chips;
                        spElm.querySelector('.other-player-bet').textContent = 'Bet: ' + spiller.currentBet;
                    }
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
</main>
</body>
</html>
