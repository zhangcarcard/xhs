<#macro title name>
    <title>${name}</title>
</#macro>

<#macro nav menu submenu>
    <nav class="navbar navbar-inverse">
        <div id="navbar-menu" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li <#if menu == "index" && submenu == "index">class="active"</#if>><a class="navbar-brand" href="/">首页</a></li>
            </ul>
            <ul class="nav navbar-nav">
                <li <#if menu == "user" && submenu == "user">class="active"</#if>><a href="/user/list.html">用户列表</a></li>
            </ul>
            <#--<ul class="nav navbar-nav navbar-right">
                <li><a href="/logout.do">退出</a></li>
            </ul>-->
        </div>
    </nav>
</#macro>