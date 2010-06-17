package com.yonet77.twitclone.snippet

import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.http.js.JE._
import _root_.net.liftweb.http.js.JsCmds._
import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.util.{Box, Full}
import _root_.com.yonet77.twitclone.model._

class HelloWorld {
  def howdy = <span>Welcome to TwitClone at {new _root_.java.util.Date}</span>

  def greeting(html: NodeSeq): NodeSeq = {
    bind("greeting", html,
       // ajaxButto関数で、サーバへAjaxでリクエストを送信するボタンを生成
      "button" -> ajaxButton(Text("ClickMe!"),{
       // 第２引数には、Ajaxで呼び出された時のサーバ側の処理を関数オブジェクトで渡す
       () => {
         println("Ajaxで呼び出された・・")
         val userName = User.currentUser.dmap("Guest"){_.shortName}
         SetHtml("id_greeting_div",Text("こんにちは！ %s さん".format(userName)))
       }
      })
    )
  }
}

