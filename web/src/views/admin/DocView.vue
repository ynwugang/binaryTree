<template>
  <el-container style="padding: 5px 20px">
    <el-main style="padding: 0">

      <!-- 搜索框和查询、新增按钮 -->
      <p>
        <el-button type="success" @click="add">新增</el-button>
      </p>

      <!-- 列表数据 -->
      <el-table :data="docs" style="width: 100%"
                row-key="id"
                border
      >
        <el-table-column label="名称">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="父文档">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.parent }}</span>
          </template>
        </el-table-column>
        <el-table-column label="排序">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.sort }}</span>
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


      <!-- 弹出层-编辑框 -->
      <el-dialog v-loading="loading" v-model="dialogFormVisible" title="文档编辑">
        <el-form :model="form">
          <el-form-item label="名称" :label-width="formLabelWidth">
            <el-input v-model="form.name" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="父文档" :label-width="formLabelWidth">
            <el-tree-select
                v-model="form.parent"
                :data="treeSelectData"
                check-strictly
                :render-after-expand="false"
                :props="{ label: 'name', value: 'id', children: 'children', disabled: 'disabled' }"
            />

            <!--            <el-select v-model="form.parent" class="m-2" placeholder="Select">-->
            <!--              <el-option-->
            <!--                  key="000"-->
            <!--                  label="无"-->
            <!--                  value="000"-->
            <!--              />-->
            <!--              <el-option-->
            <!--                  v-for="item in docs"-->
            <!--                  :key="item.id"-->
            <!--                  :label="item.name"-->
            <!--                  :value="item.id"-->
            <!--                  :disabled="item.id === form.id"-->
            <!--              />-->
            <!--            </el-select>-->
          </el-form-item>
          <el-form-item label="排序" :label-width="formLabelWidth">
            <el-input v-model="form.sort" autocomplete="off"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取消</el-button>
            <el-button type="primary" @click="openSave">确认</el-button>
          </span>
        </template>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script lang="ts">
import {ref, onMounted} from "vue";
import axios from "axios";
import {ElMessage, ElMessageBox} from 'element-plus'
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";

export default ({
  name: 'DocView',
  setup() {
    const docs = ref();

    const route = useRoute();
    //电子书ID
    const ebookId = route.params.ebookId;

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      axios.get("/doc/allList",
          {
            params: {
              ebookId: ebookId
            }
          }
      ).then((response) => {
        // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
        docs.value = [];
        const data = response.data;
        if (data.success) {
          // docs.value = data.content;
          docs.value = Tool.array2Tree(data.content, '0')

          console.log("docs.value", docs.value);
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
    }


    onMounted(() => {
      handleQuery();
    });

    //编辑弹出表单相关
    const dialogFormVisible = ref(false)
    const formLabelWidth = '140px'
    //表单数据
    const form = ref();
    //loading
    const loading = ref(true);

    const treeSelectData = ref();
    treeSelectData.value = [];


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
      loading.value = true;
      axios.post("/doc/saveDoc", form.value).then((response) => {
        //获取返回值
        const data = response.data;
        if (data.success) {
          //停止加载动画
          loading.value = false;
          //关闭弹出的表单
          dialogFormVisible.value = false;

          //重新加载列表数据
          handleQuery();

          console.log(docs.value)
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
          console.log("目标node：", node);

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
      dialogFormVisible.value = true;
      loading.value = false;

      form.value = Tool.copy(row);

      // 为选择树赋值
      treeSelectData.value = Tool.copy(docs.value);
      // 设置不能选择的节点
      setDisable(treeSelectData.value, row.id);

      console.log("treeSelectData：", treeSelectData)
      // 为选择树添加一个“无”
      treeSelectData.value.unshift({id: "0", name: "无"});
    }

    /**
     * 新增
     */
    const add = () => {
      dialogFormVisible.value = true;
      form.value = {
        ebookId: ebookId
      };

      // 为选择树赋值
      treeSelectData.value = Tool.copy(docs.value);
      // 为选择树添加一个“无”
      treeSelectData.value.unshift({id: "0", name: "无"});
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

        console.log("ids: ", ids);
        console.log("names: ", names);


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
        nameStr+=item + "，";
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

    return {
      docs,

      handleEdit,
      add,

      openDelete,

      dialogFormVisible,
      formLabelWidth,
      form,
      loading,
      openSave,

      handleQuery,

      treeSelectData
    };
  }

});
</script>