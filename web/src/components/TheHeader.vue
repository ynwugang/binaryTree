<template>
  <el-container>
    <el-header>
      <el-menu
          class="el-menu-demo"
          mode="horizontal"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          @select="handleSelect"
      >
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/admin/user">用户管理</el-menu-item>
        <el-menu-item index="/admin/ebook">电子书管理</el-menu-item>
        <el-menu-item index="/admin/category">分类管理</el-menu-item>
        <el-menu-item index="4" disabled>Info</el-menu-item>
        <el-menu-item index="/about">关于我们
        </el-menu-item>
      </el-menu>
    </el-header>
  </el-container>
</template>

<script lang="ts">
import {ref} from 'vue'
import router from "@/router";

export default {
  name: "the-header",
  setup() {
    const activeIndex = ref('/')
    const handleSelect = (key: string, keyPath: string[]) => {
      console.log(key, keyPath)
      router.push(key);
    };

    // onMounted(() => {
    //   console.log("path", router.currentRoute);
    //   console.log("value", (router.currentRoute).value);
    //
    // });

    return {
      activeIndex,
      handleSelect
    }
  }
}
</script>
