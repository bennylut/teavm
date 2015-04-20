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
package org.teavm.jso.plugin.util;

import java.io.IOException;
import org.teavm.codegen.SourceWriter;
import org.teavm.javascript.ast.ConstantExpr;
import org.teavm.javascript.spi.InjectedBy;
import org.teavm.javascript.spi.Injector;
import org.teavm.javascript.spi.InjectorContext;
import org.teavm.jso.JSObject;
import org.teavm.model.MethodReference;

/**
 *
 * @author bennyl
 */
public class Substituter implements Injector {

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c,
            JSObject d);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c,
            JSObject d, JSObject e);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c,
            JSObject d, JSObject e, JSObject f);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c,
            JSObject d, JSObject e, JSObject f, JSObject g);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c,
            JSObject d, JSObject e, JSObject f, JSObject g, JSObject h);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c,
            JSObject d, JSObject e, JSObject f, JSObject g, JSObject h, JSObject i);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c,
            JSObject d, JSObject e, JSObject f, JSObject g, JSObject h, JSObject i,
            JSObject j);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c,
            JSObject d, JSObject e, JSObject f, JSObject g, JSObject h, JSObject i,
            JSObject j, JSObject k);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c,
            JSObject d, JSObject e, JSObject f, JSObject g, JSObject h, JSObject i,
            JSObject j, JSObject k, JSObject l);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c,
            JSObject d, JSObject e, JSObject f, JSObject g, JSObject h, JSObject i,
            JSObject j, JSObject k, JSObject l, JSObject m);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c,
            JSObject d, JSObject e, JSObject f, JSObject g, JSObject h, JSObject i,
            JSObject j, JSObject k, JSObject l, JSObject m, JSObject n);

    @InjectedBy(Substituter.class)
    public static native JSObject subtitute(JSObject pattern, JSObject a, JSObject b, JSObject c,
            JSObject d, JSObject e, JSObject f, JSObject g, JSObject h, JSObject i,
            JSObject j, JSObject k, JSObject l, JSObject m, JSObject n, JSObject o);

    @Override
    public void generate(InjectorContext context, MethodReference methodRef) throws IOException {
        SourceWriter writer = context.getWriter();

        if (!(context.getArgument(0) instanceof ConstantExpr)) {
            //TODO: should I throw exception here? how else should I inform this error?
            //Is there a way to get instance of diagnostics from here?
            throw new IllegalArgumentException("first argument to subtitute expected to be constant expression");
        }

        ConstantExpr constant = (ConstantExpr) context.getArgument(0);
        String pattern = constant.getValue() instanceof String ? (String) constant.getValue() : null;

        if (pattern == null) {
            throw new IllegalArgumentException("first argument to subtitute expected to be a string");
        }

        int startIndex = 0;
        int nextIndex = pattern.indexOf("$");
        while (nextIndex >= 0) {
            writer.append(pattern, startIndex, nextIndex);
            if (pattern.charAt(nextIndex+1) == '$') {
                writer.append("$");
                startIndex = nextIndex + 2;
            } else {
                int nextSpace = pattern.indexOf(' ', nextIndex + 1);
                if (nextSpace < 0) {
                    throw new UnsupportedOperationException("could not parse substitution pattern");
                }
                
                int argIndex = Integer.parseInt(pattern.substring(nextIndex + 1, nextSpace));
                context.writeExpr(context.getArgument(1 + argIndex));
                startIndex = nextSpace + 1;
            }
            
            nextIndex = pattern.indexOf("$", startIndex);
        }
        writer.append(pattern, startIndex, pattern.length());
    }

}
