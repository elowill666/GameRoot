<template>
    <div class="layout">
        <div class="container animate__animated animate__fadeInRight">
            <div id="logo" class="animate__backInUp">
                <h1 class="logo"> <img src="../../../public/images/logo.png" style="width: 80px;height: 80px;" /></h1>
            </div>
            <!-- 上傳照片 -->
            <el-upload class="avatar-uploader" :action="`${url}/user/uploadphoto/${userDetail.username}`"
                :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload"
                :auto-upload="true">
                <span style="margin: 40px 0 0 10px;">
                    <el-icon size="35px">
                        <Edit />
                    </el-icon>
                </span>

                <img v-if="imageUrl" :src="imageUrl || defaultUser" class="avatar" style="position: absolute;object-fit:cover;
z-index: 1000;margin: 35% 0 0 29.5%;" />
                <img v-else :src="defaultUser" class="avatar" style="position: absolute;object-fit:cover;
z-index: 1000;margin: 35% 0 0 29.5%;" />
            </el-upload>


            <div class="leftbox">

            </div>
            <div class="rightbox" v-show="!isedit">
                <div class="profile animate__animated animate__fadeInRight">
                    <h1>會員檔案</h1>
                    <div style="display: flex;align-items: center;">
                        <div style="width: 50%;">
                            <h2>帳號</h2>
                            <p>{{ userDetail.username }}</p>
                        </div>
                        <div style="width: 50%;">
                            <h2>會員等級</h2>
                            <p>{{ userDetail.membership }}</p>
                        </div>
                    </div>

                    <div style="display: flex;align-items: center;">
                        <div style="width: 50%;">
                            <h2>手機</h2>
                            <p>{{ userDetail.phone_number }}</p>
                        </div>
                        <div style="width: 50%;">
                            <h2>根節點</h2>
                            <p>{{ userDetail.cash }}</p>
                        </div>
                    </div>

                    <h2>性別</h2>
                    <p>{{ userDetail.gender }}</p>
                    <h2>Email</h2>
                    <p>{{ userDetail.email }}</p>
                    <h2>地址</h2>
                    <p>{{ userDetail.address }}</p>
                    <el-button round @click="goedit()" size="large" style="width: 80px;margin:20px 0 15px">
                        <el-icon size="20px">
                            <EditPen />
                        </el-icon></el-button>
                </div>
            </div>

            <div class="rightbox" v-show="isedit">
                <div class="profile  animate__animated animate__fadeInRight">
                    <el-form :model="form" ref="ruleFormRef" :rules="rules" label-width="120px">
                        <h1>會員檔案</h1>
                        <p><el-form-item label="帳號：" prop="username" style="width: 450px;">
                                <el-input v-model="form.username" />
                            </el-form-item></p>
                        <p>
                            <el-form-item label="手機：" prop="phone_number" style="width: 450px;">
                                <el-input v-model="form.phone_number" />
                            </el-form-item>
                        </p>
                        <p>
                            <el-form-item label="性別：" prop="gender">
                                <el-radio-group v-model="form.gender">
                                    <el-radio label="男" />
                                    <el-radio label="女" />
                                </el-radio-group>
                            </el-form-item>
                        </p>
                        <p>
                            <el-form-item label="Email：" prop="email" style="width: 450px;">
                                <el-input v-model="form.email" />
                            </el-form-item>
                        </p>
                        <p>
                            <el-form-item label="地址：" prop="address" style="width: 450px;">
                                <el-input v-model="form.address" />
                            </el-form-item>
                        </p>


                        <el-button round @click="submitForm(ruleFormRef)" size="large"
                            style="width: 100px;margin:20px 0 15px">確定</el-button>
                    </el-form>
                </div>
            </div>
        </div>
    </div>
</template>
    
<script setup >
import { ref, reactive } from 'vue'
import { onBeforeMount, onMounted } from 'vue';
import axios from "axios";
import Swal from 'sweetalert2'
import { useRouter } from "vue-router"
import defaultUser from '../../assets/defaultUser.png'
import { useLoginStore } from "@/store/navbar";
import { storeToRefs } from "pinia";
const loginStore = useLoginStore();
const { userDetail } = storeToRefs(loginStore);
const imageUrl = ref(null)
const baseUrl = window.location.origin;
const router = useRouter()
const url = import.meta.env.VITE_JAVA_URL
// let userDetail = ref({});
let isedit = ref(false);
let isgoogleLogin = ref(false);
const axiosObj = axios.create({
    timeout: 5000,
    withCredentials: true
});
const form = ref({
    id: '',
    username: '',
    gender: '',
    phone_number: '',
    email: '',
    address: '',
})



const handleAvatarSuccess = async function (response, uploadFile) {
    imageUrl.value = URL.createObjectURL(await uploadFile.raw);
    userDetail.value.userphoto = URL.createObjectURL(await uploadFile.raw);
};

const beforeAvatarUpload = function (rawFile) {
    if ((rawFile.type !== 'image/jpeg') && (rawFile.type !== 'image/png')) {
        Swal.fire({
            title: '格式錯誤!',
            text: '檔案限制: .JPEG, .PNG',
            icon: 'error',
            confirmButtonText: '確定'
        })
        return false;
    } else if (rawFile.size / 1024 / 1024 > 1) {
        Swal.fire({
            title: '檔案過大!',
            text: '檔案大小:最大1MB',
            icon: 'error',
            confirmButtonText: '確定'
        })
        return false;
    }
    return true;
};

function goedit() {
    isedit.value = !isedit.value
}

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


let ruleFormRef = ref();
const rules = reactive({
    username: [{ required: true, message: '帳號不得為空', trigger: 'blur' }],
    gender: [{ required: false, message: '性別為必填', trigger: 'blur' }],
    phone_number: [{ required: false, message: '聯絡電話為必填', trigger: 'blur' }],
    email: [{ required: true, validator: validateEmail, trigger: 'blur' }],
    address: [{ required: false, message: '請輸入地址', trigger: 'blur' }],
})

//表單驗證及發送新增請求
const submitForm = (formEl) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            console.log('submit!')
            editprofile()
        } else {
            console.log('error submit!')
            return false
        }
    })
};

function editprofile() {
    let data = {
        id: form.value.id,
        username: form.value.username,
        gender: form.value.gender,
        phone_number: form.value.phone_number,
        email: form.value.email,
        address: form.value.address,
    }
    axiosObj.post(`${url}/user/updateProfile`, data)
        .then(res => {
            if (res.data == true) {
                Swal.fire({

                    text: '更新成功',
                    icon: 'success',
                    position: 'bottom-start',
                    showConfirmButton: false,
                    width: 250,
                    timer: 1500,
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
                }).then(async () => {
                    await getUserBylocalStorage();
                    isedit.value = !isedit.value
                    router.push('/user/userprofile')

                });


            } else {
                Swal.fire({
                    icon: 'error',
                    title: '更新失敗',
                    allowOutsideClick: false,
                    confirmButtonText: '確定'
                });
            }
        }).catch(err => {
            console.log(`output->err`, err)
        })
}


//從localStorage拿使用者名字去資料庫找新的資料
async function getUserBylocalStorage() {
    const userId = localStorage.getItem('userId');
    let res = await axiosObj.get(`${url}/user/getuser/${userId}`)
    userDetail.value = res.data
    imageUrl.value = res.data.userphoto
    form.value.id = res.data.id
    form.value.username = res.data.username
    form.value.gender = res.data.gender
    form.value.phone_number = res.data.phone_number
    form.value.email = res.data.email
    form.value.address = res.data.address
}
onMounted(() => {
    getUserBylocalStorage()
}
)

</script>
    
<style scoped lang="scss">
.layout {
    background: url('../../../public/images/loginImg.jpg') center/cover fixed;
    margin: 0;
    overflow: hidden;
    background-color: rgba(0, 0, 0, 0.6);
    background-blend-mode: multiply;
}

// 圖片上傳
.avatar-uploader .avatar {
    border-radius: 8px;
    width: 220px;
    height: 220px;
}

img {
    width: 220px;
    height: 220px;
}

@media (max-width: 1000px) {
    .avatar-uploader .avatar {
        width: 180px;
        height: 180px;
    }
}

@media (max-width: 766px) {
    .avatar-uploader .avatar {
        width: 125px;
        height: 125px;
        left: -25%;
    }
}

.avatar-uploader .el-upload {
    border: 1px dashed black;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
    border-color: #141f2a;
}

.el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
}

:deep(.el-input__wrapper) {
    background-color: rgb(250, 250, 250);
    border-radius: 95px;
    border: 0;
    box-shadow: 0px 0px 10px rgba(154, 154, 154, 0.5);
}


$gray: #777777;
$black: #070707;
$green: #637384;
$aqua: #3FB6A8;
$white: #FFFFFF;

@import url('https://fonts.googleapis.com/css?family=Nunito:400,900|Montserrat|Roboto');

$heading: 'Montserrat', sans-serif;
$body: 'Roboto', sans-serif;

.container {
    background-color: rgba(243, 249, 255, 0.866);
    width: 940px;
    height: 790px;
    margin: 0 auto;
    position: relative;
    margin-top: 2%;
    box-shadow: 2px 5px 20px 10px rgba(58, 58, 58, 0.8);
    border-radius: 12px;
}

.logo {
    float: right;
    margin-right: 12px;
    margin-top: 12px;
    letter-spacing: 1px;
}

.leftbox {
    float: left;
    margin-top: -105px;
    width: 15%;
    height: 835px;
    background: $green;
    box-shadow: 3px 3px 10px rgba($gray, .5);
    margin-left: 93px;
}

.rightbox {
    float: left;
    width: 60%;
    height: 100%;
}


.profile {
    position: absolute;
    width: 70%;
    font-size: 35px;
    padding-left: 100px;
}

h1 {
    font-family: $heading;
    color: $green;
    font-size: 1.2em;
    margin-top: 40px;
    margin-bottom: 35px;
}

h2 {
    color: $gray;
    font-family: $body;
    width: 80%;
    text-transform: uppercase;
    font-size: 15px;
    letter-spacing: 1px;
    margin-left: 2px;
}

p {
    border-width: 1px;
    border-style: solid;
    border-image: linear-gradient(to right, $aqua, rgba($green, .5)) 1 0%;
    border-top: 0;
    width: 80%;
    font-family: $heading;
    font-size: .7em;
    padding: 7px 0;
    color: $black;
}

span {
    font-size: .5em;
    color: $gray;
}
</style>