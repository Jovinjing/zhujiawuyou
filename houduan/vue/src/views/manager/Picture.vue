<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入名称查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
<!--      <el-button type="primary" plain @click="handleAdd">新增</el-button>-->
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="userName" label="用户名" />
        <el-table-column prop="oldPicture" label="生成前的图">
          <template v-slot="scope">
            <el-image style="width: 100px; height: 100px; border-radius:10%; display: block" v-if="scope.row.oldPicture"
                      :src="scope.row.oldPicture" :preview-src-list="[scope.row.oldPicture]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="newPicture" label="生成后的图">
          <template v-slot="scope">
            <el-image style="width: 100px; height: 100px; border-radius: 10%; display: block" v-if="scope.row.newPicture"
                      :src="scope.row.newPicture" :preview-src-list="[scope.row.newPicture]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="style" label="风格" />
        <el-table-column prop="roomStyle" label="房间类型" />
        <el-table-column prop="createTime" label="生成时间" />
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
<!--            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>-->
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="管理员信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="data.form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item prop="avatar" label="头像">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="handleFileUpload"
              list-type="picture"
              >
            <el-button type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="姓名">
          <el-input v-model="data.form.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="data.form.phone" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="data.form.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 4,
  total: 0,
  name: "",
  ids: []
})

const load = () => {
  request.post('/picture/selectPage?pageNum='+data.pageNum+'&pageSize='+data.pageSize, {
    userName:data.name
  }).then(res => {
    if (res.data.code === '200') {
      console.log(res.data.data.records)

      data.tableData = res.data.data?.records || []
      data.total = res.data.data?.total
    }
  })
}
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const save = () => {
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/picture/delete/' + id).then(res => {
      if (res.data.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.data.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}
const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete("/picture/delete/batch", {data: data.ids}).then(res => {
      if (res.data.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.data.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}
const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const handleFileUpload = (res) => {
  data.form.avatar = res.data
}

const reset = () => {
  data.name = ""
  load()
}

load()
</script>