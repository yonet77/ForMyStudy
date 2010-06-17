
package com.yonet77.twitclone.comet

import _root_.scala.actors.Actor
import Actor._
import _root_.net.liftweb._
import http._
import _root_.com.yonet77.twitclone.model._

/**
 * メッセージを配信するTwitServer
 * ScalaのActorを継承し、ListenerManagerトレイトを継承することで
 * 登録されている他のActorにメッセージを配信できる
*/
object TwitServer extends Actor with ListenerManager {
  // 投稿されメッセージ(配信するとクリアする)
  private var msgs: List[Message] = Nil

  // 登録されている他のActorに配信するメッセージ
  protected def createUpdate = Messages(msgs)

  // メッセージ受信時の処理
  override def highPriority = {
    case m: Message =>
      msgs ::= m
      updateListeners()
      msgs = Nil
  }
  this.start
}

case class Messages(msgs: List[Message])
