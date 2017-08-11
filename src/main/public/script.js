var count;
var pattern;
var cursor = 0;

$(document).ready(function() {
    $("#root\\:search").click(function() {
        count = $("#root\\:count").val();
        pattern = $("#root\\:pattern").val();
        cursor = 0;
        getKeys();
    });
    $("#root\\:nextPage").click(function() {
        if(cursor == 0) {
            $("#root\\:error").html("No more pages")
        }else {
            getKeys();
        }
    });
});

function getKeys() {
    $("#root\\:output").html("");
    $("#root\\:error").html("")
    var url = "/keys?cursor="+cursor;
    if(count != "") {
        url += "&count="+count;
    }
    if(pattern != "") {
        url += "&pattern="+pattern;
    }
    url += getHostAndPort();
    $.get(url,function(res) {
        if(res && res.cursor) {
            cursor = res.cursor;
            if(res.keys) {
                var keys = res.keys.join("\n");
                $("#root\\:output").html(keys);
            }
        }
    });
}

function getHostAndPort() {
    var host = $("#root\\:host").val();
    var port = $("#root\\:port").val();
    var url = "";
    if(host) {
        url += "&host="+host;
    }
    if(port) {
        url += "&port="+port;
    }
    return url;
}