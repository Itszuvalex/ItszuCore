/*
 * ******************************************************************************
 *  * Copyright (C) 2013  Christopher Harris (Itszuvalex)
 *  * Itszuvalex@gmail.com
 *  *
 *  * This program is free software; you can redistribute it and/or
 *  * modify it under the terms of the GNU General Public License
 *  * as published by the Free Software Foundation; either version 2
 *  * of the License, or (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program; if not, write to the Free Software
 *  * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *  *****************************************************************************
 */
package com.itszuvalex.itszucore.api.core

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.DimensionManager

/**
 * Created by Christopher Harris (Itszuvalex) on 5/9/14.
 */
case class Loc4(var x: Int, var y: Int, var z: Int, var dim: Int) extends NBTSerializable with Comparable[Loc4] {

  def this() = this(0, 0, 0, 0)

  def this(te: TileEntity) = this(te.xCoord, te.yCoord, te.zCoord, te.getWorldObj.provider.dimensionId)

  def saveToNBT(compound: NBTTagCompound) {
    compound.setInteger("x", x)
    compound.setInteger("y", y)
    compound.setInteger("z", z)
    compound.setInteger("dim", dim)
  }

  def loadFromNBT(compound: NBTTagCompound) {
    x = compound.getInteger("x")
    y = compound.getInteger("y")
    z = compound.getInteger("z")
    dim = compound.getInteger("dim")
  }

  def getWorld = Option(DimensionManager.getWorld(dim))

  def getTileEntity = getWorld match {
    case Some(a) => Option(a.getTileEntity(x, y, z))
    case None => None
  }

  def getBlock = getWorld match {
    case Some(a) => Option(a.getBlock(x, y, z))
    case None => None
  }

  def compareTo(o: Loc4): Int = {
    if (x < o.x) return -1
    if (x > o.x) return 1
    if (y < o.y) return -1
    if (y > o.y) return 1
    if (z < o.z) return -1
    if (z > o.z) return 1
    if (dim < o.dim) return -1
    if (dim > o.dim) return 1
    0
  }
}
