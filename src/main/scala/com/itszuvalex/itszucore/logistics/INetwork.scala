package com.itszuvalex.itszucore.logistics

import java.util

import com.itszuvalex.itszucore.api.core.Loc4
import io.netty.buffer.ByteBuf

import scala.collection.immutable.HashSet

/**
 * Created by Christopher on 4/5/2015.
 */

trait INetwork[C <: INetworkNode[T], T <: INetwork[C, T]] {

  /**
   *
   * @return Network Identifier.  This should be unique.
   */
  def ID: Int

  /**
   *
   * @param nodes Nodes to make a new network out of
   * @return Create a new network of this type from the given collection of nodes.
   */
  def create(nodes: util.Collection[C]): T

  /**
   *
   * @return All nodes in this network.
   */
  def getNodes: util.Collection[C]

  /**
   *
   * Adds a node to the network.  Informs the node of its being added.  Informs all other nodes that this node is added.
   *
   * @param node Node to add.
   */
  def addNode(node: C): Unit

  /**
   *
   * @param node Node to be added.
   * @return true if this node can be added to the network.
   */
  def canAddNode(node: C): Boolean

  /**
   * Removes a node from the network.  Informs the node of its being removed.  Informs all other nodes that this node is being removed.
   *
   * @param node
   */
  def removeNode(node: C): Unit

  /**
   * Simply remove all nodes from the network.  Does not inform them.
   */
  def clear(): Unit

  /**
   * Removes all nodes from the network, and informs nodes to tear down connections as well.
   */
  def tearDown(): Unit

  /**
   * Orders all nodes to refresh.
   */
  def refresh(): Unit

  /**
   * Register this network with the Network Manager.  Starts tick updates.
   */
  def register(): Unit

  /**
   * Unregister this network from the Network Manager.  Stops tick updates.
   */
  def unregister(): Unit

  /**
   *
   * @return Number of nodes in the network.
   */
  def size: Int

  /**
   *
   * Called when a node is removed from the network.  Maps all out all sub-networks created by the split, creates and registers them, and informs nodes.
   *
   * @param pivot Node that has been removed from the network.
   * @param connections Connections given by pivot before network removal.
   */
  def split(pivot: C, connections: HashSet[Loc4]): Unit

  /**
   *
   * Called when a node is added to the network.  Forcibly tears down iNetwork, and adds all of its nodes to this network.
   *
   * @param iNetwork Network that this network is taking over.
   */
  def merge(iNetwork: INetwork[C, T]): Unit

  /**
   *
   * Called when the client receives a network packet update from the server network.
   *
   * @param data Data sent in a network packet.
   */
  def handlePacketData(data: ByteBuf): Unit

}
