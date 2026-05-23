<template>
  <div ref="pieChart" style="width: 100%; height:100%;">11</div>
</template>
<style>

</style>
<script setup>

import request from "@/utils/request.js";
import {computed, nextTick, onMounted, reactive, ref, watch} from "vue";
import * as echarts from "echarts";
import {useProvinceNameStore} from "@/store/userStore.js";
const provinceNameStore = useProvinceNameStore();
const provinceName = computed(() => provinceNameStore.provinceName)
const pieChart = ref(null); // 获取DOM引用
const data=reactive({
    chinaData:[],
  provinceData:[],

})
watch(provinceName,(newValue,oldValue)=>{
  console.log("watch")
  handleClick(newValue)
})
const handleClick = (params) => {
  let chartData
  if(params==='中国'){
    chartData=data.chinaData
  }else{
    const provinceData = data.provinceData.find(item => item.province === params);
    chartData=provinceData.ranges
  }
  nextTick(() => {
    initMap(chartData)
  })
}
const initMap=(data)=>{
  provinceNameStore.setBudgetData(data)
  const myChart = echarts.init(pieChart.value);
  const option = {
    graphic:[{
  type:"text",
      top:"20",
      right:"30",
      style:{
        text: provinceName.value+" 预算分布",
        fill:"#595353",
        left:"10%",
        fontSize: 20,
        fontWeight: 'bold',
      }},
      {
        type: "rect",
        top: "19",
        right: "11",

        shape: {
          width: 10,
          height: 20,
          r:5
        },
        style: {
          fill: "blue"
        }
      }
   ],

    color: [ '#20b9cf', '#2089cf', '#205bcf', '#002a95'],
    legend: {
      orient: 'vertical',
      left: 'left',       // 更精确的左侧定位
      itemWidth: 14,     // 图例标记宽度
      itemHeight: 8,     // 图例标记高度
      itemGap: 5,        // 图例项间隔
    },
    series: [
      {
        top:30,
        name: '数据来源',
        type: 'pie',
        radius: ['35%', '60%'],

        label: {
          show: true,
          position: 'inside', // 标签显示在外侧
          formatter: '{b}: {d}%' // 显示名称 + 百分比
        },
        data:data,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  myChart.setOption(option);

  // 窗口大小变化时自适应
  window.addEventListener('resize', () => myChart.resize());
}

const load=()=>{

  request.get('/picture/getMoney').then(res => {
    if (res.data.code === '200') {
      console.log(res.data.data)
      // 初始化区间统计对象
      const ranges = [
        { name: '0-10000', value: 0 },
        { name: '10000-30000', value: 0 },
        { name: '30000-70000', value: 0 },
        { name: '70000以上', value: 0 }
      ];

// 遍历数组，统计每个区间的数量
      res.data.data.forEach(item => {
        const budget = parseInt(item.budget);
        if (budget < 10000) {
          ranges[0].value++;
        } else if (budget < 30000) {
          ranges[1].value++;
        } else if (budget < 70000) {
          ranges[2].value++;
        } else {
          ranges[3].value++;
        }
      });
      data.chinaData=ranges
console.log(ranges)

      const ranges1 = [
        { name: '0-10000', value: 0 },
        { name: '10000-30000', value: 0 },
        { name: '30000-70000', value: 0 },
        { name: '70000以上', value: 0 }
      ];
      const provinceResult = {};
      res.data.data.forEach(item => {
        const budget = parseInt(item.budget);
        const province = item.userIp.split('/')[0];

        // 如果该省份还没有统计信息，初始化
        if (!provinceResult[province]) {
          provinceResult[province] = JSON.parse(JSON.stringify(ranges1));
        }

        // 根据预算确定所属区间并增加对应区间的计数
        if (budget < 10000) {
          provinceResult[province][0].value++;
        } else if (budget < 30000) {
          provinceResult[province][1].value++;
        } else if (budget < 70000) {
          provinceResult[province][2].value++;
        } else {
          provinceResult[province][3].value++;
        }
      });

// 将结果转换为所需的对象数组格式
      const finalResult = Object.keys(provinceResult).map(province => ({
        province,
        ranges: provinceResult[province]
      }));
      data.provinceData=finalResult

      console.log(data.chinaData)
      console.log( data.provinceData)
      initMap(data.chinaData)

      }
    }
  )
}
onMounted(() => {
  load()
  // const myChart = echarts.init(pieChart.value);
  // myChart.setOption(option);
  //
  // // 窗口大小变化时自适应
  // window.addEventListener('resize', () => myChart.resize());
});
</script>