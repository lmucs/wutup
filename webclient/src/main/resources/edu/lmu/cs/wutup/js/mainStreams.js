onload = function () {
    initializeEventQueue("current-event-stream", currentEventNames.length, currentEventNames);
    initializeEventQueue("hosting-event-stream", hostingEventNames.length, hostingEventNames);
    initializeEventQueue("attending-event-stream", attendingEventNames.length, attendingEventNames);
    initializeEventQueue("invited-to-event-stream", invitedToEventNames.length, invitedToEventNames);

}

var totalCurrentEvents = 10;
var currentEventNames = ["Llama Petting Zoo",
                                        "Pool Party",
                                        "Surf Club Meeting",
                                        "Secret Santa Party",
                                        "Tree Lighting Ceremony",
                                        "999th convo",
                                        "AB Trip Meeting",
                                        "Senior Beer Tasting",
                                        "Cool Kids Club",
                                        "Thanksgiving"];

var currentEvents = [
    ["Llama Petting Zoo", "Llama Llama Llama Llama", "2:00 pm - 3:00 pm"],
        ["Pool Party", "Llama Llama Llama Llama", "2:00 pm - 3:00 pm"],
        ["Surf Club Meeting", "Llama Llama Llama Llama", "2:00 pm - 3:00 pm"],
        ["Secret Santa Party", "Llama Llama Llama Llama", "2:00 pm - 3:00 pm"],
        ["Tree Lighting Ceremony", "Llama Llama Llama Llama", "2:00 pm - 3:00 pm"],
        ["999th convo", "Llama Llama Llama Llama", "2:00 pm - 3:00 pm"],
        ["AB Trip Meeting", "Llama Llama Llama Llama", "2:00 pm - 3:00 pm"],
        ["Senior Beer Tasting", "Llama Llama Llama Llama", "2:00 pm - 3:00 pm"],
        ["Cool Kids Club", "Llama Llama Llama Llama", "2:00 pm - 3:00 pm"],
        ["Thanksgiving", "Llama Llama Llama Llama", "2:00 pm - 3:00 pm"]
];


var eventPost = function eventPost (name, desc, time) {
    this.name = name;
    //this.image = image;
    this.desc = desc;
    this.time = time;
}

var event1 = new eventPost("Llama Petting Zoo",  "Llama Llama Llama Llama", "2:00 pm - 3:00 pm");
var event2 = new eventPost("Pool Party",  "Pool Pool Pool Pool Pool", "3:00 pm - 3:30 pm");
var event3 = new eventPost("Surf Club Meeting",  "Surf Surf Surf Surf Surf", "4:00 pm - 5:00 pm");
var event4 = new eventPost("Secret Santa Party",  "Secret Secret Secret Secret Secret", "6:00 pm - 8:00 pm");
var event5 = new eventPost("Tree Lighting Ceremony",  "Tree Tree Tree Tree Tree", "8:00 pm - 8:30 pm");
var event6 = new eventPost("Convo",  "Convo Convo Convo Convo Convo", "1:00 pm - 3:00 pm");
var event7 = new eventPost("AB San Diego Meeting",  "AB AB AB AB AB", "10:00 pm - 11:00 pm");
var event8 = new eventPost("Senior Beer Tasting",  "Beer Beer Beer Beer Beer", "10:00 pm - 11:00 pm");
var event9 = new eventPost("Cool Kids Club",  "Cool Cool Cool Cool Cool", "2:00 pm - 3:00 pm");
var event10 = new eventPost("Thanksgiving",  "Thanks Thanks Thanks Thanks Thanks", "2:00 pm - 3:00 pm");

var currentEvents = [
    event1, 
    event2,
    event3,
    event4,
    event5,
    event6,
    event7,
    event8,
    event9,
    event10
];

var totalHostingEvents = 2;
var hostingEventNames = ["Llama Petting Zoo",
                                        "Pool Party"];
                                        
                                        
var totalAttendingEvents = 7;
var attendingEventNames = ["Llama Petting Zoo",
                                        "Pool Party",
                                        "Surf Club Meeting",
                                        "Secret Santa Party",
                                        "Tree Lighting Ceremony",
                                        "999th convo",
                                        "AB Trip Meeting"];
                                        
var totalInvitedToEvents = 5;
var invitedToEventNames = ["Llama Petting Zoo",
                                        "Pool Party",
                                        "Surf Club Meeting",
                                        "Secret Santa Party",
                                        "Tree Lighting Ceremony"];                                        