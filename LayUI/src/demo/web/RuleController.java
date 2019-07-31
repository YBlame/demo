package demo.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Resource;
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
import demo.tool.PageUtils;
import demo.tool.UUIDUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 首页Controller
 * @author BLAME
 *
 */
@Controller
@RequestMapping("rule")
public class RuleController {
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private Connection conn = null;
	private Connection connCreate = null;
	private List<Map<String, Object>> list;
	/**
	 * 角色页面
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping("toRuleIndex")
	public void toRoleIndex(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String guid="acc0357dd46e42129f534f4820781a5a";
		String bmc = "角色";
		request.setAttribute("guid", guid);
		request.setAttribute("bmc", bmc);
		request.getRequestDispatcher("/rule_Index.jsp?guid=" + guid + "&bmc=" + bmc ).forward(request,
				response);
	}
	/**
	 * 角色页面
	 * @throws IOException 
	 * @throws ServletException 
	 */
	static List<Object> listTree = new ArrayList<>();
	@RequestMapping("findTreeList")
	@ResponseBody
	public JSONArray findTreeList(HttpServletRequest request,HttpServletResponse response,String roleMenuGuid,String zhxxGuid) throws Exception{
		listTree=new ArrayList<>();
		PreparedStatement psta = null;
        ResultSet rs = null;
        String tn = Bmodel.findBmByGuId("5daf30f7153b4638ab306cc5ba6903a0");
        String sql ="SELECT menuGuids FROM  "+tn+" WHERE roleGuid = \'"+roleMenuGuid+"\'";
        conn = LinkSql.getConn();
        conn.setAutoCommit(false);
        psta = conn.prepareStatement(sql);
        rs = psta.executeQuery();
        String menuGuids=  "";
        while(rs.next()) {
        	menuGuids=rs.getString("menuGuids");
        }

        List<Object> treeList= findTypeTree(menuGuids,zhxxGuid);
		JSONArray json = JSONArray.fromObject(treeList);
		return json;
	}
	
	public  List<Object> findTypeTree(String menuGuids,String zhxxGuid) throws  Exception {
		PreparedStatement psta = null;
        ResultSet rs = null;
        String sql ="SELECT * FROM menu where zhxx_menu=?";
        conn = LinkSql.getConn();
        psta = conn.prepareStatement(sql);
        psta.setString(1, zhxxGuid);
        rs = psta.executeQuery();
        String [] result = null;
        int len = 0;
        if (menuGuids!=null) {
        	result = menuGuids.split(",");
        	len=result.length;
		}else{
			len=0;
		}
        while(rs.next()) {
        	String name=rs.getString("NAME");
        	int parentMenu =rs.getInt("parentMenu");
        	String guid = rs.getString("guid");
        	int id  = rs.getInt("id");
        	if(parentMenu==0){//判断是否是一级菜单
               JSONObject treeObject = new JSONObject();
               for(int a = 0;a<len;a++){
            	   if(guid.equals(result[a])){
            		   treeObject.put("checked", true);
            	   }
               }
                treeObject.put("spread", true);//是否直接展开
                treeObject.put("id", id);
                treeObject.put("guid", guid);
                treeObject.put("title", name);//tree的节点名称
                treeObject.put("children", getChildren(id,menuGuids));//孩子节点，递归遍历
                listTree.add(treeObject);
          }
        }
        conn.close();
        return listTree;
    }
	//获取树形图子类
    public static List<Object> getChildren(Integer parentId,String menuGuids) throws Exception{
    	Connection conn =LinkSql.getConn();
		PreparedStatement psta = null;
		List<Object> listChildren = new ArrayList<>();
        ResultSet rs = null;
        String sql ="SELECT * FROM menu where parentMenu=?";
        psta = conn.prepareStatement(sql);
        psta.setInt(1, parentId);
        rs = psta.executeQuery();
        String [] result = null;
        int len = 0;
        if (menuGuids!=null) {
        	result = menuGuids.split(",");
        	len=result.length;
		}else{
			len=0;
		}
        while(rs.next()) {
        	String name=rs.getString("NAME");
        	int parentMenu =rs.getInt("parentMenu");
        	String guid = rs.getString("guid");
        	int id  = rs.getInt("id");
            if(parentMenu==parentId){//判断是否是一级菜单
               JSONObject treeObject = new JSONObject();
               for(int a = 0;a<len;a++){
            	   if(guid.equals(result[a])){
            		   treeObject.put("checked", true);
            	   }
               }
                treeObject.put("id", id);
                treeObject.put("spread", true);//是否直接展开
                treeObject.put("guid", guid);
                treeObject.put("title", name);//tree的节点名称
                treeObject.put("children", getChildren(id,menuGuids));//孩子节点，递归遍历
                listChildren.add(treeObject);
            }
        }
        return listChildren;
    }
    
    /**
	 * 权限页面
	 * 页面返回值是	guidRole功能编号
	 *			guidMenu角色编号
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping("findRuleList")
	@ResponseBody
	public String findRuleList(HttpServletRequest request,HttpServletResponse response,String guidRole,String guidMenu) throws Exception{
		list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = null;
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		int columnCount = 0;
		String guid ="5855c929a66f4f39afafbe5623788837";//为描述表中编号
		String tn =Bmodel.findBmByGuId(guid);
		conn = LinkSql.getConn();
		conn.setAutoCommit(false);
		String sqlWhere = " and roleGuid = \'"+guidRole+"\' AND menuGuid=\'"+guidMenu+"\'";
		String sqlData=null;
		sqlData = "SELECT rules FROM "+tn+" WHERE 1=1 "+sqlWhere;
		try {
			ps = LinkSql.Execute(conn,sqlData,role,tn);
			rs = ps.executeQuery();
		} catch (Exception e) {
			conn.rollback();
		}
		md = rs.getMetaData();
		columnCount = md.getColumnCount();
		String rules = null;
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				rules = rs.getString("rules");
			}
		}
		return rules;
	}
	/**
	 * 在给角色添加完权限，在从本表中添加功能
	 * 在添加过程中，判断rule表中是否存在有不属于当前角色的权限设置
	 * 如果有，进行删除方法
	 * @param roleMenuGuid
	 * @param menuGuid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("inOrUpMenu")
	@ResponseBody
	public String inOrUpMenu(HttpServletRequest request,String roleMenuGuid,String menuGuid,String jsons) throws Exception{
		list = new ArrayList<Map<String, Object>>();
		HttpSession session = request.getSession();
		String role = session.getAttribute("role").toString();
		String guid ="5daf30f7153b4638ab306cc5ba6903a0";//为描述表中编号
		String tn = Bmodel.findBmByGuId(guid);
		conn = LinkSql.getConn();
		conn.setAutoCommit(false);
		String sqlWhere = " and roleGuid = \'"+roleMenuGuid+"\' ";
		String sqlData=null;
		sqlData = "SELECT roleGuid FROM "+tn+" WHERE 1=1 "+sqlWhere;
		String flagStr = null;
		try {
			//查询当前角色对应哪些菜单
			ps = LinkSql.Execute(conn,sqlData,role,tn);
			rs = ps.executeQuery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "-404";
		}
		rs.last();
		int length = rs.getRow();
		menuGuid = menuGuid.substring(0,menuGuid.length() - 1);
		if (length==1) {//存在，需要修改当前编号的数据
			int flag = 0;
			String sqlDataUpdate = "UPDATE "+tn+" SET menuGuids='"+menuGuid+"',jsons='"+jsons+"' WHERE 1=1 "+sqlWhere;
			try {
				//如果存在进行修改当前角色中的菜单
				ps = LinkSql.Execute(conn,sqlDataUpdate,role,tn);
				flag = ps.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				conn.rollback();
				flagStr = "editError";
			}
			if (flag==1) {
				flagStr = "editAdd";
			}else{
				flagStr = "editError";
			}
		}else{
			int flag =0;
			String guidRoleMenu = UUIDUtil.getUUID();
			String sqlDataUpdate = "INSERT INTO "+tn+" (guid, roleGuid, menuGuids,jsons ) VALUES ( '"+guidRoleMenu+"', '"+roleMenuGuid+"', '"+menuGuid+"','"+jsons+"' );";
			try {
				//未找到当前角色的对应菜单，进行添加操作
				ps =LinkSql.Execute(conn,sqlDataUpdate,role,tn);
				flag = ps.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				conn.rollback();
				return "-404";
			}
			if (flag==1) {
				flagStr = "editAdd";
			}else{
				flagStr = "editError";
			}
		}
		
		return flagStr;
	}
	
	 /**
		 * 获取全部菜单编号
		 * 	用于全选时使用
		 * @throws IOException 
		 * @throws ServletException 
		 */
		@RequestMapping("findAllMenu")
		@ResponseBody
		public String findAllMenu(HttpServletRequest request,HttpServletResponse response,String guidRole,String guidMenu) throws Exception{
			list = new ArrayList<Map<String, Object>>();
			HttpSession session = request.getSession();
			String role = session.getAttribute("role").toString();
			ResultSetMetaData md = null;
			int columnCount = 0;
			String guid ="73c2efa3c34f4904ae0eee4ab31dfa79";//为描述表中编号   菜单表
			String tn =Bmodel.findBmByGuId(guid);
			conn = LinkSql.getConn();
			String sqlData=null;
			sqlData = "SELECT id FROM "+tn+" WHERE 1=1 ";
			try {
				ps = LinkSql.Execute(conn,sqlData,role,tn);
				rs = ps.executeQuery();
			} catch (Exception e) {
				return "null";
			}
			md = rs.getMetaData();
			columnCount = md.getColumnCount();
			String ids = "";
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					ids += rs.getString("id")+",";
				}
			}
			ids = ids.substring(0,ids.length() - 1);
			return ids;
		}
		
		/**
		 * 赋予全部权限
		 * @throws IOException 
		 * @throws ServletException 
		 */
		static String mguid="";
		@RequestMapping("addAllRule")
		@ResponseBody
		public String addAllRule(HttpServletRequest request,HttpServletResponse response,String roleGuid,String jsons) throws Exception{
			list = new ArrayList<Map<String, Object>>();
			HttpSession session = request.getSession();
			String role = session.getAttribute("role").toString();
			String guid ="5855c929a66f4f39afafbe5623788837";//为描述表中编号
			String rules = "INSERT,EDIT,ALLDELETE,SELECT,IMPORT,EXPORT,DELETE";
			String tn = Bmodel.findBmByGuId(guid);
			conn = LinkSql.getConn();
			conn.setAutoCommit(false);
			String sqlWhere = " and roleGuid = \'"+roleGuid+"\'";
			String sqlData=null;
			sqlData = "SELECT roleGuid FROM "+tn+" WHERE 1=1 "+sqlWhere;
			Boolean flagStr = true;
			try {
				ps = LinkSql.Execute(conn,sqlData,role,tn);
				rs = ps.executeQuery();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "-404";
			}
			rs.last();
			int length = rs.getRow();
			if (length!=0) {//存在，需要修改当前编号的数据
				rs.beforeFirst();
				int flag = 0;
				String rg = null;
				while (rs.next()) {
					rg = rs.getString("roleGuid");
				}
				String sqlWhereUpdate = " and roleGuid = \'"+rg+"\'";
				String sqlDataUpdate = "delete from "+tn+"  WHERE 1=1 "+sqlWhereUpdate;
				try {
					ps = LinkSql.Execute(conn,sqlDataUpdate,role,tn);
					flag = ps.executeUpdate();
					conn.commit();
				} catch (Exception e) {
					conn.rollback();
				}
				if (flag!=0) {
					flagStr = true;
				}else{
					flagStr = false;
				}
			}
			findAllSonMenu("0");
			mguid = mguid.substring(0,mguid.length());
			String[] guids = mguid.split(",");
			for (int i = 0; i < guids.length; i++) {
				int flag =0;
				String guidRoleRule = UUIDUtil.getUUID();
				String sqlDataUpdate = "INSERT INTO "+tn+" ( guid, roleGuid, menuGuid, rules ) VALUES ('"+guidRoleRule+"', '"+roleGuid+"', '"+guids[i]+"', '"+rules+"')";
				try {
					ps = LinkSql.Execute(conn,sqlDataUpdate,role,tn);
					flag = ps.executeUpdate();
					conn.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					conn.rollback();
					return "-404";
				}
				if (flag==1) {
					flagStr = true;
				}else{
					flagStr = false;
				}
			}
			if (flagStr) {
				return "finish";
			}else{
				return "error";
			}
			
		}
		
		public  void findAllSonMenu(String parent) throws Exception{
			conn = LinkSql.getConn();
			String sqlWhere = " and parentMenu!="+parent;
			String sqlData=null;
			sqlData = "SELECT id,guid FROM menu WHERE 1=1 "+sqlWhere;
			ps = conn.prepareStatement(sqlData);
			ResultSet rsSon = ps.executeQuery();
			int id =0;
			String guid =null;
			while(rsSon.next()){
				id = rsSon.getInt("id");
				guid = rsSon.getString("guid");
				String sqlWhereRs = " and parentMenu="+id+"";
				String sqlDataRs=null;
				sqlDataRs = "SELECT id,guid FROM menu WHERE 1=1 "+sqlWhereRs;
				ps = conn.prepareStatement(sqlDataRs);
				ResultSet resultSet = ps.executeQuery();
				if (resultSet.next()) {
					while(resultSet.next()){
						Integer idSon = resultSet.getInt("id") ;
						findMenuLast(idSon);
					}
				}else{
					mguid+=guid+",";
				}
			}
		}
		private  void findMenuLast(Integer parent) throws Exception {
			// TODO Auto-generated method stub
			conn = LinkSql.getConn();
			String sqlWhere = " and parentMenu="+parent+"";
			String sqlData=null;
			sqlData = "SELECT id,guid,parentMenu FROM menu WHERE 1=1 "+sqlWhere;
			ps = conn.prepareStatement(sqlData);
			ResultSet rsLast = ps.executeQuery();
			int id=0;
			String guid = null;
			while(rsLast.next()){
				guid = rsLast.getString("guid");
				id = rsLast.getInt("id");
				String sqlWhereRs = " and parentMenu="+id;
				String sqlDataRs=null;
				sqlDataRs = "SELECT id,guid FROM menu WHERE 1=1 "+sqlWhereRs;
				ps = conn.prepareStatement(sqlDataRs);
				ResultSet resultSet = ps.executeQuery();
				if (resultSet.next()) {
					while(resultSet.next()){
						findMenuLast(resultSet.getInt("id"));
					}
				}else{
					mguid+=guid+",";
				}
			}
		}
		 /**
		 * 修改或者新增权限
		 * 页面返回值是	guidRole功能编号
		 *			guidMenu角色编号
		 * 如果存在当前功能和角色的编号就进行修改操作
		 * 不存在则相反
		 * @throws IOException 
		 * @throws ServletException 
		 */
		@RequestMapping("inOrUpRule")
		@ResponseBody
		public String inOrUpRule(HttpServletRequest request,HttpServletResponse response,String guidRole,String guidMenu,String chapterstr,String checkedData) throws Exception{
			list = new ArrayList<Map<String, Object>>();
			HttpSession session = request.getSession();
			String role = session.getAttribute("role").toString();
			String guid ="5855c929a66f4f39afafbe5623788837";//为描述表中编号
			String tn = Bmodel.findBmByGuId(guid);
			conn = LinkSql.getConn();
			conn.setAutoCommit(false);
			String sqlWhere = " and roleGuid = \'"+guidRole+"\' AND menuGuid=\'"+guidMenu+"\'";
			String sqlData=null;
			sqlData = "SELECT rules FROM "+tn+" WHERE 1=1 "+sqlWhere;
			String flagStr = null;
			try {
				ps =  LinkSql.Execute(conn,sqlData,role,tn);
				rs = ps.executeQuery();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "-404";
			}
			rs.last();
			int length = rs.getRow();
			if (length==1) {//存在，需要修改当前编号的数据
				int flag = 0;
				String sqlWhereUpdate = " and roleGuid = \'"+guidRole+"\' AND menuGuid=\'"+guidMenu+"\'";
				String sqlDataUpdate = "UPDATE "+tn+" SET rules='"+chapterstr+"' WHERE 1=1 "+sqlWhereUpdate;
				try {
					ps = LinkSql.Execute(conn,sqlDataUpdate,role,tn);
					flag = ps.executeUpdate();
					conn.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					conn.rollback();
					return "-404";
				}
				if (flag==1) {
					flagStr = "editAdd";
				}else{
					flagStr = "editError";
				}
			}else{
				int flag =0;
				String guidRoleRule = UUIDUtil.getUUID();
				String sqlDataUpdate = "INSERT INTO "+tn+" ( guid, roleGuid, menuGuid, rules) VALUES ('"+guidRoleRule+"', '"+guidRole+"', '"+guidMenu+"', '"+chapterstr+"')";
				try {
					ps = LinkSql.Execute(conn,sqlDataUpdate,role,tn);
					flag = ps.executeUpdate();
					conn.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					conn.rollback();
					return "-404";
				}
				if (flag==1) {
					flagStr = "editAdd";
				}else{
					flagStr = "editError";
				}
			}
			
			
			
			return flagStr;
		}
		
		
		
		
}