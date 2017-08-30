var count;
var pattern;
var cursor = 0;
var curkey;
var selectedRow;
var counter = 0;

$(document).ready(function () {
    var inputNextpage = $("#keys-nextpage");
    var inputPattern = $("#keys-pattern");
    var inputCount = $("#keys-count");
    inputNextpage.prop("disabled", true);

    $(document).mouseup(function(e)
    {
        var container = $("#keys-value, #keys-output, #keys-keys");

        // if the target of the click isn't the container nor a descendant of the container
        if (!container.is(e.target) && container.has(e.target).length === 0)
        {
            $(".marked").removeClass("marked");
        }
    });

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

    inputCount.focusout(function () {
        var keyscount = $("#keys-count");
        if (keyscount.val() < 0) {
            keyscount.val(0);
        } else if (keyscount.val() > 10000) {
            keyscount.val(10000);
        }
    });

    //On Count or Pattern enter event
    inputCount.keydown(function (ev) {
        //If keypress is "ENTER"
        if (ev.originalEvent.keyCode === 13) {
            search();
        }
    });

    inputPattern.keydown(function (ev) {
        //If keypress is "ENTER"
        inputNextpage.prop("disabled", true);
        if (ev.originalEvent.keyCode === 13) {
            search();
        }
    });

    //On Nextpage button click
    inputNextpage.click(function () {

        //When the cursor is 0 we are at the start again -  display "no more pages"
        if (cursor == 0) {
            $("#keys-error").html("No more pages")
        } else {
            count = $("#keys-count").val();
            //Else get the next keys
            getKeys();
        }
    });

    //On Value delete button click
    $("#keys-value-delete").click(function () {

        //Only if a value is currently selected and displayed
        var marked = $(".marked");
        if (marked.length > 0) {
            var url = "/cli?command=DEL";
            for (var i = 0; i < marked.length; i++) {
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
    if (marked.length > 0) {
        list = marked;
    } else {
        list = $("#keys-output").find("tr");
    }
    if (list.length <= 0) {
        $("#keys-value-status").html("No keys found to export");
    } else {
        var keys = "";
        for (var i = 0; i < list.length; i++) {
            keys += getKeyFromRow(list.eq(i));
            if (i < list.length - 1) {
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
    //get all input values and call getKeys() which, well..., gets the keys
    count = $("#keys-count").val();
    pattern = $("#keys-pattern").val();
    //Reset cursor
    cursor = 0;
    getKeys();
    $("#keys-nextpage").prop("disabled", false);
}

function deleteKeys(url) {
    $.get(url, function (res) {

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


/**
 * Gets the next keys from the db
 */
function getKeys(isExact, callback) {
    //Clear all data
    $("#keys-output").html("");
    $("#keys-error").html("");
    $("#keys-value-value").html("");
    $("#keys-value-status").html("");
    curkey = false;
    selectedRow = false;
    counter = 0;

    //Build url depending on filled input forms
    var url = "/exactKeys?cursor=" + cursor;
    if (count && count != "") {
        url += "&count=" + count;
    }
    if (pattern && pattern != "") {
        url += "&pattern=" + encodeURIComponent(pattern);
    }
    url += getConnection();

    $("#keys-spinner").show();
    //Send request to server
    $.get(url, function (res) {
        $("#keys-spinner").hide();
        //If there is a cursor returned
        if (res && res.cursor) {

            //save the cursor
            cursor = res.cursor;

            //if there are keys returned
            if (res.keys) {

                //Iterate all keys and append them to the output table
                res.keys.forEach(function (key) {
                    $("#keys-output").append("<tr class='keys-row'><td>" + (++counter) + "</td><td>" + key + "</td></tr>");
                });

                //If a key gets clicked
                $(".keys-row").click(keyClick);
            } else {
                $("#keys-output").append("<tr class='keys-row'><td>no results on this page</td></tr>");
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

    if (ev.originalEvent.shiftKey) {

        shiftClick(selectedRow);
    } else if (ev.originalEvent.ctrlKey || ev.originalEvent.metaKey) {
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
        $.get(url, function (response) {
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
    if (index < lastindex) {
        smaller = index;
        bigger = lastindex;
    } else {
        smaller = lastindex;
        bigger = index;
    }
    for (var i = smaller; i <= bigger; i++) {
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