package Bean;

import lombok.Data;
import java.util.*;

@Data
public class Transaction {
    private Date timeStamp;
    private int id;
    private boolean isShrinking;
    private String Tx_status;
    private Set<String> readList = new HashSet<>();
    private Set<String> writeList = new HashSet<>();
    //判断是否达到缩减阶段
    public void enterShrinking(){
        this.isShrinking = true;
    }
    @Override
    public boolean equals(Object obj) {
        return ((this.getClass().equals(obj.getClass())) && this.getId() == (((Transaction) obj).getId()));
    }
}
