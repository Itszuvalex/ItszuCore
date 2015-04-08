package com.itszuvalex.itszucore.logistics

import com.itszuvalex.itszucore.api.core.Loc4

import scala.collection.immutable.HashSet


/**
 * Created by Christopher Harris (Itszuvalex) on 4/5/15.
 */
trait INetworkNode[T <: INetwork[_, T]] {

  def getNetworkID: Int

  def setNetworkID(id: Int)

  def getNetwork: T

  def getLoc: Loc4

  def getConnections: HashSet[Loc4]

  def canConnect(loc: Loc4): Boolean

  def refresh(): Unit

  def onAdd[N <: T](iNetwork: N): Unit

  def onRemove[N <: T](iNetwork: N): Unit

  def onConnectionAdd(node: Loc4): Unit

  def onConnectionRemove(node: Loc4): Unit

}
