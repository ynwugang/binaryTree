<template>
  <el-container style="padding: 5px 20px">
    <el-main style="padding: 0">

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
                plain
                @click="handleEdit(scope.$index, scope.row)"
            >编辑
            </el-button
            >
            <el-button
                size="small"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)"
            >删除
            </el-button
            >
          </template>
        </el-table-column>
      </el-table>

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

    </el-main>
  </el-container>
</template>

<script lang="ts">
import {ref, onMounted} from "vue";
import axios from "axios";
import {ElMessage} from 'element-plus'

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
          ebooks.value = data.content.list;

          // 重置分页按钮
          pagination.value.current = params.page;
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

    /**
     * 编辑按钮
     * @param index
     * @param row
     */
    const handleEdit = (index: number, row: ebook) => {
      console.log(index, row.id)
    }

    /**
     * 删除按钮
     * @param index
     * @param row
     */
    const handleDelete = (index: number, row: ebook) => {
      console.log(index, row)
    }

    return {
      ebooks,
      pagination,
      handleSizeChange,
      handleCurrentChange,
      handleEdit,
      handleDelete
    };
  }

});
</script>