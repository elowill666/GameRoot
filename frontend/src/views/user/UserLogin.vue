<template>
  <div class="back">
    <div style="margin: 20px; display: flex; gap: 10px">
      <el-button round @click="adminAutoLogin(ruleFormRef)" size="large"
        style="width: 10%; margin-top: 10px">管理者自動登入</el-button>
      <el-button round @click="tomLogin()" size="large" style="width: 10%; margin-top: 10px">Tom</el-button>
      <el-button round @click="mayLogin()" size="large" style="width: 10%; margin-top: 10px">May</el-button>
      <el-button round @click="sandyLogin()" size="large" style="width: 10%; margin-top: 10px">Sandy</el-button>
      <el-button round @click="andyLogin()" size="large" style="width: 10%; margin-top: 10px">Andy</el-button>
      <el-button round @click="devinLogin()" size="large" style="width: 10%; margin-top: 10px">Devin</el-button>
      <el-button round @click="testMinLogin()" size="large" style="width: 10%; margin-top: 10px">測試小明</el-button>
    </div>

    <div class="backcontainer animate__animated animate__fadeInRight">
      <h2>會員登入</h2>
      <div class="login-container">
        <el-form :model="loginForm" :rules="rules" ref="ruleFormRef" label-position="left" class="login-form">
          <el-form-item label="帳號：" prop="username" style="margin: 40px">
            <el-input :prefix-icon="User" v-model="loginForm.username" :input-style="{ width: '200px' }" />
          </el-form-item>
          <el-form-item label="密碼：" prop="password" style="margin: 40px; margin-bottom: 15px">
            <el-input :prefix-icon="Lock" v-model="loginForm.password" :input-style="{ width: '200px' }" type="password"
              show-password />
          </el-form-item>
          <div style="
              color: #fafafadd;
              display: flex;
              justify-content: end;
              font-size: 14px;
            ">
            看不清楚？點擊照片換圖
          </div>
          <el-form-item label="驗證碼 :" prop="code">
            <div style="display: flex">
              <el-input :prefix-icon="CircleCheck" placeholder="請輸入驗證碼" v-model="loginForm.code"
                :input-style="{ flex: 1 }"><el-icon>
                  <CircleCheckFilled />
                </el-icon></el-input>
              <div style="flex: 1; margin-left: 10px">
                <canvas ref="canvas" :width="state.contentWidth" :height="state.contentHeight"
                  @click="changeCanvasVal"></canvas>
              </div>
            </div>
          </el-form-item>

          <el-form-item>
            <el-button round @click="submitForm(ruleFormRef)" size="large"
              style="width: 70%; margin: auto; margin-top: 10px">登入</el-button>
          </el-form-item>
          <!-- <el-form-item>
            <el-button round @click="autologin()" size="large"
              style="width: 70%; margin: auto; margin-top: 10px">懶得打驗證碼的按這個</el-button>
          </el-form-item> -->

          <el-form-item>
            <el-button round @click="google()" size="large" style="
                width: 70%;
                margin: auto;
                margin-top: 10px;
                margin-bottom: 20px;
              ">使用Google登入</el-button>
          </el-form-item>
          <div style="display: flex; color: #fafafadd">
            <div>
              還沒有帳號？請
              <span style="
                  color: #959595dd;
                  cursor: pointer;
                  flex: 1;
                  font-size: 18px;
                " @click="signup">註冊</span>
            </div>
            <div style="flex: 1; text-align: right; cursor: pointer" @click="gochange">
              忘記密碼?
            </div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, watch } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";
import { useRoute } from "vue-router";
import Swal from "sweetalert2";
import { useLoginStore } from "../../store/navbar";
import { useChatStore } from "../../store/chatroomOnetoOne";
import { User } from "@element-plus/icons-vue";
import { Lock } from "@element-plus/icons-vue";
import { CircleCheck } from "@element-plus/icons-vue";
import { storeToRefs } from "pinia";

import { forShortSweetalert } from "@/utils/someSweetalert";
//屬性
const url = import.meta.env.VITE_JAVA_URL;
const canvas = ref();
const router = useRouter();
const route = useRoute();
const loginStore = useLoginStore();
const chatStore = useChatStore();
const { userDetail, isgoogleLogin } = storeToRefs(loginStore);
const { loginuserId } = storeToRefs(chatStore);
function signup() {
  router.push("/user/usersignup");
}

function gochange() {
  router.push("/user/userEmailCheck");
}

//表單驗證 密碼
const validatePass = function validatePass(rule, value, callback) {
  console.log(loginForm.value);
  if (value === "") {
    callback(new Error("驗證碼不得為空"));
  } else {
    if (loginForm.password !== "") {
      if (!loginForm.value || value != state.identifyCode) {
        callback(new Error("驗證碼錯誤"));
      }
    }
    callback();
  }
};
//表單所需資料
const loginForm = ref({
  username: "",
  password: "",
  code: "",
});
//驗證規則
let ruleFormRef = ref();
const rules = reactive({
  username: [{ required: true, message: "請輸入帳號", trigger: "blur" }],
  password: [{ required: true, message: "請輸入密碼", trigger: "blur" }],
  code: [{ validator: validatePass, trigger: "blur" }],
});

//表單驗證
const submitForm = (formEl) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      login();
    } else {
      console.log("error submit!");
      return false;
    }
  });
};

//驗證碼對象資料
const state = reactive({
  contentWidth: 210,
  contentHeight: 40,
  valicodes: "abcdefghijklmnopqrstuvwxyz1234567890",
  identifyCode: "",
  count: 4,
});

//方法
function google() {
  // window.location.href = `http://localhost:8080/google-login`;
  window.location.href = `${url}/google-login`;
}

function autologin() {
  loginForm.value.code = state.identifyCode;
}
async function login() {
  let data = {
    username: loginForm.value.username,
    password: loginForm.value.password,
  };
  const axiosObj = axios.create({
    baseURL: url,
    timeout: 5000,
    withCredentials: true,
  });
  const rsp = await axiosObj.post("/user/login", data);
  if (rsp.data.message == "進行信箱驗證") {
    router.push("/user/firstLoginAuthentication");
  } else if (
    rsp.data.message == "登入成功" ||
    rsp.data.message == "管理員登入"
  ) {
    localStorage.setItem("userId", loginForm.value.username);
    loginStore.isLogin = true;
    if (rsp.data.message == "管理員登入") {
      loginStore.isAdmin = true;
      loginStore.isLogin = false;
    }
    const res = await axios.get(`${url}/user/profile`, {
      withCredentials: true,
    });
    userDetail.value = res.data;
    loginuserId.value = res.data.id;

    // location.href = "http://localhost:5173/home";
    router.push("/home");
  } else {
    if (rsp.data.ban == "ban") {
      forShortSweetalert("error", rsp.data.message, 600, 2000);
      return;
    }
    Swal.fire({
      html: `<h4 style="display: flex; justify-content: center; align-items: center; height: 100%; font-family= "Arial", sans-serif;">${rsp.data.message}</h4>`,
      icon: "error",
      showConfirmButton: false,
      width: 300,
      timer: 1200,
      toast: true,

      showClass: {
        popup: `
          animate__animated
          animate__fadeInUp
          animate__faster
          `,
      },
      hideClass: {
        popup: `
          animate__animated
          animate__fadeOutDown
          animate__faster
          `,
      },
    });
  }
}

onMounted(async () => {
  state.identifyCode = draw();
  console.log(state.identifyCode);
  if (route.params.id) {
    const res = await axios.get(`${url}/user/getuser/${route.params.id}`, {
      withCredentials: true,
    });
    userDetail.value = res.data;
    localStorage.setItem("userId", res.data.username);
    loginStore.isLogin = true;
    isgoogleLogin.value = true;
    router.push("/home");
  }
});
watch(
  () => {
    return state.identifyCode;
  },
  (newval, oldval) => {
    console.log(newval);
  }
);
//隨機數
const randomNum = (min, max) => {
  return Math.floor(Math.random() * (max - min) + min);
};
//隨機顏色
const randomColor = (min, max) => {
  let r = randomNum(min, max);
  let g = randomNum(min, max);
  let b = randomNum(min, max);
  return `rgb(${r},${g},${b})`;
};
//干扰线
const drawLine = (ctx) => {
  for (let i = 0; i < 5; i++) {
    //5条线
    ctx.strokeStyle = randomColor(40, 180);
    ctx.beginPath();
    ctx.moveTo(
      randomNum(0, state.contentWidth),
      randomNum(0, state.contentHeight)
    );
    ctx.lineTo(
      randomNum(0, state.contentWidth),
      randomNum(0, state.contentHeight)
    );
    ctx.stroke();
  }
};
//绘制点
const drawDot = (ctx) => {
  for (let i = 0; i < 20; i++) {
    //20个点
    ctx.fillStyle = randomColor(0, 255);
    ctx.beginPath();
    ctx.arc(
      randomNum(0, state.contentWidth),
      randomNum(0, state.contentHeight),
      1,
      0,
      2 * Math.PI
    );
    ctx.fill();
  }
};
//开始绘制
const draw = () => {
  const ctx = canvas.value.getContext("2d");
  //绘制背景
  ctx.fillStyle = randomColor(220, 255);
  ctx.fillRect(0, 0, state.contentWidth, state.contentHeight);
  // 绘制文字
  let identifyCode = "";

  for (let i = 0; i < state.count; i++) {
    //控制字数
    // drawText(ctx, state.identifyCode[i], i);
    const text = state.valicodes[randomNum(0, state.valicodes.length - 1)];
    identifyCode += text;
    // 字体随机的旋转角度
    var deg = randomNum(-10, 10);
    ctx.font = randomNum(18, 40) + "px Arial"; //字体大小
    ctx.fillStyle = randomColor(50, 160); //字体颜色

    ctx.textBaseline = "middle";

    ctx.save();
    //文字依次随机的上下左右
    let x = (state.contentWidth / state.count) * i + 10;
    let y = randomNum(
      -state.contentHeight / state.count,
      state.contentHeight / state.count
    );
    ctx.translate(x, y);
    ctx.rotate((deg * Math.PI) / 180); //文字旋转角度
    ctx.fillText(text, 0, state.contentHeight / 2);
    ctx.restore();
  }
  //绘制线条
  drawLine(ctx);
  //绘制点
  drawDot(ctx);
  return identifyCode;
};
const changeCanvasVal = () => {
  state.identifyCode = draw();
};

function adminAutoLogin(formEl) {
  (loginForm.value.username = "admin123"),
    (loginForm.value.password = "admin123"),
    autologin();
  submitForm(formEl);
}

function tomLogin(formEl) {
  loginForm.value.username = "Tom123";
  loginForm.value.password = "a12345";
  autologin();
}
function mayLogin(formEl) {
  loginForm.value.username = "May123";
  loginForm.value.password = "b12345";
  autologin();
}
function sandyLogin(formEl) {
  loginForm.value.username = "sandy123";
  loginForm.value.password = "c12345";
  autologin();
}
function andyLogin(formEl) {
  loginForm.value.username = "Andy123";
  loginForm.value.password = "d12345";
  autologin();
}
function devinLogin(formEl) {
  loginForm.value.username = "Devin123";
  loginForm.value.password = "i12345";
  autologin();
}
function testMinLogin(formEl) {
  loginForm.value.username = "min";
  loginForm.value.password = "test123456";
  autologin();
}
</script>

<style scoped>
.back {
  background: url("../../../public/images/loginImg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.494);
  background-blend-mode: multiply;
}

button {
  --el-button-bg-color: #4a7092;
  border: none;
  color: aliceblue;
  box-shadow: 0px 0px 10px rgba(255, 255, 255, 0.2);
}

.el-input {
  height: 40px;
}

:deep(.el-form-item__label) {
  color: #fafafadd;
}

h2 {
  font-size: 35px;
  color: #fafafadd;
  text-shadow: 5px 5px 10px rgba(0, 0, 0, 0.3);
}

.input_box {
  width: 300px;
  margin-right: 15px;
  border-radius: 95px;
  background: rgb(0 234 245 / 48%);
}

:deep(.el-input__wrapper) {
  background-color: #ffffff;
  border-radius: 95px;
  border: 0;
  box-shadow: 0px 0px 10px rgba(255, 255, 255, 0.5);
}

.backcontainer {
  margin: auto;
  height: 650px;
  width: 600px;
  background-color: #141f2ae4;
  border-radius: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0px 0px 40px rgba(255, 255, 255, 0.2);
  margin-bottom: 170px;
}
</style>
