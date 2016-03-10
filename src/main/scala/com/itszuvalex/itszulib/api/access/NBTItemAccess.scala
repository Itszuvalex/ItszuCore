package com.itszuvalex.itszulib.api.access

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

import scala.collection.JavaConversions._

/**
  * Created by Christopher Harris (Itszuvalex) on 3/10/16.
  */
class NBTItemAccess(nbt: NBTTagCompound, index: Int) extends IItemAccess {
  /**
    * Don't use unless absolutely necessary
    *
    * @return Backing ItemStack
    */
  override def getItemStack: Option[ItemStack] = getItemCompound(false) match {
    case None => None
    case Some(comp) => Some(ItemStack.loadItemStackFromNBT(comp))
  }

  /**
    * Sets this item access's storage to the ItemStack.
    *
    * @param stack ItemStack to set this to.
    */
  override def setItemStack(stack: ItemStack): Unit = {
    (getItemStack, getItemCompound(force = getItemStack.isDefined)) match {
      case (None, None) =>
      case (None, Some(_)) => nbt.removeTag(index.toString)
      case (Some(i), Some(comp)) =>
        comp.func_150296_c().collect { case s: String => s }.foreach(comp.removeTag)
        i.writeToNBT(comp)
    }
  }

  /**
    *
    * @param amount Amount to increase ItemStack stacksize by.  Must be >= 0
    * @return Math.min(amount, MaxStorage - CurrentStorage) -> Amount of amount added to the ItemStack.
    */
  override def increment(amount: Int): Int = getItemStack.map { i =>
    if (amount < 0) return 0

    val room = maxStorage.get - currentStorage.get
    val inc = Math.min(amount, room)
    val item = getItemStack.get
    item.stackSize += inc
    setItemStack(item)
    inc
                                                              }.getOrElse(0)

  /**
    *
    * @param amount Amount to decrease ItemStack stacksize by.  Must be <= 0
    * @return Math.min(amount, currentStorage) -> Amount of amount removed from the ItemStack.  Clears ItemStack if amount == CurrentStorage
    */
  override def decrement(amount: Int): Int = getItemStack.map { i =>
    if (amount < 0) return 0

    val dec = Math.min(amount, currentStorage.get)
    if (dec == currentStorage.get) {
      clear()
    }
    else {
      val item = getItemStack.get
      item.stackSize -= dec
      setItemStack(item)
    }
    dec
                                                              }.getOrElse(0)

  def getItemCompound(force: Boolean = false): Option[NBTTagCompound] = {
    val exists = nbt.hasKey(index.toString)
    if (exists || force) {
      if (force && !exists) {
        nbt.setTag(index.toString, new NBTTagCompound)
      }
      Some(nbt.getCompoundTag(index.toString))
    }
    else None
  }
}
