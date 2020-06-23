import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

const login = r => require.ensure([], () => r(require('@/pages/login')), 'login');

const viewInfo = r => require.ensure([], () => r(require('@/pages/viewInfo')), 'viewInfo');

// ems
const emsAdmin = r => require.ensure([], () => r(require('@/pages/ems/emsAdmin')), 'emsAdmin');
const emsMg = r => require.ensure([], () => r(require('@/pages/ems/emsMg')), 'emsMg');
const emsExaminee = r => require.ensure([], () => r(require('@/pages/ems/emsExaminee')), 'emsExaminee');

// 考试管理
const addExam = r => require.ensure([], () => r(require('@/pages/examMg/addExam')), 'addExam');
const viewExam = r => require.ensure([], () => r(require('@/pages/examMg/viewExam')), 'viewExam');
const regExam = r => require.ensure([], () => r(require('@/pages/examMg/regExam')), 'regExam');

// 教室管理
const addClr = r => require.ensure([], () => r(require('@/pages/clrMg/addClr')), 'addClr');
const viewClr = r => require.ensure([], () => r(require('@/pages/clrMg/viewClr')), 'viewClr');

// 考生管理
const addExaminee = r => require.ensure([], () => r(require('@/pages/examineeMg/addExaminee')), 'addExaminee');
const viewExaminee = r => require.ensure([], () => r(require('@/pages/examineeMg/viewExaminee')), 'viewExaminee');

// 考试信息
const viewExamInfo = r => require.ensure([], () => r(require('@/pages/examInfo/viewExamInfo')), 'viewExamInfo');
const regExamOnline = r => require.ensure([], () => r(require('@/pages/examInfo/regExamOnline')), 'regExamOnline');
const viewRegPrice = r => require.ensure([], () => r(require('@/pages/examInfo/viewRegPrice')), 'viewRegPrice');

// 个人信息
const editInfo = r => require.ensure([], () => r(require('@/pages/personalInfo/editInfo')), 'editInfo');

// 错误页面
const error4xx = r => require.ensure([], () => r(require('@/pages/error/error4xx')), 'error4xx');

// 支付成功页面
const paySuccess = r => require.ensure([], () => r(require('@/pages/pay/paySuccess')), 'paySuccess');

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: login
    },
    {
      path: '/login',
      component: login
    },
    {
      path: '/emsAdmin',
      component: emsAdmin,
      meta: {
        requireAuth: true
      },
      children: [
        {
          path: '',
          component: viewInfo,
          meta: []
        },
        {
          path: '/addExam',
          component: addExam,
          meta: ['考试管理', '添加考试']
        },
        {
          path: '/viewExam',
          component: viewExam,
          meta: ['考试管理', '查看考试']
        },
        {
          path: '/regExam',
          component: regExam,
          meta: ['考试管理', '考试报名']
        },
        {
          path: '/addClr',
          component: addClr,
          meta: ['教室管理', '添加教室'],
        },
        {
          path: '/viewClr',
          component: viewClr,
          meta: ['教室管理', '查看教室']
        },
        {
          path: '/addExaminee',
          component: addExaminee,
          meta: ['考生管理', '添加考生账号']
        },
        {
          path: '/viewExaminee',
          component: viewExaminee,
          meta: ['考生管理', '查看考生']
        },
        {
          path: '/viewExamInfo',
          component: viewExamInfo,
          meta: ['考试信息', '查看考试信息']
        },
        {
          path: '/regExamOnline',
          component: regExamOnline,
          meta: ['考试信息', '在线考试报名']
        },
        {
          path: '/editInfo',
          component: editInfo,
          meta: ['个人信息', '编辑信息']
        }
      ]
    },
    {
      path: '/emsMg',
      component: emsMg,
      meta: {
        requireAuth: true
      },
      children: [
        {
          path: '',
          component: viewInfo,
          meta: []
        },
        {
          path: '/mgAddExam',
          component: addExam,
          meta: ['考试管理', '添加考试']
        },
        {
          path: '/mgViewExam',
          component: viewExam,
          meta: ['考试管理', '查看考试']
        },
        {
          path: '/mgRegExam',
          component: regExam,
          meta: ['考试管理', '考试报名']
        },
        {
          path: '/mgAddClr',
          component: addClr,
          meta: ['教室管理', '添加教室']
        },
        {
          path: '/mgViewClr',
          component: viewClr,
          meta: ['教室管理', '查看教室']
        },
        {
          path: '/mgAddExaminee',
          component: addExaminee,
          meta: ['考生管理', '添加考生账号']
        },
        {
          path: '/mgViewExaminee',
          component: viewExaminee,
          meta: ['考生管理', '查看考生']
        },
        {
          path: '/mgEditInfo',
          component: editInfo,
          meta: ['个人信息', '编辑信息']
        },
        {
          path: '/mgPaySuccess',
          component: paySuccess
        },
      ]
    },
    {
      path: '/emsExaminee',
      component: emsExaminee,
      meta: {
        requireAuth: true
      },
      children: [
        {
          path: '',
          component: viewInfo,
          meta: []
        },
        {
          path: '/examineeViewExamInfo',
          component: viewExamInfo,
          meta: ['考试信息', '查看考试信息']
        },
        {
          path: '/examineeRegExamOnline',
          component: regExamOnline,
          meta: ['考试信息', '在线考试报名']
        },
        {
          path: '/examineeViewRegPrice',
          component: viewRegPrice,
          meta: ['考试信息', '查看未缴费']
        },
        {
          path: '/examineeEditInfo',
          component: editInfo,
          meta: ['个人信息', '编辑信息']
        },
        {
          path: '/examineePaySuccess',
          component: paySuccess
        }
      ]
    },
    {
      path: '/**',
      component: error4xx
    }
  ]
});

export default router
