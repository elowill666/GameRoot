<template>
  <aside class="aside_dashboard">
    <div style="width: 100%; background-color: #141f2a; padding: 0">
      <router-link to="/dashboard/datamanagement"
        ><button
          class="btn btn-light dashboard_btn"
          :class="{ active: activeButton === 'datamanagement' }"
          @click="setActiveButton('datamanagement')"
        >
          Dashboard
        </button></router-link
      >
      <div style="justify-content: center; align-items: center">
        <button
          class="btn btn-light dashboard_btn"
          :class="{ active: activeButton === 'usermanagement' }"
          @click="goUserManagement('usermanagement')"
        >
          會員管理
        </button>
        <button
          class="btn btn-light dashboard_btn"
          :class="{ active: activeButton === 'productmanagement' }"
          @click="goProductManagement('productmanagement')"
        >
          商品管理
        </button>
        <router-link to="/dashboard/launchproduct"
          ><button
            class="btn btn-light dashboard_btn"
            :class="{ active: activeButton === 'launchproduct' }"
            @click="setActiveButton('launchproduct')"
          >
            商品上架
          </button></router-link
        >
        <button
          class="btn btn-light dashboard_btn"
          :class="{ active: activeButton === 'ordermanagement' }"
          @click="goOrderManagement('ordermanagement')"
        >
          訂單管理</button
        ><button
          class="btn btn-light dashboard_btn"
          :class="{ active: activeButton === 'forummanagement' }"
          @click="goForumManagement('forummanagement')"
        >
          論壇管理
        </button>

        <button
          class="btn btn-light dashboard_btn"
          :class="{ active: activeButton === 'couponmanagement' }"
          @click="goCouponManagement('couponmanagement')"
        >
          新增折扣卷
        </button>
      </div>
    </div>
  </aside>
</template>
<script setup>
import router from "@/router";
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
const route = useRoute();
const activeButton = ref(null);
onMounted(() => {
  refreshPage(`${window.location.pathname}`);
});

const refreshPage = (pagePath) => {
  if (!pagePath.startsWith("/dashboard")) {
    activeButton.value = "datamanagement";
  }
  if (pagePath.startsWith("/dashboard")) {
    pagePath = pagePath.split("/");

    activeButton.value = pagePath[2];
  }
};
function goUserManagement(pagePath) {
  setActiveButton(pagePath);
  router.push({
    name: "usermanagement",
    params: {
      page: 1,
    },
  });
}

function goProductManagement(pagePath) {
  setActiveButton(pagePath);
  router.push({
    name: "productmanagement",
    params: {
      page: 1,
    },
  });
}

function goOrderManagement(pagePath) {
  setActiveButton(pagePath);
  router.push({
    name: "ordermanagement",
    params: {
      page: 1,
    },
  });
}

function goForumManagement(pagePath) {
  setActiveButton(pagePath);
  router.push({
    name: "articlemanagement",
    params: {
      page: 1,
    },
  });
}
function goCouponManagement(pagePath) {
  setActiveButton(pagePath);
  router.push({
    name: "CouponInsert",
    params: {
      page: 1,
    },
  });
}
const setActiveButton = (pagePath) => {
  activeButton.value = pagePath;
};
</script>
<style lang="css" scoped>
.btn {
  width: 72%;
  height: 200%;
  margin: 20px;
  padding: 8px;
}
.aside_dashboard {
  width: 12%;
  float: left;
  height: auto;
  margin: 0;
  display: inline;
}

p {
  font-family: "Lato", sans-serif;
  font-weight: 300;
  text-align: center;
  font-size: 18px;
  color: #676767;
}
.dashboard_btn {
  border: none;
  cursor: pointer;
  background-color: #c7c8cc;
  text-align: center;
}

.dashboard_btn.active {
  background-color: #084bbe; /* 按下后的背景颜色 */
  color: white;
}
</style>
