package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BbsDAO {
	
	private ResultSet rs;
	DataSource dataSource;

	public BbsDAO() 
		{
			try 
			{
				Context context = new InitialContext();
				dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle11g");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	
	public String getDate() {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		 
		
		try {
			con = dataSource.getConnection();
			String SQL = "SELECT NOW()";
			pstmt = con.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (pstmt !=null) pstmt.close();
				if (con !=null) con.close();
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return "";	// 데이터 베이스 오류
	}
	
	public int getNext() {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		 
		try {
			con = dataSource.getConnection();
			String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";
			pstmt = con.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1;	// 첫번째 게시물
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (pstmt !=null) pstmt.close();
				if (con !=null) con.close();
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return -1;	// 데이터 베이스 오류
	}
	
	public int write(String bbsTitle, String userID, String bbsContent) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			
			con = dataSource.getConnection();
			String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (pstmt !=null) pstmt.close();
				if (con !=null) con.close();
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return -1;	// 데이터 베이스 오류
	}
	
	public ArrayList<Bbs> getList() {
		
//		ArrayList<Bbs> dtos = new ArrayList<Bbs>();
		ArrayList<Bbs> list = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		try
		{
			con = dataSource.getConnection();
			
			String query = "select * " +
							" from bbs " +
							" ORDER BY bbsID DESC ";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				
				list.add(bbs);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			try
			{
				if (resultSet !=null) resultSet.close();
				if (pstmt !=null) pstmt.close();
				if (con !=null) con.close();				
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return list;
	}
 /*
	
	public ArrayList<Bbs> getList(int pageNumber){
		Connection con = null;
		PreparedStatement pstmt = null;
		String SQL = "SELECT * FROM BBS " +
						" ORDER BY bbsID DESC " ;
		ArrayList<Bbs> list = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(SQL);
//			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			// getNext() = 다음에 작성할 글의 번호
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				
				list.add(bbs);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally 
		{
			try
			{
				if (pstmt !=null) pstmt.close();
				if (con !=null) con.close();				
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		
		return list;	// 데이터 베이스 오류
	}
	
	public boolean nextPage(int pageNumber) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			
			con = dataSource.getConnection();
			String SQL = "SELECT * FROM BBS WHERE bID < ? ";
			pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally 
		{
			try
			{
				if (pstmt !=null) pstmt.close();
				if (con !=null) con.close();				
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return false;
	}
	*/
	public Bbs getBbs(int BbsID) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = dataSource.getConnection();
			String SQL = "SELECT * FROM BBS WHERE bID = ?\";";
			pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, BbsID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int update(int bID, String bTitle, String bContent) {
		Connection con = null;
		PreparedStatement pstmt = null;
		 
		try {
			con = dataSource.getConnection();
			String SQL = "UPDATE BBS SET bTitle = ?, bbsContent = ? WHERE bID = ?";
			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1, bTitle);
			pstmt.setString(2, bContent);
			pstmt.setInt(3, bID);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (pstmt !=null) pstmt.close();
				if (con !=null) con.close();
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return -1;	// 데이터 베이스 오류
	}
	
	public int delete(int bID) {
		 
		Connection con = null;
		PreparedStatement pstmt = null;
		 
		try {
			
			con = dataSource.getConnection();
			String SQL = "UPDATE BBS SET bAvailable = 0 WHERE bID = ?";
			pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, bID);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (pstmt !=null) pstmt.close();
				if (con !=null) con.close();
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return -1;	// 데이터 베이스 오류
	}
	
}

