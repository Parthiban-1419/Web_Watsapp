import Service from '@ember/service';
import { tracked } from '@glimmer/tracking';

export default class AppService extends Service {
  @tracked type = false;
}
