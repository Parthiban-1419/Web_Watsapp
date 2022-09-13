import Route from '@ember/routing/route';
import { service } from '@ember/service';

export default class AddContactRoute extends Route {
  @service router;
  @service app;

  model() {
    if (!this.app.number) this.router.transitionTo('app-home');
    return this.app.number;
  }
}
