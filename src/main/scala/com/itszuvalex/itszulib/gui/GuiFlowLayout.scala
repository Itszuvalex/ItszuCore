package com.itszuvalex.itszulib.gui

/**
 * Created by Christopher Harris (Itszuvalex) on 9/4/15.
 */
object GuiFlowLayout {

  object FlowDirection extends Enumeration {
    type FlowDirection = Value
    val Vertical, Horizontal = Value
  }

  /**
   * Reserved for later refinement.
   */
  object FlowVertical extends Enumeration {
    type FlowVertical = Value
    val Top, Center, Bottom = Value
  }

  /**
   * Reserved for later refinement.
   *
   */
  object FlowHorizontal extends Enumeration {
    type FlowHorizontal = Value
    val Left, Center, Right = Value
  }

}

class GuiFlowLayout(override var anchorX: Int,
                    override var anchorY: Int,
                    override var panelWidth: Int,
                    override var panelHeight: Int,
                    elements: GuiElement*) extends GuiPanel {
  add(elements: _*)

  var flowVertical     = GuiFlowLayout.FlowVertical.Top
  var flowHorizontal   = GuiFlowLayout.FlowHorizontal.Left
  var primaryFlow      = GuiFlowLayout.FlowDirection.Horizontal
  var bufferVertical   = 0
  var bufferHorizontal = 0

  override def render(screenX: Int, screenY: Int, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    layoutElements()
    //    GL11.glScissor(screenX, screenY, panelWidth, panelHeight)
    //    GL11.glEnable(GL11.GL_SCISSOR_TEST)
    super.render(screenX, screenY, mouseX, mouseY, partialTicks)
    //    GL11.glDisable(GL11.GL_SCISSOR_TEST)
  }

  def layoutElements(): Unit = {
    primaryFlow match {
      case GuiFlowLayout.FlowDirection.Horizontal =>
        var nextX = bufferHorizontal
        var nextY = bufferVertical
        var rowHeight = 0
        elements.foreach { e =>
          (nextX, nextY) match {
            case (_, y) if y >= panelHeight =>
              nextX = panelWidth
              nextY = panelHeight
              e.shouldRender = false
            case (x, _) if !(x == bufferHorizontal) && (x + e.spaceHorizontal + bufferHorizontal) >= panelWidth =>
              nextX = bufferHorizontal
              nextY += rowHeight + bufferVertical
              rowHeight = 0
              e.shouldRender = true
            case _ =>
          }
          e.anchorX = nextX
          e.anchorY = nextY
          nextX = nextX + e.spaceHorizontal + bufferHorizontal
          rowHeight = Math.max(rowHeight, e.spaceVertical)
                         }

      case GuiFlowLayout.FlowDirection.Vertical =>
        var nextX = bufferHorizontal
        var nextY = bufferVertical
        var colWidth = 0
        elements.foreach { e =>
          (nextX, nextY) match {
            case (x, _) if x >= panelWidth =>
              nextX = panelWidth
              nextY = panelHeight
              e.shouldRender = false
            case (_, y) if !(y == bufferVertical) && (y + e.spaceVertical + bufferVertical) >= panelHeight =>
              nextX += colWidth + bufferHorizontal
              nextY = bufferVertical
              colWidth = 0
              e.shouldRender = true
            case _ =>
          }
          e.anchorX = nextX
          e.anchorY = nextY
          nextY = nextY + e.spaceHorizontal + bufferVertical
          colWidth = Math.max(colWidth, e.spaceHorizontal)
                         }
      case _ =>
    }
  }

}
