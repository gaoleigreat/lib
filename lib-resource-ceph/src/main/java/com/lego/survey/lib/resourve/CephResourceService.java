package com.lego.survey.lib.resourve;

import com.ceph.rados.IoCTX;
import com.ceph.rados.Rados;
import com.ceph.rados.exceptions.RadosException;
import com.ceph.rbd.Rbd;
import com.ceph.rbd.RbdException;
import com.ceph.rbd.RbdImage;
import com.ceph.rbd.jna.RbdImageInfo;
import com.ceph.rbd.jna.RbdSnapInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/18
 **/
@Slf4j
@Component
public class CephResourceService {

    @Value("${define.resource.ceph.radosId}")
    private String radosId;

    @Value("${define.resource.ceph.confPath}")
    private String confPath;

    @Value("${deine.resource.deph.monHost}")
    private String monHost;

    @Value("${define.resource.deph.key}")
    private String key;


    @Value("${define.resource.deph.pool}")
    private String pool;

    /**
     * 连接 CEPH
     *
     * @return
     */
    public Rbd connect() {
        Rados cluster;
        try {
            cluster = new Rados(radosId);
            cluster.confSet("mon_host", monHost);
            cluster.confSet("key", key);
            // cluster.confReadFile(new File(confPath));
            cluster.connect();
            return new Rbd(cluster.ioCtxCreate(pool));
        } catch (RadosException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取文件列表
     *
     * @param rbd
     */
    public List<RbdImageInfo> getFileList(Rbd rbd) {
        List<RbdImageInfo> rbdImageInfos=new ArrayList<>();
        try {
            List<String> fileList = Arrays.asList(rbd.list());
            fileList.forEach(fileName -> {
                RbdImageInfo fileInfo = getFileInfo(rbd, fileName);
                rbdImageInfos.add(fileInfo);
            });
        } catch (RbdException e) {
            e.printStackTrace();
        }
        return rbdImageInfos;
    }

    /**
     * 获取文件详细信息
     *
     * @param rbd
     * @param fileName
     */
    private RbdImageInfo getFileInfo(Rbd rbd, String fileName) {
        try {
            RbdImage rbdImage = rbd.open(fileName);
            return rbdImage.stat();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 创建文件 1
     *
     * @param rbd
     * @param fileName
     * @param imageSize
     */
    public void createRbdFormat1(Rbd rbd, String fileName, long imageSize) {
        try {
            rbd.create(fileName, imageSize);
            RbdImage image = rbd.open(fileName);
            boolean oldFormat = image.isOldFormat();
            System.out.println("imageFormat:===========================" + oldFormat);
            rbd.close(image);
        } catch (RbdException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建 image  2
     *
     * @param rbd
     * @param fileName
     * @param imageSize
     */
    public static void createRbdFormat2(Rbd rbd, String fileName, long imageSize) {
        try {
            int features = (1 << 0);
            System.out.println("features==============" + features);
            rbd.create(fileName, imageSize, features, 0);
            RbdImage image = rbd.open(fileName);
            boolean oldFormat = image.isOldFormat();
            System.out.println("imageFormat:===========================" + oldFormat);
            rbd.close(image);
            image.flatten();
        } catch (RbdException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建一个image并对重设置大小为初始化大小的2倍
     *
     * @param fileName
     */
    public static void resizeImage(Rbd rbd, String fileName) {
        long initialSize = 10485760;
        long newSize = initialSize * 2;
        try {
            int features = (1 << 0);
            rbd.create(fileName, initialSize, features, 0);
            RbdImage image = rbd.open(fileName);
            image.resize(newSize);
            rbd.close(image);
        } catch (RbdException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建映像的快照
     *
     * @param imageName 映像名称
     * @param snapName  快照名称
     */
    public static void createSnap(Rbd rbd, String imageName, String snapName) {
        try {
            RbdImage image = rbd.open(imageName);
            //创建快照
            image.snapCreate(snapName);
            //保护快照可以防止快照被删除
            image.snapProtect(snapName);
            //返回一个image的所有快照
            List<RbdSnapInfo> snaps = image.snapList();
            for (RbdSnapInfo rbds : snaps) {
                System.out.println("快照名称：" + rbds.name);
                System.out.println("快照大小：" + rbds.size);
            }
        } catch (RbdException e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过快照克隆出新的image
     *
     * @param parentImageName 快照对应的image名称
     * @param snapName        快照的名称
     * @param newImageName    生成的新的image的名称
     */
    public static void copySnapToNewImage(Rbd rbd, IoCTX ioctx, String parentImageName, String snapName, String newImageName) {
        int features = (1 << 0);
        try {
            rbd.clone(parentImageName, snapName, ioctx, newImageName, features, 0);
        } catch (RbdException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除某个image的名叫 snapName的快照,需要注意的是要删除快照，必须保证快照没有copy的子image，否则会删除失败。
     *
     * @param imageName
     * @param snapName
     */
    public static void deleteSnap(String imageName, String snapName, Rbd rbd) {
        try {
            RbdImage image = rbd.open(imageName);
            image.snapUnprotect(snapName);
            image.snapRemove(snapName);
        } catch (RbdException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除文件
     *
     * @param rbd
     * @param fileName
     */
    public void cleanupFile(Rbd rbd, String fileName) {
        try {
            RbdImage image = rbd.open(fileName);
            rbd.close(image);
            rbd.remove(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
