package Bean;

import lombok.Data;

@Data
public class Request {
    private int transId;
    private LockType type;
    private String dataId;//防止错误的请求
    @Override
    public boolean equals(Object obj) {
        return ((this.getClass().equals(obj.getClass())) && this.getTransId() == ((Request)obj).getTransId() &&
                (this.getType().equals(((Request)obj).getType())));
    }
}
