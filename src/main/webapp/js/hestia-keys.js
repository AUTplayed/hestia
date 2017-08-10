var count;
var pattern;
var cursor = 0;

$(document).ready(function() {
    $("#keys-search").click(function() {
        count = $("#keys-count").val();
        pattern = $("#keys-pattern").val();
        cursor = 0;
        getKeys();
    });
    $("#keys-nextpage").click(function() {
        if(cursor == 0) {
            $("#keys-error").html("No more pages")
        }else {
            getKeys();
        }
    });
});

function getKeys() {
    $("#keys-output").html("");
    $("#keys-error").html("")
    var url = "/keys?cursor="+cursor;
    if(count && count != "") {
        url += "&count="+count;
    }
    if(pattern && pattern != "") {
        url += "&pattern="+pattern;
    }
    url += getConnection();
    $.get(url,function(res) {
        if(res && res.cursor) {
            cursor = res.cursor;
            if(res.keys) {
                res.keys.forEach(function (key) {
                    $("#keys-output").append("<tr class='keys-row'><td>"+key+"</td></tr>");
                });
                $(".keys-row").click(function() {

                });
            }
        }
    });
}