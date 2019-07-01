package org.davidheath;

import javax.inject.Inject;

public class RealBillingService implements BillingService {

    @Inject
    public RealBillingService() {
    }

    @Override
    @NotOnWeekends
    public Integer chargeOrder() {
        System.out.println("Charge order!");
        return 99;
    }
}
