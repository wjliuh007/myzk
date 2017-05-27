<html>
    <head><title>Create path</title>
        <link rel='stylesheet' href='<%=request.getContextPath()%>/css/stylesheets/style.css'/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/jsl.parser.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/json2.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/dataformate.js"></script>
        <script type="text/javascript">

            var temp_value = '';
            function checkData(){
                 var nodeValue=$.trim($("#data").val());
                 if(nodeValue != undefined && nodeValue != '' && nodeValue != null && nodeValue != 'null'){
                     temp_value = JSONDATA.formatJsonData(nodeValue);
                     if (temp_value != '') {
                        $('#data').val(temp_value);
                     } else {
                        return false;
                     }
                 }
                 return true;
            }
            window.onload=function(){
                $("#check_data").click(function(e){
                    var check_result = checkData();
                    if(check_result){
                        // 
                    }
                    $("#data").focus();
                });
                $("#create_node").click(function(e){
                    // check path
                    var node_path=$.trim($("#nodename").val());
                    if(node_path == undefined || node_path == '' || node_path == null || node_path == 'null'){
                        alert('node name is empty!');
                        $("#nodename").focus();
                        return false;
                    }
                    var check_result = checkData();
                    if( !check_result ){
                        $("#data").focus();
                        return false;
                    }
                    
                    temp_value=$.trim($("#data").val());
                    if(temp_value!=undefined && temp_value!='' && temp_value!=null && temp_value!="null"){
                        var compress_data = JSONDATA.compressJsonData(temp_value);
                        $('#data').val(compress_data);
                    }
                    var parent_path = $.trim($("#parentpath").val());
                    var new_node_path = parent_path + '/' + node_path;
                    if (parent_path === '/') {
                        new_node_path = '/' + node_path;
                    }
                    $('#path').val(new_node_path); 

                    //$("#node_form").submit();
                    $.post("<%=request.getContextPath()%>/zk/create.do",$('#node_form').serialize(),function(rt){
                        alert(rt);
                        if(rt=='"Create ok"'){
                            //$(parent.document.body).find('#tree').attr('src', "/node-zk/tree?path="+parenPath);
                            var tree_innerHTML = $.trim($(parent.tree.document).contents().find("#zkTree").html());
                            //alert(tree_innerHTML);
                            var to_upper_case = 0;
                            if (tree_innerHTML.indexOf('</ul>') < 0) {
                                to_upper_case = 1;
                            }
                            var current_node = '<li class="open" rel="chv" path="' + new_node_path + '"><a href="<%=request.getContextPath()%>/zk/getNode.do?path='+new_node_path
                                    +'"><ins class=ou.png>&nbsp;</ins>'+node_path+'</a></li>';
                            if (to_upper_case == 1) {
                                current_node = '<LI class="open" rel="chv" path="' + new_node_path + '"><A href="<%=request.getContextPath()%>/zk/getNode.do?path='+new_node_path
                                    +'"><INS class=ou.png>&nbsp;</INS>'+node_path+'</A></LI>';
                            }
                            if (parent_path === '/') {
                                tree_innerHTML = tree_innerHTML.substring(0, tree_innerHTML.length - 5) + current_node + tree_innerHTML.substring(tree_innerHTML.length - 5);
                                $(parent.tree.document).contents().find("#zkTree").empty();
                                $(parent.tree.document).contents().find("#zkTree").append(tree_innerHTML);
                                window.location.href="<%=request.getContextPath()%>/zk/getNode.do?path="+new_node_path;
                                return;
                            }
                            var search_path = 'path="'+parent_path+'/';
                            var index = tree_innerHTML.indexOf(search_path);
                            var new_content = '';
                            // parent node do not had other children node
                            if ( index < 0 ) {
                                index = tree_innerHTML.indexOf('path="'+parent_path+'"');
                                var before_part = tree_innerHTML.substring(0, index);
                                var after_part = tree_innerHTML.substring(index);

                                index = after_part.indexOf("</LI>");
                                if (index < 0) {
                                    index = after_part.indexOf("</li>");
                                }
                                var ul_start = '<ul>';
                                var ul_end = '</ul>';
                                if (to_upper_case == 1) {
                                    ul_start = ul_start.toUpperCase();
                                    ul_end = ul_end.toUpperCase();
                                }

                                before_part = before_part + after_part.substring(0, index);
                                after_part = after_part.substring(index);

                                new_content = before_part + ul_start + current_node + ul_end+ after_part;
                            } else {
                                index = tree_innerHTML.indexOf('path="'+parent_path);
                                var before_part = tree_innerHTML.substring(0, index);
                                var after_part = tree_innerHTML.substring(index);

                                index = after_part.indexOf("<UL>");
                                if (index < 0) {
                                    index = after_part.indexOf("<ul>");
                                }

                                before_part = before_part + after_part.substring(0, index + 4);
                                after_part = after_part.substring(index + 4);

                                new_content = before_part + current_node + after_part;
                            }
                            //alert(new_content);
                            $(parent.tree.document).contents().find("#zkTree").empty();
                            $(parent.tree.document).contents().find("#zkTree").append(new_content);
                            window.location.href="<%=request.getContextPath()%>/zk/getNode.do?path="+new_node_path;
                            return;
                        }
                    });
                 });
            } 
         </script>
    </head>
  <body>
    <div class= "container">
     <div id='login'>
       <h3>Welcome,${user } </h3>
     </div>
     <br/>
     <div>
      <form action="<%=request.getContextPath()%>/zk/create.do" method="post" id="node_form" onsubmit="return;">
        <table class= "table table-bordered">
          <tr>
           <td>Parent Path:<input type='hidden' name='parentpath' id='parentpath' value='${path }'/></td>
           <td>${path }<input type='hidden' name='path' id='path'/></td>
          </tr>
          <tr>
           <td>Node Name :</td>
           <td><input type='text' name="nodename" id='nodename' style="width:650px;"> </td>
          </tr>
         <tr>
           <td>Data (JSON format) :</td>
           <td><textarea cols='90' rows='20' name='data' id='data' style="width:650px;"
                spellcheck="false" placeholder="Please enter JSON format data."></textarea></td>
        </tr>
        <tr>
          <td>Type :</td>
          <td>
            <input type='radio' name='flag' value="0" checked="checked"/>&nbsp;PERSISTENT<br/>
            <input type='radio' name='flag' value="2"/>&nbsp;PERSISTENT_SEQUENTIAL<br/>
            <input type='radio' name='flag' value="1"/>&nbsp;EPHEMERAL<br/>
            <input type='radio' name='flag' value="3"/>&nbsp;EPHEMERAL_SEQUENTIAL<br/>
          </td>
        </tr>
        <tr>
          <td colspan="1"><input type='button' id='check_data' value="Check data" class="btnStyle"/></td>
        <% if(request.getAttribute("user") != null){ %>
          <td colspan="1"><input type='button' id='create_node' value="Create" class="btnStyle"/></td>
        <% }else{ %>
          <td colspan="1"><input type='button' id='create_node' value="Create" disabled='disabled' class="btnStyle"/></td>
        <% } %>
        </tr>
       </table>
     </form>
     </div>
   </div>
  </body>
</html>
