
// Reference messages collection
var messagesRef = firebase.database().ref('purchased');

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
  var newMessageRef = messagesRef.push();
  newMessageRef.set({
    phone:phone,
    paid:paid,
    description:desc
  });
}