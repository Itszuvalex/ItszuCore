package com.itszuvalex.itszucore.logistics

import java.util.concurrent.ConcurrentHashMap

import com.itszuvalex.itszucore.api.core.Loc4

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.collection._
import scala.collection.immutable.HashSet

/**
 * Created by Christopher Harris (Itszuvalex) on 4/5/15.
 */
object TileNetwork {

  object NetworkExplorer {
    def explore[N <: TileNetwork[_, N]](start: Loc4): HashSet[Loc4] = {
      immutable.HashSet[Loc4]() ++ expandLoc[N](start, mutable.HashSet[Loc4]())
    }

    private def expandLoc[N <: TileNetwork[_, N]](node: Loc4, explored: mutable.HashSet[Loc4]): mutable.HashSet[Loc4] = {
      node.getTileEntity() match {
        case Some(c) =>
          c match {
            case a: INetworkNode[N] if !explored.contains(a.getLoc) =>
              explored += node
              a.getConnections.foreach(expandLoc(_, explored))
            case _ =>
          }
        case None =>
      }
      explored
    }

  }


}

abstract class TileNetwork[C <: INetworkNode[N], N <: TileNetwork[C, N]](val id: Int) extends INetwork[C, N] {
  def nodeMap = new ConcurrentHashMap[Loc4, C]().asScala

  override def addNode(node: C): Unit = {
    if (!canAddNode(node)) return
    getNodes.filter(_.canConnect(node.getLoc)).foreach(n => {
      node.onConnectionAdd(node.getLoc)
      n.onConnectionAdd(n.getLoc)
    })
    nodeMap(node.getLoc) = node
    node.onAdd(this)
  }

  override def ID = id

  override def size = nodeMap.size

  override def clear(): Unit = {
    nodeMap.clear()
  }

  override def refresh(): Unit = {
    //    val nodes = getNodes
    //    nodeMap.clear()
    getNodes.foreach(_.refresh())
    //    if (getNodes.size() == 0) {
    //      ManagerNetwork.removeNetwork(this)
    //    }
  }

  override def merge(iNetwork: INetwork[C, N]): Unit = {
    val nodes = iNetwork.getNodes
    iNetwork.clear()
    iNetwork.refresh()
    nodes.foreach(addNode)
  }

  override def split(pivot: C, connections: HashSet[Loc4]): Unit = {

  }

  override def removeNode(node: C) = {
    val connections = node.getConnections
    node.onRemove(this)
    nodeMap.remove(node.getLoc)
    getNodes.filter(_.getConnections.contains(node.getLoc)).foreach(_.onConnectionRemove(node.getLoc))
    split(node, connections)
  }


  override def getNodes = nodeMap.values.asJavaCollection

  override def canAddNode(node: C): Boolean = true

  override def register() = ManagerNetwork.addNetwork(this)

  override def unregister(): Unit = ManagerNetwork.removeNetwork(this)
}
