var count;
var pattern;
var cursor = 0;
var curkey;
var selectedRow;

$(document).ready(function() {

    //On Search button click
    $("#keys-search").click(function() {

        //get all input values and call getKeys() which, well..., gets the keys
        count = $("#keys-count").val();
        pattern = $("#keys-pattern").val();

        //Reset cursor
        cursor = 0;
        getKeys();
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
        if (curkey && selectedRow) {

            //Build url and send delete request to server
            var url = "/cli?command=DEL " + curkey + getConnection();
            $.get(url, function(res) {

                //Display deleted amount
                $("#keys-value-status").html("Deleted: " + res);

                //If there were keys deleted, remove the table row from the UI
                if (res > 0) {
                    selectedRow.remove();

                    //And clear out the value display
                    $("#keys-value-value").html("");
                }
            });
        }
    });
});

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
    var url = "/keys?cursor=" + cursor;
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
                $(".keys-row").click(function(ev) {

                    //clear the delete status
                    $("#keys-value-status").html("");

                    //Save the selected row
                    selectedRow = $(ev.currentTarget);

                    //Save the selected key
                    curkey = selectedRow.children().first().html();

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
                });
            }
        }
    });
}