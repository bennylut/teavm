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

import org.teavm.diagnostics.Diagnostics;
import org.teavm.model.ClassHolder;
import org.teavm.model.ClassReaderSource;
import org.teavm.model.FieldHolder;
import org.teavm.model.MethodHolder;

/**
 *
 * @author bennyl
 */
public abstract class AbstractAnnotationProcessor implements AnnotationProcessor {

    @Override
    public void transformAnnotatedClass(ClassHolder cls, ClassReaderSource innerSource, Diagnostics diagnostics) {
    }

    @Override
    public void transformAnnotatedMethod(MethodHolder mtd, ClassReaderSource innerSource, Diagnostics diagnostics) {
    }

    @Override
    public void transformAnnotatedField(FieldHolder field, ClassReaderSource innerSource, Diagnostics diagnostics) {
    }

    @Override
    public void substituteConstructorCall(ClassHolder cls, ConstructSubtituteBuilder s) {
    }

    @Override
    public void substituteMethodInvocation(ClassHolder cls, InvokeSubtituteBuilder s) {
    }

    @Override
    public void substituteMethodInvocation(MethodHolder mtd, InvokeSubtituteBuilder s) {
    }

    @Override
    public void substituteFieldGet(ClassHolder cls, FieldGetSubtituteBuilder s) {
    }

    @Override
    public void substituteFieldGet(FieldHolder field, FieldGetSubtituteBuilder s) {
    }

    @Override
    public void substituteFieldPut(ClassHolder cls, FieldPutSubtituteBuilder s) {
    }

    @Override
    public void substituteFieldPut(FieldHolder field, FieldPutSubtituteBuilder s) {
    }

}
