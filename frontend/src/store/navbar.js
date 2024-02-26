import { defineStore } from 'pinia'
import { ref } from "vue";
export const useLoginStore = defineStore('NavBar', {
    //存數據的地方
    state() {
        return {
            isLogin: false,
            isAdmin: false,
            showOneToOne: false,
            isgoogleLogin: false,
            userDetail: ref({})
        }
    }
})