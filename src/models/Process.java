package models;

import java.math.BigInteger;

public class Process {

    private String name;
    private BigInteger time;
    private boolean isBlock;
    private boolean isSuspend;
    private boolean isResume;


    public Process(String name, BigInteger time, boolean isBlock, boolean isSuspend, boolean isResume) {
        this.name = name;
        this.time = time;
        this.isBlock = isBlock;
        this.isSuspend = isSuspend;
        this.isResume = isResume;
    }

    public Process(Process process){
        this.name = process.getName();
        this.time = process.getTime();
        this.isBlock = process.isBlock();
        this.isSuspend = process.isSuspend();
        this.isResume = process.isResume();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getTime() {
        return time;
    }

    public void setTime(BigInteger time) {
        this.time = time;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    public boolean isSuspend() {
        return isSuspend;
    }

    public void setSuspend(boolean suspend) {
        isSuspend = suspend;
    }

    public boolean isResume() {
        return isResume;
    }

    public void setResume(boolean resume) {
        isResume = resume;
    }

    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", isBlock=" + isBlock +
                ", isSuspend=" + isSuspend +
                ", isResume=" + isResume +
                '}';
    }
}
