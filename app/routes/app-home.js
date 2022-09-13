import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';

export default class AppHomeRoute extends Route {
  @service app;
  @service router;
  // @tracked user;

  model() {
    console.log('app-home');
    let self = this;
    var req = new XMLHttpRequest();

    req.onload = function () {
      console.log(this.responseText);
      if (this.responseText === '') self.router.transitionTo('index');
      // else self.user = this.responseText;
    };

    req.open('POST', 'http://' + this.app.host + ':8080/webWsatapp/get-user', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send();

    return this.socket;
  }

  setupController = function (controller) {
    // controller.set('user', this.user);
    controller.getSocket();
  };
}
