'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var signoutButton = document.querySelector('#signoutButton');

var stompClient = null;
var username = null;

function connect(event) {
	username = document.querySelector('#name').value.trim();
	document.querySelector('#name').value = '';
	
	if(username) {
		usernamePage.classList.add('hidden');
		chatPage.classList.remove('hidden');
		
		var socket = new SockJS('/ws');
		stompClient = Stomp.over(socket);
		
		stompClient.connect({}, onConnected, onError);
	}
	
	event.preventDefault();
}

function onConnected() {
	stompClient.subscribe('/topic/public', onMessageReceived);
	
	stompClient.send("/app/addUser",
		{},
		JSON.stringify({sender: username, type: 'JOIN'})
	)
	
	connectingElement.classList.add('hidden');
}

function onError(_error) {
	connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	connectingElement.style.color = 'red';
}

function sendMessage(event) {
	var messageContent = messageInput.value.trim();
	if (messageContent && stompClient) {
		var chatMessage = {
			sender: username,
			content: messageContent,
			type: 'CHAT'
		};
		stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage));
		messageInput.value = '';	
	}
	event.preventDefault();
}

function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);
	var messageElement = document.createElement('li');
	
	if (message.type === 'JOIN') {
		messageElement.classList.add('event-message');
		messageElement.textContent = message.sender + ' joined!';
	} else if (message.type === 'LEAVE') {
		messageElement.classList.add('event-message');
		messageElement.textContent = message.sender + ' left!';
	} else if (message.type === 'CHAT') {
		messageElement.classList.add('chat-message');
		
		if (message.sender === username) {
			messageElement.classList.add('own');
		} else {
			messageElement.classList.add('other');
		}
		
		var usernameSpan = document.createElement('span');
		usernameSpan.textContent = message.sender + ':';
		messageElement.appendChild(usernameSpan);
		
		var text = document.createElement('span');
		text.textContent = ' ' + message.content;
		messageElement.appendChild(text);
	}
	
	messageArea.appendChild(messageElement);
	messageArea.scrollTop = messageArea.scrollHeight;
}

function signOut() {
	stompClient.send("/app/removeUser", {}, JSON.stringify({ sender: username, type: 'LEAVE' }))
	
	if (stompClient) {
		stompClient.disconnect();
	}
	
	messageArea.innerHTML = '';
	
	chatPage.classList.add('hidden');
	usernamePage.classList.remove('hidden');
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
signoutButton.addEventListener('click', signOut, true);