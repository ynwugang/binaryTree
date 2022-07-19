<template>
  <el-container style="padding: 5px 20px">
    <el-main style="padding: 0">

      <!-- 搜索框和查询、新增按钮 -->
      <el-form :inline="true" :model="param" class="demo-form-inline">
        <el-form-item style="margin-right: 14px">
          <el-input v-model="param.name" placeholder="名称" clearable style="width: 200px"/>
        </el-form-item>
        <el-form-item style="margin-right: 14px">
          <el-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="add">新增</el-button>
        </el-form-item>
      </el-form>

      <!-- 列表数据 -->
      <el-table
          :data="ebooks"
          style="width: 100%"
          :table-layout="'auto'"
      >
        <el-table-column label="封面">
          <template #default="scope">
            <div style="display: flex; align-items: center; margin-left: 16px">
              <img
                  :title="scope.row.id"
                  style="width: 42px; height: 42px; border-radius: 5px;"
                  src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
              >
            </div>
          </template>
        </el-table-column>
        <el-table-column label="名称">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="分类一">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.category1Id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="分类二">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.category2Id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="文档数">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.docCount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="阅读数">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.viewCount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="点赞数">
          <template #default="scope">
            <span style="margin-left: 10px">{{ scope.row.voteCount }}</span>
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
          <el-form-item label="封面" :label-width="formLabelWidth">
            <el-input v-model="form.cover" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="名称" :label-width="formLabelWidth">
            <el-input v-model="form.name" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="分类一" :label-width="formLabelWidth">
            <el-input v-model="form.category1Id" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="分类二" :label-width="formLabelWidth">
            <el-input v-model="form.category2Id" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="描述" :label-width="formLabelWidth">
            <el-input v-model="form.description" autocomplete="off"/>
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
  name: 'EbookView',
  setup() {
    const param = ref();
    param.value = {};
    const ebooks = ref();
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
      axios.get("/ebook/list", {
            params: {
              page: params.page,
              size: params.size,
              name: param.value.name
            }
          }
      ).then((response) => {
        // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
        ebooks.value = [];
        const data = response.data;
        if (data.success) {
          console.log(data);
          ebooks.value = data.content.list;

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

    interface ebook {
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
        saveEbook();
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
    const saveEbook = () => {
      loading.value = true;
      axios.post("/ebook/saveEbook", form.value).then((response) => {
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
    const handleEdit = (index: number, row: ebook) => {
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
    const openDelete = (index: number, row: ebook) => {
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
    const handleDelete = (index: number, row: ebook) => {
      console.log(index, row)
      axios.delete("/ebook/deleteEbook/" + row.id)
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
      ebooks,
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