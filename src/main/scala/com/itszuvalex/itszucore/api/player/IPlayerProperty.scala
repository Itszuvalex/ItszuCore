package com.itszuvalex.itszucore.api.player

import com.itszuvalex.itszucore.api.core.NBTSerializable
import net.minecraft.nbt.NBTTagCompound

trait IPlayerProperty extends NBTSerializable {
  def saveToDescPacket(nbt: NBTTagCompound): Unit

  def loadFromDescPacket(nbt: NBTTagCompound): Unit
}
