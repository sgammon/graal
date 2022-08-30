/*
 * Copyright (c) 2022, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.dsl.processor.operations.instructions;

import com.oracle.truffle.dsl.processor.java.model.CodeTree;
import com.oracle.truffle.dsl.processor.java.model.CodeTreeBuilder;
import com.oracle.truffle.dsl.processor.operations.OperationsContext;

public class StoreLocalInstruction extends Instruction {
    private final OperationsContext context;

    public StoreLocalInstruction(OperationsContext context, int id) {
        super("store.local", id, 0);
        this.context = context;

        addPopSimple("value");
        addLocal("target");
    }

    @Override
    public CodeTree createExecuteCode(ExecutionVariables vars) {
        CodeTreeBuilder b = CodeTreeBuilder.createBuilder();

        b.startAssign("int localIdx");
        b.tree(createLocalIndex(vars, 0, false));
        b.end();

        b.startAssign("int sourceSlot").variable(vars.sp).string(" - 1").end();

        createCopyObject(vars, b);

        b.startStatement().variable(vars.sp).string("--").end();

        return b.build();
    }

    private static final boolean USE_SPEC_FRAME_COPY = true;

    private static void createCopyPrimitive(ExecutionVariables vars, CodeTreeBuilder b) {
        b.startStatement().startCall("UFA", USE_SPEC_FRAME_COPY ? "unsafeCopyPrimitive" : "unsafeCopy");
        b.variable(vars.frame);
        b.string("sourceSlot");
        b.string("localIdx");
        b.end(2);
    }

    private static void createCopyObject(ExecutionVariables vars, CodeTreeBuilder b) {
        b.startStatement().startCall("UFA", USE_SPEC_FRAME_COPY ? "unsafeCopyObject" : "unsafeCopy");
        b.variable(vars.frame);
        b.string("sourceSlot");
        b.string("localIdx");
        b.end(2);
    }

    private static void createSetSlotKind(ExecutionVariables vars, CodeTreeBuilder b, String tag) {
        b.startStatement().startCall(vars.frame, "getFrameDescriptor().setSlotKind");
        b.string("localIdx");
        b.string(tag);
        b.end(2);
    }

    private void createSetChildBoxing(ExecutionVariables vars, CodeTreeBuilder b, String tag) {
        b.startStatement().startCall("doSetResultBoxed");
        b.variable(vars.bc);
        b.variable(vars.bci);
        b.startGroup().tree(createPopIndexedIndex(vars, 0, false)).end();
        b.string(tag);
        b.end(2);
    }

    private static void createCopyAsObject(ExecutionVariables vars, CodeTreeBuilder b) {
        b.startStatement().startCall(vars.frame, "setObject");
        b.string("localIdx");
        b.startCall("expectObject").variable(vars.frame).string("sourceSlot").end();
        b.end(2);
    }

    @Override
    public BoxingEliminationBehaviour boxingEliminationBehaviour() {
        return BoxingEliminationBehaviour.DO_NOTHING;
    }

    @Override
    public CodeTree createPrepareAOT(ExecutionVariables vars, CodeTree language, CodeTree root) {
        return null;
    }

    @Override
    public boolean neverInUncached() {
        return false;
    }
}