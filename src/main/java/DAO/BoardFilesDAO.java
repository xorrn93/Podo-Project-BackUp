package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import DTO.BoardFilesDTO;



public class BoardFilesDAO {
	private static BoardFilesDAO instance;

	public static BoardFilesDAO getInstance() throws Exception {
		if (instance == null) {
			instance = new BoardFilesDAO();
		}
		return instance;
	}

	private BoardFilesDAO() {
	}

	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
		return ds.getConnection();
	}
	
	public int insert(BoardFilesDTO dto) throws Exception {
		String sql = "insert into boardFiles values(filesSeq.nextval,?,?,?,'QnA')";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, dto.getOriName());
			pstat.setString(2, dto.getSysName());
			pstat.setInt(3, dto.getParentSeq());
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	
	public BoardFilesDTO select(int parentSeq ) throws Exception {
		String sql = "select * from boardFiles where parentSeq = ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, parentSeq);

			try (ResultSet rs = pstat.executeQuery();) {
				rs.next();
				BoardFilesDTO dto = new BoardFilesDTO();
				dto.setFilesSeq(rs.getInt("filesSeq"));
				dto.setOriName(rs.getString("oriName"));			
				dto.setSysName(rs.getString("sysName"));
				dto.setParentSeq(rs.getInt("parentSeq"));
				return dto;
			}
		}
	}
}
