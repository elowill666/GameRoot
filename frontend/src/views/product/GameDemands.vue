<template>

    <div class="infor">
        <div>電腦系統：{{ gameInfor[0]?.os }}</div>
        <div>處理器：{{ gameInfor[0]?.processor }}</div>
        <div>內存：{{ gameInfor[0]?.memory }}</div>
        <div>顯卡：{{ gameInfor[0]?.graphics }}</div>
        <div>儲存空間：{{ gameInfor[0]?.storage }}</div>
    </div>
</template>
    
<script setup>
    const prop = defineProps(["gameInfor"])
</script>
<style scoped>
.infor{
 padding-left: 20px; 
 padding-bottom: 30px;
 color: gray;
 font-size: 1.5em;
}
</style>