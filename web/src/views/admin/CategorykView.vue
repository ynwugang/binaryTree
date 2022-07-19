<template>
  <el-container style="padding: 5px 20px">
    <el-main style="padding: 0">

      <!-- 搜索框和查询、新增按钮 -->
      <p>
        <el-button type="success" @click="add">新增</el-button>
      </p>

      <!-- 列表数据 -->
      <el-table :data="categorys" style="width: 100%">
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

      <!-- 分页组件 -->
      <el-pagination
          v-model:currentPage="pagination.current"
          v-model:page-size="pagination.pageSize"
          :hide-on-single-page="pagination.hideOnSinglePage"
          :page-sizes="[3, 5, 15, 20]"
          :small="pagination.small"
          :disabled="pagination.disabled"
          :background="pagination.background"
          layout="total, sizes, pager"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />

      <!-- 弹出层-编辑框 -->
      <el-dialog v-loading="loading" v-model="dialogFormVisible" title="分类编辑">
        <el-form :model="form">
          <el-form-item label="名称" :label-width="formLabelWidth">
            <el-input v-model="form.name" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="父分类" :label-width="formLabelWidth">
            <el-input v-model="form.parent" autocomplete="off"/>
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
import {reactive, ref, onMounted} from "vue";
import axios from "axios";
import {ElMessage, ElMessageBox} from 'element-plus'
import { Tool } from "@/util/tool";

export default ({
  name: 'CategoryView',
  setup() {
    const param = ref();
    param.value = {};
    const categorys = ref();
    const pagination = ref({
      current: 1,
      pageSize: 3,
      total: 0,
      small: false,
      background: false,
      disabled: false,
      hideOnSinglePage: false
    });

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      axios.get("/category/list", {
            params: {
              page: params.page,
              size: params.size,
              name: param.value.name
            }
          }
      ).then((response) => {
        // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
        categorys.value = [];
        const data = response.data;
        if (data.success) {
          console.log(data);
          categorys.value = data.content.list;

          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.pageSize = params.size;
          pagination.value.total = data.content.total;
        } else {
          ElMessage.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQuery(
          {
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
    });

    /**
     * 每页显示条数改变
     * @param val
     */
    const handleSizeChange = (val: number) => {
      console.log(`${val} items per page`)
      handleQuery(
          {
            page: pagination.value.current,
            size: val
          }
      )
    }

    /**
     * 页数改变
     * @param val
     */
    const handleCurrentChange = (val: number) => {
      console.log(`current page: ${val}`)
      handleQuery(
          {
            page: val,
            size: pagination.value.pageSize
          }
      )
    }

    interface category {
      id: string
      name: string
      category1Id: string
      category2Id: string
      description: string
      cover: string
      docCount: string
      viewCount: string
      voteCount: string
    }

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
        saveCategory();
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
    const saveCategory = () => {
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
          handleQuery(
              {
                page: pagination.value.current,
                size: pagination.value.pageSize
              }
          );
        }
      });
    }

    /**
     * 编辑按钮
     * @param index
     * @param row
     */
    const handleEdit = (index: number, row: category) => {
      console.log(index, row)
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
              handleQuery({
                page: pagination.value.current,
                size: pagination.value.pageSize
              });
            }
          });
    };

    return {
      categorys,
      pagination,
      handleSizeChange,
      handleCurrentChange,

      handleEdit,
      add,

      openDelete,

      dialogFormVisible,
      formLabelWidth,
      form,
      loading,
      openSave,

      param,
      handleQuery
    };
  }

});
</script>