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

//element-plus 国际化
import zhCn from 'element-plus/es/locale/lang/zh-cn'

//修改axios的baseURL为配置文件中定义的服务端地址
axios.defaults.baseURL = process.env.VUE_APP_SERVER;

/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config){
    console.log('请求参数：', config);
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response){
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    return Promise.reject(error);
});

const app = createApp(App)

app.use(store).use(router).use(ElementPlus, {locale: zhCn})
app.mount('#app')

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

console.log("环境：", process.env.NODE_ENV);
console.log("服务端地址：", process.env.VUE_APP_SERVER);