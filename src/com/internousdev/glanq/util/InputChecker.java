package com.internousdev.glanq.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class InputChecker {

	public List<String> docheck(String propertyName, String value, int minLength, int maxLength, boolean availableAlphabeticCharacters, boolean availableKanji, boolean availableHiragana, boolean availableHalfWidthDigits, boolean availableHalfWidthSymbols, boolean availableKatakana, boolean availableFullWidthSymbols){

		/**
		 * 入力された文字列がこちらが指定するものになっているかを確認するクラス
		 * 行う処理は
		 * ①値が空欄かどうか
		 * ②入力文字数内に収まっているかどうか
		 * ③こちらの意図した文字列が入力されているか
		 *
		 * また、
		 * ④引数として渡された「登録済パスワード」と「再確認用パスワード」が等しいかどうか
		 * という処理も行う
		 */

		List<String> resultList = new ArrayList<String>();	//結果
		List<String> messageList = new ArrayList<String>();	//メッセージ

		/**
		 * 結果を入れ、最終的に戻すリストと
		 * 実際に表示するメッセージを格納するリストを用意
		 */

		//①入力内容が空欄かどうか
		if(StringUtils.isEmpty(value)){
			resultList.add(propertyName + "を入力してください");
		}


		//②入力文字数内に収まっているかどうか
		if(value.length() < minLength || value.length() > maxLength){
			resultList.add(propertyName + "は、" + minLength + "以上" + maxLength + "以下で入力してください");
		}

		//③こちらの意図した文字列が入力されているか
		String regularExpression = "";	//正しい表現
		String errorExpression = "";	//誤った表現
		/**
		 * 正しい表現を入れるString型変数
		 * 誤った表現を入れるString型変数を用意
		 *
		 * if文、else文を用いて
		 * これから、この二つにそれぞれ文字を格納していく
		 * if文内の条件に当てはまるなら正しい表現のリストに
		 * else文内の表現に当てはまるなら誤った表現のリストに
		 * 格納する
		 *
		 * また、if文内で本来入れるべき文字列について
		 * メッセージリストにもメッセージを入れていく
		 */

		//まず始まりの括弧
		if(availableAlphabeticCharacters || availableKanji || availableHiragana || availableHalfWidthDigits || availableHalfWidthSymbols || availableKatakana || availableFullWidthSymbols){
			regularExpression += "[^";
		}

		if(!(availableAlphabeticCharacters) || !(availableKanji) || !(availableHiragana) || !(availableHalfWidthDigits) || !(availableHalfWidthSymbols) || !(availableKatakana) || !(availableHalfWidthSymbols)){
			errorExpression += "[^";
		}

		//半角英字は使用可能かどうか
		if(availableAlphabeticCharacters){
			regularExpression += "a-zA-Z";
			messageList.add( "半角英字");
		}else{
			errorExpression += "a-zA-Z";
		}

		//漢字は使用可能かどうか
		if(availableKanji){
			regularExpression += "一-龯";
			messageList.add("漢字");
		}else{
			errorExpression += "一-龯";
		}

		//ひらがなは使用可能かどうか
		if(availableHiragana){
			regularExpression += "ぁ-ん";
			messageList.add("ひらがな");
		}else{
			errorExpression += "ぁ-ん";
		}

		//半角数字は使用可能かどうか
		if(availableHalfWidthDigits){
			regularExpression += "0-9";
			messageList.add("半角数字");
		}else{
			errorExpression += "0-9";
		}

		//半角記号は使用可能かどうか
		if(availableHalfWidthSymbols){
			regularExpression += "@.,;:!#$%&'*+-/=?^_`{|}~";
			messageList.add("半角記号");
		}else{
			errorExpression += "@.,;:!#$%&'*+-/=?^_`{|}~";
		}

		//カタカナは使用可能かどうか
		if(availableKatakana){
			regularExpression += "ァ-ヺ";
			messageList.add("カタカナ");
		}else{
			errorExpression += "ァ-ヺ";
		}

		//全角記号は使用可能かどうか
		if(availableFullWidthSymbols){
			regularExpression += "＠．，；：！＃＄％＆’＊＋―／＝？＾＿｀｛｜｝～";
			messageList.add("全角記号");
		}else{
			errorExpression += "＠．，；：！＃＄％＆’＊＋―／＝？＾＿｀｛｜｝～";
		}

		//括弧を閉じる。これでそれぞれ"[^○○○]+"の形になる
		if(!StringUtils.isEmpty(regularExpression)){
			regularExpression += "]+";
		}

		if(!StringUtils.isEmpty(errorExpression)){
			errorExpression += "]+";
		}

		/**
		 * メッセージリスト内の要素をString型の変数に入れていく
		 * for文を利用し、要素の数だけ要素を変数に格納していく
		 *
		 * 同時にメッセージリストの中身を整理する
		 * 現状では「半角英字漢字ひらがな」のように隙間なく詰め込まれているため
		 * 「、」で区切る
		 * ただし、最初は必要ないためif分で分岐させていく
		 */
		String message = "";
		for(int i=0; i<messageList.size(); i++){
			if(i == 0){
				message += messageList.get(i).toString();
			}else{
				message += "、" + messageList.get(i).toString();
			}
		}

		//入力された値を正しい表現、誤った表現と比較し状況に応じてエラーメッセージを返す
		if(errorExpression.equals("")){
			if(value.matches(regularExpression)){
				resultList.add(propertyName + "は、" + message + "で入力してください。");
			}
		}else{
			if(value.matches(regularExpression) || (!value.matches(errorExpression) && value.equals(""))){
				resultList.add(propertyName + "は、" + message + "で入力してください。");
			}
		}
		return resultList;

	}

	//④引数として渡された「登録済パスワード」と「再確認用パスワード」が等しいかどうか
	public List<String> doPasswordCheck(String password, String reConfirmationPassward){
		List<String> resultList = new ArrayList<String>();
		if(!(password.equals(reConfirmationPassward))){
			resultList.add("入力されたパスワードが異なります。");
		}
		/**
		 * 引数として渡された2つのパスワードが等しいかどうか
		 * if文で分岐させる
		 *
		 * 一致しなければエラーメッセージを格納
		 */
		return resultList;
	}

}
