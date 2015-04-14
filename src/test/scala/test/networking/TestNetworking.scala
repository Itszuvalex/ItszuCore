package test.networking

import com.itszuvalex.itszulib.api.core.Loc4
import com.itszuvalex.itszulib.logistics.{INetwork, TileNetwork, TileNetworkNode}
import test.TestBase

/**
 * Created by Christopher Harris (Itszuvalex) on 4/14/15.
 */
class TestNetworking extends TestBase {

  trait Network {
    TestNetworkManager.nextId = 0
    val network = new TestNetwork(TestNetworkManager.getID)
  }

  trait OriginNode {
    val origin = new TestNode(Loc4(0, 0, 0, 0))
  }

  trait NetworkWithOrigin extends Network with OriginNode {network.addNode(origin)}

  "A Network" when {
    "constructing" should {
      "construct" in new Network {
      }
      "have no nodes" in new Network {
        network.getNodes.isEmpty shouldBe true
      }
      "have no connections" in new Network {
        network.getConnections.isEmpty shouldBe true
        network.getEdges.isEmpty shouldBe true
      }
    }

    "adding a node" should {
      "have 1 node" in new NetworkWithOrigin {
        val nodes = network.getNodes
        nodes.size() shouldBe 1
      }

      "have the added node" in new NetworkWithOrigin {
        val nodes = network.getNodes
        nodes should contain(origin)
      }
    }

    "adding connectable node" should {
      "have 2 nodes" in new NetworkWithOrigin {
        val neighbor = new TestNode(Loc4(1, 0, 0, 0))
        network.addNode(neighbor)
        val nodes = network.getNodes
        nodes.size() shouldBe 2
        nodes should contain allOf(origin, neighbor)
      }

      "have 1 edge" in new NetworkWithOrigin {
        val neighbor = new TestNode(Loc4(1, 0, 0, 0))
        network.addNode(neighbor)
        val edges = network.getEdges
        edges.size() shouldBe 1
        edges should contain(Loc4(0, 0, 0, 0) -> Loc4(1, 0, 0, 0))
      }
    }

    "creating" should {
      "make a network of the same type" in new Network {
        network.create() shouldBe a[TestNetwork]
      }
    }
  }


  /*
  TEST CLASS EXTENSIONS
   */

  object TestNetworkManager {
    var nextId     = 0
    val networkMap = scala.collection.mutable.HashMap[Int, TestNetwork]()

    def getID = { nextId += 1; nextId }
  }

  class TestNetwork(_id: Int) extends TileNetwork[TestNode, TestNetwork](_id) {

    override def addConnection(a: Loc4, b: Loc4): Unit = {
      addConnectionSilently(a, b)
    }

    override def removeConnection(a: Loc4, b: Loc4): Unit = {
      removeConnectionSilently(a, b)
    }

    /**
     *
     * @return Create an empty new network of this type.
     */
    override def create() = new TestNetwork(TestNetworkManager.getID)

    /**
     * Called on networks by another network, when that network is incorporating this network.
     *
     * @param iNetwork Network that is taking over this network.
     */
    override def onTakeover(iNetwork: INetwork[TestNode, TestNetwork]): Unit = {}

    /**
     * Called on sub networks by a main network, when that network is splitting apart.
     *
     * @param iNetwork Network that will split into this sub network.
     */
    override def onSplit(iNetwork: INetwork[TestNode, TestNetwork]): Unit = {}

    /**
     * Called when a tick starts.
     */
    override def onTickStart(): Unit = {}

    override def unregister(): Unit = { TestNetworkManager.networkMap.remove(ID) }

    override def register(): Unit = { TestNetworkManager.networkMap(ID) = this }

    /**
     * Called when a tick ends.
     */
    override def onTickEnd(): Unit = {}

    override val id: Int = {TestNetworkManager.nextId += 1; TestNetworkManager.nextId}
  }

  class TestNode(val loc: Loc4) extends TileNetworkNode[TestNode, TestNetwork] {
    override def getLoc: Loc4 = loc
  }

}
