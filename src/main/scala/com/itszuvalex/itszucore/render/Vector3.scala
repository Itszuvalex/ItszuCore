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
package com.itszuvalex.itszucore.render

import com.itszuvalex.itszucore.api.core.{NBTSerializable, Saveable}
import com.itszuvalex.itszucore.util.DataUtils
import net.minecraft.nbt.NBTTagCompound

/**
 * Created by Christopher Harris (Itszuvalex) on 5/16/14.
 */
class Vector3(@Saveable var x: Double, @Saveable var y: Double, @Saveable var z: Double) extends NBTSerializable {

  def this(a: Point3D, b: Point3D) =
    this(a.x - b.x, a.y - b.y, a.z - b.z)

  def this() =
    this(0, 0, 0)

  def copy() = new Vector3(x, y, z)

  def +(other: Vector3) = new Vector3(x + other.x, y + other.y, z + other.z)

  def +=(other: Vector3) = {
    x += other.x
    y += other.y
    z += other.z
  }

  def inversed = copy().inverse()

  def inverse() = {
    x = -x
    y = -y
    z = -z
    this
  }

  def normalize(): Vector3 = {
    val mag = magnitude
    x /= mag
    y /= mag
    z /= mag
    this
  }

  def normalized = copy().normalize()

  def magnitude = Math.sqrt(magnitudeSquared)

  def magnitudeSquared = x * x + y * y + z * z

  def cross(vector: Vector3): Vector3 = {
    val ret = new Vector3
    ret.x = y * vector.z - z * vector.y
    ret.y = z * vector.x - x * vector.z
    ret.z = x * vector.y - y * vector.x
    ret
  }

  def dot(vector: Vector3) = x * vector.x + y * vector.y + z * vector.z


  def saveToNBT(compound: NBTTagCompound) {
    DataUtils.saveObjectToNBT(compound, this, DataUtils.EnumSaveType.WORLD)
  }

  def loadFromNBT(compound: NBTTagCompound) {
    DataUtils.loadObjectFromNBT(compound, this, DataUtils.EnumSaveType.WORLD)
  }
}
