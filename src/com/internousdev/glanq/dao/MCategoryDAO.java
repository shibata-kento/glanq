package com.internousdev.glanq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.internousdev.glanq.dto.MCategoryDTO;
import com.internousdev.glanq.util.DBConnector;



public class MCategoryDAO {

	//DBに接続するための準備
	public List<MCategoryDTO> getMCategoryList(){
	DBConnector dbConnector=new DBConnector();
	Connection connection=dbConnector.getConnection();
	List<MCategoryDTO> mCategoryDtoList=new ArrayList<MCategoryDTO>();

	//sql文にてテーブル"m_category"内の全てのデータの取得
	String sql="SELECT * from m_category";
	try{
	PreparedStatement preparedStatement=connection.prepareStatement(sql);
	ResultSet resultSet=preparedStatement.executeQuery();

	//各変数にデータを追加
	// resultSet.getInt("categoryId")のようになっていたのでそれぞれ修正しました。
	while(resultSet.next()){
	MCategoryDTO mCategoryDto=new MCategoryDTO();
	mCategoryDto.setId(resultSet.getInt("id"));
	mCategoryDto.setCategoryId(resultSet.getInt("category_id"));
	mCategoryDto.setCategoryName(resultSet.getString("category_name"));
	mCategoryDto.setCategoryDescription(resultSet.getString("category_description"));
	mCategoryDto.setInsertDate(resultSet.getDate("insert_date"));
	mCategoryDto.setUpdateDate(resultSet.getDate("update_date"));

	//追加したデータをListに格納
	mCategoryDtoList.add(mCategoryDto);
	}

//"iterator"メソッドを用いてリスト内のデータを順次参照し
//次のデータがなくなったとき空データ"null"を挿入
//"データが空である"というデータを入れることで無用なエラーの防止
	Iterator<MCategoryDTO> iterator = mCategoryDtoList.iterator();
	if(!(iterator.hasNext())) {
	mCategoryDtoList = null;
	}
	}catch(SQLException e){
	e.printStackTrace();
	}try{
	connection.close();
	}catch(SQLException e){
	e.printStackTrace();
	}
	return mCategoryDtoList;
	}
	}


