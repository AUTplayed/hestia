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

            //Get command from input
            var commandInput = $("#cli-command");
            var command = commandInput.val();

            if($("#cli-clear").is(':checked')) {
                commandInput.val("");
            }

            //If command is clear, clear the output
            if(command == "clear") {
                $("#cli-output").html("");
            } else {

                //Send Request to server
                $.get("/cli?command=" + command + getConnection(), function(res) {

                    //Display result
                    $("#cli-output").html(res);
                });
            }
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

function getNamespaces() {
    var url = "/namespaces" + getConnection();
    $.get(url, function (res) {
        $("#connection-namespaces").html(buildNamespaceTable(res));
        $(".connection-namespaces-description").focusout(editNamespaceDescription);
    });
}

function editNamespaceDescription(ev) {
    var target = $(ev.currentTarget);
    var value = target.val();
    var children = target.parent().parent().children();
    var key = children.eq(0).html();
    var count = children.eq(1).html();
    var url = "/cli?command=SET info:" + key + " " + count + ":" + value;
    $.get(url, function (res) {
        if(res !== "OK") {
            target.css("background-color", "red");
        } else {
            target.css("background-color", "#8bc72a");
        }
    });
}

function buildNamespaceTable(res) {
    var table = "<table id='connection-namespaces-table'>\n";
    table += "<tr>\n<th>Namespace</th>\n<th>#</th>\n<th>Description</th>\n</tr>\n";
    res = res.split("\n");
    for(var i = 0; i < res.length; i++) {
        if(res[i]) {
            var fields = res[i].split(":");
            table += "<tr>\n<td>" + fields[0] + "</td>\n";
            table += "<td>" + fields[1] + "</td>\n";
            if(fields[2] === undefined) {
                fields[2] = "";
            }
            table += "<td><input class='connection-namespaces-description' value='" + fields[2] + "'></td>\n</tr>\n";
        }
    }
    table += "</table>";
    return table;
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