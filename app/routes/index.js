import Route from '@ember/routing/route';
import { service } from '@ember/service';

export default class IndexRoute extends Route {
  @service app;
  model() {
    window.location = 'http://' + this.app.host + ':8080/webWsatapp/';
  }
}
