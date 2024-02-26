<template>
  <svg  @click="switchWishlist(productId)" style="color: red;cursor: pointer" xmlns="http://www.w3.org/2000/svg" width="32"
    height="32" fill="currentColor" class="bi bi-heart-fill" viewBox="0 0 16 16">
    <path v-if="wishlistStatus.isWishlistFilled" fill-rule="evenodd"
      d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
    <path v-else
      d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143q.09.083.176.171a3 3 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15" />
  </svg>
</template>

<script setup>
import { onMounted, ref, defineProps, reactive, watchEffect } from "vue";
import axios from "axios";
import Swal from "sweetalert2";
import router from "@/router";

const prop = defineProps(["productId"]);

// let isWishlistFilled = ref(false)
const wishlistStatus = ref({
  isWishlistFilled: false,
})

const request = ref({
  productId: "",
  userId: "",
});


// 掛載時確認此USER有按那些愛心
onMounted(async () => {
  //監聽productId變化
  watchEffect(async () => {
    if (prop.productId) {

      //確認是否登入 
      const VITE_API_CHECKLOGIN = `${import.meta.env.VITE_JAVA_URL
        }/user/authenticate`;
      //獲取登入者資訊
      const VITE_API_USERPROFILE = `${import.meta.env.VITE_JAVA_URL}/user/profile`;

      request.value.productId = prop.productId;
      // console.log("pppp = ",request.value.productId);
      await axios
        .get(VITE_API_CHECKLOGIN, { withCredentials: true })
        .then(async (response) => {
          if (response.data) {

            await axios
              .get(VITE_API_USERPROFILE, { withCredentials: true })
              .then(async (res) => {
                request.value.userId = res.data.id;

                if (request.value.userId != null) {

                  await axios.post(`${import.meta.env.VITE_JAVA_URL}/wishlist/check`, request.value).then((response) => {
                    // console.log(response.data.input);
                    if (response.data.input) {
                      wishlistStatus.value.isWishlistFilled = response.data.input;

                    }
                  })
                }
              })

          }
        })
    }
  })
});

//點選加入願望清單
function switchWishlist(pId) {
  //獲取登入者資訊
  const VITE_API_USERPROFILE = `${import.meta.env.VITE_JAVA_URL}/user/profile`;
  //確認是否登入
  const VITE_API_CHECKLOGIN = `${import.meta.env.VITE_JAVA_URL
    }/user/authenticate`;
  //塞入購物車
  const VITE_API_INSERTWISHLIST = `${import.meta.env.VITE_JAVA_URL
    }/wishlist/insert`;
  //刪除購物車
  const VITE_API_DELETEWISHLIST = `${import.meta.env.VITE_JAVA_URL
    }/wishlist/delete`;


  request.productId = pId;
  // const sessionUser = sessionStorage.getItem('user');
  // console.log("ooooo==",sessionStorage)
  // request.userId = sessionUser?parseInt(storedUserId):null;
  // console.log("xxxxx==",request);
  axios
    .get(VITE_API_CHECKLOGIN, { withCredentials: true })
    .then((response) => {
      if (response.data == false) {
        Swal.fire({
          title: "尚未登入",
          text: "前往登入頁面？",
          icon: "warning",
          confirmButtonText: "立馬登入",
          showCancelButton: true,
          cancelButtonText: "等等再登入~",
        }).then((result) => {
          if (result.isConfirmed) {
            router.push({ name: "userlogin" });
          }
        });
      } else {
        axios
          .get(VITE_API_USERPROFILE, { withCredentials: true })
          .then((res) => {
            request.userId = res.data.id;
            console.log("request = ", request);
            if (request.userId != null) {
              axios.post(VITE_API_INSERTWISHLIST, request).then((response) => {
                console.log(response.data);
                if (response.data.scuccess) {
                  Swal.fire({
                    title: "超讚的",
                    text: response.data.text,
                    icon: "success",
                    showConfirmButton: true,
                  });
                  wishlistStatus.value.isWishlistFilled = true;
                  setTimeout(function () { Swal.close(); }, 3000);
                } else {
                  Swal.fire({
                    title: "請確認",
                    text: "是否移除此商品？",
                    icon: "question",
                    showConfirmButton: true,
                    confirmButtonText: "刪除",
                    confirmButtonColor: "red",
                    showCancelButton: true,
                    cancelButtonText: "取消"
                  }).then(async result => {
                    if (result.isConfirmed) {
                      request.value.productId = pId;
                      request.value.userId = res.data.id;
                      // console.log("request.value = ", request.value)
                      const response = await axios.delete(`${VITE_API_DELETEWISHLIST}?productId=${request.value.productId}&userId=${request.value.userId}`)

                      // console.log("delete = ", response)
                      if (response.data.scuccess) {

                        Swal.fire({
                          title: "刪除成功",
                          text: response.data.text,
                          icon: "success",
                          showConfirmButton: true,
                        })
                        wishlistStatus.value.isWishlistFilled = false;
                        if (window.location.pathname === "/product/productWishlist") {
                          location.reload();
                        }
                      } else {
                        Swal.fire({
                          title: "刪除失敗",
                          text: response.data.text,
                          icon: "question",
                          showConfirmButton: true,
                        })
                      }
                    }

                  });

                }
              });
            }
          });
      }
    })
    .catch((error) => {
      console.log("error = ", error);
    });
}
</script>

<style scoped>

</style>
