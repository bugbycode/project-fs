package com.fort.webapp.controller.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fort.module.employee.Employee;
import com.fort.module.project.ProjectMemo;
import com.fort.service.employee.EmployeeService;
import com.fort.service.project.ProjectMemoService;
import com.util.MD5Util;

@Controller
@RequestMapping("/projectMemo")
public class ProjectMemoController {
	
	@Autowired
	private ProjectMemoService projectMemoService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Value("${spring.server.projectFilePath}")
	private String basePath;

	@RequestMapping(value = "/upFile",method = {RequestMethod.POST})
	public String upFile(int projectId,String description,MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			return "redirect:/project/query";
        }
		
		if(!basePath.endsWith("/")) {
			basePath += "/";
		}
		
		String fileName = file.getOriginalFilename();
		
		File store = new File(basePath + new Date().getTime());
		
		File parent = store.getParentFile();
		if(!parent.exists()) {
			parent.mkdirs();
		}
		
		if(!store.exists()) {
			store.createNewFile();
			file.transferTo(store);
		}
		
		String md5 = MD5Util.getMd5(store.getAbsolutePath());
		
		File newFile = new File(basePath + md5);
		if(newFile.exists()) {
			store.delete();
		}else {
			store.renameTo(newFile);
		}
		
		ProjectMemo pm = new ProjectMemo();
		pm.setDescription(description);
		pm.setFileName(fileName);
		pm.setMd5Sign(md5);
		pm.setCreateTime(new Date());
		pm.setProjectId(projectId);
		projectMemoService.insert(pm);
		
		return "redirect:/project/query";
	}
	
	@RequestMapping(value = "/queryByProjectId",method = {RequestMethod.GET})
	@ResponseBody
	public List<ProjectMemo> queryByProjectId(int projectId){
		Employee user = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int empId = user.getId();
		Employee emp = employeeService.queryById(empId);
		String grant = emp.getProjectGrant();
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("projectId", projectId);
		List<ProjectMemo> list = projectMemoService.query(params);
		List<ProjectMemo> newList = new ArrayList<ProjectMemo>();
		if(user.getRole().getType() == 1) {
			for(ProjectMemo pm : list) {
				String idStr = String.valueOf(pm.getProjectId());
				if(grant.startsWith(idStr + ",") || grant.endsWith("," + idStr)
						|| grant.contains("," + idStr + ",") || grant.equals(idStr)) {
					newList.add(pm);
				}
			}
		}else {
			newList = list;
		}
		return newList;
	}
	
	@RequestMapping(value = "/download",method = {RequestMethod.GET})
	public ResponseEntity<byte[]> download(HttpServletRequest request, 
			@RequestParam("id") int id) throws Exception{
		ProjectMemo pm = projectMemoService.queryById(id);
		if(pm == null) {
			throw new RuntimeException("该记录已不存在");
		}
		
		if(!basePath.endsWith("/")) {
			basePath += "/";
		}
		
		File file = new File(basePath + pm.getMd5Sign());
		HttpHeaders headers = new HttpHeaders();
        // 解决中文乱码
        String downloadfile =  new String(pm.getFileName().getBytes("UTF-8"),"iso-8859-1");
        // 以下载方式打开文件
        headers.setContentDispositionFormData("attachment", downloadfile);
        // 二进制流
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/queryById",method = {RequestMethod.GET})
	@ResponseBody
	public ProjectMemo queryById(int id) {
		return projectMemoService.queryById(id);
	}
	
	@RequestMapping(value = "/delete",method = {RequestMethod.POST})
	public String delete(int id) {
		projectMemoService.delete(id);
		return "redirect:/project/query";
	}
}
