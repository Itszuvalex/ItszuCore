package com.itszuvalex.itszucore.logistics

import com.itszuvalex.itszucore.api.core.Loc4
import com.itszuvalex.itszucore.core.TileEntityBase
import net.minecraftforge.common.util.ForgeDirection

import scala.collection.immutable.HashSet

/**
 * Created by Christopher Harris (Itszuvalex) on 4/8/15.
 */
trait TileNetworkNode[C <: TileNetworkNode[C, T], T <: TileNetwork[C, T]] extends TileEntityBase with INetworkNode[T] {
  var network = 0

  override def canConnect(loc: Loc4): Boolean = getConnections.contains(loc)

  override def getLoc = new Loc4(this)

  override def getConnections: HashSet[Loc4] = {
    HashSet[Loc4]() ++ ForgeDirection.VALID_DIRECTIONS.map(getLoc.getOffset(_))
  }

  override def setNetworkID(id: Int) = network = id

  override def onAdd[N <: T](iNetwork: N): Unit = {}

  override def getNetwork: T = ManagerNetwork.getNetwork(network).asInstanceOf[Option[T]].get

  override def refresh(): Unit = {}

  override def onRemove[N <: T](iNetwork: N): Unit = {}

  override def getNetworkID = network

  override def onNetworkAdd[N <: T](node: INetworkNode[T], iNetwork: N): Unit = {

  }

  override def onNetworkRemove[N <: T](node: INetworkNode[T], iNetwork: N): Unit = {

  }
}
