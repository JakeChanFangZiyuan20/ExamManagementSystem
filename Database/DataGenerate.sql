-- 数据插入

-- Table roles
INSERT INTO roles(role_name) VALUES('mg0'); -- 系统管理员
INSERT INTO roles(role_name) VALUES('mg1'); -- 考试管理员
INSERT INTO roles(role_name) VALUES('mg2'); -- 考生

-- Table users
INSERT INTO users(user_name, user_password, user_role) VALUES('admin', '123456', 1); -- 系统管理员
INSERT INTO users(user_name, user_password, user_role) VALUES('mg0001', '123456', 2); -- 考试管理员1
INSERT INTO users(user_name, user_password, user_role) VALUES('mg0002', '123456', 2); -- 考试管理员2
INSERT INTO users(user_name, user_password, user_role) VALUES('mg0003', '123456', 2); -- 考试管理员3
INSERT INTO users(user_name, user_password, user_role) VALUES('08160001', '123456', 3); -- 考生1
INSERT INTO users(user_name, user_password, user_role) VALUES('08160002', '123456', 3); -- 考生2
INSERT INTO users(user_name, user_password, user_role) VALUES('08160003', '123456', 3); -- 考生3

-- Table role_management
INSERT INTO role_management(mg_a, mg_b) VALUES(1, 2);
INSERT INTO role_management(mg_a, mg_b) VALUES(1, 3);
INSERT INTO role_management(mg_a, mg_b) VALUES(2, 3);

-- Table classrooms
-- assumes:
--  small clr can contain 30 persons
--  big clr can contain 60 persons
INSERT classrooms(capacity, address, clr_number) VALUES(60, '中国矿业大学', 'B01A101');
INSERT classrooms(capacity, address, clr_number) VALUES(60, '中国矿业大学', 'B02B202');
INSERT classrooms(capacity, address, clr_number) VALUES(60, '中国矿业大学', 'B03A303');
INSERT classrooms(capacity, address, clr_number) VALUES(30, '中国矿业大学', 'B04B404');
INSERT classrooms(capacity, address, clr_number) VALUES(30, '中国矿业大学', 'B05C505');

-- Table clr_mg
INSERT INTO clr_mg(clr_id, mg_name, mg_phone) VALUES(1, '张小智','13345454545');
INSERT INTO clr_mg(clr_id, mg_name, mg_phone) VALUES(2, '张小智','13393939393');
INSERT INTO clr_mg(clr_id, mg_name, mg_phone) VALUES(1, '胡志明','13962626262');
INSERT INTO clr_mg(clr_id, mg_name, mg_phone) VALUES(1, '李四','18353535353');
INSERT INTO clr_mg(clr_id, mg_name, mg_phone) VALUES(3, '贺七','16821212121');

-- Table Exam
INSERT INTO exams(exam_number, exam_name, exam_date, exam_starttime, exam_endtime, exam_regprice) VALUES ('CS0519', 'C++程序设计', '2018-09-01', '08:00:00', '10:00:00', 30);
INSERT INTO exams(exam_number, exam_name, exam_date, exam_starttime, exam_endtime, exam_regprice) VALUES ('CE0723', '电路设计', '2019-05-016', '09:30:00', '12:00:00', 40);
INSERT INTO exams(exam_number, exam_name, exam_date, exam_starttime, exam_endtime, exam_regprice) VALUES ('EE0145', '大数据分析', '2017-06-18', '14:00:00', '16:00:00', 25);
INSERT INTO exams(exam_number, exam_name, exam_date, exam_starttime, exam_endtime, exam_regprice) VALUES ('MA14663', '管理学', '2016-11-20', '014:30:00', '17:00:00', 16);
INSERT INTO exams(exam_number, exam_name, exam_date, exam_starttime, exam_endtime, exam_regprice) VALUES ('AI32548', '创新创意导论', '2020-01-23', '19:00:00', '20:30:00', 74);

-- Table exam_mg
INSERT INTO exam_mg(exam_number, mg_name, mg_phone) VALUES('CE0723', '许五', '13946464646');
INSERT INTO exam_mg(exam_number, mg_name, mg_phone) VALUES('CE0723', '林立', '13849898646');
INSERT INTO exam_mg(exam_number, mg_name, mg_phone) VALUES('CE0723', '杨帆', '18940064646');
INSERT INTO exam_mg(exam_number, mg_name, mg_phone) VALUES('CS0519', '杨帆', '18940064646');
INSERT INTO exam_mg(exam_number, mg_name, mg_phone) VALUES('EE0145', '杨帆', '18940064646');
INSERT INTO exam_mg(exam_number, mg_name, mg_phone) VALUES('MA14663', '董勇', '18333343646');
INSERT INTO exam_mg(exam_number, mg_name, mg_phone) VALUES('AI32548', '张琳', '14444464646');

-- Table exam_clr
INSERT INTO exam_clr(exam_number, clr_number) VALUES('CS0519', 'B01A101');
INSERT INTO exam_clr(exam_number, clr_number) VALUES('CS0519', 'B02B202');
INSERT INTO exam_clr(exam_number, clr_number) VALUES('CS0519', 'B03A303');
INSERT INTO exam_clr(exam_number, clr_number) VALUES('CE0723', 'B04B404');
INSERT INTO exam_clr(exam_number, clr_number) VALUES('EE0145', 'B04B404');
INSERT INTO exam_clr(exam_number, clr_number) VALUES('MA14663', 'B04B404');
INSERT INTO exam_clr(exam_number, clr_number) VALUES('AI32548', 'B05C505');

-- Table examinees
INSERT INTO examinees(examinee_number, user_name, exam_number, payment_status) VALUES('CS06068989', '08160001', 'CS0519', 1);
INSERT INTO examinees(examinee_number, user_name, exam_number, payment_status) VALUES('CS06068989', '08160001', 'CE0723', 1);
INSERT INTO examinees(examinee_number, user_name, exam_number, payment_status) VALUES('CS06068989', '08160001', 'EE0145', 0);
INSERT INTO examinees(examinee_number, user_name, exam_number, payment_status) VALUES('RHEK890234', '08160001', 'MA14663', 0);
INSERT INTO examinees(examinee_number, user_name, exam_number, payment_status) VALUES('ES34892312', '08160002', 'CS0519', 1);
INSERT INTO examinees(examinee_number, user_name, exam_number, payment_status) VALUES('XNF9028742', '08160003', 'CS0519', 0);