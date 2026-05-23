<template>
  <div class="dataTotal">
    <div class="common-layout">
      <el-container style="height: 100vh">
        <el-header class="header">
          <h1 style="text-align: center;color: white">用户数据分析</h1></el-header>
        <el-container>
          <el-aside  class="aside">
            <div style="background-color:white;height:94%;margin-top: 7%;  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
            <wordChart></wordChart>
            </div>
          </el-aside>
          <el-container>
            <el-main class="main">
          <wordcloud></wordcloud>

            </el-main>
          </el-container>

        </el-container>
      </el-container>
    </div>
  </div>
</template>

<script setup>

</script>
<style scope>
.header{
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  width: 98.5%;
  background: #63b7fa url("@/assets/imgs/head_bg.png");
  background-size: cover;
  opacity: 0.8;
}
.aside{
  display: flex;
  flex-direction: column;
  width:300px;
}
.main{
  background-color: white;
  margin: 19px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}
.footer{
  display: flex;
  height: 230px;
}
</style>