<template>
  <div class="common-layout">
    <el-container>
      <el-header>Header</el-header>
      <router-view/>
      <el-footer>Footer</el-footer>
    </el-container>
  </div>
</template>


<style lang="scss">
.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
</style>
