package Bean;

import lombok.Data;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
@Data
public class Lock {
    private LockType type;
    private String dataId; //这里的dataId也就是Lock本身的编号
    private int writeId;
    private HashSet<Integer> readId = new HashSet<>();//s锁相容
    private Queue<Request> waitingList = new LinkedList<>();

    @Override
    public boolean equals(Object obj){
        return ((this.getClass().equals(obj.getClass()))&&this.dataId == ((Lock) obj).dataId);
    }
}
