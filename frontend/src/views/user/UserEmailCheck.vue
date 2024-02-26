<template>
    <div class="layout">
        <div class="back">
            <div class="backcontainer animate__animated animate__fadeInRight">
                <h2>信箱驗證</h2>
                <el-form :model="form" ref="ruleFormRef" :rules="rules" label-width="120px">

                    <el-form-item label="帳號：" prop="username" style="width: 450px;">
                        <el-input :prefix-icon="User" v-model="form.username" />
                    </el-form-item>

                    <el-form-item label="Email：" prop="email" style="width: 450px;">
                        <el-input :prefix-icon="Message" v-model="form.email" />
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
import { User } from "@element-plus/icons-vue";
import { Message } from "@element-plus/icons-vue";
//屬性
const url = import.meta.env.VITE_JAVA_URL
const router = useRouter();
let ruleFormRef = ref();
const form = reactive({
    username: '',
    email: '',
});


onMounted(() => {
    axiosObj.get(`${url}/user/profile`)
        .then(res => {
            form.username = res.data.username
        }).catch(err => {
            console.log(err);
        })
})


//表單驗證 信箱
const validateEmail = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('電子郵件不得為空'));
    } else {
        const check = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
        if (!check.test(value)) {
            callback(new Error('請輸入有效的電子郵件地址'));
        } else {
            callback();
        }
    }
};

const rules = reactive({
    username: [{ required: true, message: '帳號不得為空', trigger: 'blur' }],
    email: [{ required: true, validator: validateEmail, trigger: 'blur' }],
});

//表單驗證及發送新增請求
const submitForm = (formEl) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            console.log('submit!')
            send();
            // router.push('/home')
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
    axiosObj.get(`${url}/user/getuser/${form.username}`)
        .then(res => {
            if (res.data == '使用者不存在') {
                Swal.fire({
                    title: 'Error!',
                    text: res.data,
                    icon: 'error',
                    confirmButtonText: '確定'
                });
            } else {
                Swal.fire({
                    title: 'loading...',
                    allowOutsideClick: false,
                    showConfirmButton: false
                });
                axiosObj.post(`${url}/email/send`, { email: form.email })
                    .then(res => {
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
                            }).then((result) => {
                                if (result.isConfirmed) {
                                    router.push("/user/userlogin")
                                }
                            });
                        }
                    }).catch(err => {
                        console.log(err);
                    });
            }
        })
        .catch(err => {
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