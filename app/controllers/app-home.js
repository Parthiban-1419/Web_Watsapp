import Controller from '@ember/controller';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';

export default class AppHomeController extends Controller {
  @tracked chat;
  @tracked datas;
  @tracked index;
  @tracked user;
  @tracked keypad = false;
  @tracked type = true;
  @tracked friend;
  @tracked menu = false;

  getSocket() {
    console.log('creating sockets');
    this.socket = new WebSocket('ws://localhost:8080/webWsatapp/actions');
    this.socket.onmessage = this.onMessage;
    this.socket.controller = this;
  }

  onMessage(event) {
    var json = JSON.parse(event.data);
    console.log(json.action);
    if (json.action === 'newMessage') {
      console.log('new message');

      if (json.chat.sender === this.controller.friend)
        this.controller.printMessage(json);
      else
        for (let i = 0; i < this.controller.datas.length; i++)
          if (this.controller.datas[i].friend === json.chat.sender)
            this.controller.datas[i].chat.push(json.chat);
    }

    if (json.action === 'chatHistory') {
      console.log(json);
      this.controller.datas = json.chats;
    }

    if (json.action === 'status') {
      $('.'+json.user).html(json.status);
    }
    if (json.action === 'newChat') {
      var newChat = '{"chat" : [], "friend" : "' + json.friend + '", "index" : ' + this.controller.datas.length + '}'; 
      this.controller.datas.push(JSON.parse(newChat));
      console.log(this.controller.datas);
      this.controller.getChat(this.controller.datas.length-1);
    }
    if (json.action === 'friendNotFound') {
      $('#search-box').val('No chat found');
    }
  }

  printMessage(json) {
    var place;
    if (json.chat.sender === this.user) place = 'right';
    else place = 'left';

    var message =
      "<div class='" +
      place +
      "'>" +
      "<div class='message'>" +
      json.chat.message +
      '</div>' +
      '</div><br>';
    $('#' + this.friend).append(message);
    $('#' + this.friend).scrollTop($('#' + this.friend).scrollTop() + 50);
    this.datas[this.index].chat.push(json.chat);
  }

  @action
  sendMessage() {
    console.log(this.datas[this.index].chat);
    console.log(this.user);
    var message = {
      sender: this.user,
      receiver: this.datas[this.index].friend,
      message: document.getElementById('message').value,
      sent_time: Date.now(),
    };

    var json = {
      action: 'newMessage',
      chat: message,
    };
    console.log('sending');
    this.socket.send(JSON.stringify(json));
    this.printMessage(json);
    $('#message').val('');
  }

  @action
  getChat(index) {
    let self = this;
    var place, row, d, date = 0, printableDate, today = new Date().getDate() + "/" + new Date().getMonth() + "/" + new Date().getFullYear();
    
    this.chat = this.datas[index].chat;
    this.friend = this.datas[index].friend;
    console.log(this.chat);
    this.index = index;
    $('.container').html(
      '<div class="user-detail"><span class="dp"></span><p class="chatName">' +
        this.friend +
        '</p><div id="status"><p class="' + self.friend + '">offline</p></div></div><div class="chat" id="' +
        self.friend +
        '"></div>'
    );
    this.socket.send('{"action" : "getStatus", "user" : "' + self.user + '", "friend" : "' + self.friend + '"}');
    for (let i = 0; i < this.chat.length; i++) {
      d = new Date(this.chat[i].sent_time);
      if((d - date) > 86400000){
        printableDate = d.getDate() + "/" + d.getMonth() + "/" + d.getFullYear();
        if(printableDate === today)
          printableDate = "Today";
        // else if()
        //   printableDate = "Yesterday";
          
        $('#' + this.friend).html($('.chat').html() + '<center><div class="date"><center>' + printableDate + '</center></div></center>');
        date = d;
      }
      if (this.chat[i].sender === this.user) place = 'right';
      else place = 'left';
      row =
        "<div class='" +
        place +
        "'>" +
        "<div class='message'>" +
        this.chat[i].message + '<div class="time">' + d.getHours() + ":" + d.getMinutes() + '</div>' + 
        '</div></div><br>';

      $('#' + this.friend).html($('.chat').html() + row);
      $('#' + this.friend).scrollTop($('#' + this.friend).scrollTop() + 50);
    }
    this.keypad = true;
    $('#message').val('');
    $('#message').focus();
  }

  createAppHome() {
    console.log('creating app-home');
    for (let i = 0; i < this.json.chats.length; i++) {
      $('.contacts').html(function () {
        return (
          $('.contacts').html() + //'<LinkTo @route="main-w indow.chat" @model={{arr.sender}}>' +
          '<div class="name" onclick={{fn this.printChat ' +
          this.json.chats[i].chat +
          '}}>' +
          '<span class="profile"></span>' +
          '<p id="name">' +
          this.json.chats[i].friend +
          '</p>' +
          '</div>' +
          '</LinkTo>'
        );
      });
    }
  }

  @action
  toggleMenu(){
    this.menu = !this.menu;
  }

  typing(self) {
    if (self.type) {
      console.log('typing...');
      self.socket.send('{"action" : "status", "user" : "' + self.user + '", "friend" : "' + self.friend + '", "status" : "typing..."}');
      // $("#status").html("tying...");
      setTimeout(() => {
        if(!self.type){
          self.socket.send('{"action" : "status", "user" : "' + self.user + '", "friend" : "' + self.friend + '", "status" : "online"}');
          // $("#status").html("online");
          console.log('online');
        }
        self.type = true;
      }, 2000);
    }
    self.type = false;
  }

  @action
  searchUser(){
    let i=0;
    var friendName = $('#search-box').val();
    for(; i<this.datas.length; i++){
      if(friendName === this.datas[i].friend){
        this.getChat(this.datas[i].index);
        break;
      }
    }
    if(i===this.datas.length){
      if(this.user != friendName)
        this.socket.send('{"action" : "getNewChat", "user": "' + this.user + '", "friend" : "' + friendName + '"}');
    }
    setTimeout(() => { $('#search-box').val('');}, 1000);
  }
}