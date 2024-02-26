<template>
  
  <button type="button" class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#staticBackdrop" :data-bs-productId="prop.productId" :data-bs-userId="prop.userId" :data-bs-orderdetailId="prop.orderdetailId" >
    留言
  </button>

  <!-- Modal -->
  <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
    aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="staticBackdropLabel">
            寫下你對這產品的感想</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <!-- {{ prop.productId + "hehe" }}
          {{ prop.orderDetailId + "hihi" }}
          {{ prop.userId + "hoho" }} -->
          <div style="color: black; border: 1px #BEBEBE solid ;border-radius: 2px;padding: 4px; display: flex; align-items: center;">
            <span>
              <el-image  id="productImage" style="width: 125px; height: 125px" :src="null" :fit="contain" />
            </span>
            <span style="margin-left: 8px;" id="productName"></span>
          </div>
          <el-rate v-model="productComment.score" size="large" allow-half />
          {{ productComment.score }}
          <br>
          <input type="text" v-model="productComment.text" id="productComment" style="width: 450px;height: 100px;">
          <!-- {{ productComment }} -->
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
            Close
          </button>
          <button type="button" class="btn btn-primary" @click="clickToRate">送出評分</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import router from '@/router';
import axios from 'axios';
import Swal from 'sweetalert2';
import { defineProps, ref, onMounted } from 'vue'

const prop = defineProps(["productId", "userId", "orderdetailId"])
const productComment = ref({
  productId: null,
  userId: null,
  text: "",
  score: 0,
  orderdetailId: null,
  productCommentStatus: "Commented",
});
const product = ref([]);

onMounted(() => {
  installModalListener();
});
const installModalListener = () => {
  
  var exampleModal = document.getElementById('staticBackdrop')
  exampleModal.addEventListener('show.bs.modal', async (event) => {
    var button = event.relatedTarget
    var pId = button.getAttribute('data-bs-productId')
    var uId = button.getAttribute('data-bs-userId')
    var odId = button.getAttribute('data-bs-orderdetailId')
  //載入商品資料
    const VITE_API_GAMESHOP_UNITPRODUCT = `${import.meta.env.VITE_JAVA_URL}/gameshop/product/find/`;
    const response = await axios.get(VITE_API_GAMESHOP_UNITPRODUCT + pId)
    product.value = response.data;
  //另外載入圖片
  const VITE_API_PRODUCTCOVERIMAGE = `${import.meta.env.VITE_JAVA_URL
    }/dashboard/productimage/`;
  const getimg = await axios.post(VITE_API_PRODUCTCOVERIMAGE+pId);
  product.value.coverImage = getimg.data;
  console.log("product = ",product.value);

    productComment.value.productId = pId;
    productComment.value.orderdetailId = odId;
    productComment.value.userId = uId;
    console.log("productcomment = ",productComment.value)
    if (response.data !== null) {
      const productName = exampleModal.querySelector('#productName')
      const productImage = exampleModal.querySelector('#productImage')
      if (productName) {
      productName.textContent = product.value.productName;
    }

    if (productImage) {
      // console.log("hihi");
      productImage.setAttribute("src", product.value.coverImage);
    }
    }

  })
}


function clickToRate(){
  const VITE_API_GAMESHOP_INSERTPRODUCTCOMMENT = `${import.meta.env.VITE_JAVA_URL
    }/gameshop/product/createcomment`;
    if(productComment.value.score!==0){
      
      axios.post(VITE_API_GAMESHOP_INSERTPRODUCTCOMMENT,productComment.value).then(res=>{
        if(res.data){
          Swal.fire({
            title:"超棒的！",
            text:res.data.text,
            icon:"success",
            showConfirmButton:true,
            confirmButtonText:"確認"
          }).then(res=>{
            if(res.isConfirmed){
              location.reload();
            }
          })
        }
      })
    } else {
      Swal.fire({
            title:"分數為必填",
            text:"請重新評分",
            icon:"error",
            showConfirmButton:true,
            confirmButtonText:"確認"
    })
}}
</script>

<style></style>
