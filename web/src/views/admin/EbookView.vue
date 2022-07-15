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
          :hide-on-single-page="true"
          :small="true"
          :page-size="20"
          layout="prev, pager, next"
          :total="1000"
          style="float: right"
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
      pageSize: 10,
      total: 0
    });

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      ebooks.value = [];
      axios.get("/ebook/list"
          //     , {
          //   params: {
          //     page: params.page,
          //     size: params.size,
          //     name: param.value.name
          //   }
          // }
      ).then((response) => {
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content;

          // // 重置分页按钮
          // pagination.value.current = params.page;
          // pagination.value.total = data.content.total;
        } else {
          ElMessage.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQuery();
    });

    interface User {
      id: string
      name: string
      category1Id: string
      category2Id: string
      description: string
      cover: string
      docCount: string
      viewCount: string
      voteCount: string
      // date: string
      // name: string
      // address: string
    }

    const handleEdit = (index: number, row: User) => {
      console.log(index, row)
    }
    const handleDelete = (index: number, row: User) => {
      console.log(index, row)
    }

    return {
      ebooks
    };
  }

});
</script>