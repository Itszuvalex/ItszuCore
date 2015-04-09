package com.itszuvalex.itszucore.logistics

import com.itszuvalex.itszucore.api.core.Loc4


/**
 * Created by Christopher Harris (Itszuvalex) on 4/5/15.
 */
trait INetworkNode[T <: INetwork[_, T]] {

  def setNetwork(network: T)

  def getNetwork: T

  def getLoc: Loc4

  def canConnect(loc: Loc4): Boolean

  def refresh(): Unit

  def canAdd[N <: T](iNetwork: N): Boolean

  def added[N <: T](iNetwork: N): Unit

  def removed[N <: T](iNetwork: N): Unit

  def connect(node: Loc4): Unit

  def disconnect(node: Loc4): Unit

}
