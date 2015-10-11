package com.itszuvalex.itszulib.gui

import com.itszuvalex.itszulib.core.FluidSlotContainer
import com.itszuvalex.itszulib.network.PacketHandler
import com.itszuvalex.itszulib.network.messages.MessageFluidSlotClick
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container

import scala.collection.JavaConversions._

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

  override def panelWidth_=(_width: Int) = { xSize = _width }

  override def panelHeight = ySize

  override def panelHeight_=(_height: Int) = { ySize = _height }

  override def mouseClicked(mouseX: Int, mouseY: Int, button: Int): Unit = {
    val flSlot = getFluidSlotAtPosition(mouseX, mouseY)
    if (flSlot != null) {
      PacketHandler.INSTANCE.sendToServer(new MessageFluidSlotClick(flSlot.slotNumber, button))
    }
    if (!subElements.exists(gui => gui.onMouseClick(mouseX - gui.anchorX - anchorX,
                                                    mouseY - gui.anchorY - anchorY,
                                                    button)))
      super.mouseClicked(mouseX, mouseY, button)
  }

  override def updateScreen(): Unit = {
    super.updateScreen()
    subElements.foreach(_.update())
  }

  override def drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    super.drawScreen(mouseX, mouseY, partialTicks)
    val slot = getFluidSlotAtPosition(mouseX, mouseY)
    if (slot != null) {
      drawHoveringText(slot.getTooltip, mouseX, mouseY, fontRendererObj)
    }
    renderUpdate(anchorX, anchorY, mouseX - anchorX, mouseY - anchorY, partialTicks)
  }

  def getFluidSlotAtPosition(x: Int, y: Int): FluidSlot = {
    if (!inventorySlots.isInstanceOf[FluidSlotContainer]) return null
    val cont = inventorySlots.asInstanceOf[FluidSlotContainer]
    cont.fluidSlots.foreach { slot => if (isMouseOverFluidSlot(slot, x, y)) return slot }
    null
  }

  def isMouseOverFluidSlot(slot: FluidSlot, x: Int, y: Int): Boolean =
    func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 64, x, y)
}
