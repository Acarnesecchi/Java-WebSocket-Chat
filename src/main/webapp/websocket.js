window.onload = init;

const outputDiv = document.getElementById('output-div');
let sender;
let receiver;
let selectedChat;
let indexOfMessage = 0;

function init() {
    document.getElementById("text").style.display = "none";
}
function onOpen() {
    console.log("Connection established");
    socket.send("getEmployees");
}
const socket = new WebSocket("ws://localhost:8081/websocketexample/actions");
socket.onmessage = onMessage;
socket.onopen = onOpen;
function onMessage(event) {
    console.log("Message received: " + event.data);
    const message = JSON.parse(event.data);
    if (message.action === "addEmployees") {
        let employees = JSON.parse(message.employees);
        console.log(employees);
        for (let i = 0; i < employees.length; i++) {
            const employee = employees[i];
            const employeeElement = createEmployeeElement(employee);
            document.getElementById("list").appendChild(employeeElement)
            employeeElement.onclick = function () {
                const password = prompt("Enter password");
                const str = employee.id + " " + password;
                handlePassword(str);
                sender = employee.id;
                deleteEmployeeElement(employee);

            };
        };
    } else if (message.action === "addChats") {
        let chats = JSON.parse(message.chats);
        console.log(chats);
        for (let i = 0; i < chats.length; i++) {
            const chat = chats[i];
            const chatElement = createChatElement(chat);
            document.getElementById("list").appendChild(chatElement)
            chatElement.onclick = function () {
                startChat(chat);
            }
        }
    } else if (message.action === "addMessage") {
        const messageContent = message.message;
        const messageAlign = message.sender;
        const messageElement = createMessageElement(messageContent, messageAlign);
        document.getElementById("output-div").appendChild(messageElement)
    } else if (message.action === "login") {
        if (message.success === true) {
            alert("Login successful!");
            deleteEmployeeElement(message.employee);
        }
        else
            alert("Login failed!");
    }
}

function createEmployeeElement(employee) {
    const employeeElement = document.createElement("button");
    employeeElement.innerHTML = employee.name;
    employeeElement.id = "employee" + employee.id;
    return employeeElement;
}
function createChatElement(chat) {
    const chatElement = document.createElement("button");
    chatElement.innerHTML = chat.name;
    chatElement.id = "chat" + chat.id;
    return chatElement;
}
function createMessageElement(messageContent, messageAlign) {
    const messageElement = document.createElement("div");
    messageElement.innerHTML = messageContent;
    messageElement.id = "message " + indexOfMessage;
    if (messageAlign === sender) {
        messageElement.style.textAlign = "right";
    } else {
        messageElement.style.textAlign = "left";
    }
    indexOfMessage++;
    return messageElement;
}

function deleteEmployeeElement(employee) {
    const name = employee.name;
    const id = employee.id;
    document.getElementById("list").innerHTML = "";
    document.getElementById("user").innerHTML = "Logged as: " + name;
    socket.send("getChatsOfUser " + id);
}
function startChat(chat) {
    socket.send("getMessages " + chat.id);
    socket.send("setChat " + chat.id);
    document.getElementById("text").style.display = "block";
    selectedChat = chat.id;
    if(chat.user1 === sender) {
        receiver = chat.user2;
    } else {
        receiver = chat.user1;
    }

    console.log("caca" + selectedChat);
    console.log("culo" + sender);
    console.log("pedo" + receiver);
}

function createElement(str) {
    const div = document.createElement("div");
    div.innerHTML = str;
    document.getElementById("body").appendChild(div);
}

function handleMessages() {
    const message = inputText.value;
    if (message === "") {
        alert("Message cannot be empty!");
        return;
    } else {
        socket.send("message " + selectedChat + " " + sender + " " + receiver + " " + message);
        inputText.value = "";
    }
}


function handlePassword(str) {
    socket.send("login " + str);
}
