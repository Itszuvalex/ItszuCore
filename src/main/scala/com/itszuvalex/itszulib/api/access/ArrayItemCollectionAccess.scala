package com.itszuvalex.itszulib.api.access

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
  * Created by Christopher Harris (Itszuvalex) on 3/10/16.
  */
class ArrayItemCollectionAccess(private[access] val array: Array[ItemStack], private[access] var caching: Boolean = true) extends IItemCollectionAccess {
  private[access] lazy val cache = new Array[IItemAccess](array.length)

  override def canPlayerAccess(player: EntityPlayer): Boolean = true

  override def length: Int = array.length

  override def apply(idx: Int): IItemAccess = if (caching) {
    if (cache(idx) == null) {
      cache(idx) = new ArrayItemAccess(this, idx)
    }
    cache(idx)
  } else new ArrayItemAccess(this, idx)

  def isCaching = caching

  def setCaching(c: Boolean) = caching = c
}
