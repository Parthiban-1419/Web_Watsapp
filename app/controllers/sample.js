import Controller from '@ember/controller';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';

export default class SampleController extends Controller {
  @tracked num = 10;

  getSocket() {
    console.log('creating sockets');
    this.socket = new WebSocket('ws://localhost:8080/webWsatapp/actions');
    this.socket.onmessage = this.onMessage;
    this.socket.controller = this;
  }

  onMessage(event) {
    this.controller.num = 11;
  }

  @action
  myAction() {
    console.log('typing...');
    setTimeout(function () {
      console.log('online');
    }, 2000);
  }
}
