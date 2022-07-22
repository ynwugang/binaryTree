<template>
  <el-container style="padding: 5px 20px">
    <el-main style="padding: 0; height: 800px">
      <h3 v-if="docs.length === 0" style="text-align: center">对不起，找不到相关文档！</h3>
      <el-row>
        <el-col :span="6">
          <el-tree
              :data="docs"
              node-key="id"
              :props="{ label: 'name', children: 'children' }"
              @node-click="handleNodeClick"
              default-expand-all
              :expand-on-click-node="false"
              :default-checked-keys="defaultCheckedKey"
          />
        </el-col>
        <el-col :span="18">
          <div class="editor-content-view" :innerHTML="html"></div>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<style>
.editor-content-view {
  /*border: 3px solid #ccc;*/
  /*border-radius: 5px;*/
  padding: 0 10px;
  margin-top: 20px;
  overflow-x: auto;
}

.editor-content-view p,
.editor-content-view li {
  white-space: pre-wrap; /* 保留空格 */
}

.editor-content-view blockquote {
  border-left: 8px solid #d0e5f2;
  padding: 10px 10px;
  margin: 10px 0;
  background-color: #f1f1f1;
}

.editor-content-view code {
  font-family: monospace;
  background-color: #eee;
  padding: 3px;
  border-radius: 3px;
}
.editor-content-view pre>code {
  display: block;
  padding: 10px;
}

.editor-content-view table {
  border-collapse: collapse;
}
.editor-content-view td,
.editor-content-view th {
  border: 1px solid #ccc;
  min-width: 50px;
  height: 20px;
}
.editor-content-view th {
  background-color: #f1f1f1;
}

.editor-content-view ul,
.editor-content-view ol {
  padding-left: 20px;
}

.editor-content-view input[type="checkbox"] {
  margin-right: 5px;
}
</style>

<script lang="ts">
import '@wangeditor/editor/dist/css/style.css' // 引入 css

import {ref, onMounted} from "vue";
import axios from "axios";
import {ElMessage} from 'element-plus'
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
    docs.value = [];
    //路由route
    const route = useRoute();
    //电子书ID
    const ebookId = route.params.ebookId;
    //内联HTML内容
    const html = ref();
    //默认选择的节点
    const defaultCheckedKey = ref();
    defaultCheckedKey.value = [];

    /**
     * 内容查询
     * @param id
     */
    const handleQueryContent = (id: string) => {
      axios.get(`/doc/content/${id}`)
          .then((response) => {
            const data = response.data;
            if (data.success) {
              html.value = data.content;
            } else {
              ElMessage.error(data.message);
            }
          })
    };

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

              //设置第一个节点为选中状态，查询其内容
              if (Tool.isNotEmpty(docs.value)){
                defaultCheckedKey.value = [docs.value[0].id];
                handleQueryContent(docs.value[0].id)
              }
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
      handleQueryContent(data.id);
    };

    onMounted(() => {
      handleQuery();
    });


    return {
      docs,
      html,
      handleNodeClick,
      defaultCheckedKey
    };
  }

});
</script>
