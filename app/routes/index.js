import Route from '@ember/routing/route';

export default class IndexRoute extends Route {
  model() {
    window.location = 'http://localhost:8080/webWsatapp/';
  }
}
