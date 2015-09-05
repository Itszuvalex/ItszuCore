package com.itszuvalex.itszulib.gui

import scala.collection.mutable.ListBuffer

/**
 * Created by Christopher Harris (Itszuvalex) on 9/3/15.
 */
trait GuiElement {
  var anchorX: Int

  var anchorY: Int

  var moused = false

  def spaceHorizontal: Int = 0

  def spaceVertical: Int = 0

  def isMousedOver = moused

  def isLocationInside(mouseX: Int, mouseY: Int): Boolean

  def onMouseEnter() = moused = true

  def onMouseLeave() = moused = false

  /**
   *
   * @param mouseX Local mouseX coordinates
   * @param mouseY Local mouseY coordinates
   * @param button
   * @return True if click is handled
   */
  def onMouseClick(mouseX: Int, mouseY: Int, button: Int) = false

  def addTooltip(mouseX: Int, mouseY: Int, tooltip: ListBuffer[String]) = {}

  def render(screenX: Int, screenY: Int, mouseX: Int, mouseY: Int, partialTicks: Float) = {
    (isMousedOver, isLocationInside(mouseX, mouseY)) match {
      case (true, false) => onMouseLeave()
      case (false, true) => onMouseEnter()
      case _ =>
    }
  }

  def update() = {}


}
