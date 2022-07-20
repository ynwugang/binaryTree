<template>
  <el-container style="padding: 5px 20px">
    <el-aside width="200px">
      <el-row class="tac">
        <el-col :span="24">


          <el-menu
              active-text-color="#ffd04b"
              background-color="#545c64"
              class="el-menu-vertical-demo"
              default-active="0"
              text-color="#fff"
              @open="handleOpen"
              @close="handleClose"
              @select="handleSelect"
          >
            <el-menu-item index="/">
              <el-icon><Menu /></el-icon>
              <span>欢迎</span>
            </el-menu-item>

            <el-sub-menu v-for="item in categoryInfo" :key="item.id" :index="'1-' + item.id">
              <template #title>
                <span>{{ item.name }}</span>
              </template>
              <el-menu-item
                  v-for="child in item.children"
                  :key="child.id"
                  :index="'2-' + child.id"
              >{{ child.name }}
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-col>
      </el-row>
    </el-aside>
    <el-main style="padding: 0">
      <div class="welcome" v-show="showWelcome">
        <h1>欢迎来到二叉树知识库</h1>
      </div>

      <el-card v-show="!showWelcome" v-for="o in 1" :key="o" class="box-card" style="border: none;" shadow="never">
        <template #header>
          <div class="card-header">
            <span>Card name</span>
          </div>
        </template>

        <el-row>
          <el-col v-for="ebook in ebooks" :key="ebook" :span="8">
            <el-card :body-style="{ paddingTop: '0px', paddingLeft: '0px' }" style="border: none;" shadow="never">
              <div style="display: flex">
                <a :title="ebook.name" href="#" class="icon"
                   style="background-image: url('https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png')"></a>
                <div class="text">
                  <a :title="ebook.name" href="#">{{ ebook.name }}</a>
                  <p>
                    <i>
                      <el-icon>
                        <Reading/>
                      </el-icon>
                    </i>
                    <span>{{ ebook.docCount }}</span>
                    <i>
                      <el-icon>
                        <View/>
                      </el-icon>
                    </i>
                    <span>{{ ebook.viewCount }}</span>
                    <i>
                      <el-icon>
                        <Star/>
                      </el-icon>
                    </i>
                    <span>{{ ebook.voteCount }}</span>
                  </p>
                </div>
              </div>
              <div class="bottom-con">
                {{ ebook.description }}
              </div>
            </el-card>
          </el-col>
        </el-row>

      </el-card>
    </el-main>
  </el-container>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.box-card {
  width: 956px;
}

.time {
  font-size: 12px;
  color: #999;
}

.text {
  width: 222px;
  padding-left: 14px;
}

.text a {
  display: block;
  font-weight: 700;
  font-size: 16px;
  line-height: 24px;
  color: #000;
  cursor: pointer;
  margin-bottom: 2px;
  text-decoration: none;
}

.text p {
  margin: 0;
  height: 24px;
  display: flex;
  -webkit-box-orient: horizontal;
  -webkit-box-direction: normal;
  flex-direction: row;
  -webkit-box-pack: start;
  justify-content: flex-start;
  -webkit-box-align: center;
  align-items: center;
}

.text p span {
  font-size: 12px;
  color: #545c63;
  padding-right: 12px;
}

.text p i {
  font-size: 14px;
  color: #545c63;
  padding-right: 2px;
}

.bottom-con {
  font-size: 12px;
  color: #909090;
  line-height: 20px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.icon {
  width: 42px;
  height: 42px;
  display: block;
  background-position: 50%;
  background-repeat: no-repeat;
  background-size: cover;
  cursor: pointer;
  align-self: center;
}
</style>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import HelloWorld from '@/components/HelloWorld.vue'; // @ is an alias to /src
import axios from "axios";
import {ElMessage} from "element-plus/es";

export default defineComponent({
  name: 'HomeView',
  components: {
    HelloWorld,
  },
  setup() {
    const ebooks = ref();

    const categoryInfo = ref();

    //查询电子书列表
    const queryEbookList = (param: string) => {
      axios.get("/ebook/list",
          {params: {
            categoryId: param
            }}
      ).then((response) => {
            const data = response.data;
            ebooks.value = data.content.list;
          });
    };

    //查询分类信息
    const queryCategoryInfo = () => {
      axios.get("/category/allList", {}
      ).then((response) => {
        const data = response.data;
        if (data.success) {
          categoryInfo.value = data.content;
        } else {
          ElMessage.error(data.message);
        }
      });
    };

    const showWelcome = ref(true);

    const handleOpen = (key: string, keyPath: string[]) => {
      showWelcome.value = false;
      console.log("handleOpen: " + key, keyPath)
      queryEbookList(key);
    }
    const handleClose = (key: string, keyPath: string[]) => {
      console.log("handleClose: " + key, keyPath)
    }
    const handleSelect = (key: string, keyPath: string[]) => {
      showWelcome.value = false;
      console.log("handleSelect: " + key, keyPath)
      queryEbookList(key);
    }

    onMounted(() => {
      queryCategoryInfo();
      // queryEbookList(null);
    });

    return {
      ebooks,
      categoryInfo,
      handleOpen,
      handleClose,
      handleSelect,
      showWelcome
    }
  },
});
</script>

<!--<script lang="ts" setup>-->
<!--import {-->
<!--  Document,-->
<!--  Menu as IconMenu,-->
<!--  Location,-->
<!--  Setting,-->
<!--} from '@element-plus/icons-vue'-->

<!--const handleOpen = (key: string, keyPath: string[]) => {-->
<!--  //console.log(key, keyPath)-->
<!--}-->
<!--const handleClose = (key: string, keyPath: string[]) => {-->
<!--  //console.log(key, keyPath)-->
<!--}-->

<!--//当前系统时间-->
<!--const currentDate = ref(new Date());-->

<!--//获取需要显示的电子书-->
<!--const ebookList = () => {-->
<!--  const ebooks = ref();-->

<!--  onMounted(() => {-->
<!--    console.log("onMounted");-->
<!--    axios.get("http://localhost:8080/ebook/list")-->
<!--        .then((response) => {-->
<!--          const data = response.data;-->
<!--          ebooks.value = data.content;-->
<!--          console.log(response);-->
<!--        });-->
<!--  });-->

<!--  return {-->
<!--    ebooks-->
<!--  }-->
<!--}-->
<!--</script>-->
