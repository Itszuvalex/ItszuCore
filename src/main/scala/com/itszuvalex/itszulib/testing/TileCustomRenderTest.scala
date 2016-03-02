package com.itszuvalex.itszulib.testing

import com.itszuvalex.itszulib.ItszuLib
import com.itszuvalex.itszulib.core.TileEntityBase
import net.minecraft.entity.player.EntityPlayer

/**
  * Created by Alex on 20.02.2016.
  */
class TileCustomRenderTest extends TileEntityBase {

  override def getMod: AnyRef = ItszuLib

  override def hasDescription: Boolean = true

  override def onSideActivate(player: EntityPlayer, side: Int): Boolean = {
    player.openGui(getMod, 1, worldObj, xCoord, yCoord, zCoord)
    true
  }

}
