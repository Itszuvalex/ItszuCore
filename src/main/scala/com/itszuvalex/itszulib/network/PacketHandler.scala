package com.itszuvalex.itszulib.network

import com.itszuvalex.itszulib.ItszuLib
import com.itszuvalex.itszulib.network.messages.{MessageContainerUpdate, MessagePlayerProperty}
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.relauncher.Side

/**
 * Created by Christopher Harris (Itszuvalex) on 4/6/15.
 */
object PacketHandler {
  val INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ItszuLib.ID.toLowerCase)

  def init(): Unit = {
    INSTANCE.registerMessage(classOf[MessageContainerUpdate], classOf[MessageContainerUpdate], 0, Side.CLIENT)
    INSTANCE.registerMessage(classOf[MessagePlayerProperty], classOf[MessagePlayerProperty], 1, Side.CLIENT)
  }

}
