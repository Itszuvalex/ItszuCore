package com.itszuvalex.itszulib.testing

import com.itszuvalex.itszulib.core.TileContainer
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
  * Created by Alex on 20.02.2016.
  */
class BlockCustomRenderTest extends TileContainer(Material.iron) {

  override def createNewTileEntity(p_149915_1_ : World, p_149915_2_ : Int): TileEntity = new TileCustomRenderTest

}
