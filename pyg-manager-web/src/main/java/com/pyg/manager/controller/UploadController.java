package com.pyg.manager.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pyg.utils.FastDFSClient;
import com.pyg.utils.PygResult;

@RestController
public class UploadController {

	@Value("${FILE_SERVER_URL}")
	// 文件服务器地址
	private String FILE_SERVER_URL;

	@RequestMapping("upload")
	public PygResult upload(MultipartFile file) throws Exception {
//			System.out.println(file);
			// 获取文件拓展名
			String originalFilename = file.getOriginalFilename();
			// 截取文件名的后缀类型
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		try {

			// 调用工具类
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
			// 执行上传,得到是图片在服务器的相对地址group1/MM/AS/...........
			String path = fastDFSClient.uploadFile(file.getBytes(), extName);
			// 将相对地址加上服务器地址后,返回
			String url = FILE_SERVER_URL + path;
			// 上传成功
			return new PygResult(true, url);
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "上传失败");
		}

	}

}
