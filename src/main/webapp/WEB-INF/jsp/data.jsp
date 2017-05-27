<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %> 
<html>
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/easyui-icon.css">
	<link rel='stylesheet' href='<%=request.getContextPath()%>/css/stylesheets/style.css' />
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.easyui.min.js"></script>
	
	<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jsl.parser.js"></script>
	<script type="text/javascript"	src="<%=request.getContextPath()%>/js/json2.js"></script>
	<script type="text/javascript"	src="<%=request.getContextPath()%>/js/dataformate.js"></script>
<script type="text/javascript">
            $(function(){

                $("#save_data").click(function(e){
                    var path = $("input[name='path']").val();
                    var version = $("input[name='version']").val();
                    var new_data = $("#new_data").val();

    				$.ajax({
    				    url:'<%=request.getContextPath()%>/zk/edit.do',
    				    type:'POST',
    				    async:true,
    				    data:{
    				    	path:path,
    				    	new_data:new_data,
    				    	version:version
    				    },
    				    timeout:5000,
    				    dataType:'json',
    				    success:function(data){
     				    	alert(data.msg);
    				    }
    				});
				});
			})

</script>
<head>
<body>
	<div class="container">
		<div id='login'>
			<h3>
				Welcome,<%=request.getAttribute("user")%>
			</h3>

		</div>
		<div>
			<label>${path}</label>
					</div>
		<hr />
		<div id='data_container'>

			<form id='edit_form'>
				<input type='hidden' name='path' value='${path}' /> 
				<input type='hidden' name='version' value='${stat.version}' />
				<textarea cols='500' rows='50' name='new_data' id='new_data'
					style="width: 1200px;" spellcheck="false">${data}</textarea>
					<% if("admin".equals(request.getAttribute("user"))){ %>
			<input type='submit' value='Save' id='save_data' class="btnStyle" style="margin-left:20px;"/>
			<% }%>
			</form>
			<div id="dd"></div>
		</div>
	</div>
</body>
</html>
