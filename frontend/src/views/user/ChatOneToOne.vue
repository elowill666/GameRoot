<template>
    <div class="chat-container" id="chat-page" ref="chatPage">
        <div class="users-list">
            <div class="users-list-container">
                <el-skeleton v-if="!dataLoaded" :rows="12" animated style="padding: 10px 30px 10px 30px; height: 100%;
    overflow: auto;" />
                <ul id="connectedUsers" ref="connectedUsersList">
                </ul>
            </div>
        </div>

        <div class="chat-area">
            <span style="color: black; text-align: end;cursor: pointer;" @click="$emit('showChat', false)">
                <el-icon>
                    <ArrowDown />
                </el-icon></span>
            <div class="chat-area" id="chat-messages" ref="chatArea">
            </div>

            <form id="messageForm" name="messageForm" @submit.prevent="sendMessage">

                <div class="message-input">
                    <input autocomplete="off" type="text" id="message" placeholder="Type your message..."
                        v-model="messageInput">
                    <button><el-icon size="20px">
                            <Promotion />
                        </el-icon></button>
                </div>
            </form>
        </div>
    </div>
</template>
    
<script setup>
import { ref, onMounted, watch } from 'vue';
import axios from "axios";
import defaultUserPhoto from '../../assets/defaultUser.png'
import { useChatStore } from '@/store/chatroomOnetoOne.js'
import { useLoginStore } from "../../store/navbar";
import { storeToRefs } from 'pinia';
const chatStore = useChatStore();
const { connectedUsers } = storeToRefs(chatStore);
const { connectedUsersResponse } = chatStore;
const chatPage = ref(null);
const messageInput = ref('');
const chatArea = ref('');
const url = import.meta.env.VITE_JAVA_URL
const dataLoaded = ref(false)
let stompClient = ref(null);
let username = ref('');
let myuserId = ref(null)
let connectedUsersList = ref(null);
const loginStore = useLoginStore();
const { userDetail } = storeToRefs(loginStore);
let selectedUserId = ref(null);
const axiosObj = axios.create({
    timeout: 5000,
    withCredentials: true
});



function initUserChatroom() {
    myuserId.value = userDetail.value.id;
    username.value = userDetail.value.username;
    connect();
}
//初始化使用者聊天室
onMounted(async () => {
    await connectedUsersResponse()
    initUserChatroom()
});
//連線方法
function connect() {
    const socket = new SockJS(`${url}/ws`);
    stompClient.value = Stomp.over(socket);
    stompClient.value.connect({}, onConnected);
}
//訂閱
function onConnected() {
    stompClient.value.subscribe(`/user/${username.value}/queue/messages`, onMessageReceived);
    stompClient.value.subscribe(`/user/public`, onMessageReceived);
    findAndDisplayConnectedUsers().then();
}
watch(connectedUsers, async () => {
    await findAndDisplayConnectedUsers()
})
//選染聊過天的使用者
async function findAndDisplayConnectedUsers() {
    //呼叫pinia更新connectedUsers
    let alluser = connectedUsers.value
    // 過濾掉當前使用者
    alluser = alluser.filter(user => user.username !== username.value);
    connectedUsersList.value.innerHTML = '';
    alluser.forEach(user => {
        appendUserElement(user, connectedUsersList.value);
        if (alluser.indexOf(user) < alluser.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.value.appendChild(separator);
        }
    });
    dataLoaded.value = true;
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.username;

    const userImage = document.createElement('img');
    userImage.src = user.userphoto || defaultUserPhoto;


    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.username;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId.value = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');

}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === username.value) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.value.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await axiosObj.get(`${url}/messages/${username.value}/${selectedUserId.value}`);
    const userChat = await userChatResponse.data;
    chatArea.value.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });
    chatArea.value.scrollTop = chatArea.value.scrollHeight;
}
function sendMessage() {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient.value) {
        const chatMessage = {
            senderId: username.value,
            recipientId: selectedUserId.value,
            content: messageInput.value.trim(),
            timestamp: new Date()
        };
        stompClient.value.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(username.value, messageInput.value.trim());
        messageInput.value = '';
    }
    chatArea.value.scrollTop = chatArea.value.scrollHeight;
}


async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    if (selectedUserId.value && selectedUserId.value === message.senderId) {
        displayMessage(message.senderId, message.content);
        chatArea.value.scrollTop = chatArea.value.scrollHeight;
    }

    if (selectedUserId.value) {
        document.querySelector(`#${selectedUserId.value}`).classList.add('active');
    }

    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }

}
</script>
    
<style scoped src="../../assets/chatroomOnetoOne.css"></style>