package com.lego.survey.lib.resourve;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FdfsFileService implements IFdfsFileService {

    public static final Logger log = LoggerFactory.getLogger(FdfsFileService.class);

    @Autowired
    private TrackerGroup trackerGroup;

    @Value("${file.service.url}")
    private String fileServiceUrl;

    @Override
    public Map<String, Object> upload(UploadFile file) {
        Map<String, Object> map = new HashMap<>();
        StorageClient storageClient = null;
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerGroup.getConnection();
            storageClient = new StorageClient(trackerServer, null);

            String[] ary = storageClient.upload_file(file.getContent(), file.getExt(), null);
            String groupName = ary[0];
            String remoteName = ary[1];
            map.put("data", fileServiceUrl + groupName+ "/"+ remoteName+"?fileName="+ file.getFileName());
        } catch (IOException e) {
            log.error("upload file to [fastdfs] system error",e);
           // return ResultMapHelper.getResultMap("2","上传失败");
        } catch (MyException e) {
            log.error("upload file to [fastdfs] system error",e);
            //return ResultMapHelper.getResultMap("2","上传失败");
        } finally {
            if(null != trackerServer){
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    log.error("close tracker server error",e);
                }
            }
        }
        map.put("code","0");
        map.put("msg","ok");
        return map;
    }

    @Override
    public byte[] download(String groupName, String remoteFile) {
        StorageClient storageClient = null;
        TrackerServer trackerServer = null;
        byte[] datas = null;
        try {
            trackerServer = trackerGroup.getConnection();
            storageClient = new StorageClient(trackerServer, null);
            datas = storageClient.download_file(groupName, remoteFile);
        } catch (IOException e) {
            log.error("download file from [fastdfs] system error",e);
            return datas;
        } catch (MyException e) {
            log.error("download file from [fastdfs] system error",e);
            return datas;
        } finally {
            if(null != trackerServer){
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    log.error("close tracker server error",e);
                }
            }
        }
        return datas;
    }

}
