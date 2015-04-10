package com.itszuvalex.itszucore

import java.io.File
import java.util.UUID

import com.itszuvalex.itszucore.configuration.xml.XMLLoaderWriter
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent

import scala.collection.mutable

/**
 * Created by Christopher Harris (Itszuvalex) on 3/29/15.
 */
object PlayerUUIDTracker {
  private var xml: XMLLoaderWriter = null

  def init(): Unit = {
    FMLCommonHandler.instance().bus().register(this)
  }

  def setFile(file: File): Unit = {
    xml = new XMLLoaderWriter(file)
    load()
  }

  val UUIDToUsername = new mutable.HashMap[UUID, String]()
  val UsernameToUUID = new mutable.HashMap[String, UUID]()


  def getUsername(uuid: UUID) = UUIDToUsername.getOrElse(uuid, "")

  def getUUID(string: String) = UsernameToUUID.getOrElse(string, null)

  def addMapping(uuid: UUID, username: String) = {
    if (UUIDToUsername.get(uuid).orNull != username) {
      UUIDToUsername(uuid) = username
      UsernameToUUID(username) = uuid
      save()
    }
  }

  def save() = {
    xml.xml = <xml>
      {for (mapping <- UUIDToUsername) yield <Mapping uuid={mapping._1.toString} username={mapping._2}/>}
    </xml>
    xml.save()
  }

  def load() = {
    UUIDToUsername.clear()
    xml.load()
    (xml.xml \ "Mapping").foreach(node => try addMapping(UUID.fromString(node \@ "uuid"), node \@ "username") catch {case _: Throwable =>})
  }

  @SubscribeEvent
  def onPlayerLogin(event: PlayerLoggedInEvent) = {
    addMapping(event.player.getUniqueID, event.player.getCommandSenderName)
  }
}
