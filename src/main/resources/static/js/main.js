let stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);

    if(connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    const socket = new SockJS("/gs-guide-websocket");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
        setConnected(true);
        console.log(`Connected: ${frame}`);
        stompClient.subscribe("/employees", msg => {
            showGreeting(msg);
        })
    })
}

function disconnect() {
    if(stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/notify", {}, "yes");
}

function showGreeting(msg) {
    $("#greetings").append(`<tr><td>${msg}</td></tr>`)
}

$(function () {
   $("form").on("submit", e => e.preventDefault());
   $("#connect").click(_ => connect());
   $("#disconnect").click(_ => disconnect());
   $("#send").click(_ => sendName());
   console.log("Hello world!");
});