package com.itszuvalex.itszulib.testing

import com.itszuvalex.itszulib.container.ContainerInv
import net.minecraft.entity.player.{EntityPlayer, InventoryPlayer}
import net.minecraft.item.ItemStack

/**
  * Created by Alex on 21.02.2016.
  */
class ContainerCustomRenderTest(player: EntityPlayer, inv: InventoryPlayer, tile: TileCustomRenderTest) extends ContainerInv[TileCustomRenderTest](player, tile, 0, 0) {

  addPlayerInventorySlots(inv)

  override def eligibleForInput(item: ItemStack): Boolean = false
}
