package com.lego.survey.lib.resourve;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

/**
 * @author yanglf
 * @description
 * @since 2019/1/29
 **/
@Component
public class FileResourcesService {

    @Value("${define.resources.storePath}")
    private String storePath;

    public  String uploadFile(MultipartFile multipartFile,String fileName){
        try {
            multipartFile.transferTo(new File(storePath+fileName));
            return storePath+fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
