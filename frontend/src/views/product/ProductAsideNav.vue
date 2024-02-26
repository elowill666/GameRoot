<template>
    <div>
        <table>
            <!-- 遊戲 -->
            <!-- 主 -->
            <tr>
                <td class="title first" @click="selectType('digitalGames')">虛擬遊戲</td>
            </tr>
            <!-- 副 -->
            <tr>
                <td @click="selectSubype('action')">動作</td>
            </tr>
            <tr>
                <td @click="selectSubype('simulation')">模擬</td>
            </tr>
            <tr>
                <td @click="selectSubype('adventure')">冒險</td>
            </tr>
            <tr>
                <td @click="selectSubype('racing')">競賽</td>
            </tr>
            <tr>
                <td @click="selectSubype('others')">其他</td>
            </tr>
            <!-- 點數商城 -->
            <!-- 主 -->
            <tr>
                <td class="title" @click="selectType('cash')">虛擬點數</td>
            </tr>
            <!-- 副 -->
            <tr>
                <td @click="selectSubype('cash')">Node根節點</td>
            </tr>
            <!-- 周邊 -->
            <!-- 主 -->
            <tr>
                <td class="title" @click="selectType('physical')">周邊商品</td>
            </tr>
            <!-- 副 -->
            <tr>
                <td @click="selectSubype('Pokemon')">寶可夢</td>
            </tr>
            <tr>
                <td @click="selectSubype('Mario')">馬力歐</td>
            </tr>
            <tr>
                <td @click="selectSubype('Zelda')">薩爾達傳說</td>
            </tr>
            <tr>
                <td @click="selectSubype('others')">其他</td>
            </tr>
        </table>
    </div>
</template>
    
<script setup >
import router from '@/router';
import { ref } from 'vue'
let type = ref();
let subtype = ref();

function selectType(value) {
    router.push({
        name: "ProductsFindType",
        params: {
            type: value
        }
    }).then(() => {
        location.reload();

    }
    )
}
function selectSubype(value) {
    router.push({
        name: "ProductsFindSubType",
        params: {
            subtype: value
        }
    }).then(() => {
        location.reload();
    }
    )
}


</script>
    
<style scoped>
div {
    padding-top: 40px;

}

table {
    background-color: #354551;
    border-radius: 10px;
    cursor: pointer;
}

td {
    width: 300px;
    text-align: center;
    color: white;
    padding: 10px;

}

td:hover {
    font-size: 20px;
}

.title {
    background-color: #0b1722;
    color: #ffdc42;
}

.first {
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
}
</style>