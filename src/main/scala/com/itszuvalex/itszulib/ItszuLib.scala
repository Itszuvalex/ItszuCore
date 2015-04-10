package com.itszuvalex.itszulib

import com.itszuvalex.itszulib.network.PacketHandler
import com.itszuvalex.itszulib.proxy.ProxyCommon
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLInterModComms, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Mod, SidedProxy}
import org.apache.logging.log4j.LogManager

/**
 * Created by Christopher on 4/5/2015.
 */
@Mod(modid = ItszuLib.ID, name = ItszuLib.ID, version = ItszuLib.VERSION, modLanguage = "scala")
object ItszuLib {
  final val ID      = "ItszuLib"
  final val VERSION = Version.FULL_VERSION
  final val logger  = LogManager.getLogger(ID)

  @SidedProxy(clientSide = "com.itszuvalex.itszulib.proxy.ProxyClient",
              serverSide = "com.itszuvalex.itszulib.proxy.ProxyServer")
  var proxy: ProxyCommon = null

  @EventHandler def preInit(event: FMLPreInitializationEvent): Unit = {
    PacketHandler.init()
    PlayerUUIDTracker.init()
    proxy.init()
  }

  @EventHandler def load(event: FMLInitializationEvent): Unit = {

  }

  @EventHandler def postInit(event: FMLPostInitializationEvent): Unit = {

  }

  @EventHandler def imcCallback(event: FMLInterModComms.IMCEvent) {
    InterModComms.imcCallback(event)
  }

}
