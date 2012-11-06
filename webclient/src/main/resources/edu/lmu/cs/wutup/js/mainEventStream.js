onload = function () {

    initializeEventQueue("mainEventStreamContainer", "mainEventStream", 10, true);
    initializeEventQueue("tabHosting", "hostingEventStream", 2);
    initializeEventQueue("tabAttending", "attendingEventStream", 7);
    initializeEventQueue("tabInvitedTo", "invitedToEventStream", 16);

}

var initializeEventQueue = function (goesHere, id, rows, insertButtons) {

    var usesButtons = insertButtons || false;
    var totalEvents = rows;

    var root=document.getElementById(goesHere);
    var table=document.createElement("table");
    table.className="table table-hover";
    table.id = id;
    table.style.attribute = "value";

    var tableBody=document.createElement('tbody');
    var event, buttonCell, acceptRow, acceptButton, declineRow, declineButton, maybeRow, maybeButton;
    var pictureCell, picture;
    var detailsCell, eventTitle, eventDescription;
    var timeCell;
    
    for (var i = 0; i < totalEvents; i++) {
    
        event = document.createElement("tr");
    
        if (usesButtons) {
            
            buttonCell = document.createElement("td");

            acceptRow = document.createElement("tr");
            acceptButton = document.createElement("btn");
            acceptButton.id = id + "-acceptButton";
            acceptButton.className = "btn btn-small";
            acceptButton.innerHTML = "Accept";
            acceptRow.appendChild(acceptButton);

            declineRow = document.createElement("tr");
            declineButton = document.createElement("btn");
            declineButton.id = id + "-declineButton";
            declineButton.className = "btn btn-small";
            declineButton.innerHTML = "Decline";
            declineRow.appendChild(declineButton);

            maybeRow = document.createElement("tr");
            maybeButton = document.createElement("btn");
            maybeButton.id = id + "-maybeButton";
            maybeButton.className = "btn btn-small";
            maybeButton.innerHTML = "Maybe";
            maybeRow.appendChild(maybeButton);
            
            buttonCell.appendChild(acceptRow);
            buttonCell.appendChild(declineRow);
            buttonCell.appendChild(maybeRow);
            
            event.appendChild(buttonCell);
        }
        

        pictureCell = document.createElement('td');
        
        picture = document.createElement("img");
        picture.src = "http://24.media.tumblr.com/tumblr_lurm27QJ2X1r6d623o1_500.jpg";
        picture.alt = "Llamas";
        picture.height = "50";
        picture.width = "50";

        pictureCell.appendChild(picture);

        detailsCell = document.createElement("td");
        detailsCell.colSpan = "2";
        eventTitle = document.createElement("a");
        eventTitle.href = "http://www.noahsplacepettingzoo.com/llama.html";
        eventTitle.innerHTML = "Llama Petting Zoo";
        eventDescription = document.createElement("p");
        eventDescription.innerHTML = "IT'S GOING TO BE A LLAMA PALOOZA";
        detailsCell.appendChild(eventTitle);
        detailsCell.appendChild(eventDescription);

        timeCell = document.createElement("td");
        timeCell.innerHTML = "time goes here";
        
        
        event.appendChild(pictureCell);
        event.appendChild(detailsCell);
        event.appendChild(timeCell);

        tableBody.appendChild(event);
    }

    table.appendChild(tableBody);
    root.appendChild(table);
}


