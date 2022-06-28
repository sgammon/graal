/*
 * Copyright (c) 2013, 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.svm.core.genscavenge;

import com.oracle.svm.core.genscavenge.parallel.ParallelGCImpl;
import com.oracle.svm.core.heap.ReferenceAccess;
import org.graalvm.compiler.nodes.java.ArrayLengthNode;
import org.graalvm.compiler.options.Option;
import org.graalvm.compiler.word.Word;
import org.graalvm.nativeimage.Platform;
import org.graalvm.nativeimage.Platforms;

import com.oracle.svm.core.annotate.AlwaysInline;
import com.oracle.svm.core.annotate.NeverInline;
import com.oracle.svm.core.heap.ObjectVisitor;
import com.oracle.svm.core.hub.InteriorObjRefWalker;
import com.oracle.svm.core.util.VMError;

/**
 * Run an ObjectReferenceVisitor ({@link GreyToBlackObjRefVisitor}) over any interior object
 * references in the Object, turning this Object from grey to black.
 *
 * This visitor is used during GC and so it must be constructed during native image generation.
 */
public final class GreyToBlackObjectVisitor implements ObjectVisitor {
    private final GreyToBlackObjRefVisitor objRefVisitor;

    @Platforms(Platform.HOSTED_ONLY.class)
    GreyToBlackObjectVisitor(GreyToBlackObjRefVisitor greyToBlackObjRefVisitor) {
        this.objRefVisitor = greyToBlackObjRefVisitor;
    }

    @Override
    @NeverInline("Non-performance critical version")
    public boolean visitObject(Object o) {
        throw VMError.shouldNotReachHere("For performance reasons, this should not be called.");
    }

    @Override
    @NeverInline("GC performance")
    public boolean visitObjectInline(Object o) {
        if (ParallelGCImpl.isEnabled()) {
            /// temp assertions
//            Pointer offsetP = ReferenceAccess.singleton().readObjectAsUntrackedPointer(objRef, compressed);
//            assert ObjectHeaderImpl.isAlignedHeader(offsetP);
            assert GCImpl.getGCImpl().isCompleteCollection();
//            assert innerOffset == 0;

            ParallelGCImpl.QUEUE.put(o);
            if (ParallelGCImpl.WORKERS_COUNT <= 0) {
                ParallelGCImpl.QUEUE.consume(ParallelGCImpl.PROMOTE_TASK);
            }
            return true;
        } else {
            return doVisitObject(o);
        }
    }

    @NeverInline("GC performance")
    public boolean doVisitObject(Object o) {
        ReferenceObjectProcessing.discoverIfReference(o, objRefVisitor);
        InteriorObjRefWalker.walkObjectInline(o, objRefVisitor);
        return true;
    }
}
