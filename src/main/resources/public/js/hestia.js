$(document).ready(function() {
    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=confirmation]'
    });
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

String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};

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
                getAndSetInfo();
            }
        });

    });

    //On Hostname or port unfocus
    $("#connection-host, #connection-port").focusout(function () {
        getAndSetInfo();
    });

    $("#connection-host, #connection-port").keydown(function (ev) {
        //If keypress is "ENTER"
        if (ev.originalEvent.keyCode == 13) {
            getAndSetInfo();
        } else {
            $("#connection-server").val("Custom");
        }
    });

    //On Database dropdown click
    $("#connection-database").click(function() {

        //delete all choices except the first one (Default)
        $("#connection-database").find("option").slice(1).remove();

        //Build url
        var url = "/keyspaces";
        url += getConnectionNoDb();

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
    
    $("#tab-connection").click(function () {
        getAndSetInfo();
    });
}

function getAndSetInfo() {
    var url = "/info";
    url += getConnectionNoDb();

    function failed() {
        $("#connection-info").html("Unable to connect");
        $("#connection-status").html("Unable to connect");
    }
    $.get(url, function (res) {
        if(res && res != "") {
            $("#connection-info").html(res);
            $("#connection-status").html("Connected to: "+$("#connection-host").val()+" ("+$("#connection-server").val()+")");
            getNamespaces();
        } else {
            failed();
        }
    }).fail(failed);
}

function setInfoTable(info) {
    var table = "<table id='connection-table'>\n";
    var lines = info.split("\n");
    for(var i = 0; i < lines.length; i++) {
        var line = lines[i];
        if(line != "") {
            table+="<tr>\n";
            if(line.startsWith("#")) {
                var header = line.substr(2, line.length);
                table += "<th>" + header + "</th>\n</tr>\n";
            } else {
                var keyVal = line.split(":");
                table += "<td>" + keyVal[0] + "</td>\n";
                table += "<td>" + keyVal[1] + "</td>\n</tr>\n";
            }
        }
    }
    table += "</table>";
    return table;
}

function getNamespaces() {
    var url = "/namespaces" + getConnectionNoDb();
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
    currentNamespaces[key].description = value;
    var jsonString = JSON.stringify(currentNamespaces).replaceAll('\"', '\\"');  //WTF do not split into 2 method calls - won't work
    var url = "/cli?command=SET info " + "\"" + encodeURIComponent(jsonString) + "\"";
    $.get(url, function (res) {
        if(res !== "OK") {
            target.css("background-color", "red");
        } else {
            target.css("background-color", "#8bc72a");
        }
    });
}
var currentNamespaces;

function buildNamespaceTable(res) {
    currentNamespaces = res;
    var table = "<table id='connection-namespaces-table'>\n";
    table += "<tr>\n<th>Namespace</th>\n<th>#</th>\n<th>Description</th>\n</tr>\n";
    var keys = Object.keys(res);
    keys.forEach(function (key) {
        table += "<tr>\n<td>" + key + "</td>\n";
        table += "<td>" + res[key].count + "</td>\n";
        table += "<td><input class='connection-namespaces-description' value='" + res[key].description + "'></td>\n</tr>\n";
    });
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

function getConnectionNoDb() {
    //Get host and port
    var host = $("#connection-host").val();
    var port = $("#connection-port").val();

    //Build url
    var url = "";
    if (host) {
        url += "?host=" + host;
    }
    if (port) {
        url += "&port=" + port;
    }
    return url;
}