import Controller from '@ember/controller';
import { action } from '@ember/object';
import { service } from '@ember/service';

export default class AddUserController extends Controller {
  @service router;
  @service app;

  @action
  addUser() {
    let self = this;
    var req = new XMLHttpRequest();

    var number = document.getElementById('number').value;
    var user = document.getElementById('user').value;
    var about = document.getElementById('about').value;
    var password = document.getElementById('password').value;
    var cPassword = document.getElementById('cPassword').value;
    // var profile = document.getElementById('profile').value;
    // console.log(profile);
    // console.log($('#profile').val());
    // profile = 'C:\\Users\\parthi-pt4593\\Pictures\\Screenshots\\Screenshot(1).png';
    if (cPassword === password) {
      req.onload = function () {
        console.log(this.responseText);
        if (this.responseText === 'false') {
          req.onload = function () {
            console.log(this.responseText);
            alert(this.responseText);
            self.router.transitionTo('index');
          };

          req.open(
            'POST',
            'http://' + self.app.host + ':8080/webWsatapp/create-account',
            true
          );
          req.setRequestHeader(
            'Content-Type',
            'application/x-www-form-urlencoded'
          );
          req.send(
            'number=' +
              number +
              '&user=' +
              user +
              '&about=' +
              about +
              '&password=' +
              password
              
          );
        } else alert('Cannot create account. Number already exists');
      };

      req.open('POST', 'http://' + this.app.host + ':8080/webWsatapp/check-number', true);
      req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      req.send('number=' + number);
    } else {
      alert('Password miss match');
    }
  }

  @action
  change(event){
    console.log(event);
  }
}
