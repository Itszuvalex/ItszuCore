package com.itszuvalex.itszucore.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{IInventory, Slot}
import net.minecraft.item.ItemStack


class OutputSlot(par1iInventory: IInventory, par2: Int, par3: Int, par4: Int) extends Slot(par1iInventory, par2, par3, par4) {
  override def isItemValid(par1ItemStack: ItemStack) = false

  override def canTakeStack(par1EntityPlayer: EntityPlayer) = true
}