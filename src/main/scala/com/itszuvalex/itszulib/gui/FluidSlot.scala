package com.itszuvalex.itszulib.gui


import com.itszuvalex.itszulib.core.TileEntityBase
import com.itszuvalex.itszulib.core.traits.tile.{TileMultiFluidTank, TileFluidTank}
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.fluids.{FluidContainerRegistry, IFluidContainerItem, FluidStack, FluidTank}


/**
 * Created by Alex on 10.10.2015.
 */
class FluidSlot (
                  private var slotIndex: Int = 0,
                  var tankId: Int = -1,
                  var tileSingleTank: TileFluidTank = null,
                  var tileMultiTank: TileMultiFluidTank = null,
                  var xDisplayPosition: Int = 0,
                  var yDisplayPosition: Int = 0 ) {

  var slotNumber: Int = 0

  def this(tank: TileFluidTank, slotnum: Int, xPos: Int, yPos: Int) = this(slotnum, -1, tank, null, xPos, yPos)

  def this(tank: TileMultiFluidTank, tankid: Int, slotnum: Int, xPos: Int, yPos: Int) = this(slotnum, tankid, null, tank, xPos, yPos)

  def getStack: FluidStack = if (tankId == -1) tileSingleTank.tank.getFluid else tileMultiTank.tanks(tankId).getFluid

  def hasStack: Boolean = getStack != null

  def getTooltip: List[String] = {
    var ret = List.empty[String]
    tankId match {
      case -1 =>
        ret :+= ("Fluid: " + (if (tileSingleTank.tank.getFluid == null) "None" else tileSingleTank.tank.getFluid.getFluid.getLocalizedName(tileSingleTank.tank.getFluid) + ", " + tileSingleTank.tank.getFluidAmount + "mB"))
        ret :+= ("Capacity: " + tileSingleTank.tank.getCapacity + "mB")
      case _ =>
        ret :+= ("Fluid: " + (if (tileMultiTank.tanks(tankId).getFluid == null) "None" else tileMultiTank.tanks(tankId).getFluid.getFluid.getLocalizedName(tileMultiTank.tanks(tankId).getFluid) + ", " + tileMultiTank.tanks(tankId).getFluidAmount + "mB"))
        ret :+= ("Capacity: " + tileMultiTank.tanks(tankId).getCapacity + "mB")
    }
    ret :+= (EnumChatFormatting.ITALIC + EnumChatFormatting.AQUA.toString + "Left click with fluid container to fill tank" + EnumChatFormatting.RESET)
    ret :+= (EnumChatFormatting.ITALIC + EnumChatFormatting.AQUA.toString + "Right click with fluid container to drain tank" + EnumChatFormatting.RESET)
    ret
  }

  /**
   * Handles filling/emptying of tank on click.
   * @param item ItemStack currently in player's "hand"
   * @param clickedButton Mouse button clicked
   * @return ItemStack that should be in the player's "hand" afterwards
   */
  def onClick(item: ItemStack, clickedButton: Int): ItemStack = {
    if (item == null) return item
    if (!FluidContainerRegistry.isContainer(item)) return onClickedWithAltItem(item, clickedButton)
    tankId match {
      case -1 =>
        clickedButton match {
          case 0 =>
            if (!FluidContainerRegistry.isFilledContainer(item)) return item
            val itemFluid = FluidContainerRegistry.getFluidForFilledItem(item)
            val amt = tileSingleTank.tank.fill(itemFluid, false)
            if (amt < itemFluid.amount) return item
            tileSingleTank.tank.fill(itemFluid, true)
            tileSingleTank.setUpdateTank()
            FluidContainerRegistry.drainFluidContainer(item)
          case 1 =>
            if (!FluidContainerRegistry.isEmptyContainer(item)) return item
            val itemFluidCap = FluidContainerRegistry.getContainerCapacity(tileSingleTank.tank.getFluid, item)
            if (itemFluidCap == 0) return item
            if (tileSingleTank.tank.getFluidAmount < itemFluidCap) return item
            tileSingleTank.setUpdateTank()
            FluidContainerRegistry.fillFluidContainer(tileSingleTank.tank.drain(itemFluidCap, true), item)
        }
      case _ =>
        clickedButton match {
          case 0 =>
            if (!FluidContainerRegistry.isFilledContainer(item)) return item
            val itemFluid = FluidContainerRegistry.getFluidForFilledItem(item)
            val amt = tileMultiTank.tanks(tankId).fill(itemFluid, false)
            if (amt < itemFluid.amount) return item
            tileMultiTank.tanks(tankId).fill(itemFluid, true)
            tileMultiTank.setUpdateTanks()
            FluidContainerRegistry.drainFluidContainer(item)
          case 1 =>
            if (!FluidContainerRegistry.isEmptyContainer(item)) return item
            val itemFluidCap = FluidContainerRegistry.getContainerCapacity(tileMultiTank.tanks(tankId).getFluid, item)
            if (itemFluidCap == 0) return item
            if (tileMultiTank.tanks(tankId).getFluidAmount < itemFluidCap) return item
            tileMultiTank.setUpdateTanks()
            FluidContainerRegistry.fillFluidContainer(tileMultiTank.tanks(tankId).drain(itemFluidCap, true), item)
        }
    }
  }

  def onClickedWithAltItem(item: ItemStack, clickedButton: Int): ItemStack = {
    if (!item.getItem.isInstanceOf[IFluidContainerItem]) return item
    val item2 = item.getItem.asInstanceOf[IFluidContainerItem]
    tankId match {
      case -1 =>
        clickedButton match {
          case 0 =>
            if (tileSingleTank.tank.getCapacity - tileSingleTank.tank.getFluidAmount == 0) return item
            if (item2.getFluid(item) == null) return item
            tileSingleTank.tank.fill(item2.drain(item, tileSingleTank.tank.getCapacity - tileSingleTank.tank.getFluidAmount, true), true)
            tileSingleTank.setUpdateTank()
            item
          case 1 =>
            if (tileSingleTank.tank.getFluidAmount == 0) return item
            if (item2.getFluid(item).getFluid != tileSingleTank.tank.getFluid.getFluid) return item
            val amt = item2.fill(item, tileSingleTank.tank.getFluid, false)
            item2.fill(item, tileSingleTank.tank.drain(amt, true), true)
            tileSingleTank.setUpdateTank()
            item
        }
      case _ =>
        clickedButton match {
          case 0 =>
            if (tileMultiTank.tanks(tankId).getCapacity - tileMultiTank.tanks(tankId).getFluidAmount == 0) return item
            if (item2.getFluid(item) == null) return item
            tileMultiTank.tanks(tankId).fill(item2.drain(item, tileMultiTank.tanks(tankId).getCapacity - tileMultiTank.tanks(tankId).getFluidAmount, true), true)
            tileMultiTank.setUpdateTanks()
            item
          case 1 =>
            if (tileMultiTank.tanks(tankId).getFluidAmount == 0) return item
            if (item2.getFluid(item).getFluid != tileMultiTank.tanks(tankId).getFluid.getFluid) return item
            val amt = item2.fill(item, tileMultiTank.tanks(tankId).getFluid, false)
            item2.fill(item, tileMultiTank.tanks(tankId).drain(amt, true), true)
            tileMultiTank.setUpdateTanks()
            item
        }
    }
  }
}

