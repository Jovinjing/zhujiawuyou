<template>
  <!--  <div ref="pieChart" style="width: 600px; height: 400px;"></div>-->
  <!--  <div ref="pieChart1" style="width: 600px; height: 400px;"></div>-->
  <el-carousel indicator-position="outside" :autoplay="false" :loop="false" >
    <el-carousel-item >
      <div ref="pieChart" style="width: 100%; height:100%;"></div>
    </el-carousel-item>
    <el-carousel-item >
      <div ref="pieChart1" style="width: 100%; height: 100%;"></div>
    </el-carousel-item>
  </el-carousel>
</template>
<style>


</style>
<script setup>
import {ref, onMounted, reactive, nextTick,watch,computed} from 'vue';
import * as echarts from 'echarts';
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import {useProvinceNameStore} from "../store/userStore.js";
const provinceNameStore = useProvinceNameStore();
const provinceName = computed(() => provinceNameStore.provinceName)
const pieChart = ref(null); // 获取DOM引用
const pieChart1 = ref(null); // 获取DOM引用
const ageData = ref([]);
const data=reactive({
  ageData: [{name:"11",value:11}],
  ageAndProvinceData:[],
  genderData: [{name:"11",value:11}],
  genderAndProvinceData:[],
  nowData:[],
  pieClass:""
})
console.log(provinceName)
watch(provinceName,(newValue,oldValue)=>{
  console.log("watch")
  handleClick(newValue)
})
const handleClick = (params) => {
  let chartData
  let chartData1
  if(params==='中国'){
    chartData=data.ageData
     chartData1=data.genderData
  }else{
    const provinceData = data.ageAndProvinceData.find(item => item.provincename === params);

    const provinceData1 = data.genderAndProvinceData.find(item => item.provincename === params);
     chartData=provinceData.data
     chartData1=provinceData1.data
  }
  nextTick(() => {
    initMap(chartData,pieChart,"年龄")
    initMap(chartData1,pieChart1,"性别")
  })
}
const load = () => {
  request.get('/user/age').then(res => {
    if (res.data.code === '200') {
      data.ageData=res.data.data
      const  ageResult= data.ageData.reduce((acc, item) => {
        const ageCategory = categorizeAge(parseInt(item.userAge));

        // 如果年龄段已经存在，则数量加1，否则初始化数量为1
        if (acc[ageCategory]) {
          acc[ageCategory]++;
        } else {
          acc[ageCategory] = 1;
        }

        return acc;
      }, {});
// 将结果转换为[{name: value:}]的形式
      const finalResult = Object.keys(ageResult).map(key => {
        return { name: key, value: ageResult[key] };
      });
      data.ageData=finalResult


      const ageGroupedData = res.data.data.reduce((acc, item) => {
        const ageCategory = categorizeAge(parseInt(item.userAge));
        const province = item.userIp.split('/')[0]; // 提取省份

        // 如果省份和年龄段已经存在，则数量加1，否则初始化数量为1
        if (acc[province] && acc[province][ageCategory]) {
          acc[province][ageCategory]++;
        } else if (acc[province]) {
          acc[province][ageCategory] = 1;
        } else {
          acc[province] = {};
          acc[province][ageCategory] = 1;
        }

        return acc;
      }, {});

// 将结果转换为[{provincename: data:}]的形式
      const finalResult1 = Object.keys(ageGroupedData).map(province => {
        const data = Object.keys(ageGroupedData[province]).map(key => {
          return { name: key, value: ageGroupedData[province][key] };
        });

        return { provincename: province, data: data };
      });
      data.ageAndProvinceData=finalResult1
      console.log(data.ageAndProvinceData)
      provinceNameStore.setPieAgeData(data.ageData)

      initMap(data.ageData,pieChart,"年龄")
    }
  })


  request.get('/user/gender').then(res => {
    if (res.data.code === '200') {
      data.genderData=res.data.data
// 使用reduce函数处理数据，根据性别进行分组
      const genderData = res.data.data.reduce((acc, item) => {
        const gender = item.userGender;

        // 如果性别已经存在，则数量加1，否则初始化数量为1
        if (acc[gender]) {
          acc[gender]++;
        } else {
          acc[gender] = 1;
        }

        return acc;
      }, {});

// 将结果转换为[{name: value:}]的形式
      const finalResult2 = Object.keys(genderData).map(key => {
        return { name: key, value: genderData[key] };
      });
      data.genderData=finalResult2


      const genderGroupedData = res.data.data.reduce((acc, item) => {
        const gender = item.userGender;
        const province = item.userIp.split('/')[0]; // 提取省份

        // 如果省份和性别已经存在，则数量加1，否则初始化数量为1
        if (acc[province] && acc[province][gender]) {
          acc[province][gender]++;
        } else if (acc[province]) {
          acc[province][gender] = 1;
        } else {
          acc[province] = {};
          acc[province][gender] = 1;
        }

        return acc;
      }, {});

// 将结果转换为[{provincename: data:}]的形式
      const finalResult3 = Object.keys(genderGroupedData).map(province => {
        const data = Object.keys(genderGroupedData[province]).map(key => {
          return { name: key, value: genderGroupedData[province][key] };
        });

        return { provincename: province, data: data };
      });
      data.genderAndProvinceData=finalResult3
      provinceNameStore.setPieGenderData(data.genderAndProvinceData)

      initMap(data.genderData,pieChart1,"性别")
    } else {
      ElMessage.error(res.data.msg)
    }
  })
}
const initMap=(data,pieChart,pieClass)=>{
  const myChart = echarts.init(pieChart.value);
  data.nowData=data
  if(pieClass==="性别"){
    data.pieClass=pieClass
    provinceNameStore.setPieGenderData(data.nowData)
  }{
    data.pieClass=pieClass

    provinceNameStore.setPieAgeData(data.nowData)
  }
  console.log("当前饼图数据")
  console.log(data.nowData)
  const option = {
    graphic:[{
    type:"text",
    right:"30",
      top:"20",
     style:{
      text: provinceName.value+" "+data.pieClass+"分布",
       fill:"#595353",
      left:"10%",
       fontSize: 20,
       fontWeight: 'bold',
     }
    },
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
      }],

    tooltip: {
      trigger: 'item'
    },
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
        name: '数据来源',
        type: 'pie',
        radius: ['35%', '60%'],
        clockwise: false,
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

// 饼图配置项


function categorizeAge(age) {
  if (age <= 12) {
    return '儿童期';
  } else if (age <= 35) {
    return '青少年';
  } else if (age <= 60) {
    return '中年人';
  } else {
    return '老年人';
  }
}
// const ageAndProvinceResult = data.ageData.reduce((acc, item) => {
//   const ageCategory = categorizeAge(parseInt(item.userAge));
//   const province = item.userIp.split('/')[0]; // 提取省份
//
//   // 如果省份和年龄段已经存在，则数量加1，否则初始化数量为1
//   if (acc[province] && acc[province][ageCategory]) {
//     acc[province][ageCategory]++;
//   } else if (acc[province]) {
//     acc[province][ageCategory] = 1;
//   } else {
//     acc[province] = {};
//     acc[province][ageCategory] = 1;
//   }
//
//   return acc;
// }, {});
//
// // 使用reduce函数处理数据

// 初始化图表

onMounted(() => {
  load()
  // const myChart = echarts.init(pieChart.value);
  // myChart.setOption(option);
  //
  // // 窗口大小变化时自适应
  // window.addEventListener('resize', () => myChart.resize());
});
</script>