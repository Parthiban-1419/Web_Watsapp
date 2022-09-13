import Controller from '@ember/controller';
import { action } from '@ember/object';
import { service } from '@ember/service';

export default class AddContactController extends Controller {
  @service router;
  @service app;

  @action
  addContact() {
    let self = this;
    var req = new XMLHttpRequest();

    var number = document.getElementById('f_number').value;
    var name = document.getElementById('f_name').value;

    req.onload = function () {
      alert(this.responseText);
      self.router.transitionTo('app-home');
    };

    req.open('POST', 'http://' + this.app.host + ':8080/webWsatapp/add-contact', true);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('user=' + this.model + '&f_number=' + number + '&f_name=' + name);
  }
}
