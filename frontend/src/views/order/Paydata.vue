<template >
    <div>
        <div class="login-box">
            <h2>訂單資料</h2>
            <form class="needs-validation" @submit.prevent="submitData">
                <div class="user-box has-validation">
                    <input type="text" class="form-control" id="validationCustom01" v-model="billingUsername" required>
                    <label for="validationCustom01" class="form-label">帳單姓名</label>
                </div>
                <div class="user-box">
                    <div class="city-selector-set">
                        <div class="my-style-selector">
                            <select id="billingCity" v-model="billingCity" class="county"></select>
                            <select v-model="billingArea" id="billingArea" class="district"></select>
                        </div>
                    </div>
                </div>
                <div class="user-box">
                    <input type="text" name="" id="billingAddress" v-model="billingAddress" required="">
                    <label>帳單地址</label>
                </div>
                <div>
                    <select v-model="paymentMethod" class="form-select" aria-label="Default select example">
                        <option selected>請選擇付款方式</option>
                        <option value="ECPAY">綠界pay</option>
                        <option v-if="rootnode > totalPrices" value="Rootnode">根節點{{ rootnode }}</option>
                        <option v-else disabled>根節點({{ rootnode }})餘額不足</option>
                    </select>
                </div>
                <div>
                    <h5>收件人資料</h5>
                </div>
                <div class="user-box">
                    <input type="text" v-model="consigneeUsername" required="">
                    <label>姓名</label>
                </div>
                <div class="user-box">
                    <input type="text" name="" v-model="consigneePhonenumber" required="">
                    <label>手機</label>
                </div>
                <div class="user-box">
                    <input type="text" name="" v-model="consigneeEmail" required="">
                    <label>Email</label>
                </div>
                <div class="user-box">
                    <div class="city-selector-set-has-value">
                        <div class="my-style-selector">
                            <select id="consigneeCity" v-model="consigneeCity" data-value="台南市" class="county"></select>
                            <select v-model="consigneeArea" id="consigneeArea" data-value="安平區" class="district"></select>
                        </div>
                    </div>
                </div>
                <div class="user-box">
                    <input type="text" name="" v-model="consigneeAddress" required="">
                    <label>帳單地址</label>
                </div>
                <div>此筆訂單總額 <span style="color: red;">NT${{ totalPrices }}</span>
                </div>
                <button type="button" @click="setDefaultValues">Demo</button>
                <button class="custom-button">
                    <span></span>
                    <span></span>
                    <span></span>
                    <span></span>送出訂單</button>
            </form>
        </div>
    </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from "vue-router"
import TwCitySelector from 'tw-city-selector';
import axios from 'axios';
import Swal from 'sweetalert2';
const router = useRouter()
new TwCitySelector({
    el: '.city-selector-set',
    elCounty: '.county', // 在 el 裡查找 element
    elDistrict: '.district', // 在 el 裡查找 element
    elZipcode: '.zipcode' // 在 el 裡查找 element
});
new TwCitySelector({
    el: '.city-selector-set-has-value',
    elCounty: '.county',
    elDistrict: '.district'
});

const consigneeCity = ref('')
const consigneeArea = ref('')
const billingArea = ref('')
const billingCity = ref('')
const consigneePhonenumber = ref('');
const billingUsername = ref('');
const billingAddress = ref('');
const paymentMethod = ref('請選擇付款方式');
const consigneeUsername = ref('');
const consigneeEmail = ref('');
const consigneeAddress = ref('')
function setDefaultValues() {
    billingUsername.value = '陳阿明';
    billingAddress.value = '信義路五段7號89樓';
    paymentMethod.value = 'ECPAY'
    consigneeUsername.value = '王小美'
    consigneeEmail.value = 'name@123.com.tw'
    consigneePhonenumber.value = '0988123456'
    consigneeAddress.value = '重慶南路一段122號'
}


function submitData() {
    Swal.fire({
        text: "Loading.......",
        allowOutsideClick: false,
        showConfirmButton: false
    });
    console.log(billingCity.value);
    if (billingCity.value === '') {
        const Toast = Swal.mixin({
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.onmouseenter = Swal.stopTimer;
                toast.onmouseleave = Swal.resumeTimer;
            }
        });
        Toast.fire({
            icon: "error",
            title: "請選擇帳單城市"
        });
        return;
    }
    if (billingArea.value === '') {
        const Toast = Swal.mixin({
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.onmouseenter = Swal.stopTimer;
                toast.onmouseleave = Swal.resumeTimer;
            }
        });
        Toast.fire({
            icon: "error",
            title: "請選擇帳單區域"
        });
        return;
    }
    const paymethod = paymentMethod.value;
    if (paymethod === '請選擇付款方式') {
        Swal.fire({
            icon: "error",
            text: "請選擇付款方式",
            allowOutsideClick: true,
            showCancelButton: true,
            confirmButtonText: "確定",
        })
        return;
    }
    const API_URL = `${import.meta.env.VITE_JAVA_URL}/order/insert`
    const request = {
        "billingUsername": billingUsername.value,
        "billingCity": billingCity.value,
        "billingArea": billingArea.value,
        "billingAddress": billingAddress.value,
        "consigneeUsername": consigneeUsername.value,
        "consigneePhonenumber": consigneePhonenumber.value,
        "consigneeCity": consigneeCity.value,
        "consigneeArea": consigneeArea.value,
        "consigneeEmail": consigneeEmail.value,
        "consigneeAddress": consigneeAddress.value,
        "paymentMethod": paymethod,
        "totalAmount": totalPrices.value,
        "couponId": couponId.value
    };
    axios.post(API_URL, request, { withCredentials: true }).then(function (response) {
        if (paymethod === 'ECPAY') {
            const div = document.createElement("div");
            div.innerHTML = response.data; // html code
            const newTab = window.open();
            newTab.document.body.appendChild(div);
            newTab.document.forms.allPayAPIForm.submit();
            setTimeout(() => {
                Swal.close();
                router.push('/home');
            }, 3000);
        } else {
            setTimeout(() => {
                Swal.fire({
                    icon: "success",
                    text: "購買成功查看訂單",
                    allowOutsideClick: false,
                    confirmButtonText: "確定",
                }).then(function (result) {
                    if (result.isConfirmed) {
                        window.location.reload()
                        window.location.href = '/order/orderDetails/' + userId.value
                    }
                })
            }, 2000);
        }

    }).catch()

}

let totalPrices = ref(0);
const couponId = ref(0);
const rootnode = ref(0);
const userId = ref(0);
let paydata = ref([]);
let x = 0;
let loadpaydata = async () => {
    const API_URL = `${import.meta.env.VITE_JAVA_URL}/shoppingcart/paydata`
    let response = await axios.get(API_URL, { withCredentials: true });
    Object.assign(paydata.value, response.data)
    console.log(paydata.value);
    totalPrices.value = paydata.value.alltotalprice;
    couponId.value = paydata.value.couponId
    rootnode.value = paydata.value.cash
    userId.value = paydata.value.userId
    console.log(couponId.value);
}
onMounted(async () => {
    loadpaydata();

})
</script>
<style scoped>
.my-style-selector :deep(select) {
    clear: both;
    -webkit-appearance: none;
    -moz-appearance: none;
    appearance: none;
    border-color: #6ec3f5;
    border-width: 3px;
    border-radius: 10em;
    background-color: #141e30;
    color: #1390e8;
    margin-right: 10px;
    margin-bottom: 10px;
    outline: none;
    padding: .3em 1.25em;
}

html {
    height: 100%;
}

body {
    margin: 0;
    padding: 0;
    font-family: sans-serif;
    background: linear-gradient(#141e30, #243b55);
}

.login-box {
    margin: auto;
    width: 400px;
    padding: 40px;
    background: rgba(0, 0, 0, .5);
    box-sizing: border-box;
    box-shadow: 0 15px 25px rgba(0, 0, 0, .6);
    border-radius: 10px;
}

.login-box h2 {
    margin: 0 0 30px;
    padding: 0;
    color: #fff;
    text-align: center;
}

.login-box h5 {
    margin: 10px 10px 10px 0px;
    padding: 0;
    color: #fff;
}

.login-box .user-box {
    position: relative;
}

.login-box .user-box input {
    width: 100%;
    padding: 10px 0;
    font-size: 16px;
    color: #fff;
    margin-bottom: 20px;
    border: none;
    border-bottom: 1px solid #fff;
    outline: none;
    background: transparent;
}

.login-box .user-box label {
    position: absolute;
    top: 0;
    left: 0;
    padding: 10px 0;
    font-size: 16px;
    color: #fff;
    pointer-events: none;
    transition: .5s;
}

.login-box .user-box input:focus~label,
.login-box .user-box input:valid~label {
    top: -20px;
    left: 0;
    color: #03e9f4;
    font-size: 12px;
}

.login-box form button {
    position: relative;
    display: inline-block;
    padding: 10px 10px;
    border: none;
    color: #03e9f4;
    font-size: 16px;
    text-decoration: none;
    text-transform: uppercase;
    overflow: hidden;
    transition: .5s;
    margin-top: 12px;
    letter-spacing: 4px;
    background-color: transparent;
}

.login-box button:hover {
    background: #03e9f4;
    color: #fff;
    font-size: 20px;
    border-radius: 5px;
    font-weight: bold;
    box-shadow: 0 0 5px #03e9f4,
        0 0 25px #03e9f4,
        0 0 50px #03e9f4,
        0 0 100px #03e9f4;
}

.login-box button span {
    position: absolute;
    display: block;
}

.login-box button span:nth-child(1) {
    top: 0;
    left: -100%;
    width: 100%;
    height: 2px;
    background: linear-gradient(90deg, transparent, #03e9f4);
    animation: btn-anim1 1s linear infinite;
}

@keyframes btn-anim1 {
    0% {
        left: -100%;
    }

    50%,
    100% {
        left: 100%;
    }
}

.login-box button span:nth-child(2) {
    top: -100%;
    right: 0;
    width: 2px;
    height: 100%;
    background: linear-gradient(180deg, transparent, #03e9f4);
    animation: btn-anim2 1s linear infinite;
    animation-delay: .25s
}

@keyframes btn-anim2 {
    0% {
        top: -100%;
    }

    50%,
    100% {
        top: 100%;
    }
}

.login-box button span:nth-child(3) {
    bottom: 0;
    right: -100%;
    width: 100%;
    height: 2px;
    background: linear-gradient(270deg, transparent, #03e9f4);
    animation: btn-anim3 1s linear infinite;
    animation-delay: .5s
}

@keyframes btn-anim3 {
    0% {
        right: -100%;
    }

    50%,
    100% {
        right: 100%;
    }
}

.login-box button span:nth-child(4) {
    bottom: -100%;
    left: 0;
    width: 2px;
    height: 100%;
    background: linear-gradient(360deg, transparent, #03e9f4);
    animation: btn-anim4 1s linear infinite;
    animation-delay: .75s
}

@keyframes btn-anim4 {
    0% {
        bottom: -100%;
    }

    50%,
    100% {
        bottom: 100%;
    }
}
</style>