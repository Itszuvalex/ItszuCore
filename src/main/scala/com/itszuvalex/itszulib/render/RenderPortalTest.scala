package com.itszuvalex.itszulib.render

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.{RenderBlocks, Tessellator}
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{IIcon, ResourceLocation}
import org.lwjgl.opengl.GL11

/**
 * Created by Christopher on 4/11/2015.
 */
object RenderPortalTest {
  private val skyLocation    : ResourceLocation = new ResourceLocation("textures/environment/end_sky.png")
}

class RenderPortalTest extends TileEntitySpecialRenderer{
  override def renderTileEntityAt(p_147500_1_ : TileEntity, x : Double, y : Double, z : Double, p_147500_8_ : Float): Unit = {
    this.bindTexture(RenderPortalTest.skyLocation)
    ShaderUtils.bindShader(ShaderUtils.portal)
    val tes = Tessellator.instance
    val xmin = 0
    val xmax = 1
    val ymin = 0
    val ymax = 1
    val zmin = 0
    val zmax = 1
    tes.addTranslation(x.toFloat, y.toFloat, z.toFloat)
    tes.startDrawingQuads()
    tes.setColorRGBA_F(.1f, .1f, .1f, 1.0F)
    tes.addVertex(xmin, ymax, zmin)
    tes.addVertex(xmin, ymax, zmax)
    tes.addVertex(xmax, ymax, zmax)
    tes.addVertex(xmax, ymax, zmin)

    tes.addVertex(xmin, ymin, zmin)
    tes.addVertex(xmax, ymin, zmin)
    tes.addVertex(xmax, ymin, zmax)
    tes.addVertex(xmin, ymin, zmax)

    tes.addVertex(xmin, ymin, zmin)
    tes.addVertex(xmin, ymax, zmin)
    tes.addVertex(xmax, ymax, zmin)
    tes.addVertex(xmax, ymin, zmin)

    tes.addVertex(xmax, ymin, zmin)
    tes.addVertex(xmax, ymax, zmin)
    tes.addVertex(xmax, ymax, zmax)
    tes.addVertex(xmax, ymin, zmax)

    tes.addVertex(xmin, ymin, zmax)
    tes.addVertex(xmax, ymin, zmax)
    tes.addVertex(xmax, ymax, zmax)
    tes.addVertex(xmin, ymax, zmax)

    tes.addVertex(xmin, ymin, zmin)
    tes.addVertex(xmin, ymin, zmax)
    tes.addVertex(xmin, ymax, zmax)
    tes.addVertex(xmin, ymax, zmin)
    tes.addTranslation(-x.toFloat, -y.toFloat, -z.toFloat)
    tes.draw()
    ShaderUtils.releaseShader()
  }
}
