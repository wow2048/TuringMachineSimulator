package edu.nju;

import java.util.ArrayList;

/**
 * @Author: pkun
 * @CreateTime: 2021-05-23 19:37
 */
public class Tape {

    ArrayList<StringBuilder> tracks;
    private final char B;
    private int head;

    public Tape(ArrayList<StringBuilder> tracks, int head, char B) {
        this.tracks = tracks;
        this.head = head;
        this.B = B;
    }

    public String snapShot() {
        StringBuilder sb = new StringBuilder();
        tracks.forEach(each -> {
            sb.append(each.charAt(head));
        });
        return sb.toString();
    }

    public void updateHead(char c) {
        if (c == 'l') {
            head--;
        } else if (c == 'r') {
            head++;
        }

        if (head == -1) {
            tracks.forEach(each -> each.insert(0, B));
            head = 0;
        }
        tracks.forEach(each -> {
            if (head == each.length()) {
                each.append(B);
            }
        });
    }

    public void updateTape(String newTape) {
        for (int i = 0; i < newTape.length(); i++) {
            String tmp = newTape.substring(i, i + 1);
            tracks.get(i).replace(head, head + 1, tmp);
        }
    }

    public int getLowIndex() {
        int index = head;
        int tmp = head;
        while (tmp >= 0) {
            if (tracks.get(0).charAt(tmp) != B) {
                index = tmp;
            }
            tmp--;
        }
        return index;
    }

    public int getHighIndex() {
        int index = head;
        int tmp = head;
        StringBuilder track = tracks.get(0);
        while (tmp < track.length()) {
            if (track.charAt(tmp) != B) {
                index = tmp;
            }
            tmp++;
        }
        return index;
    }

    public int getHead() {
        return head;
    }
}
