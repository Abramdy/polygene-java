package org.qi4j.library.eventsourcing.application.source.memory;

import org.qi4j.api.activation.Activators;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.io.Input;
import org.qi4j.io.Output;
import org.qi4j.io.Receiver;
import org.qi4j.io.Sender;
import org.qi4j.library.eventsourcing.application.api.TransactionApplicationEvents;
import org.qi4j.library.eventsourcing.application.source.*;
import org.qi4j.library.eventsourcing.domain.api.UnitOfWorkDomainEventsValue;

import java.io.IOException;
import java.util.*;


/**
 * In-Memory ApplicationEventStore. Mainly used for testing.
 */
@Mixins(MemoryApplicationEventStoreService.MemoryStoreMixin.class)
@Activators(ApplicationEventStoreActivation.Activator.class)
public interface MemoryApplicationEventStoreService
        extends ApplicationEventSource, ApplicationEventStore, ApplicationEventStream, ApplicationEventStoreActivation, ServiceComposite
{

    abstract class MemoryStoreMixin
            extends AbstractApplicationEventStoreMixin
            implements ApplicationEventSource, ApplicationEventStoreActivation
    {

        // This list holds all transactions
        private LinkedList<TransactionApplicationEvents> store = new LinkedList<TransactionApplicationEvents>();

        @Override
        public Input<TransactionApplicationEvents, IOException> transactionsAfter(final long afterTimestamp, final long maxTransactions)
        {
            return new Input<TransactionApplicationEvents, IOException>()
            {
                @Override
                public <ReceiverThrowableType extends Throwable> void transferTo(Output<? super TransactionApplicationEvents, ReceiverThrowableType> output) throws IOException, ReceiverThrowableType
                {
                    // Lock store first
                    lock.lock();
                    try
                    {
                        output.receiveFrom(new Sender<TransactionApplicationEvents, IOException>()
                        {
                            @Override
                            public <ReceiverThrowableType extends Throwable> void sendTo(Receiver<? super TransactionApplicationEvents, ReceiverThrowableType> receiver) throws ReceiverThrowableType, IOException
                            {
                                Iterator<TransactionApplicationEvents> iterator = store.iterator();

                                long count = 0;

                                while (iterator.hasNext() && count < maxTransactions)
                                {
                                    TransactionApplicationEvents next = iterator.next();
                                    if (next.timestamp().get() > afterTimestamp)
                                    {
                                        receiver.receive(next);
                                        count++;
                                    }
                                }
                            }
                        });
                    } finally
                    {
                        lock.unlock();
                    }
                }
            };

        }

        @Override
        public Input<TransactionApplicationEvents, IOException> transactionsBefore(final long beforeTimestamp, final long maxTransactions)
        {
            return new Input<TransactionApplicationEvents, IOException>()
            {
                @Override
                public <ReceiverThrowableType extends Throwable> void transferTo(Output<? super TransactionApplicationEvents, ReceiverThrowableType> output) throws IOException, ReceiverThrowableType
                {
                    // Lock store first
                    lock.lock();
                    try
                    {
                        output.receiveFrom(new Sender<TransactionApplicationEvents, IOException>()
                        {
                            @Override
                            public <ReceiverThrowableType extends Throwable> void sendTo(Receiver<? super TransactionApplicationEvents, ReceiverThrowableType> receiver) throws ReceiverThrowableType, IOException
                            {

                                ListIterator<TransactionApplicationEvents> iterator = store.listIterator();

                                while (iterator.hasNext())
                                {
                                    TransactionApplicationEvents next = iterator.next();
                                    if (next.timestamp().get() >= beforeTimestamp)
                                    {
                                        break;
                                    }
                                }

                                long count = 0;

                                while (iterator.hasPrevious() && count < maxTransactions)
                                {
                                    TransactionApplicationEvents next = iterator.previous();
                                    receiver.receive(next);
                                    count++;
                                }
                            }
                        });
                    } finally
                    {
                        lock.unlock();
                    }
                }
            };

        }

        @Override
        protected void storeEvents(TransactionApplicationEvents transactionDomain) throws IOException
        {
            store.add(transactionDomain);
        }

    }

}
