<template>
    <table>
        <thead>
            <tr>
                <td>產品名稱</td>
                <td>數量</td>
                <td>價格</td>
                <td>評分狀態</td>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(orderDetail,index) in orderdetails" :key="index">
            <td>{{orderDetail.historyProductName}}</td>
            <td>{{orderDetail.quantity}}</td>
            <td>{{ orderDetail.historyPrice }}</td>
            <td><OrderProductCommentButton v-if="orderDetail.productCommentStatus === 'noComment' && new Date(orderDetail.productCommentEndtime) >= new Date()" :userId="order.userId" :productId="orderDetail.productId" :orderDetailId="orderDetail.orderdetailId"></OrderProductCommentButton >
            <span v-if="orderDetail.productCommentStatus === 'noComment' && new Date(orderDetail.productCommentEndtime) <= new Date()">未評論</span>
            <span v-if="orderDetail.productCommentStatus === 'Commented' ">已評論</span>
            </td>
            </tr>
        </tbody>
    </table>
        
</template>
    
<script setup>
import {ref,onMounted} from 'vue'
import { useRoute } from 'vue-router';
import axios from 'axios';
import OrderProductCommentButton from './OrderProductCommentButton.vue';
const orderdetails = ref([])
const order = ref([])
onMounted(async()=>{
    const route = useRoute();
    const orderId = route.params.id
    const VITE_API_GAMESHOP_ORDERDETAIL = `${import.meta.env.VITE_JAVA_URL}/Orderdetails/find/`
    const VITE_API_GAMESHOP_ORDER = `${import.meta.env.VITE_JAVA_URL}/order/find/`
    const response01= await axios.get(VITE_API_GAMESHOP_ORDERDETAIL+orderId)
    const response02 = await axios.get(VITE_API_GAMESHOP_ORDER+orderId)
    orderdetails.value = response01.data;
    console.log("orderdetail = ",orderdetails.value);
    order.value = response02.data;
    console.log("order = ",order.value);

})
</script>
    
<style>
    
</style>