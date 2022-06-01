/*
 * Copyright (c) 2011, 2021, Oracle and/or its affiliates. All rights reserved.
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
package org.graalvm.compiler.core.test;

import java.util.List;

import org.graalvm.collections.EconomicMap;
import org.graalvm.compiler.api.directives.GraalDirectives;
import org.graalvm.compiler.debug.DebugContext;
import org.graalvm.compiler.debug.DebugDumpScope;
import org.graalvm.compiler.loop.phases.LoopUnswitchingPhase;
import org.graalvm.compiler.nodes.ControlSplitNode;
import org.graalvm.compiler.nodes.IfNode;
import org.graalvm.compiler.nodes.StructuredGraph;
import org.graalvm.compiler.nodes.StructuredGraph.AllowAssumptions;
import org.graalvm.compiler.nodes.ValueNode;
import org.graalvm.compiler.nodes.extended.SwitchNode;
import org.graalvm.compiler.nodes.loop.DefaultLoopPolicies;
import org.graalvm.compiler.nodes.loop.LoopEx;
import org.graalvm.compiler.nodes.loop.LoopPolicies;
import org.junit.Assert;
import org.junit.Test;

public class LoopUnswitchTest extends GraalCompilerTest {

    public static int referenceSnippet1(int a) {
        int sum = 0;
        if (a > 2) {
            for (int i = 0; i < 1000; i++) {
                sum += 2;
            }
        } else {
            for (int i = 0; i < 1000; i++) {
                sum += a;
            }
        }
        return sum;
    }

    public static int test1Snippet(int a) {
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            if (a > 2) {
                sum += 2;
            } else {
                sum += a;
            }
        }
        return sum;
    }

    public static int referenceSnippet2(int a) {
        int sum = 0;
        switch (a) {
            case 0:
                for (int i = 0; i < 1000; i++) {
                    sum += System.currentTimeMillis();
                }
                break;
            case 1:
                for (int i = 0; i < 1000; i++) {
                    sum += 1;
                    sum += 5;
                }
                break;
            case 55:
                for (int i = 0; i < 1000; i++) {
                    sum += 5;
                }
                break;
            default:
                for (int i = 0; i < 1000; i++) {
                    // nothing
                }
                break;
        }
        return sum;
    }

    @SuppressWarnings("fallthrough")
    public static int test2Snippet(int a) {
        int sum = 0;
        for (int i = 0; GraalDirectives.injectIterationCount(1000, i < 1000); i++) {
            switch (a) {
                case 0:
                    sum += System.currentTimeMillis();
                    break;
                case 1:
                    sum += 1;
                    // fall through
                case 55:
                    sum += 5;
                    break;
                default:
                    // nothing
                    break;
            }
        }
        return sum;
    }

    @Test
    public void test1() {
        test("test1Snippet", "referenceSnippet1");
    }

    @Test
    public void test2() {
        test("test2Snippet", "referenceSnippet2");
    }

    public static int test3Snippet(int a, int b) {
        int sum = 0;
        for (int i = 0; GraalDirectives.injectIterationCount(1000, i < 1000); ++i) {
            if (a > 0) {
                switch (b) {
                    case 0:
                        sum += 1;
                        break;
                    default:
                        sum += 2;
                        break;
                }
            } else {
                sum += 3;
            }
        }
        return sum;
    }

    public static int reference3SwitchSnippet(int a, int b) {
        int sum = 0;
        switch (b) {
            case 0:
                for (int i = 0; i < 1000; ++i) {
                    if (a > 0) {
                        sum += 1;
                    } else {
                        sum += 3;
                    }
                }
                break;
            default:
                for (int i = 0; i < 1000; ++i) {
                    if (a > 0) {
                        sum += 2;
                    } else {
                        sum += 3;
                    }
                }
        }
        return sum;
    }

    public static int reference3IfSnippet(int a, int b) {
        int sum = 0;
        if (a > 0) {
            for (int i = 0; i < 1000; ++i) {
                switch (b) {
                    case 0:
                        sum += 1;
                        break;
                    default:
                        sum += 2;
                        break;
                }
            }
        } else {
            for (int i = 0; i < 1000; ++i) {
                sum += 3;
            }
        }
        return sum;
    }

    public static int reference3SwitchIfSnippet(int a, int b) {
        int sum = 0;
        switch (b) {
            case 0:
                if (a > 0) {
                    for (int i = 0; i < 1000; ++i) {
                        sum += 1;
                    }
                } else {
                    for (int i = 0; i < 1000; ++i) {
                        sum += 3;
                    }
                }
                break;
            default:
                if (a > 0) {
                    for (int i = 0; i < 1000; ++i) {
                        sum += 2;
                    }
                } else {
                    for (int i = 0; i < 1000; ++i) {
                        sum += 3;
                    }
                }

        }
        return sum;

    }

    public static int reference3IfSwitchSnippet(int a, int b) {
        int sum = 0;
        if (a > 0) {
            switch (b) {
                case 0:
                    for (int i = 0; i < 1000; ++i) {
                        sum += 1;
                    }
                    break;
                default:
                    for (int i = 0; i < 1000; ++i) {
                        sum += 2;
                    }
                    break;
            }
        } else {
            for (int i = 0; i < 1000; ++i) {
                sum += 3;
            }
        }
        return sum;
    }

    @Test
    public void test3Switch() {
        // Only the switch is unswitched
        test("test3Snippet", "reference3SwitchSnippet", new DefaultLoopPolicies() {
            @Override
            public UnswitchingDecision shouldUnswitch(LoopEx loop, EconomicMap<ValueNode, List<ControlSplitNode>> controlSplits) {
                for (List<ControlSplitNode> nodes : controlSplits.getValues()) {
                    Assert.assertEquals(1, nodes.size());
                    if (nodes.get(0) instanceof SwitchNode) {
                        return UnswitchingDecision.yes(nodes);
                    }
                }
                return UnswitchingDecision.NO;
            }
        });
    }

    @Test
    public void test3If() {
        // Only the if is unswitched
        test("test3Snippet", "reference3IfSnippet", new DefaultLoopPolicies() {
            @Override
            public UnswitchingDecision shouldUnswitch(LoopEx loop, EconomicMap<ValueNode, List<ControlSplitNode>> controlSplits) {
                for (List<ControlSplitNode> nodes : controlSplits.getValues()) {
                    Assert.assertEquals(1, nodes.size());
                    if (nodes.get(0) instanceof IfNode) {
                        return UnswitchingDecision.yes(nodes);
                    }
                }
                return UnswitchingDecision.NO;
            }
        });
    }

    @Test
    public void test3IfSwitch() {
        // First the if is unswitched then the switch
        test("test3Snippet", "reference3IfSwitchSnippet", new DefaultLoopPolicies() {
            @Override
            public UnswitchingDecision shouldUnswitch(LoopEx loop, EconomicMap<ValueNode, List<ControlSplitNode>> controlSplits) {
                if (controlSplits.size() == 2) {
                    for (List<ControlSplitNode> split : controlSplits.getValues()) {
                        Assert.assertEquals(1, split.size());
                        if (split.get(0) instanceof IfNode) {
                            return UnswitchingDecision.yes(split);
                        }
                    }
                    Assert.fail();
                    return null;
                } else if (controlSplits.size() == 1) {
                    List<ControlSplitNode> split = controlSplits.getValues().iterator().next();
                    Assert.assertEquals(1, split.size());
                    Assert.assertTrue(split.get(0) instanceof SwitchNode);
                    return UnswitchingDecision.yes(split);
                } else {
                    return UnswitchingDecision.NO;
                }
            }
        });
    }

    @Test
    public void test3SwitchIf() {
        // First the switch is unswitched then the if
        test("test3Snippet", "reference3SwitchIfSnippet", new DefaultLoopPolicies() {
            @Override
            public UnswitchingDecision shouldUnswitch(LoopEx loop, EconomicMap<ValueNode, List<ControlSplitNode>> controlSplits) {
                if (controlSplits.size() == 2) {
                    for (List<ControlSplitNode> split : controlSplits.getValues()) {
                        Assert.assertEquals(1, split.size());
                        if (split.get(0) instanceof SwitchNode) {
                            return UnswitchingDecision.yes(split);
                        }
                    }
                    Assert.fail();
                    return null;
                } else if (controlSplits.size() == 1) {
                    List<ControlSplitNode> split = controlSplits.getValues().iterator().next();
                    Assert.assertEquals(1, split.size());
                    Assert.assertTrue(split.get(0) instanceof IfNode);
                    return UnswitchingDecision.yes(split);
                } else {
                    return UnswitchingDecision.NO;
                }
            }
        });
    }

    @Test
    public void test3() {
        // Use the default policy and so the if should be unswitched before the switch
        test("test3Snippet", "reference3IfSwitchSnippet");
    }

    public static void test4Snippet(int a, int b) {
        for (int i = 0; GraalDirectives.injectIterationCount(1000, i < 1000); ++i) {
            if (GraalDirectives.injectBranchProbability(0.000001, a < i)) {
                // This is an invariant but on average it is exectutes 1000 * 0.000001 = 0.001 < 1
                // time per execution of the whole loop so it should not be unswitched.
                if (b > 0) {
                    GraalDirectives.sideEffect(1);
                } else {
                    GraalDirectives.sideEffect(2);
                }
            } else {
                GraalDirectives.sideEffect(3);
            }
        }
    }

    @Test
    public void test4() {
        // Using the default loop policy, no unswitch should be performed.
        test("test4Snippet", "test4Snippet");
    }

    private void test(String snippet, String referenceSnippet) {
        test(snippet, referenceSnippet, new DefaultLoopPolicies());
    }

    @SuppressWarnings("try")
    private void test(String snippet, String referenceSnippet, LoopPolicies policies) {
        DebugContext debug = getDebugContext();
        final StructuredGraph graph = parseEager(snippet, AllowAssumptions.NO);
        final StructuredGraph referenceGraph = parseEager(referenceSnippet, AllowAssumptions.NO);

        new LoopUnswitchingPhase(policies).apply(graph, getDefaultHighTierContext());

        // Framestates create comparison problems
        graph.clearAllStateAfterForTestingOnly();
        referenceGraph.clearAllStateAfterForTestingOnly();

        createCanonicalizerPhase().apply(graph, getProviders());
        createCanonicalizerPhase().apply(referenceGraph, getProviders());
        try (DebugContext.Scope s = debug.scope("Test", new DebugDumpScope("Test:" + snippet))) {
            assertEquals(referenceGraph, graph);
        } catch (Throwable e) {
            throw debug.handle(e);
        }
    }
}
