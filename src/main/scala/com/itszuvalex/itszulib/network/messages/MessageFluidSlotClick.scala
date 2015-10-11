package com.itszuvalex.itszulib.network.messages

import com.itszuvalex.itszulib.core.FluidSlotContainer
import com.itszuvalex.itszulib.network.PacketHandler
import cpw.mods.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}
import io.netty.buffer.ByteBuf
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/**
 * Created by Alex on 11.10.2015.
 */
class MessageFluidSlotClick(var slotID: Int, var button: Int) extends IMessage with IMessageHandler[MessageFluidSlotClick, IMessage] {
  def this() = this(-1, -1)

  override def toBytes(buf: ByteBuf): Unit = {
    buf.writeInt(slotID)
    buf.writeInt(button)
  }

  override def fromBytes(buf: ByteBuf): Unit = {
    slotID = buf.readInt()
    button = buf.readInt()
  }

  override def onMessage(message: MessageFluidSlotClick, ctx: MessageContext): IMessage = {
    val player = ctx.getServerHandler.playerEntity
    player.openContainer match {
      case cont: FluidSlotContainer =>
        val stack = cont.getFluidSlot(message.slotID).onClick(player.inventory.getItemStack, message.button)
        if (ItemStack.areItemStacksEqual(stack, player.inventory.getItemStack)) return null
        player.inventory.setItemStack(stack)
        val tag = new NBTTagCompound()
        if (stack != null) {
          stack.writeToNBT(tag)
        }
        PacketHandler.INSTANCE.sendTo(new MessageUpdateGuiItemStack(tag), player)
      case _ =>
    }
    null
  }
}
