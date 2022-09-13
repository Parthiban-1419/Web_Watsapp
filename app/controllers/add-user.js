import Controller from '@ember/controller';
import { action } from '@ember/object';
import { service } from '@ember/service';

export default class AddUserController extends Controller {
  @service router;
  @action
  addUser() {
    let self = this;
    var req = new XMLHttpRequest();

    var number = document.getElementById('number').value;
    var user = document.getElementById('user').value;
    var about = document.getElementById('about').value;
    var password = document.getElementById('password').value;
    var cPassword = document.getElementById('cPassword').value;

    if (cPassword === password) {
      req.onload = function () {
        console.log(this.responseText);
        if (this.responseText === 'true'){
            req.onload = function () {
              alert(this.responseText);
              self.router.transitionTo('index');
            };

            req.open('POST', 'http://localhost:8080/webWsatapp/create-account', true);
            req.setRequestHeader(
              'Content-Type',
              'application/x-www-form-urlencoded'
            );
            req.send('user=' + user + '&password=' + password);
        }
        else 
          alert('Cannot create account. Number already exists');
        
      };

      req.open('POST', 'http://localhost:8080/To-do/check-number', true);
      req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      req.send('user=' + user + '&password=' + password);
    } else {
      alert('Password miss match');
    }
  }
}
