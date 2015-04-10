package com.itszuvalex.itszulib.api.player

import com.itszuvalex.itszulib.api.core.NBTSerializable
import net.minecraft.nbt.NBTTagCompound

trait IPlayerProperty extends NBTSerializable {
  def saveToDescPacket(nbt: NBTTagCompound): Unit

  def loadFromDescPacket(nbt: NBTTagCompound): Unit
}
