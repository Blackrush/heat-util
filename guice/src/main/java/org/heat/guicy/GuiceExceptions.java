package org.heat.guicy;

import com.google.inject.CreationException;
import com.google.inject.ProvisionException;
import com.google.inject.internal.util.$ComputationException;

import java.io.PrintStream;

public final class GuiceExceptions {
    private GuiceExceptions() {}

    public static void prettyPrintExceptions(Throwable t, PrintStream out) {
        if (t instanceof $ComputationException) {
            getRootCause(t).printStackTrace(out);
        } else if (t instanceof CreationException) {
            ((CreationException) t).getErrorMessages().forEach(error -> {
//				out.println(error.getSources());
//				out.println(error.getMessage());
//				out.println();
                if (error.getCause() != null) {
                    error.getCause().printStackTrace();
                } else {
                    System.err.println(error.getMessage());
                    System.err.println("when injecting : " + error.getSources());
                    System.err.println();
                }
            });
        } else if (t instanceof ProvisionException) {
            ((ProvisionException) t).getErrorMessages().forEach(msg -> msg.getCause().printStackTrace());
        }
    }

    public static Throwable getRootCause(Throwable t) {
        Throwable rootCause = t;
        while (rootCause.getCause() != null) {
            rootCause.printStackTrace();
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }
}
