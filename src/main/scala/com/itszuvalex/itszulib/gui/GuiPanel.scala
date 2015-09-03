package com.itszuvalex.itszulib.gui

import java.util

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christopher Harris (Itszuvalex) on 9/3/15.
 */
trait GuiPanel extends GuiElement {
  var width : Int
  var height: Int
  var moused      = false
  val subElements = ArrayBuffer[GuiElement]()

  override def onMouseEnter(): Unit = {
    moused = true
  }

  override def onMouseLeave(): Unit = {
    moused = false
  }

  override def onMouseClick(mouseX: Int, mouseY: Int, button: Int): Unit = {

  }

  override def addTooltip(mouseX: Int, mouseY: Int, tooltip: util.List[String]): Unit = {

  }

  override def isLocationInside(mouseX: Int, mouseY: Int): Boolean = {
    ((mouseX >= x) && (mouseX < x + width)) &&
    ((mouseY >= y) && (mouseY < y + height))
  }

  override def isMousedOver: Boolean = moused

  override def update(): Unit = subElements.foreach(_.update())

  override def render(screenX: Int, screenY: Int): Unit = {

  }
}
