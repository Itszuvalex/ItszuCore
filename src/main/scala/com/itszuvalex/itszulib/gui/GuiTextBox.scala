package com.itszuvalex.itszulib.gui

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.{FontRenderer, Gui, GuiScreen}
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ChatAllowedCharacters
import org.lwjgl.opengl.GL11

/**
  * Created by Christopher Harris (Itszuvalex) on 2/1/2016.
  */
@SideOnly(Side.CLIENT)
class GuiTextBox(private val fontRenderer: FontRenderer, anchorX: Int, anchorY: Int, width: Int, height: Int) extends GuiButton(anchorX, anchorY, width, height) {
  /** Has the current text being edited on the textbox. */
  private var textString              = ""
  private var maxStringLength         = 32
  private var cursorCounter           = 0
  private var enableBackgroundDrawing = true
  /** if true the textbox can lose focus by clicking elsewhere on the screen */
  private var canLoseFocus            = true
  /** If this value is true along with isEnabled, keyTyped will process the keys. */
  private var focused                 = false
  /** If this value is true along with isFocused, keyTyped will process the keys. */
  private var isEnabled               = true
  /** The current character index that should be used as start of the rendered text. */
  private var lineScrollOffset        = 0
  private var cursorPosition          = 0
  /** other selection position, maybe the same as the cursor */
  private var selectionEnd            = 0
  private var enabledColor            = 14737632
  private var disabledColor           = 7368816
  /** True if this textbox is visible */
  private var visible                 = true

  /**
    * Increments the cursor counter
    */
  def updateCursorCounter() = this.cursorCounter += 1

  /**
    * Returns the contents of the textbox
    */
  def getText = textString


  /**
    * Sets the text of the textbox
    */
  def setText(text: String) {
    if (text.length > this.maxStringLength) {
      this.textString = text.substring(0, this.maxStringLength)
    }
    else {
      this.textString = text
    }
    this.setCursorPositionEnd()
  }

  /**
    * sets the cursors position to after the text
    */
  def setCursorPositionEnd() = setCursorPosition(textString.length)

  /**
    * returns the text between the cursor and selectionEnd
    */
  def getSelectedText = {
    val i = if (cursorPosition < selectionEnd) cursorPosition else selectionEnd
    val j = if (cursorPosition < selectionEnd) selectionEnd else cursorPosition
    textString.substring(i, j)
  }

  /**
    * replaces selected text, or inserts text at the position on the cursor
    */
  def writeText(itext: String) {
    var s1 = ""
    val s2 = ChatAllowedCharacters.filerAllowedCharacters(itext)
    val i = if (cursorPosition < selectionEnd) cursorPosition else selectionEnd
    val j = if (cursorPosition < selectionEnd) selectionEnd else cursorPosition
    val k = maxStringLength - textString.length - (i - selectionEnd)
    val flag = false
    if (textString.length > 0) {
      s1 = s1 + textString.substring(0, i)
    }
    var l = 0
    if (k < s2.length) {
      s1 = s1 + s2.substring(0, k)
      l = k
    }
    else {
      s1 = s1 + s2
      l = s2.length
    }
    if (textString.length > 0 && j < textString.length) {
      s1 = s1 + textString.substring(j)
    }
    textString = s1
    moveCursorBy(i - selectionEnd + l)
  }

  /**
    * Deletes the specified number of words starting at the cursor position. Negative numbers will delete words left of
    * the cursor.
    */
  def deleteWords(number: Int) {
    if (textString.length != 0) {
      if (selectionEnd != cursorPosition) {
        writeText("")
      }
      else {
        deleteFromCursor(getNthWordFromCursor(number) - cursorPosition)
      }
    }
  }

  /**
    * delete the selected text, otherwsie deletes characters from either side of the cursor. params: delete num
    */
  def deleteFromCursor(number: Int) {
    if (textString.length != 0) {
      if (selectionEnd != cursorPosition) {
        writeText("")
      }
      else {
        val flag: Boolean = number < 0
        val j = if (flag) cursorPosition + number else cursorPosition
        val k = if (flag) cursorPosition else cursorPosition + number
        var s: String = ""
        if (j >= 0) {
          s = textString.substring(0, j)
        }
        if (k < textString.length) {
          s = s + textString.substring(k)
        }
        textString = s
        if (flag) {
          moveCursorBy(number)
        }
      }
    }
  }

  /**
    * see @getNthNextWordFromPos() params: N, position
    */
  def getNthWordFromCursor(num: Int) = getNthWordFromPos(num, getCursorPosition)

  /**
    * gets the position of the nth word. N may be negative, then it looks backwards. params: N, position
    */
  def getNthWordFromPos(num: Int, pos: Int): Int = func_146197_a(num, getCursorPosition, true)

  def func_146197_a(p_146197_1_ : Int, p_146197_2_ : Int, p_146197_3_ : Boolean): Int = {
    var k: Int = p_146197_2_
    val flag1: Boolean = p_146197_1_ < 0
    val l: Int = Math.abs(p_146197_1_)
    var i1: Int = 0
    (0 until l).foreach { i1 =>
      while (i1 < l) {

        if (flag1) {
          while (p_146197_3_ && k > 0 && this.textString.charAt(k - 1) == 32) {
            k -= 1
          }
          while (k > 0 && this.textString.charAt(k - 1) != 32) {
            k -= 1
          }
        }
        else {
          val j1: Int = this.textString.length
          k = this.textString.indexOf(32, k)
          if (k == -1) {
            k = j1
          }
          else {
            while (p_146197_3_ && k < j1 && this.textString.charAt(k) == 32) {
              k += 1
            }
          }
        }
      }
                        }
    return k
  }

  /**
    * Moves the text cursor by a specified number of characters and clears the selection
    */
  def moveCursorBy(num: Int) = setCursorPosition(this.selectionEnd + num)

  /**
    * sets the cursors position to the beginning
    */
  def setCursorPositionZero() = setCursorPosition(0)

  /**
    * Call this method from your GuiScreen to process the keys into the textbox
    */
  def textboxKeyTyped(p_146201_1_ : Char, p_146201_2_ : Int): Boolean = {
    if (!isFocused) {
      return false
    }
    else {
      p_146201_1_ match {
        case 1 =>
          this.setCursorPositionEnd
          this.setSelectionPos(0)
          return true
        case 3 =>
          GuiScreen.setClipboardString(this.getSelectedText)
          return true
        case 22 =>
          if (this.isEnabled) {
            this.writeText(GuiScreen.getClipboardString)
          }
          return true
        case 24 =>
          GuiScreen.setClipboardString(this.getSelectedText)
          if (this.isEnabled) {
            this.writeText("")
          }
          return true
        case _ =>
          p_146201_2_ match {
            case 14 =>
              if (GuiScreen.isCtrlKeyDown) {
                if (this.isEnabled) {
                  this.deleteWords(-1)
                }
              }
              else if (this.isEnabled) {
                this.deleteFromCursor(-1)
              }
              return true
            case 199 =>
              if (GuiScreen.isShiftKeyDown) {
                this.setSelectionPos(0)
              }
              else {
                this.setCursorPositionZero
              }
              return true
            case 203 =>
              if (GuiScreen.isShiftKeyDown) {
                if (GuiScreen.isCtrlKeyDown) {
                  this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd))
                }
                else {
                  this.setSelectionPos(this.getSelectionEnd - 1)
                }
              }
              else if (GuiScreen.isCtrlKeyDown) {
                this.setCursorPosition(this.getNthWordFromCursor(-1))
              }
              else {
                this.moveCursorBy(-1)
              }
              return true
            case 205 =>
              if (GuiScreen.isShiftKeyDown) {
                if (GuiScreen.isCtrlKeyDown) {
                  this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd))
                }
                else {
                  this.setSelectionPos(this.getSelectionEnd + 1)
                }
              }
              else if (GuiScreen.isCtrlKeyDown) {
                this.setCursorPosition(this.getNthWordFromCursor(1))
              }
              else {
                this.moveCursorBy(1)
              }
              return true
            case 207 =>
              if (GuiScreen.isShiftKeyDown) {
                this.setSelectionPos(this.textString.length)
              }
              else {
                this.setCursorPositionEnd
              }
              return true
            case 211 =>
              if (GuiScreen.isCtrlKeyDown) {
                if (this.isEnabled) {
                  this.deleteWords(1)
                }
              }
              else if (this.isEnabled) {
                this.deleteFromCursor(1)
              }
              return true
            case _ =>
              if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_)) {
                if (this.isEnabled) {
                  this.writeText(Character.toString(p_146201_1_))
                }
                return true
              }
              else {
                return false
              }
          }
      }
    }
  }

  /**
    * Args: x, y, buttonClicked
    */
  def mouseClicked(p_146192_1_ : Int, p_146192_2_ : Int, p_146192_3_ : Int) {
    val flag: Boolean = p_146192_1_ >= this.anchorX && p_146192_1_ < this.anchorX + this.width && p_146192_2_ >= this.anchorY && p_146192_2_ < this.anchorY + this.height
    if (this.canLoseFocus) {
      this.setFocused(flag)
    }
    if (this.focused && p_146192_3_ == 0) {
      var l: Int = p_146192_1_ - this.anchorX
      if (this.enableBackgroundDrawing) {
        l -= 4
      }
      val s: String = this.fontRenderer.trimStringToWidth(this.textString.substring(this.lineScrollOffset), this.getWidth)
      this.setCursorPosition(this.fontRenderer.trimStringToWidth(s, l).length + this.lineScrollOffset)
    }
  }

  /**
    * sets the position of the cursor to the provided index
    */
  def setCursorPosition(p_146190_1_ : Int) {
    this.cursorPosition = p_146190_1_
    val j: Int = this.textString.length
    if (this.cursorPosition < 0) {
      this.cursorPosition = 0
    }
    if (this.cursorPosition > j) {
      this.cursorPosition = j
    }
    this.setSelectionPos(this.cursorPosition)
  }

  /**
    * Sets the position of the selection anchor (i.e. position the selection was started at)
    */
  def setSelectionPos(inPos: Int) {
    var pos = inPos
    val textLength: Int = textString.length
    if (pos > textLength) {
      pos = textLength
    }
    if (pos < 0) {
      pos = 0
    }
    selectionEnd = pos
    if (fontRenderer != null) {
      if (lineScrollOffset > textLength) {
        lineScrollOffset = textLength
      }
      val k: Int = getWidth
      val s: String = fontRenderer.trimStringToWidth(textString.substring(lineScrollOffset), k)
      val l: Int = s.length + lineScrollOffset
      if (pos == lineScrollOffset) {
        lineScrollOffset -= fontRenderer.trimStringToWidth(textString, k, true).length
      }
      if (pos > l) {
        lineScrollOffset += pos - l
      }
      else if (pos <= lineScrollOffset) {
        lineScrollOffset -= lineScrollOffset - pos
      }
      if (lineScrollOffset < 0) {
        lineScrollOffset = 0
      }
      if (lineScrollOffset > textLength) {
        lineScrollOffset = textLength
      }
    }
  }

  /**
    * Sets focus to this gui element
    */
  def setFocused(focused: Boolean) {
    if (focused && !this.focused) {
      cursorCounter = 0
    }
    this.focused = focused
  }

  /**
    * Draws the textbox
    */
  def drawTextBox() {
    if (this.getVisible) {
      if (this.getEnableBackgroundDrawing) {
        //        drawRect(this.anchorX - 1, this.anchorY - 1, this.anchorX + this.width + 1, this.anchorY + this.height + 1, -6250336)
        //        drawRect(this.anchorX, this.anchorY, this.anchorX + this.width, this.anchorY + this.height, -16777216)
      }
      val i: Int = if (this.isEnabled) this.enabledColor else this.disabledColor
      val j: Int = this.cursorPosition - this.lineScrollOffset
      var k: Int = this.selectionEnd - this.lineScrollOffset
      val s: String = this.fontRenderer.trimStringToWidth(this.textString.substring(this.lineScrollOffset), this.getWidth)
      val flag: Boolean = j >= 0 && j <= s.length
      val flag1: Boolean = this.focused && this.cursorCounter / 6 % 2 == 0 && flag
      val l: Int = if (this.enableBackgroundDrawing) this.anchorX + 4 else this.anchorX
      val i1: Int = if (this.enableBackgroundDrawing) this.anchorY + (this.height - 8) / 2 else this.anchorY
      var j1: Int = l
      if (k > s.length) {
        k = s.length
      }
      if (s.length > 0) {
        val s1: String = if (flag) s.substring(0, j) else s
        j1 = this.fontRenderer.drawStringWithShadow(s1, l, i1, i)
      }
      val flag2: Boolean = this.cursorPosition < this.textString.length || this.textString.length >= this.getMaxStringLength
      var k1: Int = j1
      if (!flag) {
        k1 = if (j > 0) l + this.width else l
      }
      else if (flag2) {
        k1 = j1 - 1
        j1 -= 1
      }
      if (s.length > 0 && flag && j < s.length) {
        this.fontRenderer.drawStringWithShadow(s.substring(j), j1, i1, i)
      }
      if (flag1) {
        if (flag2) {
          Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRenderer.FONT_HEIGHT, -3092272)
        }
        else {
          this.fontRenderer.drawStringWithShadow("_", k1, i1, i)
        }
      }
      if (k != j) {
        val l1: Int = l + this.fontRenderer.getStringWidth(s.substring(0, k))
        this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRenderer.FONT_HEIGHT)
      }
    }
  }

  /**
    * returns the width of the textbox depending on if background drawing is enabled
    */
  def getWidth = if (getEnableBackgroundDrawing) this.panelWidth - 8 else panelWidth

  /**
    * get enable drawing background and outline
    */
  def getEnableBackgroundDrawing = enableBackgroundDrawing

  /**
    * enable drawing background and outline
    */
  def setEnableBackgroundDrawing(enableBackground: Boolean) = enableBackgroundDrawing = enableBackground

  /**
    * draws the vertical line cursor in the textbox
    */
  private def drawCursorVertical(ileftX: Int, itopY: Int, irightX: Int, ibotY: Int) {
    var swap: Int = 0
    var leftX = ileftX
    var rightX = irightX
    var topY = itopY
    var botY = ibotY
    if (leftX < rightX) {
      swap = leftX
      leftX = rightX
      rightX = swap
    }
    if (topY < botY) {
      swap = topY
      topY = botY
      botY = swap
    }
    if (rightX > anchorX + width) {
      rightX = anchorX + width
    }
    if (leftX > anchorX + width) {
      leftX = anchorX + width
    }
    val tessellator: Tessellator = Tessellator.instance
    GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F)
    GL11.glDisable(GL11.GL_TEXTURE_2D)
    GL11.glEnable(GL11.GL_COLOR_LOGIC_OP)
    GL11.glLogicOp(GL11.GL_OR_REVERSE)
    tessellator.startDrawingQuads()
    tessellator.addVertex(leftX.toDouble, botY.toDouble, 0.0D)
    tessellator.addVertex(rightX.toDouble, botY.toDouble, 0.0D)
    tessellator.addVertex(rightX.toDouble, topY.toDouble, 0.0D)
    tessellator.addVertex(leftX.toDouble, topY.toDouble, 0.0D)
    tessellator.draw()
    GL11.glDisable(GL11.GL_COLOR_LOGIC_OP)
    GL11.glEnable(GL11.GL_TEXTURE_2D)
  }

  /**
    * returns the maximum number of character that can be contained in this textbox
    */
  def getMaxStringLength = maxStringLength

  def setMaxStringLength(length: Int) {
    maxStringLength = length
    if (textString.length > length) {
      textString = textString.substring(0, length)
    }
  }

  /**
    * returns true if this textbox is visible
    */
  def getVisible = visible

  /**
    * Sets whether or not this textbox is visible
    */
  def setVisible(visible: Boolean) = this.visible = visible


  /**
    * returns the current position of the cursor
    */
  def getCursorPosition = cursorPosition

  /**
    * Sets the text colour for this textbox (disabled text will not use this colour)
    */
  def setTextColor(color: Int) = enabledColor = color

  def setDisabledTextColour(color: Int) = disabledColor = color

  /**
    * Getter for the focused field
    */
  def isFocused = this.focused

  def setEnabled(enabled: Boolean) = isEnabled = enabled

  /**
    * the side of the selection that is not the cursor, may be the same as the cursor
    */
  def getSelectionEnd = selectionEnd

  /**
    * if true the textbox can lose focus by clicking elsewhere on the screen
    */
  def setCanLoseFocus(value: Boolean) = canLoseFocus = value
}
