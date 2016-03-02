package com.itszuvalex.itszulib.testing

import com.itszuvalex.itszulib.gui.{GuiBase, GuiCustomRender}
import com.itszuvalex.itszulib.render.vbo.obj.{VBOObjLoader, WavefrontObject}
import net.minecraft.client.gui.Gui
import net.minecraft.entity.player.{EntityPlayer, InventoryPlayer}
import net.minecraft.util.ResourceLocation

/**
  * Created by Alex on 21.02.2016.
  */
class GuiCustomRenderTest(player: EntityPlayer, inv: InventoryPlayer, private val tile: TileCustomRenderTest) extends GuiBase(new ContainerCustomRenderTest(player, inv, tile)) {

  val guiRender = new GuiCustomRender(anchorX + 20, anchorY + 20, xSize - 40, ySize - 40) {
    var model: WavefrontObject = null

    override def init(): Unit = {
      model = VBOObjLoader.load(new ResourceLocation("itszulib", "models/Frame.obj"))
    }

    override def graphicsUpdate(screenX: Int, screenY: Int, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
      model.renderAll()
    }

    override def logicUpdate(screenX: Int, screenY: Int, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
      if (model == null) init()
    }
  }

  guiRender.setShouldRender(false)

  add(guiRender)

  override def drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int): Unit = {
    Gui.drawRect(anchorX, anchorY, anchorX + xSize, anchorY + ySize, 0xffcccccc)

    guiRender.render(anchorX + guiRender.anchorX, anchorY + guiRender.anchorY, mouseX - anchorX - guiRender.anchorX, mouseY - anchorY - guiRender.anchorY, partialTicks)
  }

}
