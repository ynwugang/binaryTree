<template>
  <el-container>
    <el-aside width="200px">Aside
      <el-row>
        <el-col style="background-color: red" :span="24">
          <div class="grid-content ep-bg-purple-dark"/>111
        </el-col>
      </el-row>
      <el-row>
        <el-col style="background-color: red" :span="24">
          <div class="grid-content ep-bg-purple-dark"/>222
        </el-col>
      </el-row>
    </el-aside>
    <el-main>Main</el-main>
  </el-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import HelloWorld from '@/components/HelloWorld.vue'; // @ is an alias to /src

export default defineComponent({
  name: 'HomeView',
  components: {
    HelloWorld,
  },
});
</script>
