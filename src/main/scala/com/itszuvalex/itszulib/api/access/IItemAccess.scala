package com.itszuvalex.itszulib.api.access

import net.minecraft.item.ItemStack

/**
  * Created by Christopher Harris (Itszuvalex) on 3/10/16.
  */
trait IItemAccess {
  /**
    * Don't use unless absolutely necessary
    *
    * @return Backing ItemStack
    */
  def getItemStack: Option[ItemStack]

  /**
    * Sets this item access's storage to the ItemStack.
    *
    * @param stack ItemStack to set this to.
    */
  def setItemStack(stack: ItemStack): Unit

  /**
    *
    * @return Usually ItemStack.MaxStackSize, but could be different for different storages.
    */
  def maxStorage: Option[Int] = getItemStack.map(_.stackSize)

  /**
    *
    * @return Usually ItemStack.StackSize, but could be different for different storages.
    */
  def currentStorage: Option[Int] = getItemStack.map(_.getMaxStackSize)

  /**
    *
    * @param amount Amount to increase ItemStack stacksize by.  Must be >= 0
    * @return Math.min(amount, MaxStorage - CurrentStorage) -> Amount of amount added to the ItemStack.
    */
  def increment(amount: Int): Int = getItemStack.map { i =>
    if (amount < 0) return 0

    val room = maxStorage.get - currentStorage.get
    val inc = Math.min(amount, room)
    getItemStack.get.stackSize += inc
    inc
                                                     }.getOrElse(0)

  /**
    *
    * @param amount Amount to decrease ItemStack stacksize by.  Must be > 0
    * @return Math.min(amount, CurrentStorage) -> Amount of amount removed from the ItemStack.  Clears ItemStack if Math.min(amount, CurrentStorage) == CurrentStorage
    */
  def decrement(amount: Int): Int = getItemStack.map { i =>
    if (amount < 0) return 0

    val dec = Math.min(amount, currentStorage.get)
    if (dec == currentStorage.get) {
      clear()
    }
    else {
      getItemStack.get.stackSize -= dec
    }
    dec
                                                     }.getOrElse(0)

  /**
    *
    * @param amount Amount to remove from this storage and transfer to a new one.
    * @return New item access
    */
  def split(amount: Int): IItemAccess

  /**
    * Copies all required info from another IItemAccess
    *
    * @param other
    */
  def copyFromAccess(other: IItemAccess, copyStack: Boolean = true): Unit =
    setItemStack(other.getItemStack match {
                   case Some(i) => if (copyStack) i.copy() else i
                   case None => null
                 })

  /**
    * Remove Item and metadata from this access.
    */
  def clear(): Unit = {
    setItemStack(null)
  }

}
