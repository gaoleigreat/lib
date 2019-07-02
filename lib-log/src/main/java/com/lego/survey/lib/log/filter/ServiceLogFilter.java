package com.lego.survey.lib.log.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class ServiceLogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        if("controller".equals(iLoggingEvent.getLoggerName())){
            return FilterReply.ACCEPT;
        }
        return FilterReply.DENY;
    }
}
