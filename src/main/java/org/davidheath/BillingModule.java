package org.davidheath;

import com.google.inject.AbstractModule;

public class BillingModule extends AbstractModule {
    @Override
    protected void configure() {
        this.bind(BillingService.class).to(RealBillingService.class);
    }
}
