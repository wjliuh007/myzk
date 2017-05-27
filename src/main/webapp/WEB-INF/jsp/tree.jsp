<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.tree.js"></script>
<script type="text/javascript">
    String.prototype.endsWith = function(suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };
    $(function () {
        $.ajaxSetup({cache:false});
        $("#zkTree").tree({
            data : {
                type : "json",
                async : true,
                opts : {
                    method : "get",
                    url : "<%=request.getContextPath()%>/zk/children.do"
                }
            },
            ui : {
                theme_name : "default"
            },
            lang : {
                loading : "Directory loading......"
            },
            types : {
                "default" : {
                    draggable : false
                }
            },
            callback : {
                beforedata : function(node,tree_obj) {
                    var rootPath= '<%= request.getAttribute("path") %>';
                    return {path :  $(node).attr("path") || rootPath,rel : $(node).attr("rel")};
                },
                onselect : function(node,tree_obj) {
                    var test = $(node).children("a").attr("href");
                    $(parent.document.body).find('#content').attr('src', test);
                },
                onsearch : function(node, tree_obj) {
                    tree_obj.container.find(".search").removeClass("search");
                    node.addClass("search");
                },
                ondblclk : function(node, tree_obj) {
                    tree_obj.refresh(node);
                }
            }
        });
     });
     function searchnodes(){
         var searchPath=$('#search_path').val();
         window.location.href='<%=request.getContextPath()%>/zk/tree.do?path='+searchPath;
         $(parent.document.body).find('#content').attr('src', "<%=request.getContextPath()%>/zk/getNode.do?path="+searchPath);
     }
    var zkhosts = '<%= request.getAttribute("host") %>';
    window.onload=function(){
        var splitstr= new Array();
        splitstr=zkhosts.split(',');
        $("#zkhosts").append(splitstr.join('<br/>'));
    }
</script>
<link rel='stylesheet' href='<%=request.getContextPath()%>/js/themes/classic/style.css'/>
<link rel='stylesheet' href='<%=request.getContextPath()%>/css/stylesheets/style.css'/>
<div id="container">
     <h2><a target="_blank" href="#" style='text-decoration:none;'>zk-console</a></h2>
     <div>cluster nodes:</div>
     <div style='margin-bottom:25px;margin-top:5px;' id='zkhosts'></div>
     <div>
         <input type="text" id="search_path" value='${path}' style='height:20px;font-size:18px;'/>
     </div>
     <div id="zkTree"></div>
</div>
