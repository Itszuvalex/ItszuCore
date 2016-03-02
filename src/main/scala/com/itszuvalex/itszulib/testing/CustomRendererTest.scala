package com.itszuvalex.itszulib.testing

import com.itszuvalex.itszulib.render.vbo.obj.{Group, VBOObjLoader}
import com.itszuvalex.itszulib.render.vbo.util.CustomRenderingUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import org.lwjgl.BufferUtils

/**
  * Created by Alex on 21.02.2016.
  */
class CustomRendererTest extends TileEntitySpecialRenderer {

  val model = VBOObjLoader.load(new ResourceLocation("itszulib", "models/Frame.obj"))

  val vertices = Array[Float](
    0, 0, 0, 0, 1, 0, // 1/1(6) 0
    0, 0, -1, 0, 1, 1, // 2/2(7) 1
    1, 0, -1, 0, 0, 1, // 3/3(8) 2
    1, 0, 0, 0, 0, 0, // 4/4(5) 3
    0, 1, 0, 0, 0, 0, // 5/5(4) 4
    1, 1, 0, 0, 1, 0, // 6/6(1) 5
    1, 1, -1, 0, 1, 1, // 7/7(2) 6
    0, 1, -1, 0, 0, 1, // 8/8(3) 7
    0, 0, 0, 0, 0, 0, // 1/4(5) 8
    1, 0, 0, 0, 1, 0, // 4/1(6) 9
    1, 1, 0, 0, 1, 1, // 6/2(7) 10
    0, 1, 0, 0, 0, 1, // 5/3(8) 11
    1, 0, -1, 0, 1, 0, // 3/6(1) 12
    1, 1, 0, 0, 0, 1, // 6/8(3) 13
    1, 0, -1, 0, 0, 0, // 3/5(4) 14
    0, 0, -1, 0, 1, 0, // 2/6(1) 15
    0, 1, -1, 0, 1, 1, // 8/7(2) 16
    1, 1, -1, 0, 0, 1, // 7/8(3) 17
    0, 0, -1, 0, 0, 0, // 2/4(5) 18
    0, 1, 0, 0, 1, 1 // 5/2(7) 19
  )
  val vertBuf = BufferUtils.createFloatBuffer(vertices.length).put(vertices)
  vertBuf.flip()
  val indices = Array[Short](
    0, 1, 2,
    2, 3, 0,
    4, 5, 6,
    6, 7, 4,
    8, 9, 10,
    10, 11, 8,
    3, 12, 6,
    6, 13, 3,
    14, 15, 16,
    16, 17, 14,
    18, 0, 19,
    19, 7, 18
  )
  val indAmt = indices.length
  val indBuf = BufferUtils.createShortBuffer(indAmt).put(indices)
  indBuf.flip()

  val tgp = new Group("test", false, true)
  tgp.setDataBuffer(vertBuf)
  tgp.setIndexBuffer(indBuf, indAmt)

  val pId = CustomRenderingUtils.setupWorldRenderShaders()

  var vaoCompiled = false

  override def renderTileEntityAt(tile : TileEntity, x : Double, y : Double, z : Double, partialTicks : Float): Unit = {
    if (!vaoCompiled) {
      tgp.compileVAO()
      vaoCompiled = true
    }

    //GL20.glUseProgram(pId)

    //GL11.glTranslated(x, y, z)

    Minecraft.getMinecraft.getTextureManager.bindTexture(new ResourceLocation("itszulib", "models/frame.png"))

    //GL11.glPushMatrix()

    tgp.render()

    //GL11.glPopMatrix()

    //GL20.glUseProgram(0)
  }

}
