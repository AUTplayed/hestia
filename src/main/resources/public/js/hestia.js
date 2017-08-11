$(document).ready(function() {
    readServers();
    setupEvents();
});

/**
 * Read servers from servers.js to display in dropdown and autofill host and port
 */
function readServers() {
    var selectlist = $("#connection-server");
    servers.forEach(function(server) {
        selectlist.append("<option>" + server.name + "</option>")
    });
}

/**
 * setup change events
 */
function setupEvents() {

    //On cli keypress
    $("#cli-command").keydown(function(ev) {

        //If keypress is "ENTER"
        if (ev.originalEvent.keyCode == 13) {

            //Get command from testfield
            var command = $("#cli-command").val();

            //Send Request to server
            $.get("/cli?command=" + command + getConnection(), function(res) {

                //Display result
                $("#cli-output").html(res);
            });
        }
    });

    //On Server dropdown value change
    $("#connection-server").change(function() {

        //Get selected server
        var selected = $("#connection-server").val();

        //iterate servers
        servers.forEach(function(server) {

            //set hostname and port of selected server
            if (server.name == selected) {
                $("#connection-host").val(server.host);
                $("#connection-port").val(server.port);
            }
        });

    });

    //On Database dropdown click
    $("#connection-database").click(function() {

        //delete all choices except the first one (Default)
        $("#connection-database").find("option").slice(1).remove();

        //Get host and port
        var host = $("#connection-host").val();
        var port = $("#connection-port").val();

        //Build url
        var url = "/keyspaces";
        if (host && port) {
            url += "?host=" + host + "&port=" + port;
        }

        //Send request to server
        $.get(url, function(res) {

            //if there are databases
            if (res.length > 0) {

                //Fill in the options
                var selectlist = $("#connection-database");
                res.forEach(function(keyspace) {
                    selectlist.append("<option>" + keyspace + "</option>");
                });
            }
        });
    });
}

/**
 * Get hostname, port and database from respective input forms and parse them into an url
 * @returns {string}
 */
function getConnection() {
    var host = $("#connection-host").val();
    var port = $("#connection-port").val();
    var db = $("#connection-database").val();

    //Get Database integer
    if (db.startsWith("Default")) {
        db = 0;
    } else {
        db = db.split(":")[0];
        db = db.substr(2, db.length);
    }
    var url = "";
    if (host) {
        url += "&host=" + host;
    }
    if (port) {
        url += "&port=" + port;
    }
    if (db) {
        url += "&db=" + db;
    }
    return url;
}