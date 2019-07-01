package org.davidheath;

import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;

public class PostCommitHook extends SessionEventAdapter {
    @Override
    public void postBeginTransaction(SessionEvent event) {
        super.postBeginTransaction(event);
    }

    @Override
    public void preBeginTransaction(SessionEvent event) {
        super.preBeginTransaction(event);
    }

    @Override
    public void postCommitTransaction(SessionEvent event) {
        System.out.println("postCommitTransaction: " + event);

        System.out.println("postCommitTransaction: " + event.getQuery());
        super.postCommitTransaction(event);
    }

    @Override
    public void postCommitUnitOfWork(SessionEvent event) {
        System.out.println("postCommitUnitOfWork: " + event);
        super.postCommitUnitOfWork(event);
    }
}
