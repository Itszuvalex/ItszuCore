package com.itszuvalex.itszulib.api.access

import com.itszuvalex.itszulib.{StubItem, TestBase}
import net.minecraft.item.ItemStack

/**
  * Created by Christopher Harris (Itszuvalex) on 3/10/2016.
  */
object AccessHelpers extends TestBase {

  trait EmptyArray {
    val array = new Array[ItemStack](10)
  }

  trait PartialArray {
    val array = new Array[ItemStack](10)
    val item0 = new ItemStack(new StubItem)
    val item3 = new ItemStack(new StubItem, 5)
    val item7 = new ItemStack(new StubItem, 64)
    array(0) = item0
    array(3) = item3
    array(7) = item7
  }

}
