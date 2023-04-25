package org.caillou.company.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Context {

    private UUID uuid;

    private Object result;

    long startTime;

    long endTime;

    boolean rgpdSafe;
}
