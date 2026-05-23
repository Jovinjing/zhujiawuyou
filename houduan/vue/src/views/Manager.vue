<template>
  <div class="manager-container">
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="http://lzkai.oss-cn-guangzhou.aliyuncs.com/avatar/IMG_20250403_115429.png" alt="" style="height: 60px;width: 60px">
        <div class="title">后台管理</div>
      </div>
      <div class="manager-header-center">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ router.currentRoute.value.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="manager-header-right">
        <el-dropdown style="cursor: pointer">
          <div style="padding-right: 20px; display: flex; align-items: center">
            <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.adminAvatar" alt="">
            <span style="margin-left: 5px; color: white">{{ data.user.adminName }}</span><el-icon color="#fff"><arrow-down /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <!-- 下面部分开始 -->
    <div style="display: flex">
      <div class="manager-main-left">
        <el-menu :default-active="router.currentRoute.value.path"
                 :default-openeds="['1', '2']"
                 router
        >
          <el-sub-menu index="0">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>数据统计</span>
            </template>
            <el-menu-item index="/manager/dataTotal">用户数据可视化</el-menu-item>
            <el-menu-item index="/manager/ciyun">词云图</el-menu-item>
          </el-sub-menu>
<!--          <el-menu-item index="/manager/dataTotal">-->
<!--            <i class="el-icon-s-home"></i>-->
<!--            <span slot="title">系统首页</span>-->
<!--          </el-menu-item>-->
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/picture">图片管理</el-menu-item>
            <el-menu-item index="/manager/sharedPicture">分享图片管理</el-menu-item>
            <el-menu-item index="/manager/comment">评论管理</el-menu-item>
            <el-menu-item index="/manager/collect">收藏管理</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/user">用户信息</el-menu-item>
          </el-sub-menu>

        </el-menu>
      </div>
      <div class="manager-main-right">
        <RouterView @updateUser="updateUser" />
      </div>
    </div>
    <!-- 下面部分结束 -->


  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}')
})

const logout = () => {
  localStorage.removeItem('xm-user')
  router.push('/login')
}

const updateUser = () => {
  data.user =  JSON.parse(localStorage.getItem('xm-user') || '{}')
}

if (!data.user.id) {
  logout()
  ElMessage.error('请登录！')
}
</script>

<style scoped>
  @import "@/assets/css/manager.css";
</style>