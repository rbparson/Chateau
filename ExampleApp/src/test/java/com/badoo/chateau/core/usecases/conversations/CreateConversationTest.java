package com.badoo.chateau.core.usecases.conversations;

import com.badoo.chateau.data.models.BaseConversation;
import com.badoo.chateau.data.models.BaseUser;
import com.badoo.chateau.core.model.Message;
import com.badoo.chateau.core.repos.conversations.ConversationQuery;
import com.badoo.chateau.core.repos.conversations.ConversationRepository;
import com.badoo.unittest.rx.BaseRxTestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import rx.Observable;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateConversationTest extends BaseRxTestCase {

    private ConversationRepository mMockRepository;
    private CreateConversation mTarget;

    @Before
    public void beforeTest() {
        super.beforeTest();
        mMockRepository = mock(ConversationRepository.class);
        mTarget = new CreateConversation(mMockRepository);
    }

    @Test
    public void whenMessagesForChatRequested_thenRepoIsQueriedForCorrectChatId() throws Exception {
        // Setup
        final BaseUser user = new BaseUser("id", "displayName");
        final BaseConversation conversation = new BaseConversation("id", "name", Collections.emptyList(), new Message() {}, 0);
        when(mMockRepository.query(eq(new ConversationQuery.CreateConversationQuery(user))))
            .thenReturn(Observable.just(conversation));

        // Execute
        mTarget.execute(user);

        // Assert
        verify(mMockRepository, times(1)).query(eq(new ConversationQuery.CreateConversationQuery(user)));
    }

    @Test
    public void thatResultIsReturnedOnMainThread() throws Exception {
        // Setup
        final BaseUser user = new BaseUser("id", "displayName");
        final BaseConversation conversation = new BaseConversation("id", "name", Collections.emptyList(), new Message() {}, 0);
        when(mMockRepository.query(eq(new ConversationQuery.CreateConversationQuery(user))))
            .thenReturn(Observable.just(conversation));

        // Execute & Assert
        mTarget.execute(user)
            .doOnNext(__ -> assertTrue("Result not returned on main thread", getSchedulerFactory().isOnMainScheduler(Thread.currentThread())))
            .subscribe();
    }

}