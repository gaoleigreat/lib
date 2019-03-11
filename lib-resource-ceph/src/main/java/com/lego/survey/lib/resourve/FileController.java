package com.lego.survey.lib.resourve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/file/v1")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private IFdfsFileService fdfsFileService;

    @RequestMapping(value="/app/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> appUpload(@RequestBody UploadFile uploadFile){
        return fdfsFileService.upload(uploadFile);
    }

    @RequestMapping(value="/web/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> webUpload(HttpServletRequest req){
        List<MultipartFile> fileList = new ArrayList<>();
        if(req instanceof MultipartHttpServletRequest){
            fileList = ((MultipartHttpServletRequest) req).getFiles("file");
        }
        Map<String,Object> resultMap  = new HashMap<>();
        try {
            List<Map<String, Object>> returnList = new ArrayList<>();
            for(MultipartFile file : fileList){
                UploadFile uploadFile = new UploadFile();
                uploadFile.setFileName(file.getOriginalFilename());

                    uploadFile.setContent(file.getBytes());
                if(!StringUtils.isEmpty(file.getOriginalFilename())){
                    int pos = file.getOriginalFilename().lastIndexOf(".");
                    if(pos > -1 && pos + 1 < file.getOriginalFilename().length()){
                        uploadFile.setExt(file.getOriginalFilename().substring(pos + 1));
                    }
                }
                Map<String,Object> map = fdfsFileService.upload(uploadFile);
                if("0".equals(map.get("code"))){
                    Map f = new HashMap();
                    f.put("fileName", file.getOriginalFilename());
                    f.put("url", map.get("data"));
                    returnList.add(f);
                }
            }
            resultMap.put("datas",returnList);
        } catch (IOException e) {
            log.error("upload file error", e);
        }
        if(!resultMap.isEmpty()){
            resultMap.put("code","0");
            resultMap.put("msg","ok");
            return resultMap;
        }
        return null;
        //return ResultMapHelper.getResultMap("2","参数错误");
    }

    @RequestMapping(value="/download/{groupName}/**", method = RequestMethod.GET)
    public void appUpload(HttpServletRequest req, HttpServletResponse res, @PathVariable String groupName, @RequestParam(required = false) String fileName){
        try {
            String uri = req.getRequestURI().toString();
            int position = uri.indexOf(groupName);
            position = position + groupName.length()+1;
            String remoteName = "";
            if(position > -1 && position < uri.length()){
                remoteName = uri.substring(position);
            }
            res.setContentType("multipart/form-data");
            if(!StringUtils.isEmpty(fileName)){
                res.setHeader("Content-Disposition", "attachment;fileName="+URLEncoder.encode(fileName,"utf-8"));
            }else{
                res.setHeader("Content-Disposition", "attachment;fileName=");
            }
            byte[] datas = fdfsFileService.download(groupName, remoteName);
            res.getOutputStream().write(datas);
        } catch (UnsupportedEncodingException e) {

            log.error("download file error",e);
        } catch (IOException e) {

            log.error("download file error",e);
        }
    }
}
