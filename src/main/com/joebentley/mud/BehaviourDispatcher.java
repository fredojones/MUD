/*
 * Copyright (c) 2015
 *
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 */

package main.com.joebentley.mud;

import org.luaj.vm2.Varargs;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class BehaviourDispatcher implements BiFunction<String, Varargs, Varargs> {
    private Map<String, Behaviour> behaviours;

    public BehaviourDispatcher() {
        behaviours = new HashMap<>();
    }

    public void registerBehaviour(String trigger, String script) {
        behaviours.put(trigger, new Behaviour(script));
    }

    public void registerBehaviour(String trigger, Behaviour behaviour) {
        behaviours.put(trigger, behaviour);
    }

    @Override
    public Varargs apply(String s, Varargs args) {
        Varargs val = null;

        if (behaviours.containsKey(s)) {
            val = behaviours.get(s).apply(args);
        }

        return val;
    }
}
