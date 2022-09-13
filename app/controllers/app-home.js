import Controller from '@ember/controller';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';
import { service } from '@ember/service';

export default class AppHomeController extends Controller {
  @service app;
  @tracked chat;
  @tracked datas;
  @tracked index;
  @tracked user;
  @tracked keypad = false;
  @tracked type = true;
  @tracked f_number;
  @tracked friend;
  @tracked menu = false;
  @tracked contact = false;
  @tracked date;

  getSocket() {
    console.log('creating sockets');
    this.socket = new WebSocket('ws://' + this.app.host + ':8080/webWsatapp/actions');
    this.socket.onmessage = this.onMessage;
    this.socket.controller = this;
  }

  onMessage(event) {
    let i = 0;
    var json = JSON.parse(event.data);
    console.log(json.action);
    if (json.action === 'newMessage') {
      console.log('new message');

      if (json.chat.sender === this.controller.f_number)
        this.controller.printMessage(json.chat);
      else
        for (; i < this.controller.datas.length; i++){
          if (this.controller.datas[i].f_number === json.chat.sender){
            this.controller.datas[i].chat.push(json.chat);
          }
        }
        // if(i===this.controller.datas.length){

        // }

    }

    if (json.action === 'chatHistory') {
      console.log(json);
      this.controller.user = json.userName;
      this.controller.app.user = this.controller.user;
      this.controller.app.number = json.number;
      this.controller.app.profile = json.profile;
      this.controller.datas = json.chats;
      this.controller.createAppHome();
    }

    if (json.action === 'status') {
      $('.' + json.user).html(json.status);
    }

    if (json.action === 'newChat') {
      var newChat =
        '{"chat" : [], "friend" : "' +
        json.friend +
        '", "index" : ' +
        this.controller.datas.length +
        '}';
      this.controller.datas.push(JSON.parse(newChat));
      console.log(this.controller.datas);
      this.controller.getChat(this.controller.datas.length - 1);
    }
    if (json.action === 'friendNotFound') {
      $('#search-box').val('No chat found');
    }
  }

  printMessage(json) {
    var place;
    console.log(json);
    if (json.sender === this.app.number) place = 'right';
    else place = 'left';

    var message =
      "<div class='" +
      place +
      "'>" +
      "<div class='message'>" +
      json.message +
      '</div>' +
      '</div><br>';
    $('#' + this.friend).append(message);
    $('#' + this.friend).scrollTop($('#' + this.friend).scrollTop() + 50);
    this.datas[this.index].chat.push(json);
  }

  @action
  sendMessage() {
    var mes = document.getElementById('message').value;
    if(mes != ''){
      var message = {
        sender: this.app.number,
        receiver: this.datas[this.index].f_number,
        message: mes,
        sent_time: Date.now(),
      };

      var json = {
        action: 'newMessage',
        chat: message,
      };
      console.log('sending');
      this.socket.send(JSON.stringify(json));
      this.printMessage(json.chat);
      $('#message').val('');
    }
  }

  @action
  getChat(index) {
    console.log(index);
    if(!this.datas[index].user){
      alert('Contact not on watsapp');
    }
    else{
      $('#' + index).css('background-color', 'lightgrey');
      if(index != this.index)
        $('#' + this.index).css('background-color', 'aliceblue')
      let self = this;
      var place,
        row,
        d,
        date = 0,
        printableDate,
        today =
          new Date().getDate() +
          '/' +
          new Date().getMonth() +
          '/' +
          new Date().getFullYear();
      console.log(index);
      console.log(self.datas);
      this.chat = this.datas[index].chat;
      this.friend = this.datas[index].friend;
      this.f_number = this.datas[index].f_number;
      console.log(this.chat);
      this.index = index;
      $('.container').html(
        '<div class="user-detail"><p class="chatName">' +
          this.friend +
          '</p><div id="status"><p class="' +
          self.f_number +
          '">offline</p></div></div><div class="chat" id="' +
          self.friend +
          '"></div>'
      );
      this.socket.send(
        '{"action" : "getStatus", "user" : "' +
          self.app.number +
          '", "friend" : "' +
          self.f_number +
          '"}'
      );
      for (let i = 0; i < this.chat.length; i++) {
        d = new Date(this.chat[i].sent_time);
        if (d - date > 86400000) {
          printableDate =
            d.getDate() + '/' + d.getMonth() + '/' + d.getFullYear();
          if (printableDate === today) printableDate = 'Today';

          $('#' + this.friend).html(
            $('.chat').html() +
              '<center><div class="date"><center>' +
              printableDate +
              '</center></div></center>'
          );
          date = d;
        }

        if (this.chat[i].sender === this.app.number) place = 'right';
        else place = 'left';
        row =
          "<div class='" +
          place +
          "'>" +
          "<div class='message'>" +
          this.chat[i].message +
          '<div class="time">' +
          d.getHours() +
          ':' +
          d.getMinutes() +
          '</div>' +
          '</div></div><br>';

        console.log(this.chat[i]);
        $('#' + this.friend).html($('.chat').html() + row);
        // self.printMessage(this.chat[i]);
        $('#' + this.friend).scrollTop($('#' + this.friend).scrollTop() + 500);
      }
      this.keypad = true;
      $('#message').val('');
      $('#message').focus();
  }
  }

  createAppHome() {
    let self = this,
      i = 0;
    console.log('creating app-home');
    console.log(this.datas);
    for (; i < this.datas.length; i++) {
      if(!this.datas[i].user || this.datas[i].chat.length === 0)
        continue;
      $('.contacts').html(function () {
        return (
          $('.contacts').html() + //'<LinkTo @route="main-w indow.chat" @model={{arr.sender}}>' +
          '<div class="name" ' + // onclick={{fn this.printChat ' +
          // self.datas[i].chat +
          // '}}
          ' id="' +
          i +
          '">' +
          // '<span class="profile"> <img srv="data:image/png;base64,' + self.datas[i].profile +  '"></span>' +
          '<p id="name">' +
          self.datas[i].friend +
          '</p>' +
          '</div>' +
          '</LinkTo>'
        );
      });
      $('.name').on('click', function () {
        self.getChat($(this).attr('id'));
      });
    }
  }

  @action
  toggleMenu() {
    this.menu = !this.menu;
      $('.menu').on('mouseenter', function(){
        this.menu = !this.menu;
      });
  }

  typing(self) {
    $('#message').keypress(function(e){
      if(e.key === 'Enter'){
        console.log("enteres");
        self.sendMessage();
      }         
    })
    if (self.type) {
      self.socket.send(
        '{"action" : "status", "user" : "' +
          self.app.number +
          '", "friend" : "' +
          self.datas[self.index].f_number +
          '", "status" : "typing..."}'
      );
      setTimeout(() => {
        if (!self.type) {
          self.socket.send(
            '{"action" : "status", "user" : "' +
              self.app.number +
              '", "friend" : "' +
              self.datas[self.index].f_number +
              '", "status" : "online"}'
          );
          console.log('online');
        }
        self.type = true;
      }, 2000);
    }
    self.type = false;
  }

  @action
  searchUser() {
    let i = 0;
    var friendName = $('#search-box').val();
    for (; i < this.datas.length; i++) {
      if (friendName === this.datas[i].friend) {
        this.getChat(this.datas[i].index);
        break;
      }
    }
    if (i === this.datas.length) {
      alert('No contact found on this name');
    }
    setTimeout(() => {
      $('#search-box').val('');
    }, 500);
  }

  @action
  addContact() {
    this.menu = false;
    this.contact = true;
  }

  @action
  save() {
    this.contact = false;
    var number = document.getElementById('f_number').value;
    var name = document.getElementById('f_name').value;
    var contact = {
      action: 'newContact',
      f_number: number,
      f_name: name,
    };
    this.socket.send(JSON.stringify(contact));
  }
}
