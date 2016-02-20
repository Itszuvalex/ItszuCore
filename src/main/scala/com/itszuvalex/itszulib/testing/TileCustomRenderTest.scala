package com.itszuvalex.itszulib.testing

import com.itszuvalex.itszulib.ItszuLib
import com.itszuvalex.itszulib.core.TileEntityBase
import com.itszuvalex.itszulib.render.vbo.VBOObjLoader
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation

/**
  * Created by Alex on 20.02.2016.
  */
class TileCustomRenderTest extends TileEntityBase {

  override def getMod: AnyRef = ItszuLib

  override def hasDescription: Boolean = true

  override def onSideActivate(player: EntityPlayer, side: Int): Boolean = {
    if (worldObj.isRemote) VBOObjLoader.load(new ResourceLocation("itszulib", "models/Frame.obj"))
    true
  }

}
