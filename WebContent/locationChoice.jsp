<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>locationChoice</title>
</head>
<body>
<!-- ヘッダーをインクルード -->
<jsp:include page="header.jsp" />

<div id="contents">

  <h1>locationChoice画面</h1>

  <!-- とりあえずで作ったからレイアウトは許してほしい -->
 <s:iterator value="#session.destinationInfoDtoList" status="st">
  <div>
  <s:form action="LocationChoiceConfirmAction">
      <s:hidden name="parkId" value="%{id}"/>

      <s:submit value="%{firstName}"/>
      <table>
          <tr>
              <td>会場名</td>
              <td><s:property value="firstName"/></td>
          </tr>
          <tr>
              <td>会場名かな</td>
              <td><s:property value="firstNameKana"/></td>
          </tr>
          <tr>
              <td>会社名</td>
              <td><s:property value="familyName"/></td>
          </tr>
          <tr>
              <td>会社名かな</td>
              <td><s:property value="familyNameKana"/></td>
          </tr>
          <tr>
              <td>メールアドレス</td>
              <td><s:property value="email"/></td>
          </tr>
          <tr>
              <td>電話番号</td>
              <td><s:property value="telNumber"/></td>
          </tr>
          <tr>
              <td>住所</td>
              <td><s:property value="userAddress"/></td>
          </tr>
      </table>
  </s:form>
  </div>
</s:iterator>

</div>
<!-- フッターをインクルード -->
<jsp:include page="footer.jsp" />
</body>
</html>