<template>
	<div class="layout">
	<div id="form-main">
		<div id="form-div">
			<form class="form" id="form1" @submit.prevent="handleSubmit">
				<div style="margin: 10px;">
					<select v-model="reasonType" style="padding: 5px 5px 5px 45px;" class="form-select"
						aria-label="Default select example">
						<option selected>請選擇退款原因</option>
						<option value="序號不能用">序號不能用</option>
						<option value="這遊戲跟我想不一樣">這遊戲跟我想的不一樣</option>
						<option value="商品瑕疵">商品瑕疵</option>
						<option value="我不想要了">我不想要了</option>
					</select>
				</div>
				<p class="text">
					<textarea class="feedback-input" v-model="reason" placeholder="詳細描述最多100字"></textarea>
				</p>
				<div class="submit">
					<input type="submit" value="SEND" id="button-blue" />
					<div class="ease"></div>
				</div>
			</form>
		</div>
	</div>
</div>
</template>
    
<script setup >
import router from '@/router';
import axios from 'axios';
import Swal from 'sweetalert2';
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router';

let detailId = ref(0)
onMounted(async () => {
	const route = useRoute();
	detailId = route.params.detailId;

});
const reasonType = ref('請選擇退款原因');
const reason = ref('');

function handleSubmit() {
	Swal.fire({
		text: "Loading.......",
		allowOutsideClick: false,
		showConfirmButton: false
	});
	if (reasonType.value === '請選擇退款原因') {
		Swal.fire({
			icon: "error",
			text: "請選擇退款原因",
			allowOutsideClick: true,
			showCancelButton: true,
			confirmButtonText: "確定",
		})
		return;
	}
	if (reason.value === '') {
		Swal.fire({
			icon: "error",
			text: "請說明原因",
			allowOutsideClick: true,
			showCancelButton: true,
			confirmButtonText: "確定",
		})
		return;
	}
	const request = {
		"orderdetailId": detailId,
		"reasonType": reasonType.value,
		"reason": reason.value
	}
	const API_URL = `${import.meta.env.VITE_JAVA_URL}/Orderdetails/redfund`
	axios.post(API_URL, request, { withCredentials: true }).then((response) => {
		if (response.data === true) {
			Swal.fire({
				icon: "success",
				text: "退款審核已提交",
				allowOutsideClick: false,
				confirmButtonText: "確定",
			}).then((result) => {
				if (result.isConfirmed) {
					router.go(-1)
				}
			})
		} else {
			Swal.fire({
				icon: "error",
				text: "發生未知錯誤請重新提交",
				allowOutsideClick: true,
				showCancelButton: true,
				confirmButtonText: "確定",
			}).then((result) => {
				if (result.isConfirmed) {
					router.go(-1)
				} else {
					router.go(-1)
				}
			})
		}
	}
	)
}
</script>
    
<style scoped>
@import url(https://fonts.googleapis.com/css?family=Montserrat:400,700);

.layout {
  background: url('../../../public/images/homeimg.jpg') center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}

#feedback-page {
	text-align: center;
}

#form-main {
	margin-top: 100px;
	width: 100%;
	float: left;
	padding-top: 0px;
	min-height: 550px;
}

#form-div {
	background-color: rgba(72, 72, 72, 0.4);
	padding-left: 35px;
	padding-right: 35px;
	padding-top: 35px;
	padding-bottom: 50px;
	width: 450px;
	left: 50%;
	margin: auto;
	border-radius: 5px;
	-moz-border-radius: 7px;
	-webkit-border-radius: 7px;
}

.feedback-input {
	color: #3c3c3c;
	font-family: Helvetica, Arial, sans-serif;
	font-weight: 500;
	font-size: 18px;
	border-radius: 0;
	line-height: 22px;
	background-color: #fbfbfb;
	padding: 13px 54px 13px 54px;
	margin-bottom: 10px;
	width: 100%;
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	-ms-box-sizing: border-box;
	box-sizing: border-box;
	border: 3px solid rgba(0, 0, 0, 0);
	border-radius: 10px;
}

.feedback-input:focus {
	background: #fff;
	box-shadow: 0;
	border: 3px solid #3498db;
	outline: none;
	padding: 13px 54px 13px 54px;
}

.focused {
	color: #30aed6;
	border: #30aed6 solid 3px;
}

/* Icons ---------------------------------- */
#name {
	background-image: url(http://rexkirby.com/kirbyandson/images/name.svg);
	background-size: 30px 30px;
	background-position: 11px 8px;
	background-repeat: no-repeat;
}

#name:focus {
	background-image: url(http://rexkirby.com/kirbyandson/images/name.svg);
	background-size: 30px 30px;
	background-position: 8px 5px;
	background-position: 11px 8px;
	background-repeat: no-repeat;
}

#email {
	background-image: url(http://rexkirby.com/kirbyandson/images/email.svg);
	background-size: 30px 30px;
	background-position: 11px 8px;
	background-repeat: no-repeat;
}

#email:focus {
	background-image: url(http://rexkirby.com/kirbyandson/images/email.svg);
	background-size: 30px 30px;
	background-position: 11px 8px;
	background-repeat: no-repeat;
}

#comment {
	background-image: url(http://rexkirby.com/kirbyandson/images/comment.svg);
	background-size: 30px 30px;
	background-position: 11px 8px;
	background-repeat: no-repeat;
}

textarea {
	width: 100%;
	height: 150px;
	line-height: 150%;
	resize: vertical;
}

input:hover,
textarea:hover,
input:focus,
textarea:focus {
	background-color: white;
}

#button-blue {
	font-family: 'Montserrat', Arial, Helvetica, sans-serif;
	float: left;
	width: 100%;
	border: #fbfbfb solid 4px;
	cursor: pointer;
	background-color: #3498db;
	color: white;
	font-size: 24px;
	padding-top: 22px;
	padding-bottom: 22px;
	-webkit-transition: all 0.3s;
	-moz-transition: all 0.3s;
	transition: all 0.3s;
	margin-top: -4px;
	font-weight: 700;
}

#button-blue:hover {
	background-color: rgba(0, 0, 0, 0);
	color: #0493bd;
}

.submit:hover {
	color: #3498db;
}

.ease {
	width: 0px;
	height: 74px;
	background-color: #fbfbfb;
	-webkit-transition: .3s ease;
	-moz-transition: .3s ease;
	-o-transition: .3s ease;
	-ms-transition: .3s ease;
	transition: .3s ease;
}

.submit:hover .ease {
	width: 100%;
	background-color: white;
}

@media only screen and (max-width: 580px) {
	#form-div {
		left: 3%;
		margin-right: 3%;
		width: 88%;
		margin-left: 0;
		padding-left: 3%;
		padding-right: 3%;
	}
}
</style>