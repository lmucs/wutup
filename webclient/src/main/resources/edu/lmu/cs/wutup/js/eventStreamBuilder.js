onload = function () {

    initializeEventQueue("mainEventStream", 10);
    initializeEventQueue("hostingEventStream", 2);
    initializeEventQueue("attendingEventStream", 7);
    initializeEventQueue("invitedToEventStream", 16);

}

var initializeEventQueue = function (id, rows) {

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
}


