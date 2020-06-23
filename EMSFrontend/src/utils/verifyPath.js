const adminPathList = [
  '/emsAdmin',
  // 考试管理
  '/addExam',
  '/viewExam',
  '/regExam',
  // 教室管理
  '/addClr',
  '/viewClr',
  // 考生管理
  '/addExaminee',
  '/viewExaminee',
  // 考试信息,
  '/viewExamInfo',
  '/regExamOnline',
  // 个人信息
  '/editInfo'
];

const mgPathList = [
  '/emsMg',
  // 考试管理
  '/mgAddExam',
  '/mgViewExam',
  '/mgRegExam',
  // 教室管理
  '/mgAddClr',
  '/mgViewClr',
  // 考生管理
  '/mgAddExaminee',
  '/mgViewExaminee',
  // 个人信息
  '/mgEditInfo',
  // 支付成功页面
  '/mgPaySuccess'
];

const examineePathList = [
  '/emsExaminee',
  // 考试信息,
  '/examineeViewExamInfo',
  '/examineeRegExamOnline',
  '/examineeViewRegPrice',
  // 个人信息
  '/examineeEditInfo',
  // 支付成功页面
  '/examineePaySuccess'
];

function verify(role, toPath) {
  if(role === 1) return adminPathList.includes(toPath);
  if(role === 2) return mgPathList.includes(toPath);
  if(role === 3) return examineePathList.includes(toPath);
}

export {
  verify
}
