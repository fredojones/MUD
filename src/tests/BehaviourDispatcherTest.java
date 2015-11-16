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

package tests;

import main.com.joebentley.mud.Behaviour;
import main.com.joebentley.mud.BehaviourDispatcher;
import org.junit.BeforeClass;
import org.junit.Test;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import static org.junit.Assert.assertEquals;

public class BehaviourDispatcherTest {
    private static Behaviour oneArgScript;
    private static Behaviour varArgScript;
    private static BehaviourDispatcher dispatcher;

    @BeforeClass
    public static void setUp() {
        oneArgScript = new Behaviour("n = ... return n + 1");
        varArgScript = new Behaviour("n, m = ... return n + m + 1");
        dispatcher = new BehaviourDispatcher();

        dispatcher.registerBehaviour("onearg", oneArgScript);
        dispatcher.registerBehaviour("vararg", varArgScript);
    }

    @Test
    public void scriptIsTriggeredProperlyAndReturnsCorrectResult() {
        Varargs ret = dispatcher.apply("onearg", LuaValue.valueOf(2));

        assertEquals(ret.arg1().toint(), 3);
    }

    @Test
    public void multiArgScriptTriggeredProperlyEtc() {
        LuaValue[] args = new LuaValue[2];
        args[0] = LuaValue.valueOf(3);
        args[1] = LuaValue.valueOf(4);
        Varargs ret = dispatcher.apply("vararg", LuaValue.varargsOf(args));

        assertEquals(ret.arg1().toint(), 8);
    }
}
