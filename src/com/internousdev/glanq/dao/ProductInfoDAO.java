package com.internousdev.glanq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.glanq.dto.ProductInfoDTO;
import com.internousdev.glanq.util.DBConnector;

import freemarker.template.utility.DateUtil;

public class ProductInfoDAO {

	private DateUtil dateUtil = new DateUtil();

	// 商品情報をすべて取得するメソッド。
	public ArrayList<ProductInfoDTO> getProductInfoList() throws SQLException{
		ArrayList<ProductInfoDTO> productInfoList = new ArrayList<ProductInfoDTO>();
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		String sql = "SELECT * from product_info";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				ProductInfoDTO pInfo = new ProductInfoDTO();
				pInfo.setId(rs.getInt("id"));
				pInfo.setProductId(rs.getInt("product_id"));
				pInfo.setProductName(rs.getString("product_name"));
				pInfo.setProductNameKana(rs.getString("product_name_kana"));
				pInfo.setProductDescription(rs.getString("product_description"));
				pInfo.setCategoryId(rs.getInt("category_id"));
				pInfo.setPrice(rs.getInt("price"));
				pInfo.setImageFilePath(rs.getString("image_file_path"));
				pInfo.setImageFileName(rs.getString("image_file_name"));
				pInfo.setReleaseDate(rs.getDate("release_date"));
				pInfo.setReleaseCompany(rs.getString("release_company"));
				pInfo.setStatus(rs.getInt("status"));
				pInfo.setRegistDate(rs.getDate("regist_date"));
				pInfo.setUpdateDate(rs.getDate("update_date"));
				productInfoList.add(pInfo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.close();
		}
		return productInfoList;
	}


	// 以下、作成予定。

	// 商品IDを参照して、その商品の商品情報を取得するメソッド。
	public ProductInfoDTO getProductInfo(int productId){
		ProductInfoDTO productInfo = new ProductInfoDTO();
		return productInfo;
	}


	// 関連商品のリストを取得するメソッド。その商品のカテゴリIDと商品IDと、表示数に関連する数値を参照。
	public List<ProductInfoDTO> getProductInfoListByCategoryId(int categoryId, int productId, int limitOffset, int limitRowCount){
		List<ProductInfoDTO> productInfoListByCategoryId = new ArrayList<ProductInfoDTO>();
		return productInfoListByCategoryId;
	}


	// 商品検索によって対象の商品リストを取得するメソッド。検索欄のキーワードをリスト化したものならびにカテゴリIDを参照。
	public List<ProductInfoDTO> getProductInfoListByKeywords(String[] keywordsList, String categoryId){
		List<ProductInfoDTO> productInfoListByKeywords = new ArrayList<ProductInfoDTO>();
		return productInfoListByKeywords;
	}

	//管理者商品追加機能により、ProductIdの最大から自動的に+1してinsertするようにしているメソッド
    public int getMaxProductId(){

	int maxProductId = -1;

	DBConnector db = new DBConnector();
	Connection con = db.getConnection();

	String sql = "SELECT MAX(product_id) AS id FROM product_info";
	try{
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		if(rs.next()){
			maxProductId = rs.getInt("id");
		}
	}catch(SQLException e){
		e.printStackTrace();
	}finally{
		if(con !=null){
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	return maxProductId;
    }
    //管理者機能商品追加時に使われるメソッド。
    public int createProduct( int productid , String productName , String productNameKana, String productDescription,
    		int categoryId, int price, String releaseCompany , String releaseDate , String imageFileName , String userImageFileName ,int Status)throws SQLException{
    	DBConnector dbConnector = new DBConnector();
    	Connection con = dbConnector.getConnection();
    	int count = 0;
    	String sql = "insert into product_info(product_id,product_name, product_name_kana, product_description,"
    			+ "category_id ,price ,release_company, release_date, image_file_name, image_file_path, status, regist_date, update_date)"
    			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    			try{
    				PreparedStatement ps = con.prepareStatement(sql);
    				ps.setInt(1, productid);
    				ps.setString(2, productName);
    				ps.setString(3, productNameKana);
    				ps.setString(4, productDescription);
    				ps.setInt(5, categoryId);
    				ps.setInt(6, price);
    				ps.setString(7, releaseCompany);
    				ps.setString(8, releaseDate);
    				ps.setString(9, imageFileName);
    				ps.setString(10, userImageFileName);
    				ps.setInt(11, Status);
    				ps.setString(12, dateUtil.getDate());
    				ps.setString(13, dateUtil.getDate());
    				count = ps.executeUpdate();
    }catch(SQLException e){
    	e.printStackTrace();
    }finally{
    	con.close();
    }
    			return count;
    }

}