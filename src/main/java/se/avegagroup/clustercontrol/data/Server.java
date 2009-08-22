// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(5) braces fieldsfirst noctor nonlb space lnc 
// Source File Name:   Server.java

package se.avegagroup.clustercontrol.data;

import java.util.List;

public class Server {

            private String id;
            private List<Worker> workers;


            public void setWorkers(List<Worker> workers) {
/*  10*/        this.workers = workers;
            }

            public List<Worker> getWorkers() {
/*  14*/        return workers;
            }

            public void setId(String id) {
/*  18*/        this.id = id;
            }

            public String getId() {
/*  22*/        return id;
            }
}
