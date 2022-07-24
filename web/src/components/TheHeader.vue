<template>
  <el-container>
    <el-header>
      <el-menu
          class="el-menu-demo"
          mode="horizontal"
          :ellipsis="false"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          @select="handleSelect"
      >
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/admin/user">用户管理</el-menu-item>
        <el-menu-item index="/admin/ebook">电子书管理</el-menu-item>
        <el-menu-item index="/admin/category">分类管理</el-menu-item>
        <el-menu-item index="/about">关于我们</el-menu-item>
        <div class="flex-grow" />
        <el-menu-item v-show="!userInfo.id" class="login-menu" index="toLogin">登陆</el-menu-item>
        <el-sub-menu v-show="userInfo.id" index="userInfo">
          <template #title>{{ userInfo.name }}</template>
          <el-menu-item index="2-1">item one</el-menu-item>
          <el-menu-item index="2-2">item two</el-menu-item>
          <el-menu-item index="2-3">item three</el-menu-item>
        </el-sub-menu>
      </el-menu>

      <!-- 弹出层-密码重置 -->
      <el-dialog v-loading="loading" v-model="loginVisible" title="用户登陆">
        <el-form :model="loginUser">
          <el-form-item label="登录名" :label-width="formLabelWidth">
            <el-input v-model="loginUser.loginName" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="密码" :label-width="formLabelWidth">
            <el-input type="password" v-model="loginUser.password" autocomplete="off"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="loginVisible = false">取消</el-button>
            <el-button type="primary" @click="login">登陆</el-button>
          </span>
        </template>
      </el-dialog>
    </el-header>
  </el-container>
</template>

<script lang="ts">
import {computed, ref} from 'vue'
import router from "@/router";
import {ElMessage} from "element-plus";
import axios from "axios";
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default {
  name: "the-header",
  setup() {
    const activeIndex = ref('/')
    const handleSelect = (key: string, keyPath: string[]) => {
      if (key === "toLogin") {
        toLogin();
      } else {
        console.log(key, keyPath)
        router.push(key);
      }
    };

    //用户信息
    const userInfo = computed(() => store.state.user)

    //登陆弹出表单相关
    const loginVisible = ref(false)
    const loading = ref(true);
    const formLabelWidth = '140px'
    //表单数据
    const loginUser = ref();
    loginUser.value = {
      loginName: "test",
      password: "abc123456"
    };

    /**
     * 登陆
     * @param index
     * @param row
     */
    const login = () => {

      //停止加载动画
      loading.value = true;

      loginUser.value.password = hexMd5(loginUser.value.password + KEY);

      axios.post("/user/login", loginUser.value)
          .then((response) => {
            //停止加载动画
            loading.value = false;
            //获取返回值
            const data = response.data;
            if (data.success) {
              //给store赋值即可
              store.commit("setUser", userInfo.value);
              //关闭弹出的表单
              loginVisible.value = false;
              //给出信息提示
              ElMessage({
                type: 'success',
                message: '登陆成功',
              })
            } else {
              loginUser.value.password = null

              ElMessage({
                type: 'error',
                message: data.message,
              })
            }
          });
    }

    /**
     * 打开登陆窗口
     */
    const toLogin = () => {
      loginVisible.value = true;
    }

    return {
      activeIndex,
      handleSelect,

      loginVisible,
      formLabelWidth,
      loginUser,
      toLogin,
      login,
      loading,
      userInfo
    }
  }
}
</script>

<style>
.flex-grow {
  flex-grow: 1;
}
</style>