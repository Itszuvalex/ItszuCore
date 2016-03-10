package com.itszuvalex.itszulib.api.access

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
  * Created by Christopher Harris (Itszuvalex) on 3/10/16.
  */
class ArrayItemCollectionAccess(val array: Array[ItemStack]) extends IItemCollectionAccess {

  override def canPlayerAccess(player: EntityPlayer): Boolean = true

  override def length: Int = array.length

  override def apply(idx: Int): IItemAccess = new ArrayItemAccess(array, idx)

  override def iterator: Iterator[IItemAccess] = new DefaultItemCollectionAccessIterator(this)
}
