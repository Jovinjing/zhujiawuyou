<template>
  <div ref="chartRef" style="width: 100%; height: 600px;"></div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import request from "@/utils/request.js";

const chartRef = ref(null);
let chartInstance = null;

// 模拟数据
let originalData = [
  { name: '极简风格', value: 35 },
  { name: '奶油风格', value: 40 },
  { name: '北欧风格', value: 90 },
  { name: '美式风格', value: 15 },
  { name: '中式风格', value: 40 },
  { name: '日式风格', value: 15 },
  { name: '田园风格', value: 15 },
  { name: '工业风格', value: 25 },
  { name: '地中海风格', value: 20 },
  { name: '波西米亚风格', value: 30 },
  { name: '现代轻奢风格', value: 45 },
  { name: '法式风格', value: 35 },
  { name: '新中式风格', value: 40 },
  { name: '摩洛哥风格', value: 20 },
  { name: '东南亚风格', value: 25 },
  { name: '复古风格', value: 30 },
  { name: '日式原木风格', value: 35 },
  { name: '后现代风格', value: 25 },
  { name: '轻奢简约风格', value: 40 },
  { name: '英伦风格', value: 30 },
  { name: '韩式风格', value: 20 },
  { name: '意式风格', value: 45 },
  { name: '简约欧式风格', value: 35 },
  { name: '混搭风格', value: 30 },
  { name: '古典风格', value: 25 },
  { name: '新古典风格', value: 35 },


];

// 按数量排序
let sortedData ;

let categories;
let values ;

const initChart = () => {
  const option = {
    graphic:[
      {
        type: "rect",
        top: "25",
        left: "11",
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
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      backgroundColor: 'rgba(255,255,255,0.9)',
      borderColor: '#666',
      borderWidth: 1,
      textStyle: {
        color: '#333'
      }
    },
    title: {
      text: `关键词数量统计`,
      left: '8%',  // 调整标题距离左侧的距离
      top: '4%'
    },
    grid: {
      left: '3%',
      right: '7%',
      bottom: '1%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      boundaryGap: [0, 0.01],
      axisLabel: {
        color: '#666',
        fontSize: 12
      },
      axisLine: {
        lineStyle: {
          color: '#ccc'
        }
      },
      splitLine: {
        lineStyle: {
          color: '#eee'
        }
      }
    },
    yAxis: {
      type: 'category',
      data: categories,
      axisLabel: {
        color: '#666',
        fontSize: 14
      },
      axisLine: {

        lineStyle: {
          color: '#ccc',

        }
      },
      splitLine: {
        show: false
      },
      inverse: true,

    },
    dataZoom: [{
      type: 'inside',
      yAxisIndex: 0,
      zoomLock: true,
      start: 0,
      end: Math.min(100, (7 / originalData.length * 100)), // 初始显示阈值数量的条目
      moveOnMouseWheel: true,
      preventDefaultMouseMove: false,
      filterMode: 'empty'
    }],
    series: [
      {
        barWidth: 20,
        label: {
          show: true,
          position: 'right'  // 数值标签显示在右侧
        },
        name: '数量',
        type: 'bar',
        data: values,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(
              0, 0, 1, 0,
              [
                { offset: 0, color: '#00c0e9' },
                { offset: 1, color: '#3b73cf' }
              ]
          ),
          borderRadius: [4, 4, 4, 4]
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(
                0, 0, 1, 0,
                [
                  { offset: 0, color: '#00c0e9' },
                  { offset: 1, color: '#3b73cf' }
                ]
            )
          }
        }
      }
    ]
  };

  chartInstance.setOption(option);
};
const load=()=>{ request.get('/picture/getStyles').then(res => {
  if (res.data.code === '200') {
    originalData = res.data.data;
    sortedData = originalData.sort((a, b) => b.value - a.value);

    categories = sortedData.map(item => item.name);
    values = sortedData.map(item => item.value);

    initChart();
  } else {
    console.error('获取数据失败:', res.data.message);
  }
})}
onMounted(() => {
  chartInstance = echarts.init(chartRef.value);
  load();

  // 窗口变化自适应
  window.addEventListener('resize', () => {
    chartInstance.resize();
  });
});

onUnmounted(() => {
  window.removeEventListener('resize', () => {
    chartInstance.resize();
  });
  chartInstance.dispose();
});
</script>