package com.cyj.ems;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cyj.ems.dao.ClrDao;
import com.cyj.ems.dao.ClrMgDao;
import com.cyj.ems.dao.UserDao;
import com.cyj.ems.domain.*;
import com.cyj.ems.service.*;
import com.cyj.ems.utils.*;
import com.github.pagehelper.PageHelper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class EmsBackEndApplicationTests {
    @Autowired
    ClrDao clrDao;

    @Autowired
    ClrService clrService;

    @Autowired
    ClrMgDao clrMgDao;

    @Resource
    StringRedisTemplate stringRedisTemplate;

//    @Resource
//    RedisTemplate<String, User> redisTemplate4User;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Test
    void redisTest01() { // StringRedisTemplate
        //        stringRedisTemplate.opsForValue().set("msg2", "redis2");
        //System.out.println(stringRedisTemplate.opsForValue().get("msg"));
    }

    @Test
    void redisTest02() { // RedisTemplate
/*
        User user = userMapper.getUserByUserName("08160001");
        System.out.println(user);
        redisTemplate4User.opsForValue().set("user01", user);
*/
//        String keyName = "spring:session:sessions:8402e7cf-df22-473f-b8b1-7be1243ca877";
//        keyName = "myhash";
//        Map<Object, Object> content = redisTemplate.opsForHash().entries("1234");
//        if(content.size() == 0) {
//            System.out.println("content is null");
//        }
//        System.out.println(content);
//        String regExamineeSessionID = "4c050cee-2800-47d1-8969-2f0e3d0dba37";
//        String keyName = "spring:session:sessions:" + regExamineeSessionID;
//        Map<Object, Object> content = redisTemplate.opsForHash().entries(keyName);
//        String regExamineeStrObj = (String) content.get("sessionAttr:" + regExamineeSessionID);
//        Integer startIndex = regExamineeStrObj.indexOf('{');
//        regExamineeStrObj = regExamineeStrObj.substring(startIndex, regExamineeStrObj.length());
//        JSONObject jsonObject = JSON.parseObject(regExamineeStrObj);
//        RegExaminee regExaminee = JSONObject.toJavaObject(jsonObject, RegExaminee.class);
//        System.out.println(regExaminee);
    }

    @Test
    void JSON2JavaBean() {
//        String jsonStr = "{\"sessionID\":\"77d42eff-a14a-41f6-9d46-6f7aadeabd19\",\"userName\":\"mg0001\"}";
//        NormalRequestVO normalRequestVO = JSON.parseObject(jsonStr, NormalRequestVO.class);
//        System.out.println(normalRequestVO.toString());
    }

    @Test
    void updateTest() {
/*
        User user = new User();
        user.setUserName("08160002");
        user.setGender("男");
        userMapper.updateUserInfo(user);
*/
    }

    @Test
    void fileReadTest() throws Exception {
/*
        String downloadFileName = "格式文件-负责人列表.xls";
        String downloadFilePath = "E:\\_Java\\IdeaProject\\EMS\\FormatFile\\";

        try {
            System.out.println(downloadFileName);
            int i = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(downloadFilePath);
        }
        System.out.println(downloadFilePath + downloadFileName);
*/
    }

    @Test
    void clrTest() {
//        Classroom classroom = clrMapper.getClassroomByClrNumber("B02B202");
//        System.out.println(classroom);
//        Classroom classroom = new Classroom();
//        classroom.setClrNumber("B05A502");
//        classroom.setAddress("中国矿业大学");
//        classroom.setCapacity(30);
//        clrDao.insertClassroom(classroom);
//        System.out.println(classroom.getClrID());
/*
        Classroom classroom = clrService.getClassroomByClrNumber("B06C504");
        System.out.println(classroom);
*/
    }

    @Test
    void clrMgTest() {
//        List<ClrMg> clrMgList = clrMgDao.getClrMgByName("张小智");
//        for(ClrMg clrMg : clrMgList) {
//            System.out.println(clrMg.toString());
//        }
/*
        ClrMg clrMg = new ClrMg();
        clrMg.setClrID(4);
        clrMg.setMgName("王志");
        clrMg.setMgPhone("13243567658");
        clrMgDao.insertClrMg(clrMg);
*/
    }

    @Test
    void excelOpsTest() {
/*
        String filePath = "E:\\_Java\\IdeaProject\\EMS\\temp\\";
        String fileName = "mg0001.xls";

        InputStream fileInputStream = null;
        List<ClrMg> clrMgList = new ArrayList<>();
        try {
            fileInputStream = new FileInputStream(filePath + fileName);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream); // HSSFWorkBook for xls
            HSSFSheet sheet = workbook.getSheet("Sheet1");
            int rowNumber = sheet.getPhysicalNumberOfRows();
//            System.out.println(rowNumber);
            for(int i = 1; i < rowNumber; i++) {
                Row row = sheet.getRow(i);

                ClrMg clrMg = new ClrMg();
                clrMg.setClrID(6);

                // col 1
                Cell cell01 = row.getCell(0);
                clrMg.setMgName(cell01.getStringCellValue());

                // col 2
                Cell cell02 = row.getCell(1);
                clrMg.setMgPhone(cell02.getStringCellValue());

                clrMgList.add(clrMg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(clrMgList.size());

        System.out.println("开始批量插入");
        clrMgDao.insertClrMgList(clrMgList);
*/
    }

    @Autowired
    ExamService examService;

    private Date String2SQLDate(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(d.getTime());
    }

    private Time String2SQLTime(String timeStr) {
        SimpleDateFormat format = new SimpleDateFormat("HH-mm");
        java.util.Date d = null;
        try {
            d = format.parse(timeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Time(d.getTime());
    }

    @Test
    void examTest() {
//        System.out.println(examService.countExamSearchByExamName("学"));
    }

    @Autowired
    ExamClrService examClrService;

    @Test
    void examClrTest() {
/*
        String clrFileName = "E:\\_Java\\IdeaProject\\EMS\\temp\\AddExam\\AddExamClr" + "\\" + "mg0001" + ".xls";
        List<ExamClr> examClrList = examClrService.getExamClrListFromUploadFile(clrFileName);
        for(ExamClr examClr : examClrList) {
            System.out.println(examClr.toString());
        }
        examClrService.insertExamClrList(examClrList);
*/
    }

    @Autowired
    ExamMgService examMgService;

    @Test
    void examMgTest() {
/*
        List<ExamMg> examMgList = examMgService.getExamMgListByExamNumber("CE0723");
        for(ExamMg examMg : examMgList) {
            System.out.println(examMg.toString());
        }
*/
//        examMgService.insertExamMgList("E:\\_Java\\IdeaProject\\EMS\\temp\\AddExam\\AddExamMg" + "\\" + "mg0001" + ".xls");
    }

    @Autowired
    ExamineeService examineeService;

    @Test
    void examineeTest() {
/*
        String examineeFileName = "E:\\_Java\\IdeaProject\\EMS\\temp\\AddExam\\AddExaminee" + "\\" + "mg0001" + ".xls";
        List<Examinee> examineeList = examineeService.getExamineeListFromUploadFile(examineeFileName);
        for(Examinee examinee : examineeList) {
            System.out.println(examinee.toString());
        }
        examineeService.insertExamineeList(examineeList);
*/
    }

    @Test
    void clrCapacityTest() {
/*
        List<String> clrNumberList = new ArrayList<>();
        clrNumberList.add("B02B202");
        clrNumberList.add("B03A303");
        clrNumberList.add("B04B404");
        System.out.println(clrService.getClrCapacityByClrNumberList(clrNumberList));
*/
    }

    @Test
    void randomTest01() {
/*
        String userName = "mg0001";
        Map<String, Object> processResultMap = processClrFileAndExamineeFile(userName);

        List<ExamClr> examClrList = (List<ExamClr>) processResultMap.get("examClrList");
        // examClrService.insertExamClrList(examClrList);
        List<String> clrNumberList = new ArrayList<>();
        for(ExamClr examClr : examClrList){
            clrNumberList.add(examClr.getClrNumber());
        }
        List<Classroom> clrList = clrService.getClrListByClrNumberList(clrNumberList);
        List<ArrangeClr> arrangeClrList = new ArrayList<>();
        Integer seatCapacity = 0;
        for(int i = 0; i < clrList.size(); i++) {
            ArrangeClr arrangeClr = new ArrangeClr();
            arrangeClr.setClrNumber(clrList.get(i).getClrNumber());
            arrangeClr.setCapacity(clrList.get(i).getCapacity());
            arrangeClr.setExtra(seatCapacity);
            seatCapacity += arrangeClr.getCapacity();
            if(i == 0){
                arrangeClr.setStartPosition(1);
            } else{
                arrangeClr.setStartPosition(arrangeClrList.get(i - 1).getEndPosition() + 1);
            }
            arrangeClr.setEndPosition(arrangeClr.getCapacity() + arrangeClr.getStartPosition() - 1);
            arrangeClrList.add(arrangeClr);
        }
        for(ArrangeClr arrangeClr : arrangeClrList) {
            System.out.println(arrangeClr.toString());
        }

        List<Examinee> examineeList = (List<Examinee>) processResultMap.get("examineeList");
        Integer examineeNumber = examineeList.size();

        HashSet<Integer> seatSet = new HashSet<>();
        Integer seatPosition = 0;
        while(seatSet.size() != examineeNumber) {
            seatPosition = (int) (Math.random() * seatCapacity + 1);
            seatSet.add(seatPosition);
        }
        List<Integer> seatPosList = new ArrayList<>(seatSet);
        System.out.println(seatPosList);
        for(int i = 0; i < examineeList.size(); i++){
            Integer curSeat = seatPosList.get(i);
            String clrNumber = "";
            for(int j = 0; j < arrangeClrList.size(); j++) {
                ArrangeClr arrangeClr = arrangeClrList.get(j);
                if(curSeat >= arrangeClr.getStartPosition() && curSeat <= arrangeClr.getEndPosition()) {
                    clrNumber = arrangeClr.getClrNumber();
                    curSeat -= arrangeClr.getExtra();
                    break;
                }
            }
            examineeList.get(i).setClrNumber(clrNumber);
            examineeList.get(i).setSeatNumber(curSeat);
            System.out.println(examineeList.get(i).toString());
        }
*/
    }

    @Test
    void randomMethod02() {
/*
        String userName = "mg0001";
        Map<String, Object> processResultMap = processClrFileAndExamineeFile(userName);

        List<ExamClr> examClrList = (List<ExamClr>) processResultMap.get("examClrList");
        Integer clrNumber = examClrList.size();
        List<Examinee> examineeList = (List<Examinee>) processResultMap.get("examineeList");
        Integer examineeNumber = examineeList.size();

        List<String> clrNumberList = new ArrayList<>();
        for(ExamClr examClr : examClrList){
            clrNumberList.add(examClr.getClrNumber());
        }
        List<Classroom> clrList = clrService.getClrListByClrNumberList(clrNumberList);

        Integer seatCapacity = 0;
        for(Classroom clr : clrList) {
            seatCapacity += clr.getCapacity();
        }

        Integer perClrCapacity = examineeNumber / clrNumber;
        Integer surplus = examineeNumber % clrNumber;

        Random random = new Random();
        Collections.shuffle(clrList, random);
        Collections.shuffle(examineeList, random);

        List<ArrangeClr2> arrangeClr2List = new ArrayList<>();
        for(Classroom clr : clrList) {
            ArrangeClr2 arrangeClr2 = new ArrangeClr2();
            arrangeClr2.setClrNumber(clr.getClrNumber());
            arrangeClr2.setCapacity(clr.getCapacity());
            arrangeClr2.setExamineeNumber(perClrCapacity);
            arrangeClr2List.add(arrangeClr2);
        }

        if(surplus != 0){
            HashSet<Integer> hashSet = new HashSet<>();
            while(hashSet.size() != surplus){
                hashSet.add((int) (Math.random() * clrNumber + 1));
            }
            List<Integer> integerList = new ArrayList<>(hashSet);
//            System.out.println(integerList);
            for(int i = 0; i < integerList.size(); i++) {
                Integer curExamineeNumber = arrangeClr2List.get(integerList.get(i) - 1).getExamineeNumber();
                arrangeClr2List.get(integerList.get(i) - 1).setExamineeNumber(curExamineeNumber + 1);
            }
        }

        Integer examineeCount = 0;
        for(ArrangeClr2 arrangeClr2 : arrangeClr2List) {
            HashSet<Integer> hashSet = new HashSet<>();
            while(hashSet.size() != arrangeClr2.getExamineeNumber()){
                hashSet.add((int) (Math.random() * arrangeClr2.getCapacity() + 1));
            }
            List<Integer> curSeatPostList = new ArrayList<>(hashSet);
            for(int i = 0; i < curSeatPostList.size(); i++) {
                examineeList.get(examineeCount).setClrNumber(arrangeClr2.getClrNumber());
                examineeList.get(examineeCount).setSeatNumber(curSeatPostList.get(i));
                examineeCount++;
            }
        }

        for(Examinee examinee : examineeList) {
            System.out.println(examinee.toString());
        }

*/
    }

    private Map<String, Object> processClrFileAndExamineeFile(String userName) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("sign", 200); // 200 表示 ClrFile 和 ExamineeFile 处理成功
        String clrFileName = "E:\\_Java\\IdeaProject\\EMS\\temp\\AddExam\\AddExamClr" + "\\" + userName + ".xls";
        List<ExamClr> examClrList = examClrService.getExamClrListFromUploadFile(clrFileName);
        // 统计可容纳人数
        Integer clrCapacity = calculateClrCapacity(examClrList);

        String examineeFileName = "E:\\_Java\\IdeaProject\\EMS\\temp\\AddExam\\AddExaminee" + "\\" + userName + ".xls";
        List<Examinee> examineeList = examineeService.getExamineeListFromUploadFile(examineeFileName);
        Integer examineeNumber = examineeList.size();

        if(clrCapacity < examineeNumber){
            resultMap.replace("sign", 401); // 401 表示教室容量小于考试人数，数据库无改动
            return resultMap;
        }

        // 教室容量大于考生数目
        resultMap.put("examClrList", examClrList);
        resultMap.put("examineeList", examineeList);

        return resultMap;
    }

    private Integer calculateClrCapacity(List<ExamClr> examClrList){
        Integer result = 0;

        List<String> clrNumberList = new ArrayList<>();
        for(ExamClr examClr : examClrList) {
            clrNumberList.add(examClr.getClrNumber());
        }

        result = clrService.getClrCapacityByClrNumberList(clrNumberList);

        return result;
    }

    @Autowired
    UserService userService;

    @Test
    void userTest() {
/*
        User user = new User();
        user.setUserName("08160004");
        user.setPassword("123456");
        user.setRole(3);
        userService.insertSingleExamineeAccount(user);
*/
/*
        String filePath = "E:\\_Java\\IdeaProject\\EMS\\temp\\AddExaminee\\";
        String fileName = "mg0001" + ".xls";
        List<User> examineeAccountList = userService.getExamineeAccountListFromUploadFile(filePath + fileName);
        System.out.println(examineeAccountList.size());
//        for(User user : userList) {
//            System.out.println(user);
//        }
        Map<String, String> userNameMap = new HashMap<>();
        List<String> userNameList = new ArrayList<>();
        for(User examineeUser : examineeAccountList) {
            userNameList.add(examineeUser.getUserName());
            userNameMap.put(examineeUser.getUserName(), examineeUser.getPassword());
        }

        List<String> haveExistedList = userService.findHaveExistedList(userNameList);
        if(haveExistedList.size() != 0){
            for(String userNameStr : haveExistedList) {
                userNameMap.remove(userNameStr);
            }
        }

        examineeAccountList = new ArrayList<>();
        for(Map.Entry<String, String> entry : userNameMap.entrySet()) {
            User newExamineeUser = new User();
            newExamineeUser.setUserName(entry.getKey());
            newExamineeUser.setPassword(entry.getValue());
            newExamineeUser.setRole(3);
            examineeAccountList.add(newExamineeUser);
        }
        System.out.println(examineeAccountList.size());

        userService.insertExamineeAccountList(examineeAccountList);
*/
//        PageHelper.startPage(1, 6);
//        List<Examinee> examineeList = examineeService.getExamineeListByExamNumber("MCM2020");
//        List<String> userNameList = new ArrayList<>();
//        for(Examinee examinee : examineeList) {
//            System.out.println(examinee.getUserName());
//            userNameList.add(examinee.getUserName());
//        }
//        List<User> userList = userService.getUserListByUserNameList(userNameList);
//        for(User user : userList) {
//            System.out.println(user);
//        }
    }

    @Test
    void pageHelperTest() {
/*
        PageHelper.startPage(2,10);
        List<Exam> examList = examService.getAllExam();
        for(Exam exam : examList){
            System.out.println(exam);
        }
*/
    }

    @Test
    void saveFileTest(){
/*
        String loadFileName = "E:\\_Java\\IdeaProject\\EMS\\FormatFile\\Export\\" + "导出格式文件-考生考试信息列表.xls";
        String saveFileName = "E:\\_Java\\IdeaProject\\EMS\\temp\\Export\\viewExamInfo\\" + "08160001" + ".xls";

        UploadAndDownload.saveAs(loadFileName, saveFileName);
*/
    }

    @Test
    void exportExamInfoListTest() {
//        String userName = "08160001";
//
//        List<ExamExaminee> examExamineeList = getExamExamineeList(userName);
//
//         buildExamExamineeListFile(userName, examExamineeList);
    }

    private List<ExamExaminee> getExamExamineeList(String userName) {
        List<Examinee> examineeList = examineeService.getExamineeListByUserName(userName);

        List<String> examNumberList = new ArrayList<>();
        for(Examinee examinee : examineeList) {
            examNumberList.add(examinee.getExamNumber());
        }
        List<Exam> examList = examService.getExamListByExamNumberList(examNumberList);

        List<ExamExaminee> examExamineeList = new ArrayList<>();
        for(int i = 0; i < examineeList.size(); i++){
            Exam curExam = examList.get(i);
            Examinee curExaminee = examineeList.get(i);

            ExamExaminee newExamExaminee = new ExamExaminee();
            newExamExaminee.setExamNumber(curExam.getExamNumber());
            newExamExaminee.setExamName(curExam.getExamName());
            newExamExaminee.setExamDate(curExam.getExamDate());
            newExamExaminee.setStartTime(curExam.getStartTime());
            newExamExaminee.setEndTime(curExam.getEndTime());
            newExamExaminee.setRegPrice(curExam.getRegPrice());
            newExamExaminee.setExamineeNumber(curExaminee.getExamineeNumber());
            newExamExaminee.setPaymentStatus(curExaminee.getPaymentStatus());
            newExamExaminee.setClrNumber(curExaminee.getClrNumber());
            newExamExaminee.setSeatNumber(curExaminee.getSeatNumber());

            examExamineeList.add(newExamExaminee);
        }

        return examExamineeList;
    }

    private void buildExamExamineeListFile(String userName, List<ExamExaminee> examExamineeList) {
        String loadFileName = "E:\\_Java\\IdeaProject\\EMS\\FormatFile\\Export\\" + "导出格式文件-考生考试信息列表.xls";
        String saveFileName = "E:\\_Java\\IdeaProject\\EMS\\temp\\Export\\viewExamInfo\\" + userName + ".xls";

        // 构建导出文件
        InputStream fileInputStream = null;
        OutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(loadFileName);
            fileOutputStream = new FileOutputStream(saveFileName);

            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheet("Sheet1");
            // 第一行考生用户名构建
            Row row01 = sheet.getRow(0);
            Cell userNameCell = row01.createCell(1);
            userNameCell.setCellValue(userName);

            // 写入考生考试信息
            for(int i = 0; i < examExamineeList.size(); i++) {
                Row row = sheet.createRow(i + 2);
                ExamExaminee examExaminee = examExamineeList.get(i);

                Cell examNumberCell = row.createCell(0);
                examNumberCell.setCellValue(examExaminee.getExamNumber());

                Cell examNameCell = row.createCell(1);
                examNameCell.setCellValue(examExaminee.getExamName());

                Cell examDateCell = row.createCell(2);
                examDateCell.setCellValue(examExaminee.getExamDate().toString());

                Cell startTimeCell = row.createCell(3);
                startTimeCell.setCellValue(examExaminee.getStartTime().toString());

                Cell endTimeCell = row.createCell(4);
                endTimeCell.setCellValue(examExaminee.getEndTime().toString());

                Cell regPriceCell = row.createCell(5);
                regPriceCell.setCellValue(examExaminee.getRegPrice().toString());

                Cell examineeNumberCell = row.createCell(6);
                examineeNumberCell.setCellValue(examExaminee.getExamineeNumber());

                Cell paymentStatusCell = row.createCell(7);
                paymentStatusCell.setCellValue(examExaminee.getPaymentStatus().toString());

                Cell clrNumberCell = row.createCell(8);
                clrNumberCell.setCellValue(examExaminee.getClrNumber());

                Cell seatNumberCell = row.createCell(9);
                seatNumberCell.setCellValue(examExaminee.getSeatNumber().toString());
            }

            workbook.write(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fileInputStream != null){
                    fileInputStream.close();
                }
                if(fileOutputStream != null){
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    void normalTest() {
/*
        ExamineeUserInfo examineeUserInfo = new ExamineeUserInfo();
        System.out.println(examineeUserInfo.getPaymentStatus());
*/
    }


    @Test
    void contextLoads() {
        // System.out.println(SpringBootVersion.getVersion());
    }

}
