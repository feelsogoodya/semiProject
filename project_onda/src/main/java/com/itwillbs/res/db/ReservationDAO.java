package com.itwillbs.res.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ReservationDAO {
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	ResultSet rs = null;

	// 디비연결 메서드
	public Connection getConnection() throws Exception {
		Context init = new InitialContext();
		DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/Mysql");
		con = ds.getConnection();
		return con;
	}

	// close 메서드
	public void close() {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException ex) {
			}
		if (pstmt2 != null)
			try {
				pstmt2.close();
			} catch (SQLException ex) {
			}
		if (pstmt != null)
			try {
				pstmt.close();
			} catch (SQLException ex) {
			}
		if (con != null)
			try {
				con.close();
			} catch (SQLException ex) {
			}
	}

	public void insertReservations(ReservationDTO dto) {
		try {
			Connection con = getConnection();
			String sql2 = "select max(res_num) from reservations";
			pstmt2 = con.prepareStatement(sql2);
			rs = pstmt2.executeQuery();

			int res_num = 0;
			if (rs.next()) {
				res_num = rs.getInt("max(res_num)") + 1;
			}

			String sql="insert into reservations(res_num,cus_id,res_time,res_mem,res_date,res_use_date,res_phone,tb_num,res_name) values(?,?,?,?,?,?,?,?,?)";
			pstmt =con.prepareStatement(sql);
			pstmt.setInt(1, res_num);
			pstmt.setString(2, dto.getCus_id());
			pstmt.setString(3, dto.getRes_time());
			pstmt.setInt(4, dto.getRes_mem());
			pstmt.setTimestamp(5, dto.getRes_date());
			pstmt.setString(6, dto.getRes_use_date());
			pstmt.setString(7, dto.getRes_phone());
			pstmt.setString(8, dto.getTb_num());
			pstmt.setString(9, dto.getRes_name());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return;
	}// insertMember() 메서드
	
	// List<BoardDTO>  getReservationList() 메서드
	public List<ReservationDTO> getReservationList(String cus_id) {

		List<ReservationDTO> reservationList = new ArrayList<ReservationDTO>();
		try {
			con = getConnection();
			String sql = "select * from reservations where cus_id = ? order by res_num ";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, cus_id);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReservationDTO dto = new ReservationDTO();
				dto.setRes_num(rs.getInt("res_num"));
				dto.setCus_id(rs.getString("cus_id"));
				dto.setRes_time(rs.getString("res_time"));
				dto.setRes_mem(rs.getInt("res_mem"));
				dto.setRes_date(rs.getTimestamp("res_date"));
				dto.setRes_use_date(rs.getString("res_use_date"));
				dto.setRes_phone(rs.getString("res_phone"));
				dto.setTb_num(rs.getString("tb_num"));
				dto.setRes_name(rs.getString("res_name"));
				
				reservationList.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return reservationList;
	}// getReservationList()
	
	
    public ReservationDTO getReservation(int res_num) {
        
        ReservationDTO dto=null;
        try {
            con=getConnection();
            String sql="select * from reservations where res_num=?";
            pstmt =con.prepareStatement(sql);
            pstmt.setInt(1, res_num);
            rs=pstmt.executeQuery();
            
            if(rs.next()) {
                dto=new ReservationDTO();
                dto.setRes_num(rs.getInt("res_num"));
                dto.setCus_id(rs.getString("cus_id"));
                dto.setRes_name(rs.getString("res_name"));
                dto.setTb_num(rs.getString("tb_num"));
                dto.setRes_mem(rs.getInt("res_mem"));
                dto.setRes_use_date(rs.getString("res_use_date"));
                dto.setRes_time(rs.getString("res_time"));
                dto.setRes_phone(rs.getString("res_phone"));
                dto.setRes_date(rs.getTimestamp("res_date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return dto;
    }
	
    public void updateReservation(ReservationDTO dto) {

		try {
			con = getConnection();
			String sql = "update reservations set cus_id=?,tb_num=?,res_mem=?,res_time=?,res_date=?,res_use_date=?,res_phone=?, res_name=? where res_num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getCus_id());
			pstmt.setString(2, dto.getTb_num());
			pstmt.setInt(3, dto.getRes_mem());
			pstmt.setString(4, dto.getRes_time());
			pstmt.setTimestamp(5, dto.getRes_date());
			pstmt.setString(6, dto.getRes_use_date());
			pstmt.setString(7, dto.getRes_phone());
			pstmt.setString(8, dto.getRes_name());
			pstmt.setInt(9, dto.getRes_num());
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
    
    public int getTimeTable(Map<String, String> param) {
		
		int result = 0;
		
		try {
			con = getConnection();
			String sql ="SELECT COUNT(*) AS CNT"
					+ 	 " FROM RESERVATIONS"
					+ 	" WHERE RES_USE_DATE = ?"
					+ 	 "  AND RES_TIME = ?"
					+ 	 "  AND TB_NUM = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, param.get("res_use_date"));
			pstmt.setString(2, param.get("time"));
			pstmt.setString(3, param.get("table"));
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
                result = rs.getInt("CNT"); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}
	
    public void deleteReservation(int res_num) {
        
        try {
            con = getConnection();
            String sql = "delete from reservations where res_num=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, res_num);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }


}


