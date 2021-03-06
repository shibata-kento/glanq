package com.internousdev.glanq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.glanq.dto.ProductInfoDTO;
import com.internousdev.glanq.util.DBConnector;
import com.internousdev.glanq.util.DateUtil;


// [!]動作未確認
public class ProductInfoDAO {

	private DateUtil dateUtil = new DateUtil();

	// 商品情報をすべて取得するメソッド。
	// ProductListAction で使用。
	public List<ProductInfoDTO> getProductInfoList() throws SQLException{
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


	// 商品IDを参照して、その商品の商品情報を取得するメソッド。
	// ProductDetailsAction で使用。
	public ProductInfoDTO getProductInfo(int productId) throws SQLException{
		ProductInfoDTO pInfo = new ProductInfoDTO();
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		String sql = "SELECT * from product_info WHERE product_id = ?";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, productId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
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
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.close();
		}
		return pInfo;
	}


	// 関連商品のリストを取得するメソッド。その商品のカテゴリIDと商品IDと、表示数に関連する数値（limit ?,? の部分）を設定。
	// limitOffset ･･･ 「先頭の ○ 行を除いて」
	// limitRowCount ･･･ 「最大 □ 行を取得する」
	// ProductDetailsAction で使用。
	public List<ProductInfoDTO> getProductInfoListByCategoryId(int categoryId, int productId, int limitOffset, int limitRowCount) throws SQLException{
		ArrayList<ProductInfoDTO> productInfoListByCategoryId = new ArrayList<ProductInfoDTO>();
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		String sql = "SELECT * from product_info WHERE category_id = ? AND product_id not in (?) order by rand() limit ?, ?";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, categoryId);
			ps.setInt(2, productId);
			ps.setInt(3, limitOffset);
			ps.setInt(4, limitRowCount);
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
				productInfoListByCategoryId.add(pInfo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.close();
		}
		return productInfoListByCategoryId;
	}


	// 商品検索によって対象の商品リストを取得するメソッド。検索欄のキーワードをリスト化したものならびにカテゴリIDを参照。
	// 現在は「商品名」「商品名かな」が検索対象。
	// すべてのカテゴリ を選択した場合などは、この下にある getProductInfoListAll を用いてください。(そちらはcategoryIdが不要)
	public List<ProductInfoDTO> getProductInfoListByKeywords(String[] keywordsList, String categoryId) throws SQLException{
		ArrayList<ProductInfoDTO> productInfoListByKeywords = new ArrayList<ProductInfoDTO>();
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		String sql = "SELECT * from product_info WHERE category_id = " + categoryId + " AND ";
		boolean iFlg = true;
		// 拡張for文。キーワードが1つか複数かにより、sql文が分岐するため記述。
		for(String keyword : keywordsList){
			if(iFlg){
				sql += "(product_name like '%" + keyword + "%' or product_name_kana like '%" + keyword + "%') ";
				iFlg = false;
			}else{
				sql += "AND (product_name like '%" + keyword + "%' or product_name_kana like '%" + keyword + "%')";
			}
		}
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
				productInfoListByKeywords.add(pInfo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.close();
		}
		return productInfoListByKeywords;
	}


	// すべてのカテゴリ を選択した場合のキーワード検索。検索ワードのリストが必要。
	public ArrayList<ProductInfoDTO> getProductInfoListAll(String[] keywordsList) throws SQLException{
		ArrayList<ProductInfoDTO> productInfoListByKeywords = new ArrayList<ProductInfoDTO>();
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		String sql = "SELECT * from product_info WHERE ";
		boolean iFlg = true;
		// 拡張for文。キーワードが1つか複数かにより、sql文が分岐するため記述。
		for(String keyword : keywordsList){
			if(iFlg){
				sql += "(product_name like '%" + keyword + "%' or product_name_kana like '%" + keyword + "%') ";
				iFlg = false;
			}else{
				sql += "AND (product_name like '%" + keyword + "%' or product_name_kana like '%" + keyword + "%')";
			}
		}
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
				productInfoListByKeywords.add(pInfo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.close();
		}
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
    public int createProduct( int productid  , String productName, String productNameKana, String productDescription,
			int categoryId, int price, String releaseCompany ,String releaseDate ,int Status , String imageFilePath , String imageFileName )throws SQLException{
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		int count = 0;
		String sql = "insert into product_info(product_id,product_name, product_name_kana, product_description,"
				+ "category_id ,price ,release_company, release_date, status, image_file_path, image_file_name,  regist_date, update_date)"
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try{
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, productid);
			preparedStatement.setString(2, productName);
			preparedStatement.setString(3, productNameKana);
			preparedStatement.setString(4, productDescription);
			preparedStatement.setInt(5, categoryId);
			preparedStatement.setInt(6, price);
			preparedStatement.setString(7, releaseCompany);
			preparedStatement.setString(8, releaseDate);
			preparedStatement.setInt(9, Status);// これは何か。→特に意味はないが、チーム開発時の後々追加仕様時に
			preparedStatement.setString(10, imageFilePath);
			preparedStatement.setString(11, imageFileName);	//	纏めて9,10項目をuserImageで良いのか
			preparedStatement.setString(12, dateUtil.getDate());
			preparedStatement.setString(13, dateUtil.getDate());
			count = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return count;
	}

    //管理者変更・編集機能時に使われるメソッド
    //AdminEditDetailsCompleteAction で使用。
    public int updateProductInfo(int productid, String productName, String productNameKana, String productDescription,
    		int categoryId, int price, String releaseCompany, String releaseDate, String imageFilePath, String imageFileName)throws SQLException {
    	DBConnector dbConnector = new DBConnector();
    	Connection connection = dbConnector.getConnection();
    	int count = 0;

    	String sql = "UPDATE product_info SET "
    			+ "product_name=? , "
    			+ "product_name_kana=? , "
    			+ "product_description=? , "
    			+ "category_id=? , "
    			+ "price=? , "
    			+ "release_company=? , "
    			+ "release_date=? , "
    			+ "image_file_path=? , "
    			+ "image_file_name=? , "
    			+ "regist_date=? , "
    			+ "update_date=? "
    			+ "WHERE product_id=?";

    	try {
    		PreparedStatement preparedStatement = connection.prepareStatement(sql);
    		preparedStatement.setString(1, productName);
    		preparedStatement.setString(2, productNameKana);
    		preparedStatement.setString(3, productDescription);
    		preparedStatement.setInt(4, categoryId);
    		preparedStatement.setInt(5, price);
    		preparedStatement.setString(6, releaseCompany);
    		preparedStatement.setString(7, releaseDate);
    		preparedStatement.setString(8, imageFilePath);
    		preparedStatement.setString(9, imageFileName);
    		preparedStatement.setString(10, dateUtil.getDate());
    		preparedStatement.setString(11, dateUtil.getDate());
    		preparedStatement.setInt(12, productid);
    		count = preparedStatement.executeUpdate();

    	} catch(SQLException e) {
    		e.printStackTrace();
    	} finally {
    		connection.close();
    	}
    	return count;
    }


    //管理者機能商品削除時に使われるメソッド
	public int delete(String id) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		int count = 0;
		String sql = "delete from product_info where id=?";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, id);

			count = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

}
