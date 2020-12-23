package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	DataSource dataSource;

	public UserDAO() {
		try {
			
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle11g");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int login(String userID, String userPassword) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try
		{
			con = dataSource.getConnection();
			String query = "select userPassword from USERS where userID = ?  " ;
			pstmt = con.prepareStatement(query);
			pstmt.setString(1,  userID);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1;	// 로그인 성공
				}
				else
					return 0;	// 비밀번호 불일치
			}
			return -1;	// 아이디가 없음
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
		return -2;	// 데이터베이스 오류
	}
	
	public int join(User user) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = dataSource.getConnection();
			String query = "INSERT INTO USERS VALUES (?, ?, ?, ?, ?)" ;
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2,  user.getUserPassword());
			pstmt.setString(3,  user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();	// 성공시 0 이상, 실패시 -1
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
