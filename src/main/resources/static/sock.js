function getRequest(method, destination, content) {
    console.log("IAOF");
    return new Promise(function (resolve, reject) {
        let xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            try {
                if (this.readyState === 4 && this.status === 200) {
                    // resolve(getToken(this.responseText));
                    resolve(JSON.stringify(this.responseText));
                    console.log(JSON.stringify(this.responseText));
                }
                else {
                    // reject(this.readyState + ' ' + this.status);
                }
            } catch (e) {
                reject(e);
            }
        };
        xmlhttp.open(method, destination, true);
        xmlhttp.send(content);
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

let channelName;

function connect() {
    channelName = Date.now().toString() + window.performance.now().toString();

    //A client can be interested in many instances of an application name (all of the CCTs) (default)
    //Can be interested in one named instance of a CCT
    //Can mix and match as appropriate between apps and different instances of app types.
    let interests = {
        interests: JSON.stringify({
            apps: {
                APP: {
                    options: ["single=CCT1"],
                    cats:["update", "status"],
                    props:[]
                },
                TFE: {
                    options: ["single=CCT1"],
                    cats:["STREAM1.UPDATE", "STREAM1.STATUS"],
                    props:[]
                },
            },
            channelName: channelName
        })
    };

    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.debug = null; //This should stop the console from being flooded with sock messages
    stompClient.connect(interests, function (frame) {
        setConnected(true);
        // console.log('Connected: ' + frame);
        //must use topic prefix to clientInterest
        stompClient.subscribe('/topic/' + channelName, function (greeting) {
            // console.log(greeting.body);
            showGreeting(greeting.body);
        });
    });
    // })
    // })
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    // console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/server", {}, JSON.stringify({
            'name': $("#name").val(),
            'app': $("#app").val(),
            channelName
        })
    );
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + ":" + message + "\n" + "</td></tr>");
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