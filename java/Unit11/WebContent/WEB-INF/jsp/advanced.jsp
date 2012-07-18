<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Top Songs</title>
<link href="resources/css/top-songs.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="wrapper">
<div id="header"><a href="index.html"><img src="resources/images/banner.jpg" width="918" height="153" border="0"/></a></div>
<div id="leftcol">
  <p>&#160;</p>
  <p>&#160;</p>
  <p>&#160;</p>
  <p>&#160;</p>
  <p>&#160;</p>
  <p>&#160;</p>
  <p>&#160;</p>
</div>
<div id="rightcol">
  <div id="searchdiv">
  <form name="formadv" method="get" action="index.html" id="formadv">
  <input type="hidden" name="advanced" value="advanced"/>
  <table border="0" cellspacing="8">
    <tr>
      <td align="right">&#160;</td>
      <td colspan="4" class="songnamelarge"><span class="tiny">&#160;&#160;</span><br />
        advanced search<br />
        <span class="tiny">&#160;&#160;</span></td>
    </tr>
    <tr>
      <td align="right">search for:</td>
      <td colspan="4"><input type="text" name="keywords" id="keywords" size="50"/>
        &#160;
        <select name="type" id="type">
          <option value="all">all of these words</option>
          <option value="any">any of these words</option>
          <option value="phrase">exact phrase</option>
        </select></td>
    </tr>
    <tr>
      <td align="right">words to exclude:</td>
      <td colspan="4"><input type="text" name="exclude" id="exclude" size="50"/></td>
    </tr>
    <tr>
      <td align="right">genre:</td>
      <td colspan="4"><select name="genre" id="genre">
        <option value="all">all</option>
      </select></td>
    </tr>
    <tr>
      <td align="right">artist/writer/producer:</td>
      <td colspan="4"><input type="text" name="creator" id="creator" size="50"/></td>
    </tr>
    <tr>
      <td align="right">song title:</td>
      <td colspan="4"><input type="text" name="songtitle" id="songtitle" size="50" autocomplete="off"/></td>
    </tr>
    <tr valign="top">
      <td align="right">&#160;</td>
      <td><span class="tiny">&#160;&#160;</span><br /><input type="submit" name="submitbtn" id="submitbtn" value="search"/></td>
      <td>&#160;</td>
      <td>&#160;</td>
      <td>&#160;</td>
    </tr>
  </table>
  </form>
  </div>
</div>
<div id="footer"></div>
</div>
</body>
</html>