$(document).ready(function() {
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    var start, end;
    var calendar = $('#calendar').fullCalendar({
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
        },
        editable: true,
        theme: true,
        selectable: true,
        selectHelper: true,
        select: function(start, end, allDay) {
            if (!allDay) {
                $('#event-dialog').removeClass('hidden');
                $('#event-name').val("");
                $('#event-description').val("");
                $('#event-address').val("");
                $('#event-dialog').attr('title', 'Edit Event');
                $('#event-dialog').dialog({
                    show: "fade",
                    hide: "explode",
                    modal: true,
                    position: { 
                        my: 'top',
                        at: 'top',
                        of: $("#calendar")
                    },
                    buttons: {
                        "Create Event!": function() {
                            $(this).dialog("close");
                            calendar.fullCalendar('renderEvent', {
                                title: $("#event-name").val(),
                                description: $("#event-description").val(),
                                address: $("#event-address").val(),
                                start: start,
                                end: end,
                                allDay: false
                            }, true // make the event "stick"
                            );
                            console.log(calendar.fullCalendar('clientEvents'));
                            calendar.fullCalendar('unselect');
                        },
                        Cancel: function() {
                            $(this).dialog("close");
                        }
                    }
                });
                start = start;
                end = end;
            }
        },
        dayClick: function(date, allDay, jsEvent, view) {
            if (allDay) {
                calendar.fullCalendar('gotoDate', date.getFullYear(), date.getMonth(), date.getDate());
                calendar.fullCalendar('changeView', 'agendaDay');
            }
        },
        eventClick: function(calEvent, jsEvent, view) {
        	$('#event-dialog').removeClass('hidden');
            $("#event-name").val(calEvent.title);
            $("#event-description").val(calEvent.description);
            $("#event-address").val(calEvent.address);
            $('#event-dialog').attr('title', 'Edit Event');
            $('#event-dialog').dialog({
                    show: "blind",
                    hide: "explode",
                    modal: true,
                    position: { 
                        my: 'top',
                        at: 'top',
                        of: $("#calendar")
                    },
                    buttons: {
                        "Create Event!": function() {
                            $(this).dialog("close");
                            calendar.fullCalendar('renderEvent', {
                                title: $("#event-name").val(),
                                description: $("#event-description").val(),
                                address: $("#event-address").val(),
                                start: start,
                                end: end,
                                allDay: false
                            }, true // make the event "stick"
                            );
                            console.log(calendar.fullCalendar('clientEvents'));
                            calendar.fullCalendar('unselect');
                        },
                        Cancel: function() {
                            $(this).dialog("close");
                        }
                    }
                });
        },
        events: [{
            title: 'All Day Event',
            start: new Date(y, m, 1)
        }, {
            title: 'Long Event',
            start: new Date(y, m, d - 5),
            end: new Date(y, m, d - 2)
        }, {
            id: 999,
            title: 'Repeating Event',
            start: new Date(y, m, d - 3, 16, 0),
            allDay: false
        }, {
            id: 999,
            title: 'Repeating Event',
            start: new Date(y, m, d + 4, 16, 0),
            allDay: false
        }, {
            title: 'Meeting',
            start: new Date(y, m, d, 10, 30),
            allDay: false
        }, {
            title: 'Lunch',
            start: new Date(y, m, d, 12, 0),
            end: new Date(y, m, d, 14, 0),
            allDay: false
        }, {
            title: 'Birthday Party',
            start: new Date(y, m, d + 1, 19, 0),
            end: new Date(y, m, d + 1, 22, 30),
            allDay: false
        }, {
            title: 'Click for Google',
            start: new Date(y, m, 28),
            end: new Date(y, m, 29),
            url: 'http://google.com/'
        }]
    });
});
