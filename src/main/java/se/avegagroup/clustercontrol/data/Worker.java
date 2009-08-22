// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(5) braces fieldsfirst noctor nonlb space lnc 
// Source File Name:   Worker.java

package se.avegagroup.clustercontrol.data;


public class Worker {

            private String id;
            private String name;
            private String type;
            private String host;
            private String addr;
            private String act;
            private String state;


            public void setId(String id) {
/*  15*/        this.id = id;
            }

            public String getId() {
/*  18*/        return id;
            }

            public String getName() {
/*  21*/        return name;
            }

            public void setName(String name) {
/*  24*/        this.name = name;
            }

            public String getType() {
/*  27*/        return type;
            }

            public void setType(String type) {
/*  30*/        this.type = type;
            }

            public String getHost() {
/*  33*/        return host;
            }

            public void setHost(String host) {
/*  36*/        this.host = host;
            }

            public String getAddr() {
/*  39*/        return addr;
            }

            public void setAddr(String addr) {
/*  42*/        this.addr = addr;
            }

            public String getAct() {
/*  45*/        return act;
            }

            public void setAct(String act) {
/*  48*/        this.act = act;
            }

            public String getState() {
/*  51*/        return state;
            }

            public void setState(String state) {
/*  54*/        this.state = state;
            }
}
