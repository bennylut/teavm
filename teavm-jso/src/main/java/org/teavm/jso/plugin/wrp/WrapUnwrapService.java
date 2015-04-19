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
package org.teavm.jso.plugin.wrp;

import java.util.Collection;
import org.teavm.codegen.SourceWriter;
import org.teavm.model.AnnotationReader;
import org.teavm.model.ValueType;
import org.teavm.model.Variable;

/**
 *
 * @author bennyl
 */
public interface WrapUnwrapService {
    
    Collection<String> supportedAnnotations();
    
    Collection<ValueType> supportedTypes();
    
    void renderHelperJavascriptCode(SourceWriter writer);
    
    void unwrap(Variable toUnwrap, ValueType type, AnnotationReader typeAnnotations, UnwrapBuilder ub);

    void wrap(Variable toWrap, ValueType type, AnnotationReader typeAnnotations, WrapBuilder wb);
}
