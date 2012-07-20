<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
				
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Top Songs</title>
<link href="resources/css/top-songs.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="wrapper">
<div id="header"><a href="search.html"><img src="resources/images/banner.jpg" width="918" height="153" border="0"/></a></div>
<div id="leftcol">
  <img src="resources/images/checkblank.gif"/>facet content here<br />
  <br />
  <div class="purplesubheading"><img src="resources/images/checkblank.gif"/>check your birthday!</div>
  <form name="formbday" method="get" action="bday.html" id="formbday">
    <img src="resources/images/checkblank.gif" width="7"/>
    <input type="text" name="bday" id="bday" size="15"/> 
    <input type="submit" id="btnbday" value="go"/>
  </form>
  <div class="tinynoitalics"><img src="resources/images/checkblank.gif"/>(e.g. 1965-10-31)</div>
</div>
<div id="rightcol">
  <form name="form1" method="get" action="search" id="form1">
  <div id="searchdiv">
    <input type="text" name="q" id="q" size="55"/><button type="button" id="reset_button" onclick="document.getElementById('bday').value = ''; document.getElementById('q').value = ''; document.location.href='search.html'">x</button>&#160;
    <input style="border:0; width:0; height:0; background-color: #A7C030" type="text" size="0" maxlength="0"/><input type="submit" id="submitbtn" name="submitbtn" value="search"/>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<a href="advanced.html">advanced search</a>
  </div>
  <div id="detaildiv">
  
     <c:forEach items="${songs}" var="song">
      <div>
         <div class="songname">"${song.title}" by ${song.artist}</div>
         <div class="week"> ending week: ${song.weekending} 
            (total weeks: ${song.totalweeks})</div>    
         <div class="genre">genre: ${song.genres}</div>
         <div class="description">${song.description} ...&#160;
            <a href="search/detail?uri=${song.uri}">[more]</a>
         </div>
      </div>
	</c:forEach>
	
  </div>
  </form>
</div>
<div id="footer"></div>
</div>
</body>
</html>