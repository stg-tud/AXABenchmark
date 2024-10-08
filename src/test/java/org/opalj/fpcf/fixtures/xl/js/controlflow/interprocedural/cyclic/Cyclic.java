/* BSD 2-Clause License - see OPAL/LICENSE for details. */
package org.opalj.fpcf.fixtures.xl.js.controlflow.interprocedural.cyclic;


import org.opalj.fpcf.fixtures.xl.js.controlflow.interprocedural.interleaved.JavaScriptCallsJavaFunctionOnPassedInstance;
import org.opalj.fpcf.fixtures.xl.js.testpts.SimpleContainerClass;
import org.opalj.fpcf.properties.pts.JavaMethodContextAllocSite;
import org.opalj.fpcf.properties.pts.PointsToSet;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Cyclic execution: javascript calls java function that it was invoked in (interprocedural variation).
 * original example changed to use String (to allow pts tracking).
 * if pts were calculated correctly, pts for parameter should include defsite of value calculated below.
 */
public class Cyclic {
    ScriptEngine se;
    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        Cyclic c = new Cyclic();
        ScriptEngineManager sem = new ScriptEngineManager();
        c.se = sem.getEngineByName("JavaScript");
        c.se.put("jThis", c);
        SimpleContainerClass initial = new SimpleContainerClass();
        SimpleContainerClass res = c.decrement(initial);
        System.out.println(res);
    }
    @PointsToSet(parameterIndex = 0,
            expectedJavaAllocSites = {
                    @JavaMethodContextAllocSite(
                            cf = Cyclic.class,
                            methodName = "decrement",
                            methodDescriptor = "(org.opalj.fpcf.fixtures.xl.js.testpts.SimpleContainerClass): org.opalj.fpcf.fixtures.xl.js.testpts.SimpleContainerClass",
                            allocSiteLinenumber = 44,
                            allocatedType = "org.opalj.fpcf.fixtures.xl.js.testpts.SimpleContainerClass")
            }
    )
    public SimpleContainerClass decrement(SimpleContainerClass p) throws ScriptException, NoSuchMethodException {
        SimpleContainerClass param = p;
        System.out.println("parameter: "+param + " id: " + System.identityHashCode(param));
        SimpleContainerClass decremented = new SimpleContainerClass();
        decremented.n = param.n - 1;
        System.out.println("decremented: "+ decremented + " id: " + System.identityHashCode(decremented));

        if(decremented.n > 0){
            SimpleContainerClass result =  callDecementThroughScriptEngine(decremented);
            return result;
        }
        else {
            System.out.println("n: " + decremented);
            return decremented;
        }
    }

    public SimpleContainerClass callDecementThroughScriptEngine(SimpleContainerClass s) throws ScriptException, NoSuchMethodException {
        se.put("arg", s);
        se.eval("var res = jThis.decrement(arg)");
        SimpleContainerClass result = (SimpleContainerClass) se.get("res");
        return result;
    }
}

