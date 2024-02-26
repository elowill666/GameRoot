import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from "axios";
export const useChatStore = defineStore('ChatOneToOne', () => {

    let connectedUsers = ref({})
    const url = import.meta.env.VITE_JAVA_URL
    const loginuserId = ref(null)
    const axiosObj = axios.create({
        timeout: 15000,
        withCredentials: true
    });
    async function getloginuser() {
        const userId = localStorage.getItem('userId');
        if (userId == null) {
            const res = await axios.get(`${url}/user/profile`, {
                withCredentials: true,
            });
            loginuserId.value = res.data.id;
        } else {
            const res = await axiosObj.get(`${url}/user/getuser/${userId}`)
            loginuserId.value = res.data.id
        }

    }
    const connectedUsersResponse = async () => {
        await getloginuser()
        const res = await axiosObj.get(`${url}/user/getAllFriend/${loginuserId.value}`);
        connectedUsers.value = res.data;
        console.log(`output->`, 'piniaaaa')
    }
    return {
        connectedUsers,
        loginuserId,
        connectedUsersResponse
    }

})