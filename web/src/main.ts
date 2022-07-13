import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// element plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// element plus icon图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

//axios
import axios from "axios";

//修改axios的baseURL为配置文件中定义的服务端地址
axios.defaults.baseURL = process.env.VUE_APP_SERVER;

const app = createApp(App)

app.use(store).use(router).use(ElementPlus)
app.mount('#app')

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

console.log("环境：", process.env.NODE_ENV);
console.log("服务端地址：", process.env.VUE_APP_SERVER);