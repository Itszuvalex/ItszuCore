package com.itszuvalex.itszucore.network

import com.itszuvalex.itszucore.ItszuCore
import com.itszuvalex.itszucore.network.messages.{MessageContainerUpdate, MessagePlayerProperty}
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.relauncher.Side

/**
 * Created by Christopher Harris (Itszuvalex) on 4/6/15.
 */
object PacketHandler {
  val INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ItszuCore.ID.toLowerCase)

  def init(): Unit = {
    INSTANCE.registerMessage(classOf[MessageContainerUpdate], classOf[MessageContainerUpdate], 0, Side.CLIENT)
    INSTANCE.registerMessage(classOf[MessagePlayerProperty], classOf[MessagePlayerProperty], 1, Side.CLIENT)
  }

}
