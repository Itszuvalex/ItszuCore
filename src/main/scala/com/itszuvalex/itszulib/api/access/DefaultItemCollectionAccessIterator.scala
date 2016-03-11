package com.itszuvalex.itszulib.api.access

/**
  * Created by Christopher Harris (Itszuvalex) on 3/10/16.
  */
class DefaultItemCollectionAccessIterator(private[access] val collection: IItemCollectionAccess) extends Iterator[IItemAccess] {
  private[access] val revision = collection.getRevision
  private[access] var index    = 0

  override def hasNext: Boolean = isValid && index < collection.length

  def isValid: Boolean = revision == collection.getRevision

  override def next(): IItemAccess = {
    val ret = collection(index)
    index += 1
    ret
  }
}
