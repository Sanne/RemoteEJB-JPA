package org.jboss.as.quickstarts.ejb.remote.stateless;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;

final class StatementsCountChecker {

    private long expectedQueryCount;
    private final Statistics stats;

    StatementsCountChecker(final Session session) {
        stats = session.getSessionFactory().getStatistics();
        final long initialStatementCount = stats.getPrepareStatementCount();
        expectedQueryCount = initialStatementCount;
    }

    synchronized void verifyIncrement(final int delta) {
        expectedQueryCount = expectedQueryCount + delta;
        if (expectedQueryCount != stats.getPrepareStatementCount()) {
            throw new RuntimeException(
                  "Failed assertion! Expected number of statements: " + expectedQueryCount +
                  " ; actual number of statements measured by Hibernate ORM statistics: " + stats.getPrepareStatementCount());
        }
    }
}
