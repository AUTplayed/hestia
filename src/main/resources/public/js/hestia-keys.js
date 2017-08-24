var count;
var pattern;
var cursor = 0;
var curkey;
var selectedRow;

$(document).ready(function() {

    //On Search button click
    $("#keys-search").click(search);

    //On Count or Pattern enter event
    $("#keys-count, #keys-pattern").keydown(function (ev) {
        //If keypress is "ENTER"
        if (ev.originalEvent.keyCode == 13) {
            search();
        }
    });

    //On Nextpage button click
    $("#keys-nextpage").click(function() {

        //When the cursor is 0 we are at the start again -  display "no more pages"
        if (cursor == 0) {
            $("#keys-error").html("No more pages")
        } else {

            //Else get the next keys
            getKeys();
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

function search() {
    //get all input values and call getKeys() which, well..., gets the keys
    count = $("#keys-count").val();
    pattern = $("#keys-pattern").val();

    //Reset cursor
    cursor = 0;
    getKeys();
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

/**
 * Gets the next keys from the db
 */
function getKeys() {
    //Clear all data
    $("#keys-output").html("");
    $("#keys-error").html("");
    $("#keys-value-value").html("");
    $("#keys-value-status").html("");
    curkey = false;
    selectedRow = false;

    //Build url depending on filled input forms
    var url = "/exactKeys?cursor=" + cursor;
    if (count && count != "") {
        url += "&count=" + count;
    }
    if (pattern && pattern != "") {
        url += "&pattern=" + pattern;
    }
    url += getConnection();

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
                    $("#keys-output").append("<tr class='keys-row'><td>" + key + "</td></tr>");
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
    return encodeURIComponent(row.children().first().html());
}