package Services;

import Bean.*;
import Controller.DataController;
import Controller.DeadLockController;
import Util.Operations;

import java.util.*;

public class Utility {
    private static int time = 0;//设置一个静态的时间变量

    // 获取全局递增时间戳
    public static int getNextTimeStamp() {
        return ++time;
    }

    // 终止指定事务（WoundWait()抢占策略下使用）
    public static void abort(int txId) {
        Transaction tx = DataController.getTransInfo(txId);
        if (tx == null) return;

        System.out.println("  终止事务T" + txId);
        tx.setTx_status("aborted");
        // 释放该事务所有持有的锁
        HashSet<Lock> releasedLocks = releaseAllLocks(txId);
        for (Lock lock : releasedLocks) {
            serveWaiting(lock);
        }
    }
    // 阻塞事务 txId，等待某项资源的锁
    public static void wait(int txId, String itemId, String lockTypeStr) {
        System.out.println("  堵塞事务T" + txId);
        Transaction tx = DataController.getTransInfo(txId);
        tx.setTx_status("blocked");

        Lock lock = DataController.getLockInfo(itemId);
        //若该资源还未加锁，初始化锁对象
        if (lock == null) {
            lock = new Lock();
            lock.setDataId(itemId);
            DataController.updateLockInfo(itemId, lock);
        }
        // 构造请求，加入资源的等待队列和事务自身的等待队列
        LockType type = lockTypeStr.equalsIgnoreCase("read") ? LockType.READ : LockType.WRITE;
        Request req = new Request();
        req.setTransId(txId);
        req.setType(type);
        req.setDataId(itemId);
        lock.getWaitingList().offer(req);

        Queue<Request> pending = DataController.getWaitingInfo(txId);
        if (pending == null) {
            pending = new LinkedList<>();
            DataController.updateWaitingInfo(pending, txId);
        }
        pending.offer(req);

        System.out.println("  事务 T" + txId + " 加入等待队列，等待锁：" + type + "(" + itemId + ")");
    }
    // 申请读锁
    public static void read_lock(int txId, String itemId) {
        System.out.println("  T" + txId + " -> Lock_S(" + itemId + ")");
        Lock lock = DataController.getLockInfo(itemId);
        if (lock == null) {
            lock = new Lock();
            lock.setDataId(itemId);
            DataController.updateLockInfo(itemId, lock);
        }

        if (lock.getWriteId() == 0) {
            // 无写锁，授予读锁，这里对应写锁，就是要有一个升级操作
            lock.getReadId().add(txId);
            lock.setType(LockType.READ);
            Transaction tx = DataController.getTransInfo(txId);
            if (tx == null) {
                System.out.println("  !!!事务 T" + txId + " 未初始化，请确认已执行 begin 操作");
                return;
            }
            tx.getReadList().add(itemId);
            System.out.println("  **读锁授予给事务 T" + txId);
        } else {
            //写锁冲突，触发 WoundWait()算法
            System.out.println("  !!!发生冲突：写锁已由 T" + lock.getWriteId() + " 持有");
            HashSet<Integer> conflict = new HashSet<>();
            conflict.add(lock.getWriteId());
            DeadLockController.woundWait(LockType.READ,itemId,txId,lock,DataController.transMap());
        }
    }
    // 申请写锁
    public static void write_lock(int txId, String itemId) {
        System.out.println("  T" + txId + " ->Lock_X(" + itemId + ")");
        Lock lock = DataController.getLockInfo(itemId);
        if (lock == null) {
            lock = new Lock();
            lock.setDataId(itemId);
            DataController.updateLockInfo(itemId, lock);
        }
        // 判断是否为升级操作：当前为读锁且只有本事务持有
        boolean upgradable = lock.getType() == LockType.READ && lock.getReadId().size() == 1 && lock.getReadId().contains(txId);

        if (lock.getWriteId() == 0 && (lock.getReadId().isEmpty() || upgradable)) {
            // 无冲突，直接授予写锁
            lock.setWriteId(txId);
            lock.setType(LockType.WRITE);
            lock.getReadId().remove(txId);// 升级时移除读锁
            Transaction tx = DataController.getTransInfo(txId);
            if (tx == null) {
                System.out.println("❌ 事务 T" + txId + " 未初始化，请确认已执行 begin 操作。");
                return;
            }
            tx.getWriteList().add(itemId);
            System.out.println("  **写锁授予给事务 T" + txId);
        } else {
            // 有读/写锁冲突，触发 WoundWait算法
            HashSet<Integer> conflict = new HashSet<>(lock.getReadId());
            if (lock.getWriteId() != 0) conflict.add(lock.getWriteId());
            DeadLockController.woundWait(LockType.WRITE,itemId,txId,lock,DataController.transMap());
        }
    }


    // 释放某个事务对某个数据项的锁
    public static void unlockItem(String itemId, int txId, LockType type) {
        Lock lock = DataController.getLockInfo(itemId);
        if (lock == null) return;

        if (type == LockType.READ) {
            lock.getReadId().remove(txId);
            System.out.println("  T" + txId + " 释放读锁：" + itemId);
        } else {
            if (lock.getWriteId() == txId) {
                lock.setWriteId(0);
                System.out.println("  T" + txId + " 释放写锁：" + itemId);
            }
        }
    }
    // 释放指定事务的所有锁，返回被释放的锁集合
    public static HashSet<Lock> releaseAllLocks(int txId) {
        HashSet<Lock> released = new HashSet<>();
        for (Lock lock : DataController.getAllLocks()) {
            if (lock.getWriteId() == txId || lock.getReadId().contains(txId)) {
                LockType type = lock.getWriteId() == txId ? LockType.WRITE : LockType.READ;
                //写出具体释放锁类型
                if(type == LockType.WRITE)
                    System.out.println("释放" + "Lock_X(" +lock.getDataId()+ ")");
                else
                    System.out.println("释放" + "Lock_S(" +lock.getDataId()+ ")");
                unlockItem(lock.getDataId(), txId, type);
                released.add(lock);
            }
        }
        return released;
    }
    // 唤醒在某个锁上等待的事务
    public static void serveWaiting(Lock lock) {
        while (!lock.getWaitingList().isEmpty()) {
            Request req = lock.getWaitingList().peek();
            Transaction tx = DataController.getTransInfo(req.getTransId());

            // 若事务已终止或提交，跳过
            if (tx == null ||
                    "aborted".equalsIgnoreCase(tx.getTx_status()) ||
                    "committed".equalsIgnoreCase(tx.getTx_status())) {
                lock.getWaitingList().poll(); // 移除无效请求
                continue;
            }

            tx.setTx_status("active");
            Queue<Request> ops = DataController.getWaitingInfo(tx.getId());
            if (ops != null) {
                System.out.println("    唤醒事务 T" + tx.getId());
                System.out.println("    状态置为 active，开始执行等待队列");
                while (!ops.isEmpty()) {
                    Request next = ops.poll();
                    runOperation(next, next.getDataId());
                }
            }
            break;// 一次只唤醒一个事务！！
        }
    }
    // 执行缓存中的单个操作——唤醒后使用
    public static void runOperation(Request req, String itemId) {
        int txId = req.getTransId();
        LockType type = req.getType();
        if (type == LockType.READ) {
            read_lock(txId, itemId);
        } else {
            write_lock(txId, itemId);
        }
    }
    // 解析并执行输入字符串操作指令
    public static void runOperation(String opcode) {
        if (opcode == null || opcode.length() < 2) return;


        char action = opcode.charAt(0);
        int txId = Character.getNumericValue(opcode.charAt(1));
        String itemId = (opcode.length() > 3) ? String.valueOf(opcode.charAt(3)) : null;

        Transaction tx = DataController.getTransInfo(txId);
        //终止的操作
        if (tx != null && "aborted".equalsIgnoreCase(tx.getTx_status())) {
            System.out.println("    操作 " + opcode + " 被跳过：事务 T" + txId + " 已终止");
            return;
        }
        // 若事务处于阻塞状态，将操作加入其等待队列
        if (tx != null && "blocked".equalsIgnoreCase(tx.getTx_status())) {
            Request req = new Request();
            req.setTransId(txId);
            req.setType(action == 'r' ? LockType.READ : LockType.WRITE);
            req.setDataId(itemId);// 注意！关键点
            Queue<Request> q = DataController.getWaitingInfo(txId);
            if (q == null) q = new LinkedList<>();
            q.add(req);
            DataController.updateWaitingInfo(q, txId);
            System.out.println("    操作 " + opcode + " 被缓存，等待事务 T" + txId + "被唤醒后执行");
            return;
        }
        // 正常调度操作
        switch (action) {
            case 'b': Operations.begin(txId); break;
            case 'r': if (itemId != null) Operations.read(txId, itemId); break;
            case 'w': if (itemId != null) Operations.write(txId, itemId); break;
            case 'e': Operations.end(txId); break;
            default: System.out.println("  无效操作指令：" + opcode);
        }
    }

}
