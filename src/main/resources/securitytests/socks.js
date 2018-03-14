function tokenGetRequest() {
    return new Promise(function (resolve, reject) {
        let xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            try {
                if (this.readyState === 4 && this.status === 200) {
                    resolve(getToken(this.responseText));
                }
                else {
                    // reject(this.readyState + ' ' + this.status);
                }
            } catch (e) {
                reject(e);
            }
        };
        xmlhttp.open("GET", "/csrf", true);
        xmlhttp.send();
    })
}


function getToken(result) {
    let token = {};
    try {
        let tokenObj = JSON.parse(result);
        token[tokenObj.headerName] = tokenObj.token;
    } catch (e) {
        console.log(e);
    } finally {
        return token;
    }
}


let stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    // tokenGetRequest().then((result) => {
    console.log("TRYING TO CONNECCCT");
    let socket = new SockJS('http://localhost:8080/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        //must use topic prefix to clientInterest
        stompClient.subscribe('/topic/clientInterest', function (greeting) {
            console.log(greeting.body);
            showGreeting(greeting.body);
        });
    });
    // })
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    // tokenGetRequest().then((result) => {
    stompClient.send("/app/server", {}, JSON.stringify({
            'name': $("#name").val()
        })
    );
    // })
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });
});