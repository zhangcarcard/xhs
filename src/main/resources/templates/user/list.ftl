<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#import "/common.ftl" as common>
    <#include "/meta.ftl">
    <@common.title name="用户列表" />
    <#include "/head.ftl">
</head>
<body>
<#include "/fileupload.ftl">
<div class="container">
    <@common.nav menu="user" submenu="user" />

    <div id="toolbar" class="btn-group">
        <button id="btn_add" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
        </button>
        <button id="btn_delete" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
        </button>
        <button id="btn_batch" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>批量导入
        </button>
        <a type="button" class="btn btn-default" href="/file/template/user.do">
            <span class="glyphicon glyphicon-cloud-download" aria-hidden="true"></span>模板下载
        </a>
    </div>
    <table id="table"></table>
</div>

<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="addModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="addModalLabel">新增</h4>
            </div>
            <div class="modal-body">
                <form id="form">
                    <div class="form-group">
                        <label for="username">用户名</label>
                        <input type="text" name="username" class="form-control" id="username" placeholder="用户名" required autofocus="autofocus">
                    </div>
                    <div class="form-group">
                        <label for="sid">用户密钥(sid)</label>
                        <input type="text" name="sid" class="form-control" id="sid" placeholder="用户密钥(sid)" required>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
                        <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<#include "/tail.ftl">
<script>
    $(function() {
        createBootstrapTable('#table', '/user.do', ['id', 'username', 'sid'], ['ID', '用户名称', '用户密钥(sid)'], true, '#toolbar', 'GET', true, 800);
        createFileInput('#pupload', '/file/template/user.do', function (event, data, previewId, index) {
            if (data.response.success) {
                $('#table').bootstrapTable('refresh', {silent: true});
                $("#fileinputModal").modal("hide");
            } else {
                toastr.error(data.response.message);
            }
        });

        $("#btn_batch").click(function() {
            $("#pupload").fileinput('refresh');
            $("#fileinputModal").modal("show");
        });
        $("#btn_add").click(function() {
            $("#form")[0].reset();
            $("#addModalLabel").text("新增");
            $('#addModal').modal();
        });
        $("#btn_delete").click(function() {
            var selections = $('#table').bootstrapTable('getSelections');
            if (selections.length == 0) {
                toastr.error("请选择删除的记录!");
            } else {
                swal({
                    title: '确认要删除吗?',
                    text: "删除后，不可恢复!",
                    icon: "warning",
                    dangerMode: true,
                    buttons: {
                        cancel: true,
                        confirm: true,
                    },
                }).then((willDelete) => {
                    if (willDelete) {
                        $.ajax({
                            url: "/user.do",
                            type: "DELETE",
                            data: JSON.stringify(selections),
                            contentType:"application/json; charset=UTF-8",
                            dataType:"json",
                            success: function(result) {
                                if (result.success) {
                                    toastr.success('记录已删除.');
                                    $('#table').bootstrapTable('refresh', {silent: true});
                                } else {
                                    toastr.error(result.message);
                                }
                            }
                        });
                    }
                }).catch(swal.noop);
            }
        });
        $("#form").submit(function(){
            var params = $(this).serialize();
            $.post("/user.do", params, function(result) {
                if (result.success) {
                    toastr.success('保存成功.');
                    $('#table').bootstrapTable('refresh', {silent: true});
                    $('#addModal').modal('hide');
                } else {
                    toastr.error(result.message);
                }
            }, "json");
            return false;
        });
    });
</script>
</body>
</html>