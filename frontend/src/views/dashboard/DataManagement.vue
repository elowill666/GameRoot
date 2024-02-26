<template lang="">
  <div style="display: flex" class="layout">
    <DashboardRouterlink style="height: 100vh"></DashboardRouterlink>
    <main
      style="
        width: 100%;
        height: 100%;
        margin-left: 30px;
        margin-bottom: 30px;
        margin-top: 30px;
      "
    >
      <div class="count-up-container">
        <div>
          <span>{{ countUpTittle.forTotal }}</span>
          <count-up :end-val="totalNumber"></count-up>
        </div>
        <div>
          <span>{{ countUpTittle.forThisYear }}</span>
          <count-up :end-val="yearNumber"></count-up>
        </div>
        <div>
          <span>{{ countUpTittle.forThisMonth }}</span>
          <count-up :end-val="mouthNumber"></count-up>
        </div>
      </div>

      <div style="display: flex; width: 100%; height: 50%">
        <div class="div_canvas" style="margin-left: 0; width: 52%; height: 50%">
          <canvas ref="revenuneBarChart"></canvas>
        </div>
        <div class="div_canvas" style="width: 27%; height: 20%">
          <canvas ref="revenunePieChart"></canvas>
        </div>
        <div
          class="div_canvas d-flex flex-column"
          style="width: 21%; align-items: center; justify-content: center"
        >
          <div style="font-size: 0.9em; margin-bottom: 0.5em">日期</div>
          <div>
            <el-date-picker
              format="YYYY/MM/DD"
              type="date"
              placeholder="Pick a day"
              v-model="searchdate"
              size="small"
              style="width: 150px; margin-bottom: 0.5em"
              @change="loading(searchdate)"
            />
          </div>
          <div style="font-size: 0.9em">產品類別</div>
          <el-select
            class="m-2"
            placeholder="Select"
            style="width: 150px"
            v-model="wantProductType"
            size="small"
            @change="findproductsubType(wantProductType)"
          >
            <el-option
              v-for="item in searchProductType"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
          <div style="font-size: 0.9em" v-if="isSelect.type">產品子類別</div>
          <el-select
            class="m-2"
            placeholder="Select"
            style="width: 150px"
            size="small"
            v-model="wantProductSubtype"
            v-if="isSelect.type"
            @change="findproduct(wantProductSubtype)"
          >
            <el-option
              v-for="item in searchSubtype"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
          <div
            style="font-size: 0.9em; margin-bottom: 0.5em"
            v-if="isSelect.subtype"
          >
            產品
          </div>
          <el-select-v2
            v-model="wantProductName"
            filterable
            size="small"
            :options="searchProductName"
            placeholder="Please select"
            style="width: 150px"
            v-if="isSelect.subtype"
            @change="loading"
          />
          <div>
            <button
              class="btn btn-secondary"
              style="margin-top: 10px"
              @click="dbclickcleanSearch"
            >
              清除搜尋
            </button>
          </div>
        </div>
      </div>
      <div style="display: flex; width: 100%; height: 50%">
        <div class="div_canvas" style="margin-left: 0; width: 50%; height: 50%">
          <canvas ref="annualRevenuneChart"></canvas>
        </div>

        <div class="div_canvas" style="width: 50%; height: 50%">
          <canvas ref="monthlyRevenuneChart"></canvas>
        </div>
      </div>
    </main>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import Chart from "chart.js/auto";
import CountUp from "vue-countup-v3";
import axios from "axios";
import DashboardRouterlink from "./DashboardRouterlink.vue";

const countUpTittle = ref({
  forTotal: "總營收",
  forThisYear: "本年總營收",
  forThisMonth: "本月總營收",
});

const chartTitle = ref({
  barChart: "歷史累積總營收",
  pieChart: `${new Date().getFullYear()}年各類產品銷售佔比(%)`,
  annualChart: `${new Date().getFullYear()}年每月營收`,
  monthlyChart: `${new Date().getMonth() + 1}月每日營收`,
});

const searchdate = ref(new Date());
const searchProductType = ref([]);
const searchSubtype = ref([]);
const searchProductName = ref([]);

const wantProductType = ref("");
const wantProductSubtype = ref("");
const wantProductName = ref("");

const isSelect = ref({
  type: false,
  subtype: false,
});

const dataBackgroundColor = [
  "rgba(245, 168, 14, 0.8)",
  "rgba(0,145,0,1)",
  "rgba(184,112,112,1)",
  "rgba(190,119,255,1)",
  "rgba(255,151,151,1)",
  "rgba(255,160,66,1)",
  "rgba(90,90,173,1)",
];

const path = import.meta.env.VITE_JAVA_URL;
//年度每月總營收圖表渲染用
const annualRevenuneChart = ref(null);
//年度每月總營收數據
const annualRevenuneChartData = ref([]);
//年度每月遊戲營收數據
const annualRevenuneChartDataGame = ref([]);
//年度每月實體營收數據
const annualRevenuneChartDataPhy = ref([]);
//年度每月總營收x軸
const x_axisOfAnnualRevenune = ref([
  "Jan",
  "Feb",
  "Mar",
  "Apr",
  "May",
  "Jun",
  "Jul",
  "Aug",
  "Sep",
  "Oct",
  "Nov",
  "Dec",
]);
//送圖別集中用的
const ARChartData = ref([]);

//某月每日總營收圖表渲染用
const monthlyRevenuneChart = ref(null);
//某月每日總營收圖表用數據
const monthlyRevenuneChartData = ref([]);
//年度每月遊戲營收數據
const monthlyRevenuneChartDataGame = ref([]);
//年度每月實體營收數據
const monthlyRevenuneChartDataPhy = ref([]);
//某月每日總營收圖表用x軸
const x_axisOfMonthlyRevenune = ref([]);
//送圖別集中用的
const MRChartData = ref([]);

//總營收Bar圖表渲染用
const revenuneBarChart = ref(null);
//Bar圖歷史總營收數據
const revenuneBarChartData = ref([]);
//Bar圖歷史總營收x軸
const x_axisOfBarChart = ref([]);
//送圖別集中用的
const RBChartData = ref();

//總營收看產品佔比Pie圖渲染用
const revenunePieChart = ref(null);
//總營收看產品佔比Pie圖數據
const revenunePieChartData = ref([]);
//總營收看產品佔比Pie圖各區名字
const x_axisOfPieChart = ref([]);
//送圖別集中用的
const RPChartData = ref();

const totalNumber = ref(0);
const yearNumber = ref(0);
const mouthNumber = ref(0);
const chartData = ref([]);

let totalAnnualLineChart = null;
let barChart = null;
let monthlyLineChart = null;
let pieChart = null;

onMounted(() => {
  loading();
});

async function loading(date) {
  // refreshData();
  chartData.value = await findAllData(date);
  console.log(chartData.value);
  dynamicChangeTitle();
  //拿來綁訂及計算計數器上的數字
  sumAllKindOFRevenune();
  //新增一個年度營收圖表
  createAnnualRevenuneChart(
    annualRevenuneChart,
    chartData.value,
    x_axisOfAnnualRevenune
  );
  //新增一個歷史營收總累計Bar
  createTotalRevenuneBarChart(
    revenuneBarChart,
    chartData.value,
    x_axisOfBarChart
  );
  //新增一個某月營收
  createMonthlyRevenuneChart(
    monthlyRevenuneChart,
    chartData.value,
    x_axisOfMonthlyRevenune
  );
  //新增一個各類營收圓餅圖
  createRevenunePieChart(revenunePieChart, chartData.value, x_axisOfPieChart);
  if (searchProductType.value.length == 0) {
    findproductType();
  }
}

//拿來綁訂及計算計數器上的數字
function sumAllKindOFRevenune() {
  totalNumber.value = chartData.value.revenune;
  for (let temp in chartData.value.annualRevenune) {
    yearNumber.value += chartData.value.annualRevenune[temp];
  }
  for (let temp in chartData.value.monthlyRevenune) {
    mouthNumber.value += chartData.value.monthlyRevenune[temp];
  }
}

//後端找資料
async function findAllData(timedate) {
  let request = {
    date: searchdate.value.toISOString(),
    productType: wantProductType.value,
    productSubtype: wantProductSubtype.value,
    productName: wantProductName.value,
  };
  if (!request.date) {
    request.date = new Date().toISOString();
  }
  try {
    const findAllDataresponse = await axios.post(
      `${path}/dashboard/data/find`,
      request
    );
    refreshData();
    return findAllDataresponse.data;
  } catch (error) {
    console.log(error);
  }
}
//做一個每年營收
function createAnnualRevenuneChart(canvasRef, data, x_axis) {
  let i = 0;
  //抓各項
  for (let tempfirst in data.annualRevenuneByProductType) {
    annualRevenuneChartData.value = [];
    for (let tempsecond in data.annualRevenuneByProductType[tempfirst]) {
      annualRevenuneChartData.value.push(
        data.annualRevenuneByProductType[tempfirst][tempsecond]
      );
    }
    ARChartData.value.push(
      forLineChartData(
        tempfirst,
        annualRevenuneChartData.value,
        dataBackgroundColor[i]
      )
    );
    i++;
  }
  annualRevenuneChartData.value = [];
  //抓total
  for (let temp in data.annualRevenune) {
    annualRevenuneChartData.value.push(data.annualRevenune[temp]);
  }

  ARChartData.value.push(
    forLineChartData(
      "Total",
      annualRevenuneChartData.value,
      "rgb(75, 192, 192)"
    )
  );

  //帶入圖表
  createtotalAnnualLineChart(canvasRef, ARChartData.value, x_axis);
}
//新增年度總營收
function createTotalRevenuneBarChart(canvasRef, data, x_axis) {
  //取得fulldata
  let barCartBackgrondColor = [];
  let i = 0;
  revenuneBarChartData.value = [];

  //取各項

  for (let temp of Object.keys(data.revenueByProductType)) {
    revenuneBarChartData.value.push(data.revenueByProductType[temp]);
    x_axis.value.push(temp);
    barCartBackgrondColor.push(dataBackgroundColor[i]);
    i++;
  }

  //取total
  revenuneBarChartData.value.push(data.revenune);
  barCartBackgrondColor.push("rgb(75, 192, 192)");
  x_axis.value.push("Total");
  RBChartData.value = {
    data: {
      label: "FullRevenune",
      data: revenuneBarChartData.value,
      backgroundColor: barCartBackgrondColor,
      borderColor: barCartBackgrondColor,
    },
  };

  createBarChart(canvasRef, RBChartData.value, x_axis);
}

//圓餅圖數據為各類總銷售額
function createRevenunePieChart(canvasRef, data, x_axis) {
  let pieCartBackgrondColor = [];
  let i = 0;
  for (let temp in data.revenueByProductType) {
    revenunePieChartData.value.push(
      ((data.revenueByProductType[temp] / data.revenune) * 100).toFixed(0)
    );
    x_axis.value.push(temp);
    pieCartBackgrondColor.push(dataBackgroundColor[i]);
    i++;
  }
  RPChartData.value = {
    data: {
      label: "",
      data: revenunePieChartData.value,
      backgroundColor: pieCartBackgrondColor,
      hoverOffset: 4,
    },
  };
  createPieChart(canvasRef, RPChartData.value, x_axis);
}

//做一個月營收
function createMonthlyRevenuneChart(canvasRef, data, x_axis) {
  let i = 0;
  let b = 1;
  //抓各項
  for (let tempfirst in data.monthlyRevenuneByProductType) {
    monthlyRevenuneChartData.value = [];
    for (let tempsecond in data.monthlyRevenuneByProductType[tempfirst]) {
      monthlyRevenuneChartData.value.push(
        data.monthlyRevenuneByProductType[tempfirst][tempsecond]
      );
      b++;
    }
    MRChartData.value.push(
      forLineChartData(
        tempfirst,
        monthlyRevenuneChartData.value,
        dataBackgroundColor[i]
      )
    );
    i++;
  }
  monthlyRevenuneChartData.value = [];
  //抓total
  for (let temp in data.monthlyRevenune) {
    monthlyRevenuneChartData.value.push(data.monthlyRevenune[temp]);
    x_axis.value.push(temp);
  }

  MRChartData.value.push(
    forLineChartData(
      "Total",
      monthlyRevenuneChartData.value,
      "rgb(75, 192, 192)"
    )
  );

  // 帶入圖表
  createMonthlyLineChart(canvasRef, MRChartData.value, x_axis);
}

function createMonthlyLineChart(canvasRef, fulldata, x_axis) {
  const canvasName = canvasRef.value.getContext("2d");
  if (monthlyLineChart) {
    monthlyLineChart.destroy();
  }

  monthlyLineChart = new Chart(canvasName, {
    type: "line",
    data: {
      labels: x_axis.value,
      datasets: fulldata,
    },
    options: {
      scales: {
        y: {
          grid: {
            color: "#272727", // Y 軸網格線顏色
          },
        },
        x: {
          grid: {
            display: false, // 將 X 軸顯示設置為 false
          },
        },
      },
      responsive: true,
      plugins: {
        legend: {
          position: "top",
        },
        title: {
          display: true,
          text: chartTitle.value.monthlyChart,
          font: {
            size: 18,
          },
        },
      },
    },
  });
}

//每年營收用的
function createtotalAnnualLineChart(canvasRef, fulldata, x_axis) {
  const canvasName = canvasRef.value.getContext("2d");

  if (totalAnnualLineChart) {
    totalAnnualLineChart.destroy();
  }
  totalAnnualLineChart = new Chart(canvasName, {
    type: "line",
    data: {
      labels: x_axis.value,
      datasets: fulldata,
    },
    options: {
      scales: {
        y: {
          grid: {
            color: "#272727", // Y 軸網格線顏色
          },
        },
        x: {
          grid: {
            display: false, // 將 X 軸顯示設置為 false
          },
        },
      },
      responsive: true,
      plugins: {
        legend: {
          position: "top",
        },
        title: {
          display: true,
          text: chartTitle.value.annualChart,
          font: {
            size: 18,
          },
        },
      },
    },
  });
}
//把折線圖要帶的數據包裝起來，只需填入標籤名，數據及顏色
function forLineChartData(label, data, color) {
  return {
    label: label,
    data: data,
    fill: false,
    borderColor: color,
  };
}

function createBarChart(canvasRef, dataOfBar, x_axis) {
  const canvas = canvasRef.value.getContext("2d");
  if (barChart) {
    barChart.destroy();
  }
  barChart = new Chart(canvas, {
    type: "bar",
    data: {
      labels: x_axis.value,
      datasets: [dataOfBar.data],
    },
    options: {
      scales: {
        y: {
          grid: {
            color: "#272727", // Y 軸網格線顏色
          },
        },
        x: {
          grid: {
            display: false, // 將 X 軸顯示設置為 false
          },
        },
      },
      plugins: {
        title: {
          display: true,
          text: chartTitle.value.barChart,
          fontColor: "blue",
          font: {
            size: 18,
          },
          position: "top", // 標題位置
        },
      },
    },
  });
}

function createPieChart(canvasRef, data, x_axis) {
  const canvas = canvasRef.value.getContext("2d");
  if (pieChart) {
    pieChart.destroy();
  }

  pieChart = new Chart(canvas, {
    type: "pie",
    data: {
      labels: x_axis.value,
      datasets: [data.data],
    },
    options: {
      layout: {
        padding: {
          left: 0,
          right: 0,
          top: 0,
          bottom: 0,
        },
      },

      radius: 110,
      responsive: true,
      plugins: {
        legend: {
          position: "top",
          labels: {
            padding: 5,
            font: {
              size: 14,
            },
          },
        },
        title: {
          display: true,
          text: chartTitle.value.pieChart,
          font: {
            size: 18,
          },
        },
      },
    },
  });
}

async function findproductType() {
  try {
    const response = await axios.get(`${path}/dashboard/product/producttype`);
    const tempData = [];
    for (let temp of response.data) {
      let tempobj = {
        value: temp,
        label: temp,
      };
      searchProductType.value.push(tempobj);
    }
    return response.data;
  } catch (error) {
    console.log(error);
  }
}
async function findproductsubType(type) {
  let productType = {
    productType: type,
  };
  try {
    const response = await axios.post(
      `${path}/dashboard/product/subtype`,
      productType
    );
    searchSubtype.value = [];
    wantProductSubtype.value = "";
    searchProductName.value = [];
    wantProductName.value = "";
    isSelect.value.type = false;
    isSelect.value.subtype = false;
    for (let temp of response.data) {
      let tempobj = {
        value: temp,
        label: temp,
      };
      searchSubtype.value.push(tempobj);
    }
    isSelect.value.type = true;
    loading();
    return response.data;
  } catch (error) {
    console.log(error);
  }
}

async function findproduct(type) {
  let productSubtype = {
    productSubtype: type,
  };
  try {
    const response = await axios.post(
      `${path}/dashboard/product/productname`,
      productSubtype
    );
    searchProductName.value = [];
    wantProductName.value = "";
    for (let temp of response.data) {
      let tempobj = {
        value: temp,
        label: temp,
      };
      searchProductName.value.push(tempobj);
    }
    isSelect.value.subtype = true;
    loading();
    return response.data;
  } catch (error) {
    console.log(error);
  }
}
//動態顯示名字
function dynamicChangeTitle() {
  if (wantProductName.value != 0) {
    countUpTittle.value.forTotal = `${wantProductName.value}總營收`;
    countUpTittle.value.forThisYear = `${wantProductName.value}本年總營收`;
    countUpTittle.value.forThisMonth = `${wantProductName.value}本月總營收`;
    chartTitle.value.barChart = `${wantProductName.value}歷史累積總營收`;
    chartTitle.value.pieChart = `${searchdate.value.getFullYear()}年${
      wantProductName.value
    }銷售佔比(%)`;
    chartTitle.value.annualChart = `${searchdate.value.getFullYear()}年${
      wantProductName.value
    }每月營收`;
    chartTitle.value.monthlyChart = `${searchdate.value.getMonth() + 1}月${
      wantProductName.value
    }每日營收`;
  } else if (wantProductSubtype.value != 0) {
    countUpTittle.value.forTotal = `${wantProductSubtype.value}總營收`;
    countUpTittle.value.forThisYear = `${wantProductSubtype.value}本年總營收`;
    countUpTittle.value.forThisMonth = `${wantProductSubtype.value}本月總營收`;
    chartTitle.value.barChart = `${wantProductSubtype.value}歷史累積總營收`;
    chartTitle.value.pieChart = `${searchdate.value.getFullYear()}年${
      wantProductSubtype.value
    }銷售佔比(%)`;
    chartTitle.value.annualChart = `${searchdate.value.getFullYear()}年${
      wantProductSubtype.value
    }每月營收`;
    chartTitle.value.monthlyChart = `${searchdate.value.getMonth() + 1}月${
      wantProductSubtype.value
    }每日營收`;
  } else if (wantProductType.value != 0) {
    countUpTittle.value.forTotal = `${wantProductType.value}總營收`;
    countUpTittle.value.forThisYear = `${wantProductType.value}本年總營收`;
    countUpTittle.value.forThisMonth = `${wantProductType.value}本月總營收`;
    chartTitle.value.barChart = `${wantProductType.value}歷史累積總營收`;
    chartTitle.value.pieChart = `${searchdate.value.getFullYear()}年${
      wantProductType.value
    }銷售佔比(%)`;
    chartTitle.value.annualChart = `${searchdate.value.getFullYear()}年${
      wantProductType.value
    }每月營收`;
    chartTitle.value.monthlyChart = `${searchdate.value.getMonth() + 1}月${
      wantProductType.value
    }每日營收`;
  } else {
    countUpTittle.value.forTotal = `總營收`;
    countUpTittle.value.forThisYear = `本年總營收`;
    countUpTittle.value.forThisMonth = `本月總營收`;
    chartTitle.value.barChart = "歷史累積總營收";
    chartTitle.value.pieChart = `${searchdate.value.getFullYear()}年各類銷售佔比(%)`;
    chartTitle.value.annualChart = `${searchdate.value.getFullYear()}年每月營收`;
    chartTitle.value.monthlyChart = `${
      searchdate.value.getMonth() + 1
    }月每日營收`;
  }
}
function dbclickcleanSearch() {
  wantProductName.value = "";
  wantProductType.value = "";
  wantProductSubtype.value = "";
  isSelect.value.type = "";
  isSelect.value.subtype = "";
  searchProductName.value = [];
  searchSubtype.value = [];
  searchProductType.value = [];
  searchdate.value = new Date();
  loading();
  findproductType();
}

function refreshData() {
  // searchProductType.value = [];
  // searchSubtype.value = [];
  // wantProductType.value = "";
  // wantProductSubtype.value = "";
  // annualRevenuneChart.value = null;
  //年度每月總營收數據
  annualRevenuneChartData.value = [];
  //年度每月遊戲營收數據
  annualRevenuneChartDataGame.value = [];
  //年度每月實體營收數據
  annualRevenuneChartDataPhy.value = [];

  //送圖別集中用的
  ARChartData.value = [];

  //某月每日總營收圖表渲染用
  // monthlyRevenuneChart.value = null;
  //某月每日總營收圖表用數據
  monthlyRevenuneChartData.value = [];
  //年度每月遊戲營收數據
  monthlyRevenuneChartDataGame.value = [];
  //年度每月實體營收數據
  monthlyRevenuneChartDataPhy.value = [];
  //某月每日總營收圖表用x軸
  x_axisOfMonthlyRevenune.value = [];
  //送圖別集中用的
  MRChartData.value = [];

  //總營收Bar圖表渲染用
  // revenuneBarChart.value = null;
  //Bar圖歷史總營收數據
  revenuneBarChartData.value = [];
  //Bar圖歷史總營收x軸
  x_axisOfBarChart.value = [];
  //送圖別集中用的
  RBChartData.value = null;

  //總營收看產品佔比Pie圖渲染用
  // revenunePieChart.value = null;
  //總營收看產品佔比Pie圖數據
  revenunePieChartData.value = [];
  //總營收看產品佔比Pie圖各區名字
  x_axisOfPieChart.value = [];
  //送圖別集中用的
  RPChartData.value = null;

  totalNumber.value = 0;
  yearNumber.value = 0;
  mouthNumber.value = 0;
  chartData.value = [];
}
</script>
<style scoped>
.layout {
  background: url("../../../public/images/loginImg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}

canvas {
  width: 100%;
  height: 100%;
  margin: auto;
  padding: 10px;
}

.count-up-container {
  display: flex;
  justify-content: space-between;
  width: 80%;
  background-color: #141f2a;
  font-size: 1.5em;
  padding-left: 20px;
  padding-right: 20px;

  margin-bottom: 30px;
}

.div_canvas {
  padding: 5px;
  margin: 20px;

  width: 100%;
  background-color: #141f2a;
}

.pair_canvas1 {
  display: block;
  width: 40%;
  margin-right: 5px;
}

.pair_canvas2 {
  margin-left: 5px;
  width: 40%;
  display: block;
}

.pie_chart {
  width: 100%;
}
</style>
