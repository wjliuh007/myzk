<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %> 
<html>
    <head><title>zk-console</title>
	    <link rel='stylesheet', href='<%=request.getContextPath()%>/css/stylesheets/style.css'/>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jsl.parser.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/json2.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/dataformate.js"></script>
<script type="text/javascript">
            $(function(){
              $("#quit").click(function(){
    				$.ajax({
    				    url:'<%=request.getContextPath()%>/user/logout.do',
    				    type:'POST',
    				    async:true,
    				    data:{},
    				    timeout:5000,
    				    dataType:'json',
    				    success:function(data,textStatus,jqXHR){
    				    	 window.location.href='<%=request.getContextPath()%>/';
    				    }
    				});
                });
			})
</script>
    </head>
    <frameset cols="15%,*" style="frameborder:0;border:0;">
          <frame style="frameborder:0;" src="<%=request.getContextPath()%>/zk/tree.do" id="tree" name="tree">
          <frame style="frameborder:0;" src="<%=request.getContextPath()%>/zk/getNode.do" id="content" name="content"/>
     </frameset>
</html>
