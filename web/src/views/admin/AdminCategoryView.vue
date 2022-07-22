<template>
  <el-container style="padding: 5px 20px">
    <el-main style="padding: 0">

      <!-- 搜索框和查询、新增按钮 -->
      <p>
        <el-button type="success" @click="add">新增</el-button>
      </p>

      <!-- 列表数据 -->
      <el-table :data="categorys" style="width: 100%"
                row-key="id"
                border
      >
        <el-table-column label="名称">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="父分类">
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
      <el-dialog v-loading="loading" v-model="dialogFormVisible" title="分类编辑">
        <el-form :model="form">
          <el-form-item label="名称" :label-width="formLabelWidth">
            <el-input v-model="form.name" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="父分类" :label-width="formLabelWidth">
<!--            <el-input v-model="form.parent" autocomplete="off"/>-->
            <el-select v-model="form.parent" class="m-2" placeholder="Select">
              <el-option
                  key="000"
                  label="无"
                  value="000"
              />
              <el-option
                  v-for="item in categorys"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                  :disabled="item.id === form.id"
              />
            </el-select>
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
import { Tool } from "@/util/tool";

export default ({
  name: 'AdminCategoryView',
  setup() {
    const categorys = ref();

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      axios.get("/category/allList", {
          }
      ).then((response) => {
        // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
        categorys.value = [];
        const data = response.data;
        if (data.success) {
          categorys.value = data.content;
        } else {
          ElMessage.error(data.message);
        }
      });
    };

    interface category {
      id: string
      parent: string
      name: string
      sort: number
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
        saveCategory(form.value);
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
     * @param row
     */
    const saveCategory = (row: category) => {
      loading.value = true;
      axios.post("/category/saveCategory", form.value).then((response) => {
        //获取返回值
        const data = response.data;
        if (data.success) {
          //停止加载动画
          loading.value = false;
          //关闭弹出的表单
          dialogFormVisible.value = false;

          //重新加载列表数据
          handleQuery();

          console.log(categorys.value)
        }
      });
    }

    /**
     * 编辑按钮
     * @param index
     * @param row
     */
    const handleEdit = (index: number, row: category) => {
      dialogFormVisible.value = true;
      loading.value = false;

      form.value = Tool.copy(row);
    }

    /**
     * 新增
     */
    const add = () => {
      dialogFormVisible.value = true;
      form.value = {};
    }

    /**
     * 删除确认对话框
     */
    const openDelete = (index: number, row: category) => {
      ElMessageBox.confirm(
          '删除后无法恢复，确定要删除吗?',
          '警告',
          {
            confirmButtonText: '确认',
            cancelButtonText: '再想想',
            type: 'warning',
          }
      ).then(() => {
        //执行保存操作
        handleDelete(index, row);
        //给出信息提示
        ElMessage({
          type: 'success',
          message: '删除成功',
        })
      }).catch(() => {

        // ElMessage({
        //   type: 'info',
        //   message: 'Delete canceled',
        // })
      })
    }

    /**
     * 删除按钮
     * @param index
     * @param row
     */
    const handleDelete = (index: number, row: category) => {
      console.log(index, row)
      axios.delete("/category/deleteCategory/" + row.id)
          .then((response) => {
            const data = response.data;
            if (data.success) {
              //重新加载列表
              handleQuery();
            }
          });
    };

    return {
      categorys,

      handleEdit,
      add,

      openDelete,

      dialogFormVisible,
      formLabelWidth,
      form,
      loading,
      openSave,

      handleQuery,
    };
  }

});
</script>