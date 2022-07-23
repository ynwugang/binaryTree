<template>
  <el-container style="padding: 5px 20px">
    <el-main style="padding: 0">

      <!-- 搜索框和查询、新增按钮 -->
      <el-form :inline="true" :model="param" class="demo-form-inline">
        <el-form-item style="margin-right: 14px">
          <el-input v-model="param.loginName" placeholder="登录名" clearable style="width: 200px"/>
        </el-form-item>
        <el-form-item style="margin-right: 14px">
          <el-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="add">新增</el-button>
        </el-form-item>
      </el-form>

      <!-- 列表数据 -->
      <el-table :data="users" style="width: 100%" :table-layout="'auto'">
        <el-table-column label="登录名">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.loginName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="名称">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="密码">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.password }}</span>
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
      <el-dialog v-loading="loading" v-model="dialogFormVisible" title="电子书编辑">
        <el-form :model="form">
          <el-form-item label="登录名" :label-width="formLabelWidth">
            <el-input v-model="form.loginName" :disabled="!!form.id" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="昵称" :label-width="formLabelWidth">
            <el-input v-model="form.name" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="密码" :label-width="formLabelWidth" v-show="!form.id">
            <el-input v-model="form.password" autocomplete="off"/>
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

declare let hexMd5: any;
declare let KEY: any;

export default ({
  name: 'AdminUserView',
  setup() {
    const param = ref();
    param.value = {};
    const users = ref();
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
      axios.get("/user/list", {
            params: {
              page: params.page,
              size: params.size,
              loginName: param.value.loginName
            }
          }
      ).then((response) => {
        // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
        users.value = [];
        const data = response.data;
        if (data.success) {
          console.log(data);
          users.value = data.content.list;

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

    interface user {
      id: string
      loginName: string
      name: string
      password: string
    }

    //编辑弹出表单相关
    const dialogFormVisible = ref(false)
    const formLabelWidth = '140px'
    //表单数据
    const form = ref();
    //loading
    const loading = ref(true);

    const options = ref([]);


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
        saveUser();
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
    const saveUser = () => {
      //加载动画
      loading.value = true;

      form.value.password = hexMd5(form.value.password + KEY);

      axios.post("/user/saveUser", form.value)
          .then((response) => {
            console.log("form", form.value);

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
              //给出信息提示
              ElMessage({
                type: 'success',
                message: '保存成功',
              })
            } else {
              ElMessage({
                type: 'error',
                message: data.message,
              })
            }
          });
    }

    /**
     * 编辑按钮
     * @param index
     * @param row
     */
    const handleEdit = (index: number, row: user) => {
      console.log(index, row)
      dialogFormVisible.value = true;
      loading.value = false;

      console.log(form);

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
    const openDelete = (index: number, row: user) => {
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
    const handleDelete = (index: number, row: user) => {
      console.log(index, row)
      axios.delete("/user/deleteUser/" + row.id)
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
      users,
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
      handleQuery,

      options
    };
  }

});
</script>