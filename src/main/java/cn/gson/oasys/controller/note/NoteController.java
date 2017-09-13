package cn.gson.oasys.controller.note;


import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Matchers.longThat;
import static org.mockito.Mockito.mockingDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.gson.oasys.model.dao.BlogDao;
import cn.gson.oasys.model.dao.notedao.CatalogDao;
import cn.gson.oasys.model.dao.notedao.NoteDao;
import cn.gson.oasys.model.entity.Blog;
import cn.gson.oasys.model.entity.note.Catalog;
import cn.gson.oasys.model.entity.note.Note;


@Controller
@RequestMapping("/")
public class NoteController {
		
	Logger log=LoggerFactory.getLogger(getClass());
	
	@Autowired 
	private NoteDao noteDao;
	@Autowired
	private CatalogDao catalogDao;
	
	List<Note> noteList;
	List<Catalog> cataloglist;
    
	//保存的get方法
	@RequestMapping(value="notesave",method=RequestMethod.GET)
	public void testdfd(@RequestParam("file") MultipartFile file,HttpServletRequest request){
	}
	
	//保存的post方法
		@RequestMapping(value="notesave",method=RequestMethod.POST)
		public String testdfddf(@RequestParam("file") MultipartFile file, HttpServletRequest request){
			String type=request.getParameter("type");
			System.out.println(type);
			System.out.println("---"+file.getOriginalFilename());
			return "redirect:/noteview";
		}
	
	//查找类型
	@RequestMapping("notetype")
	public String test43(Model model,HttpServletRequest request,@Param("typeid")String id){
		long typeid=Long.valueOf(id);
		noteList =noteDao.findByTypeId(typeid);
		System.out.println(noteList);
		model.addAttribute("nlist", noteList);
		return "note/notewrite";
	}
	
	//笔记删除
	@RequestMapping("notedelete")
	public String testrw(Model model,HttpServletRequest request){
		String nid=request.getParameter("nid");
		long noteid=Long.valueOf(nid);
		noteDao.delete(noteid);
		return "redirect:/noteview";
	}
	
	//目录删除
	@RequestMapping("catadelete")
	public String testrwd(Model model,HttpServletRequest request){
		String cid=request.getParameter("cid");
		long catalogid=Long.valueOf(cid);
		catalogDao.delete(catalogid);
		return "redirect:/noteview";
	}
	
	//笔记主界面
	@RequestMapping(value="noteview",method=RequestMethod.GET)
	public String test(Model model,HttpServletRequest request){
		cataloglist=(List<Catalog>) catalogDao.findAll();
		noteList = (List<Note>) noteDao.findAll();
//		long typeid=Long.valueOf(typeId);
//		noteList =noteDao.findByTypeId(typeid);
		System.out.println(noteList);
		model.addAttribute("nlist", noteList);
		System.out.println(cataloglist);
		model.addAttribute("calist", cataloglist);
		return "note/noteview";
	}
	
	//post请求 添加类型
		@RequestMapping(value="noteview",method=RequestMethod.POST)
		public String test3332(HttpServletRequest request,@Param("title")String title){
			String catalogName=request.getParameter("name");
			catalogDao.save(new Catalog(catalogName));
			return "redirect:/noteview";
		}
	
	//显示具体信息
	@RequestMapping("noteinfo")
	public String test3(@Param("id")String id,HttpServletRequest Request){
		Long nid=Long.valueOf(id);
		Note note=noteDao.findOne(nid);
		Request.setAttribute("note", note);
		return "note/noteinfo";
	}
	
	//显示所有
	@RequestMapping(value = "notewrite", method = RequestMethod.GET)
	public String test33(Model model,HttpServletRequest request){
		if(!request.getParameter("id").equals("-2")){
			String cid=request.getParameter("id");
			Long id=Long.valueOf(cid);
			noteList=noteDao.findByCatalogId(id);
			System.out.println(noteList);
		}
		else if(request.getParameter("id").equals("-2")){
			//返回的时候跳-2 
		noteList = (List<Note>) noteDao.findAll();
		System.out.println(noteList);}
		model.addAttribute("nlist", noteList);
		return "note/notewrite";
	}
	
	//模糊查询或者是根据目录查找
	@RequestMapping(value="notewrite",method=RequestMethod.POST)
	public String test333(Model model,HttpServletRequest request){
		//模糊查找
		String title=request.getParameter("title");
		noteList =noteDao.findBytitle(title);
		System.out.println(noteList);
		//根据目录
		model.addAttribute("nlist", noteList);
		return "note/notewrite";
	}
	
	//编辑
	@RequestMapping(value="noteedit",method=RequestMethod.GET)
	public String test4(@Param("id")String id,HttpServletRequest Request){
		//目录
		cataloglist=(List<Catalog>) catalogDao.findAll();
		Request.setAttribute("calist", cataloglist);
		
		Long nid=Long.valueOf(id);
		//新建
		if(nid==-1){
			Request.setAttribute("note",null);
			System.out.println("保存一个对象");
		}
		
		//修改
		else if(nid>0){
			Note note=noteDao.findOne(nid);
			Request.setAttribute("note", note);
			System.out.println(note);
		}
		Request.setAttribute("id", nid);
		return "note/noteedit";
	}
	
	//编辑提交数据
		@RequestMapping(value="noteedit",method=RequestMethod.POST)
		public String test4e(@Param("id")String id,HttpServletRequest Request){
			
			return "note/noteedit";
		}
		
	
}
