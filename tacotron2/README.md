# How to Use
## pre-setting
1. 충분한 저장 공간(>= 7Gb)
2. packages 설치 (각자 다를 수 있음 / 설치가 잘 안 되면 --ignore-installed 옵션 추가)
```
pip install jamo
pip install unidecode
pip install librosa
pip install tensorflow-gpu==1.15 (반드시 1.x 버전 이용)
```

## dataset
- Kaggle data의 1, 2만 사용
- dataset\1, dataset\2에 들어갈 json 파일 추가 (wav 파일은 직접 dataset 폴더에 audio 폴더 만들어서 옮겨야 됨)
- datafeeder_tacotron2.py의 line 349를 자신의 폴더 경로에 맞게 세팅
```
data_dirs=['D:\\ITA\tacotron2\\datasets\\1', 'D:\\ITA\tacotron2\\datasets\\2']
```

- 기타 py파일들은 그대로 사용

## <text>preprocess.py</text>
- 그대로 사용

## train_tacotron2.py
- line 245를 자신의 폴더 경로에 맞게 세팅

```
parser.add_argument('--data_paths', default='D:\\ITA\tacotron2\\data\\1, D:\\ITA\tacotron2\\data\\2)
```
 
- datasets가 아니라 data인 이유: preprocess 명령에서 out_dir이 data이기 때문에 npz 데이터가 data 폴더에 생성됨
```
python preprocess.py --num_workers 8 --name 1 --in_dir ./datasets/1 --out_dir ./data/1
```

## Step
1. kaggle에서 wav 파일 받아서 audio 폴더에 추가
2. 위에 언급한 datafeeder_tacotron2.py와 train_tacotron2.py에서 경로 수정
3. preprocess 명령
4. train_tacotron2 명령
5. logdir-tacotron2 폴더가 생성되며, 학습 시작