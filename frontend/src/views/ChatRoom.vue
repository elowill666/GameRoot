<template>
    <div class="chat-toggle-box" @click="showChatRoom" v-show="!isChatRoomVisible">

        <el-icon size="28px">
            <ChatDotRound />
        </el-icon>
    </div>
    <div id="chat-page" ref="chatPage" v-show="isChatRoomVisible && !showOneToOne.showOneToOne">
        <div class="chat-container" :class="{ 'expanded': isChatRoomVisible }">
            <div class="chat-header">
                <span style="color: black; text-align: left; font-size:20px;cursor: pointer;"
                    @click="toggleOneToOne"><el-icon size="25px" color="#ffc107">
                        <Avatar />
                    </el-icon><el-icon size="20px" color="#ffc107">
                        <Comment />
                    </el-icon></span>
                <span style="color: black;"><el-icon size="30px" color="#ffc107">
                        <ChatLineRound />
                    </el-icon></span>
                <span style="color: black; text-align: end;cursor: pointer;" @click="toggleChatRoom"
                    v-show="isChatRoomVisible"> <el-icon>
                        <ArrowDown />
                    </el-icon></span>
            </div>
            <div class="connecting" ref="connectingElement">
                連線中...
            </div>

            <ul id="messageArea" ref="messageArea">

            </ul>

            <form id="messageForm" name="messageForm" @submit.prevent="sendMessage">
                <div class="form-group">
                    <div class="input-group clearfix">
                        <input type="text" id="message" placeholder="Type a message..." autocomplete="off"
                            class="form-control" v-model="messageInput" />
                        <button style="border-radius: 5px;padding: 5px;" @click="sendMessage" type="button"><el-icon
                                size="20px" color="white">
                                <Promotion />
                            </el-icon></button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>
    
<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from "vue-router"
import axios from "axios";
import Swal from 'sweetalert2'
import { useLoginStore } from '../store/navbar'
import { storeToRefs } from "pinia";

const showOneToOne = useLoginStore()
const { userDetail } = storeToRefs(showOneToOne);
const messageInput = ref('');
const messageArea = ref(null);
const connectingElement = ref(null);
const stompClient = ref(null);
const router = useRouter()
const url = import.meta.env.VITE_JAVA_URL
const chatPage = ref(null);
let senderphoto = ref(null)

const isChatRoomVisible = ref(false);
// let userDetail = reactive({});

const axiosObj = axios.create({
    timeout: 5000,
    withCredentials: true
});

function toggleOneToOne() {
    showOneToOne.showOneToOne = !showOneToOne.showOneToOne
}

function checkLoginAndConnectChatroom() {
    const userId = localStorage.getItem('userId');
    axiosObj.get(`${url}/user/getuser/${userId}`)
        .then(res => {
            userDetail.value = res.data
            connect();
            isChatRoomVisible.value = !isChatRoomVisible.value;
        }).catch(err => {
            console.log(err);
        })
}
//點擊按鈕後進行登入校驗 然後連線websocket 開啟聊天室
const showChatRoom = () => {
    checkLoginAndConnectChatroom();
};
const toggleChatRoom = () => {
    isChatRoomVisible.value = !isChatRoomVisible.value;
}

//1. 建立websocket連線
function connect() {
    // 如果已經連上了就不要再連一次
    if (stompClient.value) {
        console.log('User is onconnect.');
        return
    }
    console.log(`output->`, '連線中')
    const socket = new SockJS(`${url}/ws`);
    console.log(`output->`, '連線了')
    stompClient.value = Stomp.over(socket);
    console.log(`output->`, '連線2')
    stompClient.value.connect({}, onConnected, onError);
    console.log(`output->`, '連線3')
}

//2. 訂閱 /topic/public 並呼叫回呼函數接收訊息
function onConnected() {
    stompClient.value.subscribe('/topic/public', onMessageReceived);
    stompClient.value.send("/app/chat.addUser",
        {},
        JSON.stringify({ sender: userDetail.value.username, type: 'JOIN' })
    )
    connectingElement.value.classList.add('hidden');
}

function onError(error) {
    connectingElement.value.textContent = '無法連線請重試!';
    connectingElement.value.style.color = 'red';
}
//3. 送出訊息
function sendMessage() {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient.value) {
        const chatMessage = {
            sender: userDetail.value.username,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.value.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
}
async function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);

    let messageElement = document.createElement('li');

    if (message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        messageElement.textContent = message.sender + ' 加入聊天室!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        messageElement.textContent = message.sender + ' 已離開!';
    } else {
        try {
            if (userDetail.value.username !== message.sender) {
                const res = await axiosObj.get(`${url}/user/getuser/${message.sender}`);
                senderphoto.value = res.data.userphoto;

                messageElement.classList.add('chat-message');

                const avatarElement = document.createElement('img');
                avatarElement.src = senderphoto.value;
                avatarElement.style.width = '45px';
                avatarElement.style.height = '45px';
                avatarElement.style.borderRadius = '50%';
                avatarElement.style.marginRight = '10px';
                avatarElement.style.objectFit = 'cover';
                avatarElement.style.overflow = 'hidden';

                messageElement.appendChild(avatarElement);
            } else {
                messageElement.classList.add('my-message');
            }

            const usernameElement = document.createElement('span');
            usernameElement.textContent = message.sender;
            messageElement.appendChild(usernameElement);

            const textElement = document.createElement('p');
            const messageText = document.createTextNode(message.content);
            textElement.appendChild(messageText);

            messageElement.appendChild(textElement);
        } catch (err) {
            console.log(`output->err`, err);
        }
    }

    // 确保 messageArea 可用并附加 messageElement
    const messageAreaElement = document.getElementById('messageArea');
    if (messageAreaElement && messageElement) {
        messageAreaElement.appendChild(messageElement);
        messageAreaElement.scrollTop = messageAreaElement.scrollHeight;
    }
}
</script>
    
<style scoped src="../assets/chatroom.css"></style>