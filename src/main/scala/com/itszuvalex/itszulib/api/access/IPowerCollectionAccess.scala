package com.itszuvalex.itszulib.api.access

import java.util.concurrent.atomic.AtomicInteger

import net.minecraft.entity.player.EntityPlayer

/**
  * Created by Christopher Harris (Itszuvalex) on 3/12/2016.
  */
trait IPowerCollectionAccess extends scala.collection.mutable.Seq[IPowerAccess] {
  private val revision = new AtomicInteger(0)

  def canPlayerAccess(player: EntityPlayer): Boolean

  /**
    * Called when an item in the inventory changes.
    *
    * @param index Index of changed item, or -1 if unknown/multiple.
    */
  def onInventoryChanged(index: Int): Unit = {}

  def copyFromAccess(access: IPowerCollectionAccess) = {
    if (length == access.length) {
      (this zip access).foreach(pair => pair._1.copyFromAccess(pair._2))
      revision.incrementAndGet()
    }
  }

  def getRevision: Int = revision.intValue()

  override def iterator: Iterator[IPowerAccess] = new DefaultPowerCollectionAccessIterator(this)
}
