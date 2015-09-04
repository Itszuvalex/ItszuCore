package com.itszuvalex.itszulib.gui

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christopher Harris (Itszuvalex) on 9/3/15.
 */

trait GuiPanel extends GuiElement {
  var panelWidth: Int

  var panelHeight: Int

  val subElements = ArrayBuffer[GuiElement]()

  def add(elements: GuiElement*) = {
    subElements ++= elements
    this
  }

  override def onMouseClick(mouseX: Int, mouseY: Int, button: Int): Boolean = {
    subElements.exists(gui => gui.onMouseClick(mouseX - gui.anchorX, mouseY - gui.anchorY, button))
  }

  override def addTooltip(mouseX: Int, mouseY: Int, tooltip: List[String]): Unit = {
    subElements.foreach(gui => gui.addTooltip(mouseX - gui.anchorX, mouseY - gui.anchorY, tooltip))
  }

  override def isLocationInside(mouseX: Int, mouseY: Int): Boolean = {
    ((mouseX >= anchorX) && (mouseX < anchorX + panelWidth)) &&
    ((mouseY >= anchorY) && (mouseY < anchorY + panelHeight))
  }

  override def update(): Unit = subElements.foreach(_.update())

  override def render(screenX: Int, screenY: Int, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    subElements.foreach(gui => gui.render(screenX + gui.anchorX,
                                          screenY + gui.anchorY,
                                          mouseX - gui.anchorX,
                                          mouseY - gui.anchorY,
                                          partialTicks))
  }
}
