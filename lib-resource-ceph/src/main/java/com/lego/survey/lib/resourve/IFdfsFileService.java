package com.lego.survey.lib.resourve;

import java.util.Map;

public interface IFdfsFileService {

    /**
     * 上传
     * @param file
     * @return
     */
    Map<String, Object> upload(UploadFile file);

    /**
     * 下载
     * @param remoteFile
     * @param groupName
     * @return
     */
    byte[] download(String groupName, String remoteFile);
}
