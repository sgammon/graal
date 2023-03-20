/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.graalvm.wasm.debugging;

import com.oracle.truffle.api.frame.MaterializedFrame;
import org.graalvm.wasm.debugging.data.DebugAddressSize;
import org.graalvm.wasm.debugging.parser.DebugParser;
import org.graalvm.wasm.nodes.WasmDataAccess;

import java.util.Objects;

/**
 * Represents a location descriptor. The value consist of a type (local variable, global variable,
 * value stack, memory) and a corresponding address. This is needed to retrieve the value of a debug
 * object.
 */
public final class DebugLocation {
    private static final byte LOCAL = 1;
    private static final byte GLOBAL = 2;
    private static final byte STACK = 3;
    private static final byte MEMORY = 4;

    private final MaterializedFrame frame;
    private final WasmDataAccess dataAccess;

    private final long address;
    private final byte type;

    private final DebugLocation frameBase;
    private final DebugAddressSize addressSize;

    private DebugLocation(long address, byte type, MaterializedFrame frame, WasmDataAccess dataAccess, DebugLocation frameBase, DebugAddressSize addressSize) {
        this.address = address;
        this.type = type;
        this.frame = frame;
        this.dataAccess = dataAccess;
        this.frameBase = frameBase;
        this.addressSize = addressSize;
    }

    /**
     * For internal use only. See {@link DebugParser#readExpression(byte[], DebugLocation)}.
     */
    public static DebugLocation createFrameBase(MaterializedFrame frame, WasmDataAccess dataAccess, byte[] frameBaseExpression) {
        final DebugLocation location = DebugParser.readFrameBaseExpression(frameBaseExpression, frame, dataAccess, DebugAddressSize.I32).loadAsLocation();
        return new DebugLocation(location.address, location.type, location.frame, location.dataAccess, location, location.addressSize);
    }

    /**
     * For internal use only. See {@link DebugParser#readExpression(byte[], DebugLocation)}.
     */
    public static DebugLocation createLocalAccess(int address, MaterializedFrame frame, WasmDataAccess dataAccess, DebugLocation frameBase, DebugAddressSize addressSize) {
        return new DebugLocation(address, LOCAL, frame, dataAccess, frameBase, addressSize);
    }

    /**
     * For internal use only. See {@link DebugParser#readExpression(byte[], DebugLocation)}.
     */
    public static DebugLocation createGlobalAccess(int address, MaterializedFrame frame, WasmDataAccess dataAccess, DebugLocation frameBase, DebugAddressSize addressSize) {
        return new DebugLocation(address, GLOBAL, frame, dataAccess, frameBase, addressSize);
    }

    /**
     * For internal use only. See {@link DebugParser#readExpression(byte[], DebugLocation)}.
     */
    public static DebugLocation createStackAccess(int address, MaterializedFrame frame, WasmDataAccess dataAccess, DebugLocation frameBase, DebugAddressSize addressSize) {
        return new DebugLocation(address, STACK, frame, dataAccess, frameBase, addressSize);
    }

    /**
     * For internal use only. See {@link DebugParser#readExpression(byte[], DebugLocation)}.
     */
    public static DebugLocation createMemoryAccess(long address, MaterializedFrame frame, WasmDataAccess dataAccess, DebugLocation frameBase, DebugAddressSize addressSize) {
        return new DebugLocation(address, MEMORY, frame, dataAccess, frameBase, addressSize);
    }

    private static DebugLocation createInvalid(DebugLocation frameBase, MaterializedFrame frame, WasmDataAccess dataAccess, DebugAddressSize addressSize) {
        return new DebugLocation(-1, MEMORY, frame, dataAccess, frameBase, addressSize);
    }

    /**
     * @return The frame used for this location. For internal use only. See
     *         {@link DebugParser#readExpression(byte[], DebugLocation)}.
     */
    public MaterializedFrame frame() {
        return frame;
    }

    /**
     * @return The data access of this location. For internal use only. See
     *         {@link DebugParser#readExpression(byte[], DebugLocation)}.
     */

    public WasmDataAccess dataAccess() {
        return dataAccess;
    }

    /**
     * 
     * @return The frame base of this location. For internal use only. See
     *         {@link DebugParser#readExpression(byte[], DebugLocation)}.
     */
    public DebugLocation frameBase() {
        return frameBase;
    }

    /**
     * 
     * @return The address size of this location. For internal use only. See
     *         {@link DebugParser#readExpression(byte[], DebugLocation)}.
     */
    public DebugAddressSize addressSize() {
        return addressSize;
    }

    private boolean isLocal() {
        return type == LOCAL;
    }

    private boolean isGlobal() {
        return type == GLOBAL;
    }

    private boolean isStack() {
        return type == STACK;
    }

    private boolean isMemory() {
        return type == MEMORY;
    }

    /**
     * 
     * @return A duplicate of this location with the address moved by one byte.
     */
    public DebugLocation nextByte() {
        return addOffset(1);
    }

    /**
     * 
     * @return A duplicate of this location with the address moved by two bytes.
     */
    public DebugLocation nextShort() {
        return addOffset(2);
    }

    /**
     * @return A duplicate of this location with the address moved by four bytes.
     */
    public DebugLocation nextInt() {
        return addOffset(4);
    }

    /**
     * @return A duplicate of this location with the address moved by eight bytes.
     */
    public DebugLocation nextLong() {
        return addOffset(8);
    }

    /**
     * @param offset the number of bytes to add to this location.
     * @return A duplicate of this location with the address moved by the given number of bytes.
     */

    public DebugLocation addOffset(long offset) {
        if (isInvalid()) {
            return this;
        }
        if (isMemory()) {
            return new DebugLocation(address + offset, type, frame, dataAccess, Objects.requireNonNullElse(frameBase, this), addressSize);
        } else {
            if (frameBase == null) {
                final DebugLocation location = loadAsLocation();
                return new DebugLocation(location.address + offset, location.type, location.frame, location.dataAccess, location, location.addressSize);
            }
            return loadAsLocation().addOffset(offset);
        }
    }

    /**
     * @return If this location represents an invalid address.
     */
    public boolean isInvalid() {
        return address < 0 || ((isStack() || isLocal() || isGlobal()) && !isIntAddress());
    }

    private static int shiftInt(int value, int bitSize, int bitOffset) {
        return (value << bitOffset) >> (32 - bitSize);
    }

    private static int shiftUnsignedInt(int value, int bitSize, int bitOffset) {
        return (value << bitOffset) >>> (32 - bitSize);
    }

    private static long shiftLong(long value, int bitSize, int bitOffset) {
        return (value << bitOffset) >> (64 - bitSize);
    }

    private static long shiftUnsignedLong(long value, int bitSize, int bitOffset) {
        return (value << bitOffset) >>> (64 - bitSize);
    }

    private static int doubleShiftUnsignedInt(int value, int left, int right) {
        return (value << left) >>> right;
    }

    private static int doubleShiftInt(int value, int left, int right) {
        return (value << left) >> right;
    }

    private static long doubleShiftUnsignedLong(long value, int left, int right) {
        return (value << left) >>> right;
    }

    private static long doubleShiftLong(long value, int left, int right) {
        return (value << left) >> right;
    }

    private boolean isIntAddress() {
        return address < Integer.MAX_VALUE;
    }

    /**
     * Loads the signed byte value at this location.
     * 
     * @throws WasmDebugException if this location is invalid.
     * 
     * @see #isInvalid()
     */
    public byte loadI8() {
        if (isStack() && isIntAddress()) {
            return (byte) dataAccess.loadI32FromStack(frame, (int) address);
        }
        if (isLocal() && isIntAddress()) {
            return (byte) dataAccess.loadI32FromLocals(frame, (int) address);
        }
        if (isGlobal() && isIntAddress()) {
            return (byte) dataAccess.loadI32FromGlobals((int) address);
        }
        if (isMemory()) {
            return dataAccess.loadI8FromMemory(address);
        }
        throw new WasmDebugException("Unable to read byte value at " + this);
    }

    /**
     * Loads the signed byte value at this location.
     *
     * @param bitSize the bit size
     * @param bitOffset the bit offset
     *
     * @throws WasmDebugException if this location is invalid
     * 
     * @see #isInvalid()
     */
    public byte loadI8(int bitSize, int bitOffset) {
        final byte value = loadI8();
        if (bitSize >= 0) {
            if (bitOffset < 0) {
                final byte upper = nextByte().loadI8();
                final int upperValue = doubleShiftInt(upper, 32 + bitOffset, bitSize + bitOffset + 24);
                final int lowerValue = value >>> (8 - (bitSize + bitOffset));
                return (byte) (upperValue | lowerValue);
            } else {
                return (byte) shiftInt(value, bitSize, bitOffset + 24);
            }
        }
        return value;
    }

    /**
     * Loads the unsigned byte at this location.
     * 
     * @param bitSize the bit size
     * @param bitOffset the bit offset
     * 
     * @throws WasmDebugException if this location is invalid.
     * 
     * @see #isInvalid()
     */
    public int loadU8(int bitSize, int bitOffset) {
        final byte value = loadI8();
        if (bitSize < 0) {
            return value & 0xff;
        }
        if (bitOffset < 0) {
            final byte upper = nextByte().loadI8();
            final int upperValue = doubleShiftUnsignedInt(upper, 32 + bitOffset, 32 - bitSize);
            final int lowerValue = (value & 0xff) >>> (8 - (bitSize + bitOffset));
            return (upperValue | lowerValue) & 0xff;
        } else {
            return shiftUnsignedInt(value, bitSize, bitOffset + 24) & 0xff;
        }
    }

    /**
     * Loads the unsigned byte at this location.
     *
     * @throws WasmDebugException if this location is invalid.
     * 
     * @see #isInvalid()
     */
    public int loadU8() {
        return loadU8(-1, 0);
    }

    /**
     * Loads the signed short value at this location.
     * 
     * @throws WasmDebugException if this location is invalid.
     * 
     * @see #isInvalid()
     */
    public short loadI16() {
        if (isStack() && isIntAddress()) {
            return (short) dataAccess.loadI32FromStack(frame, (int) address);
        }
        if (isLocal() && isIntAddress()) {
            return (short) dataAccess.loadI32FromLocals(frame, (int) address);
        }
        if (isGlobal() && isIntAddress()) {
            return (short) dataAccess.loadI32FromGlobals((int) address);
        }
        if (isMemory()) {
            return dataAccess.loadI16FromMemory(address);
        }
        throw new WasmDebugException("Unable to read short value from " + this);
    }

    /**
     * Loads the signed short value at this location.
     * 
     * @param bitSize the bit size
     * @param bitOffset the bit offset
     * 
     * @throws WasmDebugException if this location is invalid.
     * 
     * @see #isInvalid()
     */
    public short loadI16(int bitSize, int bitOffset) {
        final short value = loadI16();
        if (bitSize < 0) {
            return value;
        }
        if (bitOffset < 0) {
            final short upper = nextShort().loadI16();
            final int upperValue = doubleShiftInt(upper, 32 + bitOffset, 32 - bitSize);
            final int lowerValue = value >>> (16 - (bitSize + bitOffset));
            return (short) (upperValue | lowerValue);
        } else {
            return (short) shiftInt(value, bitSize, bitOffset + 16);
        }
    }

    /**
     * Loads the unsigned short value at this location.
     * 
     * @param bitSize the bit size
     * @param bitOffset the bit offset
     * @throws WasmDebugException if this location is invalid.
     * 
     * @see #isInvalid()
     */
    public int loadU16(int bitSize, int bitOffset) {
        final short value = loadI16();
        if (bitSize < 0) {
            return value & 0xffff;
        }
        if (bitOffset < 0) {
            final short upper = nextShort().loadI16();
            final int upperValue = doubleShiftUnsignedInt(upper, 32 + bitOffset, 32 - bitSize);
            final int lowerValue = (value & 0xffff) >>> (16 - (bitSize + bitOffset));
            return (upperValue | lowerValue) & 0xffff;
        } else {
            return shiftUnsignedInt(value, bitSize, bitOffset + 16) & 0xffff;
        }
    }

    /**
     * Loads the signed integer at this location.
     * 
     * @throws WasmDebugException if this location is invalid.
     * 
     * @see #isInvalid()
     */
    public int loadI32() {
        if (isStack() && isIntAddress()) {
            return dataAccess.loadI32FromStack(frame, (int) address);
        }
        if (isLocal() && isIntAddress()) {
            return dataAccess.loadI32FromLocals(frame, (int) address);
        }
        if (isGlobal() && isIntAddress()) {
            return dataAccess.loadI32FromGlobals((int) address);
        }
        if (isMemory()) {
            return dataAccess.loadI32FromMemory(address);
        }
        throw new WasmDebugException("Unable to read int value from " + this);
    }

    /**
     * Loads the signed integer at this location.
     * 
     * @param bitSize the bit size
     * @param bitOffset the bit offset
     * 
     * @throws WasmDebugException if this location is invalid.
     * 
     * @see #isInvalid()
     */
    public int loadI32(int bitSize, int bitOffset) {
        final int value = loadI32();
        if (bitSize < 0) {
            return value;
        }
        if (bitOffset < 0) {
            final int upper = nextInt().loadI32();
            final int upperValue = doubleShiftInt(upper, 32 + bitOffset, 32 - bitSize);
            final int lowerValue = value >>> (32 - (bitSize + bitOffset));
            return upperValue | lowerValue;
        } else {
            return shiftInt(value, bitSize, bitOffset);
        }
    }

    /**
     * Loads the unsigned integer at this location.
     * 
     * @param bitSize the bit size
     * @param bitOffset the bit offset
     * 
     * @throws WasmDebugException if this location is invalid.
     * 
     * @see #isInvalid()
     */
    public long loadU32(int bitSize, int bitOffset) {
        final int value = loadI32();
        if (bitSize < 0) {
            return Integer.toUnsignedLong(value);
        }
        if (bitOffset < 0) {
            final int upper = nextInt().loadI32();
            final int upperValue = doubleShiftUnsignedInt(upper, 32 + bitOffset, 32 - bitSize);
            final int lowerValue = value >>> (32 - (bitSize + bitOffset));
            return Integer.toUnsignedLong(upperValue | lowerValue);
        } else {
            return Integer.toUnsignedLong(shiftUnsignedInt(value, bitSize, bitOffset));
        }
    }

    /**
     * Loads the unsigned integer at this location.
     * 
     * @throws WasmDebugException if this location is invalid.
     * 
     * @see #isInvalid()
     */
    public long loadU32() {
        return loadU32(-1, 0);
    }

    /**
     * Loads the signed long at this location.
     *
     * @throws WasmDebugException if this location is invalid.
     *
     * @see #isInvalid()
     */
    public long loadI64() {
        if (isStack() && isIntAddress()) {
            return dataAccess.loadI64FromStack(frame, (int) address);
        }
        if (isLocal() && isIntAddress()) {
            return dataAccess.loadI64FromLocals(frame, (int) address);
        }
        if (isGlobal() && isIntAddress()) {
            return dataAccess.loadI64FromGlobals((int) address);
        }
        if (isMemory()) {
            return dataAccess.loadI64FromMemory(address);
        }
        throw new WasmDebugException("Unable to read long value from " + this);
    }

    /**
     * Loads the signed long at this location.
     * 
     * @param bitSize the bit size
     * @param bitOffset the bit offset
     *
     * @throws WasmDebugException if this location is invalid.
     *
     * @see #isInvalid()
     */
    public long loadI64(int bitSize, int bitOffset) {
        final long value = loadI64();
        if (bitSize < 0) {
            return value;
        }
        if (bitOffset < 0) {
            final long upper = nextLong().loadI64();
            final long upperValue = doubleShiftLong(upper, 64 + bitOffset, 64 - bitSize);
            final long lowerValue = value >>> (64 - (bitSize + bitOffset));
            return upperValue | lowerValue;
        } else {
            return shiftLong(value, bitSize, bitOffset);
        }
    }

    /**
     * Loads the unsigned long at this location.
     * 
     * @param bitSize the bit size
     * @param bitOffset the bit offset
     *
     * @throws WasmDebugException if this location is invalid.
     *
     * @see #isInvalid()
     */
    public String loadU64(int bitSize, int bitOffset) {
        final long value = loadI64();
        if (bitSize < 0) {
            return Long.toUnsignedString(value);
        }
        if (bitOffset < 0) {
            final long upper = nextLong().loadI64();
            long upperValue = doubleShiftUnsignedLong(upper, 64 + bitOffset, 64 - bitSize);
            long lowerValue = value >>> (64 - (bitSize + bitOffset));
            return Long.toUnsignedString(upperValue | lowerValue);
        } else {
            return Long.toUnsignedString(shiftUnsignedLong(value, bitSize, bitOffset));
        }
    }

    /**
     * Loads the float at this location.
     *
     * @throws WasmDebugException if this location is invalid.
     *
     * @see #isInvalid()
     */
    public float loadF32() {
        if (isStack() && isIntAddress()) {
            return dataAccess.loadF32FromStack(frame, (int) address);
        }
        if (isLocal() && isIntAddress()) {
            return dataAccess.loadF32FromLocals(frame, (int) address);
        }
        if (isGlobal() && isIntAddress()) {
            return dataAccess.loadF32FromGlobals((int) address);
        }
        if (isMemory()) {
            return dataAccess.loadF32FromMemory(address);
        }
        throw new WasmDebugException("Unable to load float value from " + this);
    }

    /**
     * Loads the double at this location.
     * 
     * @throws WasmDebugException if this location is invalid.
     *
     * @see #isInvalid()
     */
    public double loadF64() {
        if (isStack() && isIntAddress()) {
            return dataAccess.loadF64FromStack(frame, (int) address);
        }
        if (isLocal() && isIntAddress()) {
            return dataAccess.loadF64FromLocals(frame, (int) address);
        }
        if (isGlobal() && isIntAddress()) {
            return dataAccess.loadF64FromGlobals((int) address);
        }
        if (isMemory()) {
            return dataAccess.loadF64FromMemory(address);
        }
        throw new WasmDebugException("Unable to load double value from " + this);
    }

    /**
     * Loads the string at this location.
     * 
     * @param length the length of the string
     * 
     * @throws WasmDebugException if this location is invalid.
     *
     * @see #isInvalid()
     */
    public String loadString(int length) {
        if (isInvalid()) {
            throw new WasmDebugException("Invalid location");
        }
        // Make sure non memory locations are resolved.
        return addOffset(0).loadStringFromMemory(length);
    }

    private String loadStringFromMemory(int length) {
        if (isMemory()) {
            return dataAccess.loadStringFromMemory(address, length);
        }
        throw new WasmDebugException("Unable to read string value from " + this);
    }

    /**
     * Loads the location value at this location.
     */
    public DebugLocation loadAsLocation() {
        if (isInvalid()) {
            return this;
        }
        if (addressSize == DebugAddressSize.I32) {
            return createMemoryAccess(loadI32(), frame, dataAccess, frameBase, addressSize);
        }
        if (addressSize == DebugAddressSize.I64) {
            return createMemoryAccess(loadI64(), frame, dataAccess, frameBase, addressSize);
        }
        return createInvalid(frameBase, frame, dataAccess, addressSize);
    }

    /**
     * Invalidates this location.
     */
    public DebugLocation invalidate() {
        return createInvalid(frameBase, frame, dataAccess, addressSize);
    }

    /**
     * @return Whether the address of this location is zero or not.
     */
    public boolean isZero() {
        return address == 0L;
    }

    @Override
    public String toString() {
        if (isStack()) {
            return "operand stack value " + address;
        }
        if (isLocal()) {
            return "local " + address;
        }
        if (isGlobal()) {
            return "global " + address;
        }
        return "0x" + Long.toHexString(address);
    }
}
