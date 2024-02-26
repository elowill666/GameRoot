<template>
  <div class="layout">
    <div class="backcontainer animate__animated animate__fadeInRight">
      <h2>會員註冊</h2>

      <el-form :model="form" ref="ruleFormRef" :rules="rules" label-width="120px">
        <el-form-item label="帳號：" prop="username" style="width: 450px">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密碼：" prop="password" style="width: 450px">
          <el-input v-model="form.password" />
        </el-form-item>
        <el-form-item label="Email：" prop="email" style="width: 450px">
          <el-input v-model="form.email" />
        </el-form-item>

        <el-form-item label="手機：" prop="phone_number" style="width: 450px">
          <el-input v-model="form.phone_number" />
        </el-form-item>

        <el-form-item label="性別：" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio label="男" />
            <el-radio label="女" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="生日：" prop="birth" style="width: 800px">
          <el-col :span="11">
            <el-date-picker v-model="form.birth" type="date" placeholder="Pick a date" style="width: 100%" />
          </el-col>
        </el-form-item>

        <el-form-item label="地址：" prop="address" style="width: 450px">
          <el-input v-model="form.address" />
        </el-form-item>

        <div class="mybutton">
          <el-button round size="large" @click="submitForm(ruleFormRef)">送出</el-button>
          <el-button round size="large" @click="autoSign()">一鍵填入</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";
import Swal from "sweetalert2";
const url = import.meta.env.VITE_JAVA_URL;
// do not use same name with ref
const form = reactive({
  username: "",
  password: "",
  gender: "",
  birth: "",
  phone_number: "",
  email: "",
  address: "",
});
const router = useRouter();
//自動填入
function autoSign() {
  // form.username = "test100";
  form.password = "test12345";
  form.gender = "男";
  form.birth = "2000-07-13";
  form.phone_number = "093284552";
  form.email = "test123@gmail.com";
  form.address = "臺中市沙鹿區六路二街21號";
}

//表單驗證 密碼
const validateUsername = (rule, value, callback) => {
  if (/\d/.test(value) && /[A-Za-z]/.test(value) && /^.{6,}$/.test(value))
    return callback();
  callback(new Error("須為長度大於6，且只能包含英文及數字"));
};

const validatePass = (rule, value, callback) => {
  if (/\d/.test(value) && /[A-Za-z]/.test(value) && /^.{6,}$/.test(value))
    return callback();
  callback(new Error("須為長度大於6，且只能包含英文及數字"));
};

//表單驗證 信箱
const validateEmail = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("電子郵件不得為空"));
  } else {
    const check = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    if (!check.test(value)) {
      callback(new Error("請輸入有效的電子郵件地址"));
    } else {
      callback();
    }
  }
};

let ruleFormRef = ref();
const rules = reactive({
  username: [{ required: true, validator: validateUsername, trigger: "blur" }],
  password: [{ required: true, validator: validatePass, trigger: "blur" }],
  gender: [{ required: true, message: "性別為必填", trigger: "blur" }],
  birth: [{ required: true, message: "請選擇生日", trigger: "blur" }],
  phone_number: [
    { required: true, message: "聯絡電話為必填", trigger: "blur" },
  ],
  email: [{ required: true, validator: validateEmail, trigger: "blur" }],
  address: [{ required: true, message: "請輸入地址", trigger: "blur" }],
});

//表單驗證及發送新增請求
const submitForm = (formEl) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      console.log("submit!");
      signUp();
      // router.push('/home')
    } else {
      console.log("error submit!");
      return false;
    }
  });
};

function signUp() {
  let data = {
    username: form.username,
    password: form.password,
    gender: form.gender,
    birth: form.birth,
    phone_number: form.phone_number,
    email: form.email,
    address: form.address,
  };
  const axiosObj = axios.create({
    baseURL: url,
    timeout: 5000,
    withCredentials: true,
  });
  axiosObj
    .post("/user/insert", data)
    .then((rsp) => {
      console.log(rsp.data);
      if (rsp.data == "新增成功") {
        Swal.fire({
          html: `<h4 style="display: flex; justify-content: center; align-items: center; height: 100%; font-family= "Arial", sans-serif;">註冊成功 請重新登入</h4>`,
          icon: "success",
          showConfirmButton: false,
          width: 450,
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
        }).then(() => {
          router.push("/user/userlogin")
        })
      } else {
        Swal.fire({
          html: `<h4 style="display: flex; justify-content: center; align-items: center; height: 100%; font-family= "Arial", sans-serif;">帳號已被使用</h4>`,
          icon: "error",
          showConfirmButton: false,
          width: 350,
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
      }
    })
    .catch((err) => {
      console.log(err);
    });
}
</script>

<style scoped>
.layout {
  background: url("../../../public/images/loginImg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}

:deep(.el-form-item__label) {
  color: #fafafadd;
}

h2 {
  font-size: 35px;
  color: #fafafadd;
  margin-bottom: 25px;
}

.backcontainer {
  margin: auto;
  margin-top: 100px;
  margin-bottom: 150px;
  height: 690px;
  width: 620px;
  background-color: #141f2ae4;
  border-radius: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0px 0px 40px rgba(255, 255, 255, 0.2);
}

.el-form-item {
  margin-bottom: 30px;
}

:deep(.el-input__wrapper) {
  background-color: #fafafadd;
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
