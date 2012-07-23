<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@	taglib	uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
				
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
    <input type="text" name="q" id="q" size="55" value="${query.parameter}" />
    <button type="button" id="reset_button" onclick="document.getElementById('bday').value = ''; document.getElementById('q').value = ''; document.location.href='search.html'">x</button>&#160;
    <input style="border:0; width:0; height:0; background-color: #A7C030" type="text" size="0" maxlength="0"/><input type="submit" id="submitbtn" name="submitbtn" value="search"/>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
    		<a href="advanced.html">advanced search</a>
  </div>
  <div id="detaildiv">
<div id="countdiv" xmlns=""><b>1</b> to <b>10</b> of 56</div>
<div id="sortbydiv" xmlns="">
             sort by: 
<select name="sortby" id="sortby" onchange="this.form.submit()">
	<c:forEach items="${sortoptions}" var="sortoption" >
		 <option value="${sortoption.option}"
			<c:if test="${sortoption.selected}" >
		 			selected="true"
		 	</c:if>
		 	>${sortoption.option}</option>
	</c:forEach>
 
</select>
</div>
  <div id="detaildiv">
  <c:if test="${mode =='list'}" >
     <c:forEach items="${songs}" var="song">
      <div>
         <div class="songname">"${song.title}" by ${song.artist}</div>
         <div class="week"> ending week: ${song.weekending} 
            (total weeks: ${song.totalweeks})</div> 
            
	         <c:if test="${song.genres!=''}">
	         	<div class="genre">genre: ${song.genres}</div>
			 </c:if>   
         
         <div class="description">
         	<c:forEach items="${song.snippets}" var="snippet" >
         		<c:choose >
         			<c:when test="${snippet.ishighlighted}" >
         				<span class="highlight">${snippet.text}</span>
         			</c:when>
         			<c:otherwise>
         				${snippet.text}
         			</c:otherwise>  		
         		</c:choose>
            </c:forEach>
            &#160;
            <a href="detail.html?uri=${song.uri}">[more]</a>

         </div>
      </div>
	</c:forEach>
	<c:if test="${fn:length(songs) eq 0}">
	       <div> Sorry, no results for your search. <br/><br/><br/></div>
	</c:if>   
  </c:if>
  <c:if test="${mode =='detail'}" >
   <div>
    <div class="songnamelarge">"${song.title}"</div>
    <c:if test="${song.albumimage !='' }" >
    	<div class="albumimage"><img src="image?uri=${song.albumimage}"/></div>
    </c:if> 
    <div class="detailitem">#1 weeks: ${song.totalweeks}</div>  
    <div class="detailitem">weeks: ${song.weeks} </div> 
    <c:if test="${song.genres !='' }" >
    	<div class="detailitem">genre: ${song.genres}</div>
    </c:if> 
    <c:if test="${song.artist !='' }" >
    	<div class="detailitem">artist: ${song.artist}</div>
    </c:if> 
    <c:if test="${song.writers !='' }" >
    	<div class="detailitem">writers: ${song.writers}</div>
    </c:if> 
    <c:if test="${song.producers !='' }" >
    	<div class="detailitem">producers: ${song.producers}</div>
    </c:if> 
    <c:if test="${song.label !='' }" >
    	<div class="detailitem">label: ${song.label}</div>
    </c:if> 
    <c:if test="${song.formats !='' }" >
    	<div class="detailitem">formats: ${song.formats}</div>
    </c:if> 
    <c:if test="${song.lengths !='' }" >
    	<div class="detailitem">lengths: ${song.lengths}</div>
    </c:if> 
    <c:if test="${song.description !='' }" >
    	<div class="detailitem">${song.description}</div>
    </c:if> 
   </div>
  
  </c:if>
  
  </div>
  </form>
</div>
<div id="footer"></div>
</div>
</body>
</html>