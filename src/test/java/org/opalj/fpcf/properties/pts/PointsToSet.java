/* BSD 2-Clause License - see OPAL/LICENSE for details. */
package org.opalj.fpcf.properties.pts;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Describe the expected Points-To-Set at a defsite.
 */
@Retention(CLASS)
@Target({METHOD, CONSTRUCTOR})
@Documented
public @interface PointsToSet {

    /**
     * the points-to-set to be validated (line number of def-site)
     */
    int variableDefinition() default -1;
    int parameterIndex() default -1;
    JavaMethodContextAllocSite[] expectedJavaAllocSites() default {};
    JavaScriptContextAllocSite[] expectedJavaScriptAllocSites() default {};

}