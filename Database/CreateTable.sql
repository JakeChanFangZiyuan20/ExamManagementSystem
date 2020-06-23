-- ExamManagementSystem TABLES CREATE

-- TABLE users
CREATE TABLE users(
    user_id INT NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(20) NOT NULL COMMENT '用户登录账号',
    user_password VARCHAR(20) NOT NULL,
    user_role TINYINT(2) NOT NULL,
    user_truename VARCHAR(15) NULL COMMENT '用户姓名',
    user_gender VARCHAR(8) NULL,
    user_birthday DATE NULL,
    user_identity VARCHAR(20) NULL,
    user_phone VARCHAR(16) NULL,
    user_email VARCHAR(50) NULL,
    user_photo VARCHAR(30) NULL COMMENT '用户照片，存放内容为地址',
    PRIMARY KEY (user_id),
    INDEX index_uname (user_name),
    INDEX index_utruename (user_truename)
)ENGINE=INNODB, COMMENT = '用户表';

-- TABLE role_management
-- mg_a manages mg_b
CREATE TABLE role_management(
    mg_a TINYINT(2) NOT NULL COMMENT '角色 a',
    mg_b TINYINT(2) NOT NULL COMMENT '角色 b'
)ENGINE=INNODB, COMMENT = '角色管理表';

-- TABLE role
CREATE TABLE roles(
    role_id INT NOT NULL AUTO_INCREMENT,
    role_name VARCHAR(15) NOT NULL COMMENT '角色名称',
		PRIMARY KEY (role_id)
)ENGINE=INNODB, COMMENT = '角色表';

-- TABLE classrooms
-- assumes:
--  small clr can contain 30 persons
--  big clr can contain 60 persons
CREATE TABLE classrooms(
    clr_id INT NOT NULL AUTO_INCREMENT,
    capacity INT NOT NULL,
    address VARCHAR(30) NOT NULL COMMENT '详细地址',
    clr_number VARCHAR(20) NOT NULL COMMENT '门牌号',
    PRIMARY KEY (clr_id)
)ENGINE=INNODB, COMMENT = '教室表';

-- TABLE classroom_management
CREATE TABLE clr_mg(
    clr_id INT NOT NULL,
    mg_name VARCHAR(20) NOT NULL COMMENT '管理者姓名',
    mg_phone VARCHAR(16) NOT NULL COMMENT '管理者电话',
    INDEX index_cid (clr_id),
    INDEX index_mname (mg_name)
)ENGINE=INNODB, COMMENT = '教室管理表';

-- TABLE exams
CREATE TABLE exams(
    exam_id INT NOT NULL AUTO_INCREMENT,
    exam_number VARCHAR(30) NOT NULL COMMENT '考试项目编号',
    exam_name VARCHAR(30) NOT NULL COMMENT '考试项目名称',
    exam_date DATE NOT NULL,
    exam_starttime TIME NOT NULL,
    exam_endtime TIME NOT NULL,
    exam_regprice INT NOT NULL COMMENT '考试报名费',
    PRIMARY KEY (exam_id),
    INDEX index_tnumber (exam_number) USING BTREE,
    INDEX index_tname (exam_name) USING BTREE
)ENGINE=INNODB, COMMENT = '考试项目表';

-- TABLE exam_mg
CREATE TABLE exam_mg(
    exam_number VARCHAR(30) NOT NULL COMMENT '考试项目编号',
    mg_name VARCHAR(20) NOT NULL COMMENT '管理者姓名',
    mg_phone VARCHAR(16) NOT NULL COMMENT '管理者电话',
    INDEX index_enumber (exam_number),
    INDEX index_mname (mg_name)
)ENGINE=INNODB, COMMENT = '考试负责人表';

-- TABLE exam_classroom
CREATE TABLE exam_clr(
    exam_number VARCHAR(30) NOT NULL COMMENT '考试项目编号',
    clr_number VARCHAR(20) NOT NULL COMMENT '门牌号',
    INDEX index_enumber (exam_number),
    INDEX index_clrnumber (clr_number)
)ENGINE=INNODB, COMMENT = '考试教室表';

-- TABLE examinees
CREATE TABLE examinees(
    examinee_id INT NOT NULL AUTO_INCREMENT,
    examinee_number VARCHAR(30) NOT NULL COMMENT '准考证号',
    user_name VARCHAR(20) NOT NULL,
    exam_number VARCHAR(30) NOT NULL COMMENT '考试项目编号',
    payment_status TINYINT(1) NOT NULL COMMENT '付款状态，0 - 未付款，1 - 付款',
    clr_number VARCHAR(20) NULL COMMENT '教室门牌号',
    seat_number INT NULL,
    PRIMARY KEY (examinee_id),
    INDEX index_uname (user_name),
    INDEX index_enumber (exam_number),
    INDEX index_clrnumber (clr_number)
)ENGINE=INNODB, COMMENT = '考试信息表';

