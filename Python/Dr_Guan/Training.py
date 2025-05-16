import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
plt.rcParams['font.sans-serif'] = ['SimHei']  # 设置中文字体
plt.rcParams['axes.unicode_minus'] = False     # 正常显示负号
class Training:
    # 实现状态转移矩阵的构造
    def status(self):
        df = pd.read_csv('cleaned_train.csv')
        bins = [-0.05, -0.03, -0.01, 0.01, 0.03, 0.05]
        labels = ["大跌","下跌", "震荡", "上涨", "大涨"]
        # 打标签
        df['state'] = pd.cut(df['pct_change'], bins=bins, labels=labels)
        # 构造状态转移序列（前一状态 -> 当前状态）
        df['prev_state'] = df['state'].shift(1)
        # 去掉shift影响的第一行
        df_cleaner = df.dropna(subset=['state', 'prev_state'])
        # 构建状态转移计数矩阵
        transition_counts = pd.crosstab(df_cleaner['prev_state'], df_cleaner['state'])
        # 构建转移概率矩阵（按行归一化）
        global transition_matrix
        transition_matrix = transition_counts.div(transition_counts.sum(axis=1), axis=0)
        # 展示转移概率矩阵
        plt.figure(figsize=(8, 6))
        sns.heatmap(transition_matrix, annot=True, fmt=".2f", cmap="Blues", cbar=True, linewidths=0.5)
        plt.title("马尔可夫链状态转移概率矩阵", fontsize=14)
        plt.xlabel("当前状态", fontsize=12)
        plt.ylabel("前一状态", fontsize=12)

        plt.tight_layout()
        plt.show()
    def check(self):
        """
        实现收敛分析，这里采用迭代法
        """
        T = transition_matrix.values
        # 设置初始状态
        v = np.zeros(T.shape[0])
        v[0] = 1.0
        tolerance = 1e-6
        max_steps = 500
        history = [v.copy()]

        for i in range(max_steps):
            v_next = v @ T
            history.append(v_next)
            if np.allclose(v_next, v, atol=tolerance):
                print(f"收敛于第 {i + 1} 步")
                break
            v = v_next
        else:
            print("未收敛")
        # 打印稳态分布
        print("稳态分布向量：", v_next.round(4))
        history = np.array(history)
        plt.figure(figsize=(10, 6))
        for i in range(history.shape[1]):
            plt.plot(history[:, i], label=f"状态 {transition_matrix.columns[i]}")


        plt.xlabel("步数")
        plt.ylabel("状态概率")
        plt.title("马尔可夫链状态概率收敛过程")
        plt.legend()
        plt.grid()
        plt.tight_layout()
        plt.show()


if __name__ == '__main__':
    train = Training()
    train.status()
    train.check()