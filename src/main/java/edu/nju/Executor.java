package edu.nju;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
     * @return Boolean
     */
    public Boolean execute() {
        if (canRun) {
            String input = snapShotTape();
            TransitionFunction delta = q.getDelta(input);
            updateTape(delta.getOutput());
            q = delta.getDestinationState();
            moveHeads(delta.getDirection());
            if (tm.isStop(q, snapShotTape())) {
                canRun = false;
            }
            steps++;
        }

        return canRun;
    }

    /**
     * TODO
     * 1. 检查磁带的数量是否正确 ( checkTapeNum )
     * 2. 检查磁带上的字符是否是输入符号组的 ( checkTape )
     *
     * @param tapes tapes
     */
    public void loadTape(ArrayList<Tape> tapes) {
        if (!tm.checkTapeNum(tapes.size())) {
            System.err.println("Error: 2");
            canRun = false;
        }
        Set<Character> alphabet = new HashSet<>();
        for (Tape each : tapes) {
            each.tracks.forEach(track -> {
                for (char ch : track.toString().toCharArray()) {
                    alphabet.add(ch);
                }
            });
        }
        alphabet.remove(tm.getB());
        if (!tm.checkTape(alphabet)) {
            System.err.println("Error: 1");
            canRun = false;
        }
        this.tapes = tapes;
    }

    /**
     * TODO
     * 获取所有磁带的快照，也就是把每个磁带上磁头指向的字符全都收集起来
     *
     * @return String
     */
    private String snapShotTape() {
        StringBuilder sb = new StringBuilder();
        for (Tape tape : tapes) {
            sb.append(tape.snapShot());
        }
        return sb.toString();
    }

    /**
     * TODO
     * 按照README给出当前图灵机和磁带的快照
     *
     * @return String
     */
    public String snapShot() {
        StringBuilder sb = new StringBuilder();
        int len = calLen();
        // print steps
        sb.append(fillName("Step", len)).append(": ").append(steps).append(System.lineSeparator());
        // print tape
        for (int i = 0; i < tapes.size(); i++) {
            sb.append(fillName("Tape" + i, len)).append(":").append(System.lineSeparator());
            // print index
            Tape tape = tapes.get(i);
            int low = tape.getLowIndex();
            int high = tape.getHighIndex();
            sb.append(fillName("Index" + i, len)).append(":").append(fillIndex(low, high)).append(System.lineSeparator());
            // print track
            for (int j = 0; j < tape.tracks.size(); j++) {
                sb.append(fillName("Track" + j, len)).append(":").append(fillTrack(tape.tracks.get(j), low, high)).append(System.lineSeparator());
            }
            // print head
            sb.append(fillName("Head" + i, len)).append(": ").append(tape.getHead()).append(System.lineSeparator());
        }
        // print state
        sb.append(fillName("State", len)).append(": ").append(q.getQ());
        return sb.toString();
    }

    private int calLen() {
        int tapeLen = String.valueOf(tapes.size()).length();
        int res = 4 + tapeLen;
        for (Tape tape : tapes) {
            int trackLen = String.valueOf(tape.tracks.size()).length();
            res = Math.max(res, 5 + trackLen);
        }
        return res;
    }

    private String fillName(String name, int len) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        for (int i = 0; i < len - name.length(); i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    private String fillIndex(int low, int high) {
        StringBuilder sb = new StringBuilder();
        for (int i = low; i <= high; i++) {
            sb.append(" ").append(i);
        }
        return sb.toString();
    }

    private String fillTrack(StringBuilder track, int low, int high) {
        StringBuilder sb = new StringBuilder();
        for (int i = low; i <= high; i++) {
            sb.append(" ").append(track.charAt(i));
        }
        return sb.toString();
    }

    /**
     * TODO
     * 不断切割newTapes，传递给每个Tape的updateTape方法
     *
     * @param newTapes newTapes
     */
    private void updateTape(String newTapes) {
        int index = 0;
        for (Tape tape : tapes) {
            String newTape = newTapes.substring(index, index + tape.tracks.size());
            tape.updateTape(newTape);
            index += tape.tracks.size();
        }
    }

    /**
     * TODO
     * 将每个direction里的char都分配给Tape的updateHead方法
     *
     * @param direction direction
     */
    private void moveHeads(String direction) {
        for (int i = 0; i < direction.length(); i++) {
            Tape tape = tapes.get(i);
            char d = direction.charAt(i);
            tape.updateHead(d);
        }
    }
}
