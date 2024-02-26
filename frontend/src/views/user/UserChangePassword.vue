<template>
    <div class="layout">
        <div class="d1">
            <div class="backcontainer animate__animated animate__fadeInRight">
                <h2>重置密碼</h2>
                <el-form :model="form" ref="ruleFormRef" :rules="rules" label-width="120px">

                    <el-form-item label="帳號：" prop="username" style="width: 450px;">
                        <el-input :prefix-icon="User" v-model="form.username" />
                    </el-form-item>

                    <el-form-item label="新密碼：" prop="password" style="width: 450px;">
                        <el-input :prefix-icon="Lock" v-model="form.password" type="password" show-password />
                    </el-form-item>

                    <el-form-item label="確認密碼：" prop="password2" style="width: 450px;">
                        <el-input :prefix-icon="Lock" v-model="form.password2" type="password" show-password />
                    </el-form-item>
                    <div class="mybutton">
                        <el-button round size="large" @click="submitForm(ruleFormRef)">送出</el-button>
                    </div>

                </el-form>
            </div>
        </div>
    </div>
</template>
    
<script setup>
import { reactive, ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from "vue-router"
import Swal from 'sweetalert2'
import { User } from '@element-plus/icons-vue'
import { Lock } from '@element-plus/icons-vue'
import { useLoginStore } from "../../store/navbar";
import { useChatStore } from "../../store/chatroomOnetoOne";
const loginStore = useLoginStore();
//屬性
const vuebaseUrl = window.location.origin;
const url = import.meta.env.VITE_JAVA_URL
const router = useRouter();
let ruleFormRef = ref();
const form = reactive({
    username: '',
    password: '',
    password2: '',
});
const axiosObj = axios.create({
    timeout: 5000,
    withCredentials: true
});

async function gochangepassword() {
    await axiosObj.get(`${url}/user/getuser/${form.username}`)
        .then(res => {
            if (res.data == '使用者不存在') {
                Swal.fire({
                    title: 'Error!',
                    text: res.data,
                    icon: 'error',
                    confirmButtonText: '確定'
                });
            } else {
                //發送改密碼請求
                let data = {
                    username: form.username,
                    newpassword: form.password
                }
                axiosObj.post(`${url}/user/changepassword`, data)
                    .then(res => {
                        if (res.data == '密碼更新成功') {
                            Logout();
                        }
                    })
            }
        })
}
const Logout = async () => {
    try {
        const { data } = await axios.get(`${url}/user/logout`, {
            withCredentials: true,
        });
        if (data == true) {
            Swal.fire({

                // text: '已登出',
                html: '<h4 style="display: flex; justify-content: center; align-items: center; height: 100%; font-family= "Arial", sans-serif;">已重置密碼 請重新登入</h4>',
                icon: 'success',
                showConfirmButton: false,
                width: 700,
                timer: 1000,
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
            }).then(() => {
                // router.push('/home')
                loginStore.isLogin = false;
                loginStore.isAdmin = false;
                loginStore.showOneToOne = false;
                localStorage.removeItem("userId");
                location.href = `${vuebaseUrl}/user/userlogin`;
            });
        }
    } catch (error) {
        console.error("Logout failed", error);
    }
};
onMounted(() => {
    axiosObj.get(`${url}/user/profile`)
        .then(res => {
            console.log("res.data = ", res.data);
            form.username = res.data.username

        }).catch(err => {
            console.log(err);
        })
})


const validatePass = (rule, value, callback) => {
    if (/\d/.test(value) && /[A-Za-z]/.test(value) && /^.{6,}$/.test(value)) return callback();
    callback(new Error("須為長度大於6，且只能包含英文及數字"));
};

const validatePass2 = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('密碼不得為空'));
    } else {
        if (value != form.password) {
            callback(new Error('必須與上方密碼相同'));
        } else {
            callback();
        }
    }
};

const rules = reactive({
    username: [{ required: true, message: '帳號不得為空', trigger: 'blur' }],
    password: [{ required: true, validator: validatePass, trigger: 'blur' }],
    password2: [{ required: true, validator: validatePass2, trigger: 'blur' }],
});

//表單驗證及發送新增請求
const submitForm = (formEl) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            console.log('submit!')
            gochangepassword();
            // router.push('/home')
        } else {
            console.log('error submit!')
            return false
        }
    })
};





</script>
    
<style scoped>
.layout {
    background: url('../../../public/images/loginImg.jpg') center/cover fixed;
    margin: 0;
    overflow: hidden;
    background-color: rgba(0, 0, 0, 0.6);
    background-blend-mode: multiply;
}

.d1 {
    height: 40em;
    margin-top: 100px;

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
    height: 400px;
    width: 500px;
    background-color: #141f2ae4;
    border-radius: 12px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    box-shadow: 0px 0px 40px rgba(255, 255, 255, 0.2);
}

.el-form-item {
    margin-bottom: 30px;
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