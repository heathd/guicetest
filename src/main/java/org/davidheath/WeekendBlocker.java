package org.davidheath;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.LONG;
import static java.util.Locale.ENGLISH;

public class WeekendBlocker implements MethodInterceptor {
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Calendar today = new GregorianCalendar();
        final String displayName = today.getDisplayName(DAY_OF_WEEK, LONG, ENGLISH);
        System.out.println("Today is " + displayName);
        if (displayName.startsWith("M")) {
            throw new IllegalStateException(
                    invocation.getMethod().getName() + " not allowed on weekends!");
        }
        final Object response = invocation.proceed();

        System.out.println("Done, result was " + response);
        return response;
    }
}
