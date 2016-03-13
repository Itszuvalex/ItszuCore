package com.itszuvalex.itszulib.api.access

/**
  * Created by Christopher Harris (Itszuvalex) on 3/12/2016.
  */
abstract class AccessCollectionIterator[Collection <: Seq[Access], Access](private[access] val collection: Collection) extends Iterator[Access] {
  private[access] var index = 0

  override def hasNext: Boolean = isValid && index < collection.length

  def isValid: Boolean

  override def next(): Access = {
    val ret = collection(index)
    index += 1
    ret
  }
}
