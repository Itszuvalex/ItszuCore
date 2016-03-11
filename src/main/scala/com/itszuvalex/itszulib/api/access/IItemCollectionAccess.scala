package com.itszuvalex.itszulib.api.access

import java.util.concurrent.atomic.AtomicInteger

import net.minecraft.entity.player.EntityPlayer

/**
  * Created by Christopher Harris (Itszuvalex) on 3/10/16.
  */

trait IItemCollectionAccess extends scala.collection.immutable.Seq[IItemAccess] {
  private val revision = new AtomicInteger(0)

  def hasEmptySpace: Boolean = contains(null)

  def canPlayerAccess(player: EntityPlayer): Boolean

  /**
    * Called when an item in the inventory changes.
    *
    * @param index Index of changed item, or -1 if unknown/multiple.
    */
  def onInventoryChanged(index: Int): Unit = {}

  def copyFromAccess(access: IItemCollectionAccess, copy: Boolean = true) = {
    if (length == access.length) {
      (this zip access).foreach(pair => pair._1.copyFromAccess(pair._2, copy))
      revision.incrementAndGet()
    }
  }

  def getRevision: Int = revision.intValue()

}

