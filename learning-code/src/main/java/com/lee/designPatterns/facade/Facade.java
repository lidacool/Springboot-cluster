package com.lee.designPatterns.facade;

import com.lee.designPatterns.facade.baseClass.Cpu;
import com.lee.designPatterns.facade.baseClass.Disk;
import com.lee.designPatterns.facade.baseClass.Memory;


//外观模式是为了解决类与类之家的依赖关系的，像spring一样，可以将类和类之间的关系配置到配置文件中，
// 而外观模式就是将他们的关系放在一个Facade类中，降低了类类之间的耦合度
public class Facade {

    private Cpu cpu;
    private Memory memory;
    private Disk disk;

    public Facade() {
        this.cpu=new Cpu();
        this.memory=new Memory();
        this.disk=new Disk();
    }

    public void startComputer(){
        System.out.println("computer start");
        cpu.start();
        memory.start();
        disk.start();
        System.out.println("computer start complete");
    }

    public void shutDownComputer(){
        System.out.println("computer shutDown");
        cpu.shutDown();
        memory.shutDown();
        disk.shutDown();
        System.out.println("computer shutDown complete");
    }
}
