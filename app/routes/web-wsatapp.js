import Route from '@ember/routing/route';

export default class WebWsatappRoute extends Route {
  model(params) {
    let self = this;
    var req = new XMLHttpRequest();

    req.onload = function () {
      console.log(this.responseText);
    };

    req.open('POST', 'http://localhost:8080/webWsatapp/getSession ', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send();
  }
}
