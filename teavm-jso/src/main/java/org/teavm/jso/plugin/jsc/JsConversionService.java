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
package org.teavm.jso.plugin.jsc;

import java.util.Collection;
import org.teavm.codegen.SourceWriter;
import org.teavm.model.ClassHolderSource;
import org.teavm.model.ValueType;
import org.teavm.model.Variable;

/**
 *
 * @author bennyl
 */
public abstract class JsConversionService {
    protected ClassHolderSource classHolderSource;
    protected JsConversionDriver convertor;

    public final void initialize (ClassHolderSource classHolderSource, JsConversionDriver convertor) {
        this.classHolderSource = classHolderSource;
        this.convertor = convertor;
    }
    
    public abstract Collection<String> supportedAnnotations();

    public abstract Collection<ValueType> supportedTypes();

    public void renderHelperJavascriptCode(SourceWriter writer) {
        // do nothing... 
    }

    public abstract void toJava(Variable jsVar, ValueType type, JsConversionBuilder wb);

    public abstract void toJs(Variable javaVar, ValueType type, JsConversionBuilder wb);
    
    public abstract void toJava(Variable jsVar, ValueType.Array type, JsConversionBuilder wb);

    public abstract void toJs(Variable javaVar, ValueType.Array type, JsConversionBuilder wb);
}
