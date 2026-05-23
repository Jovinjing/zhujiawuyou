<template>
      <div ref="chartRef" style="width: 100%; height:100%;"></div>
</template>
<style>

</style>

<script setup>
import {onMounted, ref, reactive, watch, computed} from 'vue';
import * as echarts from 'echarts';
import request from "@/utils/request.js";
import {useProvinceNameStore} from "@/store/userStore.js";
const provinceNameStore = useProvinceNameStore();
const chartRef = ref(null);
const provinceName = computed(() => provinceNameStore.provinceName)

const data=reactive({
  allData:[],
  provinceData: [],
  nowData:[]
})
watch(provinceName,(newValue,oldValue)=>{
  console.log("watch")
  initLine(newValue)
})
const initLine=(params) => {
  let provinceData
  if(params==="中国"){
    provinceData=data.allData
  }
  else{
    provinceData = data.provinceData[params];
  }

  data.nowData=provinceData
  provinceNameStore.setLineData(data.nowData)
  console.log("当前折线图数据")
  console.log(data.nowData)
  const chart = echarts.init(chartRef.value);

  const option = {
    tooltip : {
      trigger: 'axis'
    },
    title: {
      text: provinceName.value+' 各季度用户使用图',  // 主标题
      left: 'center',
      top:"5%",// 标题位置居中
      textStyle: {
        fontSize: 18,  // 标题字体大小
        fontWeight: 'bold'  // 标题字体加粗
      }
    },

    grid: {
      left: '3%',
      right: '5%',
      top:'21%',
      bottom: '5%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['第一季度', '第二季度', '第三季度', '第四季度'],


    },

    yAxis: {
      type: 'value'
    },
    series: [
      {
        lineStyle:{
          color:'#72b0f9',
        },
        data: provinceData,
        type: 'line',
        areaStyle: {

          normal: {type: 'default',
            color: new echarts.graphic.LinearGradient(0, 0, 0, 0.8, [{
              offset: 0,
              color: 'rgba(129,197,255,.6)'
            }, {
              offset: 1,
              color: 'rgba(129,197,255,.0)'
            }], false)
          }
        },
        smooth:true,

      }
    ]
  };

  chart.setOption(option);

  // 窗口大小变化时自适应
  window.addEventListener('resize', () => chart.resize());
}
const load = () => {
  request.get('/picture/time',).then(res => {
    if (res.data.code === '200') {

      // 存储每个季度的数量
      const quarterCounts = [0, 0, 0, 0]; // Q1, Q2, Q3, Q4
// 存储每个省份的季度数量
      const provinceData = {};
// 遍历数据
      res.data.data.forEach(record => {
        // 将createTime字符串转换为日期对象
        const date = new Date(record.createTime);
        //---------------------------------------------
        // 确定季度
        let quarter;
        switch (date.getMonth()) {
          case 0: // January
          case 1: // February
          case 2: // March
            quarter = 0; // 第一季度
            break;
          case 3: // April
          case 4: // May
          case 5: // June
            quarter = 1; // 第二季度
            break;
          case 6: // July
          case 7: // August
          case 8: // September
            quarter = 2; // 第三季度
            break;
          case 9: // October
          case 10: // November
          case 11: // December
            quarter = 3; // 第四季度
            break;
        }
        // 解析省份和城市
        const [province, city] = record.userIp.split("/");

        // 初始化省份的季度数量数组
        if (!provinceData[province]) {
          provinceData[province] = [0, 0, 0, 0]; // Q1, Q2, Q3, Q4
        }

        // 增加对应季度的数量
        provinceData[province][quarter]++;
        // 增加对应季度的数量
        quarterCounts[quarter]++;
      });
      data.provinceData=provinceData
      data.allData=quarterCounts

      initLine("中国")
    }






  })
}



onMounted(
    () => {
      load()
    }
);
</script>