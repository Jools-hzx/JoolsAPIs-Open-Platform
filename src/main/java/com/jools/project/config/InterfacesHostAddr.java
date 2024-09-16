package com.jools.project.config;

public enum InterfacesHostAddr {

    VIRTUAL_INTERFACES_PLATFORM("http://localhost:10000");

    private String addr;

    InterfacesHostAddr(String addr) {
        this.addr = addr;
    }

    public String getAddr() {
        return addr;
    }
}
