<?xml version="1.0" encoding="UTF-8"?>
<!--
	// 
	// Copyright (c) 2008 Beau D. Scott | http://www.beauscott.com
	// 
	// Permission is hereby granted, free of charge, to any person
	// obtaining a copy of this software and associated documentation
	// files (the "Software"), to deal in the Software without
	// restriction, including without limitation the rights to use,
	// copy, modify, merge, publish, distribute, sublicense, and/or sell
	// copies of the Software, and to permit persons to whom the
	// Software is furnished to do so, subject to the following
	// conditions:
	// 
	// The above copyright notice and this permission notice shall be
	// included in all copies or substantial portions of the Software.
	// 
	// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
	// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
	// OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
	// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
	// HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
	// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
	// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
	// OTHER DEALINGS IN THE SOFTWARE.
	// 
-->
<xsl:stylesheet version="1.0"
	xmlns:bs="http://www.beauscott.com/ref/2008/pbcd"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/02/xpath-functions">
	<xsl:output omit-xml-declaration="no" standalone="yes"
		media-type="text/xml" encoding="UTF-8" version="1.0" indent="yes"
		method="html" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

	<xsl:template match="/bs:classes">
		<html>
			<head>
				<title>AutoComplete.js 1.2 API</title>
				<link rel="stylesheet" type="text/css"
					href="assets/style.css" />
			</head>
			<body>
				<a name="top" />
				<div id="header">
					<div id="backlinks">
						<a href="http://www.beauscott.com/">
							BeauScott.com
						</a>
					</div>
					<h1>AutoComplete.js 1.2 API</h1>
				</div>

				<div id="content">

					<div style="text-align: center">
						<a href="examples.html">Basic Usage Examples</a>
						 | 
						<a href="ns_examples.html">Non-Scriptaculous Examples</a>
						<br/>
						<a href="../autocomplete.zip">Download now!</a> 
						 | 
						<a href="assets/ac.phps">Example results script</a>
					</div>

					<table>
						<tr>
							<td id="keys">
								<div id="keys_div">
									<ul>
										<xsl:for-each
											select="bs:class">
											<li>
												<a href="#{@name}"
													class="class">
													<xsl:value-of
														select="@name" />
												</a>
												<xsl:if
													test="count(bs:properties/bs:property) &gt; 0">
													<div
														class="properties">
														Properties
														<br />
														<ul>
															<xsl:for-each
																select="bs:properties/bs:property">
																<xsl:sort select="@name" />
																<li>
																	<a
																		href="#{../../@name}.{@name}" class="{@scope}_link">
																		<xsl:if test="@static"><xsl:value-of select="'*'"/></xsl:if>
																		<xsl:value-of
																			select="@name" />
																	</a>
																</li>
															</xsl:for-each>
														</ul>
													</div>
												</xsl:if>
												<xsl:if
													test="count(bs:methods/bs:method) &gt; 0">
													<div
														class="methods">
														Methods
														<br />
														<ul>
															<xsl:for-each
																select="bs:methods/bs:method">
																<xsl:sort select="@name" />
																<li>
																	<xsl:if test="@static"><xsl:value-of select="'*'"/></xsl:if>
																	<a
																		href="#{../../@name}.{@name}" class="{@scope}_link">
																		<xsl:value-of
																			select="@name" />
																	</a>
																</li>
															</xsl:for-each>
														</ul>
													</div>
												</xsl:if>
												<br />
												<br />
											</li>
										</xsl:for-each>
									</ul>
								</div>
							</td>
							<td id="descriptions">
								<xsl:for-each select="bs:class">
									<xsl:sort select="@name" />
									<a name="{@name}"></a>
									<div class="class_title">
										<a class="topAnchor"
											href="#top">
											(top)
										</a>
										<h2>
											<xsl:value-of
												select="@name" />
										</h2>
										<xsl:value-of
											select="bs:description" />
									</div>

									<xsl:if
										test="count(bs:properties/bs:property[@scope='private']) &gt; 0">
										<h3>Private Properties</h3>
										<xsl:for-each
											select="bs:properties/bs:property[@scope='private']">
											<xsl:sort select="@name" />
											<xsl:call-template
												name="property" />
										</xsl:for-each>
									</xsl:if>
									<xsl:if
										test="count(bs:properties/bs:property[@scope='protected']) &gt; 0">
										<h3>Protected Properties</h3>
										<xsl:for-each
											select="bs:properties/bs:property[@scope='protected']">
											<xsl:sort select="@name" />
											<xsl:call-template
												name="property" />
										</xsl:for-each>
									</xsl:if>
									<xsl:if
										test="count(bs:properties/bs:property[@scope='public']) &gt; 0">
										<h3>Public Properties</h3>
										<xsl:for-each
											select="bs:properties/bs:property[@scope='public']">
											<xsl:sort select="@name" />
											<xsl:call-template
												name="property" />
										</xsl:for-each>
									</xsl:if>

									<xsl:if
										test="count(bs:methods/bs:method[@scope='private']) &gt; 0">
										<h3>Private Methods</h3>
										<xsl:for-each
											select="bs:methods/bs:method[@scope='private']">
											<xsl:sort select="@name" />
											<xsl:call-template
												name="method" />
										</xsl:for-each>
									</xsl:if>

									<xsl:if
										test="count(bs:methods/bs:method[@scope='protected']) &gt; 0">
										<h3>Protected Methods</h3>
										<xsl:for-each
											select="bs:methods/bs:method[@scope='protected']">
											<xsl:sort select="@name" />
											<xsl:call-template
												name="method" />
										</xsl:for-each>
									</xsl:if>

									<xsl:if
										test="count(bs:methods/bs:method[@scope='public']) &gt; 0">
										<h3>Public Methods</h3>
										<xsl:for-each
											select="bs:methods/bs:method[@scope='public']">
											<xsl:sort select="@name" />
											<xsl:call-template
												name="method" />
										</xsl:for-each>
									</xsl:if>
									<br />
									<br />
								</xsl:for-each>
							</td>
						</tr>
					</table>
				</div>
				<div id="copyright">
					Copyright &#169; 2008 Beau D. Scott. All Rights
					Reserved.
					<a href="http://www.beauscott.com/">
						BeauScott.com
					</a>
					<br />
					Licensed under MIT-style terms
					<script
						src="http://www.google-analytics.com/urchin.js"
						type="text/javascript">
					</script>
					<script type="text/javascript"><![CDATA[
					_uacct = "UA-524142-2";
					urchinTracker();
					]]>
					</script>
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template name="typeLink">
		<xsl:param name="type" />
		<xsl:choose>
			<xsl:when
				test="count(//bs:classes/bs:class[@name = $type]) &gt; 0">
				<a href="#{$type}">
					<xsl:value-of select="$type" />
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$type" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="method">
		<div class="member">
			<a class="topAnchor" href="#top">(top)</a>
			<a name="{../../@name}.{@name}"></a>
			<h4>
				<xsl:value-of select="@name" />
				<xsl:if test="@constructor">
					<xsl:value-of select="' '" />
					<span class="constructor">(constructor)</span>
				</xsl:if>
			</h4>
			<dd>
				<code>
					<xsl:value-of select="@scope" />
					function
					<xsl:value-of select="' '" />
					<strong>
						<xsl:value-of select="@name" />
					</strong>
					(
					<xsl:for-each select="bs:parameters/bs:parameter">
						<xsl:value-of select="@name" />
						<xsl:value-of select="' : '" />
						<span class="type">
							<xsl:call-template name="typeLink">
								<xsl:with-param name="type"
									select="@type" />
							</xsl:call-template>
						</span>
						<xsl:if test="position() &lt; count(..)">
							,
						</xsl:if>
					</xsl:for-each>
					)
					<xsl:value-of select="' : '" />
					<span class="type">
						<xsl:call-template name="typeLink">
							<xsl:with-param name="type"
								select="@returnType" />
						</xsl:call-template>
					</span>

				</code>
				<xsl:value-of select="bs:description" />
			</dd>
		</div>
	</xsl:template>

	<xsl:template name="property">
		<div class="member">
			<a class="topAnchor" href="#top">(top)</a>
			<a name="{../../@name}.{@name}"></a>
			<h4>
				<xsl:value-of select="@name" />
			</h4>
			<dd>
				<code>
					<xsl:value-of select="@scope" />
					<xsl:if test="@static">
						<xsl:value-of select="' '"/>
						<span class="static">STATIC</span>
						<xsl:value-of select="' '"/>
					</xsl:if>
					var
					<strong>
						<xsl:value-of select="@name" />
					</strong>
					<xsl:value-of select="' : '" />
					<span class="type">
						<xsl:call-template name="typeLink">
							<xsl:with-param name="type" select="@type" />
						</xsl:call-template>
					</span>
					<xsl:if test="@default != ''">
						= <xsl:value-of select="@default" />
					</xsl:if>
				</code>
				<xsl:value-of select="bs:description" />
			</dd>
		</div>
	</xsl:template>

</xsl:stylesheet>