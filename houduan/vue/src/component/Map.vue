<template>
  <div class="container">
    <!-- 左：中国地图 -->
    <div class="left-panel">

      <div ref="mapChart" class="chart" style="height:100%;width: 100%; ">
      </div>
    </div>

    <!-- 右：柱状图 -->
    <div class="right-panel">
      <div v-show="showBarChart" ref="barChart" class="chart" style="height: 100%;width: 95%"></div>

    </div>
  </div>
</template>


<script setup>
import {ref, onMounted, shallowRef, reactive, computed, watch} from 'vue'
import {useProvinceNameStore} from "../store/userStore.js";

import * as echarts from 'echarts'
import chinaJson from '../assets/static/中国_省.json' // 中国地图数据
import {nextTick} from "vue";
import houseData from '../assets/static/data.json'

import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
// 注册中国地图
echarts.registerMap('China', chinaJson)
const data = reactive({
  ipData:[],
  provinceData:[],
  nowData:[],
  sum:0,
})

const provinceNameStore = useProvinceNameStore();
const provinceName = computed(() => provinceNameStore.provinceName)
const nowProvinceNum = computed(() => provinceNameStore.nowProvinceNum)
const mapChart = ref(null)
const barChart = ref(null)
const mapInstance = shallowRef(null)
const barInstance = shallowRef(null)
const showBarChart = ref(true)

watch(provinceName,(newValue,oldValue)=>{
  console.log("watch")
  // 添加安全检查，确保mapInstance已初始化
  if (mapInstance.value && newValue) {
    mapInstance.value.dispatchAction({
      type: 'select',
      name: newValue
    });

    if(newValue==="中国"){
      initBarChart("中国",data.provinceData)
    }else{
      const province = data.ipData.find(item => item.provinceName === newValue);
      const cityData=province ? province.cities : [];
      initBarChart(newValue,cityData)
    }
  }
})

// 初始化地图（修改后的版本）
const load = () => {
  request.get('/user/ip').then(res => {

    if (res.data.code === '200') {
      data.ipData=res.data?.data||[]
      const provinceCityCount = {};
      const provinceCount = {};

      data.ipData.forEach(city => {
        const [province, cityPart] = city.split('/');
        if (!provinceCityCount[province]) {
          provinceCityCount[province] = {};
        }
        if (!provinceCount[province]) {
          provinceCount[province] = 0;
        }
        provinceCount[province]++;
        if (!provinceCityCount[province][cityPart]) {
          provinceCityCount[province][cityPart] = 0;
        }
        provinceCityCount[province][cityPart]++;
      });

// 将对象转换为所需的数组格式
      const result = Object.keys(provinceCityCount).map(province => ({
        provinceName: province,
        cities: Object.keys(provinceCityCount[province]).map(city => ({
          name: city,
          value: provinceCityCount[province][city]
        }))
      }));
      const result1 = Object.keys(provinceCount).map(province => ({
        name: province,
        value: provinceCount[province]
      }));
      data.ipData=result
      data.provinceData=result1
      console.log("data1.provinceData="+data.provinceData)
      for (let i = 0; i < data.provinceData.length; i++) {
        const currentValue = data.provinceData[i].value;
        if (typeof currentValue === 'number' &&!isNaN(currentValue)) {
          data.sum += currentValue;
        }
      }
      initMap()

    } else {
      ElMessage.error(res.data.msg || '获取数据失败')
      // 即使API失败，也要初始化地图显示默认状态
      initMap()
    }
  }).catch(error => {
    console.error('API请求失败:', error)
    ElMessage.error('网络请求失败，请检查API服务是否正常')
    // 即使API失败，也要初始化地图显示默认状态
    initMap()
  })

}
const initMap = () => {
  initBarChart("中国",data.provinceData)
  mapInstance.value = echarts.init(mapChart.value)

  console.log("data.provinceData2")
  console.log(data.provinceData)
  const option = {
    title: {
      text: '中国省份人数分布', // 主标题文本
      left: '10', // 标题水平居中
      top: '15', // 标题垂直居上
      textStyle: {
        fontSize: 20, // 主标题字体大小
        fontWeight: 'bold', // 主标题字体加粗
        color: '#595353' // 主标题字体颜色
      }
    },
    visualMap: {
      type: 'continuous',   // 连续型视觉映射
      left: 'right',
      min: 0,
      max: data.sum,
      text: ['高', '低'],   // 两端文本
      realtime: false,
      calculable: true,
      inRange: {
        color: ['#fee5d9', '#fcae91', '#fb6a4a', '#cb181d'] // 蓝系渐变
      }
    },
    geo: {
      zoom: 1.6,
      center: [94.114129, 34.550339],
      map: 'China',
      roam: true,
      label: {
        show: false,
        color: '#333',
        fontSize: 11
      },
      itemStyle: {
        areaColor: '#86b6df',
        borderColor: '#0c0d0e',
        borderWidth: 1, // 正常状态下的边框宽度，增大为 2
      },
      emphasis: {
        label: {
          show: true,
          color: '#900'
        },
        itemStyle: {
          areaColor: '#287fe4'
        }
      },
      regions:data.provinceData.map(p => ({  // 绑定数据染色
        name: p.name,
        itemStyle: {
          areaColor: null  // 由visualMap自动处理颜色
        }
      }))
    },
    series: [{
      type: 'map',
      geoIndex: 0,  // 使用geo组件
      data: data.provinceData  // 数据绑定
    }]
    ,
    graphic: [
      {
        type: 'text',
        left: 190,
        bottom: 10,
        style: {
          text: '审图号：GS（2024）0650号',
          fontSize: 11,
          fill: '#333',
          fontWeight: 'bold',
          lineHeight: 24
        },
        z: 3
      },
      {
        type: 'text',
        left: 20,
        bottom: 120,
        style: {
          text: '全国用户人数:',
          fontSize: 25,
          fill: '#333',
          fontWeight: 'bold',
          lineHeight: 24
        },
        z: 3
      },
      {
        type: "rect",
        top: "19",
        left: function () {
          // 这里需要根据文本宽度动态计算圆形的位置，
          // 实际应用中可能需要根据具体绘图库的方法获取文本宽度
          // 这里简单假设文本宽度为 200，你需要根据实际情况调整
          const textWidth = 200;
          return "10%" + textWidth + 10; // 10 是文本和圆形之间的间距
        },
        shape: {
          width: 10,
          height: 20,
          r:5
        },
        style: {
          fill: "blue"
        }
      },
      {
        type: 'text',
        left: 20, // 根据实际情况调整位置
        bottom: 60,
        style: {
          text: data.sum,
          fontSize: 36,
          fill: '#0c0d0e', // 可以修改为你想要的颜色
          fontWeight: 'bold',
          lineHeight: 24,
          textAlign: 'center' // 让 11 文本居中显示
        },
        z: 3
      }
    ]
  }

  mapInstance.value.setOption(option)
  mapInstance.value.on('click', handleMapClick)

  // 窗口大小变化时自适应
  window.addEventListener('resize', () => mapInstance.value.resize());
}


// 处理地图点击事件
const handleMapClick = (params) => {
  const province = data.ipData.find(item => item.provinceName === params.name);
  if(!province){
    ElMessage.error("该省份暂无数据")
  }else{
    provinceNameStore.setProvinceName(params.name)
    console.log(province)
    const cityData=province ? province.cities : [];
    console.log("cityData")
    console.log(cityData)
    let sum=0;
    for (let i = 0; i < cityData.length; i++) {
      const currentValue = cityData[i].value;
      if (typeof currentValue === 'number' &&!isNaN(currentValue)) {
        sum+= currentValue;
      }
    }
    provinceNameStore.setProvinceNum(sum)
    console.log("nowProvinceNum.value")
    console.log(nowProvinceNum.value)
    showBarChart.value = true
    nextTick(() => {
      initBarChart(params.name,cityData)
    })
  }

}

// 修改后的柱状图初始化
const initBarChart = (provinceName, cityData) => {
  // 销毁之前的实例
  if (barInstance.value) barInstance.value.dispose()

  // 生成排序后的数据
  let data = [...cityData]; // 复制数组以免修改原数据
  const length = data.length;
  
  // 如果数据不足6条，补充空数据
  if (data.length < 6) {
    for (let i = 0; i < 6 - length; i++) {
      data.push({ name: "暂无", value: 0 });
    }
  }

  // 保存当前地图数据到 store
  provinceNameStore.setMapData(data);

  // 排序并截取前6条
  const sortedData = data.sort((a, b) => b.value - a.value).slice(0, 6);
  const names = sortedData.map(item => item.name);
  const values = sortedData.map(item => item.value);

  barInstance.value = echarts.init(barChart.value);

  const option = {
    title: {
      text: provinceName + '城市人数分布',
      left: '10',
      top: '15',
      textStyle: {
        fontSize: 20,
        fontWeight: 'bold',
        color: '#595353'
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      boundaryGap: [0, 0.01]
    },
    yAxis: {
      type: 'category',
      data: names
    },
    series: [
      {
        name: '人数',
        type: 'bar',
        data: values,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#2378f7' },
              { offset: 0.7, color: '#2378f7' },
              { offset: 1, color: '#83bff6' }
            ])
          }
        }
      }
    ]
  };

  barInstance.value.setOption(option);
  
  // 窗口大小变化时自适应
  window.addEventListener('resize', () => barInstance.value.resize());
}

// 组件挂载时加载数据
onMounted(() => {
  load()
})

</script>

<style scoped>
.container {
  display: flex;
  height: 100%;
  width: 100%;
}

.left-panel {
  flex:5;
  height: 100%;
  width: 100%;
  margin-right: 15px;
  border-radius: 8px;
}

.right-panel {
  flex: 2;
  height: 100%;
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  position: relative;
}

.chart {

}

.placeholder {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #999;
  text-align: center;
}

@media (max-width: 1200px) {
  .container {
    flex-direction: column;
  }

  .left-panel,
  .right-panel {
    margin: 0 0 20px 0;
    min-width: auto;
  }
}
</style>