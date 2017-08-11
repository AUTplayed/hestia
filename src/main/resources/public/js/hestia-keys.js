var count;
var pattern;
var cursor = 0;
var curkey;
var selectedRow;

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
        } else {
            getKeys();
        }
    });
    $("#keys-value-delete").click(function () {
        if(curkey && selectedRow) {
            var url = "/cli?command=DEL "+curkey+getConnection();
            $.get(url,function (res) {
                $("#keys-value-status").html("Deleted: "+ res);
                if(res > 0) {
                    selectedRow.remove();
                    $("#keys-value-value").html("");
                }
            });
        }
    });
});

function getKeys() {
    $("#keys-output").html("");
    $("#keys-error").html("")
    $("#keys-value-value").html("");
    $("#keys-value-status").html("");
    curkey = false;
    selectedRow = false;
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
                $(".keys-row").click(function(ev) {
                    $("#keys-value-status").html("");
                    selectedRow = $(ev.currentTarget);
                    curkey = selectedRow.children().first().html();
                    var url = "/cli?command=GET "+curkey+getConnection();
                    $.get(url,function (response) {
                        try {
                            var json = JSON.parse(response);
                            $("#keys-value-value").html(JSON.stringify(json,null,2));
                        }catch(e){
                            $("#keys-value-value").html(response);
                        }
                    });
                });
            }
        }
    });
}