<template>
  <el-container>
    <el-footer>
      <div style="background-color: #545c64;
                  color: #fff;
                  text-align: center;
                  width: 100%;
                  height: 60px;
                  line-height: 60px">
        这是一个footer{{ user.name }}
      </div>

    </el-footer>
  </el-container>
</template>

<script lang="ts">
import {computed} from "vue";
import store from "@/store";

export default {
  name: "TheFooter",
  setup() {
    const user = computed(() => store.state.user);

    return {
      user
    };
  }
}
</script>
