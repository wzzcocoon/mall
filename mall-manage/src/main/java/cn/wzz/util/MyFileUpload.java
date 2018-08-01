package cn.wzz.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/** 文件上传工具类 */
public class MyFileUpload {

	/** 将文件上传,返回上传文件名 */
	public static List<String> uploadFile(MultipartFile[] files) {

		List<String> list_name = new ArrayList<String>();
		
		// 读取配置文件，返回值
		String path = MyPropertyUtil.getProperty("myUpload.properties", "windows_path");

		for (int i = 0; i < files.length; i++) {
			if (!files[i].isEmpty()) {
				// 获取每一个文件的原始名称
				String originalFilename = files[i].getOriginalFilename();
				// UUID.randomUUID();
				String name = System.currentTimeMillis() + originalFilename;
				String upload_pathname = path + "/" + name;
				try {
					// 上传每一个文件
					files[i].transferTo(new File(upload_pathname));
					//把上传的文件名保存下来
					list_name.add(name);
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list_name;
	}

}
