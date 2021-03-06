/*
 * Copyright 2013 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gwtplatform.carstore.client.application;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.carstore.client.application.event.ActionBarVisibilityEvent;
import com.gwtplatform.carstore.client.application.event.ChangeActionBarEvent;
import com.gwtplatform.carstore.client.application.widget.header.HeaderPresenter;
import com.gwtplatform.carstore.client.application.widget.message.MessagesPresenter;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.presenter.slots.SingleSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy>
        implements ChangeActionBarEvent.ChangeActionBarHandler, ActionBarVisibilityEvent.ActionBarVisibilityHandler {

    interface MyView extends View {
        void adjustActionBar(Boolean actionBarVisible);

        void adjustLayout(Boolean tabsVisible);
    }

    @ProxyStandard
    interface MyProxy extends Proxy<ApplicationPresenter> {
    }

    public static final NestedSlot SLOT_MAIN_CONTENT = new NestedSlot();

    public static final SingleSlot SLOT_MESSAGES_CONTENT = new SingleSlot();
    public static final SingleSlot SLOT_HEADER_CONTENT = new SingleSlot();

    private final HeaderPresenter headerPresenter;
    private final MessagesPresenter messagesPresenter;

    @Inject
    ApplicationPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            HeaderPresenter headerPresenter,
            MessagesPresenter messagesPresenter) {
        super(eventBus, view, proxy, RevealType.RootLayout);

        this.headerPresenter = headerPresenter;
        this.messagesPresenter = messagesPresenter;
    }

    @Override
    public void onActionBarVisible(ActionBarVisibilityEvent event) {
        getView().adjustActionBar(event.isVisible());
    }

    @Override
    public void onChangeActionBar(ChangeActionBarEvent event) {
        getView().adjustLayout(event.getTabsVisible());
    }

    @Override
    protected void onBind() {
        setInSlot(SLOT_HEADER_CONTENT, headerPresenter);
        setInSlot(SLOT_MESSAGES_CONTENT, messagesPresenter);

        addRegisteredHandler(ChangeActionBarEvent.getType(), this);
        addRegisteredHandler(ActionBarVisibilityEvent.getType(), this);
    }
}
