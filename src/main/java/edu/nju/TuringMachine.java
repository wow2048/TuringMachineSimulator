package edu.nju;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: pkun
 * @CreateTime: 2021-05-23 16:15
 */
public class TuringMachine {

    // 状态集合
    private final Map<String, State> Q;
    // 输入符号集
    private final Set<Character> S;
    // 磁带符号集
    private final Set<Character> G;
    // 初始状态
    private String q0;
    // 终止状态集
    private final Set<String> F;
    // 空格符号
    private Character B;
    // 磁带数
    private Integer tapeNum;

    public TuringMachine(Set<String> Q, Set<Character> S, Set<Character> G, String q, Set<String> F, char B, int tapeNum, Set<TransitionFunction> Delta) {
        this.S = S;
        this.G = G;
        this.F = F;
        this.B = B;
        this.q0 = q;
        this.Q = new HashMap<>();
        for (String state : Q) {
            State temp = new State(state);
            temp.setQ(state);
            this.Q.put(state, temp);
        }
        this.tapeNum = tapeNum;
        for (TransitionFunction t : Delta) {
            this.Q.get(t.getSourceState().getQ()).addTransitionFunction(t);
        }
    }

    /**
     * TODO
     * is done in Lab1 ~
     *
     * @param tm tm
     */
    public TuringMachine(String tm) {
        Q = new HashMap<>();
        S = new HashSet<>();
        G = new HashSet<>();
        F = new HashSet<>();
        Set<String> infoSet = new HashSet<>();

        String[] lines = tm.split(System.lineSeparator());
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (StringUtils.isEmpty(line) || Utils.IsComment(line)) {
                continue;
            }
            line = line.trim();
            if (StringUtils.startsWith(line, "#")) {
                resolveTuringMachine(line, i, infoSet);
            } else {
                System.err.println("Error: " + i);
            }
        }
        checkLack(infoSet);
    }

    private void resolveTuringMachine(String line, int lineNo, Set<String> infoSet) {
        char info = line.charAt(1);
        switch (info) {
            case 'Q':
                setStatesSet(line, lineNo);
                infoSet.add("Q");
                break;
            case 'S':
                setAlphabet(line, lineNo, S, getInvalidOfS());
                infoSet.add("S");
                break;
            case 'G':
                setAlphabet(line, lineNo, G, getInvalidOfG());
                G.add('_');
                infoSet.add("G");
                break;
            case 'q':
                q0 = line.substring(6);
                infoSet.add("q0");
                break;
            case 'F':
                setFinalStateSet(line, lineNo);
                infoSet.add("F");
                break;
            case 'B':
                B = line.charAt(5);
                infoSet.add("B");
                break;
            case 'N':
                tapeNum = Integer.parseInt(line.substring(5));
                infoSet.add("N");
                break;
            case 'D':
                if (resolverTransitionFunction(line, lineNo)) {
                    infoSet.add("D");
                }
                break;
            default:
                System.err.println("Error: " + lineNo);
        }
    }

    private void setStatesSet(String line, int lineNo) {
        String[] states = Utils.SplitString(line);
        if (states == null) {
            System.err.println("Error: " + lineNo);
            return;
        }
        for (String state : states) {
            if (state.matches("[A-Za-z0-9_]+")) {
                Q.put(state, new State(state));
            } else {
                System.err.println("Error: " + lineNo);
                Q.clear();
                return;
            }
        }
    }

    private void setAlphabet(String line, int lineNo, Set<Character> alphabetSet, Set<Character> invalidChars) {
        String[] alphabets = Utils.SplitString(line);
        if (alphabets == null) {
            System.err.println("Error: " + lineNo);
            return;
        }
        for (String alphabet : alphabets) {
            Character ch = alphabet.charAt(0);
            if (!invalidChars.contains(ch)) {
                alphabetSet.add(ch);
            } else {
                System.err.println("Error: " + lineNo);
                alphabetSet.clear();
                return;
            }
        }
    }

    private void setFinalStateSet(String line, int lineNo) {
        String[] states = Utils.SplitString(line);
        if (states == null) {
            System.err.println("Error: " + lineNo);
            return;
        }
        for (String state : states) {
            if (state.matches("[A-Za-z0-9_]+")) {
                F.add(state);
            } else {
                System.err.println("Error: " + lineNo);
                F.clear();
                return;
            }
        }
    }

    private Set<Character> getInvalidOfS() {
        Set<Character> set = new HashSet<>();
        set.add(' ');
        set.add(',');
        set.add(';');
        set.add('{');
        set.add('}');
        set.add('*');
        set.add('_');
        return set;

    }

    private Set<Character> getInvalidOfG() {
        Set<Character> set = new HashSet<>();
        set.add(' ');
        set.add(',');
        set.add(';');
        set.add('{');
        set.add('}');
        set.add('*');
        return set;
    }

    private void checkLack(Set<String> infoSet) {
        if (infoSet.size() == 8) {
            return;
        }
        String[] set = new String[]{
                "Q", "S", "G", "q0", "F", "B", "N", "D"
        };
        for (String each : set) {
            if (!infoSet.contains(each)) {
                System.err.println("Error: lack" + each);
            }
        }
    }

    public State getInitState() {
        return Q.get(q0);
    }

    /**
     * TODO
     * 停止的两个条件 1. 到了终止态 2. 无路可走，halts
     *
     * @param q q
     * @param Z Z
     * @return boolean
     */
    public boolean isStop(State q, String Z) {
        if (F.contains(q.getQ())) {
            return true;
        } else return q.getDelta(Z) == null;
    }

    public boolean checkTape(Set<Character> tape) {
        return Utils.isSubSet(tape, S);
    }

    public boolean checkTapeNum(int tapeNum) {
        return this.tapeNum == tapeNum;
    }

    public Character getB() {
        return B;
    }


    /**
     * TODO
     * 检查迁移函数是否符合要求
     *
     * @param s      s
     * @param lineNo lineNo
     */
    private boolean resolverTransitionFunction(String s, int lineNo) {
        TransitionFunction transitionFunction = new TransitionFunction(s.substring(3), Q);
        String input = transitionFunction.getInput();
        String output = transitionFunction.getOutput();
        if (input.length() != output.length()) {
            System.err.println("Error: " + lineNo);
            return false;
        }
        Q.get(transitionFunction.getSourceState().getQ()).addTransitionFunction(transitionFunction);
        return true;
    }


    /**
     * TODO
     * is done in lab1 ~
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.SetToString("Q", Q.keySet())).append(System.lineSeparator());
        sb.append(Utils.SetToString("S", S)).append(System.lineSeparator());
        sb.append(Utils.SetToString("G", G)).append(System.lineSeparator());
        sb.append("#q0 = ").append(q0).append(System.lineSeparator());
        sb.append(Utils.SetToString("F", F)).append(System.lineSeparator());
        sb.append("#N = ").append(tapeNum).append(System.lineSeparator());
        sb.append("#B = ").append(B).append(System.lineSeparator());
        for (State each : Q.values()) {
            each.getDeltas().forEach(tf -> sb.append(tf.toString()).append(System.lineSeparator()));
        }
        return sb.toString();
    }

}
