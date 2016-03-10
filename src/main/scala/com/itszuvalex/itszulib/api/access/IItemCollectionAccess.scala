package com.itszuvalex.itszulib.api.access

import net.minecraft.entity.player.EntityPlayer

/**
  * Created by Christopher Harris (Itszuvalex) on 3/10/16.
  */

trait IItemCollectionAccess extends scala.collection.immutable.Seq[IItemAccess] {

  def hasEmptySpace: Boolean = contains(null)

  def canPlayerAccess(player: EntityPlayer): Boolean


}

