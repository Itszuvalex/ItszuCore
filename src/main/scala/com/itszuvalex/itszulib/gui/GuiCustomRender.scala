package com.itszuvalex.itszulib.gui

import com.itszuvalex.itszulib.render.vbo.util.CustomRenderingUtils
import org.lwjgl.opengl.{Display, GL11}
import org.lwjgl.util.vector.Matrix4f

/**
  * Created by Alex on 21.02.2016.
  */
abstract class GuiCustomRender(override var anchorX: Int,
                               override var anchorY: Int,
                               override var _panelWidth: Int,
                               override var _panelHeight: Int) extends GuiPanel {

  var projMatrix: Matrix4f = CustomRenderingUtils.perspectiveMatrix(math.toRadians(60).toFloat, panelWidth.toFloat / panelHeight.toFloat, 0.1f, 100f)
  var viewMatrix: Matrix4f = new Matrix4f()
  var modelMatrix: Matrix4f = new Matrix4f()

  init()

  override def render(screenX: Int, screenY: Int, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    super.render(screenX, screenY, mouseX, mouseY, partialTicks)

    GL11.glViewport(screenX * 2, screenY * 2, _panelWidth * 2, _panelHeight * 2)

    logicUpdate(screenX, screenY, mouseX, mouseY, partialTicks)

    graphicsUpdate(screenX, screenY, mouseX, mouseY, partialTicks)

    GL11.glViewport(0, 0, Display.getWidth, Display.getHeight)

  }

  def init(): Unit

  def logicUpdate(screenX: Int, screenY: Int, mouseX: Int, mouseY: Int, partialTicks: Float): Unit

  def graphicsUpdate(screenX: Int, screenY: Int, mouseX: Int, mouseY: Int, partialTicks: Float): Unit

}
