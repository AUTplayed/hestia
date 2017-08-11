$(document).ready(function () {
    readServers();
    setupEvents();
});


function readServers() {
    var selectlist = $("#connection-server");
    servers.forEach(function (server) {
        selectlist.append("<option>"+server.name+"</option>")
    });
}

function setupEvents() {
    $("#cli-command").keydown(function (ev) {
        if(ev.originalEvent.keyCode == 13) {
            var command = $("#cli-command").val();
            $.get("/cli?command="+command+getConnection(),function (res) {
                $("#cli-output").html(res);
            });
        }
    });

    $("#connection-server").change(function () {
        var selected = $("#connection-server").val();
        servers.forEach(function (server) {
            if(server.name == selected) {
                $("#connection-host").val(server.host);
                $("#connection-port").val(server.port);
            }
        });

    });

    $("#connection-database").click(function () {
        $("#connection-database").find("option").slice(1).remove();
        var host = $("#connection-host").val();
        var port = $("#connection-port").val();
        var url = "/keyspaces";
        if(host && port) {
            url+="?host="+host+"&port="+port;
        }
        $.get(url, function (res) {
            if(res.length > 0) {
                var selectlist = $("#connection-database");
                res.forEach(function (keyspace) {
                    selectlist.append("<option>"+keyspace+"</option>");
                });
            }
        });
    });
}

function getConnection() {
    var host = $("#connection-host").val();
    var port = $("#connection-port").val();
    var db = $("#connection-database").val();
    if(db.startsWith("Default")) {
        db = 0;
    } else {
        db = db.split(":")[0];
        db = db.substr(2, db.length);
    }
    var url = "";
    if(host) {
        url += "&host="+host;
    }
    if(port) {
        url += "&port="+port;
    }
    if(db) {
        url += "&db="+db;
    }
    return url;
}