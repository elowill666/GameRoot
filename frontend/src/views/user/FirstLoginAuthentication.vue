<template>
    <div class="layout">
        <div class="back">
            <div class="backcontainer animate__animated animate__fadeInRight">
                <h3>首次登入請進行信箱驗證以開通帳號</h3>
                <el-form :model="form" ref="ruleFormRef" :rules="rules" label-width="120px">
                    <el-form-item label="Email：" prop="email" style="width: 450px;">
                        <el-input :prefix-icon="Message" v-model="form.email" />
                    </el-form-item>
                    <el-form-item>
                        <el-button round size="large" @click="submitForm1(ruleFormRef)">取得驗證碼</el-button>
                    </el-form-item>
                </el-form>
                <el-form :model="form2" ref="ruleFormRef2" :rules="rules2" label-width="120px">
                    <el-form-item label="驗證碼：" prop="code" style="width: 450px;">
                        <el-input :prefix-icon="Message" v-model="form2.code" />
                    </el-form-item>
                    <el-form-item>
                        <el-button round size="large" @click="submitForm2(ruleFormRef2)">確定</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>
    
<script setup>
import { computed, reactive, ref } from 'vue'
import axios from 'axios'
import { useRouter } from "vue-router"
import Swal from 'sweetalert2'
import { Message } from "@element-plus/icons-vue";
import { storeToRefs } from "pinia";
import { useLoginStore } from "../../store/navbar";
import { useChatStore } from "../../store/chatroomOnetoOne";
//屬性
const loginStore = useLoginStore();
const chatStore = useChatStore();
const { userDetail } = storeToRefs(loginStore);
const url = import.meta.env.VITE_JAVA_URL
const router = useRouter();
let ruleFormRef = ref();
let ruleFormRef2 = ref();
const form = reactive({
    email: '',
});
const form2 = reactive({
    code: '',
});


//表單驗證 信箱
const validateEmail = (rule, value, callback) => {
    const check = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    if (!check.test(value)) {
        callback(new Error('請輸入有效的電子郵件地址'));
    } else {
        callback();
    }
};
const validateCode = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('驗證碼不得為空'));
    } else {
        if (value != generateIdentifyCode.value) {
            callback(new Error('驗證碼錯誤'));
        } else {
            callback();
        }
    }
};

const rules = reactive({
    email: [{ required: false, validator: validateEmail, trigger: 'submit' }],
});
const rules2 = reactive({
    code: [{ required: true, validator: validateCode, trigger: 'submit' }],
});
const state = reactive({
    contentWidth: 210,
    contentHeight: 40,
    valicodes: "abcdefghijklmnopqrstuvwxyz1234567890",
    identifyCode: '',
    count: 5,
});
const randomNum = (min, max) => {
    return Math.floor(Math.random() * (max - min) + min);
};
const generateIdentifyCode = computed(() => {
    let code = '';
    for (let i = 0; i < state.count; i++) {
        const text = state.valicodes[randomNum(0, state.valicodes.length - 1)];
        code += text;
    }
    return code;
});
console.log(`output->generateIdentifyCode`, generateIdentifyCode.value)

//表單驗證及發送新增請求
const submitForm1 = (formEl) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            console.log('submit!')
            send();
        } else {
            console.log('error submit!')
            return false
        }
    })
};
const submitForm2 = async (formEl) => {
    if (!formEl) return
    formEl.validate(async (valid) => {
        if (valid) {
            loginStore.isLogin = true;
            const res = await axios.get(`${url}/user/emailAuthenticate`, {
                withCredentials: true,
            });
            localStorage.setItem("userId", res.data.username);
            userDetail.value = res.data;
            router.push('/home')
        } else {
            console.log('error submit!')
            return false
        }
    })
};
//方法
const axiosObj = axios.create({
    timeout: 10000,
    withCredentials: true
});
function send() {
    Swal.fire({
        title: 'loading...',
        allowOutsideClick: false,
        showConfirmButton: false
    });
    axiosObj.post(`${url}/email/loginAuthentication`, { email: form.email, code: generateIdentifyCode.value })
        .then(res => {
            console.log(res.data);

            if (res.data == '郵件已成功發送') {
                Swal.fire({
                    html: `<h4 style="display: flex; justify-content: center; align-items: center; height: 100%; font-family= "Arial", sans-serif;">郵件已成功發送 請至信箱確認</h4>`,
                    icon: "success",
                    showConfirmButton: false,
                    width: 600,
                    timer: 1200,
                    toast: true,

                    showClass: {
                        popup: `
                                        animate__animated
                                        animate__fadeInUp
                                        animate__faster
                                        `
                    },
                    hideClass: {
                        popup: `
                                        animate__animated
                                        animate__fadeOutDown
                                        animate__faster
                                        `
                    }
                })
            } else {
                Swal.fire({
                    title: '信箱不存在',
                    icon: 'error',
                    confirmButtonText: '確定'
                });
            }
        }).catch(err => {
            console.log(err);
        });
}



</script>
    
<style scoped>
.layout {
    background: url('../../../public/images/loginImg.jpg') center/cover fixed;
    margin: 0;
    overflow: hidden;
    background-color: rgba(0, 0, 0, 0.6);
    background-blend-mode: multiply;
}

.back {
    height: 100vh;
}


h2 {
    font-size: 40px;
    color: #fafafadd;
    margin-bottom: 25px;
    margin-top: -20px;
}

:deep(.el-form-item__label) {
    color: #fafafadd;
}

.backcontainer {
    margin: auto;
    margin-top: 100px;
    height: 400px;
    width: 600px;
    background-color: #141f2ae4;
    border-radius: 12px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    box-shadow: 0px 0px 40px rgba(255, 255, 255, 0.2);
}

.el-form-item {
    margin-bottom: 20px;
}

.el-input {
    height: 35px;
    width: 300px;
}


:deep(.el-input__wrapper) {
    background-color: #ffffff;
    border-radius: 95px;
    border: 0;
    box-shadow: 0px 0px 10px rgba(255, 255, 255, 0.5);
}


button {
    --el-button-bg-color: #4a7092;
    border: none;
    color: aliceblue;
    box-shadow: 0px 0px 10px rgba(255, 255, 255, 0.2);
}

.mybutton {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: auto;

}

.el-form {
    width: 100%;
    max-width: 530px;
    margin-top: 20px;

}
</style>