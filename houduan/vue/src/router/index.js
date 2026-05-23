import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // { path: '/', redirect: '/manager/dataTotal' },
    { path: '/', redirect: '/login' },  
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      children: [
        { path: 'home', meta: { name: '系统首页' }, component: () => import('@/views/manager/Home.vue'),  },
        { path: 'admin', meta: { name: '管理员信息' }, component: () => import('@/views/manager/Admin.vue'), },
        { path: 'notice', meta: { name: '系统公告' }, component: () => import('@/views/manager/Notice.vue'), },
        { path: 'person', meta: { name: '个人资料' }, component: () => import('@/views/manager/Person.vue'), },
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/manager/Password.vue'), },
        { path: 'picture', meta: { name: '图片' }, component: () => import('@/views/manager/Picture.vue'), },
        { path: 'comment', meta: { name: '评论' }, component: () => import('@/views/manager/Comment.vue'), },
        { path: 'collect', meta: { name: '收藏' }, component: () => import('@/views/manager/Collect.vue'), },
        { path: 'sharedPicture', meta: { name: '分享图片' }, component: () => import('@/views/manager/SharedPicture.vue'), },
        { path: 'user', meta: { name: '用户' }, component: () => import('@/views/manager/User.vue'), },
        { path: 'dataTotal', meta: { name: '数据统计' }, component: () => import('@/views/manager/dataTotal.vue'), },
        { path: 'ciyun', meta: { name: '词云图' }, component: () => import('@/views/manager/ciyun.vue'), },
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue'),  },
        { path: 'person', component: () => import('@/views/front/Person.vue'),  }
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    { path: '/404', component: () => import('@/views/404.vue') },
    { path: '/:pathMatch(.*)', redirect: '/404' }
  ]
})

// 路由守卫：检查登录状态
router.beforeEach((to, from, next) => {
  const user = localStorage.getItem('xm-user')

  // 如果访问登录页或注册页，直接放行
  if (to.path === '/login' || to.path === '/register') {
    next()
    return
  }

  // 如果访问其他页面但没有登录，跳转到登录页
  if (!user) {
    next('/login')
  } else {
    next()
  }
})

export default router