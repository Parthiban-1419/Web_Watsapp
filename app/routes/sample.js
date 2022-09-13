import Route from '@ember/routing/route';
import { tracked } from '@glimmer/tracking';
import { service } from '@ember/service';

export default class SampleRoute extends Route {
  @service router;
  @tracked number = 10;

  model() {
    // this.socket = new WebSocket('ws://localhost:8080/webWsatapp/actions');
    // this.socket.onmessage = this.onMessage;
    // this.socket.route = this;
    // this.socket.Num = this.num;
    // // this.socket.numb = 11;
    // console.log(this.number);
    // return this.socket;
    // console.log("model");
    // this.router.transitionTo('app-home');
  }
  onMessage(event) {
    // self.call(1);
    // self.Num();
    // self.numb = 5;
    // self.num1 = 5;
    console.log(this.route.number);
    this.route.number = 11;
    this.Num();
  }
  call(i) {
    console.log('call ' + i + ' ' + this.numb);
  }
  num() {
    console.log(this.number);
  }
  // });

  setupController = function (controller) {
    controller.getSocket();
  };
}
