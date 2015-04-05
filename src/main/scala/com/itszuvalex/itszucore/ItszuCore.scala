package com.itszuvalex.itszucore

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}

/**
 * Created by Christopher on 4/5/2015.
 */
@Mod(modid = ItszuCore.ID, name = ItszuCore.ID, version = ItszuCore.VERSION, modLanguage = "scala")
object ItszuCore {
  final val ID      = "ItszuCore"
  final val VERSION = Version.FULL_VERSION

  @EventHandler def preInit(event: FMLPreInitializationEvent): Unit = {

  }

  @EventHandler def load(event: FMLInitializationEvent): Unit = {
   
  }

  @EventHandler def postInit(event: FMLPostInitializationEvent): Unit = {

  }

}
