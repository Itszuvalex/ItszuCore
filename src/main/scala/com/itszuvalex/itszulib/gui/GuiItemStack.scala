package com.itszuvalex.itszulib.gui

import com.itszuvalex.itszulib.util.Color
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.{FontRenderer, Gui}
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.GL11

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
 * Created by Christopher Harris (Itszuvalex) on 9/4/15.
 */
object GuiItemStack {
  val DEFAULT_RAISED_COLOR     = Color(255.toByte, 64, 64, 64).toInt
  val DEFAULT_LOWERED_COLOR    = Color(255.toByte, 15, 15, 15).toInt
  val DEFAULT_BACKGROUND_COLOR = Color(255.toByte, 40, 40, 40).toInt
  val DEFAULT_FONT_COLOR       = Color(255.toByte, 255.toByte, 255.toByte, 255.toByte).toInt
}

class GuiItemStack(override var anchorX: Int,
                   override var anchorY: Int,
                   var itemStack: ItemStack = null) extends GuiPanel {

  override var panelHeight: Int = 16
  override var panelWidth : Int = 16

  var colorRaised     = GuiItemStack.DEFAULT_RAISED_COLOR
  var colorLowered    = GuiItemStack.DEFAULT_LOWERED_COLOR
  var colorBackground = GuiItemStack.DEFAULT_BACKGROUND_COLOR
  var colorFont       = GuiItemStack.DEFAULT_FONT_COLOR
  var itemRenderer    = new RenderItem()
  var fontRenderer    = Minecraft.getMinecraft.fontRenderer

  override def addTooltip(mouseX: Int, mouseY: Int, tooltip: ListBuffer[String]): Unit = {
    super.addTooltip(mouseX, mouseY, tooltip)
    if (itemStack != null) tooltip ++= itemStack.getTooltip(Minecraft.getMinecraft.thePlayer, Minecraft.getMinecraft.gameSettings.advancedItemTooltips).asInstanceOf[java.util.List[String]]
  }

  override def render(screenX: Int, screenY: Int, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    super.render(screenX, screenY, mouseX, mouseY, partialTicks)

    //Top lowered rect
    Gui.drawRect(screenX, screenY, screenX + panelWidth, screenY + 1, colorLowered)
    //Left lowered rect
    Gui.drawRect(screenX, screenY + 1, screenX + 1, screenY + panelHeight - 1, colorLowered)
    //Bottom raised rect
    Gui.drawRect(screenX, screenY + panelHeight - 1, screenX + panelWidth, screenY + panelHeight, colorRaised)
    //Right raised rect
    Gui.drawRect(screenX + panelWidth - 1, screenY + 1, screenX + panelWidth, screenY + panelHeight - 1, colorRaised)
    //Main rect
    Gui.drawRect(screenX + 1, screenY + 1, screenX + panelWidth - 1, screenY + panelHeight - 1, colorBackground)

    drawItemStack(if (itemStack == null) "" else itemStack.stackSize.toString)
  }

  def drawItemStack(amt: String) {
    if (itemStack == null) return

    GL11.glPushMatrix()
    GL11.glTranslatef(0.0F, 0.0F, 32.0F)
    GL11.glColor3f(1,1,1)
    //      this.zLevel = 200.0F
    itemRenderer.zLevel = 200.0F
    var font: FontRenderer = null
    if (itemStack != null) font = itemStack.getItem.getFontRenderer(itemStack)
    if (font == null) font = fontRenderer
    itemRenderer.renderItemAndEffectIntoGUI(font, Minecraft.getMinecraft.getTextureManager, itemStack, anchorX + 1, anchorY + 1)
    itemRenderer.renderItemOverlayIntoGUI(font, Minecraft.getMinecraft.getTextureManager, itemStack, anchorX + 1, anchorY + 1, amt)
    itemRenderer.zLevel = 0.0F
    GL11.glPopMatrix()
  }
}
