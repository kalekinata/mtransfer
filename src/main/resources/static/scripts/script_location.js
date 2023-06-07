$(document).ready(function() {
    getLocation();
});

function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(sendLocation);
    } else {
        alert("Geolocation is not supported by this browser.");
    }
}

function sendLocation(position) {
    var location = {
        latitude: position.coords.latitude,
        longitude: position.coords.longitude
    };
    $.ajax({
        type: "POST",
        url: "/location/updateLocation",
        contentType: "application/json",
        data: JSON.stringify(location),
        success: function(response) {
            console.log(response);
        }
    });
}