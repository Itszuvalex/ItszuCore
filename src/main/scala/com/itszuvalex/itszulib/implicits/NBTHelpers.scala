package com.itszuvalex.itszulib.implicits

import com.itszuvalex.itszulib.api.core.NBTSerializable
import net.minecraft.nbt.{NBTBase, NBTTagCompound, NBTTagList}

import scala.collection.TraversableOnce

/**
 * Created by Christopher on 8/17/2015.
 */
object NBTHelpers {

  object NBTLiterals {

    def NBTCompound(serializable: NBTSerializable) = {
      val compound = new NBTTagCompound
      serializable.saveToNBT(compound)
      compound
    }

    def NBTCompound(elems: (String, Any)*): NBTTagCompound = NBTAdditions.NBTCompoundAdding(new NBTTagCompound)(elems: _*)

    def NBTList(collection: TraversableOnce[_ <: NBTBase]) = {
      val list = new NBTTagList
      collection.foreach(list.appendTag)
      list
    }

    def NBTList(elems: NBTBase*): NBTTagList = {
      val list = new NBTTagList
      elems.foreach(list.appendTag)
      list
    }

  }

  object NBTAdditions {

    implicit class NBTListAdding(list: NBTTagList) {
      def +=(tag: NBTBase) = list.appendTag(tag)

      def ++=(xs: TraversableOnce[_ <: NBTBase]) = xs.foreach(list.appendTag)
    }

    implicit class NBTCompoundAdding(compound: NBTTagCompound) {
      def apply(elems: (String, Any)*): NBTTagCompound = {
        elems.foreach { case (key, value) => value match {
          case null                  =>
          case b: Boolean            => compound.setBoolean(key, b)
          case b: Byte               => compound.setByte(key, b)
          case ba: Array[Byte]       => compound.setByteArray(key, ba)
          case d: Double             => compound.setDouble(key, d)
          case f: Float              => compound.setFloat(key, f)
          case ia: Array[Int]        => compound.setIntArray(key, ia)
          case i: Int                => compound.setInteger(key, i)
          case l: Long               => compound.setLong(key, l)
          case s: Short              => compound.setShort(key, s)
          case s: String             => compound.setString(key, s)
          case n: NBTBase            => compound.setTag(key, n)
          case save: NBTSerializable =>
            compound.setTag(key, NBTLiterals.NBTCompound(save))
          case _                     =>
        }
                      }
        compound
      }
    }

  }

}
