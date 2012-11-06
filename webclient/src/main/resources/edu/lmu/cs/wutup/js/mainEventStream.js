onload= function () {

    var totalRows=12;

    var root=document.getElementById("mainEventStreamContainer");
    var table=document.createElement("table");
    table.className="table table-hover";
    table.id = "mainEventStream";

    var tableHead = document.createElement("thead");
    var headRow = document.createElement("tr");
    var eventPicture = document.createElement("th");
    eventPicture.innerHTML = "eventPicture";
    var eventDetails = document.createElement("th");
    eventDetails.innerHTML = "eventDetails";
    eventDetails.colSpan = "2";
    var eventTime = document.createElement("th");
    eventTime.innerHTML = "eventTime";
    
    headRow.appendChild(eventPicture);
    headRow.appendChild(eventDetails);
    headRow.appendChild(eventTime);
    tableHead.appendChild(headRow);
    table.appendChild(tableHead);

    var tableBody=document.createElement('tbody');
    var row, pictureCell, picture, detailsCell, eventTitle, eventDescription, timeCell;
    
    for (var i = 0; i < totalRows; i++) {
        row = document.createElement("tr");

        pictureCell = document.createElement('td');
        
        picture = document.createElement("img");
        picture.src = "http://24.media.tumblr.com/tumblr_lurm27QJ2X1r6d623o1_500.jpg";
        picture.alt = "Llamas";
        picture.height = "50";
        picture.width = "50";

        pictureCell.appendChild(picture);

        detailsCell = document.createElement("td");
        detailsCell.colSpan = "2";
        eventTitle = document.createElement("p");
        eventTitle.innerHTML = "Llama Petting Zoo";
        eventDescription = document.createElement("p");
        eventDescription.innerHTML = "IT'S GOING TO BE A LLAMA PALOOZA";
        detailsCell.appendChild(eventTitle);
        detailsCell.appendChild(eventDescription);

        timeCell = document.createElement("td");
        timeCell.innerHTML = "time goes here";
        
        row.appendChild(pictureCell);
        row.appendChild(detailsCell);
        row.appendChild(timeCell);

        tableBody.appendChild(row);
    }

    table.appendChild(tableBody);
    root.appendChild(table);
}


