package edu.nju;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @Author: pkun
 * @CreateTime: 2021-05-25 23:53
 */
public class Executor {

    ArrayList<Tape> tapes;
    TuringMachine tm;
    State q;
    int steps = 0;
    boolean canRun = true;

    public Executor(TuringMachine tm, ArrayList<Tape> tapes) {
        this.tm = tm;
        q = tm.getInitState();
        loadTape(tapes);
    }

    /**
     * TODO
     * 1. 检查能否运行
     * 2. 调用tm.delta
     * 3. 更新磁带
     * 4. 返回下次能否执行
     *
     * @return
     */
    public Boolean execute() {
        return true;
    }

    /**
     * TODO
     * 1. 检查磁带的数量是否正确 ( checkTapeNum )
     * 2. 检查磁带上的字符是否是输入符号组的 ( checkTape )
     *
     * @param tapes
     */
    public void loadTape(ArrayList<Tape> tapes) {

    }

    /**
     * TODO
     * 获取所有磁带的快照，也就是把每个磁带上磁头指向的字符全都收集起来
     *
     * @return
     */
    private String snapShotTape() {
        return null;
    }

    /**
     * TODO
     * 按照README给出当前图灵机和磁带的快照
     *
     * @return
     */
    public String snapShot() {
        return null;
    }


    /**
     * TODO
     * 不断切割newTapes，传递给每个Tape的updateTape方法
     *
     * @param newTapes
     */
    private void updateTape(String newTapes) {

    }

    /**
     * TODO
     * 将每个direction里的char都分配给Tape的updateHead方法
     *
     * @param direction
     */
    private void moveHeads(String direction) {

    }



}
