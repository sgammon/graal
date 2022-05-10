package com.oracle.truffle.api.operation.test.example;

import java.util.List;

import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.GenerateAOT;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.operation.AbstractOperationsTruffleException;
import com.oracle.truffle.api.operation.GenerateOperations;
import com.oracle.truffle.api.operation.GenerateOperations.Metadata;
import com.oracle.truffle.api.operation.MetadataKey;
import com.oracle.truffle.api.operation.Operation;
import com.oracle.truffle.api.operation.Variadic;

@GenerateAOT
@GenerateOperations
public final class TestOperations {

    @Metadata("TestData") //
    public static final MetadataKey<String> TEST_DATA = new MetadataKey<>("default value");

    private static class TestException extends AbstractOperationsTruffleException {

        private static final long serialVersionUID = -9143719084054578413L;

        public TestException(String string, Node node, int bci) {
            super(string, node, bci);
        }
    }

    @Operation
    @GenerateAOT
    static class AddOperation {
        @Specialization
        public static long add(long lhs, long rhs) {
            return lhs + rhs;
        }

        @Specialization
        public static String addStrings(String lhs, String rhs) {
            return lhs + rhs;
        }
    }

    @Operation
    @GenerateAOT
    static class LessThanOperation {
        @Specialization
        public static boolean lessThan(long lhs, long rhs) {
            return lhs < rhs;
        }
    }

    @Operation
    @GenerateAOT
    static class VeryComplexOperation {
        @Specialization
        public static long bla(long a1, @Variadic Object[] a2) {
            return a1 + a2.length;
        }
    }

    @Operation
    @GenerateAOT
    static class ThrowOperation {
        @Specialization
        public static Object perform(@Bind("$bci") int bci, @Bind("this") Node node) {
            throw new TestException("fail", node, bci);
        }
    }

    @Operation
    static class AlwaysBoxOperation {
        @Specialization
        public static Object perform(Object value) {
            return value;
        }
    }

    @Operation
    static class AppenderOperation {
        @Specialization
        public static void perform(List<Object> list, Object value) {
            list.add(value);
        }
    }
}