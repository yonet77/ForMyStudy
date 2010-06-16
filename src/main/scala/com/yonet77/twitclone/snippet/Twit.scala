
package com.yonet77.twitclone.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.http.{RequestVar, S}
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util.{Box, Full}
import _root_.com.yonet77.twitclone.model._

class Twit {

  // <lift:Twit.post>タグでコールされるSnipeet関数
  // 入力フォームを生成
  def post(xhtml:NodeSeq):NodeSeq = {

    val user = User.currentUser

    val message = Message.create.user(user)

    val name = userName(User.currentUser)

    // submitされた時点で呼び出され、メッセージの内容をデータベースに保存する
    def addMessage:Unit = message.validate match{
      case Nil => message.save ; S.notice("メッセージを投稿しました。")
      case x => S.error (x)
    }

    // bind関数を利用して、引数のXHTML内で<twit:...>で始まるタグの内容を置き換え
    bind("twit", xhtml,
      "name" -> name,
      "status" -> message.status.toForm,
      "submit" -> submit("投稿する", () => addMessage)
    )
  }

  // <lift:Twit.show>タグでコールされるSnippet関数
  def show(xhtml:NodeSeq):NodeSeq = {
    <xml:Group>{
      Message.findAll.flatMap{ msg =>
        bind("twit", xhtml,
          "message" -> msg.status.is,
          "user" -> userName(msg.user.obj),
          "dateOf" -> msg.dateOf.is.toString
        )
      }
    }</xml:Group>
  }

/** Userモデルオブジェクトからユーザ名を取得する */
def userName( user:Box[User]) = user.dmap("Guest") {user => user.shortName}
}
