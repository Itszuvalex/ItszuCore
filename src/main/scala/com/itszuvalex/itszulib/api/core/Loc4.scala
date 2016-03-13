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
package com.itszuvalex.itszulib.api.core

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.common.util.ForgeDirection

/**
  * Created by Christopher Harris (Itszuvalex) on 5/9/14.
  */
object Loc4 {
  // World to Int
  val defaultWorldIntMapper: (World) => Int = _.provider.dimensionId
  // Int to World
  val defaultIntWorldMapper: (Int) => World = DimensionManager.getWorld
  var currentWorldIntMapper                 = defaultWorldIntMapper
  var currentIntWorldMapper                 = defaultIntWorldMapper

  def apply(compound: NBTTagCompound): Loc4 = {
    if (compound == null) null
    else {
      val loc = new Loc4(0, 0, 0, 0)
      loc.loadFromNBT(compound)
      loc
    }
  }

  def setWorldIntMapper(func: (World) => Int) = currentWorldIntMapper = func

  def restoreDefaultWorldIntMapper() = currentWorldIntMapper = defaultWorldIntMapper

  def mapWorld(world: World): Int = currentWorldIntMapper(world)

  def setIntWorldMapper(func: (Int) => World) = currentIntWorldMapper = func

  def restoreDefaultIntWorldMapper() = currentIntWorldMapper = defaultIntWorldMapper

  def mapInt(int: Int): World = currentIntWorldMapper(int)
}

case class Loc4(var x: Int, var y: Int, var z: Int, var dim: Int) extends NBTSerializable with Comparable[Loc4] {

  def this() = this(0, 0, 0, 0)

  def this(te: TileEntity) = this(te.xCoord, te.yCoord, te.zCoord, Loc4.mapWorld(te.getWorldObj))

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

  def getTileEntity(force: Boolean = false) = getWorld match {
    case Some(a) => Option(if (a.blockExists(x, y, z) || force) a.getTileEntity(x, y, z) else null)
    case None => None
  }

  def getBlock(force: Boolean = false) = getWorld match {
    case Some(a) => Option(if (a.blockExists(x, y, z) || force) a.getBlock(x, y, z) else null)
    case None => None
  }

  def getWorld = Option(Loc4.mapInt(dim))

  def getMetadata(force: Boolean = false) = getWorld match {
    case Some(a) => Option(if (a.blockExists(x, y, z) || force) a.getBlockMetadata(x, y, z) else null)
    case None => None
  }

  def getChunk(force: Boolean = false) = getWorld match {
    case Some(a) => Option(if (a.blockExists(x, y, z) || force) a.getChunkFromBlockCoords(x, z) else null)
    case None => None
  }

  def chunkCoords = (x >> 4, z >> 4)

  def chunkContains(chunk: Chunk): Boolean = {
    if (Loc4.mapWorld(chunk.worldObj) != dim) false
    else if (x <= chunk.xPosition * 16) false
    else if (x > chunk.xPosition * 16 + 16) false
    else if (z <= chunk.zPosition * 16) false
    else if (z > chunk.zPosition * 16 + 16) false
    else true
  }

  def isNeighbor(loc: Loc4): Boolean = {
    if (loc.dim != dim) false
    else if ((Math.abs(loc.x - x) == 1) && loc.y == y && loc.z == z) true //x
    else if (loc.x == x && (Math.abs(loc.y - y) == 1) && loc.z == z) true //y
    else if (loc.x == x && loc.y == y && (Math.abs(loc.z - z) == 1)) true //z
    else false
  }

  def getOffset(dir: ForgeDirection, distance: Int = 1): Loc4 = getOffset(distance * dir.offsetX,
                                                                          distance * dir.offsetY,
                                                                          distance * dir.offsetZ)


  def getOffset(xOffset: Int, yOffset: Int, zOffset: Int): Loc4 = new Loc4(x + xOffset, y + yOffset, z + zOffset, dim)

  def distSqr(other: Loc4): Double = {
    if (other.dim != dim) return Float.MaxValue
    distSqr(other.x, other.y, other.z)
  }

  def distSqr(x: Int, y: Int, z: Int): Double =
    (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y) + (this.z - z) * (this.z - z)

  def dist(other: Loc4): Double = {
    if (other.dim != dim) return Float.MaxValue
    dist(other.x, other.y, other.z)
  }

  def dist(x: Int, y: Int, z: Int) = Math.sqrt(distSqr(x, y, z))

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
