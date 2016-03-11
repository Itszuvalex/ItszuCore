package test.api.access

import com.itszuvalex.itszulib.api.access.ArrayItemCollectionAccess
import net.minecraft.item.{Item, ItemStack}
import test.TestBase

/**
  * Created by Christopher Harris (Itszuvalex) on 3/10/2016.
  */
class TestArrayItemCollectionAccess extends TestBase {
  val arrayEmpty = new Array[ItemStack](10)

  val arrayPartial = new Array[ItemStack](10)
  val item1        = new ItemStack(null.asInstanceOf[Item])
  val item3        = new ItemStack(null.asInstanceOf[Item])
  val item7        = new ItemStack(null.asInstanceOf[Item])
  arrayPartial(1) = item1
  arrayPartial(3) = item3
  arrayPartial(7) = item7

  "An ArrayItemCollectionAccess" when {
    "constructed on an empty array" should {
      "have length equal to the backing array" in {
        val collection = new ArrayItemCollectionAccess(arrayEmpty)
        collection.length shouldEqual arrayEmpty.length
      }
    }

  }

}
