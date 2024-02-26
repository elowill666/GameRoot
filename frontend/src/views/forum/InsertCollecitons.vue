<template>
  <span  @click.stop=switchCollection(articleId) style="cursor: pointer;">
  <svg style="color:#FFD306;cursor: pointer" xmlns="http://www.w3.org/2000/svg" width="28" height="28" fill="currentColor" class="bi bi-bookmark" viewBox="0 0 16 16" >
<!-- 空的 -->
  <path v-if="!collectionStatus.isCollectionFilled" d="M2 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v13.5a.5.5 0 0 1-.777.416L8 13.101l-5.223 2.815A.5.5 0 0 1 2 15.5zm2-1a1 1 0 0 0-1 1v12.566l4.723-2.482a.5.5 0 0 1 .554 0L13 14.566V2a1 1 0 0 0-1-1z"/>
  <!-- 滿的 -->
  <path v-else d="M2 2v13.5a.5.5 0 0 0 .74.439L8 13.069l5.26 2.87A.5.5 0 0 0 14 15.5V2a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2"/>
</svg>

</span>
</template>
    
<script setup>
import { ref,onMounted,watchEffect } from 'vue'
import axios from 'axios';
import Swal from 'sweetalert2';
import router from '@/router';
const prop = defineProps(["articleId"]);

const collectionStatus = ref({
  isCollectionFilled: false,
})

const request = ref({
  articleId: "",
  userId: "",
});

// 掛載時確認此USER有按那些愛心
onMounted(async () => {
  //監聽articleId變化
  watchEffect(async () => {
    if (prop.articleId) {

      //確認是否登入 
      const VITE_API_CHECKLOGIN = `${import.meta.env.VITE_JAVA_URL
        }/user/authenticate`;
      //獲取登入者資訊
      const VITE_API_USERPROFILE = `${import.meta.env.VITE_JAVA_URL}/user/profile`;

      request.value.articleId = prop.articleId;
      await axios
        .get(VITE_API_CHECKLOGIN, { withCredentials: true })
        .then(async (response) => {
          if (response.data) {

            await axios
              .get(VITE_API_USERPROFILE, { withCredentials: true })
              .then(async (res) => {
                request.value.userId = res.data.id;

                if (request.value.userId != null) {

                  await axios.post(`${import.meta.env.VITE_JAVA_URL}/collections/check`, request.value).then((response) => {
                    // console.log(response.data.input);
                    if (response.data.input) {
                        collectionStatus.value.isCollectionFilled = response.data.input;

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
function switchCollection(aId) {
  //獲取登入者資訊
  const VITE_API_USERPROFILE = `${import.meta.env.VITE_JAVA_URL}/user/profile`;
  //確認是否登入
  const VITE_API_CHECKLOGIN = `${import.meta.env.VITE_JAVA_URL
    }/user/authenticate`;
  //塞入文章收藏
  const VITE_API_INSERTCOLLECTION = `${import.meta.env.VITE_JAVA_URL
    }/collections/insert`;
  //刪除文章收藏
  const VITE_API_DELETECOLLECTION = `${import.meta.env.VITE_JAVA_URL
    }/collections/delete`;


  request.articleId = aId;
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
              axios.post(VITE_API_INSERTCOLLECTION, request).then((response) => {
                console.log(response.data);
                if (response.data.scuccess) {
                  Swal.fire({
                    title: "超讚的",
                    text: response.data.text,
                    icon: "success",
                    showConfirmButton: true,
                  });
                  collectionStatus.value.isCollectionFilled = true;
                } else {
                  Swal.fire({
                    title: "請確認",
                    text: "是否移除此文章？",
                    icon: "question",
                    showConfirmButton: true,
                    confirmButtonText: "刪除",
                    confirmButtonColor: "red",
                    showCancelButton: true,
                    cancelButtonText: "取消"
                  }).then(async result => {
                    if (result.isConfirmed) {
                      request.value.articleId = aId;
                      request.value.userId = res.data.id;
                      // console.log("request.value = ", request.value)
                      const response = await axios.delete(`${VITE_API_DELETECOLLECTION}?articleId=${request.value.articleId}&userId=${request.value.userId}`)

                      // console.log("delete = ", response)
                      if (response.data.scuccess) {

                        Swal.fire({
                          title: "刪除成功",
                          text: response.data.text,
                          icon: "success",
                          showConfirmButton: true,
                        })
                        collectionStatus.value.isCollectionFilled = false;
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
/* span{
  pointer-events: none;
} */
    
</style>