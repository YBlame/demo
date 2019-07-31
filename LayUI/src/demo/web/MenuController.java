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
 * @author BLAME
 *
 */
@Controller
@RequestMapping("menu")
public class MenuController {

	private PreparedStatement ps;
	private ResultSet rs;
	private Connection conn;
	private List<Map<String, Object>> list;
	
	
	/**
	 * 去菜单管理列表页面
	 * @param request
	 * @param response
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toMenu")
	public String toMenu(HttpServletRequest request, HttpServletResponse response,String guid) throws Exception{
		request.setAttribute("guid", "73c2efa3c34f4904ae0eee4ab31dfa79");
		return "/menu";
	}
	/**
	 * 加载菜单
	 * @param request
	 * @param res
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	static String html = "" ;
	@RequestMapping("toMenuIndex")
	@ResponseBody
	public String toMenuIndex(HttpServletRequest request, HttpServletResponse response,String guid,String flag,String selected) throws Exception{
		/*
		 * 定义
		 * */
		html="";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
		String tn  = null;//数据表表名，根据guid获取
		//根据guid获取数据表表名，根据规则得到描述表表名
		tn= Bmodel.findBmByGuId(guid);//描述表
		try {
			dispalyListConn(tn,selected);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "";
		}
		return html;
	}
	
    public static void dispalyListConn(String tn,String selected) throws Exception {
        Connection conn = null;
        try {
            conn = LinkSql.getConn();
            displayList(conn,0,0,tn,selected);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
			conn.close();
		}
    }
    /**
     * 递归显示父子级列表
     * @param conn
     * @param pid
     * @param level
     * @param tn
     * @throws Exception
     */
    public static void displayList(Connection conn,int pid,int level,String tn,String selected) throws Exception {
			PreparedStatement psta = null;
			ResultSet rs = null;
			if (pid==0) {
			}
			String sql = "SELECT * FROM "+tn+" WHERE parentMenu = ? and zhxx_menu=? ORDER BY sort asc";
			psta = conn.prepareStatement(sql);
			psta.setInt(1,pid);
			psta.setString(2, selected);
			rs = psta.executeQuery();
			String guidBmodel = "73c2efa3c34f4904ae0eee4ab31dfa79";
			while(rs.next()) {
				//打印输出
				String name=rs.getString("NAME");
				int parentMenu =rs.getInt("parentMenu");
				String guid = rs.getString("guid");
				String url =rs.getString("url");
				String bm = rs.getString("bm");
				String bmc= rs.getString("bmc");
				if (url==null) {
					url="";
				}
				int sort =rs.getInt("sort");
				if (sort==0) {
					sort=0;
				}
				String isshow= rs.getString("isshow");
				if (isshow==null) {
					isshow="";
				}
			    html+="<tr id='"+rs.getInt("id")+"' pId='"+parentMenu+"'>";
			    html+="	<td nowrap><i class='icon- hide'></i><a href='javascript:findMenu(\""+selected+"\",\""+bm+"\",\""+bmc+"\")'>"+name+"</a></td>";
			    html+="	<td title=''>"+url+"</td>";
			    html+="	<td style='text-align:center;'>";
			    html+="		<input type='hidden' name='ids' value='"+guid+"'/>";	
			    html+="		<input name='sorts' type='text' value='"+sort+"' style='width:50px;margin:0;padding:0;text-align:center;'>";
			    html+="	</td>";
			    html+="	<td style='text-align:center;'>"+isshow+"</td>";
			    html+="	<td nowrap>";
			    html+="		<a href='doc/toUpdateDoc?guid="+guid+"&guidBmodel="+guidBmodel+"'>修改</a>";
			    html+="		<a href='javascript:deleteDoc(\""+rs.getString("id")+"\",\""+guid+"\",\""+name+"\");' >删除</a>";
			    html+="		<a href='doc/toAddDataJsp?id="+rs.getString("id")+"&thisName="+name+"'>添加下级菜单</a>";
			    html+="	</td>";
			    html+="</tr>";
			    displayList(conn,rs.getInt("id"),level+1,tn,selected);
			}
    }

    
    static String flagStr =""; 
	/**
	 * 删除菜单及其子级菜单
	 * 删除父类之前先查询是否存在子级，若存在就先删除子级
	 * @param model
	 * @param guidBmodel
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("doDeleteMenu")
	@ResponseBody
	public Object doDeleteMenu(Integer id,String guid) throws Exception {
		String flag = null;
		try {
			forDeleteConn(id);
			flagStr = flagStr.substring(0,flagStr.length()-1);
			flag="delFinish";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			flag = "delError";
		}
		return flag;
	}
	/**
	 * 递归删除的数据库链接
	 * @throws Exception
	 */
	public void forDeleteConn(Integer id) throws Exception{
		Connection conn = null;
        try {
            conn = LinkSql.getConn();
            forDelete(conn,id);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
           conn.close();
        }
	}
	/**
	 * 递归删除过程
	 * @param conn
	 * @param thisId
	 * @throws SQLException
	 */
	public void forDelete(Connection conn, Integer thisId) throws SQLException {
		PreparedStatement psta = null;
        ResultSet rs = null;
        Integer id = null;
        String sqlDel = "delete from menu where id = ?";
    	psta = conn.prepareStatement(sqlDel);
    	psta.setInt(1, thisId);
    	int flag=0;
		flag = psta.executeUpdate();
		flagStr+=flag+",";
    	if (flag==1) {
    		String sql = "SELECT id, guid, NAME FROM layui.menu WHERE parentMenu = ? ";
            psta = conn.prepareStatement(sql);
            psta.setInt(1,thisId);
            rs = psta.executeQuery();
            while(rs.next()) {
            	id = rs.getInt("id");
            	forDelete(conn,id);
            }
		}
	}
	
	/**
	 * 从主页更改排序顺序
	 * @param ids
	 * @param sorts
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("updateSort")
	public void updateSort(String ids,String sorts, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String tn = Bmodel.findBmByGuId("73c2efa3c34f4904ae0eee4ab31dfa79");
		conn =LinkSql.getConn();
		conn.setAutoCommit(false);
		if (ids!=null||sorts!=null) {
			String[] result = ids.split(",");
			String[] s = sorts.split(",");
			for (int i = 0; i < result.length; i++) {
				String sortStr=s[i];
				if (sortStr==null||sortStr.equals("")) {
					sortStr="null";
				}else{
					sortStr="'"+s[i]+"'";
				}
				String sql = "UPDATE "+tn+" SET sort="+sortStr+" WHERE guid='"+result[i]+"'";
				try {
					ps = LinkSql.Execute(conn,sql,role,tn);
					int flag = ps.executeUpdate();
					conn.commit();
					if (flag!=1) {
						conn.rollback();
						String flagStr = "editError";
						request.getRequestDispatcher("toMenu").forward(request,
								response);
					}
				} catch (Exception e) {
					conn.rollback();
				}
			}
		}
		
		String flagStr = "editFinish";
		request.getRequestDispatcher("toMenu").forward(request,
				response);
	}
	/**
	 * 修改和添加时查询对应字段信息
	 * @param request
	 * @param res
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("findParentMenuDes")
	@ResponseBody
	public Object findParentMenuDes( HttpServletRequest request, HttpServletResponse res,String guid)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String tn = null;
		int columnCount = 0;
		ResultSetMetaData md = null;
		list = new ArrayList<Map<String, Object>>();
		tn= Bmodel.findBmByGuId(guid);//描述表
		conn =LinkSql.getConn();
		String sqlFindDes ="select zdm,zdmc,width,guid from "+tn+"_des"+" where guid = \""+guid+"\" ";
		ps = LinkSql.Execute(conn,sqlFindDes,role,tn+"_des");
		rs = ps.executeQuery();
		try {
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
		}
		return list;
	}
	
	
	/**
	 * 查询菜单管理中上级菜单
	 * @param request
	 * @param res
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("findParentMenu")
	@ResponseBody
	public Object findParentMenu( HttpServletRequest request, HttpServletResponse res,String apiVal,String api,String zdmc,String guid)
			throws Exception {
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String tn = null;
		int columnCount = 0;
		ResultSetMetaData md = null;
		list = new ArrayList<Map<String, Object>>();
		tn= Bmodel.findBmByGuId(guid);//描述表
		conn =LinkSql.getConn();
		String sqlFindDes ="SELECT id,guid,name FROM "+tn+" WHERE parentMenu=0";
		ps = LinkSql.Execute(conn,sqlFindDes,role,tn);
		rs = ps.executeQuery();
		try {
			md = rs.getMetaData(); // 获得结果集结构信息,元数据
			columnCount = md.getColumnCount(); // 获得列数
			rs.last();// 指针移到最后一条后面
			int rows = rs.getRow();
			if (rows==0) {
				String ex ="empty";
				return ex;
			}
			rs.beforeFirst();
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
		}
		return list;
		
	}
	
}