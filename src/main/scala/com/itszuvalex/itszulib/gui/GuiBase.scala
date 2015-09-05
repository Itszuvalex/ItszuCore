package com.itszuvalex.itszulib.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container

/**
 * Created by Christopher Harris (Itszuvalex) on 10/19/14.
 */
abstract class GuiBase(c: Container) extends GuiContainer(c) with GuiPanel {
  def isPointInRegion(x: Int, y: Int, width: Int, height: Int, mouseX: Int, mouseY: Int) = func_146978_c(x, y, width, height, mouseX, mouseY)

  override def anchorX = guiLeft

  override def anchorX_=(_x: Int) = { guiLeft = _x }

  override def anchorY = guiTop

  override def anchorY_=(_y: Int) = { guiTop = _y }

  override def panelWidth = xSize

  override def panelWidth_=(_width: Int) = { xSize = width }

  override def panelHeight = ySize

  override def panelHeight_=(_height: Int) = { ySize = width }

  override def drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    super.drawScreen(mouseX, mouseY, partialTicks)
    subElements.foreach(gui => gui.render(anchorX + gui.anchorX,
                                          anchorY + gui.anchorY,
                                          mouseX - gui.anchorX - anchorX,
                                          mouseY - gui.anchorY - anchorY,
                                          partialTicks))
  }

  override def mouseClicked(mouseX: Int, mouseY: Int, button: Int): Unit = {
    if (!subElements.exists(gui => gui.onMouseClick(mouseX - gui.anchorX - anchorX,
                                                    mouseY - gui.anchorY - anchorY,
                                                    button)))
      super.mouseClicked(mouseX, mouseY, button)
  }

  override def updateScreen(): Unit = {
    super.updateScreen()
    subElements.foreach(_.update())
  }
}
