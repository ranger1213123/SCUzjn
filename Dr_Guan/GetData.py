import akshare as ak
import pandas as pd

class get_data:
    def get(self):
        dailyTrain_df = ak.stock_zh_index_daily_em(symbol="sh000001", start_date="20000101", end_date="20181231")
        dailyTest_df = ak.stock_zh_index_daily_em(symbol="sh000001", start_date="20190101", end_date="20191231")
        """
        训练数据
        """
        # 保存为文本&csv
        with open("train.txt", "w", encoding="utf-8") as f:
            f.write(dailyTrain_df.to_string(index=False))
        dailyTrain_df.to_csv("train.csv", index=False, encoding="utf-8")
        """
        测试数据
        """
        with open("test.txt", "w", encoding="utf-8") as f:
            f.write(dailyTest_df.to_string(index=False))
        dailyTest_df.to_csv("test.csv", index=False, encoding="utf-8")


# 实现离散化
    def cleanData(self,path):
        df = pd.read_csv(path)
        # 对应前一次的收盘数据
        df['prev_close'] = df['close'].shift(1)
        # 百分比计算
        df['pct_change'] = (df['close'] - df['prev_close'])/df['prev_close']
        # 筛选
        filtered_df = df[(-0.05 <= df['pct_change']) & (df['pct_change'] <= 0.05)]
        result_df = filtered_df[['date', 'close', 'pct_change']]
        result_df.to_csv("cleaned_" + path, index=False)

