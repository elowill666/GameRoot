<template>
    <div class="interview" v-html="productInter?.product?.description"></div>
    <div class="imgdiv" v-for="item in productInter?.descriptionImages" :key="item.productImageId">
        <img :src="item?.productImage" :alt="item?.productImageId">
    </div>
</template>
    
<script setup>
import { defineProps,onMounted, ref } from 'vue';

const prop = defineProps(["productInter"])

onMounted(async () => {
    // console.log("prop = ",prop);
    // const product = ref({prop});
})

</script>
    
<style scoped>
div {
    font-size: 1.5em;
}
img {
    width: 600px;
    height: 600px;
}
.imgdiv {
    text-align: center;
}
.interview {
    color: black;
    font-size: 1em;
    padding: 30px;
}
</style>