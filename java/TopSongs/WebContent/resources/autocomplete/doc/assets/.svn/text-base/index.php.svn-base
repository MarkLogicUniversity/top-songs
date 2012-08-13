<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>Yet another AutoComplete (Google Suggest)</title>
		<style type="text/css">
		<!--
			.parameter {
				font-weight: bold;
				font-style: italic;
				color: black;
				font-size: 11px;
				font-family: tahoma, verdana, sans-serif;
				padding: 5px;
				vertical-align: top;
				width: 150px;
			}
			.parameter {
				color: black;
				font-size: 11px;
				font-family: tahoma, verdana, sans-serif;
				padding: 5px;
				vertical-align: top;
			}

		-->
		</style>
	</head>
	</head>
	<body style="margin: 0px; padding: 0px; background: url(example_files/header-bg.png); background-repeat: repeat-x;">
			<h1 style="margin: 20px 0px 39px 20px; color: white; vertical-align: middle;">Yet another AutoComplete (Google Suggest)</h1>
		<br/>
		<div style="margin: 10px;">
			<?php
				if(!$_REQUEST['ns']){
			?>
			<h3>Example Dictionary Search (<a href="index.php?ns=1">View Without Scriptaculous Effects</a>)</h3>
			<?php
				} else {
			?>
			<h3>Example Dictionary Search (<a href="index.php">View With Scriptaculous Effects</a>)</h3>
			<?php
				}
			?>
			<div>
				In this example, I use the same dictionary I used in the WordLadder coding challenge. Simply start typing a word and
				the suggest tool will give you results.
			</div>
			<script type="text/javascript" src="../lib/prototype/prototype.js"></script>
			<?php if(!$_REQUEST['ns']){?><script type="text/javascript" src="../lib/scriptaculous/scriptaculous.js"></script><?php }?>
			<script type="text/javascript" src="autocomplete.js"></script>
			<input type="text" id="my_ac" name="my_ac" size="45"/>
			<script type="text/javascript">
				new AutoComplete('my_ac', 'ac.php?s=',{delay: .25});
			</script>
			<br/><span style="color: gray; font-size: small">(3 letters minimum required to trigger a suggestion)</span>
			<br/><input type="text" /> 
			<h3>Source Code:</h3>
			<pre style="border: 1px dashed blue; background: #efefef; padding: 10px; width: 700px"><?php
			$s = $_REQUEST['ns'] ? '' : '
<script type="text/javascript" src="../lib/scriptaculous/scriptaculous.js"></script>';
$html = <<<EOF
<script type="text/javascript" src="../lib/prototype/prototype.js"></script>$s
<script type="text/javascript" src="autocomplete.js"></script>
<input type="text" id="my_ac" name="my_ac" size="45"/>
<script type="text/javascript">
	new AutoComplete('my_ac', 'ac.php?s=', {
		delay: 0.25
	});
</script>
EOF;
echo htmlspecialchars($html);
?></pre>
			<h3>API Documentation</h3>
			<div>
				<h4>Parameters</h4>
				<table>
					<tr>
						<td class="parameter">bindField</td>
						<td class="definition">The ID of the form input field you want to monitor for autocompletion. Could also be the actual DOM Input element.</td>
					</tr>
					<tr>
						<td class="parameter">action</td>
						<td class="definition">URL of the search script. This script should return an XML document of the following schema:
							<pre style="border: 1px dashed blue; background: #efefef; padding: 10px;">
&lt;?xml version="1.0"?&gt;
&lt;Suggestions&gt;
	&lt;suggestion&gt;beau&lt;/suggestion&gt;
	&lt;suggestion&gt;beauty&lt;/suggestion&gt;
	&lt;suggestion&gt;beautiful&lt;/suggestion&gt;
	&lt;suggestion&gt;beaufort&lt;/suggestion&gt;
&lt;/Suggestions&gt;</pre>
						</td>
					</tr>
					<tr>
						<td class="parameter">options</td>
						<td class="definition">object of optional parameters (see below)</td>
					</tr>
				</table>
				<h4>Options</h4>
				<table>
					<tr>
						<td class="parameter">size</td>
						<td class="definition">size of the result screen to show (# of options).<br/>
							Default: 10
						</td>
					</tr>
					<tr>
						<td class="parameter">cssClass</td>
						<td class="definition">CSS class name of the select object.</td>
					</tr>
					<tr>
						<td class="parameter">onSelect</td>
						<td class="definition">Function to execute upon option selection. The bindField element will be passed in as the only parameter to this function.</td>
					</tr>
					<tr>
						<td class="parameter">threshold</td>
						<td class="definition"># of characters to require before a suggestion is made.<br/>
							Default: 3
						</td>
					</tr>
					<tr>
						<td class="parameter">delay</td>
						<td class="definition">Time in seconds to wait before a suggestion is triggered.<br/>
							Default: .2
						</td>
					</tr>
				</table>
			</div>
		</div>
<?php include_once('../urchin.php'); ?>
	</body>
</html>
