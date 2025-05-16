from Dr_Guan.GetData import get_data
from Dr_Guan.Training import Training

if __name__ == "__main__":
    handler = get_data()
    handler.get()
    handler.cleanData("train.csv")
    handler.cleanData("test.csv")
    train = Training()
    train.status()