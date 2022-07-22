<template>
  <el-container style="padding: 5px 20px">
    <el-main style="padding: 0">
      <el-row>
        <el-col :span="6">
          <el-tree
              :data="docs"
              node-key="id"
              :props="{ label: 'name', children: 'children' }"
              @node-click="handleNodeClick"
              default-expand-all
              :expand-on-click-node="false"
          />
        </el-col>
        <el-col :span="18">
          <div>
            <h1>文档内容</h1>
          </div>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>


<script lang="ts">
import '@wangeditor/editor/dist/css/style.css' // 引入 css

import {ref, onMounted, shallowRef, onBeforeUnmount} from "vue";
import axios from "axios";
import {ElMessage, ElMessageBox} from 'element-plus'
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";

import {Editor, Toolbar} from '@wangeditor/editor-for-vue'

export default ({
  name: 'DocView',
  components: {
    Editor,
    Toolbar
  },
  setup() {
    //doc模板
    interface doc {
      id: string
      ebookId: string
      parent: string
      name: string
      sort: number
      viewCount: number
      voteCount: number
      content: string
    }

    //doc数据
    const docs = ref();
    //路由route
    const route = useRoute();
    //电子书ID
    const ebookId = route.params.ebookId;


    /**
     * 数据查询
     **/
    const handleQuery = () => {
      axios.get(`/doc/allList/${ebookId}`)
          .then((response) => {
            // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
            docs.value = [];
            const data = response.data;
            if (data.success) {
              docs.value = Tool.array2Tree(data.content, '0')
              console.log("docs.value", docs.value);
            } else {
              ElMessage.error(data.message);
            }
          });
    };

    /**
     * 节点点击事假
     * @param data
     */
    const handleNodeClick = (data: doc) => {
      console.log(data)
    }

    onMounted(() => {
      handleQuery();
    });


    return {
      docs,
      handleNodeClick
    };
  }

});
</script>
