<template>
    <RouterView />
</template>
<!--<template>-->
<!--  <div ref="chartRef" style="width: 600px; height: 400px;"></div>-->
<!--</template>-->

<!--<script setup>-->
<!--import { onMounted, ref,reactive } from 'vue';-->
<!--import * as echarts from 'echarts';-->
<!--import request from "@/utils/request.js";-->

<!--const chartRef = ref(null);-->
<!--const data=reactive({-->
<!--  allData:[],-->
<!--  provinceData: []-->
<!--})-->
<!--const initLine=(params) => {-->
<!--  let provinceData-->
<!--  if(params===""){-->
<!--    provinceData=data.allData-->
<!--  }-->
<!--  else{-->
<!--     provinceData = data.provinceData[params];-->
<!--  }-->
<!--  const chart = echarts.init(chartRef.value);-->

<!--  const option = {-->
<!--    xAxis: {-->
<!--      type: 'category',-->
<!--      data: ['第一季度', '第二季度', '第三季度', '第四季度']-->
<!--    },-->
<!--    yAxis: {-->
<!--      type: 'value'-->
<!--    },-->
<!--    series: [-->
<!--      {-->
<!--        data: provinceData,-->
<!--        type: 'line'-->
<!--      }-->
<!--    ]-->
<!--  };-->

<!--  chart.setOption(option);-->
<!--}-->
<!--const load = () => {-->
<!--  request.get('/picture/time',).then(res => {-->
<!--    if (res.data.code === '200') {-->

<!--      // 存储每个季度的数量-->
<!--      const quarterCounts = [0, 0, 0, 0]; // Q1, Q2, Q3, Q4-->
<!--// 存储每个省份的季度数量-->
<!--      const provinceData = {};-->
<!--// 遍历数据-->
<!--      res.data.data.forEach(record => {-->
<!--        // 将createTime字符串转换为日期对象-->
<!--        const date = new Date(record.createTime);-->
<!--        //-&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;-->
<!--        // 确定季度-->
<!--        let quarter;-->
<!--        switch (date.getMonth()) {-->
<!--          case 0: // January-->
<!--          case 1: // February-->
<!--          case 2: // March-->
<!--            quarter = 0; // 第一季度-->
<!--            break;-->
<!--          case 3: // April-->
<!--          case 4: // May-->
<!--          case 5: // June-->
<!--            quarter = 1; // 第二季度-->
<!--            break;-->
<!--          case 6: // July-->
<!--          case 7: // August-->
<!--          case 8: // September-->
<!--            quarter = 2; // 第三季度-->
<!--            break;-->
<!--          case 9: // October-->
<!--          case 10: // November-->
<!--          case 11: // December-->
<!--            quarter = 3; // 第四季度-->
<!--            break;-->
<!--        }-->
<!--        // 解析省份和城市-->
<!--        const [province, city] = record.userIp.split("/");-->

<!--        // 初始化省份的季度数量数组-->
<!--        if (!provinceData[province]) {-->
<!--          provinceData[province] = [0, 0, 0, 0]; // Q1, Q2, Q3, Q4-->
<!--        }-->

<!--        // 增加对应季度的数量-->
<!--        provinceData[province][quarter]++;-->
<!--        // 增加对应季度的数量-->
<!--        quarterCounts[quarter]++;-->
<!--      });-->
<!--      console.log(provinceData)-->
<!--      data.provinceData=provinceData-->
<!--        console.log(quarterCounts)-->
<!--      data.allData=quarterCounts-->

<!--      initLine("")-->
<!--    }-->






<!--  })-->
<!--}-->



<!--onMounted(-->
<!--    load(),-->
<!--   );-->
<!--</script>-->







<!--<template>-->
<!--&lt;!&ndash;  <div ref="pieChart" style="width: 600px; height: 400px;"></div>&ndash;&gt;-->
<!--&lt;!&ndash;  <div ref="pieChart1" style="width: 600px; height: 400px;"></div>&ndash;&gt;-->

<!--  <el-carousel indicator-position="outside" :autoplay="false" :loop="false" >-->
<!--    <el-carousel-item >-->
<!--      <div ref="pieChart" style="width: 100%; height: 100%;background-color:red;"></div>-->

<!--    </el-carousel-item>-->
<!--    <el-carousel-item >-->
<!--        <div ref="pieChart1" style="width: 100%; height: 100%;background-color:green;"></div>-->


<!--    </el-carousel-item>-->
<!--  </el-carousel>-->
<!--</template>-->
<!--<style>-->
<!--.el-carousel{-->


<!--}-->

<!--</style>-->
<!--<script setup>-->
<!--import {ref, onMounted, reactive, nextTick} from 'vue';-->
<!--import * as echarts from 'echarts';-->
<!--import request from "@/utils/request.js";-->
<!--import {ElMessage} from "element-plus";-->

<!--const pieChart = ref(null); // 获取DOM引用-->
<!--const pieChart1 = ref(null); // 获取DOM引用-->
<!--const ageData = ref([]);-->
<!--const data=reactive({-->
<!--  ageData: [{name:"11",value:11}],-->
<!--  ageAndProvinceData:[]-->
<!--})-->

<!--const handleClick = (params) => {-->
<!--  const provinceData = data.ageAndProvinceData.find(item => item.provincename === params);-->

<!--  nextTick(() => {-->
<!--    initMap(provinceData.data)-->
<!--  })-->
<!--}-->
<!--const load = () => {-->
<!--  console.log("获取")-->
<!--  request.get('/user/age').then(res => {-->
<!--    if (res.data.code === '200') {-->
<!--      data.ageData=res.data.data-->
<!--      const  ageResult= data.ageData.reduce((acc, item) => {-->
<!--        const ageCategory = categorizeAge(parseInt(item.userAge));-->

<!--        // 如果年龄段已经存在，则数量加1，否则初始化数量为1-->
<!--        if (acc[ageCategory]) {-->
<!--          acc[ageCategory]++;-->
<!--        } else {-->
<!--          acc[ageCategory] = 1;-->
<!--        }-->

<!--        return acc;-->
<!--      }, {});-->
<!--// 将结果转换为[{name: value:}]的形式-->
<!--      const finalResult = Object.keys(ageResult).map(key => {-->
<!--        return { name: key, value: ageResult[key] };-->
<!--      });-->
<!--      data.ageData=finalResult-->


<!--      const ageGroupedData = res.data.data.reduce((acc, item) => {-->
<!--        const ageCategory = categorizeAge(parseInt(item.userAge));-->
<!--        const province = item.userIp.split('/')[0]; // 提取省份-->

<!--        // 如果省份和年龄段已经存在，则数量加1，否则初始化数量为1-->
<!--        if (acc[province] && acc[province][ageCategory]) {-->
<!--          acc[province][ageCategory]++;-->
<!--        } else if (acc[province]) {-->
<!--          acc[province][ageCategory] = 1;-->
<!--        } else {-->
<!--          acc[province] = {};-->
<!--          acc[province][ageCategory] = 1;-->
<!--        }-->

<!--        return acc;-->
<!--      }, {});-->

<!--// 将结果转换为[{provincename: data:}]的形式-->
<!--      const finalResult1 = Object.keys(ageGroupedData).map(province => {-->
<!--        const data = Object.keys(ageGroupedData[province]).map(key => {-->
<!--          return { name: key, value: ageGroupedData[province][key] };-->
<!--        });-->

<!--        return { provincename: province, data: data };-->
<!--      });-->
<!--      data.ageAndProvinceData=finalResult1-->
<!--      initMap(data.ageData,pieChart)-->
<!--      initMap(data.ageData,pieChart1)-->
<!--    } else {-->
<!--      ElMessage.error(res.data.msg)-->
<!--    }-->
<!--  })-->

<!--}-->
<!--const initMap=(data,pieChart)=>{-->
<!--  const myChart = echarts.init(pieChart.value);-->
<!--  const option = {-->

<!--    title: {-->
<!--      text: '示例饼图',-->
<!--      left: 'center'-->
<!--    },-->
<!--    tooltip: {-->
<!--      trigger: 'item'-->
<!--    },-->
<!--    legend: {-->
<!--      orient: 'vertical',-->
<!--      left: 'left'-->
<!--    },-->
<!--    series: [-->
<!--      {-->
<!--        name: '数据来源',-->
<!--        type: 'pie',-->
<!--        radius: ['40%', '70%'], // 饼图半径-->
<!--        label: {-->
<!--          show: true,-->
<!--          position: 'outside', // 标签显示在外侧-->
<!--          formatter: '{b}: {d}%' // 显示名称 + 百分比-->
<!--        },-->
<!--        data:data,-->
<!--        emphasis: {-->
<!--          itemStyle: {-->
<!--            shadowBlur: 10,-->
<!--            shadowOffsetX: 0,-->
<!--            shadowColor: 'rgba(0, 0, 0, 0.5)'-->
<!--          }-->
<!--        }-->
<!--      }-->
<!--    ]-->
<!--  };-->
<!--  myChart.setOption(option);-->

<!--  // 窗口大小变化时自适应-->
<!--  window.addEventListener('resize', () => myChart.resize());-->
<!--}-->

<!--// 饼图配置项-->


<!--function categorizeAge(age) {-->
<!--  if (age <= 12) {-->
<!--    return '儿童期';-->
<!--  } else if (age <= 35) {-->
<!--    return '青少年';-->
<!--  } else if (age <= 60) {-->
<!--    return '中年人';-->
<!--  } else {-->
<!--    return '老年人';-->
<!--  }-->
<!--}-->
<!--// const ageAndProvinceResult = data.ageData.reduce((acc, item) => {-->
<!--//   const ageCategory = categorizeAge(parseInt(item.userAge));-->
<!--//   const province = item.userIp.split('/')[0]; // 提取省份-->
<!--//-->
<!--//   // 如果省份和年龄段已经存在，则数量加1，否则初始化数量为1-->
<!--//   if (acc[province] && acc[province][ageCategory]) {-->
<!--//     acc[province][ageCategory]++;-->
<!--//   } else if (acc[province]) {-->
<!--//     acc[province][ageCategory] = 1;-->
<!--//   } else {-->
<!--//     acc[province] = {};-->
<!--//     acc[province][ageCategory] = 1;-->
<!--//   }-->
<!--//-->
<!--//   return acc;-->
<!--// }, {});-->
<!--//-->
<!--// // 使用reduce函数处理数据-->

<!--// 初始化图表-->

<!--onMounted(() => {-->
<!--  load()-->
<!--  console.log(ageData.value)-->
<!--  // const myChart = echarts.init(pieChart.value);-->
<!--  // myChart.setOption(option);-->
<!--  //-->
<!--  // // 窗口大小变化时自适应-->
<!--  // window.addEventListener('resize', () => myChart.resize());-->
<!--});-->
<!--</script>-->














