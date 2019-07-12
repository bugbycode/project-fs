package com.fort.webapp.controller.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
import com.util.StringUtil;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/projectMemo")
public class ProjectMemoController {
	
	@Autowired
	private ProjectMemoService projectMemoService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Value("${spring.server.projectFilePath}")
	private String basePath;
	
	@RequestMapping(value = "/testUpFile",method = {RequestMethod.POST})
	public String testUpFile(MultipartFile file) {
		return "redirect:/project/query";
	}
	
	@RequestMapping(value = "/upFile",method = {RequestMethod.POST})
	public String upFile(int projectId,String description,MultipartFile file) throws IOException {
		Employee user = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int empId = user.getId();
		Employee emp = employeeService.queryById(empId);
		String grant = emp.getProjectGrant();
		
		if(!"系统管理员".equals(user.getRole().getName())) {
			if(StringUtil.isEmpty(grant)) {
				throw new AccessDeniedException("您无权访问该信息");
			}
			if(!(grant.startsWith(projectId + ",") || grant.contains("," + projectId + ",")
					|| grant.endsWith("," + projectId) || grant.equals(String.valueOf(projectId)))) {
				throw new AccessDeniedException("您无权访问该信息");
			}
		}
		
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
	public SearchResult<ProjectMemo> queryByProjectId(
		int projectId,
		@RequestParam(name = "paramQuery",defaultValue="")
		String paramQuery,
		@RequestParam(name = "startIndex",defaultValue="0")
		int offset,
		@RequestParam(name = "pageSize",defaultValue="10")
		int limit,HttpServletRequest request){
		HttpSession session = request.getSession();
		Employee user = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int empId = user.getId();
		Employee emp = employeeService.queryById(empId);
		String grant = emp.getProjectGrant();
		
		session.setAttribute("defaultProjectId", projectId);
		
		if("系统管理员".equals(user.getRole().getName())) {
			grant = "";
		}else if(StringUtil.isEmpty(grant)) {
			return new SearchResult<ProjectMemo>();
		}
		
		return projectMemoService.search(projectId, grant, paramQuery, offset, limit);
		
		/*Map<String, Object> params = new HashMap<String,Object>();
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
		
		session.setAttribute("defaultProjectId", projectId);
		
		return newList;*/
	}
	
	@RequestMapping(value = "/download",method = {RequestMethod.GET})
	public void download(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("id") int id){
		
		Employee user = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int empId = user.getId();
		Employee emp = employeeService.queryById(empId);
		String grant = emp.getProjectGrant();
		
		ProjectMemo pm = projectMemoService.queryById(id);
		if(pm == null) {
			throw new AccessDeniedException("该记录已不存在");
		}
		
		int projectId = pm.getProjectId();
		
		if(!"系统管理员".equals(user.getRole().getName())) {
			if(StringUtil.isEmpty(grant)) {
				throw new AccessDeniedException("您无权访问该信息");
			}
			if(!(grant.startsWith(projectId + ",") || grant.contains("," + projectId + ",")
					|| grant.endsWith("," + projectId) || grant.equals(String.valueOf(projectId)))) {
				throw new AccessDeniedException("您无权访问该信息");
			}
		}
		
		if(!basePath.endsWith("/")) {
			basePath += "/";
		}
		
		File file = new File(basePath + pm.getMd5Sign());
		
		InputStream in = null;
		OutputStream out = null;
		byte[] buff = new byte[4096];
		int len = -1;
		try {
			//下载的文件携带这个名称
		    response.setHeader("Content-Disposition", "attachment;filename=" + new String(pm.getFileName().getBytes("UTF-8"),"iso-8859-1"));
		    //文件下载类型--二进制文件
		    response.setContentType("application/octet-stream");
			
		    out = response.getOutputStream();
		    
		    in = new FileInputStream(file);
		    while((len = in.read(buff)) != -1) {
		    	out.write(buff, 0, len);
		    	out.flush();
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(out != null) {
					out.close();
				}
				if(in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	    
		/*
		HttpHeaders headers = new HttpHeaders();
        // 解决中文乱码
        String downloadfile =  new String(pm.getFileName().getBytes("UTF-8"),"iso-8859-1");
        // 以下载方式打开文件
        headers.setContentDispositionFormData("attachment", downloadfile);
        // 二进制流
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED);*/
	}
	
	@RequestMapping(value = "/queryById",method = {RequestMethod.GET})
	@ResponseBody
	public ProjectMemo queryById(int id) {
		Employee user = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int empId = user.getId();
		Employee emp = employeeService.queryById(empId);
		String grant = emp.getProjectGrant();
		
		ProjectMemo pm = projectMemoService.queryById(id);
		if(pm == null) {
			throw new AccessDeniedException("该记录已不存在");
		}
		
		int projectId = pm.getProjectId();
		
		if(!"系统管理员".equals(user.getRole().getName())) {
			if(StringUtil.isEmpty(grant)) {
				throw new AccessDeniedException("您无权访问该信息");
			}
			if(!(grant.startsWith(projectId + ",") || grant.contains("," + projectId + ",")
					|| grant.endsWith("," + projectId) || grant.equals(String.valueOf(projectId)))) {
				throw new AccessDeniedException("您无权访问该信息");
			}
		}
		return projectMemoService.queryById(id);
	}
	
	@RequestMapping(value = "/delete",method = {RequestMethod.POST})
	public String delete(int id) {
		
		Employee user = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int empId = user.getId();
		Employee emp = employeeService.queryById(empId);
		String grant = emp.getProjectGrant();
		
		ProjectMemo pm = projectMemoService.queryById(id);
		if(pm == null) {
			throw new AccessDeniedException("该记录已不存在");
		}
		
		int projectId = pm.getProjectId();
		
		if(!"系统管理员".equals(user.getRole().getName())) {
			if(StringUtil.isEmpty(grant)) {
				throw new AccessDeniedException("您无权访问该信息");
			}
			if(!(grant.startsWith(projectId + ",") || grant.contains("," + projectId + ",")
					|| grant.endsWith("," + projectId) || grant.equals(String.valueOf(projectId)))) {
				throw new AccessDeniedException("您无权访问该信息");
			}
		}
		
		projectMemoService.delete(id);
		return "redirect:/project/query";
	}
}
