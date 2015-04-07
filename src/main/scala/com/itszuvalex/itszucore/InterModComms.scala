package com.itszuvalex.itszucore

import cpw.mods.fml.common.event.FMLInterModComms
import org.apache.logging.log4j.Level

import scala.collection.JavaConversions._

/**
 * Created by Christopher Harris (Itszuvalex) on 4/6/15.
 */
object InterModComms {
  final val KEY_GREETING = "GREETING"
  final val mods         = Set[String]()

  def imcCallback(event: FMLInterModComms.IMCEvent) {
    event.getMessages.foreach { message =>
      try {
        message.key match {
          case KEY_GREETING => onGreeting(message.getSender, message.getStringValue)
        }

      }
      catch {
        case e: Exception =>
          ItszuCore.logger.log(Level.ERROR, "Error processing inter-mod communications from " + message.getSender + ", key=" + message.key)
      }
                              }
  }


  private def onGreeting(modID: String, talkBack: String): Unit = {
    ItszuCore.logger.log(Level.INFO, talkBack)
  }
}
