<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@	taglib	uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
				
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Top Songs</title>
<link href="resources/css/top-songs.css" rel="stylesheet" type="text/css"/>
<script src="resources/js/top-songs.js"  type="text/javascript" ></script>
</head>
<body>
<div id="wrapper">
<div id="header"><a href="search.html"><img src="resources/images/banner.jpg" width="918" height="153" border="0"/></a></div>
<div id="leftcol">
  <c:set var="query" value="${fn:escapeXml(query.parameter)}" />
  
  <div class="purplesubheading"><img src="resources/images/checkblank.gif"/>check your birthday!</div>
  <form name="formbday" method="get" action="bday.html" id="formbday">
    <img src="resources/images/checkblank.gif" width="7"/>
    <input type="text" name="bday" id="bday" size="15"/> 
    <input type="submit" id="btnbday" value="go"/>
  </form>
  <div class="tinynoitalics"><img src="resources/images/checkblank.gif"/>(e.g. 1965-10-31)</div>
  <br/>
  <div class="padleft">
  	 <c:choose>
	   <c:when test="${login =='error' }">
	     <div class="purplesubheading">administrator login</div>
	     <form name="formlogin" method="post" action="login.html" id="formlogin">
	         <input type="text" name="username" id="username" size="15"/><br/>
	         <input type="password" name="password" id="password" size="10"/> 
	         <input type="submit" id="btnlogin" value="go"/><br/>
	         <div class="tinynoitalics">(login/password)</div>       
	     </form>
	     <div class="purplesubheading"><c:out value="${loginmsg}" /></div>
 	     
	   </c:when> 
	   <c:when test="${login =='ok' }">
	   		<c:set var="login" value="${param.login}" />
	     <div class="purplesubheading"><c:out value="${loginmsg}" /></div>
	     <a href="insert.html">insert new song</a>&#160;
	     <a href="logout.html">log out</a>
	   </c:when> 
	   <c:otherwise >
  
	     <div class="purplesubheading">administrator login</div>
	     <form name="formlogin" method="post" action="login.html" id="formlogin">
	         <input type="text" name="username" id="username" size="15"/><br/>
	         <input type="password" name="password" id="password" size="10"/> 
	         <input type="submit" id="btnlogin" value="go"/><br/>
	         <div class="tinynoitalics">(login/password)</div>       
	     </form>
	     
	   </c:otherwise> 
	 </c:choose>            
  </div> 
 
  <br/>
</div>

<div id="rightcol">
  <form name="form1" method="get" action="search" id="form1">
  <div id="searchdiv">
    <input type="text" name="q" id="q" size="55" value="${query}" />
    <button type="button" id="reset_button" onclick="document.getElementById('bday').value = ''; document.getElementById('q').value = ''; document.location.href='search.html'">x</button>&#160;
    <input style="border:0; width:0; height:0; background-color: #A7C030" type="text" size="0" maxlength="0"/><input type="submit" id="submitbtn" name="submitbtn" value="search"/>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
    		<a href="advanced.html">advanced search</a>
  </div>
  </form>
  <div id="detaildiv">
  <c:choose>
  <c:when test="${insert eq 'loaded'}">
  Song loaded.
  </c:when>
  <c:otherwise>
    <div>
       <form name="form1" method="get" action="insertsong.html">
      <table border="0" cellspacing="8" xmlns="http://www.w3.org/1999/xhtml">
        <tr>
          <td align="right" valign="top">&#160;</td>
          <td class="songnamelarge"><span class="tiny">&#160;</span><br />
            insert new song<br />
            <span class="tiny">&#160;</span></td>
        </tr>
        <tr>
          <td align="right" valign="top"> song title:</td>
          <td><input type="text" name="title" id="title" size="50"/>
            <br />
            <span class="tiny">e.g. Imma Be</span></td>
        </tr>
        <tr>
          <td align="right" valign="top">artist:</td>
          <td><input type="text" name="artist" id="artist" size="50"/>
            <br />
            <span class="tiny">e.g. Black Eyed Peas</span></td>
        </tr>
        <tr>
          <td align="right" valign="top">album:</td>
          <td><input type="text" name="album" id="album" size="50"/>
            <span class="tiny"><br />
            e.g. The E.N.D.</span><br /></td>
        </tr>
        <tr>
          <td align="right" valign="top">genres:</td>
          <td><input type="text" name="genres" id="genres" size="50"/>
            <br />
            <span class="tiny">e.g. hip-hop, electro-hop</span></td>
        </tr>
        <tr>
          <td align="right" valign="top">writers:</td>
          <td><input type="text" name="writers" id="writers" size="50"/>
            <br />
            <span class="tiny">e.g. T. Brenneck, M. Deller, D. Foder, K. Harris, Allen Pineda</span></td>
        </tr>
        <tr>
          <td align="right" valign="top">producers:</td>
          <td><input type="text" name="producers" id="producers" size="50"/>
            <br />
            <span class="tiny">e.g. will.i.am</span> <br /></td>
        </tr>
        <tr>
          <td align="right" valign="top">label:</td>
          <td><input type="text" name="label" id="label" size="50"/>
            <br />
            <span class="tiny">e.g. Interscope</span><br /></td>
        </tr>
        <tr>
          <td align="right" valign="top">description:</td>
          <td><textarea name="description" id="description" cols="40"  rows="5"></textarea>
          <br />
            <span class="tiny">e.g. "Imma Be" is a song performed by the American group...</span></td>
        </tr>
        <tr>
          <td align="right" valign="top">#1 week:</td>
          <td><input type="text" name="weeks" id="weeks" size="50"/>
            <br />
            <span class="tiny">e.g. 2010-03-06, 2010-03-13</span></td>
        </tr>
        <tr valign="top">
          <td align="right" valign="top">&#160;</td>
          <td><input type="submit" name="insertbtn" id="insertbtn" value="insert"/></td>
          </tr>
      </table>
      </form>
    </div>	
  </c:otherwise>
  </c:choose>
  </div>

</div>
<div id="footer"></div>
</div>
</body>
</html>