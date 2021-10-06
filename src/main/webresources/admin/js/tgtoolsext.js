//jquery 添加 header 已知影响  tgtools  和 miniui 所有使用 jquery ajax 的。
if (jQuery && jQuery.ajaxSetup) {
    $.ajaxSetup({
        beforeSend: function(xhr) {
            xhr.setRequestHeader('SECURITY-TOKEN', createSecurityToken());
        }
    });
}
