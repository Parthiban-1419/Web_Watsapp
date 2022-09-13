import Route from '@ember/routing/route';

export default class LogOutRoute extends Route {
  model() {
    window.location = 'http://localhost:8080/webWsatapp/logOut.jsp';

    // var req = new XMLHttpRequest();

    // this.session;
    // req.open('POST', 'http://localhost:8080/Sample/Servlet2', true);
    // req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    // req.send();

    // req.onload = function () {
    //   console.log(this.responseText);
    // };
  }
}
