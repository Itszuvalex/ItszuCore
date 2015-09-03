package com.itszuvalex.itszulib.gui

/**
 * Created by Christopher Harris (Itszuvalex) on 9/3/15.
 */
trait GuiElement {
  var x: Int
  var y: Int

  def isMousedOver : Boolean

  def isLocationInside(mouseX: Int, mouseY: Int): Boolean

  def onMouseEnter()

  def onMouseLeave()

  def onMouseClick(mouseX: Int, mouseY: Int, button: Int)

  def addTooltip(mouseX: Int, mouseY: Int, tooltip: java.util.List[String])

  def render(screenX: Int, screenY: Int)

  def update()

}
