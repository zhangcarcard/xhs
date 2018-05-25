<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#import "/common.ftl" as common>
    <#include "/meta.ftl">
    <@common.title name="首页" />
    <#include "/head.ftl">
</head>
<body>
<#include "/fileupload.ftl">
<div class="container">
    <@common.nav menu="index" submenu="index" />
    <table id="table"></table>
    <div style="margin-top: 30px;">
        <form id="form">
            <div class="form-group">
                <label for="noteId">文章</label>
                <input type="text" class="form-control" name="noteId" placeholder="文章ID" required>
            </div>
            <div class="form-group">
                <label for="userId">用户</label>
                <input type="text" class="form-control" name="userId" placeholder="用户ID" required>
            </div>
            <div class="form-inline" style="margin-top: 16px;">
                <div class="form-group hide follows">
                    <label for="follows">关注</label>
                    <input type="number" class="form-control" name="follows" id="follows" placeholder="关注次数">
                </div>
                <div class="form-group hide likes">
                    <label for="likes">点赞</label>
                    <input type="number" class="form-control" name="likes" id="likes" placeholder="点赞次数">
                </div>
                <div class="form-group hide collects">
                    <label for="collects">收藏</label>
                    <input type="number" class="form-control" name="collects" id="collects" placeholder="收藏次数">
                </div>
                <div class="form-group hide cts">
                    <label for="cts">评论</label>
                    <input type="number" class="form-control" name="cts" id="cts" placeholder="评论次数">
                </div>
            </div>
            <div class="form-group hide comments" style="margin-top: 16px;">
                <label for="comments">评论内容</label>
                <input type="text" class="form-control" name="comments" id="comments" placeholder="评论内容">
            </div>
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="operate" value="follow"> 关注&nbsp;&nbsp;&nbsp;
                </label>
                <label>
                    <input type="checkbox" name="operate" value="like"> 点赞&nbsp;&nbsp;&nbsp;
                </label>
                <label>
                    <input type="checkbox" name="operate" value="collect"> 收藏&nbsp;&nbsp;&nbsp;
                </label>
                <label>
                    <input type="checkbox" name="operate" value="comment"> 评论&nbsp;&nbsp;&nbsp;
                </label>
            </div>
            <button type="submit" class="btn btn-primary">
                <span class="glyphicon glyphicon-play" aria-hidden="true"></span>开始
            </button>
        </form>
    </div>
</div>

<#include "/tail.ftl">
<script>
    $(function() {
        createBootstrapTable('#table', '', ['id', 'username', 'sid', 'follow', 'like', 'comment', 'collect'], ['ID', '用户名称', '用户密钥(sid)', '关注', '点赞', '评论', '收藏'], false, '#toolbar', 'GET', false);

        var websocket = null;
        //判断当前浏览器是否支持WebSocket
        if ('WebSocket' in window) {
            websocket = new WebSocket("ws://" + window.location.host + "/websocket");
        } else {
            toastr.error("当前浏览器 Not support websocket!");
        }

        //连接发生错误的回调方法
        websocket.onerror = function () {
            toastr.error("连接发生错误");
        };

        //连接成功建立的回调方法
        websocket.onopen = function () {
            toastr.info("连接建立成功")
        }

        //接收到消息的回调方法
        websocket.onmessage = function (event) {
            var data = JSON.parse(event.data);
            var tag = data.tag;
            if (tag === "table") {
                $('#table').bootstrapTable('append', data);
                $('#table').bootstrapTable('scrollTo', 'bottom');
            } else if (tag === "operate") {
                toastr.info(data.message);
            }
        }

        //连接关闭的回调方法
        websocket.onclose = function () {
            toastr.info("连接已关闭")
        }

        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            websocket.close();
        }

        $("#form").submit(function(){
            if ($(":checked").length == 0) {
                toastr.error("请选择操作类型!");
                return false;
            }

            var params = $(this).serialize();
            websocket.send(params);
            return false;
        });

        $(":checkbox").change(function () {
            var val = $(this).val();
            if (this.checked) {
                $("." + val + "s").removeClass("hide");
                $("#" + val + "s").attr("required", "required");
            } else {
                $("." + val + "s").addClass("hide");
                $("#" + val + "s").removeAttr("required")
            }

            if (val === "comment") {
                if (this.checked) {
                    $(".cts").removeClass("hide");
                    $("#cts").attr("required", "required");
                } else {
                    $(".cts").addClass("hide");
                    $("#cts").removeAttr("required")
                }
            }
        });
    });
</script>
</body>
</html>