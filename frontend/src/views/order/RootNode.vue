<template>
    <div class="layout">
    <main>
        <div class="row row-cols-1 row-cols-md-3 g-4 all">
            <div class="col" v-for="item in rootNode" :key="item.productId">
                <div class="card">
                    <img :src=item.coverImage alt="a snow-capped mountain range" />
                    <div class="card-body">
                        <h2 class="card-title">{{ item.productName }}</h2>
                        <span v-if="item.productStatus === 'discount'" class="org-price">
                            原價{{ item.price }}</span>
                        <span v-if="item.productStatus === 'discount'" class="discount-price">
                            驚爆大增量{{ item.discount }}</span>
                        <button class="btn btn-secondary bbun" @click="gopay(item)">儲值去</button>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>
</template>
    
<script setup>
import axios from 'axios';
import Swal from 'sweetalert2';
import { ref, onMounted } from 'vue'
import { useRouter } from "vue-router";


let rootNode = ref([]);
let discountprice = ref(0);
const router = useRouter();

function gopay(item) {
    Swal.fire({
        text: "Loading.......",
        allowOutsideClick: false,
        showConfirmButton: false
    });
    const API_URL = `${import.meta.env.VITE_JAVA_URL}/cash/getcash/`
    if (item.productStatus === 'discount') {
        discountprice.value = item.discount
    } else { discountprice.value = 0 }
    axios.get(API_URL + item.price + "/" + item.productId + "/" + discountprice.value, { withCredentials: true }).then(function (response) {
        const div = document.createElement("div");
        div.innerHTML = response.data; // html code
        const newTab = window.open();
        newTab.document.body.appendChild(div);
        newTab.document.forms.allPayAPIForm.submit();
        setTimeout(() => {
            Swal.fire({
                icon: "success",
                text: "儲值成功",
                allowOutsideClick: false,
                showCancelButton: false,
                showDenyButton: true,
                confirmButtonText: "前往商店",
                denyButtonText: `繼續儲值`,
            }).then(function (result) {
                if (result.isConfirmed) {
                    window.location.reload()
                    window.location.href = '/product/products'
                } else {
                    location.reload()
                }
            })
        }, 5000);
    })
}


function loadRootNode() {
    const API_URL = `${import.meta.env.VITE_JAVA_URL}/cash/rootnodeproduct`
    axios.get(API_URL, { withCredentials: true }).then(function (response) {
        rootNode.value = response.data
        console.log(rootNode.value);
    })
}
onMounted(async () => {
    loadRootNode();
})

</script>
<style scoped>
.layout {
  background: url('../../../public/images/homeimg.jpg') center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}
main {
    height: 1000px;
}

.discount-price {
    color: red;
    font-size: x-large;
}

.org-price {
    color: gray;
    text-decoration: line-through;
}

.all {
    margin: auto;
    width: 50%;
}

.bbun {
    float: right;
}
</style>
