<!-- Step.vue  -->
<template>
    <div class="stepOut">
        <ul>
            <li class="stepItem" v-for="(stepItem, index) in stepInfo.list" :key="index">
                <!-- 模拟步骤条的节点，此处为圆圈 -->
                <div :class="stepInfo.step >= index + 1 ? 'icon active' : 'icon'"></div>
                <!-- 模拟步骤条连接线，动态显示  -->
                <div :class="stepInfo.step >= index + 2 ? 'line lineActive' : 'line'"
                    v-show="index !== stepInfo.list.length - 1">
                </div>
                <!-- 步骤名称  -->
                <p class="stepStatus">{{ stepItem.status }}</p>
                <!-- 步骤时间  -->
                <p class="statusTime">{{ stepItem.statusTime }}</p>
            </li>
        </ul>
    </div>
</template>
  
<script>
export default {
    name: 'steps',
    props: {
        // 传递步骤参数
        stepInfo: {
            type: Object,
            default: function () {
                return {
                    list: [],
                    step: 0
                }
            }
        }
    }
}
</script>

<style lang="less" scoped>
ul {
    list-style: none;
    /* 移除列表项前面的默认样式（如圆点）*/
}

.stepOut {
    width: 100%;
    margin: 25px;
    display: flex;
    justify-content: center;

    .stepItem {
        width: 260px;
        height: 70px;
        float: left;
        font-family: SimSun;
        font-size: 16px;
        text-align: center;
        position: relative;

        .icon {
            width: 13px;
            height: 13px;
            border-radius: 50%;
            background: rgba(226, 226, 226, 1);
            margin: 0 auto;
            position: relative;
            z-index: 888;
        }

        .active {
            background-color: green;
        }

        .line {
            position: absolute;
            top: 6px;
            left: 50%;
            border-bottom: 1px dashed rgba(255, 255, 255, 0.7);
            width: 260px;
            z-index: 111;
        }

        .lineActive {
            border-bottom: 1px solid green;
        }

        .stepStatus {
            color: yellow;
        }

        .statusTime {
            color: rgb(255, 255, 255);
            // opacity: 0.5;
            line-height: 1px;
        }
    }
}
</style>
  
  