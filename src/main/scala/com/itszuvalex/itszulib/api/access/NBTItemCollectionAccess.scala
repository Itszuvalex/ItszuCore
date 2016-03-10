package com.itszuvalex.itszulib.api.access

import com.itszuvalex.itszulib.api.access.NBTItemCollectionAccess._
import com.itszuvalex.itszulib.implicits.NBTHelpers.NBTAdditions._
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound

/**
  * Created by Christopher Harris (Itszuvalex) on 3/10/16.
  */
object NBTItemCollectionAccess {
  val SIZE_KEY = "Size"
}

/**
  * The given NBT compound has a specific setting.
  * Key: String -> Value: Type
  *
  *
  * Size -> # of ItemStacks stored in this compound: Int
  * 0 -> ItemStack 0: NBTTagCompound
  * .
  * .
  * .
  * (size - 1) -> ItemStack (size -1): NBTTagCompound
  *
  * @param nbt NBT compound storing items.
  */
class NBTItemCollectionAccess(nbt: NBTTagCompound) extends IItemCollectionAccess {
  override def canPlayerAccess(player: EntityPlayer): Boolean = true

  override def length: Int = nbt.Int(SIZE_KEY)

  override def apply(idx: Int): IItemAccess = new NBTItemAccess(nbt, idx)

  override def iterator: Iterator[IItemAccess] = new DefaultItemCollectionAccessIterator(this)
}
