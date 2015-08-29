package org.heat.guicy;

import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;

public interface ContextualScope<Ctx> extends Scope, Provider<Ctx>, Module {
    void enter(Ctx ctx);
    void quit();
    void clean(Ctx ctx);
}
