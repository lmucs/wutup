onload = function () {
    initializeEventQueue("currentEventStream", currentEventNames.length, currentEventNames);
    initializeEventQueue("hostingEventStream", hostingEventNames.length, hostingEventNames);
    initializeEventQueue("attendingEventStream", attendingEventNames.length, attendingEventNames);
    initializeEventQueue("invitedToEventStream", invitedToEventNames.length, invitedToEventNames);

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