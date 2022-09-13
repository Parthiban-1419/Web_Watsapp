import EmberRouter from '@ember/routing/router';
import config from 'web-watsapp/config/environment';

export default class Router extends EmberRouter {
  location = config.locationType;
  rootURL = config.rootURL;
}

Router.map(function () {
  this.route('app-home');
  this.route('log-out');
  this.route('test');
  this.route('add-user');
  this.route('Sample');
  this.route('webWsatapp');
  this.route('add-contact');
  this.route('update-user');
  this.route('my-account');
});
