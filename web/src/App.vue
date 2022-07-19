<template>
  <div class="common-layout">
    <TheHeader/>
    <router-view/>
    <TheFooter/>
  </div>
</template>

<script lang="ts">
import TheHeader from '@/components/TheHeader.vue';
import TheFooter from '@/components/TheFooter.vue';

export default {
  name: 'app',
  components: {
    TheHeader,
    TheFooter,
  },
};
</script>

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


