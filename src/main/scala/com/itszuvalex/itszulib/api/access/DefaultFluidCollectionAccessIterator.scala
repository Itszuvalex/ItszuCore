package com.itszuvalex.itszulib.api.access

/**
  * Created by Christopher Harris (Itszuvalex) on 3/10/16.
  */
class DefaultFluidCollectionAccessIterator(collection: IFluidCollectionAccess) extends AccessCollectionIterator[IFluidCollectionAccess, IFluidAccess](collection) {
  private[access] val revision = collection.getRevision

  def isValid: Boolean = revision == collection.getRevision
}
