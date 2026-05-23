import { createApp } from 'vue'
import { createPinia } from "pinia";
import App from './App.vue'
import router from './router'
import '@/assets/css/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import Map from './component/Map.vue'
import ageAndGender from './component/ageAndGender.vue'
import LineChart from "@/component/LineChart.vue";
import search from "@/component/Search.vue";
import money from "@/component/Money.vue";
import wordcloud from "@/component/wordcloud.vue";
import wordChart from "@/component/wordChart.vue";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
const pinia = createPinia();
app.use(router)
app.use(pinia)
app.use(ElementPlus, { locale: zhCn })

// 注册全局组件
app.component('Map', Map)
app.component('ageAndGender', ageAndGender)
app.component('LineChart', LineChart)
app.component('search', search)
app.component('Budget', money)
app.component('wordcloud', wordcloud)
app.component('wordChart', wordChart)

// 注册 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// 最后挂载应用
app.mount('#app')
