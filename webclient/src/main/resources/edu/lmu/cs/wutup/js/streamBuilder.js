var initializeEventQueue = function (id, rows, names) {

    var isMainEventStream = (id === "mainEventStream") ? true : false;
    var totalEvents = rows;

    var table=document.getElementById(id);
    table.style.attribute = "value";

    var tableBody=document.createElement('tbody');
    var event, buttonCell, acceptRow, acceptButton, declineRow, declineButton, maybeRow, maybeButton;
    var pictureCell, picture;
    var detailsCell, eventTitle, eventDescription;
    var timeCell;
    
    var navigation, previousDateCell, previousDate, previousDateIcon, nextDateCell, nextDate, nextDateIcon, todayDateCell, todayDate;
 
    var date = new Date();
    var day = date.getDate();
    var month = date.getMonth();
    var year = date.getFullYear();
 
    for (var i = 0; i < totalEvents; i++) {
    
        event = document.createElement("tr");
    
        if (isMainEventStream) {
            

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
        eventTitle.href = "edu.lmu.cs.wutup.EventPage";
        eventTitle.innerHTML = names[i];
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
}

var initializeGuestList = function (id, rows, names) {

    var totalAttendees = rows;

    var table=document.getElementById(id);
    table.style.attribute = "value";

    var tableBody=document.createElement('tbody');
    var attendee;
    var profilePictureCell, profilePicture;
    var guestNameCell;
    var navigation, previousDateCell, previousDate, previousDateIcon, nextDateCell, nextDate, nextDateIcon, todayDateCell, todayDate;
 
    for (var i = 0; i < totalAttendees; i++) {
    
        attendee = document.createElement("tr");

        profilePictureCell = document.createElement('td');
        
        profilePicture = document.createElement("img");
        profilePicture.src = "http://24.media.tumblr.com/tumblr_lurm27QJ2X1r6d623o1_500.jpg";
        profilePicture.alt = "Llamas";
        profilePicture.height = "50";
        profilePicture.width = "50";

        profilePictureCell.appendChild(profilePicture);

        guestNameCell = document.createElement("td");
        guestNameCell.innerHTML = names[i];
        
        
        attendee.appendChild(profilePictureCell);
        attendee.appendChild(guestNameCell);

        tableBody.appendChild(attendee);
    }

    table.appendChild(tableBody);

}


