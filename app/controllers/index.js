import Controller from '@ember/controller';
import { action } from '@ember/object';
import { service } from '@ember/service';

export default class IndexController extends Controller {
  //   @service router;
  //   @action
  //   checkUser() {
  //     let self = this;
  //     var req = new XMLHttpRequest();
  //     var user = document.getElementById('userId').value;
  //     var password = document.getElementById('password').value;
  //     req.open('POST', 'http://localhost:8080/Watsapp/check-user', true);
  //     req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  //     req.send('user=' + user + '&password=' + password);
  //     req.onload = function () {
  //       console.log('check user');
  //       console.log(this.responseText);
  //       if (this.responseText.match('true')) {
  //         self.router.transitionTo('app-home');
  //       } else {
  //         alert(this.responseText);
  //         location.reload();
  //       }
  //     };
  //   }
}
