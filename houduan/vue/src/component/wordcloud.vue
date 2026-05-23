<template>
  <div ref="chartRef" style="width: 100%; height: 600px;"></div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import 'echarts-wordcloud';
import request from "@/utils/request.js";

const chartRef = ref(null);
let chartInstance = null;

// 生成彩虹色系
const generateColors = () => {
  return new echarts.graphic.LinearGradient(0, 0, 1, 0, [
    { offset: 0, color: '#FF6B6B' },
    { offset: 0.2, color: '#FFD93D' },
    { offset: 0.4, color: '#6C5CE7' },
    { offset: 0.6, color: '#00B894' },
    { offset: 0.8, color: '#F368E0' },
    { offset: 1, color: '#FF9F43' }
  ]);
};

let words = [  { name: '极简风格', value: 35 },
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
]

const getColorByWeight = (weight) => {
  const maxWeight = Math.max(...words.map(word => word.value));
  const minWeight = Math.min(...words.map(word => word.value));
  const range = maxWeight - minWeight;
  const normalizedWeight = (weight - minWeight) / range;

  // 定义蓝色渐变颜色
  const startColor = [150, 150, 255]; // 深蓝色
  const endColor = [173, 216, 230]; // 浅蓝色

  const r = Math.round(startColor[0] + (endColor[0] - startColor[0]) * normalizedWeight);
  const g = Math.round(startColor[1] + (endColor[1] - startColor[1]) * normalizedWeight);
  const b = Math.round(startColor[2] + (endColor[2] - startColor[2]) * normalizedWeight);

  return `rgb(${r}, ${g}, ${b})`;
};

const image ='data:image/png;base64,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'
const maskImage = new Image();
maskImage.src = image

const initChart = () => {
  const option = {
    renderer: 'canvas',

    title: {
      text: '风格词云图',
      top:"20",
      left: 'center',
      textStyle: {
        color: '#0c0d0e',
        fontSize: 34,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      show: true,
      formatter: params => {
        return `${params.name}<br/>数量: ${params.value}`;
      },
      backgroundColor: 'rgba(255,255,255,0.9)',
      borderColor: '#666',
      borderWidth: 1,
      textStyle: {
        color: '#333'
      }
    },
    series: [{
      type: 'wordCloud',
      // maskImage: maskImage,
      left: 'center',
      shape:"circle",
      top:"80",
      width: '100%',
      height: '85%',
      sizeRange: [20, 80],
      rotationRange: [-45, 45],
      rotationStep: 45,
      gridSize: 8,
      drawOutOfBound: false,
      textStyle: {
        // 根据权重动态设置颜色
        color: (params) => getColorByWeight(params.value),
        emphasis: {
          shadowBlur: 10,
          shadowColor: 'rgba(0,0,0,0.3)'
        },
        textBorderColor: '#000',
        textBorderWidth: 1
      },
      data: words.sort(() => Math.random() - 0.5),
      animation: {
        duration: 2000,
        easing: 'cubicOut',
        delay: 100
      }
    }]
  };

  chartInstance.setOption(option);
};
const load=()=>{ request.get('/picture/getStyles').then(res => {
  if (res.data.code === '200') {
    words = res.data.data;
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
