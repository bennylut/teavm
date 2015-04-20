/*
 * Copyright 2015 bennyl.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.teavm.jso.plugin.aproc;

import org.teavm.jso.plugin.aproc.ConstructSubtituteBuilder;
import org.teavm.jso.plugin.aproc.InvokeSubtituteBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import org.teavm.diagnostics.Diagnostics;
import org.teavm.jso.JSNative;
import org.teavm.jso.plugin.aproc.AbstractAnnotationProcessor;
import org.teavm.jso.plugin.aproc.FieldGetSubtituteBuilder;
import org.teavm.jso.plugin.aproc.FieldPutSubtituteBuilder;
import org.teavm.model.CallLocation;
import org.teavm.model.ClassHolder;
import org.teavm.model.ClassReaderSource;
import org.teavm.model.ElementModifier;
import org.teavm.model.MethodHolder;

/**
 *
 * @author bennyl
 */
public class JSNativeAnnotationProcessor extends AbstractAnnotationProcessor {

    @Override
    public Collection<String> supportedAnnotations() {
        return Arrays.asList(JSNative.class.getCanonicalName());
    }

    @Override
    public void substituteConstructorCall(ClassHolder cls, ConstructSubtituteBuilder s) {
        s.substituteWithEmptyInstruction();
    }

    @Override
    public void substituteMethodInvocation(ClassHolder cls, InvokeSubtituteBuilder s) {
        String methodName = s.getInstruction().getMethod().getName();
        if (methodName.equals("<init>")) {
            String constructorName = cls.getAnnotations().get(JSNative.class.getName()).getValue("value").getString();
            s.append("(").appendInstance().append(" = new ", constructorName).appendArgListJS().append(")");
        }
    }

    @Override
    public void substituteFieldGet(ClassHolder cls, FieldGetSubtituteBuilder s) {
        s.appendInstance().append(".").append(s.getFieldName());
    }

    @Override
    public void substituteFieldPut(ClassHolder cls, FieldPutSubtituteBuilder s) {
        s.appendInstance().append(".").append(s.getFieldName(), " = ").appendValueJS();
    }

    @Override
    public void transformAnnotatedClass(ClassHolder cls, ClassReaderSource innerSource, Diagnostics diagnostics) {
        for (MethodHolder method : cls.getMethods()) {
            EnumSet<ElementModifier> modifiers = method.getModifiers();
            if (modifiers.contains(ElementModifier.NATIVE)) {
                method.getModifiers().remove(ElementModifier.NATIVE);
                method.getModifiers().add(ElementModifier.ABSTRACT);
            } else if (!modifiers.contains(ElementModifier.FINAL)
                    && !modifiers.contains(ElementModifier.STATIC)
                    && !method.getName().equals("<init>")) {
                CallLocation callLocation = new CallLocation(method.getReference());
                diagnostics.error(callLocation, "Method {{m0}} is not a proper native JavaScript method "
                        + "declaration (must be native, final or constructor)", method.getReference());
            }
        }
    }

}
