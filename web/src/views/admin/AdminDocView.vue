<template>
  <el-container style="padding: 5px 20px">
    <el-main style="padding: 0; height: 800px">
      <el-row>
        <el-col :span="8">
          <!-- 搜索框和查询、新增按钮 -->
          <p>
            <el-button type="success" @click="add">新增</el-button>
          </p>

          <!-- 列表数据 -->
          <el-table :data="docs"
                    style="width: 100%"
                    row-key="id"
                    default-expand-all
          >
            <el-table-column label="名称">
              <template #default="scope">
                <span style="margin-left: 10px">{{ scope.row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button
                    size="small"
                    type="primary"
                    @click="handleEdit(scope.$index, scope.row)"
                >编辑
                </el-button>
                <el-button
                    size="small"
                    type="danger"
                    @click="openDelete(scope.$index, scope.row)"
                >删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="16">

          <p>
            <el-button type="primary" @click="openSave">保存</el-button>
          </p>

          <el-form :model="form">
            <el-form-item>
              <el-input placeholder="请输入文档名称" v-model="form.name" autocomplete="off"/>
            </el-form-item>
            <el-form-item>
              <el-tree-select
                  style="width: 100%"
                  placeholder="请选择父文档"
                  v-model="form.parent"
                  :data="treeSelectData"
                  check-strictly
                  :render-after-expand="false"
                  :props="{ label: 'name', value: 'id', children: 'children', disabled: 'disabled' }"
              />
            </el-form-item>
            <el-form-item>
              <el-input placeholder="请输入序号" v-model="form.sort" autocomplete="off"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" plain @click="handlePreviewContent">内容预览</el-button>
            </el-form-item>
            <el-form-item>
              <div id="content" style="width: 100%">
                <Toolbar
                    style="border-bottom: 1px solid #ccc"
                    :editor="editorRef"
                    :defaultConfig="toolbarConfig"
                    :mode="mode"
                />
                <Editor
                    style="height: 400px;
                    overflow-y: hidden;"
                    v-model="valueHtml"
                    :defaultConfig="editorConfig"
                    :mode="mode"
                    @onCreated="handleCreated"
                />
              </div>
            </el-form-item>
          </el-form>

        </el-col>
      </el-row>

      <!-- 抽屉组件，用于文档预览 -->
      <el-drawer size="80%" v-model="drawer">
        <div class="editor-content-view" :innerHTML="previewHtml"></div>
      </el-drawer>
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
  name: 'AdminDocView',
  components: {
    Editor,
    Toolbar
  },
  setup() {
    const docs = ref();
    const route = useRoute();
    //电子书ID
    const ebookId = route.params.ebookId;
    //表单数据
    const form = ref();
    form.value = {}
    //文档树
    const treeSelectData = ref();
    treeSelectData.value = [];

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
              // docs.value = data.content;
              docs.value = Tool.array2Tree(data.content, '0')

              add();
            } else {
              ElMessage.error(data.message);
            }
          });
    };

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

    // 编辑器实例，必须用 shallowRef
    const editorRef = shallowRef()

    // 内容 HTML
    const valueHtml = ref()

    /**
     * 查询文档内容
     * @param id
     */
    const handleQueryContent = (id: string) => {
      axios.get(`/doc/content/${id}`)
          .then((response) => {
            const data = response.data;
            if (data.success) {
              valueHtml.value = data.content;
            } else {
              ElMessage.error(data.message);
            }
          })
    }

    const toolbarConfig = {}
    const editorConfig = {placeholder: '请输入内容...'}

    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value
      if (editor == null) return
      editor.destroy()
    })

    const handleCreated = (editor: any) => {
      editorRef.value = editor // 记录 editor 实例，重要！
    }


    //保存确认对话框
    const openSave = () => {
      ElMessageBox.confirm(
          '确认保存吗?',
          '提示',
          {
            confirmButtonText: '确认',
            cancelButtonText: '再想想',
            type: 'warning',
          }
      ).then(() => {
        form.value.content = editorRef.value.getHtml();
        //执行保存操作
        saveDoc();
        //给出信息提示
        ElMessage({
          type: 'success',
          message: '保存成功',
        })
      }).catch(() => {

        // ElMessage({
        //   type: 'info',
        //   message: 'Delete canceled',
        // })
      })
    }

    /**
     * 保存修改
     * @param index
     */
    const saveDoc = () => {
      axios.post("/doc/saveDoc", form.value).then((response) => {
        //获取返回值
        const data = response.data;
        if (data.success) {
          //重新加载列表数据
          handleQuery();
        }
      });
    }

    /**
     * 将某个节点及其子孙节点全部设置disabled为true
     * @param treeData
     * @param id
     */
    const setDisable = (treeData: any, id: any) => {
      //遍历数组
      for (let i = 0; i < treeData.length; i++) {
        const node = treeData[i];
        //找到目标节点
        if (node.id === id) {
          //将目标节点设置为disabled
          node.disabled = true;

          //遍历子节点，将所有子节点都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          //如果当前节点不是目标节点，则到其子节点中寻找
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id)
          }
        }
      }
    };

    /**
     * 编辑按钮
     * @param index
     * @param row
     */
    const handleEdit = (index: number, row: doc) => {
      //清空文档内容
      valueHtml.value = '<p><br></p>';
      //查询文档内容
      handleQueryContent(row.id);

      form.value = Tool.copy(row);
      // 为选择树赋值
      treeSelectData.value = Tool.copy(docs.value);
      // 设置不能选择的节点
      setDisable(treeSelectData.value, row.id);
      // 为选择树添加一个“无”
      treeSelectData.value.unshift({id: "0", name: "无"});
    }

    /**
     * 新增
     */
    const add = () => {
      //清空文档内容
      valueHtml.value = '<p><br></p>';
      //电子书id赋值
      form.value = {
        ebookId: ebookId
      };

      console.log("ebookId", ebookId)
      console.log("form", form.value)
      // 为选择树赋值
      treeSelectData.value = Tool.copy(docs.value);

      if (Tool.isEmpty(treeSelectData.value)) {
        treeSelectData.value = [{id: "0", name: "无"}]
        // treeSelectData.value.push({id: "0", name: "无"});
      } else {
        // 为选择树添加一个“无”
        treeSelectData.value.unshift({id: "0", name: "无"});
      }
    }

    /**
     * 删除确认对话框
     */
    const openDelete = (index: number, row: doc) => {
      ElMessageBox.confirm(
          '删除会将下面的子文档一同删除，并且删除后无法恢复，确定要删除吗?',
          '警告',
          {
            confirmButtonText: '确认',
            cancelButtonText: '再想想',
            type: 'warning',
          }
      ).then(() => {
        //存储删除的id
        const ids: string[] = [];
        //存储删除的文档名称
        const names: string[] = [];
        //获取当前节点及其子孙节点的id
        getDeleteIds(row, row.id, ids, names);
        //再次提示
        openDelete2(ids, names);
      }).catch(() => {

        // ElMessage({
        //   type: 'info',
        //   message: 'Delete canceled',
        // })
      })
    }

    /**
     * 获取指定节点及其子节点的ID
     * @param treeData
     * @param id
     */
    const getDeleteIds = (treeData: any, id: any, ids: string[], names: string[]) => {
      //将当前节点的id存入数组
      ids.push(id);
      //存储文档名称
      names.push(treeData.name)
      //获取当前节点的子节点
      const children = treeData.children;
      //若子节点存在，则遍历子节点，递归调用该方法
      if (Tool.isNotEmpty(children)) {
        //遍历子节点对象
        for (let j = 0; j < children.length; j++) {
          //递归调用，将每一个子节点，作为参数传递
          getDeleteIds(children[j], children[j].id, ids, names)
        }
      }
    }

    /**
     * 删除功能的二次提示
     * @param ids
     * @param names
     */
    const openDelete2 = (ids: string[], names: string[]) => {
      //将要删除的文档名称处理
      let nameStr = "";
      names.forEach((item) => {
        nameStr += item + "，";
      })
      nameStr = nameStr.substring(0, nameStr.length - 1);

      //消息框
      ElMessageBox.confirm(
          '即将删除：[' + nameStr + ']，删除后不可恢复，确认删除吗?',
          '再次警告',
          {
            confirmButtonText: '确认',
            cancelButtonText: '再想想',
            type: 'warning',
          }
      ).then(() => {
        //执行删除操作
        handleDelete(ids);
        //给出信息提示
        ElMessage({
          type: 'success',
          message: '删除成功',
        })
      }).catch(() => {
        console.log("二次提示点击取消")
      })
    }

    /**
     * 删除按钮
     * @param index
     * @param row
     */
    const handleDelete = (ids: string[]) => {
      axios.delete("/doc/deleteDoc/" + ids)
          .then((response) => {
            const data = response.data;
            if (data.success) {
              //重新加载列表
              handleQuery();
            }
          });
    };

    // ---------富文本预览--------
    //抽屉显示
    const drawer = ref(false);
    //预览内容
    const previewHtml = ref();

    //内容预览按钮点击事件
    const handlePreviewContent = () => {
      const html = editorRef.value.getHtml();
      previewHtml.value = html;
      drawer.value = true;
    }

    onMounted(() => {
      //查询数据
      handleQuery()
    });

    return {
      docs,

      handleEdit,
      add,

      openDelete,

      form,
      openSave,

      handleQuery,

      treeSelectData,

      editorRef,
      valueHtml,
      mode: 'default', // 或 'simple'
      toolbarConfig,
      editorConfig,
      handleCreated,

      handlePreviewContent,
      drawer,
      previewHtml
    };
  }

});
</script>