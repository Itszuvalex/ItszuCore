package com.itszuvalex.itszulib.gui

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * Created by Christopher Harris (Itszuvalex) on 9/3/15.
  */

trait GuiPanel extends GuiElement {
  val subElements = ArrayBuffer[GuiElement]()
  var panelWidth: Int
  var panelHeight: Int

  override def spaceHorizontal = panelWidth

  override def spaceVertical = panelHeight

  def add(elements: GuiElement*) = {
    subElements ++= elements.filter(gui => gui.setParent(this))
    this
  }

  def remove(elements: GuiElement*) = {
    subElements --= elements.filter(gui => gui.setParent(null))
    this
  }

  override def onMouseClick(mouseX: Int, mouseY: Int, button: Int): Boolean = {
    passAlongMouseClick(mouseX, mouseY, button)
  }

  def passAlongMouseClick(mouseX: Int, mouseY: Int, button: Int): Boolean = {
    subElements.exists(gui => gui.onMouseClick(mouseX - gui.anchorX, mouseY - gui.anchorY, button))
  }

  override def addTooltip(mouseX: Int, mouseY: Int, tooltip: ListBuffer[String]): Unit = {
    super.addTooltip(mouseX, mouseY, tooltip)
    addSubElementTooltips(mouseX, mouseY, tooltip)
  }

  def addSubElementTooltips(mouseX: Int, mouseY: Int, tooltip: ListBuffer[String]): Unit = {
    subElements.foreach(gui => gui.addTooltip(mouseX - gui.anchorX, mouseY - gui.anchorY, tooltip))
  }

  override def isLocationInside(mouseX: Int, mouseY: Int): Boolean = {
    ((mouseX >= 0) && (mouseX < panelWidth)) &&
    ((mouseY >= 0) && (mouseY < panelHeight))
  }

  override def update(): Unit = subElements.foreach(_.update())

  override def renderUpdate(screenX: Int, screenY: Int, mouseX: Int, mouseY: Int, partialTicks: Float) = {
    super.renderUpdate(screenX, screenY, mouseX, mouseY, partialTicks)
    subElements.foreach(gui => gui.renderUpdate(screenX + gui.anchorX,
                                                screenY + gui.anchorY,
                                                mouseX - gui.anchorX,
                                                mouseY - gui.anchorY,
                                                partialTicks))
  }
}
