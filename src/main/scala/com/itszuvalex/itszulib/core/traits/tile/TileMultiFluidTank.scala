package com.itszuvalex.itszulib.core.traits.tile

import com.itszuvalex.itszulib.api.core.Saveable
import com.itszuvalex.itszulib.core.TileEntityBase
import com.itszuvalex.itszulib.network.PacketHandler
import com.itszuvalex.itszulib.network.messages.MessageFluidTankUpdate
import net.minecraft.nbt.{NBTTagList, NBTTagCompound}
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{IFluidHandler, FluidTankInfo, FluidStack, FluidTank}

/**
 * Created by Alex on 04.10.2015.
 */
trait TileMultiFluidTank extends TileEntityBase with IFluidHandler {
  @Saveable var tanks: Array[FluidTank] = defaultTanks

  var updateNeeded: Boolean = false
  var tanksToSync: Seq[Int] = Seq.empty[Int]

  def defaultTanks: Array[FluidTank]

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int

  def fill(id: Int, from: ForgeDirection, resource: FluidStack, doFill: Boolean) = tanks(id).fill(resource, doFill)

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack

  def drain(id: Int, from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = {
    if (resource == null || !resource.isFluidEqual(tanks(id).getFluid)) null
    else tanks(id).drain(resource.amount, doDrain)
  }

  override def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] = {for (i <- 0 until tanks.length) yield tanks(i).getInfo}.toArray

  /**
   * If you change your tanks in serverUpdate, make sure to change them *BEFORE* calling super.serverUpdate().
   * This way the client will get notified of the change in the same tick.
   */
  override def serverUpdate(): Unit = {
    super.serverUpdate()
    if (!updateNeeded) return
    for (i <- 0 until tanks.length) {
      val tank = tanks(i)
      PacketHandler.INSTANCE.sendToDimension(new MessageFluidTankUpdate(xCoord, yCoord, zCoord, i, if (tank.getFluid == null) -1 else tank.getFluid.getFluidID, tank.getFluidAmount), getWorldObj.provider.dimensionId)
    }
    updateNeeded = false
  }

  override def hasDescription: Boolean = tanksToSync.nonEmpty

  override def saveToDescriptionCompound(compound: NBTTagCompound): Unit = {
    super.saveToDescriptionCompound(compound)
    if (tanksToSync.isEmpty) return
    val tankList = new NBTTagList
    tanksToSync.foreach { id =>
      val tankComp = new NBTTagCompound
      tankComp.setInteger("index", id)
      tankComp.setInteger("capacity", tanks(id).getCapacity)
      tanks(id).writeToNBT(tankComp)
      tankList.appendTag(tankComp)
    }
    compound.setTag("tanks", tankList)
  }

  override def handleDescriptionNBT(compound: NBTTagCompound): Unit = {
    super.handleDescriptionNBT(compound)
    if (!compound.hasKey("tanks")) return
    val tankList = compound.getTagList("tanks", 10)
    for (id <- 0 until tankList.tagCount()) {
      val tankComp = tankList.getCompoundTagAt(id)
      tanks.padTo(tankComp.getInteger("index") + 1, null)
      tanks(tankComp.getInteger("index")) = new FluidTank(tankComp.getInteger("capacity"))
      tanks(tankComp.getInteger("index")).readFromNBT(tankComp)
    }
  }

  def setUpdateTanks() = updateNeeded = true

  def setTanksToSync(xs: Int*): Unit = tanksToSync = xs

}
