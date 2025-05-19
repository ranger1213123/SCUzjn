package Controller;

import Bean.*;
import java.util.*;
import Controller.WaitType;
import Services.Utility;

public class DeadLockController {
    //实现wouldWait算法，这里使用时间戳比较
        /**
         * 实现 Wound-Wait 算法判断是否允许加锁
         */
        public static WaitType woundWait(
                LockType type,
                String dataId,
                int reqTxId,
                Lock lock,
                Map<Integer, Transaction> txMap) {

            Transaction requester = txMap.get(reqTxId);
            if (requester == null) return WaitType.WAIT;
            Date reqTime = requester.getTimeStamp();

            // WRITE 锁冲突判断
            if (type == LockType.WRITE) {
                Set<Integer> readers = lock.getReadId();
                int writer = lock.getWriteId();

                for (int holderId : readers) {
                    if (holderId == reqTxId) continue;
                    Transaction holder = txMap.get(holderId);
                    if (holder != null && reqTime.before(holder.getTimeStamp())) {
                        System.out.println("  T" + reqTxId + " 抢占（终止）读锁持有者 T" + holderId);
                        Utility.abort(holderId);
                    } else {
                        Utility.wait(reqTxId, dataId, "write"); //调用 wait()
                        return WaitType.WAIT;
                    }
                }

                if (writer != 0 && writer != reqTxId) {
                    Transaction holder = txMap.get(writer);
                    if (holder != null && reqTime.before(holder.getTimeStamp())) {
                        System.out.println("  T" + reqTxId + " 抢占（终止）写锁持有者 T" + writer);
                        Utility.abort(writer);
                    } else {
                        Utility.wait(reqTxId, dataId, "write");
                        return WaitType.WAIT;
                    }
                }

                return WaitType.GRANTED;
            }

            // READ 锁冲突判断
            if (type == LockType.READ) {
                int writer = lock.getWriteId();
                if (writer != 0 && writer != reqTxId) {
                    Transaction holder = txMap.get(writer);
                    if (holder != null && reqTime.before(holder.getTimeStamp())) {
                        System.out.println("  T" + reqTxId + " 抢占（终止）写锁持有者 T" + writer);
                        Utility.abort(writer); // 调用 abort()
                    } else {
                        Utility.wait(reqTxId, dataId, "read");
                        return WaitType.WAIT;
                    }
                }

                return WaitType.GRANTED;
            }

            return WaitType.GRANTED;
        }

}
