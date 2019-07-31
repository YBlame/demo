package demo.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.dao.Bmodel;
import demo.tool.LinkSql;
import demo.tool.MD5;
import demo.tool.PageUtils;
import demo.tool.PublicMethod;
import demo.tool.UUIDUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 本类中凡是guid或者Modelguid为"73c2efa3c34f4904ae0eee4ab31dfa79"都属于菜单管理
 * 记录表，用于存储模型表中对应数据
 * @author BLAME
 *
 */
@Controller
@RequestMapping("doc")
public class DocController {

	private PreparedStatement ps;
	private ResultSet rs;
	private Connection conn;
	private List<Map<String, Object>> list;

	@RequestMapping(value = "findDoc")
	@ResponseBody
	/**
	 * 获取表头
	 * @param page
	 * @param request
	 * @param res
	 * @param guid
	 * @param num
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Object findDoc(PageUtils page, HttpServletRequest request, HttpServletResponse res, String guid, Integer num,Integer  limit)
			throws Exception {
		try {
			HttpSession session = request.getSession();
			final String user = (String) session.getAttribute("user");
			final String role = (String) session.getAttribute("role");
			list = new ArrayList<Map<String, Object>>();
			String tn = null;
			ResultSetMetaData md = null;
			int columnCount = 0;
			String destn = null;
			String sqlRale = null;
			tn= Bmodel.findBmByGuId(guid);//描述表
			destn = tn + "_des";
			String sqlDes = "select zdm,zdmc,width,formtypes from " + destn + " where 1=1 ";
			sqlDes = sqlDes + " and isshow = 1  ";
			if (user != null && role != null) {
				sqlRale = "";
				sqlDes = sqlDes + sqlRale;
			}
			sqlDes = sqlDes + " order by lisnum asc,id asc ";
			conn = LinkSql.getConn();
			ps = LinkSql.Execute(conn,sqlDes,role,destn);
			rs = ps.executeQuery();
			md = rs.getMetaData(); // 获得结果集结构信息,元数据
			columnCount = md.getColumnCount(); // 获得列数
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);
			}
		} finally{
			ps.close();
			rs.close();
			conn.close();
		}
		return list;

	}
	/**
	 * 渲染数据表格
	 * @param page
	 * @param request
	 * @param res
	 * @param guid
	 * @param num
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "findDocTable", produces = "text/html;charset=utf-8")
	@ResponseBody
	// 获取表头、表数据
	public Object findDocTable(PageUtils page, HttpServletRequest request, HttpServletResponse res, String guid,String postData, Integer num,Integer  limit)
			throws Exception {
		HttpSession session = request.getSession();
		final String role = (String) session.getAttribute("role");
		list = new ArrayList<Map<String, Object>>();
		String tn = null;
		ResultSetMetaData md = null;
		int columnCount = 0;
		String destn = null;
		tn= Bmodel.findBmByGuId(guid);//描述表
		destn = tn + "_des";
		if (limit==null) {
			limit=10;
		}
		page.setRows(limit);
		list = new ArrayList<Map<String, Object>>();
		String sqlZdmc = " ";
		conn = LinkSql.getConn();
		String sqlzdm = "select zdm from " + destn;
		ps = LinkSql.Execute(conn,sqlzdm,role,destn);
		rs = ps.executeQuery();
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				sqlZdmc = sqlZdmc + rs.getObject(i) + ",";
			}
		}
		sqlZdmc = sqlZdmc.substring(0, sqlZdmc.length() - 1);
		if (sqlZdmc.length()!=0) {
			String sqlWhere = "";
			if (postData!=null) {
				String tmp[] =postData.split("&");
				for (int i = 0; i < tmp.length; i++) {
					String s = postData.split("&")[i];
					s = s+" ";
					String name = s.split("=")[0];
					String value =s.split("=")[1];
					value=value.trim();
					if (value.length()!=0) {
						if (!value.equals("请选择")) {
							sqlWhere += " and "+name+" like '%"+value+"%' ";
						}
					}
				}
			}
			String sqlData=null;
			//用于权限设置当不是开发人员时，无法对开发和主管理员的权限进行设置。
			if (guid.equals("acc0357dd46e42129f534f4820781a5a")) {
				if (!role.equals("开发人员")) {
					sqlWhere += " and guid != 'a666df5383f345868cc5b46a257da351' AND guid !='97e115aba8124896833070694701d96b'";
				}
			}
			sqlData = "select " + sqlZdmc + ",guid from " + tn + " where 1=1 "+sqlWhere;
			ps = LinkSql.Execute(conn,sqlData,role,tn);
			rs = ps.executeQuery();
			md = rs.getMetaData();
			columnCount = md.getColumnCount();
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					if (rs.getObject(i)==null) {
						rowData.put(md.getColumnName(i), rs.getObject(i));
					}else{
						rowData.put(md.getColumnName(i), rs.getObject(i).toString());
					}
						
				}
				list.add(rowData);
			}
			
			//得到总数
			String sqlCount = "select count(*) from " + tn + " where 1=1 "+sqlWhere+" ";
				ps = conn.prepareStatement(sqlCount);
				rs = ps.executeQuery();
			md = rs.getMetaData();
			columnCount = md.getColumnCount();
			String count = null;
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					count = rs.getObject(i).toString();
				}
			}
			JSONArray json = JSONArray.fromObject(list);
			String js = json.toString();
			String jso = "{\"code\":0,\"msg\":\"\",\"count\":" + count + ",\"data\":" + js + "}";
			return jso;
		}else{
			String jso = "{\"code\":0,\"msg\":\"\",\"count\":" + 0 + ",\"data\":" + null + "}";
			return jso;
		}
		
	}
	
	
	/**
	 * 查询条件
	 * @throws Exception 
	 */
	@RequestMapping(value = "queryCondition")
	@ResponseBody
	public Object queryCondition(HttpServletRequest request, HttpServletResponse res,String guid) throws Exception{
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		try {
			/*
			 * 定义
			 * */
			list = new ArrayList<Map<String, Object>>();//实例化
			String tn  = null;//数据表表名，根据guid获取
			String desTn = null;//描述表表名
			String sqlWhere=  null;//sql语句条件
			ResultSetMetaData md = null;
			int columnCount=0;
			//根据guid获取数据表表名，根据规则得到描述表表名
			
			tn= Bmodel.findBmByGuId(guid);//描述表
			desTn = tn + "_des";
			
			//连接数据库
			conn = LinkSql.getConn();//创建对象
			sqlWhere =" and  isselect >=1 ORDER BY isselect asc";
			String sqlFind=" SELECT id,zdm,zdmc,initval,types,jsdm,isedit,api,formtypes,guid,isselect,width FROM  "+desTn+" WHERE 1=1"+sqlWhere;
			ps =LinkSql.Execute(conn,sqlFind,role,desTn);
			rs = ps.executeQuery();
			md = rs.getMetaData(); // 获得结果集结构信息,元数据
			columnCount = md.getColumnCount(); // 获得列数
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);
			}
		} finally {
			rs.close();
			ps.close();
			conn.close();
		}
		return list;
		
	}
	/**
	 * 去添加数据页面
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toAddDataJsp")
	public String toAddDataJsp(Model model, HttpServletRequest request, HttpServletResponse res, String guid,Integer id,String thisName)
			throws Exception {
			if (id!=null||thisName!=null) {
				request.setAttribute("guid", "73c2efa3c34f4904ae0eee4ab31dfa79");
		        request.setAttribute("id", id);
		        request.setAttribute("parentName", thisName);
			}else{
				request.setAttribute("guid",guid );
				request.setAttribute("id", null);
		        request.setAttribute("parentName", null);
			}
			return "/doc_add";
	}

	/**
	 * 去添加数据
	 * 
	 * @param model
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toAddData")
	@ResponseBody
	public Object toAddData(Model model, HttpServletRequest request, HttpServletResponse res, String guid,String guidBmodel)
			throws Exception {
		// 拿到guid,查询模型表表名
		list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = null;
		String tn = null;
		int columnCount = 0;
		if (guidBmodel==null||guidBmodel.equals("null")) {
			if (guid.equals("null")||guid==null) {
				guidBmodel="73c2efa3c34f4904ae0eee4ab31dfa79";
			}else{
				guidBmodel=guid;
			}
			
		}
		tn= Bmodel.findBmByGuId(guidBmodel);//描述表
		// 获取描述表字段
		rs = PublicMethod.findBmodelField(tn, guidBmodel);
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			if (rs.getObject("jsdm").equals(".")) {
				rowData.put("jsdmFlag", "0");
			}else{
				rowData.put("jsdmFlag", "1");
			}
			list.add(rowData);
		}
		rs.close();
		return list;
	}
	

	
	
	/**
	 * 查找JSDM包括级联查询操作
	 * 
	 * @param model
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("findJSDM")
	@ResponseBody
	public Object findJSDM( HttpServletRequest request, HttpServletResponse res,String apiVal,String api,String zdmc,String guid)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
				String tn = null;
				String jsdm = null;
				String sqlWhere = null;
				int columnCount = 0;
				ResultSetMetaData md = null;
				list = new ArrayList<Map<String, Object>>();
				tn= Bmodel.findBmByGuId(guid);//描述表
				conn =LinkSql.getConn();
				String sqlFindDes ="select jsdm from "+tn+"_des"+" where zdm = \""+zdmc+"\" and guid = \""+guid+"\" ";
				ps = LinkSql.Execute(conn,sqlFindDes,role,tn+"_des");
				rs = ps.executeQuery();
				while(rs!=null && rs.next()){
					int i = rs.getRow();
					jsdm = rs.getString(i);//获取当前记录的第1列数据
				}
				try {
					String sql =null;
					String selectFrom = "";
					String[] parts = jsdm.split("[|]");
					
					int index = 3;//因为根据jsdm源字段在以竖线分割后的第三个下标下(下标从零开始)
					int jsdmFlag = Integer.parseInt(parts[0]);//得到第一位为jsdm级联数量
					for (int i = 0; i <jsdmFlag ; i++) {
						selectFrom += ""+parts[index]+",";
						index+=2;
					}
					selectFrom = selectFrom.substring(0, selectFrom.length() - 1);// 数据表字段
					int sub = jsdm.lastIndexOf("||");
					if (sub ==-1) {
						sqlWhere = " ";
					}else{
						api= jsdm.substring(sub+2);
						sqlWhere =" and "+api+"=\""+apiVal+"\"   ";
					}
					sql = "select distinct "+selectFrom+" from "+parts[1]+"."+parts[2]+" where  1=1 "+ sqlWhere +"";
					ps =  LinkSql.Execute(conn,sql,role,parts[2]);
				} catch (Exception e1) {
					String ex1 ="errorJSDM";
					return ex1;
				}
				try {
					rs = ps.executeQuery();
					md = rs.getMetaData(); // 获得结果集结构信息,元数据
					columnCount = md.getColumnCount(); // 获得列数
					while (rs.next()) {
						Map<String, Object> rowData = new HashMap<String, Object>();
						for (int i = 1; i <= columnCount; i++) {
							rowData.put(md.getColumnName(i), rs.getObject(i));
						}
						list.add(rowData);
					}
				} catch (Exception e) {
					String ex ="error";
					return ex;
				}finally {
					rs.close();
					ps.close();
					conn.close();
				}
			return list;
	}
	
	/**
	 * 返回JSDM对应字段
	 * @param request
	 * @param res
	 * @param zdmc
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("findJSDMZdm")
	@ResponseBody
	public Object findJSDMZdm( HttpServletRequest request, HttpServletResponse res,String zdmc,String guid)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
				String tn = null;
				String jsdm = null;
				list = new ArrayList<Map<String, Object>>();
				tn= Bmodel.findBmByGuId(guid);//描述表
				conn =LinkSql.getConn();
				String sqlFindDes ="select jsdm from "+tn+"_des"+" where zdm = \""+zdmc+"\" and guid = \""+guid+"\"";
				ps =  LinkSql.Execute(conn,sqlFindDes,role,tn+"_des");
				rs = ps.executeQuery();
				while(rs!=null && rs.next()){
					int i = rs.getRow();
					jsdm = rs.getString(i);//获取当前记录的第1列数据
				}
				String jsdmVal = "";
				String fromVal = "";
				int jsdmFlag = 0;
				int index = 0;
				String[] parts =null;
				List val = new ArrayList<>();
				try {
					parts = jsdm.split("[|]");//分割字符串
					jsdmFlag = Integer.parseInt(parts[0]);//得到数量
					index = 3;//定位下标
					for (int i = 0; i <jsdmFlag ; i++) {
						jsdmVal += ""+parts[index]+",";//拼接源字段
						index+=2;
					}
					jsdmVal = jsdmVal.substring(0, jsdmVal.length() - 1);// 数据表字段
					parts = jsdm.split("[|]");
					jsdmFlag = Integer.parseInt(parts[0]);
					index = 4;
					for (int i = 0; i <jsdmFlag ; i++) {
						fromVal += ""+parts[index]+",";
						index+=2;
					}
					fromVal = fromVal.substring(0, fromVal.length() - 1);// 数据表字段
					val.add(0, jsdmVal);//JSDM中 源表的字段
					val.add(1, fromVal);//JSDM中 目标表字段
				} catch (Exception e1) {
					String ex1 ="errorJSDM";
					return ex1;
				}finally {
					rs.close();
					ps.close();
					conn.close();
				}
			return val;
	}
	/**
	 * 添加数据
	 * 
	 * @param model
	 * @param guid
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("doc_doAdd")
	public void boc_doAdd(Model model, String guid, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		guid = request.getParameter("guid");
		String dataName = "";
		String destn = "";
		String dataTname = null;
		ResultSetMetaData md = null;
		int columnCount = 0;
		String name = null;
		Enumeration pNames = request.getParameterNames();
		if (guid==null||guid.equals("")||guid.equals("null")) {
			guid="73c2efa3c34f4904ae0eee4ab31dfa79";
		}
			dataName =Bmodel.findBmByGuId(guid);//描述表
			dataTname =Bmodel.findBmcBybm(dataName);
		
		// 获取数据表中字段
		list = new ArrayList<Map<String, Object>>();
		String sqlZdmc = "";
		destn = dataName + "_des";
		rs = PublicMethod.findBmodelByZdm(destn);
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		// 循环出字段名
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				sqlZdmc = sqlZdmc + rs.getObject(i) + ",";

			}
		}
		rs.last();// 指针移到最后一条后面
		int rows = rs.getRow();
		String value = "";
		// 循环出字段对应值
		for (int i = 0; i < rows; i++) {
			value += "? ,";
		}
		value = value.substring(0, value.length() - 1);// 数据表字段对应值
		sqlZdmc = sqlZdmc.substring(0, sqlZdmc.length() - 1);// 数据表字段
		/**
		 * 当添加或修改时的隐藏字段，赋予默认值
		 */
		conn = LinkSql.getConn();
		String sqlXs ="select zdm,types from "+destn+" where xs=0";
		ps = conn.prepareStatement(sqlXs);
		rs = null;
		rs = ps.executeQuery();
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		String valueXs = "";
		String sqlZdmcXs = "";
		columnCount = 0;
		rs.last();
		int row =rs.getRow();
		if (row!=0) {
			rs.previous();
			while (rs.next()) {
				for (int i = 0; i < row; i++) {
					String types = rs.getString("types");
					String zdm = rs.getString("zdm");
					valueXs +=","+zdm;
					switch (types) {
					case "datetime":
						sqlZdmcXs +=",NOW()";
						break;
					case "varchar":
						sqlZdmcXs +=",null";
						break;
					case "int":
						sqlZdmcXs +=",1";
						break;
					case "data":
						sqlZdmcXs +=",CURDATE()";
						break;
					}
				}
			}
			value+=sqlZdmcXs;//包含添加或修改中的不显示的字段
			sqlZdmc +=valueXs;//包含添加或修改中的不显示的字段
		}
		
		
		guid = UUIDUtil.getUUID();
		String sqlFindByBm = "INSERT INTO " + dataName + "(" + sqlZdmc + ",guid)VALUES(" + value + ",\'" + guid + "\')";
		conn= LinkSql.getConn();
		conn.setAutoCommit(false);
		String flag = null;
		try {
			ps = LinkSql.Execute(conn,sqlFindByBm,role,dataName);
			for (int e = 1; e <= rows; e++) {
				while (pNames.hasMoreElements()) {
					name = (String) pNames.nextElement();
					if (name.equals("file")) {
						ps.setString(e, request.getParameter("imgUrls"));
					}else{
						if (request.getParameter(name) == "") {
							if (name.equals("parentMenu")) {
								ps.setInt(e, 0);
							}else{
								ps.setString(e, null);
							}
						} else {
							if (name.equals("mm")) {
								ps.setString(e,MD5.GetMd5(request.getParameter(name)) );
							}else{
								ps.setString(e, request.getParameter(name));
							}
						}
					}
					break;
				}
			}
			ps.executeUpdate();
			conn.commit();
			if (dataName.equals("menu")) {
				flag = "addFinish";
				//拿到选择的表名进行判断
				String bm = request.getParameter("bm");
				String zhxxGuid = request.getParameter("zhxx_menu");
				if (!bm.equals("")) {
					String newBm = bm+"_"+zhxxGuid;
					String newBmDes = bm+"_des_"+zhxxGuid;
					String bmDes = bm+"_des";
					rs = conn.getMetaData().getTables(null, null, newBm, null);
					if (rs.next()) {
						request.getRequestDispatcher("../menu/toMenu").forward(request,
								response);
					}else{
						conn = LinkSql.getConn();
						conn.setAutoCommit(false);
						String sqlCopyBm = "create table "+newBm+" like "+bm+" ";
						ps = conn.prepareStatement(sqlCopyBm);
						int flagInt = ps.executeUpdate();
						if (flagInt==0) {
							conn.commit();
							String sqlCopyBmData = "INSERT INTO  "+newBm+" select * from "+bm+"";
							ps = conn.prepareStatement(sqlCopyBmData);
							flagInt = ps.executeUpdate();
							conn.commit();
							String sqlCopyBmDes= "create table "+newBmDes+" like "+bmDes+" ";
							ps = conn.prepareStatement(sqlCopyBmDes);
							flagInt = ps.executeUpdate();
							if (flagInt==0) {
								conn.commit();
								String sqlCopyBmDesData= "INSERT INTO  "+newBmDes+" select * from "+bmDes+"";
								ps = conn.prepareStatement(sqlCopyBmDesData);
								flagInt = ps.executeUpdate();
								conn.commit();
								request.getRequestDispatcher("../menu/toMenu").forward(request,
										response);
							}
						}
					}
				}else{
					request.getRequestDispatcher("../menu/toMenu").forward(request,
							response);
				}
			}else{
				flag = "addFinish";
				request.getRequestDispatcher("/doc_Index.jsp?flag=" + flag + "&bmc=" + dataTname).forward(request,
						response);
			}
		} catch (Exception e) {
				e.printStackTrace();
				conn.rollback();
				flag = "addError";
				request.getRequestDispatcher("/doc_add.jsp?flag=" + flag).forward(request, response);
		}finally {
			rs.close();
			ps.close();
			conn.close();
		}
	}

	/**
	 * 删除数据信息
	 * 
	 * @param model
	 * @param guidBmodel
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("deleteDoc")
	@ResponseBody
	public Object deleteDoc(HttpServletRequest request,Model model, String guidBmodel, String guid) throws Exception {
		String bmodelName = null;
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String flag = null;
		bmodelName = Bmodel.findBmByGuId(guidBmodel);//描述表
		conn = LinkSql.getConn();
		conn.setAutoCommit(false);
		String sql = "DELETE FROM " + bmodelName + " WHERE guid= \'" + guid + "\'";
		try {
			ps = LinkSql.Execute(conn,sql,role,bmodelName);
			ps.executeUpdate();
			conn.commit();
			flag = "delFinish";
		} catch (Exception e) {
			conn.rollback();
			flag = "delError";
		}
		return flag;
	}

	/**
	 * 去修改页面
	 * 
	 * @param request
	 * @param res
	 * @param guid
	 * @param guidBmodel
	 * @throws Exception
	 */
	@RequestMapping("toUpdateDoc")
	public void toUpdate(HttpServletRequest request, HttpServletResponse res, String guid, String guidBmodel)
			throws Exception {
		request.getRequestDispatcher("/doc_edit.jsp?guid="+guid+"&guidBmodel="+guidBmodel).forward(request, res);
	}

	/**
	 * 修改页面初始化； 添加表单 回显数据
	 * 
	 * @param model
	 * @param request
	 * @param res
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toUpdateData")
	@ResponseBody
	public Object toUpdateData(Model model, HttpServletRequest request, HttpServletResponse res, String guid,
			String guidBmodel) throws Exception {
		// 拿到guid,查询模型表表名
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		conn = LinkSql.getConn();
		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new TreeMap<String, Object>();
		ResultSetMetaData md = null;
		String tn = null;
		int columnCount = 0;
		//获取表名
		if (guidBmodel==null||guidBmodel.equals("")) {
			guidBmodel="73c2efa3c34f4904ae0eee4ab31dfa79";
		}
		
		tn = Bmodel.findBmByGuId(guidBmodel);
		// 获取数据表中字段
		String sqlZdmc = "";
		String destn = tn + "_des";
		rs = PublicMethod.findBmodelByZdm(destn);
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		// 循环出字段名
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				sqlZdmc = sqlZdmc + rs.getObject(i) + ",";

			}
		}
		sqlZdmc = sqlZdmc.substring(0, sqlZdmc.length() - 1);
		String sqlData = "select " + sqlZdmc + ",guid from " + tn + " where guid = \'"+guid+"\' ";
		ps = LinkSql.Execute(conn,sqlData,role,tn);
		rs = ps.executeQuery();
		md = rs.getMetaData();
		columnCount = md.getColumnCount();
		int parentId = 0;
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				if (md.getColumnName(i).equals("parentMenu")) {
					if (rs.getObject(i)!=null) {
						parentId = Integer.parseInt(rs.getObject(i).toString());
					}
					
				}
				map.put(md.getColumnName(i), rs.getObject(i));
			}
		}
		if (parentId!=0) {
			String sqlFindDes ="SELECT name FROM "+tn+" WHERE id="+parentId+"";
			ps = LinkSql.Execute(conn,sqlFindDes,role,tn);
			rs = ps.executeQuery();
			String parentName = null;
			md = rs.getMetaData();
			Integer cc = md.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= cc; i++) {
					parentName = rs.getObject(i).toString();
				}
			}
			map.put("parentName",parentName);
		}
		rs.close();
		ps.close();
		conn.close();
		
		return map;
	}
	/**
	 * 返回表单字段
	 * @param model
	 * @param request
	 * @param res
	 * @param guid
	 * @param guidBmodel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("returnZdmList")
	@ResponseBody
	public Object returnZdmList(Model model, HttpServletRequest request, HttpServletResponse res, String guid,
			String guidBmodel) throws Exception {
		// 获取描述表字段
		list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = null;
		String tn = null;
		int columnCount = 0;
		if (guidBmodel.equals("")||guidBmodel==null) {
			guidBmodel = "73c2efa3c34f4904ae0eee4ab31dfa79";//菜单表
		}
		tn = Bmodel.findBmByGuId(guidBmodel);
		// 获取数据表中字段
		rs = PublicMethod.findBmodelField(tn, guidBmodel);
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
				if (rs.getObject("jsdm").equals(".")) {
					rowData.put("jsdmFlag", "0");
				}else{
					rowData.put("jsdmFlag", "1");
				}
			}
			list.add(rowData);
		}
		rs.close();
		ps.close();
		conn.close();
		return list;
	}
	/**
	 * 做修改操作
	 * @param model
	 * @param guid
	 * @param guidBmodel
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("doc_doEdit")
	public void doc_doEdit(Model model, String guid,String guidBmodel, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		guidBmodel = request.getParameter("guidBmodel");//描述表GUID;做查询表名和字段名操作
		guid =request.getParameter("guid"); //数据表GUID;做修改数据操作
		String dataName = "";
		String destn = "";
		String dataTname = null;
		ResultSetMetaData md = null;
		int columnCount = 0;
		String name = null;
		Enumeration pNames = request.getParameterNames();
		dataName = Bmodel.findBmByGuId(guidBmodel);
		dataTname = Bmodel.findBmcBybm(dataName);
		// 获取数据表中字段
		list = new ArrayList<Map<String, Object>>();
		String sqlZdmc = "";
		destn = dataName + "_des";
		rs = PublicMethod.findBmodelByZdm(destn);
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		// 循环出字段名
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				sqlZdmc = sqlZdmc + rs.getObject(i) + "=?,";
			}
		}
		rs.last();// 指针移到最后一条后面
		int rows = rs.getRow();
		sqlZdmc = sqlZdmc.substring(0, sqlZdmc.length() - 1);// 数据表字段
		/**
		 * 当添加或修改时的隐藏字段，赋予默认值
		 */
		conn = LinkSql.getConn();
		String sqlXs ="select zdm,types from "+destn+" where xs=0";
		ps = conn.prepareStatement(sqlXs);
		rs = ps.executeQuery();
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		String valueXs = "";
		rs.last();
		int row = rs.getRow();
		if (row!=0) {
			rs.previous();
			while (rs.next()) {
				for (int i = 0; i < row; i++) {
					String types = rs.getString("types");
					String zdm = rs.getString("zdm");
					switch (types) {
					case "datetime":
						valueXs +=","+zdm+"=NOW() ";
						break;
					case "varchar":
							valueXs +=","+zdm+"=null";
						break;
					case "int":
						if (zdm.equals("parentMenu")) {
							valueXs +=","+zdm+"=0";
						}else{
							valueXs +=","+zdm+"=1";
						}
						break;
					case "data":
						valueXs +=","+zdm+"=CURDATE()";
						break;
					}
				}
			}
			sqlZdmc+=valueXs;//包含添加或修改中的不显示的字段
		}
		conn = LinkSql.getConn();
		conn.setAutoCommit(false);
		String sql = "UPDATE "+dataName+" SET "+sqlZdmc+" WHERE guid =\'"+guid+"\'";
		ps = LinkSql.Execute(conn,sql,role,dataName);
		for (int e = 1; e <= rows; e++) {
			while (pNames.hasMoreElements()) {
				name = (String) pNames.nextElement();
				if (request.getParameter(name) == "") {
					ps.setString(e, null);
				} else {
					if (name.equals("mm")) {
						ps.setString(e,MD5.GetMd5(request.getParameter(name)) );
					}else{
						ps.setString(e, request.getParameter(name));
					}
				}
				break;
			}
		}
		String flag = null;
		try {
			ps.executeUpdate();
			conn.commit();
			if (dataName.equals("menu")) {
				flag = "editFinish";
				request.getRequestDispatcher("../menu/toMenu").forward(request,
						response);
			}else{
				flag = "editFinish";
				request.getRequestDispatcher("/doc_Index.jsp?flag=" + flag + "&bmc=" + dataTname +"&guidBmodel="+guid +" &guid="+guidBmodel).forward(request,
						response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			flag = "editError";
			request.getRequestDispatcher("/doc_edit.jsp?flag=" + flag + "&bmc=" + dataTname +" &guidBmodel="+guidBmodel+" &guid="+guid).forward(request,
					response);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 角色页面
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping("findRoleList")
	@ResponseBody
	public String findRoleList(HttpServletRequest request,HttpServletResponse response,String guid,Integer total) throws Exception{
		list = new ArrayList<Map<String, Object>>();
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String tn = null;
		ResultSetMetaData md = null;
		int columnCount = 0;
		String destn = null;
		guid ="acc0357dd46e42129f534f4820781a5a";
		tn = Bmodel.findBmByGuId(guid);
		destn = tn + "_des";
		list = new ArrayList<Map<String, Object>>();
		String sqlZdmc = " ";
		conn = LinkSql.getConn();
		conn.setAutoCommit(false);
		String sqlzdm = "select zdm from " + destn;
		ps = LinkSql.Execute(conn,sqlzdm,role,destn);
		rs = ps.executeQuery();
		md = rs.getMetaData(); // 获得结果集结构信息,元数据
		columnCount = md.getColumnCount(); // 获得列数
		//得到分页数据
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				sqlZdmc = sqlZdmc + rs.getObject(i) + ",";
			}
		}
		sqlZdmc = sqlZdmc.substring(0, sqlZdmc.length() - 1);
		String sqlWhere = "";
		String sqlData=null;
		sqlData = "select guid,englishName,roleName,ssjg from " + tn + " where 1=1 "+sqlWhere;
		try {
			ps =  LinkSql.Execute(conn,sqlData,role,tn);
			rs = ps.executeQuery();
		} catch (Exception e) {
			conn.rollback();
		}
		md = rs.getMetaData();
		columnCount = md.getColumnCount();
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		}
		
		//得到总数
		String sqlCount = "select count(*) from " + tn + " where 1=1 "+sqlWhere+" ";
		try {
			ps =   LinkSql.Execute(conn,sqlCount,role,tn);
			rs = ps.executeQuery();
		} catch (Exception e) {
			conn.rollback();
		}
		md = rs.getMetaData();
		columnCount = md.getColumnCount();
		String count = null;
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				count = rs.getObject(i).toString();
			}
		}
		JSONArray json = JSONArray.fromObject(list);
		String js = json.toString();
		String jso = "{\"code\":0,\"msg\":\"\",\"count\":" + count + ",\"data\":" + js + "}";
		return jso;
	}
	
	
    
    
	
	
	
		
		
}




