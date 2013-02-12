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
  <c:set var="maxfacets" value="8" />
  
 
  <div class="facet">
	<c:forEach items="${results.facets}" var="facet">
		<c:if test="${fn:length(facet.facetvalues) ne '0'}" >
	      <div class="purplesubheading" ><img src="resources/images/checkblank.gif"/><c:out value="${facet.name}"/></div>
	        <div>
	        <c:forEach items="${facet.facetvalues}" var="facetvalue" begin="0" end="${maxfacets-1}">
	       		<c:if test="${facetvalue.name !=''}" >		       	
			      <div class="facet-value">
	       			<c:set var="me" value="${facet.name}:&#034;${facetvalue.name}&#034;" />       			
	       			<c:choose>
	       				<c:when test="${fn:contains(query,me)}">
			       		  <c:set var="cleanquery" value="${fn:replace(query,me,'')}" />
			       		  <img src="resources/images/checkmark.gif" />
			       		  <a href="search.html?q=${fn:trim(fn:replace(cleanquery,'AND',''))}"><c:out value="${fn:toLowerCase(facetvalue.name)}"/></a> <c:out value="${facetvalue.count}" />
	       				</c:when>
	       				<c:otherwise>
			       		  <img src="resources/images/checkblank.gif" />
			       		  <a href="search.html?q=${facet.name}:%22${facetvalue.name}%22%20AND%20${query}"><c:out value="${fn:toLowerCase(facetvalue.name)}"/></a> <c:out value="${facetvalue.count}" />
	       				</c:otherwise>
	       			</c:choose>     			
	       	      </div>			
	       		</c:if>
	   		</c:forEach>
	   		</div>
	   		<div class="facet-hidden" id="${facet.name}" >
	        <c:forEach items="${facet.facetvalues}" var="facetvalue" begin="${maxfacets}" >
	       		<c:if test="${facetvalue.name !=''}" >		       	
			      <div class="facet-value">
	       			<c:set var="me" value="${facet.name}:&#034;${facetvalue.name}&#034;" />       			
	       			<c:choose>
	       				<c:when test="${fn:contains(query,me)}">
			       		  <c:set var="cleanquery" value="${fn:replace(query,me,'')}" />
			       		  <img src="resources/images/checkmark.gif" />
			       		  <a href="search.html?q=${fn:trim(fn:replace(cleanquery,'AND',''))}"><c:out value="${fn:toLowerCase(facetvalue.name)}"/></a> <c:out value="${facetvalue.count}" />
	       				</c:when>
	       				<c:otherwise>
			       		  <img src="resources/images/checkblank.gif" />
			       		  <a href="search.html?q=${facet.name}:%22${facetvalue.name}%22%20AND%20${query}"><c:out value="${fn:toLowerCase(facetvalue.name)}"/></a> <c:out value="${facetvalue.count}" />
	       				</c:otherwise>
	       			</c:choose>
	       			
	       	      </div>			
	       		</c:if>
	   		</c:forEach>
	   		
	   	    </div> 	 
 			<c:if test="${fn:length(facet.facetvalues) gt maxfacets}" >
	  			<div class="facet-toggle" id="${facet.name}_more"><img src="resources/images/checkblank.gif"/><a href="javascript:toggle('${facet.name}');" class="white">more...</a></div>
	    		<div class="facet-toggle-hidden" id="${facet.name}_less"><img src="resources/images/checkblank.gif"/><a href="javascript:toggle('${facet.name}');" class="white">less...</a></div>	   	     
			</c:if>
   		</c:if>
  		<br />  	
  	</c:forEach> 

  </div>
  
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
	     <a href="insertform.html">insert new song</a>&#160;
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

  <div id="detaildiv">

  <c:if test="${mode =='list'}" >
  
	<c:if test="${fn:length(results.songs) ne '0'}" >
	  	
		<div id="countdiv"> <b>${page.start}</b> to <b>${page.end}</b> of ${page.total}</div>
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
		
		<c:if test="${page.rangestart != page.rangeend}" >
			<div id="pagenumdiv"> 
				<c:if test="${page.previous ne '0'}" >
					<a href="search.html?q=${query}&amp;start=${page.previous}&amp;submitbtn=page" title="View previous ${page.length} results"><img src="resources/images/prevarrow.gif" class="imgbaseline"  border="0" /></a>
				</c:if>
				<c:forEach var="i" begin="${page.rangestart}" end="${page.rangeend}">
					<c:set var="pagestart" value="${(page.length * i) + 1 - page.length}"/>
					<c:choose>
		   			<c:when test="${i == page.currpage}">
		        		<b>&#160;<u><c:out value="${i}"/></u>&#160;</b>
		    		</c:when>
		    		<c:otherwise>
		        		<span class="hspace">&#160;<a href="search.html?q=${query}&amp;start=${pagestart}&amp;submitbtn=pagemin"><c:out value="${i}"/></a>&#160;</span>
		    		</c:otherwise>
					</c:choose>
		
				</c:forEach>
				<c:if test="${page.next ne '0'}" >
					<a href="search.html?q=${query}&amp;start=${page.next}&amp;submitbtn=page" title="View next ${page.length} results"><img src="resources/images/nextarrow.gif" class="imgbaseline"  border="0" /></a>
				</c:if>
			</div>
		  
		</c:if>
	
	</c:if>

     <c:forEach items="${results.songs}" var="song">
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
	<c:if test="${fn:length(results.songs) eq '0'}">
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