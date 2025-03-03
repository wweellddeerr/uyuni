/*
 * Copyright (c) 2008--2015 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package com.redhat.satellite.search.scheduler.tasks.tests;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import com.redhat.satellite.search.config.Configuration;
import com.redhat.satellite.search.db.DatabaseManager;
import com.redhat.satellite.search.db.WriteQuery;
import com.redhat.satellite.search.index.IndexManager;
import com.redhat.satellite.search.index.IndexingException;
import com.redhat.satellite.search.scheduler.tasks.IndexErrataTask;
import com.redhat.satellite.search.tests.BaseTestCase;
import com.redhat.satellite.search.tests.TestUtil;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.AbstractTrigger;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.spi.MutableTrigger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * IndexErrataTaskTest
 * @version $Rev$
 */
public class IndexErrataTaskTest extends BaseTestCase {
    private static Logger log = Logger.getLogger(IndexErrataTaskTest.class);
    private Scheduler scheduler;

    @Override
    public void tearDown() throws Exception {
        DatabaseManager databaseManager = (DatabaseManager)
            container.getComponentInstanceOfType(DatabaseManager.class);
        WriteQuery updateQuery = databaseManager.getWriterQuery("updateLastErrata");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", 0L);
        params.put("last_modified", new Date(0));
        updateQuery.update(params);
        updateQuery.close();
        if (scheduler != null) {
            scheduler.shutdown(false);
        }
        super.tearDown();
    }

    public void testExecute() throws InterruptedException, SchedulerException {

        DatabaseManager databaseManager = (DatabaseManager)
            container.getComponentInstanceOfType(DatabaseManager.class);

        IndexManager indexManager = (IndexManager)
            container.getComponentInstanceOfType(IndexManager.class);

        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        }
        catch (SchedulerException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        SimpleTrigger trigger = newTrigger().withIdentity("index", "default")
                          .forJob("index", "default")
                          .startAt(new Date())
                          .endAt(null)
                          .withSchedule(simpleSchedule()
                                  .withRepeatCount(0)
                                  .withIntervalInMilliseconds(100))
                          .build();
        MutableTrigger mtrigger = null;

        if (trigger instanceof MutableTrigger) {
            mtrigger = (MutableTrigger) trigger;
            mtrigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT);
        }
        else {
            log.error("Cannot set MisfireInstruction since trigger is not instance of MutableTrigger: " + trigger);
        }
        JobBuilder detail = newJob(IndexErrataTask.class)
                .withIdentity("index", "default");
        JobDataMap jobData = new JobDataMap();
        jobData.put("indexManager", indexManager);
        jobData.put("databaseManager", databaseManager);
        detail.usingJobData(jobData);
        try {
            scheduler.getListenerManager().addTriggerListener(new TestTrigger());
            if (trigger instanceof MutableTrigger) {
                scheduler.scheduleJob(detail.build(), mtrigger);
            }
            else {
                scheduler.scheduleJob(detail.build(), trigger);
            }
        }
        catch (SchedulerException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {

            scheduler.start();
        }
        catch (SchedulerException e) {
            System.out.println("woo hoo");
        }
        Thread.sleep(1000);
        if(!scheduler.isShutdown()) {
            scheduler.shutdown(true);
        }
        System.out.println("running");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class[] getComponentClasses() {
        Class[] comps = {DatabaseManager.class, TestIndexManager.class};
        return TestUtil.buildComponentsList(comps);
    }

    public static class TestIndexManager extends IndexManager {

        @Override
        public void addToIndex(String indexName, Document doc, String lang) throws IndexingException {
            assertNotNull(doc);
            assertNotNull(doc.getField("advisory"));
            assertNotNull(doc.getField("advisory").stringValue());
            log.info("idx[" + indexName + "] doc [" + doc.toString() + "]");
        }

        /**
         * @param config
         */
        public TestIndexManager(Configuration config) {
            super(config);
        }

    }

    public static class TestTrigger implements TriggerListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName() {
            return "test trigger";
        }

        /**
         * {@inheritDoc}
         */
        public void triggerComplete(Trigger trigger, JobExecutionContext context, int triggerInstructionCode) {
            System.out.println("**************************** completed");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void triggerFired(Trigger trigger, JobExecutionContext context) {
            System.out.println("**************************** fired");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void triggerMisfired(Trigger trigger) {
            System.out.println("**************************** misfire");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
            return false;
        }

        @Override
        public void triggerComplete(Trigger arg0, JobExecutionContext arg1,
                CompletedExecutionInstruction arg2) {
        }

    }
}
