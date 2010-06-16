
package com.yonet77.twitclone.model

import java.util.Date
import _root_.net.liftweb.mapper._
import _root_.scala.xml.Text
import _root_.net.liftweb.http.FieldError
import _root_.net.liftweb.util.Helpers._

/**
* 投稿されたメッセージに対応するMapperクラス
*/
class Message extends LongKeyedMapper[Message] with IdPK {
  // MetaMapperを返す
  def getSingleton = Message

  // 本文
  object status extends MappedTextarea(this, 140){
    /** Textareaのカラム数 */
    override def textareaCols = 40
    /** Textareaの行数 */
    override def textareaRows = 4

    /** 入力チェックの定義 */
    override def validations =
      valNotNull("メッセージを入力してください。") _ ::
      valMaxLen(140, "メッセージは140文字以内です。") _ ::
      super.validations

    /** 入力チェック関数 */
    def valNotNull(msg : => String)(value : String): List[FieldError] =
      if ((value ne null) && (value ne "")) Nil
      else List(FieldError(this, Text(msg)))
  }

  // 投稿日時
  object dateOf extends MappedDateTime(this){
    override def defaultValue = new Date
  }

  // 投稿ユーザ
  object user extends MappedLongForeignKey(this, User)

}

/**
* 投稿されたメッセージに対応するMetaMapperオブジェクト
*/
object Message extends Message with LongKeyedMetaMapper[Message]{
  // ソート順の指定
  override def fieldOrder = List(dateOf, id, status)
}