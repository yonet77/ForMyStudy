
package com.yonet77.twitclone.comet

import _root_.scala.actors._
import _root_.scala.actors.Actor._
import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util.{Box, Full}
import _root_.net.liftweb.http.js.JsCmds._
import _root_.net.liftweb.http.js.JE._
import _root_.net.liftweb.http.js.jquery.JqJsCmds._
import _root_.com.yonet77.twitclone.model._

/**
 * ブラウザとComet接続し、非同期にメッセージを表示させるSnippet
 * CometActorを継承し、他のListenerManagerを継承したActorに登録するため、
 * CometListeneeトレイトを継承する
*/
class CometTwit extends CometActor with CometListenee {

  // NameSpace
  override def defaultPrefix = Full("twit")

  // 非同期にメッセージを表示させるタグのID
  private lazy val spanId = uniqueId + "_messages_span"

  // SnippetがTemplateから呼び出された時、<Twit:messages/>タグの内容を出力する
  def render = bind("messages" -> <span id={spanId}><div></div></span>)

  // 他のActorに登録する。
  protected def registerWith = TwitServer

  // このActorがメッセージを受信したときの処理
  override def highPriority = {
    case Messages(msgs) =>
      partialUpdate(PrependHtml(spanId,
        <xml:Group>
          { msgs.map( m=>
            <ul class="status">
              <li class = "message">{m.status.is}</li>
              <li class = "user">{userName(m.user.obj)}</li>
              <li class = "dateOf">{m.dateOf.is.toString}</li>
              <hr/>
            </ul> )
          }
        </xml:Group>
      ))
  }

  /**
   * Userモデルオブジェクトからユーザー名を取得するユーティリティ関数
   */
  def userName( user:Box[User] ) = user.dmap( "Guest" ){ user => user.shortName}

}
