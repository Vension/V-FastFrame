package com.vension.fastframe.app.ui.bus.rx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * ========================================================================
 * 作 者：Vension
 * 主 页：Github: https://github.com/Vension
 * 日 期：2019/9/2 11:25
 *   ______       _____ _______ /\/|_____ _____            __  __ ______
 *  |  ____/\    / ____|__   __|/\/  ____|  __ \     /\   |  \/  |  ____|
 *  | |__ /  \  | (___    | |     | |__  | |__) |   /  \  | \  / | |__
 *  |  __/ /\ \  \___ \   | |     |  __| |  _  /   / /\ \ | |\/| |  __|
 *  | | / ____ \ ____) |  | |     | |    | | \ \  / ____ \| |  | | |____
 *  |_|/_/    \_\_____/   |_|     |_|    |_|  \_\/_/    \_\_|  |_|______|
 *
 * Take advantage of youth and toss about !
 * ------------------------------------------------------------------------
 * 描述：事件处理基类
 * ========================================================================
 */
public class EventBase {
    protected final static Map<Class<?>, Object> STICKY_EVENT_MAP;
    protected final static Subject<Object> SUBJECT;

    static {
        SUBJECT = PublishSubject.create().toSerialized();
        STICKY_EVENT_MAP = new HashMap<>();
    }

    protected EventBase() {
    }

    static <T> Flowable<T> toFlowable(Class<T> eventType) {
        return SUBJECT.ofType(eventType).toFlowable(BackpressureStrategy.BUFFER);
    }

    /**
     * 删除指定粘性事件
     *
     * @param event
     */
    static synchronized void dellSticky(Object event) {
        if (!STICKY_EVENT_MAP.isEmpty()) {
            List<Class> classes = new ArrayList<>();
            for (Map.Entry<Class<?>, Object> objectEntry : STICKY_EVENT_MAP.entrySet()) {
                if (objectEntry.getKey() == event.getClass()) {
                    classes.add(event.getClass());
                }
            }
            stickyEventMapRemove(classes);
        }
    }

    static void stickyEventMapRemove(List<Class> classes) {
        for (Class aClass : classes) STICKY_EVENT_MAP.remove(aClass);
    }
}
