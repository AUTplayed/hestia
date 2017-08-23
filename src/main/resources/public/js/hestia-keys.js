var count;
var requestCount;
var pattern;
var cursor = 0;
var curkey;
var selectedRow;
var rest = [];
var meanSum = 0;
var meanCount = 0;
var counter = 0;

$(document).ready(function() {

    $("#keys-value-export").confirmation({
        rootSelector: "#keys-value-export",
        title: "Choose file format",
        buttons: [
            {
                label: "csv",
                onClick: function () {
                    exportClick("csv");
                }
            },
            {
                label: "json",
                onClick: function () {
                    exportClick("json");
                }
            }
        ]
    });
    //On Search button click
    $("#keys-search").click(search);

    $("#keys-pattern").keydown(function () {
        resetKeys();
    });

    $("#keys-count").focusout(function () {
        var keyscount = $("#keys-count");
        if(keyscount.val() < 0) {
            keyscount.val(0);
        } else if(keyscount.val() > 1000) {
            keyscount.val(1000);
        }
    });

    //On Count or Pattern enter event
    $("#keys-count, #keys-pattern").keydown(function (ev) {
        //If keypress is "ENTER"
        if (ev.originalEvent.keyCode === 13) {
            search();
        }
    });

    //On Nextpage button click
    $("#keys-nextpage").click(function() {

        count = $("#keys-count").val();
        counter = 0;
        requestCount = count;
        //When the cursor is 0 we are at the start again -  display "no more pages"
        if (cursor == 0) {
            $("#keys-error").html("No more pages")
        } else {

            //Else get the next keys
            getExactKeys();
        }
    });

    //On Value delete button click
    $("#keys-value-delete").click(function() {

        //Only if a value is currently selected and displayed
        var marked = $(".marked");
        if (marked.length > 0) {
            var url = "/cli?command=DEL";
            for(var i = 0; i < marked.length; i++) {
                var key = getKeyFromRow(marked.eq(i));
                url += " " + key;
            }
            url += getConnection();
            deleteKeys(url);
        }
    });

});

function exportClick(format) {
    var marked = $(".marked");
    var list;
    if(marked.length > 0) {
        list = marked;
    } else {
        list = $("#keys-output").find("tr");
    }
    if(list.length <= 0) {
        $("#keys-value-status").html("No keys found to export");
    } else {
        var keys = "";
        for(var i = 0; i < list.length; i++) {
            keys += getKeyFromRow(list.eq(i));
            if(i < list.length - 1) {
                keys += "\n";
            }
        }
        exportFile(keys, format);
    }
}

function exportFile(data, format) {
    var url = "/export?format=" + format + getConnection();
    var xhr = new XMLHttpRequest();
    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === 4) {
            download("export." + format, this.responseText);
        }
    });
    xhr.open("POST", url);
    xhr.send(data);
}

function download(filename, text) {
    var element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
    element.setAttribute('download', filename);

    element.style.display = 'none';
    document.body.appendChild(element);

    element.click();

    document.body.removeChild(element);
}

function search() {
    resetKeys();
    //get all input values and call getKeys() which, well..., gets the keys
    count = $("#keys-count").val();
    requestCount = count;
    pattern = $("#keys-pattern").val();

    //Reset cursor
    cursor = 0;
    getExactKeys();
}

function deleteKeys(url) {
    $.get(url, function(res) {

        //Display deleted amount
        $("#keys-value-status").html("Deleted: " + res);

        //If there were keys deleted, remove the table row from the UI
        if (res > 0) {
            $(".marked").remove();

            //And clear out the value display
            $("#keys-value-value").html("");
        }
    });
}

function resetKeys() {
    meanCount = 0;
    meanSum = 0;
    rest = [];
    counter = 0;
}

function getExactKeys() {
    if(count === 0) {
        $("#keys-output").html("");
    } else {
        if($("#keys-exact").is(':checked')) {
            $("#keys-output").html("");
            var remaining = count - rest.length;
            getExactKeysRec(remaining);
        } else {
            getKeys(false);
        }
    }
}

function getExactKeysRec(remaining) {
    getKeys(true, function (resCount) {
        remaining -= resCount;
        if(resCount === 0) {
            resCount = requestCount / (requestCount * 4);
        }
        meanCount++;
        meanSum += requestCount / resCount;
        if(remaining > 0) {
            requestCount = remaining * (meanSum / meanCount);
            requestCount = Math.ceil(requestCount);
            if (cursor == 0) {
                $("#keys-error").html("No more pages")
            } else {
                getExactKeysRec(remaining);
            }
        }
    });
}

/**
 * Gets the next keys from the db
 */
function getKeys(isExact, callback) {
    //Clear all data
    if(!isExact) {
        $("#keys-output").html("");
    }
    $("#keys-error").html("");
    $("#keys-value-value").html("");
    $("#keys-value-status").html("");
    curkey = false;
    selectedRow = false;

    //Build url depending on filled input forms
    var url = "/keys?cursor=" + cursor;
    if (requestCount && requestCount != "") {
        url += "&count=" + requestCount;
    }
    if (pattern && pattern != "") {
        url += "&pattern=" + pattern;
    }
    url += getConnection();

    //Append rest from earlier request
    if(rest.length > 0) {

        var restlen = rest.length;
        for(var i = 0; i < restlen; i++) {
            var output = $("#keys-output");
            var length = output.find("tr").length;
            if(length >= count) {
                rest.push(key);
            } else {
                output.append("<tr class='keys-row'><td>" + (++counter) + "</td><td>" + rest.shift() + "</td></tr>");
            }
        }
    }
    if(requestCount < 1 || rest.length > 0) {
        return;
    }
    //Send request to server
    $.get(url, function(res) {

        //If there is a cursor returned
        if (res && res.cursor) {

            //save the cursor
            cursor = res.cursor;

            //if there are keys returned
            if (res.keys) {

                //Iterate all keys and append them to the output table
                res.keys.forEach(function(key) {
                    var output = $("#keys-output");
                    var length = output.find("tr").length;
                    if(length < count) {
                        output.append("<tr class='keys-row'><td>" + (++counter) + "</td><td>" + key + "</td></tr>");
                    }
                });
                if(callback) callback(res.keys.length);
                //If a key gets clicked
                $(".keys-row").click(keyClick);
            } else {
                if(!isExact) {
                    $("#keys-output").append("<tr class='keys-row'><td>no results on this page</td></tr>");
                }
                if(callback) callback(0);
            }
        }
    });
}

var lastClick;

function keyClick(ev) {
    ev.preventDefault();
    //clear the delete status
    $("#keys-value-status").html("");

    //Save the selected row
    selectedRow = $(ev.currentTarget);

    if(ev.originalEvent.shiftKey) {
        shiftClick(selectedRow);
    } else if(ev.originalEvent.ctrlKey || ev.originalEvent.metaKey) {
        ctrlClick(selectedRow);
    } else {
        //Remember last clicked row
        lastClick = selectedRow;

        //Unmark other rows
        $(".marked").removeClass("marked");

        //Mark the row as clicked
        selectedRow.addClass("marked");

        //Save the selected key
        curkey = getKeyFromRow(selectedRow);

        //Build url and send get request to server
        var url = "/cli?command=GET " + curkey + getConnection();
        $.get(url, function(response) {
            try {

                //If returned value is a json, pretty print to UI
                var json = JSON.parse(response);
                $("#keys-value-value").html(JSON.stringify(json, null, 2));
            } catch (e) {

                //If returned values is not a json, just display it normally
                $("#keys-value-value").html(response);
            }
        });
    }
}

function shiftClick(row) {
    var lastindex = lastClick.index();
    var index = row.index();
    var smaller, bigger;
    if(index < lastindex) {
        smaller = index;
        bigger = lastindex;
    } else {
        smaller = lastindex;
        bigger = index;
    }
    for(var i = smaller; i <= bigger; i++) {
        $(".keys-row").eq(i).addClass("marked");
    }
    var count = $(".marked").length;
    $("#keys-value-status").html("Selected: " + count);
}

function ctrlClick(row) {
    lastClick = row;
    row.addClass("marked");
    var count = $(".marked").length;
    $("#keys-value-status").html("Selected: " + count);
}

function getKeyFromRow(row) {
    return encodeURIComponent(row.children().last().html());
}