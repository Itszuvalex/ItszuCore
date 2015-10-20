package com.itszuvalex.itszulib.core.traits.tile

import com.itszuvalex.itszulib.api.core.Saveable
import com.itszuvalex.itszulib.core.TileEntityBase
import com.itszuvalex.itszulib.network.PacketHandler
import com.itszuvalex.itszulib.network.messages.MessageFluidTankUpdate
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._

/**
 * Created by Chris on 11/30/2014.
 */
trait TileFluidTank extends TileEntityBase with IFluidHandler {
  @Saveable var tank = defaultTank

  var syncTank: Boolean = false

  def defaultTank: FluidTank

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean) = tank.fill(resource, doFill)

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = {
    if (resource == null || !resource.isFluidEqual(tank.getFluid)) null
    else tank.drain(resource.amount, doDrain)
  }

  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean) = tank.drain(maxDrain, doDrain)

  override def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] = Array(tank.getInfo)

  var updateNeeded: Boolean = false

  /**
   * If you change your tanks in serverUpdate, make sure to change them *BEFORE* calling super.serverUpdate().
   * This way the client will get notified of the change in the same tick.
   */
  override def serverUpdate(): Unit = {
    super.serverUpdate()
    if (!updateNeeded) return
    PacketHandler.INSTANCE.sendToDimension(new MessageFluidTankUpdate(xCoord, yCoord, zCoord, if (tank.getFluid == null) -1 else tank.getFluid.getFluidID, tank.getFluidAmount), getWorldObj.provider.dimensionId)
    updateNeeded = false
  }

  override def hasDescription: Boolean = syncTank

  override def saveToDescriptionCompound(compound: NBTTagCompound): Unit = {
    super.saveToDescriptionCompound(compound)
    if (!syncTank) return
    val tankComp = new NBTTagCompound
    tankComp.setInteger("capacity", tank.getCapacity)
    tank.writeToNBT(tankComp)
    compound.setTag("tank", tankComp)
  }

  override def handleDescriptionNBT(compound: NBTTagCompound): Unit = {
    super.handleDescriptionNBT(compound)
    if (!compound.hasKey("tank")) return
    val tankComp = compound.getCompoundTag("tank")
    tank = new FluidTank(tankComp.getInteger("capacity"))
    tank.readFromNBT(tankComp)
  }

  def setUpdateTank() = updateNeeded = true

  def setTankToSync(value: Boolean) = syncTank = value
}
