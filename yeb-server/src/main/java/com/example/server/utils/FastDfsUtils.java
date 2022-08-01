package com.example.server.utils;

import cn.hutool.core.io.resource.ClassPathResource;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


@Component
public class FastDfsUtils {
    private final static Logger LOGGER= LoggerFactory.getLogger(FastDfsUtils.class);
//    初始化客户端
    public FastDfsUtils(){
        try {
//            获取配置文件的绝对路径
            String filePath=new ClassPathResource("fdfs_client.conf")
                    .getFile()
                    .getAbsolutePath();
            ClientGlobal.init(filePath);
        } catch (MyException | IOException e) {
            LOGGER.error("初始化失败",e);
        }
    }

//    获取Tracker服务器端
    private static TrackerServer getTrackerServer() throws IOException {
//        首先新建一个TrackerClient
        TrackerClient trackerClient=new TrackerClient();
        return trackerClient.getConnection();
    }
//    生成Storage客户端
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer=getTrackerServer();
        return new StorageClient(trackerServer,null);
    }

//    上传文件
    public static String[] upload(MultipartFile file){
//        首先获取传过来的文件的文件名
        String filename=file.getOriginalFilename();
        LOGGER.info("File Name: "+filename);
//        记录一下开始时间
        long startTime=System.currentTimeMillis();
//        上传的结果
        String[] uploadResults=null;
        StorageClient storageClient=null;

//        通过storageClient来完成文件上传
        try {
            storageClient=getStorageClient();
//            开始上传
            try {
                assert filename != null;
                uploadResults=storageClient.upload_file(file.getBytes()
                        ,filename.substring(filename.lastIndexOf('.')+1)
                        ,null);
            } catch (MyException e) {
                e.printStackTrace();
                LOGGER.error(filename+" 出错了",e);
            }

        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(filename+" 出错了",e);
        }
        LOGGER.info(filename+"花费了"+(System.currentTimeMillis()-startTime)+" ms");
//        验证一下上传结果:没有上传内容，但是客户端获取到了
        if (uploadResults==null){
            assert storageClient != null;
            LOGGER.error("上传失败,错误信息: "+storageClient.getErrorCode());
            return null;
        }
//        上传成功就直接返回上传结果
        LOGGER.info("上传 "+filename+" 成功,groupName: "+uploadResults[0]+" ,remoteFilename: "+uploadResults[1]);
        return uploadResults;
    }
//    获取文件的信息
    public static FileInfo getFileInfo(String groupName,String remoteFilename){
        try {
//            需要通过StorageClient获取文件信息
            StorageClient storageClient=getStorageClient();
            return storageClient.get_file_info(groupName,remoteFilename);
        } catch (MyException | IOException e) {
            e.printStackTrace();
            LOGGER.error("获取文件信息出错",e);
        }
        return null;
    }
//    获取文件开头路径
    public static String getTrackerUrl() throws IOException {
        String hostString=getTrackerServer().getInetSocketAddress().getHostString();
        return "https://" +hostString+":8888/";
    }
//    下载文件
    public static InputStream downFile(String groupName,String remoteFilename){
        try {
            StorageClient storageClient=getStorageClient();
            byte[] bytes=storageClient.download_file(groupName,remoteFilename);
            return new ByteArrayInputStream(bytes);
        } catch (MyException | IOException e) {
            e.printStackTrace();
            LOGGER.error("下载出错",e);
        }
        return null;
    }
//    删除文件
    public static void deleteFile(String groupName,String remoteFilename) throws IOException, MyException {
        StorageClient storageClient=getStorageClient();
        int i=storageClient.delete_file(groupName,remoteFilename);
        LOGGER.info("删除成功 "+i);
    }

}
