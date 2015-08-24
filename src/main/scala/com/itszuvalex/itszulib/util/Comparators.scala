package com.itszuvalex.itszulib.util

import java.util.Comparator

import com.itszuvalex.itszulib.implicits.IDImplicits._
import com.itszuvalex.itszulib.util.Comparators.NBT.CompoundSizeComparator
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.oredict.OreDictionary

/**
 * Created by Christopher on 8/24/2015.
 */
object Comparators {

  object ItemStack {

    object IDComparator extends Comparator[ItemStack] {
      override def compare(o1: ItemStack, o2: ItemStack): Int = (o1, o2) match {
        case (null, null) => 0
        case (null, _)    => -1
        case (_, null)    => 1
        case (i1, i2)     => i2.itemID - i1.itemID
      }
    }

    object DamageComparator extends Comparator[ItemStack] {
      override def compare(o1: ItemStack, o2: ItemStack): Int = (o1, o2) match {
        case (null, null) => 0
        case (null, _)    => -1
        case (_, null)    => 1
        case (i1, i2)     => i2.getItemDamage - i1.getItemDamage
      }
    }

    object DamageWildCardComparator extends Comparator[ItemStack] {
      override def compare(o1: ItemStack, o2: ItemStack): Int = (o1, o2) match {
        case (null, null) => 0
        case (null, _)    => -1
        case (_, null)    => 1
        case (i1, i2)     => (i1.getItemDamage, i2.getItemDamage) match {
          case (OreDictionary.WILDCARD_VALUE, _) => 0
          case (_, OreDictionary.WILDCARD_VALUE) => 0
          case (d1, d2)                          => d2 - d1
        }
      }
    }

    object IDDamageComparator extends Comparator[ItemStack] {
      override def compare(o1: ItemStack, o2: ItemStack): Int = {
        IDComparator.compare(o1, o2) match {
          case 0 => DamageComparator.compare(o1, o2)
          case r => r
        }
      }
    }

    object IDDamageWildCardComparator extends Comparator[ItemStack] {
      override def compare(o1: ItemStack, o2: ItemStack): Int = IDComparator.compare(o1, o2) match {
        case 0 => DamageWildCardComparator.compare(o1, o2)
        case r => r
      }
    }

    object IDDamageNBTComparator extends Comparator[ItemStack] {
      override def compare(o1: ItemStack, o2: ItemStack): Int = IDComparator.compare(o1, o2) match {
        case 0 => DamageComparator.compare(o1, o2) match {
          case 0 if o1 != null && o2 != null => CompoundSizeComparator.compare(o1.getTagCompound, o2.getTagCompound)
          case r                             => r
        }
        case r => r
      }
    }

    object IDDamageWildCardNBTComparator extends Comparator[ItemStack] {
      override def compare(o1: ItemStack, o2: ItemStack): Int = IDComparator.compare(o1, o2) match {
        case 0 => DamageWildCardComparator.compare(o1, o2) match {
          case 0 if o1 != null && o2 != null => CompoundSizeComparator.compare(o1.getTagCompound, o2.getTagCompound)
          case r                             => r
        }
        case r => r
      }
    }

  }

  object FluidStack {

    object IDComparator extends Comparator[FluidStack] {
      override def compare(o1: FluidStack, o2: FluidStack): Int = (o1, o2) match {
        case (null, null) => 0
        case (null, _)    => -1
        case (_, null)    => 1
        case (f1, f2)     => f2.fluidID - f1.fluidID
      }
    }


    object IDNBTComparator extends Comparator[FluidStack] {
      override def compare(o1: FluidStack, o2: FluidStack): Int = {
        IDNBTComparator.compare(o1, o2) match {
          case 0 if o1 != null && o2 != null => CompoundSizeComparator.compare(o1.tag, o2.tag)
          case r                             => r
        }
      }

    }

  }


  object NBT {

    object CompoundSizeComparator extends Comparator[NBTTagCompound] {
      override def compare(o1: NBTTagCompound, o2: NBTTagCompound): Int = (o1, o2) match {
        case (null, null) => 0
        case (null, _)    => -1
        case (_, null)    => 1
        case (c1, c2)     => c2.func_150296_c().size() - c1.func_150296_c().size()
      }
    }

  }

}
