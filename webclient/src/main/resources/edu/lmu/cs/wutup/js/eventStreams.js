onload = function () {
    initializeEventQueue("hostingEventStream", hostingEventNames.length, hostingEventNames);
    initializeEventQueue("attendingEventStream", attendingEventNames.length, attendingEventNames);
    initializeEventQueue("invitedToEventStream", invitedToEventNames.length, invitedToEventNames);
    
   
    initializeGuestList("Attending", 7, attendingGuests);
    initializeGuestList("Maybe", 4, maybeGuests);

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

var attendingGuests = ["Allyson",
                                    "Sam",
                                    "Marc",
                                    "Andy",
                                    "Eric",
                                    "Rich",
                                    "Tyler"];

var maybeGuests = ["Amy", "Shelby", "Bao", "Alexis"]