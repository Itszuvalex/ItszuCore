package com.itszuvalex.itszulib.gui

import com.itszuvalex.itszulib.render.RenderUtils
import com.itszuvalex.itszulib.util.Color
import net.minecraft.client.gui.Gui
import net.minecraftforge.fluids.FluidTank

import scala.collection.mutable.ListBuffer

/**
 * Created by Alex on 04.10.2015.
 */
object GuiFluidTank {
  val DEFAULT_RAISED_COLOR     = Color(255.toByte, 64, 64, 64).toInt
  val DEFAULT_LOWERED_COLOR    = Color(255.toByte, 15, 15, 15).toInt
  val DEFAULT_BACKGROUND_COLOR = Color(255.toByte, 40, 40, 40).toInt
  val DEFAULT_FLUID_BACK_COLOR = Color(255.toByte, 255.toByte, 255.toByte, 255.toByte).toInt
  val DEFAULT_SCALE_COLOR      = Color(255.toByte, 200.toByte, 0, 0).toInt
}

class GuiFluidTank(override var anchorX: Int,
                   override var anchorY: Int,
                   var gui: GuiBase,
                   var tank: FluidTank,
                   var drawTank: Boolean = true) extends GuiPanel{

  override var panelWidth: Int = 18
  override var panelHeight: Int = 66

  var colorRaised = GuiFluidTank.DEFAULT_RAISED_COLOR
  var colorLowered = GuiFluidTank.DEFAULT_LOWERED_COLOR
  var colorBackground = GuiFluidTank.DEFAULT_BACKGROUND_COLOR
  var colorFluidBack = GuiFluidTank.DEFAULT_FLUID_BACK_COLOR
  var colorScale = GuiFluidTank.DEFAULT_SCALE_COLOR

  override def addTooltip(mouseX: Int, mouseY: Int, tooltip: ListBuffer[String]): Unit = {
    super.addTooltip(mouseX, mouseY, tooltip)
    if (tank.getFluid == null) tooltip += "Empty" else tooltip += (tank.getFluid.getFluid.getLocalizedName(tank.getFluid) + ", " + tank.getFluidAmount + "mb")
  }

  override def render(screenX: Int, screenY: Int, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    super.render(screenX, screenY, mouseX, mouseY, partialTicks)

    if (drawTank) {
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
      drawFluid(screenX, screenY)
      drawScale(screenX, screenY)
    }
  }

  def drawScale(screenX: Int, screenY: Int): Unit = {
    //Full
    Gui.drawRect(screenX + 1, screenY + 1, screenX + 13, screenY + 2, colorScale)
    //Half
    Gui.drawRect(screenX + 1, screenY + 33, screenX + 13, screenY + 34, colorScale)
    //1 Quarter
    Gui.drawRect(screenX + 1, screenY + 49, screenX + 9, screenY + 50, colorScale)
    //3 Quarters
    Gui.drawRect(screenX + 1, screenY + 17, screenX + 9, screenY + 18, colorScale)
    //1 Eighth
    Gui.drawRect(screenX + 1, screenY + 57, screenX + 5, screenY + 58, colorScale)
    //3 Eighths
    Gui.drawRect(screenX + 1, screenY + 41, screenX + 5, screenY + 42, colorScale)
    //5 Eighths
    Gui.drawRect(screenX + 1, screenY + 25, screenX + 5, screenY + 26, colorScale)
    //7 Eighths
    Gui.drawRect(screenX + 1, screenY + 9, screenX + 5, screenY + 10, colorScale)
  }

  def drawFluid(screenX: Int, screenY: Int): Unit = {
    if (tank.getFluid == null) return
    if (tank.getFluid.amount == 0) return
    val icon = tank.getFluid.getFluid.getStillIcon
    val height: Int = math.floor((tank.getFluid.amount / tank.getCapacity.toDouble) * 64).toInt
    val topPx = screenY + 65 - height

    Gui.drawRect(screenX + 1, topPx, screenX + 17, screenY + 65, colorFluidBack)

    RenderUtils.renderLiquidInGUI(gui, 0, icon, screenX + 1, topPx, 16, height)
  }

}

