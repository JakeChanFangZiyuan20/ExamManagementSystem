package com.cyj.ems.controller;

import com.cyj.ems.domain.Classroom;
import com.cyj.ems.dto.NormalResultDTO;
import com.cyj.ems.service.ClrMgService;
import com.cyj.ems.service.ClrService;
import com.cyj.ems.utils.UploadAndDownload;
import com.cyj.ems.vo.AddClrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class AddClrController {

    @Value("${localfilepath.formatfile.addclr}")
    private String formatFileAddClrPath;

    @Value("${localfilepath.temp.addclr}")
    private String tempFileAddClrPath;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ClrService clrService;

    @Autowired
    private ClrMgService clrMgService;

    @CrossOrigin
    @GetMapping("/api/downloadClrMgList") // not need filter and interceptor
//    @ResponseBody
    public void downloadClrMgList(HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("进入下载");
        String downloadFileName = "格式文件-教室负责人列表.xls";
        String downloadFilePath = formatFileAddClrPath;
        response = UploadAndDownload.download(downloadFilePath, downloadFileName, response);
    }

    @CrossOrigin
    @PostMapping("/api/uploadXLSFile") // no need filter and interceptor
    @ResponseBody
    public Map<String, Object> uploadXLSFile(@RequestParam("file") MultipartFile xlsFile,
                              @RequestParam("sessionID") String sessionID,
                              @RequestParam("userName") String userName){
        String fileName = tempFileAddClrPath + userName + ".xls";
        return UploadAndDownload.uploadFile(redisTemplate, xlsFile, sessionID, fileName);
    }

    @CrossOrigin
    @PostMapping("/api/removeUploadFile") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> removeUploadFile(@RequestBody Map<String, Object>requestMap) {
//        System.out.println("run into removeUploadFile");
        String filePath = tempFileAddClrPath;
        String fileName = requestMap.get("userName") + ".xls";
        return UploadAndDownload.removeUploadFile(filePath, fileName);
    }

    @CrossOrigin
    @PostMapping("/api/insertClrMgList") // need filter and interceptor
    @ResponseBody
    public NormalResultDTO insertClrMgList(@RequestBody Map<String, Object>requestMap) {
//        System.out.println(addClrVo.toString());
        NormalResultDTO normalResultDTO = new NormalResultDTO();
        String filePath = tempFileAddClrPath;
        String fileName = requestMap.get("userName") + ".xls";

        String clrNumber = requestMap.get("clrNumber").toString();
        String address = requestMap.get("address").toString();
        Integer capacity = Integer.valueOf(requestMap.get("capacity").toString());

        // 查看是否已有教室
        if(clrService.hasClassroom(clrNumber)){
            normalResultDTO.setStatus(400); // 400 已存在教室
            return normalResultDTO;
        }

        // 新教室设置
        Classroom classroom = new Classroom();
        classroom.setClrNumber(clrNumber);
        classroom.setAddress(address);
        classroom.setCapacity(capacity);

        // 添加教室
        clrService.insertClassroom(classroom);

        // 为教室添加负责人列表
        clrMgService.insertClrMgList(classroom.getClrID(), filePath + fileName);

        normalResultDTO.setStatus(200); // 200 成功
        return normalResultDTO;
    }

}
