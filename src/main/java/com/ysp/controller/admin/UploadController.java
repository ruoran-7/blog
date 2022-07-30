package com.ysp.controller.admin;


import com.ysp.util.TencentCOSUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/admin")
public class UploadController {


        @PostMapping(value = "/upload")
        @ResponseBody
        public Map<String, Object> upload(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file){
            String url = null;


            Map<String, Object> resultMap = new HashMap<String, Object>();

            try {
                url = TencentCOSUtil.uploadfile(file);
                resultMap.put("success", 1);
                resultMap.put("message", "上传成功！");
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("success", 0);
                resultMap.put("message", "文件上传失败！");
            }
            int success = Integer.parseInt(resultMap.get("success").toString());
            if (success == 1) {
                resultMap.put("url",url);
            }
            return resultMap;
        }
}
