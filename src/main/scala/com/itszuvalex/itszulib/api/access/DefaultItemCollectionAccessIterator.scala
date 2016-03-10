package com.itszuvalex.itszulib.api.access

/**
  * Created by Christopher Harris (Itszuvalex) on 3/10/16.
  */
class DefaultItemCollectionAccessIterator(collection: IItemCollectionAccess) extends Iterator[IItemAccess] {
  var index = 0

  override def hasNext: Boolean = index < collection.length

  override def next(): IItemAccess = {
    val ret = collection(index)
    index += 1
    ret
  }
}
