<template>
  <el-container style="padding: 5px 20px">
    <el-aside width="200px">
      <el-row class="tac">
        <el-col :span="24">
          <el-menu
              active-text-color="#ffd04b"
              background-color="#545c64"
              class="el-menu-vertical-demo"
              default-active="2"
              text-color="#fff"
              @open="handleOpen"
              @close="handleClose"
          >
            <el-sub-menu index="1">
              <template #title>
                <el-icon>
                  <location/>
                </el-icon>
                <span>导航一</span>
              </template>
              <el-menu-item-group title="Group One">
                <el-menu-item index="1-1">item one</el-menu-item>
                <el-menu-item index="1-2">item one</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="Group Two">
                <el-menu-item index="1-3">item three</el-menu-item>
              </el-menu-item-group>
              <el-sub-menu index="1-4">
                <template #title>item four</template>
                <el-menu-item index="1-4-1">item one</el-menu-item>
              </el-sub-menu>
            </el-sub-menu>
            <el-menu-item index="2">
              <el-icon>
                <icon-menu/>
              </el-icon>
              <span>导航二</span>
            </el-menu-item>
            <el-menu-item index="3" disabled>
              <el-icon>
                <document/>
              </el-icon>
              <span>导航三</span>
            </el-menu-item>
            <el-menu-item index="4">
              <el-icon>
                <setting/>
              </el-icon>
              <span>导航四</span>
            </el-menu-item>
          </el-menu>
        </el-col>
      </el-row>
    </el-aside>
    <el-main style="padding: 0">
      <el-card v-for="o in 1" :key="o" class="box-card" style="border: none;" shadow="never">
        <template #header>
          <div class="card-header">
            <span>Card name</span>
          </div>
        </template>

        <el-row>
          <el-col v-for="ebook in ebooks" :key="ebook" :span="8">
            <el-card :body-style="{ paddingTop: '0px', paddingLeft: '0px' }" style="border: none;" shadow="never">
              <div style="display: flex">
                <a :title="ebook.name" href="#" class="icon" style="background-image: url('https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png')"></a>
                <div class="text">
                  <a :title="ebook.name" href="#">{{ ebook.name }}</a>
                  <p>
                    <i><el-icon><Reading /></el-icon></i>
                    <span>{{ ebook.docCount }}</span>
                    <i><el-icon><View /></el-icon></i>
                    <span>{{ ebook.viewCount }}</span>
                    <i><el-icon><Star /></el-icon></i>
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
.text p span{
  font-size: 12px;
  color: #545c63;
  padding-right: 12px;
}
.text p i{
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
import {defineComponent, onMounted, ref, reactive, toRef} from 'vue';
import HelloWorld from '@/components/HelloWorld.vue'; // @ is an alias to /src
import axios from "axios";

export default defineComponent({
  name: 'HomeView',
  components: {
    HelloWorld,
  },
  setup() {
    const ebooks = ref();

    onMounted(() => {
      axios.get("/ebook/list")
          .then((response) => {
            const data = response.data;
            ebooks.value = data.content;
          });
    });

    return {
      ebooks
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
