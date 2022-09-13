import Controller from '@ember/controller';
import { action } from '@ember/object';

export default class TestController extends Controller {
  @action
  checkUser() {
    let self = this;
    var req = new XMLHttpRequest();

    var user = document.getElementById('userId').value;
    var password = document.getElementById('password').value;

    req.open('POST', 'http://localhost:8080/Watsapp/check-user', true);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('user=' + user + '&password=' + password);

    req.onload = function () {
      console.log('check user');
      console.log(this.responseText);
      console.log(req.getRemoteUser);
    };
  }

  @action
  btn() {
    let self = this;
    $('.test').html('<button>click</button>');
    $('button').on('click', function () {
      self.print();
    });
  }
  print() {
    console.log('printing...');
  }
}
