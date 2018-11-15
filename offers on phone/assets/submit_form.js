
// Reference messages collection
var messagesRef = firebase.database().ref("stores/"+store_name);

// Listen for form submit
window.onload=function(){
  document.getElementById('purchase_form').addEventListener('submit', submitForm);

}

// Submit form
function submitForm(e){
  e.preventDefault();

  // Get values
  var phone = getInputVal('phone');
  var paid = getInputVal('paid');
  var desc = getInputVal('desc');

  //console.log(phone,paid,desc);

  // Save message
  saveMessage(phone, paid, desc);

  // Show alert
  document.querySelector('.alert').style.display = 'block'; 

  // Hide alert after 3 seconds
  setTimeout(function(){
    document.querySelector('.alert').style.display = 'none';
  },2000);

  // Clear form
  document.getElementById('purchase_form').reset();
}

// Function to get get form values
function getInputVal(id){
  return document.getElementById(id).value;
}

// Save message to firebase
function saveMessage(phone, paid, desc){
  var date=(Date(Date.now())).toString();
  var newMessageRef = messagesRef.push();
  newMessageRef.set({
    phone:phone,
    paid:paid,
    description:desc,
    date:date
  });
  
  //referring to user node
  var userSms = firebase.database().ref("users/"+phone+"/stores/"+store_name);
  //sending to user node
  var newUserSms = userSms.push();
  newUserSms.set({
    paid:paid,
    date:date
  });
}
function scan(){
  let scanner = new Instascan.Scanner({ video: document.getElementById('preview') });
  scanner.addListener('scan', function (content) {
    //console.log(content);
    document.getElementById("phone").value = parseInt(content);
    scanner.stop();
    off();
  });
  Instascan.Camera.getCameras().then(function (cameras) {
    if (cameras.length > 0) {
      scanner.start(cameras[0]);
    } else {
      console.error('No cameras found.');
    }
  }).catch(function (e) {
    console.error(e);
  });
}
function off() {
  document.getElementById("overlay").style.display = "none";
}
function on() {
  document.getElementById("overlay").style.display = "block";
  let scanner = new Instascan.Scanner({ video: document.getElementById('preview') });
  scanner.addListener('scan', function (content) {
    //console.log(content);
    document.getElementById("phone").value = parseInt(content);
    scanner.stop();
    off();
  });
  Instascan.Camera.getCameras().then(function (cameras) {
    if (cameras.length > 0) {
      scanner.start(cameras[0]);
    } else {
      console.error('No cameras found.');
    }
  }).catch(function (e) {
    console.error(e);
  });
}

