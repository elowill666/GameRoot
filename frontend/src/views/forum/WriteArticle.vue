<template>
    <!-- 撰寫文章按鈕 -->
    <button class="btn btn-primary btn-lg fixed-button btn-warning" :style="buttonStyle" @click="publishArticle">
        <i class="bi bi-pencil-square"></i>
    </button>
</template>
    
<script setup>
import { onMounted, ref, defineProps, reactive, watchEffect } from "vue";
import axios from "axios";
import Swal from "sweetalert2";
import router from "@/router";

const publishArticle = () => {

    const API_URL = `${import.meta.env.VITE_JAVA_URL}`;
    axios
        .get(`${API_URL}/user/authenticate`, { withCredentials: true })
        .then((res) => {
            if (res.data == false) {
                Swal.fire({
                    title: "尚未登入",
                    text: "是否要立即登入？",
                    icon: "warning",
                    confirmButtonText: "登入",
                    showCancelButton: true,
                    cancelButtonText: "暫時不要",
                }).then((result) => {
                    if (result.isConfirmed) {
                        router.push("/user/userlogin");
                        return;
                    }
                });
            } else {
                router.push({ name: 'articlewrite' });
            }
        })
        .catch((error) => {
            console.error(error);
        });

}

const buttonStyle = {
    position: 'fixed',
    bottom: '20px',
    left: '20px',
};
</script>
    
<style scoped>
.fixed-button {
    margin-left: 10px;
    width: 70px;
    height: 70px;
    font-size: 20px;
}
</style>