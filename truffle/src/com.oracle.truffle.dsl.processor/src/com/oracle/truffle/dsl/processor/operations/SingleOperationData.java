package com.oracle.truffle.dsl.processor.operations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import com.oracle.truffle.dsl.processor.ProcessorContext;
import com.oracle.truffle.dsl.processor.TruffleTypes;
import com.oracle.truffle.dsl.processor.java.model.CodeTypeMirror.ArrayCodeTypeMirror;
import com.oracle.truffle.dsl.processor.model.MessageContainer;
import com.oracle.truffle.dsl.processor.model.NodeData;
import com.oracle.truffle.dsl.processor.model.Template;

public class SingleOperationData extends Template {
    private final String name;
    private MethodProperties mainProperties;
    private NodeData nodeData;
    private OperationsData parent;
    private final Set<TypeMirror> throwDeclarations = new HashSet<>();

    static enum ParameterKind {
        STACK_VALUE,
        VARIADIC,
        THE_NODE;

        public TypeMirror getParameterType(ProcessorContext context, TruffleTypes types) {
            switch (this) {
                case STACK_VALUE:
                    return context.getType(Object.class);
                case VARIADIC:
                    return new ArrayCodeTypeMirror(context.getType(Object.class));
                case THE_NODE:
                    return types.Node;
                default:
                    throw new IllegalArgumentException("" + this);
            }
        }
    }

    static class MethodProperties {
        public final ExecutableElement element;
        public final List<ParameterKind> parameters;
        public final boolean isVariadic;
        public final boolean returnsValue;
        public final int numStackValues;

        public MethodProperties(ExecutableElement element, List<ParameterKind> parameters, boolean isVariadic, boolean returnsValue) {
            this.element = element;
            this.parameters = parameters;
            int numStackValues = 0;
            for (ParameterKind param : parameters) {
                if (param == ParameterKind.STACK_VALUE || param == ParameterKind.VARIADIC) {
                    numStackValues++;
                }
            }
            this.numStackValues = numStackValues;
            this.isVariadic = isVariadic;
            this.returnsValue = returnsValue;
        }

        public void checkMatches(SingleOperationData data, MethodProperties other) {
            if (other.numStackValues != numStackValues) {
                data.addError(element, "All methods must have same number of arguments");
            }

            if (other.isVariadic != isVariadic) {
                data.addError(element, "All methods must (not) be variadic");
            }

            if (other.returnsValue != returnsValue) {
                data.addError(element, "All methods must (not) return value");
            }
        }

        @Override
        public String toString() {
            return "Props[parameters=" + parameters + ", variadic=" + isVariadic + ", returns=" + returnsValue + ", numStackValues=" + numStackValues + "]";
        }
    }

    public SingleOperationData(ProcessorContext context, TypeElement templateType, AnnotationMirror annotation, OperationsData parent) {
        super(context, templateType, annotation);
        this.parent = parent;
        name = templateType.getSimpleName().toString();
    }

    @Override
    public MessageContainer getBaseContainer() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public Set<TypeMirror> getThrowDeclarations() {
        return throwDeclarations;
    }

    public MethodProperties getMainProperties() {
        return mainProperties;
    }

    public void setMainProperties(MethodProperties mainProperties) {
        this.mainProperties = mainProperties;
    }

    public NodeData getNodeData() {
        return nodeData;
    }

    void setNodeData(NodeData data) {
        this.nodeData = data;
    }

}