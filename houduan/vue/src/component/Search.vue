<script setup>
import {computed, reactive,ref} from 'vue'
import {useProvinceNameStore} from "@/store/userStore.js";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import axios from "axios";
const provinceNameStore = useProvinceNameStore();
const provinceName = computed(() => provinceNameStore.provinceName)
const ageData=computed(() => provinceNameStore.pieAgeData)
const genderData=computed(() => provinceNameStore.pieGenderData)
const lineData=computed(() => provinceNameStore.lineData)
const mapData=computed(() => provinceNameStore.mapData)
const budgetData=computed(() => provinceNameStore.budgetData)
const progress = ref(0);
const data=reactive({
  searchKey:'',

})
const load=()=>{
  progress.value=20;
  // axios
  request({
    method: 'post',
    responseType: 'arraybuffer', // 指定响应类型为blob
    // url: 'http://1.12.238.137:9090/files/downloadWord',
    url: '/files/downloadWord',
    data:  {
        provinceName:provinceName.value,
        ageData:ageData.value,
        areaData:mapData.value,
        genderData:genderData.value,
      seasonData:lineData.value,
      budgetData:budgetData.value,
      }, // 导入文件一般都用FormData格式数据
  }).then(res => {
    progress.value=60;

    console.log(res)
    if (res.status === 200) {
      progress.value=70;

      // 导入成功
      const content = res.data
      const blob = new Blob([content])
      const fileName = provinceName.value+'数据.docx'
      if ('download' in document.createElement('a')) { // 非IE下载
        const elink = document.createElement('a')
        elink.download = fileName
        elink.style.display = 'none'
        elink.href = URL.createObjectURL(blob)
        document.body.appendChild(elink)
        elink.click()
        URL.revokeObjectURL(elink.href) // 释放URL 对象
        document.body.removeChild(elink)
        progress.value=100;

      } else { // IE10+下载
        navigator.msSaveBlob(blob, fileName)
        progress.value=100;

      }
    } else {
      // 输出失败信息
    }
  }).catch(error => {
    console.error('下载失败', error);
  });





  // console.log(provinceName.value)
  // console.log(ageData.value)
  // console.log(genderData.value)
  // console.log(lineData.value)
  // console.log(mapData.value)
  // request.post('/files/downloadWord', {
  //   provinceName:provinceName.value,
  //   ageData:ageData.value,
  //   areaData:mapData.value,
  //   genderData:genderData.value,
  //   seasonData:lineData.value,
  // },).then(res => {
  //   console.log(111)
  //   console.log(res)
  //   if (res.data.code === '200') {
  //     ElMessage.success('操作成功')
  //   } else {
  //     ElMessage.error(res.data.msg)
  //   }
  // })
}
</script>


<template>
  <div class="container">
    <el-input v-model="data.searchKey" prefix-icon="Search" style="width: 240px;margin-top: 10px" placeholder="请输入省份查询"></el-input>
    <el-button type="info" plain @click="provinceNameStore.setProvinceName(data.searchKey)"  style="width: 240px">查询</el-button>
    <el-button type="info" plain @click="provinceNameStore.setProvinceName('中国')"  style="width: 240px;margin-left: 0px">查询全国数据</el-button>

    <el-button type="info" plain @click="load"  style="width: 240px;;margin-left: 0px">获取当前数据分析</el-button>
      <el-progress :percentage="progress"  style="width: 240px;margin-bottom: 10px"/>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  background-color: white;
  height: 100%;
  width: 100%;
  border-radius: 8px;
}
</style>