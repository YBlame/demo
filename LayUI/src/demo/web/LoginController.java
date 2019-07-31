package demo.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.dao.Bmodel;
import demo.tool.LinkSql;
import demo.tool.MD5;
/**
 * 登录页面
 * @author BLAME
 *
 */
@Controller
public class LoginController {

	private PreparedStatement ps;
	private ResultSet rs;
	private Connection conn;
	private List<Map<String, Object>> list;

	/**
	 * 去login页面
	 * 
	 * @param request
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/login")
	public String toLogin(HttpServletRequest request, HttpServletResponse res) throws Exception {
		return "login";
	}

	/**
	 * 用户登录方法， 以手机号和密码登录 判断其当前角色进入不同页面
	 * 
	 * @param request
	 * @param res
	 * @param phone
	 * @param password
	 * @throws Exception
	 */
	@RequestMapping(value = "loginIn")
	@ResponseBody
	public String loginIn(HttpServletRequest request, HttpServletResponse res, String sj, String mm)
			throws Exception {
		ResultSetMetaData md = null;
		list = new ArrayList<Map<String, Object>>();
		String returnVal =null;
		int columnCount = 0;
		conn = LinkSql.getConn();
		String role = "system";//当前角色为开发者
		String tn = Bmodel.findBmByGuId("00c99009ec2d4cb883acc9ae24f73b6e");// 根据模型表中guid判断当前表名
		String pwd = MD5.GetMd5(mm);// MD5加密进行数据库判断
		String sqlWhere = " and sj=? and mm=? ";
		String sql = "select guid, NAME, sj, email, mm, roleName,roleid  from " + tn + " where 1=1  " + sqlWhere;
		try {
			ps =LinkSql.Execute(conn, sql, role, tn);
			ps.setString(1, sj);
			ps.setString(2, pwd);
			rs = ps.executeQuery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rs.last();
		int row = rs.getRow();
		if (row == 0) {
			returnVal = "loginLose";// 当登录失败
		} else {
			rs.previous();
			md = rs.getMetaData(); // 获得结果集结构信息,元数据
			columnCount = md.getColumnCount(); // 获得列数
			HttpSession session = request.getSession();
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
					session.setAttribute(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);
			}
			returnVal = "loginfinish";// 当登录失败
		}
		return returnVal;
	}

	@RequestMapping(value = "userOut")
	public String userOut(HttpServletRequest request, HttpServletResponse res) throws Exception{
		HttpSession session =request.getSession();
		session.invalidate();
		return "redirect:login";
	}
}
